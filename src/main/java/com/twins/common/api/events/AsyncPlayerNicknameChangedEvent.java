package com.twins.common.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class AsyncPlayerNicknameChangedEvent extends Event {

    private final UUID uuid;
    private final String oldNickname;
    private final String newNickname;
    private static final HandlerList HANDLERS = new HandlerList();

    public AsyncPlayerNicknameChangedEvent(UUID uuid, String oldNickname, String newNickname) {
        this.uuid = uuid;
        this.oldNickname = oldNickname;
        this.newNickname = newNickname;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getOldNickname() {
        return oldNickname;
    }

    public String getNewNickname() {
        return newNickname;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
