package com.grumpycraft;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class BCLUserCommand extends Command
{

    public BCLUserCommand()
    {
        super("resetchunloaders");
    }

    public void execute(CommandSender commandSender, String[] strings) {

        db.zeroChunks(this, );
    }
}
