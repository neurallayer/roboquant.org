@file:Suppress("unused", "UnnecessaryVariable", "UNUSED_VARIABLE")

import org.roboquant.brokers.sim.execution.*
import org.roboquant.brokers.sim.Pricing
import org.roboquant.common.Asset
import org.roboquant.common.Size
import org.roboquant.orders.*
import java.time.Instant


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


fun customCreateOrder() {

    // tag::customOrder[]
    // Simple custom order type
    class MyOrder(
        asset: Asset,
        val size: Size,
        val customProperty: Int,
    ) : CreateOrder(asset, "")

    // Define a handler for your custom order type.
    // This is only required if you want your order to be supported by the SimBroker
    class MyOrderExecutor(override val order: MyOrder) : CreateOrderExecutor<MyOrder> {

        override var status = OrderStatus.INITIAL

        // Cannot be cancelled
        override fun cancel(time: Instant) = false

        // Cannot be updated
        override fun update(order: CreateOrder, time: Instant) = false

        // Execute the order. This will only be called when there is a price available
        // for the asset
        override fun execute(pricing: Pricing, time: Instant): List<Execution> {

            // Set state to accepted
            status = OrderStatus.ACCEPTED

            // Get the price to use for the execution
            val price = pricing.marketPrice(order.size)

            // the logic for the order type
            // ....

            // Set the state to be COMPLETED once done.
            // As long as the state is not in a closed state, the executor stays active and will be invoked when new
            // price actions become available
            status = OrderStatus.COMPLETED

            // Return the executions, or empty list if there are no executions
            return listOf(Execution(order, order.size, price))
        }

    }

    // Register the handler
    ExecutionEngine.register<MyOrder> { MyOrderExecutor(it) }
    // end::customOrder[]

}


