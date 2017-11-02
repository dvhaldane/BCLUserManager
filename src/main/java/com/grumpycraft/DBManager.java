package com.grumpycraft;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DBManager
{
    private Connection connection;
    private String host, database, username, password;
    private int port;
    Statement statement;
    BCLUserManager plugin;

    ConcurrentHashMap<String, Integer> chunkOwners = new ConcurrentHashMap<>();

    public DBManager(BCLUserManager plugin)
    {
        this.plugin = plugin;
        host = "localhost";
        port = 3306;
        database = "bcl";
        username = "root";
        password = "";

        try
        {
            openConnection();
            statement = connection.createStatement();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

    }



    public void openConnection() throws SQLException, ClassNotFoundException
    {
        if (connection != null && !connection.isClosed())
        {
            return;
        }

        synchronized (this)
        {
            if (connection != null && !connection.isClosed())
            {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
        }

    }

    public int getChunks (UUID uuid)
    {
        try
        {
            ResultSet res = this.statement.executeQuery("SELECT onlineonly FROM bcl_playersdata WHERE pid = " + UUIDtoHexString(uuid));
            if (res.next())
            {
                return res.getInt("onlineonly");
            }
        }
        catch (Exception e)
        {
            plugin.getLogger().info(e.toString());
        }
        return 0;
    }

    public void setChunks(UUID uuid)
    {
        try
        {
            this.statement.executeUpdate("INSERT INTO bcl_playersdata (pid, alwayson, onlineonly) VALUES (" + UUIDtoHexString(uuid) + " , 0, 27) ON DUPLICATE KEY UPDATE onlineonly = 27;");
        }
        catch (Exception e)
        {
            plugin.getLogger().info(e.toString());
        }
    }

    public void zeroChunks(UUID uuid)
    {
        try
        {
            this.statement.executeUpdate("UPDATE bcl_playersdata SET onlineonly = 0, alwayson = 0 WHERE pid = " + UUIDtoHexString(uuid) + ";");
        }
        catch (Exception e)
        {
            plugin.getLogger().info(e.toString());
        }

        try
        {
            this.statement.executeUpdate("DELETE FROM bcl_chunkloaders WHERE owner = " + UUIDtoHexString(uuid) + ";");
        }
        catch (Exception e)
        {
            plugin.getLogger().info(e.toString());
        }
    }

    public void removeDonor(UUID uuid)
    {
        try
        {
            this.statement.executeUpdate("DELETE FROM bcl_playersdata WHERE pid = " + UUIDtoHexString(uuid) + ";");
        }
        catch (Exception e)
        {
            plugin.getLogger().info(e.toString());
        }

        try
        {
            this.statement.executeUpdate("DELETE FROM bcl_chunkloaders WHERE owner = " + UUIDtoHexString(uuid) + ";");
        }
        catch (Exception e)
        {
            plugin.getLogger().info(e.toString());
        }

        chunkOwners.remove(uuid);
    }

    public void refreshCache()
    {
        try
        {
            plugin.getLogger().info("===== Refreshing Cache =====");
            ResultSet res = this.statement.executeQuery("SELECT * FROM bcl_playersdata");
            while (res.next())
            {
                plugin.getLogger().info("Player: " + res.getString("pid") + " Chunks: " + Integer.toString(res.getInt("onlineonly")));
                chunkOwners.putIfAbsent(res.getString("pid"), res.getInt("onlineonly"));
            }
        }
        catch (Exception e)
        {
            plugin.getLogger().info(e.toString());
        }
    }

    private static String UUIDtoHexString(UUID uuid) {
        if (uuid == null) {
            return "";
        }
        return "\"" + uuid.toString() + "\"";
    }

}
