package com.grumpycraft;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import net.md_5.bungee.api.plugin.Plugin;

public class BCLUserManager extends Plugin
{

    public void onEnable()
    {
        DBManager db = new DBManager(this);
        db.refreshCache();

        getProxy().getInstance().getPluginManager().registerCommand(this, new ZeroChunksCommand(db));
        getProxy().getInstance().getPluginManager().registerCommand(this, new RefreshCacheCommand(db));
        getProxy().getPluginManager().registerListener(this, new BungeeEventListener(this, db));

        //No longer using the API from LuckPerms - using login events against a user cache
        //LuckPermsApi api = LuckPerms.getApi();
        //LuckPermsEventListener listener = new LuckPermsEventListener(this, api, db);


    }

}

