@file:Suppress("unused", "UNUSED_VARIABLE")

import org.roboquant.Roboquant
import org.roboquant.brokers.Account
import org.roboquant.common.Asset
import org.roboquant.feeds.Event
import org.roboquant.metrics.*
import org.roboquant.strategies.Strategy


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


// tag::simple[]
class MyMetric : SimpleMetric() {

    override fun calc(account: Account, event: Event): MetricResults {
        return mapOf("metricName1" to 1.0, "metricName2" to 2.0)
    }

}
// end::simple[]