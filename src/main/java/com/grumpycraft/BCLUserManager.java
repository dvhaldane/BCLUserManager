package com.grumpycraft;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BCLUserManager extends Plugin
{

    public void onEnable()
    {
        // You should not put an enable message in your plugin.
        // BungeeCord already does so
        getLogger().info("Yay! It loads!");
        DBManager dbManager = new DBManager();

        LuckPermsApi api = LuckPerms.getApi();

        getProxy().getPluginManager().registerCommand(this, new BCLUserCommand());
        EventListener listener = new EventListener(this, api);
    }

}

