package com.grumpycraft;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.packet.Chat;

import java.util.UUID;

public class BungeeEventListener implements Listener
{
    BCLUserManager plugin;
    DBManager db;

    BungeeEventListener(BCLUserManager plugin, DBManager db)
    {
        this.db = db;
        this.plugin = plugin;
    }


    @EventHandler
    public void onPostLogin(PostLoginEvent event)
    {
        ProxiedPlayer player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String donorstatus = "No";
        String discordSync = "No";

        //If they're a donor and do not have any chunks assigned, then assign and kick to update
        if (player.hasPermission("group.donor"))
        {
            if (db.getChunks(playerUUID) == 0)
            {
                plugin.getLogger().info(player.getDisplayName() + " had chunks added");
                db.setChunks(playerUUID);
                db.refreshCache();
                TextComponent header = new TextComponent(ChatColor.MAGIC+ "##############################################");
                TextComponent message = new TextComponent(ChatColor.RED + "Donor status been updated.  Please log back in.");
                message.setBold( true );
                player.sendMessage(header);
                player.sendMessage(message);
                player.sendMessage(header);
            }
            else
            {
                //Send donor a message on login
                TextComponent message = new TextComponent("*** Welcome donor! Click here to view our donor guide ***" );
                message.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, "https://gitlab.com/GrumpyCraft/Support/wikis/Donor-Guide" ) );
                message.setColor(ChatColor.GOLD);
                message.setBold( true );
                player.sendMessage(message);
            }
        }


        //Send welcome messages when joins

        //Send welcome message on login
        TextComponent discord = new TextComponent("Need help? Click here to visit our discord." );
        discord.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, "http://discord.grumpycraft.com" ) );
        discord.setColor(ChatColor.DARK_AQUA);
        discord.setBold( true );
        player.sendMessage(discord);

        //Send welcome message on login
        TextComponent donor = new TextComponent("Help our nonprofit network and get cool perks by becoming a donor!  Click here!" );
        donor.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, "http://www.grumpycraft.com/#donate" ) );
        donor.setColor(ChatColor.GREEN);
        donor.setBold( true );
        player.sendMessage(donor);

        //Send welcome message on login
        TextComponent vote = new TextComponent("Please vote every day!  Click here to vote" );
        vote.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, "http://www.grumpycraft.com/" ) );
        vote.setColor(ChatColor.LIGHT_PURPLE);
        vote.setBold( true );
        player.sendMessage(vote);

        //Send header
        player.sendMessage("&bTest &cTest &dTest");

        //If they're not a donor and are still in the database
        if (db.chunkOwners.get(playerUUID.toString()) != null && !player.hasPermission("group.donor"))
        {

            plugin.getLogger().info(player.getDisplayName() + " had chunks removed");
            db.removeDonor(playerUUID);
            db.refreshCache();
        }


    }
}
