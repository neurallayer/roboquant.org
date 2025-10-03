@file:Suppress("unused", "UNUSED_PARAMETER", "TooManyFunctions", "SpreadOperator", "MagicNumber", "WildcardImport")

// tag::alpaca-import[]
// end::alpaca-import[]
import org.roboquant.alpaca.*
import org.roboquant.common.*
import org.roboquant.brokers.Broker
import org.roboquant.brokers.ECBExchangeRates
import org.roboquant.brokers.InternalAccount
import org.roboquant.ibkr.IBKRHistoricFeed
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate


fun ibkrFeed() {
    // tag::ibkrfeed[]
    val feed = IBKRHistoricFeed()
    val asset = Stock("TSLA")
    feed.retrieve(asset)
    // end::ibkrfeed[]
}


fun ecb() {
    // tag::ecb[]
    // Download the latest rates from the ECB website
    // The results are cached by default
    Amount.registerConvertor(ECBExchangeRates.fromWeb())

    // Create a wallet holding different currencies
    val wallet = 100.EUR + 20.USD + 1000.JPY

    // convert the wallet to GBP at today's exchange rates
    wallet.convert(Currency.GBP, Instant.now())

    //  convert the wallet to GBP using 5 years ago exchange rates
    wallet.convert(Currency.GBP, Instant.now() - 5.years)
    // end::ecb[]
}


fun ibkrConfig() {
    // tag::ibkr-config[]
    Config.setProperty("ibkr.host", "host.docker.internal")
    // end::ibkr-config[]
}



fun alpacaHistoricFeed() {
    // tag::alpacahistoric[]
    val feed = AlpacaHistoricFeed()
    val tf = Timeframe.past(100.days)

    // You can retrieve historic price-bars, quotes or trades
    feed.retrieveStockPriceBars("AAPL,JPM,TSLA", timeframe = tf)
    feed.retrieveStockQuotes("AAPL,JPM,TSLA", timeframe = tf)
    feed.retrieveStockTrades("AAPL,JPM,TSLA", timeframe = tf)
    // end::alpacahistoric[]
}


fun alpacaLiveFeed() {
    // tag::alpacalive[]
    val feed = AlpacaLiveFeed()
    feed.subscribeStocks("AAPL", "IBM")
    // end::alpacalive[]
    println(feed)
}

fun alpacaConfig() {
    // tag::alpacaconfig[]
    // instantiation with hard-coded configuration
    val feed = AlpacaHistoricFeed {
        publicKey = "123"
        secretKey = "456"
        dataType = DataType.IEX
    }
    // end::alpacaconfig[]
    println(feed)
}

private class FeedAPI {
    fun getTrades(symbol: String, start: Instant, end: Instant): List<Triple<LocalDate, Double, Int>> {
        TODO()
    }
}

@Suppress("CyclomaticComplexMethod")
fun myBroker() {

    abstract class BrokerOrder {
        abstract var status: String
    }

    class BrokerApi {
        fun placeMarketOrder(symbol: String, volume: BigDecimal, id: String) {
            TODO()
        }

        fun getOrder(id: Any): BrokerOrder {
            TODO()
        }

        fun getBuyingPower(): BigDecimal = TODO()
        fun getPositions(): List<Any> = TODO()
    }

    // tag::customBroker[]
    class MyBroker : Broker {

        private val api = BrokerApi()
        private var id = 0

        // Internal Account is a mutable version of Account
        private val iAccount = InternalAccount(baseCurrency = Currency.USD)


        override fun sync(event: Event?) : Account {
            // All the hard work goes here where you sync the InternalAccount object with your broker
            // The api calls are all fictitious examples

            val now = Instant.now()

            // Optional check that this broker isn't used in a back test
            if (event != null  && event.time < Instant.now() - 1.hours)
                throw UnsupportedException("Cannot place old orders")

            // Optional check to avoid to many calls to the broker
            if (iAccount.orders.isEmpty() && (now < iAccount.lastUpdate + 1.minutes)) return iAccount.toAccount()

            // Sync the positions
            iAccount.positions.clear()
            for (position in api.getPositions()) {
                TODO()
                // iAccount.setPosition(...)
            }

            // Sync the open orders
            for (order in iAccount.orders) {
                val brokerOrder = api.getOrder(order.id)

                // Fictitious implementation
                when (brokerOrder.status) {
                    "RECEIVED" -> TODO()
                    "DONE" -> TODO()
                }
            }

            // Sync buying-power
            val buyingPower = api.getBuyingPower()
            iAccount.buyingPower = buyingPower.USD

            // Set the lastUpdate time
            iAccount.lastUpdate = now
            return  iAccount.toAccount()
        }


        override fun placeOrders(orders: List<Order>) {


            // Process the orders
            for (order in orders) {
                order.id = id++.toString()
                api.placeMarketOrder(order.asset.symbol, order.size.toBigDecimal(), order.id)
                iAccount.orders.add(order)
            }


        }

    }
    // end::customBroker[]

}

fun alpacaBroker() {
    // tag::alpacabroker[]
    // instantiation with hard-coded configuration, rather than using dotenv property file
    val broker = AlpacaBroker {
        publicKey = "123"
        secretKey = "456"
        accountType = AccountType.PAPER
        dataType = DataType.IEX
    }
    println(broker.sync())

    // place a market order to buy 100 stocks Apple
    val order = Order(Stock("AAPL"), Size(100), 100.0)
    broker.placeOrders(listOf(order))
    // end::alpacabroker[]
}
