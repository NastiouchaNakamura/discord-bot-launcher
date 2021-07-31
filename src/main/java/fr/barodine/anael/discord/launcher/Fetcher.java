package fr.barodine.anael.discord.launcher;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class Fetcher {
    // Attributs
    public final Database bdd;

    // Constructeurs
    public Fetcher(@Nonnull final Database bdd) {
        this.bdd = bdd;
    }

    // Méthodes
    public List<Bot> fetchBots() throws SQLException {
        PreparedStatement preparedStatement = this.bdd.getConnection().prepareStatement(
                "SELECT id, discord_bot_token, listener_class FROM ttbot_bots;"
        );
        ResultSet result = preparedStatement.executeQuery();

        List<Bot> bots = new ArrayList<>();
        while (result.next()) {
            try {
                Class<?> listenerClass = Class.forName(result.getString(3));

                if (!AbstractBaseListener.class.isAssignableFrom(listenerClass)) {
                    Launcher.logger.log("Class '" + result.getString(3) + "' not extending " + AbstractBaseListener.class.getName());
                    continue;
                }

                bots.add(new Bot(
                        result.getLong(1),
                        result.getString(2),
                        (Class<? extends AbstractBaseListener>) Class.forName(result.getString(3))
                ));
            } catch (ClassNotFoundException e) {
                Launcher.logger.log("Class '" + result.getString(3) + "' not found");
            } catch (Exception e) {
                Launcher.logger.log("Search of class '" + result.getString(3) + "' caused exception. " + e.getClass() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        result.close();
        preparedStatement.close();

        return bots;
    }
}
