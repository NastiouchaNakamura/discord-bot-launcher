package fr.barodine.anael.discord.launcher;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public abstract class AbstractBaseListener extends ListenerAdapter {
    // Attributs
    private final long idBot;
    private final JDA jda;

    // Constructeurs
    public AbstractBaseListener(long idBot, @Nonnull final JDA jda) {
        this.idBot = idBot;
        this.jda = jda;
    }

    // Getteurs
    public long getIdBot() {
        return this.idBot;
    }

    public JDA getJda() {
        return this.jda;
    }

    // Méthodes
    public void log(@NotNull final String message) {
        Launcher.logger.log(this.idBot, message);
    }
}
