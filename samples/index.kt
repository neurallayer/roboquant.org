@file:Suppress("unused", "UnnecessaryVariable", "WildcardImport", "MagicNumber", "ReturnCount")

import org.roboquant.run
import org.roboquant.avro.AvroFeed
import org.roboquant.brokers.Account
import org.roboquant.brokers.Broker
import org.roboquant.feeds.Event
import org.roboquant.feeds.EventChannel
import org.roboquant.feeds.csv.CSVFeed
import org.roboquant.policies.Policy
import org.roboquant.strategies.EMACrossover
import org.roboquant.strategies.Signal
import org.roboquant.strategies.Strategy


fun myFirst() {
    val feed = AvroFeed.sp25()
    val account = run(feed, EMACrossover.PERIODS_5_15)
    println(account)
}


fun overview() {

    // tag::overview[]
    class MyStrategy : Strategy { // <1>
        override fun generate(event: Event): List<Signal> {
            TODO() // Your code goes here
        }
    }

    val strategy = MyStrategy() // <2>
    val feed = CSVFeed("./data/stocks") //<3>
    run(feed, strategy) //<4>
    // end::overview[]
}


suspend fun details(channel: EventChannel, strategy: Strategy, policy: Policy, broker: Broker, runName: String) {

    fun runMetrics(account: Account, event: Event, run: String) = print("$account $event $run")

    // tag::step[]
    val event = channel.receive() // <1>
    val signals = strategy.generate(event) // <2>
    val account = broker.sync(event) // <3>

    val orders = policy.act(signals, account, event) // <5>
    broker.place(orders) // <6>
    runMetrics(account, event, runName) // <7>
    // end::step[]
}
