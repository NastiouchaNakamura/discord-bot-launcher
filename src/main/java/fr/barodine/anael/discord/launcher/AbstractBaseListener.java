package fr.barodine.anael.discord.launcher;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBaseListener extends ListenerAdapter {
    // Attributs
    private final long idBot;
    private final JDA jda;
    private final Map<String, String> variables;

    // Constructeurs
    public AbstractBaseListener(long idBot, @Nonnull final JDA jda) {
        this.idBot = idBot;
        this.jda = jda;
        this.variables = new HashMap<>();
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

    // Méthodes
    public void log(@NotNull final String message) {
        Launcher.logger.log(this.idBot, message);
    }
}
