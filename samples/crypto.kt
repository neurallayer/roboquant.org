@file:Suppress("unused", "UNUSED_PARAMETER", "UNUSED_VARIABLE")

import org.roboquant.binance.BinanceHistoricFeed
import org.roboquant.common.Timeframe
import org.roboquant.common.days


fun binanceHistoricFeed() {
    // tag::binancehistoricfeed[]
    val feed = BinanceHistoricFeed()
    val timeframe = Timeframe.past(4.days)
    feed.retrieve("BTCUSDT", timeframe = timeframe)
    // end::binancehistoricfeed[]
}

