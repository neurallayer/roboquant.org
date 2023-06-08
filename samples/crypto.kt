@file:Suppress("unused", "WildcardImport")

import info.bitrich.xchangestream.bitstamp.v2.BitstampStreamingExchange
import info.bitrich.xchangestream.core.StreamingExchange
import info.bitrich.xchangestream.core.StreamingExchangeFactory
import org.roboquant.binance.BinanceBroker
import org.roboquant.binance.BinanceHistoricFeed
import org.roboquant.binance.BinanceLiveFeed
import org.roboquant.binance.Interval
import org.roboquant.common.*
import org.roboquant.xchange.XChangeLiveFeed


fun binanceHistoricFeed() {
    // tag::binancehistoricfeed[]
    val feed = BinanceHistoricFeed()
    println(feed.availableAssets.summary())

    // Retrieve 1-minute candlesticks for two currency pairs for the last day
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


fun getBitstampExchange() {
    // tag::bitstamp[]
    val exchange = StreamingExchangeFactory.INSTANCE.createExchange(BitstampStreamingExchange::class.java)

    // Connect to the Exchange WebSocket API. Here we use a blocking wait.
    exchange.connect().blockingAwait()
    // end::bitstamp[]
}

fun xchangeLiveFeed(exchange: StreamingExchange) {
    // tag::xchangelivefeed[]
    val feed = XChangeLiveFeed(exchange)

    // subscribe to live quotes for two currency pairs
    feed.subscribeTicker("BTC_USD", "ETH_USD")
    // end::xchangelivefeed[]
}
