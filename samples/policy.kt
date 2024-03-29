@file:Suppress("unused", "UnnecessaryVariable", "UNUSED_VARIABLE", "WildcardImport", "MagicNumber", "ReturnCount")

import org.roboquant.Roboquant
import org.roboquant.brokers.Account
import org.roboquant.brokers.Position
import org.roboquant.brokers.diff
import org.roboquant.common.*
import org.roboquant.feeds.AssetFeed
import org.roboquant.feeds.Event
import org.roboquant.feeds.PriceAction
import org.roboquant.orders.*
import org.roboquant.policies.*
import org.roboquant.strategies.NoSignalStrategy
import org.roboquant.strategies.Signal
import org.roboquant.ta.AssetPriceBarSeries
import org.roboquant.ta.TaLib
import java.time.Instant

// tag::basic[]
class MyPolicy : Policy {

    override fun act(signals: List<Signal>, account: Account, event: Event): List<Order> {
        val orders = mutableListOf<Order>()
        // Your code goes here
        return orders
    }
}
// end::basic[]


private fun chainedPolicies() {
    // tag::chaining[]
    val policy = MyPolicy()
        .resolve(SignalResolution.NO_CONFLICTS) // remove all conflicting signals
        .circuitBreaker(10, 1.days) // stop orders if there are too many created
    // end::chaining[]
}


fun constr() {
    // tag::constructor[]
    val policy = FlexPolicy {
        orderPercentage = 0.01
        shorting = true
        priceType = "OPEN"
        fractions = 4
        oneOrderOnly = true
        safetyMargin = 0.1
        minPrice = 1000.USD
    }
    // end::constructor[]
}


// tag::default[]
class MyFlexPolicy : FlexPolicy() {

    override fun createOrder(signal: Signal, size: Size, priceAction: PriceAction): Order? {
        // We don't short in this example, and exit orders are already covered by the bracket order
        if (size < 0) return null

        val asset = signal.asset
        val price = priceAction.getPrice(config.priceType)

        // Create a bracket order with an additional take-profit and stop-loss defined
        return BracketOrder(
            LimitOrder(asset, size, price), // limit order at current price for entry
            TrailOrder(asset, -size, 0.05), // 5% trail order for take profit
            StopOrder(asset, -size, price * 0.98) // stop loss order 2% under current price
        )
    }
}
// end::default[]

fun naivePolicy1() {
    // tag::naive[]
    class MyNaivePolicy : Policy {

        override fun act(signals: List<Signal>, account: Account, event: Event): List<Order> {
            val orders = mutableListOf<Order>()
            for (signal in signals) {
                val size = if (signal.rating.isPositive) 100 else -100
                val order = MarketOrder(signal.asset, size)
                orders.add(order)
            }
            return orders
        }
    }
    // end::naive[]
}

fun naivePolicy2() {
    // tag::naive2[]
    class MyNaivePolicy : Policy {

        override fun act(signals: List<Signal>, account: Account, event: Event): List<Order> {
            return signals.map {
                val size = if (it.rating.isPositive) 100 else -100
                MarketOrder(it.asset, size)
            }
        }
    }
    // end::naive2[]
}


fun customPolicy2() {
    // tag::custom3[]
    /**
     * Custom Policy that extends the FlexPolicy and uses the ATR (Average True Range)
     * to set the limit amount of a LimitOrder.
     */
    class SmartLimitPolicy(val atrPercentage: Double = 0.02, val windowSize: Int = 5) : FlexPolicy() {

        // Keep track of historic prices per asset
        private var prices = AssetPriceBarSeries(windowSize + 1)

        // Use TaLib for calculation of the ATR
        private val taLib = TaLib()

        override fun act(signals: List<Signal>, account: Account, event: Event): List<Order> {
            // Update prices, so we have them available when the createOrder is invoked.
            prices.addAll(event)

            // Call the regular signal processing
            return super.act(signals, account, event)
        }

        /**
         * Override the default behavior of creating a simple MarkerOrder. Create limit BUY and
         * SELL orders with the actual limit based on the ATR of the underlying asset.
         */
        override fun createOrder(signal: Signal, size: Size, priceAction: PriceAction): Order? {
            val asset = signal.asset
            val price = priceAction.getPrice(config.priceType)

            // We set a limit based on the ATR. The higher the ATR, the more the limit price
            // will be distanced from the current price.
            val priceBarSerie = prices.getValue(asset)
            if (!priceBarSerie.isFull()) return null

            val atr = taLib.atr(priceBarSerie, windowSize)
            val limit = price - size.sign * atr * atrPercentage

            return LimitOrder(asset, size, limit)
        }

        override fun reset() {
            prices.clear()
            super.reset()
        }
    }
    // end::custom3[]
}

fun customSingleAssetPolicy(feed: AssetFeed) {
    // tag::singleAsset[]
    class SingleAssetPolicy(private val asset: Asset) : Policy {

        private val history = PriceSeries(10)

        /**
         * Open a position
         */
        private fun openPosition(account: Account, priceAction: PriceAction): Order? {
            val available = account.buyingPower.value * 0.9 // Let's not use 100%
            val price = priceAction.getPrice()
            val size = asset.contractSize(available, price, fractions = 0)

            return if (history.add(price)) {
                // Your logic to decide to open a position
                TODO()
            } else {
                null
            }
        }

        /**
         * Close a position
         */
        private fun closePosition(position: Position, priceAction: PriceAction): Order? {
            val price = priceAction.getPrice()
            val profit = price / position.avgPrice - 1.0

            return when {
                profit >= 0.01 -> MarketOrder(position.asset, -position.size) // take profit
                profit <= 0.01 -> MarketOrder(position.asset, -position.size) // stop loss
                else -> null
            }
        }

        override fun act(signals: List<Signal>, account: Account, event: Event): List<Order> {
            // If there is already an open order, don't do anything
            if (account.openOrders.assets.contains(asset)) return emptyList()

            // If there is no price, don't do anything
            val priceAction = event.prices[asset] ?: return emptyList()

            val position = account.positions.firstOrNull { it.asset == asset }
            val order = if (position != null) {
                closePosition(position, priceAction)
            } else {
                openPosition(account, priceAction)
            }

            return if (order == null) emptyList() else listOf(order)
        }

        override fun reset() {
            history.clear()
        }

    }

    val roboquant = Roboquant(
        NoSignalStrategy(), // will always return an empty list of signals
        policy = SingleAssetPolicy(feed.assets.first())
    )

    roboquant.run(feed)
    // end::singleAsset[]
}

fun noStrategy() {
    // tag::advanced[]
    class MyPolicy : Policy {

        private var rebalanceDate = Instant.MIN
        private val holdingPeriod = 20.days

        /**
         * Based on some logic determine the target portfolio
         */
        fun getTargetPortfolio(): List<Position> {
            TODO("your logic goes here")
        }

        override fun act(signals: List<Signal>, account: Account, event: Event): List<Order> {
            if (event.time < rebalanceDate) return emptyList()

            rebalanceDate = event.time + holdingPeriod
            val targetPortfolio = getTargetPortfolio()

            // Get the difference of target portfolio and the current portfolio
            val diff = account.positions.diff(targetPortfolio)

            // Transform the difference into MarketOrders
            return diff.map { MarketOrder(it.key, it.value) }
        }

        override fun reset() {
            rebalanceDate = Instant.MIN
        }
    }

    val roboquant = Roboquant(
        NoSignalStrategy(), // will always return an empty list of signals
        policy = MyPolicy()
    )
    // end::advanced[]
}
