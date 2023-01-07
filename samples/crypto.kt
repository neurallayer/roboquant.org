@file:Suppress("unused")

import org.roboquant.binance.BinanceBroker
import org.roboquant.binance.BinanceHistoricFeed
import org.roboquant.binance.BinanceLiveFeed
import org.roboquant.binance.Interval
import org.roboquant.common.Timeframe
import org.roboquant.common.days
import org.roboquant.common.summary


fun binanceHistoricFeed() {
    // tag::binancehistoricfeed[]
    val feed = BinanceHistoricFeed()
    println(feed.availableAssets.summary())

    // Retrieve 1 minute candlesticks for two currency pair for the last day
    val timeframe = Timeframe.past(1.days)
    feed.retrieve("BTCBUSD", "ETHBUSD", timeframe = timeframe, interval = Interval.ONE_MINUTE)
    // end::binancehistoricfeed[]
}


fun binanceLiveFeed() {
    // tag::binancelivefeed[]
    val feed = BinanceLiveFeed()
    println(feed.availableAssets.summary())

    // subscribe to live quotes for two currency pairs
    feed.subscribePriceQuote("BTCBUSD", "ETHBUSD")
    // end::binancelivefeed[]
}


fun binanceBroker() {
    // tag::binancebroker[]
    val broker = BinanceBroker {
        publicKey = "123"
        secretKey = "456"
    }
    println(broker.account.fullSummary())
    // end::binancebroker[]
}