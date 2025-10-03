@file:Suppress("unused", "UnnecessaryVariable", "WildcardImport", "MagicNumber", "ReturnCount")

import org.roboquant.avro.AvroFeed
import org.roboquant.common.Event
import org.roboquant.feeds.csv.CSVFeed
import org.roboquant.run
import org.roboquant.strategies.EMACrossover
import org.roboquant.common.Signal
import org.roboquant.strategies.Strategy


fun myFirst() {
    val feed = AvroFeed.sp25()
    val account = run(feed, EMACrossover.PERIODS_5_15)
    println(account)
}


fun overview() {

    // tag::overview[]
    class MyStrategy : Strategy { // <1>
        override fun createSignals(event: Event): List<Signal> {
            TODO() // Your code goes here
        }
    }

    val strategy = MyStrategy() // <2>
    val feed = CSVFeed("./data/stocks") //<3>
    val account = run(feed, strategy) //<4>
    println(account.equity()) //<5>
    // end::overview[]
}

