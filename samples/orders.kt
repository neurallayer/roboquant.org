@file:Suppress("unused", "UnnecessaryVariable", "MagicNumber", "WildcardImport")

import org.roboquant.alpaca.AlpacaBroker
import org.roboquant.common.*


fun signal2order(signals: List<Signal>, event: Event) {
    // tag::orders[]
    val orders = mutableListOf<Order>()
    for (signal in signals) {
        val size = if (signal.isBuy) Size(10) else Size(-10)
        val limit = event.getPrice(signal.asset, "CLOSE")
        if (limit != null) {
            val order = Order(signal.asset, size, limit)
            orders.add(order)
        }
    }
    // end::orders[]
}


private fun manualOrder() {
    // tag::manualOrder[]
    // Connect to the broker
    val broker = AlpacaBroker {
        publicKey = "..."
        secretKey = "..."
    }

    // create the order
    val tesla = Stock("TSLA")
    val order = Order(tesla, Size(10), 420.0)

    // place the order
    broker.placeOrders(listOf(order))

    // Get an updated versions of your broker account
    repeat(10) {
        val account = broker.sync()
        println(account)
        Thread.sleep(1_000)
    }
    // end::manualOrder[]
}




