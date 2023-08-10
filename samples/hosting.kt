@file:Suppress("unused", "TooManyFunctions", "SpreadOperator", "MagicNumber", "WildcardImport")

import org.roboquant.Roboquant
import org.roboquant.binance.BinanceLiveFeed
import org.roboquant.brokers.sim.SimBroker
import org.roboquant.common.Amount
import org.roboquant.common.Timeframe
import org.roboquant.common.hours
import org.roboquant.common.summary
import org.roboquant.http.WebServer
import org.roboquant.loggers.InfoLogger
import org.roboquant.policies.FlexPolicy
import org.roboquant.polygon.PolygonHistoricFeed
import org.roboquant.strategies.EMAStrategy


suspend fun binanceWebServer() {
    // tag::webserver[]
    // set up the feed and subscribe to asset(s) of interest
    val feed = BinanceLiveFeed()
    feed.subscribePriceBar("BTCBUSD")

    // Set up a roboquant instance
    val strategy = EMAStrategy.PERIODS_5_15
    val initialDeposit = Amount("BUSD", 10_000).toWallet()
    val broker = SimBroker(initialDeposit)
    val policy = FlexPolicy.singleAsset()
    policy.enableMetrics = true
    val rq = Roboquant(strategy, broker = broker, policy = policy, logger = InfoLogger())

    // start the web server
    val server = WebServer(username = "test", password = "secret")
    server.start()

    // Start a run for 8 hours
    val tf = Timeframe.next(8.hours)
    server.runAsync(rq, feed, tf)

    // After the run has finished, stop the webserver
    server.stop()
    // end::webserver[]
}
