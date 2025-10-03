@file:Suppress("unused", "UNUSED_VARIABLE", "UNUSED_PARAMETER", "WildcardImport", "MagicNumber")

import org.roboquant.common.Account
import org.roboquant.charts.TimeSeriesChart
import org.roboquant.common.Asset
import org.roboquant.common.Event
import org.roboquant.common.Order
import org.roboquant.common.Stock
import org.roboquant.common.TimeSeries
import org.roboquant.feeds.Feed
import org.roboquant.journals.Journal
import org.roboquant.journals.MemoryJournal
import org.roboquant.journals.metrics.*
import org.roboquant.questdb.QuestDBJournal
import org.roboquant.run
import org.roboquant.strategies.EMACrossover
import org.roboquant.common.Signal
import org.roboquant.strategies.Strategy
import java.time.Instant


fun standard(strategy: Strategy, sp500Asset: Asset) {
    // tag::standard[]
    val metric1 = AccountMetric()
    val metric2 = ExposureMetric()
    val metric3 = ProgressMetric()
    val metric4 = ReturnsMetric()
    val metric5 = PNLMetric()
    val metric6 = AlphaBetaMetric(period = 250)

    val journal = MemoryJournal(
        metric1, metric2, metric3, metric4, metric5, metric6,
    )
    // end::standard[]
}


fun questdbLogger(feed: Feed) {
    // tag::questdblogger[]
    // Without specifying a path, the logger will use
    // the default DB path ~/.roboquant/questdb-metrics/db
    val journal = QuestDBJournal(AccountMetric())
    run(feed, EMACrossover(), journal)
    // end::questdblogger[]
}


fun memoryLogger(journal: MemoryJournal) {
    // tag::memoryLogger[]
    // You can always find out which metrics are available after a run
    println(journal.getMetricNames())

    // And plot a metric in a Jupyter Notebook
    val equity = journal.getMetric("account.equity")
    TimeSeriesChart(equity)
    // end::memoryLogger[]
}

fun staticExample() {
    // tag::simple[]
    class MyMetric : Metric {

        private val asset = Stock("AAPL")

        override fun calculate(
            event: Event,
            account: Account,
            signals: List<Signal>,
            orders: List<Order>
        ): Map<String, Double> {
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
    class MyDatabaseLogger : Journal {

        private val database = Database()

        override fun track(event: Event, account: Account, signals: List<Signal>, orders: List<Order>) {
            for (order in orders) {
                database.store(order.asset.symbol, order.size.toDouble(), event.time)
            }
        }

    }
    // end::customLogger[]
}
