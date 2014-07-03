package com.division.fearforall.config;

import com.division.fearforall.core.FearForAll;
import com.division.fearforall.regions.HealRegion;
import com.division.fearforall.regions.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.BlockVector;

/**
 *
 * @author Evan
 */
public class FFAConfig {

    FileConfiguration ffaconfig = new YamlConfiguration();
    FearForAll FFA;
    private static HashMap<String, ArrayList<Location>> spawns = new HashMap<String, ArrayList<Location>>();
    private static String mysqlhost;
    private static String mysqlport;
    private static String mysqluser;
    private static String mysqlpass;
    private static String mysqldata;
    private boolean changed = false;

    public FFAConfig(FearForAll instance) {
        this.FFA = instance;
        instance.saveDefaultConfig();
        ffaconfig = instance.getConfig();
    }

    public void load() {
        spawns.clear();
        System.out.println("[FearForAll] loading config...");
        if (ffaconfig.contains("region")) {
            Set<String> arenaList = ffaconfig.getConfigurationSection("region").getKeys(false);
            Set<String> spawnList;
            for (String arena : arenaList) {
            	if (!ffaconfig.contains("region." + arena + ".spawn")) {
            		System.out.println("[FearForAll] arena " + arena + " has no spawns defined!");
            		continue;
            	}
            	
            	spawnList = ffaconfig.getConfigurationSection("region." + arena + ".spawn").getKeys(false);
            	for (String s : spawnList){
	                World world = Bukkit.getServer().getWorld(ffaconfig.getString("region." + arena + ".spawn." + s + ".world", "world"));
	                double x = ffaconfig.getDouble("region." + arena + ".spawn." + s + ".x");
	                double y = ffaconfig.getDouble("region." + arena + ".spawn." + s + ".y");
	                double z = ffaconfig.getDouble("region." + arena + ".spawn." + s + ".z");
	                float yaw = (float) ffaconfig.getDouble("region." + arena + ".spawn." + s + ".yaw");
	                float pitch = (float) ffaconfig.getDouble("region." + arena + ".spawn." + s + ".pitch");
	                
	                addSpawn(arena, new Location(world, x, y, z, yaw, pitch));
            	}
            }
        }
        if (!ffaconfig.contains("mysql.host")) {
            ffaconfig.set("mysql.host", "localhost");
            changed = true;
        } else {
            mysqlhost = ffaconfig.getString("mysql.host");
        }
        if (!ffaconfig.contains("mysql.port")) {
            ffaconfig.set("mysql.port", "3306");
            changed = true;
        } else {
            mysqlport = ffaconfig.getString("mysql.port");
        }
        if (!ffaconfig.contains("mysql.username")) {
            ffaconfig.set("mysql.username", "root");
            changed = true;
        } else {
            mysqluser = ffaconfig.getString("mysql.username");
        }
        if (!ffaconfig.contains("mysql.password")) {
            ffaconfig.set("mysql.password", "password");
            changed = true;
        } else {
            mysqlpass = ffaconfig.getString("mysql.password");
        }
        if (!ffaconfig.contains("mysql.database")) {
            ffaconfig.set("mysql.database", "ffa");
            changed = true;
        } else {
            mysqldata = ffaconfig.getString("mysql.database");
        }
        if (changed) {
            FFA.saveConfig();
        } else {
            System.out.println("[FearForAll] Done loading config!");
        }
    }

    public void setSpawn(String arena, String rname, Location loc) {
        String name = rname.toLowerCase();
        ffaconfig.set("region." + arena + ".spawn." + name + ".world", loc.getWorld().getName());
        ffaconfig.set("region." + arena + ".spawn." + name + ".x", loc.getX());
        ffaconfig.set("region." + arena + ".spawn." + name + ".y", loc.getY());
        ffaconfig.set("region." + arena + ".spawn." + name + ".z", loc.getZ());
        ffaconfig.set("region." + arena + ".spawn." + name + ".yaw", loc.getYaw());
        ffaconfig.set("region." + arena + ".spawn." + name + ".pitch", loc.getPitch());
        FFA.saveConfig();
        addSpawn(arena, loc);
    }

