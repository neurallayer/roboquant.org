import org.roboquant.common.TimeFrame
import org.roboquant.common.days
import org.roboquant.common.minutes
import org.roboquant.oanda.OANDABroker
import org.roboquant.oanda.OANDAHistoricFeed
import org.roboquant.oanda.OANDALiveFeed
import org.roboquant.yahoo.YahooHistoricFeed


fun yahooFeed() {
    // tag::yahoo[]
    val feed = YahooHistoricFeed()
    val tf = TimeFrame.past(100.days)
    feed.retrieve("AAPL", "GOOGL", "IBM", timeFrame = tf)
    // end::yahoo[]
}


fun oandaHistoricFeed() {
    // tag::oandahistoric[]
    val feed = OANDAHistoricFeed()
    val tf = TimeFrame.past(2.days)
    feed.retrieveCandles("EUR_USD", "USD_JPY", "GBP_USD", timeFrame = tf)
    // end::oandahistoric[]
}


fun oandaLiveFeed() {
    // tag::oandalive[]
    val feed = OANDALiveFeed()
    feed.subscribePrices("EUR_USD", "USD_JPY", "GBP_USD")

    val tf = TimeFrame.next(120.minutes)
    roboquant.run(feed,tf)
    // end::oandalive[]
}



fun oandaBroker() {
    // tag::oandabroker[]
    val broker = OANDABroker()
    broker.account.summary().log()
    // end::oandabroker[]
}
