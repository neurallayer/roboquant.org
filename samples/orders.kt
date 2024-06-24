@file:Suppress("unused", "UnnecessaryVariable", "UNUSED_VARIABLE", "MagicNumber", "WildcardImport")

import org.roboquant.alpaca.AlpacaBroker
import org.roboquant.brokers.Broker
import org.roboquant.brokers.sim.execution.Execution
import org.roboquant.brokers.sim.execution.OrderExecutor
import org.roboquant.brokers.sim.execution.OrderExecutorFactory
import org.roboquant.common.Asset
import org.roboquant.common.Size
import org.roboquant.feeds.PriceItem
import org.roboquant.orders.*
import org.roboquant.strategies.Signal
import java.time.Instant

fun signal2order(signals: List<Signal>) {
    // tag::orders[]
    val orders = mutableListOf<Order>()
    for (signal in signals) {
        val size = if (signal.isBuy) 100 else -100
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


private fun manualOrder() {
    // tag::manualOrder[]
    // Connect to the broker
    val broker = AlpacaBroker {
        publicKey = "..."
        secretKey = "..."
    }

    // create the order
    val tesla = Asset("TSLA")
    val order = MarketOrder(tesla, 10)

    // place the order
    broker.place(listOf(order))

    // Get an updated versions of your broker account
    repeat(10) {
        val account = broker.sync()
        println(account)
        Thread.sleep(1_000)
    }
    // end::manualOrder[]
}


fun accountOrder(broker: Broker) {
    // tag::accountOrder[]
    val asset = Asset("AAPL")
    val order = MarketOrder(asset, 100)
    broker.place(listOf(order))

    // You might want to sleep a bit to give broker time to process your order
    Thread.sleep(1_000)

    // Get the latest state
    val account = broker.sync()

    val openOrder = account.openOrders.last()
    assert(openOrder is MarketOrder)
    assert(order == openOrder)

    with(openOrder) {
        println("status=$status openedAt=$openedAt")
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
    ) : Order(asset, tag)

    // Define an executor for your custom order type.
    class MyOrderExecutor(override val order: MyOrder) : OrderExecutor {

        override var status = OrderStatus.INITIAL

        // Our order type can be cancelled, but doesn't support other modify order types
        override fun cancel(time: Instant): Boolean {
            status = OrderStatus.CANCELLED
            return true
        }

        // Execute the order. This will only be called when there is a price available
        // for the asset
        override fun execute(item: PriceItem, time: Instant): List<Execution> {

            // Set status to ACCEPTED
            status = OrderStatus.ACCEPTED

            // Get the price to use for the execution
            val price = item.getPrice("OPENa")

            // the logic for the custom order type
            // .....

            // Set the state to be COMPLETED once done.
            // As long as the state is not in a closed state, the executor stays active and will be invoked when new
            // price actions become available for the underlying asset
            status = OrderStatus.COMPLETED

            // Return the executions, or empty list if there are no executions
            return listOf(Execution(order, order.size, price))
        }

    }

    // Register the executor so the SimBroker knows how to execute this new order type
    OrderExecutorFactory.register<MyOrder> { MyOrderExecutor(it) }
    // end::customOrder[]

}


