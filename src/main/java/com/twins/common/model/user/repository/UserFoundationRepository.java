package com.twins.common.model.user.repository;

import com.twins.common.model.user.User;

import java.util.Collection;
import java.util.UUID;

public interface UserFoundationRepository {

    void setup();

    void updateNickname(UUID uuid, String nickname);

    void insertOrUpdate(Collection<User> users);

    void insertOrUpdateOnDisable(Collection<User> users);

    User findOne(UUID uuid) throws Exception;

    void insert(User user) throws Exception;

}
