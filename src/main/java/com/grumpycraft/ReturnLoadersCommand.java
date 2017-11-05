package com.grumpycraft;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReturnLoadersCommand extends Command
{

    DBManager db;

    public ReturnLoadersCommand(DBManager db)
    {
        super("returnloaders");
        this.db = db;
    }

    @Override
    public void execute(CommandSender sender, String[] args)
    {
        if (sender instanceof ProxiedPlayer) {
            if (((ProxiedPlayer) sender).hasPermission("group.donor") && db.getChunks(((ProxiedPlayer) sender).getUniqueId()) > 0) {
                db.returnChunks(((ProxiedPlayer) sender).getUniqueId());
                TextComponent message = new TextComponent( "Your chunkloaders have been removed from servers and refunded to your balance." );
                message.setColor( ChatColor.GREEN );
                ((ProxiedPlayer) sender).sendMessage(message);
            }
        }
    }
}
