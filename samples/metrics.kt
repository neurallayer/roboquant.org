@file:Suppress("unused", "UNUSED_VARIABLE" , "UNUSED_PARAMETER")

import org.roboquant.Roboquant
import org.roboquant.brokers.Account
import org.roboquant.common.Asset
import org.roboquant.common.TimeSeries
import org.roboquant.feeds.Event
import org.roboquant.jupyter.MetricChart
import org.roboquant.loggers.MetricsLogger
import org.roboquant.metrics.*
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


fun memoryLogger(roboquant: Roboquant) {
    // tag::memoryLogger[]
    // You can always find out which metrics are available after a run
    val logger = roboquant.logger
    println(logger.metricNames)

    // And plot a metric in a Jupyter Notebook
    val equity = logger.getMetric("account.equity")
    MetricChart(equity)
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
        fun store(key: String, value: Number, time: Instant) { TODO() }
        fun retrieve(key: String) :  Map<String, TimeSeries> { TODO() }
        fun retrieveKeys(): List<String> { TODO() }
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
        override fun getMetric(name: String): Map<String, TimeSeries> {
            return database.retrieve(name)
        }

        /**
         * Optional, if you want to access the logged metrics via roboquant
         */
        override val metricNames
            get() = database.retrieveKeys()

    }
    // end::customLogger[]
}