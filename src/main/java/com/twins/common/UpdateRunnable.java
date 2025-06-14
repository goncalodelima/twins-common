package com.twins.common;

import com.twins.common.model.user.User;
import com.twins.common.model.user.service.UserFoundationService;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class UpdateRunnable extends BukkitRunnable {

    private final UserFoundationService userService;

    public UpdateRunnable(UserFoundationService userService) {
        this.userService = userService;
    }

    @Override
    public void run() {

        Map<UUID, User> users = userService.getPendingUpdates();

        int size = users.size();

        if (size == 0) {
            return;
        }

        Map<UUID, User> usersCopy = new HashMap<>((int) (size / 0.75f) + 1); // load factor is 75%. let's avoid this additional load
        usersCopy.putAll(users);

        userService.update(usersCopy.values()); // It is necessary to make the copy because the update occurs in another thread and we have to control the number of users saved
        users.clear(); // safe, because pendingUpdates#add is only executed in the Main Thread that is responsible for calling this method as well

    }

}
