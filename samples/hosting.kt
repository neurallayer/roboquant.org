@file:Suppress("unused", "TooManyFunctions", "SpreadOperator", "MagicNumber", "WildcardImport")

import org.roboquant.Roboquant
import org.roboquant.binance.BinanceLiveFeed
import org.roboquant.brokers.sim.SimBroker
import org.roboquant.common.*
import org.roboquant.feeds.Feed
import org.roboquant.policies.FlexPolicy
import org.roboquant.server.WebServer
import org.roboquant.strategies.EMAStrategy


private fun binanceWebServer() {
    // tag::webserver[]
    // set up the feed and subscribe to asset(s) of interest
    val feed = BinanceLiveFeed()
    feed.subscribePriceBar("BTCBUSD")

    // Set up a roboquant instance
    val strategy = EMAStrategy.PERIODS_5_15
    val initialDeposit = Amount("BUSD", 10_000).toWallet()
    val broker = SimBroker(initialDeposit)
    val policy = FlexPolicy.singleAsset()
    val rq = Roboquant(strategy, broker = broker, policy = policy)

    // start the web server, secured with credentials and running on the provided port
    val server = WebServer {
        username = "test"
        password = "secret"
        port = 8081
    }

    // Start a run for 8 hours
    val tf = Timeframe.next(8.hours)
    server.run(rq, feed, tf)

    // After the run has finished, stop the webserver and close the feed.
    server.stop()
    feed.close()
    // end::webserver[]
}


private fun multiRun(roboquant1: Roboquant, roboquant2: Roboquant, feed1: Feed, feed2: Feed) {
    // tag::multirun[]
    // start the web server with default port (8080) and NO login required
    val server = WebServer()

    // Start two runs
    val jobs = ParallelJobs()
    jobs.add {
        val tf = Timeframe.next(60.minutes)
        server.runAsync(roboquant1, feed1, tf, "run-strategy1")
        server.runAsync(roboquant2, feed2, tf, "run-strategy2")
    }

    jobs.joinAllBlocking()
    server.stop()
    // end::multirun[]
}
