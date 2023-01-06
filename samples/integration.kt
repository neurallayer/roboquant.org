@file:Suppress("unused", "TooManyFunctions", "SpreadOperator")

import com.crazzyghost.alphavantage.parameters.Interval
import org.roboquant.Roboquant
import org.roboquant.alpaca.AlpacaBroker
import org.roboquant.alpaca.AlpacaHistoricFeed
import org.roboquant.alpaca.AlpacaLiveFeed
import org.roboquant.alphavantage.AlphaVantageHistoricFeed
import org.roboquant.brokers.ECBExchangeRates
import org.roboquant.common.*
import org.roboquant.oanda.OANDABroker
import org.roboquant.oanda.OANDAHistoricFeed
import org.roboquant.oanda.OANDALiveFeed
import org.roboquant.orders.MarketOrder
import org.roboquant.polygon.PolygonHistoricFeed
import org.roboquant.polygon.PolygonLiveFeed
import org.roboquant.yahoo.YahooHistoricFeed
import java.time.Instant

fun yahooFeed() {
    // tag::yahoo[]
    val feed = YahooHistoricFeed()
    val tf = Timeframe.past(100.days)
    feed.retrieve("AAPL", "GOOGL", "IBM", timeframe = tf)
    // end::yahoo[]
}


fun oandaHistoricFeed() {
    // tag::oandahistoric[]
    val feed = OANDAHistoricFeed()
    val tf = Timeframe.past(2.days)
    feed.retrieve("EUR_USD", "USD_JPY", "GBP_USD", timeframe = tf)
    // end::oandahistoric[]
}


fun oandaLiveFeed(roboquant: Roboquant) {
    // tag::oandalive[]
    val feed = OANDALiveFeed()
    feed.subscribePriceBar("EUR_USD", "USD_JPY", "GBP_USD")

    val tf = Timeframe.next(120.minutes)
    roboquant.run(feed,tf)
    // end::oandalive[]
}



fun ecb() {
    // tag::ecb[]
    // Download the latest rates from the ECB website
    // The results are cached by default
    Config.exchangeRates = ECBExchangeRates.fromWeb()

    // Create a wallet with different currencies
    val wallet = 100.EUR + 20.USD + 1000.JPY

    // What is the conversion today in GBP
    wallet.convert(Currency.GBP)

    // What would the same conversion have been 5 years ago
    wallet.convert(Currency.GBP, Instant.now() - 5.years)
    // end::ecb[]
}



fun alphaHistoricFeed() {
    // tag::alphahistoric[]
    val feed = AlphaVantageHistoricFeed()

    // You can retrieve end of day prices
    val assets = listOf(
        // regular US stock
        Asset("AAPL"),

        // stock listed on a non-US exchange
        Asset("DAI.DEX", currency = Currency.EUR, exchange = Exchange.DEX)
    )
    feed.retrieveDaily(*assets.toTypedArray())

    // You can retrieve intra-day prices
    feed.retrieveIntraday(Asset("TSLA"), interval = Interval.ONE_MIN)
    // end::alphahistoric[]
}


fun polygonHistoricFeed() {
    // tag::polygonhistoric[]
    val feed = PolygonHistoricFeed()
    val tf = Timeframe.past(100.days)
    feed.retrieve("AAPL", "JPM", "TSLA", timeframe = tf)
    // end::polygonhistoric[]
}

suspend fun polygonLiveFeed(roboquant: Roboquant) {
    // tag::polygonlive[]
    val feed = PolygonLiveFeed()
    feed.subscribe("AAPL", "JPM", "TSLA")

    val tf = Timeframe.next(120.minutes)
    roboquant.run(feed,tf)
    // end::polygonlive[]
}


fun oandaBroker() {
    // tag::oandabroker[]
    val broker = OANDABroker()
    println(broker.account.summary())
    println(broker.availableAssets)

    val order = MarketOrder(Asset("AAPL"), 100)
    broker.place(listOf(order))
    // end::oandabroker[]
}

fun alpacaHistoricFeed() {
    // tag::alpacahistoric[]
    val feed = AlpacaHistoricFeed()
    val tf = Timeframe.past(100.days)

    // You can retrieve historic price-bars, quotes or trades
    feed.retrieveStockPriceBars("AAPL", "JPM", "TSLA", timeframe = tf)
    feed.retrieveStockQuotes("AAPL", "JPM", "TSLA", timeframe = tf)
    feed.retrieveStockTrades("AAPL", "JPM", "TSLA", timeframe = tf)
    // end::alpacahistoric[]
}

fun alpacaLiveFeed(roboquant: Roboquant) {
    // tag::alpacalive[]
    val feed = AlpacaLiveFeed()
    feed.subscribeStocks("AAPL", "IBM")

    val tf = Timeframe.next(120.minutes)
    roboquant.run(feed,tf)
    // end::alpacalive[]
}

fun alpacaBroker() {
    // tag::alpacabroker[]
    val broker = AlpacaBroker()
    println(broker.account.summary())
    println(broker.availableAssets)

    val order = MarketOrder(Asset("AAPL"), 100)
    broker.place(listOf(order))
    // end::alpacabroker[]
}
