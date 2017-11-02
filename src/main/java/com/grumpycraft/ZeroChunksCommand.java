package com.grumpycraft;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ZeroChunksCommand extends Command
{

    DBManager db;

    public ZeroChunksCommand(DBManager db)
    {
        super("zerochunks");
        this.db = db;
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if (sender instanceof ProxiedPlayer) {
            if (((ProxiedPlayer) sender).hasPermission("group.donor") && db.getChunks(((ProxiedPlayer) sender).getUniqueId()) > 0) {
                db.zeroChunks(((ProxiedPlayer) sender).getUniqueId());
            }
        }
    }
}
