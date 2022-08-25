@file:Suppress("unused", "UNUSED_VARIABLE" , "UNUSED_PARAMETER")

import org.roboquant.Roboquant
import org.roboquant.RunInfo
import org.roboquant.brokers.Account
import org.roboquant.common.Asset
import org.roboquant.feeds.Event
import org.roboquant.jupyter.MetricChart
import org.roboquant.logging.MetricsLogger
import org.roboquant.metrics.*
import org.roboquant.strategies.Strategy
import java.time.Instant


fun standard(strategy: Strategy, sp500Asset: Asset) {
    // tag::standard[]
    val metric1 = AccountSummary()
    val metric2 = PortfolioExposure()
    val metric3 = ProgressMetric()
    val metric4 = VWAPMetric()
    val metric5 = SharpRatio()
    val metric6 = PNL()
    val metric7 = AlphaBeta(sp500Asset, 250)

    val roboquant = Roboquant(
        strategy, metric1, metric2, metric3, metric4, metric5, metric6, metric7
    )
    // end::standard[]
}


fun memoryLogger(roboquant: Roboquant) {
    // tag::memoryLogger[]
    // You can always find out which metrics are available after a run
    val logger = roboquant.logger
    println(logger.metricNames)

    // And easily plot a metric in a notebook
    val equity = logger.getMetric("account.equity")
    MetricChart(equity)
    // end::memoryLogger[]
}

fun staticExample() {
    // tag::simple[]
    class MyMetric : Metric {

        private val asset = Asset("AAPL")

        override fun calculate(account: Account, event: Event): MetricResults {
            val metric1 = account.buyingPower.value
            val metric2 = event.getPrice(asset) ?: Double.NaN
            return mapOf("buyingpower" to metric1, "appleprice" to metric2)
        }

    }
    // end::simple[]
}



fun exampleCustomLogger() {
    class Database { fun store(key: String, value: Number, time: Instant) {} }

    // tag::customLogger[]
    class MyConsoleLogger : MetricsLogger {

        private val database = Database()

        override fun log(results: MetricResults, info: RunInfo) {
            for ((key, value) in results) database.store(key, value, info.time)
        }

    }
    // end::customLogger[]
}