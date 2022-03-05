@file:Suppress("unused")

import org.roboquant.Roboquant
import org.roboquant.brokers.Account
import org.roboquant.feeds.Event
import org.roboquant.metrics.*
import org.roboquant.strategies.Strategy


fun standard(strategy: Strategy) {
    // tag::standard[]
    val metric1 = AccountSummary()
    val metric2 = PortfolioExposure()
    val metric3 = ProgressMetric()
    val metric4 = VWAPMetric()
    val metric5 = SharpRatio()

    val roboquant = Roboquant(strategy, metric1, metric2, metric3, metric4, metric5)
    // end::standard[]
}


// tag::simple[]
class MyMetric : SimpleMetric() {

    override fun calc(account: Account, event: Event): MetricResults {
        return mapOf("metricName1" to 1.0, "metricName2" to 2.0)
    }

}
// end::simple[]