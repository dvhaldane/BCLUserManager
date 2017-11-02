package com.grumpycraft;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class RefreshCacheCommand extends Command
{
    DBManager db;

    public RefreshCacheCommand(DBManager db)
    {
        super("refreshcache");
        this.db = db;
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if (sender instanceof ProxiedPlayer)
        {
            if (((ProxiedPlayer) sender).hasPermission("group.admin"))
            {

                if (args.length == 0)
                {
                    db.refreshCache();
                }
            }
        }
        else
        {
            if (args.length == 0)
            {
                db.refreshCache();
            }
        }
    }
}
