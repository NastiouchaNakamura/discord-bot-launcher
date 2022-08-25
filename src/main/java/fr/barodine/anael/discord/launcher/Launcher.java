package fr.barodine.anael.discord.launcher;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Launcher {
    /* package private */ static Logger logger;
    private static Fetcher fetcher;

    public static void launch() {
        // Vérification que les variables d'environnement sont paramétrées.
        String prefix = "DISCORD_BOT_LAUNCHER_MAIN_";

        String databaseUrl = System.getenv(prefix + "SQL_DATABASE_URL");
        if (databaseUrl == null) System.err.println("Main database URL environment variable '" + prefix + "SQL_DATABASE_URL" + "' is undefined or null");

        String databaseUser = System.getenv(prefix + "SQL_DATABASE_USER");
        if (databaseUser == null) System.err.println("Main database user environment variable '" + prefix + "SQL_DATABASE_USER" + "' is undefined or null");

        String databasePasswd = System.getenv(prefix + "SQL_DATABASE_PASSWD");
        if (databasePasswd == null) System.err.println("Main database password environment variable '" + prefix + "SQL_DATABASE_PASSWD" + "' is undefined or null");

        if (databaseUrl == null || databaseUser == null || databasePasswd == null) {
            System.err.println("Please specify all above environment variables");
            System.exit(1);
        }

        // Instantiation du logger et du fetcher.
        Database bddMySQL = new Database(
                databaseUrl,
                databaseUser,
                databasePasswd
        );

        if (!bddMySQL.testConnexion()) {
            if (bddMySQL.getSqlException() == null) {
                System.err.println("Unable to connect to database; no SQL exception message given");
            } else {
                System.err.println("Unable to connect to database; SQL exception message: " + bddMySQL.getSqlException());
            }
            System.exit(1);
        }

        logger = new Logger(bddMySQL);
        fetcher = new Fetcher(bddMySQL);

        // Récupération des bots.
        List<Bot> bots = new ArrayList<>();
        try {
            bots = fetcher.fetchBots();
        } catch (SQLException sqlException) {
            System.err.println("Unable to fetch bots configs from database");
            sqlException.printStackTrace();
        }

        // Lancement des bots.
        int launchedBots = 0;
        for (Bot bot : bots) {
            try {
                bot.launch();
                launchedBots++;
            } catch (Exception exception) {
                logger.log("Exception occurred when launching bot " + bot.getId() + ". " + exception.getClass().getName() + ": " + exception.getMessage());
            }
        }
        logger.log("Successfully launched " + launchedBots + " bots, failed to launch " + (bots.size() - launchedBots) + " bots");
    }
}
