import org.roboquant.Roboquant
import org.roboquant.feeds.Event
import org.roboquant.feeds.csv.CSVFeed
import org.roboquant.metrics.AccountSummary
import org.roboquant.strategies.Signal
import org.roboquant.strategies.Strategy

fun overview() {

    // tag::overview[]
    class MyStrategy : Strategy { // <1>
        override fun generate(event: Event): List<Signal> {
            TODO() // Your code goes here
        }
    }

    val strategy = MyStrategy() // <2>
    val metric = AccountSummary() //<3>
    val roboquant = Roboquant(strategy, metric) //<4>

    val feed = CSVFeed("./data/stocks") //<5>
    roboquant.run(feed) //<6>
    // end::overview[]
}