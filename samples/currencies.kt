@file:Suppress("unused")

import org.roboquant.brokers.ECBExchangeRates
import org.roboquant.common.*


fun amount() {
    // tag::amount[]
    val amount1 = Amount(Currency.USD, 100.0)
    val amount2 = 50.USD * 2
    println(amount1 == amount2) // true
    // end::amount[]
}

fun wallet() {
    // tag::wallet[]
    val wallet1 = Wallet(Amount(Currency.USD, 100.0))
    wallet1.deposit(Amount(Currency.GBP, 200.0))
    wallet1.withdraw(Amount(Currency.JPY, 1000))

    val wallet2 = 20.EUR - 1000.JPY + 100.GBP

    val wallet3 = 0.02.BTC + 100.USDT + 0.1.ETH
    // end::wallet[]
}


fun exchangeRates() {
    // tag::er[]
    Config.exchangeRates = ECBExchangeRates.fromWeb()
    val amountEUR = 20.EUR
    val amountUSD = amountEUR.convert(Currency.USD)
    val wallet = 20.USD + 1000.JPY
    val amountGBP = wallet.convert(Currency.GBP)
    // end::er[]
}