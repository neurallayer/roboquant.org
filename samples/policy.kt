@file:Suppress("unused", "UnnecessaryVariable", "UNUSED_VARIABLE")

import org.roboquant.Roboquant
import org.roboquant.brokers.Account
import org.roboquant.brokers.sim.Execution
import org.roboquant.brokers.sim.ExecutionEngine
import org.roboquant.brokers.sim.Pricing
import org.roboquant.brokers.sim.TradeOrderHandler
import org.roboquant.common.Asset
import org.roboquant.common.Size
import org.roboquant.feeds.Event
import org.roboquant.metrics.MetricResults
import org.roboquant.orders.*
import org.roboquant.policies.DefaultPolicy
import org.roboquant.policies.Policy
import org.roboquant.strategies.NoSignalStrategy
import org.roboquant.strategies.Signal
import org.roboquant.ta.TaLibMetric
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


// tag::default[]
class MyDefaultPolicy(private val percentage:Double = 0.05) : DefaultPolicy() {

    override fun createOrder(signal: Signal, size: Size, price: Double): Order? {
        // We don't short and  all other sell/exit orders are covered by the bracket order
        if (size < 0) return null

        val asset = signal.asset
        return BracketOrder(
            MarketOrder(asset, size),
            TrailOrder(asset, -size, percentage/2.0),
            StopOrder(asset, -size, price * (1 - percentage))
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

fun customPolicy() {
    // tag::custom3[]
    /**
     * Custom Policy that extends the DefaultPolicy and captures the ATR (Average True Range) using the TaLibMetric. It
     * then uses the ATR to set the limit amount of a LimitOrder.
     */
    class SmartLimitPolicy(private val atrPercentage: Double = 0.02, private val atrPeriod: Int) : DefaultPolicy() {

        // use TaLibMetric to calculate the ATR values
        private val atr = TaLibMetric("atr", atrPeriod + 1) { atr(it, atrPeriod) }
        private var atrMetrics: MetricResults = emptyMap()

        override fun act(signals: List<Signal>, account: Account, event: Event): List<Order> {
            // Update the metrics and store the results, so we have them available when the
            // createOrder is invoked.
            atrMetrics = atr.calculate(account, event)

            // Call the regular DefaultPolicy processing
            return super.act(signals, account, event)
        }

        /**
         * Override the default behavior of creating a simple MarkerOrder. Create limit BUY and SELL orders with the
         * actual limit based on the ATR of the underlying asset.
         */
        override fun createOrder(signal: Signal, size: Size, price: Double): Order? {
            val metricName = "atr.${signal.asset.symbol.lowercase()}"
            val value = atrMetrics[metricName]
            return if (value != null) {
                val direction = if (size > 0) 1 else -1
                val limit = price - direction * value * atrPercentage
                LimitOrder(signal.asset, size, limit)
            } else {
                null
            }
        }

        override fun reset() {
            atr.reset()
            super.reset()
        }
    }
    // end::custom3[]
}

fun noStrategy(myAdvancedPolicy:Policy) {
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
    class MyOrder(asset: Asset, val size: Size, val customProperty: Int, id:Int = nextId()) : Order(asset, id)

    // Define a handler for your custom order type.
    // This is only required if you want your order to be supported by the SimBroker
    class MyOrderHandler(val order: MyOrder) : TradeOrderHandler {

        override var state = OrderState(order)

        override fun execute(pricing: Pricing, time: Instant): List<Execution> {

            // Set state to accepted
            state = OrderState(order, OrderStatus.ACCEPTED, time)

            // some logic for the order type
            // ....

            // Calculate the price to use
            val price = pricing.marketPrice(order.size)

            // Set the state to be COMPLETED. As long as the state is not in a Closed state, this handler stays active.
            state = OrderState(order, OrderStatus.COMPLETED, time, time)

            // Return the executions
            return listOf(Execution(order, order.size, price))
        }

    }

    // Register the handler
    ExecutionEngine.register<MyOrder> { MyOrderHandler(it) }

    // end::customOrder[]
}