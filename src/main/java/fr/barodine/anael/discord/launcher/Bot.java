package fr.barodine.anael.discord.launcher;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.lang.reflect.InvocationTargetException;

class Bot {
    // Attributs
    private final long id;
    private final JDA jda;
    private final TtListener listener;

    // Constructeurs
    public Bot(
            long id,
            @Nonnull final String discordToken,
            @Nonnull final Class<? extends TtListener> listenerClass
    ) throws LoginException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.id = id;
        this.jda = JDABuilder.createDefault(discordToken).build();
        this.listener = listenerClass.getConstructor(long.class, JDA.class).newInstance(id, this.jda);
    }

    // Getteurs
    public long getId() {
        return this.id;
    }

    // Méthodes
    public void launch() {
        this.jda.addEventListener(this.listener);
    }
}
