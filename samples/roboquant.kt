@file:Suppress("unused", "UNUSED_PARAMETER", "UNUSED_VARIABLE")

import org.roboquant.Roboquant
import org.roboquant.brokers.Account
import org.roboquant.brokers.sim.SimBroker
import org.roboquant.common.ParallelJobs
import org.roboquant.common.Timeframe
import org.roboquant.common.minutes
import org.roboquant.common.years
import org.roboquant.feeds.Feed
import org.roboquant.feeds.HistoricFeed
import org.roboquant.feeds.LiveFeed
import org.roboquant.feeds.random.RandomWalk
import org.roboquant.jupyter.MetricChart
import org.roboquant.jupyter.TradeChart
import org.roboquant.logging.MemoryLogger
import org.roboquant.metrics.AccountSummary
import org.roboquant.metrics.ProgressMetric
import org.roboquant.policies.DefaultPolicy
import org.roboquant.strategies.EMACrossover


fun basic() {
    // tag::basic[]
    val strategy  = EMACrossover()
    val roboquant = Roboquant(strategy)
    // end::basic[]
}


fun tradeChart(account: Account) {
    // tag::tradeChart[]
    TradeChart(account.trades)
    // end::tradeChart[]
}

fun complete() {
    val strategy  = EMACrossover()
    val myBroker = SimBroker()
    val myPolicy = DefaultPolicy()
    val metric1 = AccountSummary()
    val metric2 = ProgressMetric()
    val metric3 = ProgressMetric()
    val myLogger  = MemoryLogger()
    // tag::complete[]
    val roboquant = Roboquant(
        strategy,
        metric1, metric2, metric3,
        policy = myPolicy,
        broker = myBroker,
        logger = myLogger)
    // end::complete[]
}



fun run() {
    val roboquant = Roboquant(EMACrossover())
    val feed = RandomWalk.lastYears()
    // tag::run[]
    roboquant.run(feed)
    // end::run[]
}


fun run2(feed1: HistoricFeed, feed2: LiveFeed, roboquant: Roboquant ) {
    // tag::run2[]
    // Historical feed example
    val timeframe = Timeframe.fromYears(2015, 2020)
    roboquant.run(feed1, timeframe)

    // Live feed example
    val timeframe2 = Timeframe.next(120.minutes)
    roboquant.run(feed2, timeframe2)
    // end::run2[]
}


fun run3(feed: Feed, roboquant: Roboquant) {

    // tag::run3[]
    val timeframe = feed.timeframe
    timeframe.split(2.years).forEach {
        roboquant.run(feed, it)
        println(roboquant.broker.account.equityAmount)
    }
    // end::run3[]
}



fun runParallel(feed: Feed) {

    // tag::runParallel[]
    val timeframe = feed.timeframe
    val logger = MemoryLogger(showProgress = false)
    val jobs = ParallelJobs()

    for (period in timeframe.split(2.years)) {
        jobs.add {
            // Create a new roboquant instance for each job
            val roboquant = Roboquant(EMACrossover(), AccountSummary(), logger = logger)

            // Give the run a unique name so the metrics can be easily identified
            roboquant.run(feed, period, runName = "run-$period")
        }
    }

    // Wait till all jobs are finished
    jobs.joinAllBlocking()

    // If you are in Jupyter Notebook you can plot the metrics
    val data = logger.getMetric("account.equity")
    MetricChart(data)
    // end::runParallel[]
}
