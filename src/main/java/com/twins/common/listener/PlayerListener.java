package com.twins.common.listener;

import com.twins.common.api.events.AsyncPlayerNicknameChangedEvent;
import com.twins.common.model.user.User;
import com.twins.common.model.user.service.UserFoundationService;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final UserFoundationService userService;

    public PlayerListener(UserFoundationService userService) {
        this.userService = userService;
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {

        User user = userService.getPendingUpdates().get(event.getUniqueId());

        if (user != null) {
            userService.put(user);
            return;
        }

        user = userService.findOrInsert(event.getUniqueId(), event.getName());

        if (user == null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cAn error occurred while trying to join the server. Try again...");
            return;
        }

        userService.put(user);

        final String oldName = user.getNickname();

        if (!oldName.equals(event.getName())) {
            user.setNickname(event.getName());
            Bukkit.getPluginManager().callEvent(new AsyncPlayerNicknameChangedEvent(user.getUuid(), oldName, event.getName()));
        }

    }

    @EventHandler
    public void onAsyncPlayerNicknameChangedEvent(AsyncPlayerNicknameChangedEvent event) { // code called in AsyncPlayerPreLoginEvent(priority=EventPriority.NORMAL)
        userService.updateNickname(event.getUuid(), event.getNewNickname());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {

        User user = userService.get(event.getPlayer().getUniqueId());

        if (user == null) {
            event.getPlayer().kickPlayer("§cAn error occurred while trying to join the server. Try again...");
            return;
        }

        user.setNickname(event.getPlayer().getName());
        user.setOnline(true);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {

        User user = userService.remove(event.getPlayer().getUniqueId());

        if (user == null) {
            return;
        }

        user.setOnline(false);
    }

}
