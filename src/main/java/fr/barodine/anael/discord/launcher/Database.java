package fr.barodine.anael.discord.launcher;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* package private */ class Database implements AutoCloseable {
    // Attributs
    private final Connection connection;

    // Constructeurs
    public Database(
            @Nonnull final String url,
            @Nonnull final String user,
            @Nonnull final String passwd
    ) throws SQLException {
        // Class.forName("com.mysql.jdbc.Driver");
        this.connection = DriverManager.getConnection(
                url,
                user,
                passwd
        );
    }

    // Getteurs
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void close() throws SQLException {
        this.connection.close();
    }
}
