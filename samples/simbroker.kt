@file:Suppress("unused", "UNUSED_VARIABLE", "WildcardImport", "MagicNumber")

import org.roboquant.brokers.sim.*
import org.roboquant.common.Currency
import org.roboquant.common.EUR
import org.roboquant.common.Size
import org.roboquant.common.Wallet
import org.roboquant.feeds.OrderBook
import org.roboquant.feeds.PriceAction
import org.roboquant.feeds.PriceQuote
import java.time.Instant


fun usageBasic() {
    // tag::basic[]
    val broker = SimBroker()
    // end::basic[]
}



fun configSimBroker() {
    // tag::extra[]
    val broker = SimBroker(
        initialDeposit = Wallet(10_000.EUR), // how much to initially deposit into the account
        baseCurrency = Currency.EUR, // currency to use for reporting
        feeModel = PercentageFeeModel(), // model to use to calculate fees, commissions, etc
        accountModel = MarginAccount(), // type of account to model, like a Cash or Margin account
        pricingEngine = SpreadPricingEngine() // engine to use to calculate the final price of a trade
    )
    // end::extra[]
}


fun includedModels() {
    // tag::included[]
    // Account Models
    val accountModel1 = CashAccount(minimum = 1_000.0)
    val accountModel2 = MarginAccount(
        initialMargin = 0.5,
        maintenanceMarginLong = 0.3,
        maintenanceMarginShort = 0.2,
        minimumEquity = 10_000.0
    )

    // Fee models
    val feeModel1 = PercentageFeeModel(0.01) // 1% fee
    val feeModel2 = NoFeeModel() // no additional fees apply

    // Pricing engines
    val pricingEngine1 = SpreadPricingEngine(spreadInBips = 5, priceType = "OPEN")
    val pricingEngine2 = NoCostPricingEngine(priceType = "CLOSE")
    // end::included[]
}

fun pricingEngine() {
    // tag::pricing[]
    class MyPricingEngine : PricingEngine {

        inner class MyPricing(val action : PriceAction) : Pricing {

            override fun marketPrice(size: Size): Double {
                return when(action) {
                    is PriceQuote -> if (size.isPositive) action.askPrice else action.bidPrice
                    is OrderBook -> if (size.isPositive) action.bestOffer else action.bestBid
                    else -> action.getPrice()
                }
            }

        }

        override fun getPricing(action: PriceAction, time: Instant): Pricing {
            return MyPricing(action)
        }

    }
    // end::pricing[]

}
