package com.twins.common.model.user.adapter;

import com.minecraftsolutions.database.adapter.DatabaseAdapter;
import com.minecraftsolutions.database.executor.DatabaseQuery;
import com.twins.common.model.user.LanguageType;
import com.twins.common.model.user.User;
import com.twins.common.util.UUIDConverter;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserAdapter implements DatabaseAdapter<User> {

    @Override
    public User adapt(DatabaseQuery databaseQuery) {

        UUID uuid = UUIDConverter.convert((byte[]) databaseQuery.get("uuid"));
        String nickname = (String) databaseQuery.get("nickname");
        LocalDateTime lastLoginDate = (LocalDateTime) databaseQuery.get("lastLoginDate");
        String language = (String) databaseQuery.get("languageType");
        LanguageType languageType = LanguageType.valueOf(language);
        boolean forceLanguage = (boolean) databaseQuery.get("forceLanguage");

        return new User(uuid, nickname, lastLoginDate, languageType, forceLanguage);
    }

}
