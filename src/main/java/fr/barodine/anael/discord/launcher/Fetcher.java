package fr.barodine.anael.discord.launcher;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/* package private */ class Fetcher {
    // Attributs
    public final Database bdd;

    // Constructeurs
    public Fetcher(@Nonnull final Database bdd) {
        this.bdd = bdd;
    }

    // Méthodes
    public List<Bot> fetchBots() throws SQLException {
        List<Bot> bots = new ArrayList<>();
        
        try (Connection connection = this.bdd.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id, discord_bot_token, listener_class FROM ttbot_bots;"
            );
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                try {
                    Class<?> listenerClass = Class.forName(result.getString(3));

                    if (!AbstractBaseListener.class.isAssignableFrom(listenerClass)) {
                        Launcher.logger.log("Bot " + result.getString(1) + ": Class '" + result.getString(3) + "' not extending " + AbstractBaseListener.class.getName());
                        continue;
                    }

                    bots.add(new Bot(
                            result.getLong(1),
                            result.getString(2),
                            (Class<? extends AbstractBaseListener>) listenerClass
                    ));
                } catch (ClassNotFoundException e) {
                    Launcher.logger.log("Bot " + result.getString(1) + ": Class '" + result.getString(3) + "' not found");
                } catch (Exception e) {
                    Launcher.logger.log("Bot " + result.getString(1) + ": Search of class '" + result.getString(3) + "' caused exception. " + e.getClass() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }

            result.close();
            preparedStatement.close();

            Launcher.logger.log("Successfully fetched " + bots.size() + " bots");
        }

        return bots;
    }
}
