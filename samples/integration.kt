@file:Suppress("unused", "UNUSED_PARAMETER", "TooManyFunctions", "SpreadOperator", "MagicNumber", "WildcardImport")

import com.crazzyghost.alphavantage.parameters.Interval
import net.jacobpeterson.alpaca.model.properties.DataAPIType
import org.roboquant.Roboquant
import org.roboquant.alpaca.AccountType
import org.roboquant.alpaca.AlpacaBroker
import org.roboquant.alpaca.AlpacaHistoricFeed
import org.roboquant.alpaca.AlpacaLiveFeed
import org.roboquant.alphavantage.AlphaVantageHistoricFeed
import org.roboquant.brokers.Account
import org.roboquant.brokers.Broker
import org.roboquant.brokers.ECBExchangeRates
import org.roboquant.brokers.sim.execution.InternalAccount
import org.roboquant.common.*
import org.roboquant.common.Currency
import org.roboquant.feeds.Event
import org.roboquant.feeds.HistoricPriceFeed
import org.roboquant.feeds.TradePrice
import org.roboquant.ibkr.IBKRHistoricFeed
import org.roboquant.orders.MarketOrder
import org.roboquant.orders.Order
import org.roboquant.orders.OrderStatus
import org.roboquant.polygon.PolygonHistoricFeed
import org.roboquant.polygon.PolygonLiveFeed
import org.roboquant.strategies.EMAStrategy
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate


fun ibkrFeed(roboquant: Roboquant) {
    // tag::ibkrfeed[]
    val feed = IBKRHistoricFeed()
    val asset = Asset("TSLA")
    feed.retrieve(asset)
    roboquant.run(feed)
    // end::ibkrfeed[]
}


fun ecb() {
    // tag::ecb[]
    // Download the latest rates from the ECB website
    // The results are cached by default
    Config.exchangeRates = ECBExchangeRates.fromWeb()

    // Create a wallet holding different currencies
    val wallet = 100.EUR + 20.USD + 1000.JPY

    // convert the wallet to GBP at today's exchange rates
    wallet.convert(Currency.GBP)

    //  convert the wallet to GBP using 5 years ago exchange rates
    wallet.convert(Currency.GBP, Instant.now() - 5.years)
    // end::ecb[]
}


fun alphaHistoricFeed() {
    // tag::alphahistoric[]
    val feed = AlphaVantageHistoricFeed()

    // You can retrieve the end-of-day prices
    val assets = listOf(
        // regular US stock
        Asset("AAPL"),

        // stock listed on a non-US exchange
        Asset("DAI.DEX", currency = Currency.EUR, exchange = Exchange.DEX)
    )
    feed.retrieveDaily(*assets.toTypedArray())

    // You can retrieve intra-day prices
    feed.retrieveIntraday(Asset("TSLA"), interval = Interval.ONE_MIN)
    // end::alphahistoric[]
}


fun polygonHistoricFeed() {
    // tag::polygonhistoric[]
    val feed = PolygonHistoricFeed()
    val tf = Timeframe.past(100.days)
    feed.retrieve("AAPL", "JPM", "TSLA", timeframe = tf)
    // end::polygonhistoric[]
}

suspend fun polygonLiveFeed(roboquant: Roboquant) {
    // tag::polygonlive[]
    val feed = PolygonLiveFeed()
    feed.subscribe("AAPL", "JPM", "TSLA")

    val tf = Timeframe.next(120.minutes)
    roboquant.run(feed, tf)
    // end::polygonlive[]
}


fun alpacaHistoricFeed() {
    // tag::alpacahistoric[]
    val feed = AlpacaHistoricFeed()
    val tf = Timeframe.past(100.days)

    // You can retrieve historic price-bars, quotes or trades
    feed.retrieveStockPriceBars("AAPL", "JPM", "TSLA", timeframe = tf)
    feed.retrieveStockQuotes("AAPL", "JPM", "TSLA", timeframe = tf)
    feed.retrieveStockTrades("AAPL", "JPM", "TSLA", timeframe = tf)
    // end::alpacahistoric[]
}


fun alpacaLiveFeed(roboquant: Roboquant) {
    // tag::alpacalive[]
    val feed = AlpacaLiveFeed()
    feed.subscribeStocks("AAPL", "IBM")

    val tf = Timeframe.next(120.minutes)
    roboquant.run(feed, tf)
    // end::alpacalive[]
    println(feed)
}

