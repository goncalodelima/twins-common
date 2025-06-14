package com.twins.common.model.user.service;

import com.twins.common.model.user.User;

import javax.annotation.Nullable;
import java.util.*;

public interface UserFoundationService {

    void put(User user);

    void update(User user);

    void updateNickname(UUID uuid, String nickname);

    void update(Collection<User> users);

    void updateOnDisable(Collection<User> users);

    User remove(UUID uuid);

    @Nullable
    User get(UUID uuid);

    @Nullable
    User findOrInsert(UUID uuid, String nickname);

    Map<UUID, User> getPendingUpdates();

}
