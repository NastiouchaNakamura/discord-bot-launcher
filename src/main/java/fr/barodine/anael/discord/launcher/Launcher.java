package fr.barodine.anael.discord.launcher;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Launcher {
    /* package private */ static Logger logger;
    private static Fetcher fetcher;

    public static void launch() {
        // Vérification que les variables d'environnement sont paramétrées.
        String databaseDomain = System.getenv("DISCORD_BOT_LAUNCHER_MAIN_SQL_DATABASE_DOMAIN");
        if (databaseDomain == null) System.err.println("Main database domain environment variable '" + "DISCORD_BOT_LAUNCHER_MAIN_SQL_DATABASE_DOMAIN" + "' is undefined or null");

        String databasePort = System.getenv("DISCORD_BOT_LAUNCHER_MAIN_SQL_DATABASE_PORT");
        if (databasePort == null) System.err.println("Main database port environment variable '" + "DISCORD_BOT_LAUNCHER_MAIN_SQL_DATABASE_PORT" + "' is undefined or null");

        String databaseDb = System.getenv("DISCORD_BOT_LAUNCHER_MAIN_SQL_DATABASE_DB");
        if (databaseDb == null) System.err.println("Main database target database environment variable '" + "DISCORD_BOT_LAUNCHER_MAIN_SQL_DATABASE_DB" + "' is undefined or null");

        String databaseUser = System.getenv("DISCORD_BOT_LAUNCHER_MAIN_SQL_DATABASE_USER");
        if (databaseUser == null) System.err.println("Main database user environment variable '" + "DISCORD_BOT_LAUNCHER_MAIN_SQL_DATABASE_USER" + "' is undefined or null");

        String databasePasswd = System.getenv("DISCORD_BOT_LAUNCHER_MAIN_SQL_DATABASE_PASSWD");
        if (databasePasswd == null) System.err.println("Main database password environment variable '" + "DISCORD_BOT_LAUNCHER_MAIN_SQL_DATABASE_PASSWD" + "' is undefined or null");

        if (databaseDomain == null || databasePort == null || databaseDb == null || databaseUser == null || databasePasswd == null) {
            System.err.println("Please specify all above environment variables");
            System.exit(1);
        }

        // Instantiation du logger et du fetcher.
        try {
            Database bddMySQL = new Database(
                    databaseDomain,
                    databasePort,
                    databaseDb,
                    databaseUser,
                    databasePasswd
            );

            logger = new Logger(bddMySQL);
            fetcher = new Fetcher(bddMySQL);
        } catch (SQLException sqlException) {
            System.err.println("Unable to connect to database");
            sqlException.printStackTrace();
            System.exit(1);
        }

        // Récupération des bots.
        List<Bot> bots = new ArrayList<>();
        try {
            bots = fetcher.fetchBots();
        } catch (SQLException sqlException) {
            System.err.println("Unable to fetch bots configs from database");
            sqlException.printStackTrace();
        }

        // Lancement des bots.
        for (Bot bot : bots) {
            try {
                bot.launch();
            } catch (Exception exception) {
                logger.log(0, "Exception occurred when launching bot " + bot.getId() + ". " + exception.getClass().getName() + ": " + exception.getMessage());            }
        }
    }
}
