package com.division.freeforall.engines;

import com.division.freeforall.core.FreeForAll;
import com.division.freeforall.core.PlayerStorage;
import com.division.freeforall.events.PlayerDeathInArenaEvent;
import com.division.freeforall.events.PlayerKilledPlayerInArenaEvent;
import com.division.freeforall.events.PlayerKillstreakAwardedEvent;
import com.division.freeforall.events.PlayerDeathInArenaEvent.DeathCause;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author Evan
 */
@EngineInfo(author = "mastershake71",
version = "0.2.23RB",
depends = {"Storage", "Inventory"})
public class KillStreakEngine extends Engine {

    StorageEngine SE = null;
    InventoryEngine IE = null;

    @Override
    public final String getName() {
        return ("Killstreak");
    }

    public KillStreakEngine() {
    }

    @Override
    public void runStartupChecks() throws EngineException {
        Engine eng = FreeForAll.getInstance().getEngineManger().getEngine("Storage");
        if (eng != null) {
            if (eng instanceof StorageEngine) {
                this.SE = (StorageEngine) eng;
            }
        }
        eng = FreeForAll.getInstance().getEngineManger().getEngine("Inventory");
        if (eng != null) {
            if (eng instanceof InventoryEngine) {
                this.IE = (InventoryEngine) eng;
            }
        }
        if (SE == null || IE == null) {
            throw new EngineException("Missing Dependency.");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerKill(PlayerKilledPlayerInArenaEvent evt) {
        int killstreak = addKill(evt.getKiller(), evt.getVictim());
        rewardKillStreak(evt.getKiller());
        FreeForAll.getInstance().getServer().getPluginManager().callEvent(new PlayerKillstreakAwardedEvent(evt.getKiller(), killstreak, new ArrayList<ItemStack>()));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerKillstreakRewarded(PlayerKillstreakAwardedEvent evt) {
        Player p = evt.getPlayer();
        PlayerInventory pInv = p.getInventory();
        ArrayList<ItemStack> rewards = evt.getRewards();
        PlayerStorage pStorage = SE.getStorage(p.getName());
        int killStreak = evt.getKillStreak();
        if (!pStorage.isRewarded(killStreak)) {
            if (killStreak == 1) {
                pInv.remove(Material.IRON_SWORD);
                pInv.remove(Material.IRON_AXE);
                rewards.add(new ItemStack(Material.DIAMOND_SWORD, 1));
                rewards.add(new ItemStack(Material.DIAMOND_AXE, 1));
                pStorage.rewardKillStreak(killStreak);

            } else if (killStreak == 2) {
                rewards.add(new ItemStack(Material.BOW, 1));
                rewards.add(new ItemStack(Material.ARROW, 64));
                pStorage.rewardKillStreak(killStreak);
            } else if (killStreak == 6) {
                rewards.add(new ItemStack(Material.GOLDEN_APPLE, 1));
                rewards.add(new ItemStack(Material.BREAD, 32));
                rewards.add(new ItemStack(Material.ARROW, 32));
            } else if (killStreak == 9) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120000, 1));
                IE.addArmor(p);
            }
        }
        rewards.add(new ItemStack(Material.BREAD, 12));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeathInArena(PlayerDeathInArenaEvent evt) {
        Player victim = evt.getVictim();
        if (evt.getCause() == DeathCause.ENVIRONMENT) {
            String lastAttacker = SE.getStorage(victim.getName()).getLastHit();
            Player killer = Bukkit.getServer().getPlayer(lastAttacker);
            if (killer != null) {
                Bukkit.getServer().getPluginManager().callEvent(new PlayerKilledPlayerInArenaEvent(victim, killer));
            }
        }
    }

    public int addKill(Player killer, Player victim) {
        PlayerStorage pStorage;
        if (killer != null) {
            pStorage = SE.getStorage(killer.getName());
            if (pStorage != null) {
                pStorage.addKill(victim);
                killer.sendMessage(ChatColor.YELLOW + "[FreeForAll]" + ChatColor.RED + " You killed: " + victim.getName());
                victim.sendMessage(ChatColor.YELLOW + "[FreeForAll]" + ChatColor.RED + " You were killed by: " + killer.getName());
                return pStorage.getKillStreak();
            }
        }
        return -1;
    }

    public void rewardKillStreak(Player p) {
        PlayerStorage pStorage = SE.getStorage(p.getName());
        System.out.println(pStorage);
        
        int killStreak = pStorage.getKillStreak();
        double streakVal = killStreak / 3.0;
        if (streakVal >= 1) {
            if (streakVal % 1 > 0) {
                if (!pStorage.isRewarded(killStreak)) {
                    FreeForAll.getEcon().depositPlayer(p.getName(), streakVal * 50);
                    p.sendMessage(ChatColor.YELLOW + "[FreeForAll] " + ChatColor.RED + "You have been awarded $" + streakVal * 50);
                    System.out.println("[FreeForAll] Awarded " + p.getName() + " $" + streakVal * 250);
                    if (streakVal != 1) {
                        Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "[FreeForAll] " + ChatColor.RED + p.getName() + " is on a " + killStreak + " Kill Streak! Type /ffa to stop him!");
                    }
                    pStorage.rewardKillStreak(killStreak);
                }
            }
        }
    }
}
