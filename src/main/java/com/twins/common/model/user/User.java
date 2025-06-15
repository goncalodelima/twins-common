package com.twins.common.model.user;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class User {

    private final UUID uuid;
    private String nickname;
    private LocalDateTime lastLoginDate;
    private LanguageType languageType;
    private boolean forceLanguage;
    private boolean online;

    public User(UUID uuid, String nickname, LocalDateTime lastLoginDate, LanguageType languageType, boolean forceLanguage) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.lastLoginDate = lastLoginDate;
        this.languageType = languageType;
        this.forceLanguage = forceLanguage;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public LanguageType getLanguageType() {
        return languageType;
    }

    public void setLanguageType(LanguageType languageType) {
        this.languageType = languageType;
    }

    public boolean isForceLanguage() {
        return forceLanguage;
    }

    public void setForceLanguage(boolean forceLanguage) {
        this.forceLanguage = forceLanguage;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(uuid, user.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }

}
