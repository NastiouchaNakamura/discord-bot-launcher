package fr.barodine.anael.discord.launcher;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.annotation.Nonnull;
import java.util.Set;

public class ExampleListener extends AbstractBaseListener {
    // Constructeurs
    public ExampleListener(long idBot) {
        super(idBot, Set.of(GatewayIntent.MESSAGE_CONTENT));
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        if (this.getJda().getGuilds().isEmpty()) {
            this.log("Example listener successfully executed, no guild or no text channel found in reach");
        } else {
            for (Guild guild : this.getJda().getGuilds()) {
                if (!guild.getTextChannels().isEmpty()) {
                    guild.getTextChannels().get(0).sendMessage("Example listener successfully executed").queue();
                }
            }
            this.log("Example listener successfully executed, message sent to first text channel of each guild");
        }
    }
}
