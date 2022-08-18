@file:Suppress("unused", "UNUSED_VARIABLE")

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
    val amount1 = Amount(Currency.getInstance("USD"), 100.0) // the most code
    val amount2 = Amount(Currency.USD, 100.0) // less code
    val amount3 = 100.USD // the least code

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
    // Load the exchange rates as published by the ECB (European Central Bank)
    Config.exchangeRates = ECBExchangeRates.fromWeb()
    val amountEUR = 20.EUR
    val amountUSD = amountEUR.convert(Currency.USD)

    val wallet = 20.USD + 1000.JPY

    // Convert wallet based on today's exchange rates
    val amountGBP1 = wallet.convert(Currency.GBP)

    // Convert wallet based on two years ago exchange rates
    val time = Instant.now() - 2.years
    val amountGBP2 = wallet.convert(Currency.GBP, time)
    require(amountGBP1 != amountGBP2)
    // end::er[]
}


fun timeFrame() {
    // tag::tf[]
    // Parse a string
    val tf1 = Timeframe.parse("2019-01-01", "2020-01-01")

    // Add to years to the timeframe
    val tf2 = tf1 + 2.years

    // Split timeframe in 31 days periods
    val tf3 = tf2.split(31.days) // result is List<Timeframe>

    // Use a predefined timeframes
    val tf4 = Timeframe.financialCrisis2008
    // end::tf[]
}