@file:Suppress("unused", "UNUSED_VARIABLE", "MagicNumber", "WildcardImport")

import org.roboquant.Roboquant
import org.roboquant.brokers.Account
import org.roboquant.brokers.ECBExchangeRates
import org.roboquant.common.*
import org.roboquant.feeds.Feed
import org.roboquant.feeds.csv.CSVFeed
import org.roboquant.orders.MarketOrder
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime


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



private fun assets() {
    // tag::assets[]
    val start = Instant.parse("2023-01-04-T10:00:00Z")
    val end = Instant.parse("2023-01-04-T16:00:00Z")
    val now = Instant.now()

    val exchange = Exchange.SSX // Sydney exchange
    val asset = Asset("XYZ", exchange = exchange)
    exchange.sameDay(start, end) // False

    // LocalDate features
    val localDate = exchange.getLocalDate(now)
    val dayOfWeek = localDate.dayOfWeek
    val dayOfMonth = localDate.dayOfMonth

    // using the configured TradingCalendar
    exchange.isTrading(now)
    exchange.getOpeningTime(localDate)
    exchange.getClosingTime(localDate)
    // end::assets[]
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

    // Predefined timeframes
    val tf5 = Timeframe.blackMonday1987
    // end::tf[]
}

private fun timeline(roboquant: Roboquant) {
    // tag::timeline[]
    val feed = CSVFeed("somepath")

    // Split the timeline in chunks of 250
    // and run back test over them
    feed.timeline.split(250).forEach {
        roboquant.run(feed, it)
    }
    // end::timeline[]
}

private fun tradingPeriod(roboquant: Roboquant, feed: Feed) {
    // tag::tradingperiod[]
    // Use the TimePeriod constructor directly
    val period = TimeSpan(years = 1, months = 6, seconds = 30)

    // Use extension methods
    val oneDay = 1.days
    val oneHour = 1.hours
    val myTimePeriod = 1.years + 6.months
    val myHighFrequencyPeriod = 1.seconds + 500.millis

    // Calculate using TimePeriods (using UTC if required)
    val now = Instant.now()
    val tomorrow = now + 1.days
    val yesterday = now - 1.days
    val nextHourAndHalf = now + 1.hours + 30.minutes
    val nextYearAndHalf = now + myTimePeriod

    // Using an explicit timezone
    val zone = ZoneId.of("Europe/Amsterdam")
    val localTomorrow = now.plus(1.months, zone)

    // Using timezone aware ZonedDateTime
    val localNow = ZonedDateTime.now() // timezone aware datetime
    val localTomorrow2 = localNow + 1.days

    // Run a forward test for the next 8 hours
    val timeframe = Timeframe.next(8.hours)
    roboquant.run(feed, timeframe)

    // Extend a timeframe
    val timeframe2 = Timeframe.blackMonday1987.extend(before = 2.months, after = 1.months)
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

    // Run a back test against these periods
    timeFrames.forEach {
        roboquant.run(feed, it.extend(5.days))
    }
    // end::predefined[]
}
