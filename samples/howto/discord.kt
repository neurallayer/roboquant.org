package howto

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import org.roboquant.Roboquant
import org.roboquant.brokers.Account
import org.roboquant.common.Timeframe
import org.roboquant.common.hours
import org.roboquant.feeds.Event
import org.roboquant.feeds.util.LiveTestFeed
import org.roboquant.orders.Order
import org.roboquant.policies.FlexPolicy
import org.roboquant.strategies.EMAStrategy
import org.roboquant.strategies.Signal
import java.time.Instant
import java.util.concurrent.TimeUnit

class DiscordBot(channelName: String = "general") : ListenerAdapter() {

    private var jda: JDA
    private var channel: MessageChannel

    init {
        val token = "my_secret_discord_token"
        jda = JDABuilder
            .createDefault(token)
            .addEventListeners(this)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT)
            .build()
            .awaitReady()

        channel = jda.getTextChannelsByName(channelName, true).first()
    }


    fun publish(signal: Signal, time: Instant) {

        val embed = EmbedBuilder()
            .setTitle("Signal Detector")
            .setAuthor("roboquant", "http://roboquant.org", "http://roboquant.org/img/avatar.png")
            .setFooter("generated by roboquant", "http://roboquant.org/img/avatar.png")

        val fields = listOf(
            MessageEmbed.Field("asset", signal.asset.symbol, true),
            MessageEmbed.Field("rating", signal.rating.toString(), true),
            MessageEmbed.Field("time", time.toString(), true)
        )

        embed.fields.addAll(fields)
        val msg = MessageCreateData.fromEmbeds(embed.build())
        val action = channel.sendMessage(msg)
        val id = action.submit().get().id

        // Delete after 30 minutes
        channel.deleteMessageById(id).queueAfter(30, TimeUnit.MINUTES)
    }

}


class DiscordPolicy : FlexPolicy() {

    private val bot = DiscordBot("bot-insights")

    override fun act(signals: List<Signal>, account: Account, event: Event): List<Order> {
        for (signal in signals) bot.publish(signal, event.time)

        // If you don't want to (sim)trade, return an empty list
        // return emptyList()

        return super.act(signals, account, event)
    }

}

fun main() {
    val feed = LiveTestFeed()
    val strategy = EMAStrategy()

    val rq = Roboquant(strategy, policy = DiscordPolicy())
    rq.run(feed, Timeframe.next(8.hours))
}