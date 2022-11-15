@file:Suppress("unused")

import org.roboquant.Roboquant
import org.roboquant.alpaca.AlpacaBroker
import org.roboquant.alpaca.AlpacaHistoricFeed
import org.roboquant.alpaca.AlpacaLiveFeed
import org.roboquant.common.Timeframe
import org.roboquant.common.days
import org.roboquant.common.minutes
import org.roboquant.iexcloud.IEXCloudHistoricFeed
import org.roboquant.iexcloud.IEXCloudLiveFeed
import org.roboquant.iexcloud.Interval
import org.roboquant.iexcloud.Range
import org.roboquant.oanda.OANDABroker
import org.roboquant.oanda.OANDAHistoricFeed
import org.roboquant.oanda.OANDALiveFeed
import org.roboquant.yahoo.YahooHistoricFeed


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


fun oandaBroker() {
    // tag::oandabroker[]
    val broker = OANDABroker()
    println(broker.account.summary())
    // end::oandabroker[]
}



fun alpacaHistoricFeed() {
    // tag::alpacahistoric[]
    val feed = AlpacaHistoricFeed()
    val tf = Timeframe.past(100.days)
    feed.retrieveStockPriceBars("AAPL", "IBM", timeframe = tf)
    // end::alpacahistoric[]
}



fun iexHistoricFeed() {
    // tag::iexhistoric[]
    val feed = IEXCloudHistoricFeed()
    feed.retrieve("AAPL", "IBM", range = Range.FIVE_YEARS)
    // end::iexhistoric[]
}


fun iexLiveFeed() {
    // tag::iexlive[]
    val feed = IEXCloudLiveFeed()
    feed.subscribeQuotes("IBM", "AAPL", interval = Interval.ONE_MINUTE)
    // end::iexlive[]
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
    // end::alpacabroker[]
}
