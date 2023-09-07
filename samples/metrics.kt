@file:Suppress("unused", "UNUSED_VARIABLE", "UNUSED_PARAMETER", "WildcardImport", "MagicNumber")

import org.roboquant.Roboquant
import org.roboquant.brokers.Account
import org.roboquant.common.Asset
import org.roboquant.common.TimeSeries
import org.roboquant.feeds.Event
import org.roboquant.charts.TimeSeriesChart
import org.roboquant.common.years
import org.roboquant.feeds.Feed
import org.roboquant.loggers.MetricsLogger
import org.roboquant.metrics.*
import org.roboquant.questdb.QuestDBMetricsLogger
import org.roboquant.strategies.EMAStrategy
import org.roboquant.strategies.Strategy
import java.time.Instant


fun standard(strategy: Strategy, sp500Asset: Asset) {
    // tag::standard[]
    val metric1 = AccountMetric()
    val metric2 = ExposureMetric()
    val metric3 = ProgressMetric()
    val metric4 = VWAPMetric()
    val metric5 = ReturnsMetric()
    val metric6 = PNLMetric()
    val metric7 = AlphaBetaMetric(sp500Asset, 250)
    val metric8 = ScorecardMetric()

    val roboquant = Roboquant(
        strategy, metric1, metric2, metric3, metric4, metric5, metric6, metric7, metric8
    )
    // end::standard[]
}


fun questdbLogger(roboquant: Roboquant, feed: Feed) {
    // tag::questdblogger[]
    // Without specifying a path, the logger will use
    // the default DB path ~/.roboquant/questdb-metrics/db
    val logger = QuestDBMetricsLogger()

    val rq = Roboquant(EMAStrategy(), AccountMetric(), logger = logger)

    // The logger can be used for multiple runs, even concurrently if required.
    // Just make sure every run has a unique name
    feed.timeframe.split(1.years).forEach {
        rq.run(feed, it , name = "run-${it.toPrettyString()}")
    }
    // end::questdblogger[]
}


fun memoryLogger(roboquant: Roboquant) {
    // tag::memoryLogger[]
    // You can always find out which metrics are available after a run
    val logger = roboquant.logger
    println(logger.getMetricNames())

    // And plot a metric in a Jupyter Notebook
    val equity = logger.getMetric("account.equity")
    TimeSeriesChart(equity)
    // end::memoryLogger[]
}

fun staticExample() {
    // tag::simple[]
    class MyMetric : Metric {

        private val asset = Asset("AAPL")

        override fun calculate(account: Account, event: Event): Map<String, Double> {
            val metric1 = account.buyingPower.value
            val metric2 = event.getPrice(asset) ?: Double.NaN
            return mapOf("buyingpower" to metric1, "appleprice" to metric2)
        }

    }
    // end::simple[]
}


fun exampleCustomLogger() {
    class Database {
        fun store(key: String, value: Number, time: Instant) {
            TODO()
        }

        fun retrieve(key: String, run: String): TimeSeries {
            TODO()
        }

        fun retrieveKeys(run: String): Set<String> {
            TODO()
        }
    }

    // tag::customLogger[]
    class MyDatabaseLogger : MetricsLogger {

        private val database = Database()

        override fun log(results: Map<String, Double>, time: Instant, run: String) {
            for ((key, value) in results) database.store(key, value, time)
        }

        /**
         * Optional, if you want to access the logged metrics via roboquant
         */
        override fun getMetric(metricName: String, run: String): TimeSeries {
            return database.retrieve(metricName, run)
        }

        /**
         * Optional, if you want to access the logged metrics via roboquant
         */
        override fun getMetricNames(run: String) = database.retrieveKeys(run)

    }
    // end::customLogger[]
}
