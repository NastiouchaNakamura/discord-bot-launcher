package fr.barodine.anael.discord.launcher;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/* package private */ class Logger {
    // Attributs
    private final Database bdd;

    // Constructeurs
    public Logger(@Nonnull final Database bdd) {
        this.bdd = bdd;
    }

    // Méthodes
    public void log(final long idBot, @NotNull final String message) {
        try {
            PreparedStatement preparedStatement = this.bdd.getConnection().prepareStatement(
                    "INSERT INTO ttbot_bots_log (id_bot, message, time) VALUES (?, ?, ?);"
            );
            preparedStatement.setLong(1, idBot);
            preparedStatement.setString(2, message);
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    void log(@NotNull final String message) {
        try {
            PreparedStatement preparedStatement = this.bdd.getConnection().prepareStatement(
                    "INSERT INTO ttbot_global_log (message, time) VALUES (?, ?);"
            );
            preparedStatement.setString(1, message);
            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
