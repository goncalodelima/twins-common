package com.twins.common.api.events;

import com.twins.common.model.user.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserLanguageUpdateEvent extends Event {

    private final User user;
    private static final HandlerList HANDLERS = new HandlerList();

    public UserLanguageUpdateEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
