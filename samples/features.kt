@file:Suppress("unused")

import org.roboquant.brokers.Broker
import org.roboquant.common.Currency.Companion.USD
import org.roboquant.common.EUR
import org.roboquant.common.JPY
import org.roboquant.common.USD
import org.roboquant.jupyter.TradeChart


fun friendly1() {
    // tag::friendly1[]
    val wallet = 100.EUR + 20.USD + 1_000.JPY
    wallet.convert(USD)
    // end::friendly1[]
}


fun friendly2(broker: Broker) {
    // tag::friendly2[]
    val account = broker.account
    TradeChart(account.trades)
    // end::friendly2[]
}
