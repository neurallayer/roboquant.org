@file:Suppress("unused", "UNUSED_PARAMETER", "UNUSED_VARIABLE")

import org.roboquant.brokers.Account
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


fun friendly2(account: Account) {
    // tag::friendly2[]
    TradeChart(account.trades)
    // end::friendly2[]
}
