package com.grumpycraft;

import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.event.EventBus;
import me.lucko.luckperms.api.event.log.LogPublishEvent;
import me.lucko.luckperms.api.event.user.UserLoadEvent;
import me.lucko.luckperms.api.event.user.track.UserDemoteEvent;
import me.lucko.luckperms.api.event.user.track.UserPromoteEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class EventListener {
    private final BCLUserManager plugin;
    private DBManager dbManager;

    public EventListener(BCLUserManager plugin, LuckPermsApi api, DBManager dbManager) {
        this.plugin = plugin;
        this.dbManager = dbManager;

        // get the LuckPerms event bus
        EventBus eventBus = api.getEventBus();

        eventBus.subscribe(UserPromoteEvent.class, this::onUserPromote);
        eventBus.subscribe(UserDemoteEvent.class, this::onUserDemote);

    }

    private void onUserPromote(UserPromoteEvent event) {
        // as we want to access the Bukkit API, we need to use the scheduler to jump back onto the main thread.
        plugin.getLogger().info("Logged promote event");
        if (event.getGroupTo().get().equalsIgnoreCase("donor"))
        {

            plugin.getLogger().info(event.getUser().getName() + " was promoted to donor.");

            //Add rows in database for user
            this.dbManager.setChunks(plugin, event.getUser().getUuid());

            TextComponent message = new TextComponent( "You are now a donor! Thank you for donating!  Click this text to view our donor guide." );
            message.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, "https://gitlab.com/GrumpyCraft/Support/wikis/Donor-Guide" ) );
            message.setColor( ChatColor.GREEN );
            message.setBold( true );

            //Send message to donor to tell them to log out and log back in.
            plugin.getProxy().getPlayer(event.getUser().getUuid()).sendMessage(message);
        }
    }

    private void onUserDemote(UserDemoteEvent event) {

        // as we want to access the Bukkit API, we need to use the scheduler to jump back onto the main thread.
        plugin.getLogger().info("Logged demote event");
        if (event.getGroupTo().get().equalsIgnoreCase("default") && event.getGroupFrom().get().equalsIgnoreCase("donor"))
        {

            TextComponent message = new TextComponent( "Your donation status has expired.  Click this text to resubscribe." );
            message.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, "http://www.grumpycraft.com/shop/m/27792817/page" ) );
            message.setColor( ChatColor.RED );
            message.setBold( true );

            plugin.getLogger().info(event.getUser().getName() + " was demoted from donor.");

            //Add rows in database for user
            dbManager.zeroChunks(plugin, event.getUser().getUuid());

            //Send message to donor to tell them to log out and log back in.
            plugin.getProxy().getPlayer(event.getUser().getUuid()).sendMessage(message);

        }

    }

}
