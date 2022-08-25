package fr.barodine.anael.discord.launcher;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.lang.reflect.InvocationTargetException;

/* package private */ class Bot {
    // Attributs
    private final long id;
    private final JDA jda;
    private final AbstractBaseListener listener;

    // Constructeurs
    public Bot(
            long id,
            @Nonnull final String discordToken,
            @Nonnull final Class<? extends AbstractBaseListener> listenerClass
    ) throws LoginException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.id = id;
        this.listener = listenerClass.getConstructor(long.class).newInstance(id);
        this.jda = JDABuilder
                .create(discordToken, this.listener.getGatewayIntents())
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS)
                .build();
        this.listener.setJda(this.jda);
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
