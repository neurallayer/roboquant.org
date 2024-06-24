@file:Suppress("unused", "UNUSED_VARIABLE", "UNUSED_PARAMETER", "WildcardImport", "MagicNumber")

import org.roboquant.Roboquant
import org.roboquant.brokers.Account
import org.roboquant.common.Asset
import org.roboquant.common.TimeSeries
import org.roboquant.feeds.Event
import org.roboquant.charts.TimeSeriesChart
import org.roboquant.run
import org.roboquant.feeds.Feed
import org.roboquant.journals.Journal
import org.roboquant.journals.MemoryJournal
import org.roboquant.metrics.*
import org.roboquant.orders.Instruction
import org.roboquant.orders.Order
import org.roboquant.questdb.QuestDBJournal
import org.roboquant.strategies.EMACrossover
import org.roboquant.strategies.Signal
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
    val metric7 = AlphaBetaMetric(period = 250)
    val metric8 = ScorecardMetric()

    val journal = MemoryJournal(
        metric1, metric2, metric3, metric4, metric5, metric6, metric7, metric8
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
    class MyDatabaseLogger : Journal {

        private val database = Database()

        override fun track(event: Event, account: Account, signals: List<Signal>, instructions: List<Instruction>) {
            for (signal in signals) {
                database.store(signal.asset.symbol, signal.rating, event.time)
            }
        }

    }
    // end::customLogger[]
}
