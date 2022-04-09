@file:Suppress("unused", "UnnecessaryVariable", "UNUSED_PARAMETER", "UNUSED_VARIABLE")

import org.roboquant.Roboquant
import org.roboquant.brokers.Account
import org.roboquant.brokers.sim.Execution
import org.roboquant.brokers.sim.ExecutionEngine
import org.roboquant.brokers.sim.Pricing
import org.roboquant.brokers.sim.TradeOrderHandler
import org.roboquant.common.Asset
import org.roboquant.feeds.Event
import org.roboquant.orders.*
import org.roboquant.policies.DefaultPolicy
import org.roboquant.policies.Policy
import org.roboquant.strategies.NoSignalStrategy
import org.roboquant.strategies.Signal
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

    override fun createOrder(signal: Signal, qty: Double, price: Double): Order? {
        // We don't short and  all other sell/exit orders are covered by the bracket order
        if (qty < 0) return null

        val asset = signal.asset
        return BracketOrder(
            MarketOrder(asset, qty),
            TrailOrder(asset, -qty, percentage/2.0),
            StopOrder(asset, -qty, price* (1 - percentage))
        )
    }
}
// end::default[]


// tag::naive[]
class MyNaivePolicy : Policy {

    override fun act(signals: List<Signal>, account: Account, event: Event): List<Order> {
        val orders = mutableListOf<Order>()
        for (signal in signals) {
            val qty = if (signal.rating.isPositive) 100.0 else -100.0
            val order = MarketOrder(signal.asset, qty)
            orders.add(order)
        }
        return orders
    }
}
// end::naive[]

// tag::naive2[]
class MyNaivePolicy2 : Policy {

    override fun act(signals: List<Signal>, account: Account, event: Event): List<Order> {
        return signals.map {
            val qty = if (it.rating.isPositive) 100.0 else -100.0
            MarketOrder(it.asset, qty)
        }
    }
}
// end::naive2[]

fun noStrategy(myAdvancedPolicy:Policy) {
    // tag::advanced[]
    val roboquant = Roboquant(NoSignalStrategy(), policy = myAdvancedPolicy)
    // end::advanced[]
}

fun orders(signals: List<Signal>) {
    // tag::orders[]
    val orders = mutableListOf<Order>()
    for (signal in signals) {
        val qty = if (signal.rating.isPositive) 100.0 else -100.0
        val order = MarketOrder(signal.asset, qty)
        orders.add(order)
    }
    // end::orders[]
}


fun bracketOrder(asset: Asset, price: Double) {
    // tag::bracketOrder[]
    val order = BracketOrder(
        MarketOrder(asset, 10.0), // main order
        LimitOrder(asset, -10.0, price * 1.05), // take profit order
        StopOrder(asset, -10.0, price * 0.95) // stop loss order
    )
    // end::bracketOrder[]
}



fun customOrder() {

    // tag::customOrder[]

    // Simple custom order type
    class MyOrder(asset: Asset, val quantity: Double, val customProperty: Int, id:Int = nextId()) : Order(asset, id)

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
            val price = pricing.marketPrice(order.quantity)

            // Set the state to COMPLETED. As long as the state is not closed, this handler stay active.
            state = OrderState(order, OrderStatus.COMPLETED, time, time)

            // Return the executions
            return listOf(Execution(order, order.quantity, price))
        }

    }

    // Register the handler
    ExecutionEngine.register<MyOrder> { MyOrderHandler(it) }

    // end::customOrder[]
}