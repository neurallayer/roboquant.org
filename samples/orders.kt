@file:Suppress("unused", "UnnecessaryVariable", "UNUSED_VARIABLE")

import org.roboquant.brokers.Broker
import org.roboquant.brokers.sim.Pricing
import org.roboquant.brokers.sim.execution.CreateOrderExecutor
import org.roboquant.brokers.sim.execution.Execution
import org.roboquant.brokers.sim.execution.ExecutionEngine
import org.roboquant.common.Asset
import org.roboquant.common.Size
import org.roboquant.orders.*
import org.roboquant.strategies.Signal
import java.time.Instant

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
        MarketOrder(asset, size), // entry order
        LimitOrder(asset, -size, price * 1.05), // take profit exit order
        StopOrder(asset, -size, price * 0.95) // stop loss exit order
    )
    // end::bracketOrder[]
}



fun accountOrder(broker: Broker) {
    // tag::accountOrder[]
    val asset = Asset("AAPL")
    val order = MarketOrder(asset, 100)
    val account = broker.place(listOf(order))

    val openOrder = account.openOrders.last()
    assert(openOrder.order is MarketOrder)
    assert(order == openOrder.order)
    
    with (openOrder) {
        println("status=$status openedAt=$openedAt closedAt=$closedAt")
    }
    // end::accountOrder[]
}

fun customCreateOrder() {

    // tag::customOrder[]
    // Simple custom order type
    class MyOrder(
        asset: Asset,
        val size: Size,
        val customProperty: Int,
        tag: String = ""
    ) : CreateOrder(asset, tag)

    // Define a handler for your custom order type.
    // This is only required if you want your order to be supported by the SimBroker
    class MyOrderExecutor(override val order: MyOrder) : CreateOrderExecutor<MyOrder> {

        override var status = OrderStatus.INITIAL

        // Our order type can be cancelled
        override fun cancel(time: Instant) : Boolean {
            return if (status.open) {
                status = OrderStatus.CANCELLED
                true
            } else {
                false
            }
        }

        // Our order type cannot be updated
        override fun update(order: CreateOrder, time: Instant) = false

        // Execute the order. This will only be called when there is a price available
        // for the asset
        override fun execute(pricing: Pricing, time: Instant): List<Execution> {

            // Set state to accepted
            status = OrderStatus.ACCEPTED

            // Get the price to use for the execution
            val price = pricing.marketPrice(order.size)

            // the logic for the custom order type
            // ....

            // Set the state to be COMPLETED once done.
            // As long as the state is not in a closed state, the executor stays active and will be invoked when new
            // price actions become available for the underlying asset
            status = OrderStatus.COMPLETED

            // Return the executions, or empty list if there are no executions
            return listOf(Execution(order, order.size, price))
        }

    }

    // Register the handler so the SimBroker knows how to execute this new order type
    ExecutionEngine.register<MyOrder> { MyOrderExecutor(it) }
    // end::customOrder[]

}


