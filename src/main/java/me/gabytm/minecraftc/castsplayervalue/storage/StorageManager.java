package me.gabytm.minecraftc.castsplayervalue.storage;

import me.gabytm.minecraftc.castsplayervalue.CastsPlayerValuePlugin;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class StorageManager {

    private final CastsPlayerValuePlugin plugin;
    private final Logger logger;

    private Connection connection;
    private String uri;

    public StorageManager(@NotNull final CastsPlayerValuePlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getSLF4JLogger();
        connect();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createDatabase() {
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();

        final var database = new File(plugin.getDataFolder(), "database.db");

        if (!database.exists()) {
            try {
                database.createNewFile();
            } catch (IOException e) {
                logger.error("Could not create " + database.getAbsolutePath(), e);
            }
        }

        this.uri = "jdbc:sqlite:" + database.toPath();
    }

    private void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }

            createDatabase();

            try {
                Class.forName("org.sqlite.JDBC");
                this.connection = DriverManager.getConnection(uri);

                if (connection != null) {
                    createTable();
                }
            } catch (ClassNotFoundException e) {
                logger.error("Could not find class org.sqlite.JDBC", e);
            }
        } catch (SQLException e) {
            logger.error("Could not connect to the database", e);
        }
    }

    private void createTable() {
        try (final var table = Query.CREATE_TABLE.prepare(connection)) {
            table.execute();
        } catch (SQLException e) {
            logger.error("Could not create the tables", e);
        }
    }

    @NotNull
    public List<Pair<UUID, Integer>> getTop() {
        connect();
        final var top = new ArrayList<Pair<UUID, Integer>>();

        try (final var select = Query.SELECT_TOP.prepare(connection)) {
            final var result = select.executeQuery();

            while (result.next()) {
                final var uuid = UUID.fromString(result.getString("uuid"));
                final var value = result.getInt("player_value");
                top.add(Pair.of(uuid, value));
            }

        } catch (SQLException e) {
            logger.error("Could not get top", e);
        }

        return top;
    }

    @NotNull
    public CompletableFuture<Integer> getValue(@NotNull final UUID uuid) {
        connect();
        final var uuidString = uuid.toString();

        try (final var select = Query.SELECT_VALUE.prepare(connection)) {
            select.setString(1, uuidString);

            final var result = select.executeQuery();
            var value = 0;

            if (result.next()) {
                value = result.getInt("player_value");
            } else {
                value = plugin.config().startingValue();
                updateValue(uuid, value); // Handle missing players, set their value to 'starting value'
            }

            return CompletableFuture.completedFuture(value);
        } catch (SQLException e) {
            logger.error("Could not get the value of " + uuidString, e);
            return CompletableFuture.completedFuture(null);
        }
    }

    public void updateValue(@NotNull final UUID uuid, final int value) {
        connect();
        final var uuidString = uuid.toString();

        try (final var update = Query.UPDATE_VALUE.prepare(connection)) {
            update.setString(1, uuidString);
            update.setInt(2, value);
            update.executeUpdate();
        } catch (SQLException e) {
            logger.error("Could not set %s's value to %d".formatted(uuidString, value), e);
        }
    }

    private enum Query {

        CREATE_TABLE,
        SELECT_TOP,
        SELECT_VALUE,
        UPDATE_VALUE;

        private final String query;

        Query() {
            String temp = null;

            try (final var in = getClass().getResourceAsStream("/sql/%s.sql".formatted(name().toLowerCase()))) {
                temp = new String(in.readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.query = temp;
        }

        @NotNull
        public PreparedStatement prepare(@NotNull final Connection connection) throws SQLException {
            return connection.prepareStatement(this.query);
        }

    }

}
