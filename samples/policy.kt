@file:Suppress("unused", "UnnecessaryVariable", "UNUSED_VARIABLE")

import org.roboquant.Roboquant
import org.roboquant.brokers.Account
import org.roboquant.common.Size
import org.roboquant.common.days
import org.roboquant.feeds.Event
import org.roboquant.orders.*
import org.roboquant.policies.*
import org.roboquant.strategies.NoSignalStrategy
import org.roboquant.strategies.Signal
import org.roboquant.ta.PriceBarSeries
import org.roboquant.ta.TaLib

// tag::basic[]
class MyPolicy : Policy {

    override fun act(signals: List<Signal>, account: Account, event: Event): List<Order> {
        val orders = mutableListOf<Order>()
        // Your code here
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


// tag::default[]
class MyFlexPolicy : FlexPolicy() {

    override fun createOrder(signal: Signal, size: Size, price: Double): Order? {
        // We don't short in this example and exit orders are already covered by the bracket order
        if (size < 0) return null

        val asset = signal.asset

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
        private var prices = PriceBarSeries(windowSize + 1)

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
        override fun createOrder(signal: Signal, size: Size, price: Double): Order? {
            val asset = signal.asset

            // We set a limit based on the ATR. The higher the ATR, the more the limit price
            // will be distanced from the current price.
            val priceBarSerie = prices.getValue(asset)
            if (! priceBarSerie.isFull()) return null

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

fun noStrategy(myAdvancedPolicy: Policy) {
    // tag::advanced[]
    val roboquant = Roboquant(NoSignalStrategy(), policy = myAdvancedPolicy)
    // end::advanced[]
}

fun signal2order(signals: List<Signal>) {
    // tag::orders[]
    val orders = mutableListOf<Order>()
    for (signal in signals) {
        val size = if (signal.rating.isPositive) 100 else -100
        val order = MarketOrder(signal.asset, size)
        orders.add(order)
    }
    // end::orders[]
}

