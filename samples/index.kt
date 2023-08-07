@file:Suppress("unused", "UnnecessaryVariable", "WildcardImport", "MagicNumber", "ReturnCount")

import org.roboquant.Roboquant
import org.roboquant.brokers.Account
import org.roboquant.brokers.Broker
import org.roboquant.feeds.AvroFeed
import org.roboquant.feeds.Event
import org.roboquant.feeds.EventChannel
import org.roboquant.feeds.csv.CSVFeed
import org.roboquant.metrics.AccountMetric
import org.roboquant.policies.Policy
import org.roboquant.strategies.EMAStrategy
import org.roboquant.strategies.Signal
import org.roboquant.strategies.Strategy



fun myFirst() {

    val rq = Roboquant(EMAStrategy.PERIODS_5_15)
    val feed = AvroFeed.sp500()

    rq.run(feed)
    println(rq.broker.account.summary())

}


fun overview() {

    // tag::overview[]
    class MyStrategy : Strategy { // <1>
        override fun generate(event: Event): List<Signal> {
            TODO() // Your code goes here
        }
    }

    val strategy = MyStrategy() // <2>
    val metric = AccountMetric() //<3>
    val roboquant = Roboquant(strategy, metric) //<4>

    val feed = CSVFeed("./data/stocks") //<5>
    roboquant.run(feed) //<6>
    // end::overview[]
}


suspend fun details(channel: EventChannel, strategy: Strategy, policy: Policy, broker: Broker, runName: String) {

    fun runMetrics(account: Account, event: Event, run: String) = print("$account $event $run")

    // tag::step[]
    val event = channel.receive() // <1>
    val signals = strategy.generate(event) // <2>
    // broker.sync(event) // <3>
    val account = broker.account // <4>
    val orders = policy.act(signals, account, event) // <5>
    broker.place(orders) // <6>
    runMetrics(account, event, runName) // <7>
    // end::step[]
}
