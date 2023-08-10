@file:Suppress("unused", "UNUSED_VARIABLE", "MagicNumber", "WildcardImport", "TooManyFunctions")

import org.roboquant.Roboquant
import org.roboquant.alpaca.AlpacaBroker
import org.roboquant.brokers.Account
import org.roboquant.common.*
import org.roboquant.feeds.Feed
import org.roboquant.feeds.HistoricFeed
import org.roboquant.feeds.LiveFeed
import org.roboquant.feeds.RandomWalkFeed
import org.roboquant.charts.TimeSeriesChart
import org.roboquant.charts.TradeChart
import org.roboquant.loggers.ConsoleLogger
import org.roboquant.loggers.InfoLogger
import org.roboquant.loggers.MemoryLogger
import org.roboquant.metrics.AccountMetric
import org.roboquant.metrics.PNLMetric
import org.roboquant.policies.FlexPolicy
import org.roboquant.strategies.CombinedStrategy
import org.roboquant.strategies.EMAStrategy


fun basic() {
    // tag::basic[]
    val strategy = EMAStrategy()
    val roboquant = Roboquant(strategy)
    // end::basic[]
}

fun tradeChart(account: Account) {
    // tag::tradeChart[]
    TradeChart(account.trades)
    // end::tradeChart[]
}

fun complete() {
    // tag::complete[]
    val roboquant = Roboquant(
        EMAStrategy(),
        AccountMetric(), PNLMetric(),
        policy = FlexPolicy {
            shorting = true
        },
        broker = AlpacaBroker(),
        logger = InfoLogger()
    )
    // end::complete[]
}

fun combinedStrategy() {
    // tag::combined[]
    val strategy1 = EMAStrategy.PERIODS_12_26
    val strategy2 = EMAStrategy.PERIODS_50_200
    val strategy = CombinedStrategy(strategy1, strategy2)
    val roboquant = Roboquant(strategy)
    // end::combined[]
}


fun run() {
    val roboquant = Roboquant(EMAStrategy())
    val feed = RandomWalkFeed.lastYears()
    // tag::run[]
    roboquant.run(feed)
    // end::run[]
}


fun run2(feed1: HistoricFeed, feed2: LiveFeed, roboquant: Roboquant) {
    // tag::run2[]
    // Historical feed run example
    val timeframe = Timeframe.fromYears(2015, 2020)
    roboquant.run(feed1, timeframe)

    // Live feed run example
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


fun run4(feed: Feed, roboquant: Roboquant) {

    // tag::run4[]
    val timeframe = feed.timeframe
    timeframe.sample(2.years, 100).forEach {
        roboquant.run(feed, it)
        println(roboquant.broker.account.equityAmount)
    }
    // end::run4[]
}


fun runValidation(feed: Feed, roboquant: Roboquant) {

    // tag::runValidation[]
    // Single run example
    val (main, validation) = feed.timeframe.splitTrainTest(0.2) // 20% for validation phase
    roboquant.run(feed, main, "run-main")
    println(roboquant.broker.account.equityAmount)
    roboquant.broker.reset()
    roboquant.run(feed, validation, "run-validation")

    // Walk forward example
    feed.timeframe.split(2.years).forEach {
        val (main2, validation2) = it.splitTrainTest(0.25) // 25% for validation phase
        roboquant.run(feed, main2)
        roboquant.broker.reset()
        roboquant.run(feed, validation, "run-validation")
        println(roboquant.broker.account.equityAmount)
    }
    // end::runValidation[]
}


fun runParallel(feed: Feed) {

    // tag::runParallel[]
    val timeframe = feed.timeframe
    val logger = MemoryLogger(showProgress = false)
    val jobs = ParallelJobs()

    for (period in timeframe.split(2.years)) {
        jobs.add {
            // Create a new roboquant instance for each job
            val roboquant = Roboquant(EMAStrategy(), AccountMetric(), logger = logger)

            // Give the run a unique identifiable name
            roboquant.runAsync(feed, period, name = "run-$period")
        }
    }

    // Wait until all the jobs are finished
    jobs.joinAllBlocking()

    // If you are in Jupyter Notebook, you can easily plot a metric for all the runs
    val equity = logger.getMetric("account.equity")
    TimeSeriesChart(equity)
    // end::runParallel[]
}


fun debug(feed: Feed) {

    // tag::debug[]
    val strategy = EMAStrategy()
    val policy = FlexPolicy()
    policy.enableMetrics = true
    val roboquant = Roboquant(strategy, policy = policy, logger = ConsoleLogger())
    roboquant.run(feed)
    // end::debug[]
}
