@file:Suppress("unused")

import org.roboquant.alpaca.AlpacaBroker
import org.roboquant.alpaca.AlpacaHistoricFeed
import org.roboquant.alpaca.AlpacaLiveFeed
import org.roboquant.common.Asset
import org.roboquant.common.Timeframe
import org.roboquant.common.days
import org.roboquant.common.minutes
import org.roboquant.iex.IEXHistoricFeed
import org.roboquant.iex.IEXLiveFeed
import org.roboquant.iex.Interval
import org.roboquant.iex.Range
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
    feed.retrieveCandles("EUR_USD", "USD_JPY", "GBP_USD", timeframe = tf)
    // end::oandahistoric[]
}


fun oandaLiveFeed() {
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
    broker.account.summary().log()
    // end::oandabroker[]
}



fun alpacaHistoricFeed() {
    // tag::alpacahistoric[]
    val feed = AlpacaHistoricFeed()
    val tf = Timeframe.past(100.days)
    feed.retrieveBars("AAPL", "IBM", timeframe = tf)
    // end::alpacahistoric[]
}



fun iexHistoricFeed() {
    // tag::iexhistoric[]
    val feed = IEXHistoricFeed()
    feed.retrievePriceBar("AAPL", "IBM", range = Range.FIVE_YEARS)
    // end::iexhistoric[]
}


fun iexLiveFeed() {
    // tag::iexlive[]
    val feed = IEXLiveFeed()
    feed.subscribeQuotes(Asset("IBM"), Asset("AAPL"), interval = Interval.ONE_MINUTE)
    // end::iexlive[]
}


fun alpacaLiveFeed() {
    // tag::alpacalive[]
    val feed = AlpacaLiveFeed()
    feed.subscribe("AAPL", "IBM")

    val tf = Timeframe.next(120.minutes)
    roboquant.run(feed,tf)
    // end::alpacalive[]
}


fun alpacaBroker() {
    // tag::alpacabroker[]
    val broker = AlpacaBroker()
    broker.account.summary().log()
    // end::alpacabroker[]
}
