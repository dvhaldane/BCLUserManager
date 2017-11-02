package com.grumpycraft;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import java.util.HashMap;
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


        //If they're a donor and do not have any chunks assigned, then assign and kick to update
        if (player.hasPermission("group.donor"))
        {
            if (db.getChunks(playerUUID) == 0)
            {
                plugin.getLogger().info(player.getDisplayName() + " had chunks added");
                db.setChunks(playerUUID);
                db.refreshCache();

                //Kick player to update chunks
                player.disconnect(new TextComponent("Your donor chunk loaders have been updated.  Please log back in.  Thanks for helping out our community!"));
            }
        }

        //If they're not a donor and are still in the database
        if (db.chunkOwners.get(playerUUID.toString()) != null && !player.hasPermission("group.donor"))
        {
            plugin.getLogger().info(player.getDisplayName() + " had chunks removed");
            db.removeDonor(playerUUID);
            db.refreshCache();
        }


    }
}
