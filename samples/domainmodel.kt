@file:Suppress("unused", "UNUSED_VARIABLE")

import org.roboquant.Roboquant
import org.roboquant.brokers.Account
import org.roboquant.brokers.ECBExchangeRates
import org.roboquant.common.*
import org.roboquant.feeds.Feed
import org.roboquant.orders.MarketOrder
import java.time.Instant


private fun currency() {
    // tag::currency[]
    Currency.USD
    Currency.BTC
    Currency.getInstance("MyCryptoCoin")
    // end::currency[]
}



private fun size(bitcoin: Asset, account: Account) {
    // tag::size[]
    val size = Size(100) + Size(200)
    val size2 = Size("100.12")

    val order = MarketOrder(bitcoin, Size("0.01"))
    val positionSize = account.positions.first().size
    // end::size[]
}

private fun amount() {
    // tag::amount[]
    val amount1 = Amount(Currency.getInstance("USD"), 100.0) // the most code
    val amount2 = Amount(Currency.USD, 100.0) // less code
    val amount3 = 100.USD // the least code

    val amount4 = 50.USD * 2 // simple calculation
    require(amount4 == amount3)

    // display using the common number of decimals for that currency
    println(amount1.formatValue())
    // end::amount[]
}

private fun wallet() {
    // tag::wallet[]
    val wallet1 = Wallet(100.EUR, 10.USD)
    wallet1.deposit(200.GBP)
    wallet1.withdraw(1000.JPY)

    val wallet2 = 20.EUR - 1000.JPY + 100.GBP
    val wallet3 = 0.02.BTC + 100.USDT + 0.1.ETH
    // end::wallet[]
}


private fun exchangeRates() {
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


private fun timeFrame() {
    // tag::tf[]
    // Parse a string
    val tf1 = Timeframe.parse("2019-01-01", "2020-01-01")

    // Add to years to the timeframe
    val tf2 = tf1 + 2.years

    // Split timeframe in 31 days periods
    val tf3 = tf2.split(31.days) // result is List<Timeframe>

    // Create 100 random timeframes, each of 3 months duration
    val tf4 = tf2.sample(3.months, 100) // result is List<Timeframe>
    // end::tf[]
}


private fun tradingPeriod(roboquant: Roboquant, feed: Feed) {
    // tag::tradingperiod[]
    val now = Instant.now()
    val tomorrow = now + 1.days
    val nextWeek = now + 1.weeks

    // Create a back-test 6 months before and after the Black Monday
    val timeframe = Timeframe.blackMonday1987
    val backTestPeriod = timeframe.extend(6.months)

    // Run a strategy for the 8 hours
    roboquant.run(feed, Timeframe.next(8.hours))
    // end::tradingperiod[]
}



private fun predefined(roboquant: Roboquant, feed: Feed) {
    // tag::predefined[]
    val timeFrames = listOf(
        Timeframe.blackMonday1987,
        Timeframe.financialCrisis2008,
        Timeframe.coronaCrash2020,
        Timeframe.flashCrash2010,
        Timeframe.tenYearBullMarket2009,
    )

    timeFrames.forEach {
        roboquant.run(feed, it)
    }
    // end::predefined[]
}