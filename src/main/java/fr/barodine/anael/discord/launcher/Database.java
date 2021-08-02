package fr.barodine.anael.discord.launcher;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* package private */ class Database {
    // Attributs
    private final String url;
    private final String user;
    private final String passwd;

    // Constructeurs
    public Database(@Nonnull final String url, @Nonnull final String user, @Nonnull final String passwd) {
        this.url = url;
        this.user = user;
        this.passwd = passwd;
    }

    // Getteurs
    public Connection getConnection() throws SQLException {
        // Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(
                url,
                user,
                passwd
        );
    }

    // Méthodes
    public boolean testConnexion() {
        try (Connection connection = this.getConnection()) {
            return connection.isValid(0);
        } catch (SQLException sqlException) {
            return false;
        }
    }
}
