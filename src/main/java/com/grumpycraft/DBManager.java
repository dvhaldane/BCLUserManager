package com.grumpycraft;

import java.sql.*;
import java.util.UUID;

public class DBManager
{
    private Connection connection;
    private String host, database, username, password;
    private int port;
    Statement statement;

    public DBManager()
    {
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

    public void setChunks(BCLUserManager plugin, UUID uuid)
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

    public void zeroChunks(BCLUserManager plugin, UUID uuid)
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

    private static String UUIDtoHexString(UUID uuid) {
        if (uuid == null) {
            return "";
        }
        return "\"" + uuid.toString() + "\"";
    }

}
