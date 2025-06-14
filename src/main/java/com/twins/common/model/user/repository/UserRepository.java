package com.twins.common.model.user.repository;

import com.minecraftsolutions.database.Database;
import com.minecraftsolutions.database.executor.DatabaseExecutor;
import com.twins.common.model.user.User;
import com.twins.common.model.user.adapter.UserAdapter;
import com.twins.common.util.UUIDConverter;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRepository implements UserFoundationRepository {

    private final Logger logger;

    private final Database database;

    private final ExecutorService asyncExecutor;

    private final UserAdapter adapter = new UserAdapter();

    public UserRepository(Logger logger, Database database, ExecutorService asyncExecutor) {
        this.logger = logger;
        this.database = database;
        this.asyncExecutor = asyncExecutor;
    }

    @Override
    public void setup() {
        try (DatabaseExecutor executor = database.execute()) {
            executor
                    .query("CREATE TABLE IF NOT EXISTS common_user (uuid BINARY(16) PRIMARY KEY, nickname VARCHAR(16), languageType CHAR(2), forceLanguage BOOLEAN)")
                    .write();
        }
    }

    @Override
    public void updateNickname(UUID uuid, String nickname) {
        try (DatabaseExecutor executor = database.execute()) {
            executor
                    .query("UPDATE common_user SET nickname = ? WHERE uuid = ?")
                    .write(statement -> {
                        statement.set(1, nickname);
                        statement.set(2, UUIDConverter.convert(uuid));
                    });
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to update common user nickname", e);
        }
    }

    @Override
    public void insertOrUpdate(Collection<User> users) {
        CompletableFuture.runAsync(() -> {
            try (DatabaseExecutor executor = database.execute()) {
                executor
                        .query("INSERT INTO common_user (uuid, nickname, languageType, forceLanguage) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE nickname = VALUES(nickname), languageType = VALUES(languageType), forceLanguage = VALUES(forceLanguage)")
                        .batch(users, (user, statement) -> {
                            statement.set(1, UUIDConverter.convert(user.getUuid()));
                            statement.set(2, user.getNickname());
                            statement.set(3, user.getLanguageType().name());
                            statement.set(4, user.isForceLanguage());
                        });
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Failed to insert or update common users data", e);
            }
        }, asyncExecutor);
    }

    @Override
    public void insertOrUpdateOnDisable(Collection<User> users) {
        try (DatabaseExecutor executor = database.execute()) {

            executor
                    .query("INSERT INTO common_user (uuid, nickname, languageType, forceLanguage) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE nickname = VALUES(nickname), languageType = VALUES(languageType), forceLanguage = VALUES(forceLanguage)")
                    .batch(users, (user, statement) -> {
                        statement.set(1, UUIDConverter.convert(user.getUuid()));
                        statement.set(2, user.getNickname());
                        statement.set(3, user.getLanguageType().name());
                        statement.set(4, user.isForceLanguage());
                    });

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to batch insert/update common user data", e);
        }
    }

    @Override
    public User findOne(UUID uuid) {
        try (DatabaseExecutor executor = database.execute()) {

            return executor
                    .query("SELECT * from common_user where uuid = ?")
                    .readOne(statement -> statement.set(1, UUIDConverter.convert(uuid)), adapter)
                    .orElse(null);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to retrieve common user data", e);
            throw e;
        }
    }

    @Override
    public void insert(User user) {
        try (DatabaseExecutor executor = database.execute()) {

            executor
                    .query("INSERT INTO common_user (uuid, nickname, languageType, forceLanguage) VALUES (?,?,?,?)")
                    .write(statement -> {
                        statement.set(1, UUIDConverter.convert(user.getUuid()));
                        statement.set(2, user.getNickname());
                        statement.set(3, user.getLanguageType().name());
                        statement.set(4, user.isForceLanguage());
                    });

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to insert common user data", e);
            throw e;
        }
    }

}
