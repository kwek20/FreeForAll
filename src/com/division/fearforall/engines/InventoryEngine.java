package com.division.fearforall.engines;

import com.division.fearforall.core.FearForAll;
import com.division.fearforall.core.PlayerStorage;
import com.division.fearforall.events.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

/**
 *
 * @author Evan
 */
@EngineInfo(author = "mastershake71",
version = "0.2.31RB",
depends = {"OfflineStorage", "Storage"})
public class InventoryEngine extends Engine {

    OfflineStorageEngine OSE = null;
    StorageEngine SE = null;

    public InventoryEngine() {
    }

    @Override
    public void runStartupChecks() throws EngineException {
        Engine eng = FearForAll.getInstance().getEngineManger().getEngine("OfflineStorage");
        if (eng != null) {
            if (eng instanceof OfflineStorageEngine) {
                this.OSE = (OfflineStorageEngine) eng;
            }
        }
        eng = FearForAll.getInstance().getEngineManger().getEngine("Storage");
        if (eng != null) {
            if (eng instanceof StorageEngine) {
                this.SE = (StorageEngine) eng;
            }
        }
        if (OSE == null || SE == null) {
            throw new EngineException("Missing Dependency.");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerEnteredArena(PlayerEnteredArenaEvent evt) {
        if (!evt.isCancelled()) {
            Player evtPlayer = evt.getPlayer();
            if (!evtPlayer.isDead() || evt.getMethod() == MoveMethod.RESPAWNED) {
                evtPlayer.getInventory().clear();
                addItems(evtPlayer);
                addArmor(evtPlayer);
                evtPlayer.setHealth(20);
                evtPlayer.setFoodLevel(20);
                evtPlayer.sendMessage(ChatColor.YELLOW + "[FearForAll]" + ChatColor.RED + " You have entered the arena. Your inventory has been saved.");
                for (PotionEffect pt : evtPlayer.getActivePotionEffects()) {
                    evtPlayer.removePotionEffect(pt.getType());
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerPreCheck(PlayerPreCheckEvent evt){
        Player evtPlayer = evt.getPlayer();
        if(OSE.hasOfflineStorage(evtPlayer.getName())){
            restoreInventory(evtPlayer);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLeftArena(PlayerLeftArenaEvent evt) {
        restoreInventory(evt.getPlayer());
        //SE.playersInArena.remove(evt.getPlayer());
    }

    public void addArmor(Player p) {
        PlayerInventory pInv = p.getInventory();
        pInv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
        pInv.setBoots(new ItemStack(Material.IRON_BOOTS, 1));
        pInv.setHelmet(new ItemStack(Material.IRON_HELMET, 1));
        pInv.setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
    }

    public void addItems(Player p) {
        PlayerInventory pInv = p.getInventory();
        pInv.addItem(new ItemStack(Material.IRON_SWORD, 1));
        pInv.addItem(new ItemStack(Material.BREAD, 16));
        pInv.addItem(new ItemStack(Material.IRON_AXE, 1));
    }

    public boolean restoreInventory(Player player) {
        if (OSE.hasOfflineStorage(player.getName())) {
            OSE.loadOfflineStorage(player,player.getName());
            if (SE.hasStorage(player)) {
                PlayerStorage pStorage = SE.getStorage(player.getName());
                SE.removeStorage(pStorage);
            }
            return true;
        }
        PlayerStorage pStorage = SE.getStorage(player.getName());
        if (pStorage != null) {
            pStorage.restoreInv(player);
            SE.removeStorage(pStorage);
            player.sendMessage(ChatColor.YELLOW + "[FearForAll]" + ChatColor.RED + " Your inventory has been restored.");
            return true;
        }
        return false;
    }

    @Override
    public final String getName() {
        return ("Inventory");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void distributeRewards(PlayerKillstreakAwardedEvent evt) {
        for (ItemStack is : evt.getRewards()) {
            evt.getPlayer().getInventory().addItem(is);
        }
    }
}
