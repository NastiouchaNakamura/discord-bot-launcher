package fr.barodine.anael.discord.launcher;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractBaseListener extends ListenerAdapter {
    // Attributs
    private final long idBot;
    private JDA jda;
    private final Map<String, String> variables;
    private final Set<GatewayIntent> gatewayIntents;

    // Constructeurs
    public AbstractBaseListener(long idBot, @Nonnull Set<GatewayIntent> gatewayIntents) {
        this.idBot = idBot;
        this.variables = new HashMap<>();
        this.gatewayIntents = gatewayIntents;
        String prefix = "DISCORD_BOT_LAUNCHER_BOT_" + this.idBot + "_";
        for (Map.Entry<String, String> variable : System.getenv().entrySet()) {
            if (variable.getKey().startsWith(prefix)) {
                variables.put(variable.getKey().substring(prefix.length()), variable.getValue());
            }
        }
    }

    // Getteurs
    public long getIdBot() {
        return this.idBot;
    }

    public JDA getJda() {
        return this.jda;
    }

    public Map<String, String> getVariables() {
        return this.variables;
    }

    public String getVariable(@Nonnull final String key) {
        return this.variables.get(key);
    }

    public Set<GatewayIntent> getGatewayIntents() {
        return this.gatewayIntents;
    }

    // Setteurs
    /* package private */ void setJda(JDA jda) {
        this.jda = jda;
    }

    // Méthodes
    public void log(@Nonnull final String message) {
        Launcher.logger.log(this.idBot, message);
    }
}
