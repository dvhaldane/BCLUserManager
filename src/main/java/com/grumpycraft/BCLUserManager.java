package com.grumpycraft;
import net.md_5.bungee.api.plugin.Plugin;

public class BCLUserManager extends Plugin
{

    public void onEnable()
    {
        DBManager db = new DBManager(this);
        db.refreshCache();

        getProxy().getInstance().getPluginManager().registerCommand(this, new ReturnLoadersCommand(db));
        getProxy().getInstance().getPluginManager().registerCommand(this, new RefreshCacheCommand(db));
        getProxy().getPluginManager().registerListener(this, new BungeeEventListener(this, db));

    }

}