fun feedPolygon() {

    // Get the feed
    val feed = PolygonHistoricFeed()
    println(feed.availableAssets.summary())
    val tf = Timeframe.fromYears(2021, 2022)
    feed.retrieve("IBM", "AAPL", timeframe = tf)
    println(feed.assets)
    println(feed.timeline.size)

    // Use the feed
    val strategy = EMAStrategy()
    val roboquant = Roboquant(strategy)
    roboquant.run(feed)
    val account = roboquant.broker.account
    println(account.fullSummary())
}

fun alphaVantage() {
    val feed = AlphaVantageHistoricFeed()

    val assets = listOf(
        // regular US stock
        Asset("IBM"),

        // stock listed on non-US exchange
        Asset("DAI.DEX", currency = Currency.EUR, exchange = Exchange.DEX)
    )

    feed.retrieveDaily(*assets.toTypedArray())
    feed.retrieveIntraday(Asset("TSLA"))
    println(feed.timeframe)
}

fun alpacaConfig() {
    // tag::alpacaconfig[]
    // instantiation with hard-coded configuration
    val feed = AlpacaHistoricFeed {
        publicKey = "123"
        secretKey = "456"
        dataType = DataAPIType.IEX
    }
    // end::alpacaconfig[]
    println(feed)
}

private class FeedAPI {
    fun getTrades(symbol: String, start: Instant, end: Instant): List<Triple<LocalDate, Double, Int>> {
        TODO()
    }
}

private fun feedIntegration() {
    // tag::feedIntegration[]
    class MyHistoricFeed : HistoricPriceFeed() {

        // The api of your provider
        private val api = FeedAPI()

        fun retrieve(symbol: String, timeframe: Timeframe) {

            // Make the API call
            val trades = api.getTrades(symbol, timeframe.start, timeframe.end)

            // The API returns American stocks
            val asset = Asset(symbol, AssetType.STOCK, Currency.USD, Exchange.US)

            // Loop over trades
            for ((date, price, volume) in trades) {

                // Convert to a TradePrice
                val tradePrice = TradePrice(asset, price, volume.toDouble())

                // Convert to an Instant
                val time = asset.exchange.getClosingTime(date)

                // Add it
                add(time, tradePrice)
            }
        }

    }
    // end::feedIntegration[]
}

@Suppress("CyclomaticComplexMethod")
fun myBroker() {

    abstract class BrokerOrder {
        abstract var status: String
    }

    class BrokerApi {
        fun placeMarketOrder(symbol: String, volume: BigDecimal, id: Int) {
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

        // Internal Account is a mutable version of Account
        private val iAccount = InternalAccount(baseCurrency = Currency.USD)

        override val account: Account
            get() = iAccount.toAccount()

        override fun sync(event: Event) {
            // All the hard work goes here where you sync the InternalAccount object with your broker
            // The api calls are all fictitious examples
            val now = Instant.now()

            // Optional check to avoid to many calls to the broker
            if (iAccount.orders.isEmpty() && (now < account.lastUpdate + 1.minutes)) return

            // Sync the positions
            iAccount.portfolio.clear()
            for (position in api.getPositions()) {
                TODO()
                // iAccount.setPosition(...)
            }

            // Sync the open orders
            for (order in iAccount.orders) {
                val brokerOrder = api.getOrder(order.orderId)

                // Fictitious implementation
                when (brokerOrder.status) {
                    "RECEIVED" -> iAccount.updateOrder(order.order, now, OrderStatus.ACCEPTED)
                    "DONE" -> iAccount.updateOrder(order.order, now, OrderStatus.COMPLETED)
                }
            }

            // Sync buying-power
            val buyingPower = api.getBuyingPower()
            iAccount.buyingPower = buyingPower.USD

            // Set the lastUpdate time
            iAccount.lastUpdate = now
        }


        override fun place(orders: List<Order>, time: Instant) {

            // Optional sanity check that we don't use this broker in historic back tests
            if (time < Instant.now() - 1.hours) throw UnsupportedException("Cannot place old orders")

            // Validation of the supported order types
            require(orders.all { it is MarketOrder })

            // Store them in the internal account. Orders should never be (temporary) lost
            iAccount.initializeOrders(orders)

            // Process the orders
            for (order in orders.filterIsInstance<MarketOrder>()) {
                // Fictitious API call
                api.placeMarketOrder(order.asset.symbol, order.size.toBigDecimal(), order.id)
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
        dataType = DataAPIType.IEX
    }
    println(broker.account.summary())
    println(broker.availableAssets)

    // place a market order to buy 100 stocks Apple
    val order = MarketOrder(Asset("AAPL"), 100)
    broker.place(listOf(order))
    // end::alpacabroker[]
}
