@file:Suppress("unused", "UNUSED_VARIABLE", "MagicNumber", "WildcardImport", "TooManyFunctions")

import org.roboquant.alpaca.AlpacaBroker
import org.roboquant.brokers.Account
import org.roboquant.charts.TimeSeriesChart
import org.roboquant.charts.TradeChart
import org.roboquant.common.ParallelJobs
import org.roboquant.common.Timeframe
import org.roboquant.common.minutes
import org.roboquant.common.years
import org.roboquant.feeds.Feed
import org.roboquant.feeds.HistoricFeed
import org.roboquant.feeds.LiveFeed
import org.roboquant.journals.BasicJournal
import org.roboquant.journals.MemoryJournal
import org.roboquant.journals.MultiRunJournal
import org.roboquant.metrics.AccountMetric
import org.roboquant.policies.FlexPolicy
import org.roboquant.run
import org.roboquant.runAsync
import org.roboquant.strategies.CombinedStrategy
import org.roboquant.strategies.EMACrossover


fun basic(feed: Feed) {
    // tag::basic[]
    val strategy = EMACrossover()
    run(feed, strategy)
    // end::basic[]
}

fun tradeChart(account: Account) {
    // tag::tradeChart[]
    TradeChart(account.trades)
    // end::tradeChart[]
}

fun complete(feed: Feed) {
    // tag::complete[]
    run(
        feed,
        EMACrossover(), // the strategy to use
        policy = FlexPolicy { shorting = true }, // a customized policy
        broker = AlpacaBroker(), // using the Alpaca broker
        journal = BasicJournal(),
        timeframe = Timeframe.blackMonday1987,
        heartbeatTimeout = 2000,
        showProgressBar = true
    )
    // end::complete[]
}

fun combinedStrategy() {
    // tag::combined[]
    val strategy1 = EMACrossover.PERIODS_12_26
    val strategy2 = EMACrossover.PERIODS_50_200
    val strategy = CombinedStrategy(strategy1, strategy2)
    // end::combined[]
}


fun run(feed: Feed) {
    // tag::run[]
    run(feed, EMACrossover())
    // end::run[]
}


fun run2(feed1: HistoricFeed, feed2: LiveFeed) {
    // tag::run2[]
    // Historical feed run example
    val timeframe = Timeframe.fromYears(2015, 2020)
    run(feed1, EMACrossover(), timeframe = timeframe)

    // Live feed run example
    val timeframe2 = Timeframe.next(120.minutes)
    run(feed2, EMACrossover(), timeframe = timeframe2)
    // end::run2[]
}


fun run3(feed: Feed) {

    // tag::run3[]
    val timeframe = feed.timeframe
    timeframe.split(2.years).forEach {
        val account = run(feed, EMACrossover(), timeframe = it)
        println(account.equity)
    }
    // end::run3[]
}


fun run4(feed: Feed) {

    // tag::run4[]
    val timeframe = feed.timeframe
    timeframe.sample(2.years, 100).forEach {
        val account = run(feed, EMACrossover(), timeframe = it)
        println(account.equity)
    }
    // end::run4[]
}




fun runParallel(feed: Feed) {

    // tag::runParallel[]
    val timeframe = feed.timeframe
    val mrj = MultiRunJournal { MemoryJournal(AccountMetric()) }
    val jobs = ParallelJobs()

    for (period in timeframe.split(2.years)) {
        jobs.add {
            // Give the run a unique identifiable name
            runAsync(feed, EMACrossover(), mrj.getJournal(), period)
        }
    }

    // Wait until all the jobs are finished
    jobs.joinAllBlocking()

    // If you are in Jupyter Notebook, you can easily plot a metric for all the runs
    val equity = mrj.getMetric("account.equity")
    TimeSeriesChart(equity)
    // end::runParallel[]
}


fun debug(feed: Feed) {

    // tag::debug[]
    val strategy = EMACrossover()
    run(feed, EMACrossover())
    // end::debug[]
}
