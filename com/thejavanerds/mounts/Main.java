package com.thejavanerds.mounts;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by ZhsCraft on 2/5/2015.
 */
public class Main extends JavaPlugin{

    public static Main plugin;

    public void onEnable()
    {
        plugin = this;
        Bukkit.getServer().getPluginManager().registerEvents(new Mount(plugin), this);
    }

    public void onDisable()
    {

    }


}
