@file:Suppress("unused", "TooManyFunctions", "SpreadOperator", "MagicNumber", "WildcardImport")

import com.crazzyghost.alphavantage.parameters.Interval
import net.jacobpeterson.alpaca.model.properties.DataAPIType
import org.roboquant.Roboquant
import org.roboquant.alpaca.AccountType
import org.roboquant.alpaca.AlpacaBroker
import org.roboquant.alpaca.AlpacaHistoricFeed
import org.roboquant.alpaca.AlpacaLiveFeed
import org.roboquant.alphavantage.AlphaVantageHistoricFeed
import org.roboquant.brokers.ECBExchangeRates
import org.roboquant.common.*
import org.roboquant.ibkr.IBKRHistoricFeed
import org.roboquant.orders.MarketOrder
import org.roboquant.polygon.PolygonHistoricFeed
import org.roboquant.polygon.PolygonLiveFeed
import java.time.Instant



fun ibkrFeed(roboquant: Roboquant) {
    // tag::ibkrfeed[]
    val feed = IBKRHistoricFeed()
    val asset = Asset("TSLA")
    feed.retrieve(asset)
    roboquant.run(feed)
    // end::ibkrfeed[]
}


fun ecb() {
    // tag::ecb[]
    // Download the latest rates from the ECB website
    // The results are cached by default
    Config.exchangeRates = ECBExchangeRates.fromWeb()

    // Create a wallet holding different currencies
    val wallet = 100.EUR + 20.USD + 1000.JPY

    // convert the wallet to GBP at today's exchange rates
    wallet.convert(Currency.GBP)

    //  convert the wallet to GBP using 5 years ago exchange rates
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
    // instantiation with hard coded configuration, rather than using dotenv property file
    val broker = AlpacaBroker {
        publicKey = "123"
        secretKey = "456"
        accountType = AccountType.PAPER
        dataType = DataAPIType.IEX
    }
    println(broker.account.summary())
    println(broker.availableAssets)

    // place a market order to buy 100 stocks Apple
    val order = MarketOrder(Asset("AAPL"), 100)
    broker.place(listOf(order))
    // end::alpacabroker[]
}
