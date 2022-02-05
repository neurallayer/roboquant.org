@file:Suppress("unused")

import org.roboquant.brokers.ECBExchangeRates
import org.roboquant.common.*
import java.time.Instant


fun currency() {
    // tag::currency[]
    Currency.USD
    Currency.BTC
    Currency.getInstance("MyCryptoCoin")
    // end::currency[]
}


fun amount() {
    // tag::amount[]
    val amount1 = Amount(Currency.getInstance("USD"), 100.0) // the most amount of code
    val amount2 = Amount(Currency.USD, 100.0) // less code
    val amount3 = 100.USD // and even less code

    val amount4 = 50.USD * 2 // simple calculation
    require(amount4 == amount3)

    println(amount1.formatValue())
    // end::amount[]
}

fun wallet() {
    // tag::wallet[]
    val wallet1 = Wallet(100.EUR, 10.USD)
    wallet1.deposit(200.GBP)
    wallet1.withdraw(1000.JPY)

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
    val amountGBP1 = wallet.convert(Currency.GBP)
    val time = Instant.now() - 2.years
    val amountGBP2 = wallet.convert(Currency.GBP, time)
    require(amountGBP1 != amountGBP2)
    // end::er[]
}