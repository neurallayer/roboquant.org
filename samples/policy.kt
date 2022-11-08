@file:Suppress("unused", "UnnecessaryVariable", "UNUSED_VARIABLE")

import org.roboquant.Roboquant
import org.roboquant.brokers.Account
import org.roboquant.brokers.sim.Execution
import org.roboquant.brokers.sim.ExecutionEngine
import org.roboquant.brokers.sim.Pricing
import org.roboquant.brokers.sim.TradeOrderHandler
import org.roboquant.common.Asset
import org.roboquant.common.Size
import org.roboquant.common.days
import org.roboquant.feeds.Event
import org.roboquant.orders.*
import org.roboquant.policies.*
import org.roboquant.strategies.NoSignalStrategy
import org.roboquant.strategies.Signal
import org.roboquant.strategies.utils.MultiAssetPriceBarSeries
import org.roboquant.ta.TaLib
import java.time.Instant

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
        .singleOrder() // remove multiple (open) orders for the same asset
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
    class SmartLimitPolicy(val atrPercentage: Double = 0.02, val window: Int = 5) : FlexPolicy() {

        // Keep track of historic prices per asset
        private var prices = MultiAssetPriceBarSeries(window + 1)

        // Use TaLib for calculation of the ATR
        private val taLib = TaLib()

        override fun act(signals: List<Signal>, account: Account, event: Event): List<Order> {
            // Update prices, so we have them available when the createOrder is invoked.
            prices.add(event)

            // Call the regular signal processing
            return super.act(signals, account, event)
        }

        /**
         * Override the default behavior of creating a simple MarkerOrder. Create limit BUY and
         * SELL orders with the actual limit based on the ATR of the underlying asset.
         */
        override fun createOrder(signal: Signal, size: Size, price: Double): Order? {
            val asset = signal.asset

            // Don't create an order if we don't have enough data yet to calculate the ATR
            if (! prices.isAvailable(asset)) return null

            // We set a limit based on the ATR. The higher the ATR, the more the limit price
            // will be distanced from the current price.
            val priceBarSeries = prices.getSeries(asset)
            val atr = taLib.atr(priceBarSeries, window)
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


fun bracketOrder(asset: Asset, price: Double) {
    // tag::bracketOrder[]
    val size = Size(10)
    val order = BracketOrder(
        MarketOrder(asset, size), // main order
        LimitOrder(asset, -size, price * 1.05), // take profit order
        StopOrder(asset, -size, price * 0.95) // stop loss order
    )
    // end::bracketOrder[]
}


fun customOrder() {

    // tag::customOrder[]
    // Simple custom order type
    class MyOrder(asset: Asset, val size: Size, val customProperty: Int, id: Int = nextId()) : Order(asset, id)

    // Define a handler for your custom order type.
    // This is only required if you want your order to be supported by the SimBroker
    class MyOrderHandler(val order: MyOrder) : TradeOrderHandler {

        override var state = OrderState(order)

        override fun execute(pricing: Pricing, time: Instant): List<Execution> {

            // Set state to accepted
            state = OrderState(order, OrderStatus.ACCEPTED, time)

            // some logic for the order type
            // ....

            // Get the price to use for the execution
            val price = pricing.marketPrice(order.size)

            // Set the state to be COMPLETED. As long as the state is not in a CLOSED state, the handler stays active.
            state = OrderState(order, OrderStatus.COMPLETED, time, time)

            // Return the executions
            return listOf(Execution(order, order.size, price))
        }

    }

    // Register the handler
    ExecutionEngine.register<MyOrder> { MyOrderHandler(it) }
    // end::customOrder[]
}