    public void setRegion(String arena, World world, BlockVector p1, BlockVector p2) {
        ffaconfig.set("region." + arena + ".world", world.getName());
        ffaconfig.set("region." + arena + ".p1.x", p1.getX());
        ffaconfig.set("region." + arena + ".p1.y", 0);
        ffaconfig.set("region." + arena + ".p1.z", p1.getZ());
        ffaconfig.set("region." + arena + ".p2.x", p2.getX());
        ffaconfig.set("region." + arena + ".p2.y", 256);
        ffaconfig.set("region." + arena + ".p2.z", p2.getZ());
        FFA.saveConfig();
    }

    public void setHealRegion(String arena, World world, BlockVector p1, BlockVector p2) {
        ffaconfig.set("region." + arena + ".healregion.world", world.getName());
        ffaconfig.set("region." + arena + ".healregion.p1.x", p1.getX());
        ffaconfig.set("region." + arena + ".healregion.p1.y", 0);
        ffaconfig.set("region." + arena + ".healregion.p1.z", p1.getZ());
        ffaconfig.set("region." + arena + ".healregion.p2.x", p2.getX());
        ffaconfig.set("region." + arena + ".healregion.p2.y", 256);
        ffaconfig.set("region." + arena + ".healregion.p2.z", p2.getZ());
        FFA.saveConfig();
    }
    
    public void addSpawn(String arena, Location loc){
    	ArrayList<Location> list = spawns.get(arena);
    	if (list == null) list = new ArrayList<Location>();
    	list.add(loc);
    	spawns.put(arena, list);
    }

    public HealRegion getHealRegion(String arena) {
        World world = Bukkit.getServer().getWorld(ffaconfig.getString("region." + arena + "healregion.world", "world"));
        double p1x = ffaconfig.getDouble("region." + arena + ".healregion.p1.x");
        double p1y = ffaconfig.getDouble("region." + arena + ".healregion.p1.y");
        double p1z = ffaconfig.getDouble("region." + arena + ".healregion.p1.z");
        double p2x = ffaconfig.getDouble("region." + arena + ".healregion.p2.x");
        double p2y = ffaconfig.getDouble("region." + arena + ".healregion.p2.y");
        double p2z = ffaconfig.getDouble("region." + arena + ".healregion.p2.z");
        BlockVector p1 = new BlockVector(p1x, p1y, p1z);
        BlockVector p2 = new BlockVector(p2x, p2y, p2z);
        return new HealRegion(world, p1, p2, FFA);
    }

    public Region getRegion(String arena) {
        World world = Bukkit.getWorld(ffaconfig.getString("region." + arena + ".world", "world"));
        double p1x = ffaconfig.getDouble("region." + arena + ".p1.x");
        double p1y = ffaconfig.getDouble("region." + arena + ".p1.y");
        double p1z = ffaconfig.getDouble("region." + arena + ".p1.z");
        double p2x = ffaconfig.getDouble("region." + arena + ".p2.x");
        double p2y = ffaconfig.getDouble("region." + arena + ".p2.y");
        double p2z = ffaconfig.getDouble("region." + arena + ".p2.z");
        BlockVector p1 = new BlockVector(p1x, p1y, p1z);
        BlockVector p2 = new BlockVector(p2x, p2y, p2z);
        return new Region(world, p1, p2);
    }

    public ArrayList<Location> getSpawns(String arena) {
        return spawns.get(arena);
    }

    public Set<String> getSpawnNames(String arena) {
        return ffaconfig.getConfigurationSection("region." + arena + ".spawn").getKeys(false);
    }

    public boolean removeSpawn(String arena, String name) {
        if (getSpawnNames(arena).contains(name.toLowerCase())) {
            ffaconfig.set("region." + arena + ".spawn." + name.toLowerCase(), null);
            FFA.saveConfig();
            return true;
        }
        return false;
    }

    public String getMySQLDatabase() {
        return mysqldata;
    }

    public String getMySQLPassword() {
        return mysqlpass;
    }

    public String getMySQLUsername() {
        return mysqluser;
    }

    public String getMySQLHost() {
        return mysqlhost;
    }

    public String getMySQLPort() {
        return mysqlport;
    }

	/**
	 * @return
	 */
	public Set<String> getArenas() {
		return ffaconfig.getConfigurationSection("region").getKeys(false);
	}
}
