package com.twins.common;

import com.github.retrooper.packetevents.PacketEvents;
import com.minecraftsolutions.database.DatabaseType;
import com.minecraftsolutions.database.connection.DatabaseConnection;
import com.minecraftsolutions.database.credentials.impl.DatabaseCredentialsImpl;
import com.twins.common.command.LanguageCommand;
import com.twins.common.listener.LanguageListener;
import com.twins.common.listener.PlayerListener;
import com.twins.common.model.user.User;
import com.twins.common.model.user.service.UserFoundationService;
import com.twins.common.model.user.service.UserService;
import com.twins.common.util.Configuration;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.minecraftsolutions.database.Database;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CommonPlugin extends JavaPlugin {

    private Database datacenter;

    private ExecutorService asyncExecutor;

    private UserFoundationService userService;

    public static CommonPlugin INSTANCE;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        //Are all listeners read only?
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false)
                .checkForUpdates(true)
                .bStats(false);
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {

        INSTANCE = this;
        
        saveDefaultConfig();

        Configuration lang = new Configuration(this, "lang", "lang.yml");
        lang.saveDefaultConfig();

        datacenter = new DatabaseConnection(
                new DatabaseCredentialsImpl(DatabaseType.MYSQL,
                        getConfig().getString("database.host"),
                        "3306", getConfig().getString("database.database"),
                        getConfig().getString("database.user"),
                        getConfig().getString("database.password"),
                        getConfig().getString("database.file"))
        ).setup();

        asyncExecutor = Executors.newFixedThreadPool(1);

        userService = new UserService(getLogger(), datacenter, asyncExecutor);

        getServer().getPluginManager().registerEvents(new PlayerListener(userService), this);
        Bukkit.getPluginCommand("language").setExecutor(new LanguageCommand(userService, lang));

        new UpdateRunnable(userService).runTaskTimer(this, 20 * 36, 20 * 36);

        PacketEvents.getAPI().getEventManager().registerListener(new LanguageListener(this, userService));
        PacketEvents.getAPI().init();

    }

    @Override
    public void onDisable() {

        Map<UUID, User> users = userService.getPendingUpdates();

        if (!users.isEmpty()) {
            userService.updateOnDisable(users.values());
        }

        PacketEvents.getAPI().terminate();

        asyncExecutor.shutdown();

        try {
            // Wait for currently executing tasks to finish
            if (!asyncExecutor.awaitTermination(36, TimeUnit.SECONDS)) {
                // Force shutdown if tasks are not finished in the given time
                asyncExecutor.shutdownNow();
                // Wait for tasks to respond to being cancelled
                if (!asyncExecutor.awaitTermination(36, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate in the specified time.");
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            asyncExecutor.shutdownNow();
        }

        datacenter.close();

    }

    public UserFoundationService getUserService() {
        return userService;
    }

}
