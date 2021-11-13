import org.roboquant.Roboquant
import org.roboquant.brokers.sim.SimBroker
import org.roboquant.common.TimeFrame
import org.roboquant.feeds.random.RandomWalk
import org.roboquant.logging.MemoryLogger
import org.roboquant.metrics.AccountSummary
import org.roboquant.metrics.ProgressMetric
import org.roboquant.policies.DefaultPolicy
import org.roboquant.strategies.EMACrossover
import java.time.Period


fun basic() {
    // tag::basic[]
    val strategy  = EMACrossover()
    val roboquant = Roboquant(strategy)
    // end::basic[]
}


fun complete() {
    val strategy  = EMACrossover()
    val myBroker = SimBroker()
    val myPolicy = DefaultPolicy()
    val metric1 = AccountSummary()
    val metric2 = ProgressMetric()
    val myLogger  = MemoryLogger()
    // tag::complete[]
    val roboquant = Roboquant(strategy, metric1, metric2, policy = myPolicy, broker = myBroker, logger = myLogger)
    // end::complete[]
}



fun run() {
    val roboquant = Roboquant(EMACrossover())
    val feed = RandomWalk.lastYears()
    // tag::run[]
    roboquant.run(feed)
    // end::run[]
}


fun run2() {
    val roboquant = Roboquant(EMACrossover())
    val feed = RandomWalk.lastYears()
    // tag::run2[]
    val timeFrame = TimeFrame.fromYears(2015, 2020)
    roboquant.run(feed, timeFrame)
    // end::run2[]
}


fun run3() {
    val roboquant = Roboquant(EMACrossover())
    val feed = RandomWalk.lastYears()
    // tag::run3[]
    val timeFrame = TimeFrame.fromYears(2010, 2020)
    timeFrame.split(Period.ofYears(2)).forEach {
        roboquant.run(feed, it)
    }
    // end::run3[]
}
