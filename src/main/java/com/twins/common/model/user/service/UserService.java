package com.twins.common.model.user.service;

import com.minecraftsolutions.database.Database;
import com.twins.common.model.user.LanguageType;
import com.twins.common.model.user.User;
import com.twins.common.model.user.repository.UserFoundationRepository;
import com.twins.common.model.user.repository.UserRepository;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class UserService implements UserFoundationService {

    private final UserFoundationRepository userRepository;

    private final Map<UUID, User> cache = new ConcurrentHashMap<>();

    private final Map<UUID, User> pendingUpdates = new ConcurrentHashMap<>();

    public UserService(Logger logger, Database database, ExecutorService asyncExecutor) {
        userRepository = new UserRepository(logger, database, asyncExecutor);
        userRepository.setup();
    }

    @Override
    public void put(User user) {
        cache.put(user.getUuid(), user);
    }

    @Override
    public void update(User user) {
        pendingUpdates.put(user.getUuid(), user);
    }

    @Override
    public void updateNickname(UUID uuid, String nickname) {
        userRepository.updateNickname(uuid, nickname);
    }

    @Override
    public void update(Collection<User> users) {
        userRepository.insertOrUpdate(users);
    }

    @Override
    public void updateOnDisable(Collection<User> users) {
        userRepository.insertOrUpdateOnDisable(users);
    }

    @Override
    public User remove(UUID uuid) {
        return cache.remove(uuid);
    }

    @Nullable
    @Override
    public User get(UUID uuid) {
        return cache.get(uuid);
    }

    @Nullable
    @Override
    public User findOrInsert(UUID uuid, String nickname) {

        try {

            User userRepositoryOne = userRepository.findOne(uuid);

            if (userRepositoryOne == null) {

                userRepositoryOne = new User(uuid, nickname, LanguageType.EN, true);

                try {
                    userRepository.insert(userRepositoryOne);
                } catch (Exception e) {
                    return null;
                }

            }

            return userRepositoryOne;
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public Map<UUID, User> getPendingUpdates() {
        return pendingUpdates;
    }

}
