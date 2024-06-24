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
