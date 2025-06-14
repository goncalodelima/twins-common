package com.twins.common.listener;

import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSettings;
import com.twins.common.CommonPlugin;
import com.twins.common.api.events.UserLanguageUpdateEvent;
import com.twins.common.model.user.LanguageType;
import com.twins.common.model.user.User;
import com.twins.common.model.user.service.UserFoundationService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class LanguageListener extends SimplePacketListenerAbstract {

    private final CommonPlugin plugin;

    private final UserFoundationService userService;

    private final Map<String, LanguageType> languageMap = new HashMap<>();

    public LanguageListener(CommonPlugin plugin, UserFoundationService userService) {

        this.plugin = plugin;
        this.userService = userService;

        languageMap.put("pt", LanguageType.PT);
        languageMap.put("es", LanguageType.ES);
        languageMap.put("fr", LanguageType.FR);
        languageMap.put("de", LanguageType.DE);
        languageMap.put("nl", LanguageType.NL);
        languageMap.put("it", LanguageType.IT);
        languageMap.put("ru", LanguageType.RU);
        languageMap.put("lb", LanguageType.LU);

    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {

        if (event.getPacketType() == PacketType.Play.Client.CLIENT_SETTINGS) {

            Player player = event.getPlayer();

            if (player == null) {
                return;
            }

            User user = userService.get(player.getUniqueId());

            if (user == null) {
                return;
            }

            WrapperPlayClientSettings settings = new WrapperPlayClientSettings(event);

            if (user.isForceLanguage() && !settings.getLocale().startsWith(user.getLanguageType().name())) {

                String locale = settings.getLocale().substring(0, 2);
                user.setLanguageType(languageMap.getOrDefault(locale, LanguageType.EN));

                new BukkitRunnable() {
                    @Override
                    public void run() {

                        if (user.isOnline()) { // code executed on the next tick. ensure only update the player if he is still online
                            userService.update(user); // sync pendingUpdates#put
                            Bukkit.getPluginManager().callEvent(new UserLanguageUpdateEvent(user));
                        }

                    }
                }.runTask(plugin);

            }

        }

    }

}
