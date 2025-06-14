package com.twins.common.command;

import com.twins.common.api.events.UserLanguageUpdateEvent;
import com.twins.common.model.user.LanguageType;
import com.twins.common.model.user.User;
import com.twins.common.model.user.service.UserFoundationService;
import com.twins.common.util.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class LanguageCommand implements CommandExecutor {

    private final UserFoundationService userService;
    
    private final Configuration lang;

    public LanguageCommand(UserFoundationService userService, Configuration lang) {
        this.userService = userService;
        this.lang = lang;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player player)) {
            return false;
        }

        User user = userService.get(player.getUniqueId());

        if (user == null) {
            player.kickPlayer("§cTry logging in. If you can't, ask an administrator at www.discord.twinsmc.com.");
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(lang.getString(user.getLanguageType(), "line0-languageType"));
            player.sendMessage(lang.getString(user.getLanguageType(), "line1-languageType"));
            player.sendMessage("§f/lang §7" + Arrays.toString(LanguageType.values()));
            return false;
        }

        for (LanguageType type : LanguageType.values()) {

            if (args[0].equalsIgnoreCase(type.name())) {

                if (user.getLanguageType() == type) {
                    player.sendMessage(lang.getString(type, "already-languageType"));
                } else {
                    user.setLanguageType(type);
                    user.setForceLanguage(false);
                    userService.update(user);
                    Bukkit.getPluginManager().callEvent(new UserLanguageUpdateEvent(user));
                    player.sendMessage(lang.getString(type, "changed-languageType"));
                }

                return true;
            }

        }

        if (args[0].equalsIgnoreCase("force")) {

            if (user.isForceLanguage()) {
                user.setForceLanguage(false);
                player.sendMessage(lang.getString(user.getLanguageType(), "force-LanguageType1"));
            } else {
                user.setForceLanguage(true);
                player.sendMessage(lang.getString(user.getLanguageType(), "force-LanguageType"));
            }

            userService.update(user);
            return true;
        }

        player.sendMessage(lang.getString(user.getLanguageType(), "line0-languageType"));
        player.sendMessage(lang.getString(user.getLanguageType(), "line1-languageType"));
        player.sendMessage("§f/lang §7" + Arrays.toString(LanguageType.values()));
        return false;
    }

}
