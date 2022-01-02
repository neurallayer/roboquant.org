import org.roboquant.Roboquant
import org.roboquant.brokers.sim.SimBroker
import org.roboquant.common.TimeFrame
import org.roboquant.common.minutes
import org.roboquant.common.years
import org.roboquant.feeds.random.RandomWalk
import org.roboquant.logging.MemoryLogger
import org.roboquant.metrics.AccountSummary
import org.roboquant.metrics.ProgressMetric
import org.roboquant.policies.DefaultPolicy
import org.roboquant.strategies.EMACrossover


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
    // Historical feed example
    val timeFrame = TimeFrame.fromYears(2015, 2020)
    roboquant.run(feed, timeFrame)

    // Live feed example
    val timeFrame2 = TimeFrame.next(120.minutes)
    roboquant.run(feed, timeFrame2)
    // end::run2[]
}



fun run2b() {
    val roboquant = Roboquant(EMACrossover())
    val feed = RandomWalk.lastYears()
    // tag::run2b[]
    val timeFrame = TimeFrame.next(60.minutes)
    roboquant.run(feed, timeFrame)
    // end::run2b[]
}


fun run3() {
    val roboquant = Roboquant(EMACrossover())
    val feed = RandomWalk.lastYears()
    // tag::run3[]
    val timeFrame = TimeFrame.fromYears(2010, 2020)
    timeFrame.split(2.years).forEach {
        roboquant.run(feed, it)
    }
    // end::run3[]
}
