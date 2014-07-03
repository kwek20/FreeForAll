package com.division.fearforall.engines;

import com.division.fearforall.core.FearForAll;
import com.division.fearforall.core.PlayerStorage;
import com.division.fearforall.events.PlayerDamageInArenaEvent;
import com.division.fearforall.events.PlayerEnteredArenaEvent;
import com.division.fearforall.events.PlayerQuitInArenaEvent;

import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author Evan
 *
 */
@EngineInfo(author = "kwek20",
version = "0.2.4EB",
depends = {"OfflineStorage"})
public class StorageEngine extends Engine {

    protected int aM;

    @Override
    public String getName() {
        return ("Storage");
    }
    private static ArrayList<PlayerStorage> massStorage = new ArrayList<PlayerStorage>();
    private OfflineStorageEngine OSE = null;

    public StorageEngine() {
    }

    @Override
    public void runStartupChecks() throws EngineException {
        Engine eng = FearForAll.getInstance().getEngineManger().getEngine("OfflineStorage");
        if (eng != null) {
            if (eng instanceof OfflineStorageEngine) {
                this.OSE = (OfflineStorageEngine) eng;
            }
        }
        if (OSE == null) {
            throw new EngineException("Missing Dependency.");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDamagedInArena(PlayerDamageInArenaEvent evt) {
        Player victim = evt.getVictim();
        EntityDamageEvent ede = evt.getDamageEvent();
        EntityDamageByEntityEvent edee;
        if (ede instanceof EntityDamageByEntityEvent) {
            edee = (EntityDamageByEntityEvent) ede;
            Player attacker;
            if (edee.getDamager() instanceof Player) {
                attacker = (Player) edee.getDamager();
                getStorage(victim.getName()).setLastHit(attacker.getName());
                if (!evt.getDamageEvent().isCancelled()) {
                    double damage = getArmorRedox(attacker, evt.getDamageEvent().getDamage());
                    victim.damage(damage);
                    for(ItemStack item: victim.getInventory().getArmorContents()){
                        item.setDurability((short)(item.getDurability()-3));
                    }
                    evt.getDamageEvent().setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuitInArena(PlayerQuitInArenaEvent evt) {
        Player evtPlayer = evt.getPlayer();
        if (hasStorage(evt.getPlayer())) {
            PlayerStorage pStorage = getStorage(evtPlayer.getName());
            if (evtPlayer.hasPotionEffect(PotionEffectType.SPEED)) {
                evtPlayer.removePotionEffect(PotionEffectType.SPEED);
            }
            if (!OSE.hasOfflineStorage(pStorage.getKey())) {
                OSE.covertPlayerStorage(pStorage);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerEnteredArena(PlayerEnteredArenaEvent evt) {
        if (!hasStorage(evt.getPlayer())) {
            addStorage(evt.getPlayer());
        } else {
            evt.setCancelled(true);
        }
    }

    public void addStorage(Player key) {
        PlayerStorage pStorage = new PlayerStorage(key.getName(), key.getInventory());
        massStorage.add(pStorage);
        System.out.println("added storage for player " + key.getName());
    }

    public PlayerStorage getStorage(String rKey) {
        for (PlayerStorage ps : massStorage) {
            if (ps.getKey().equals(rKey)) {
                return ps;
            }
        }
        return null;
    }

    public void safeRestore(Player p) {
        if (!p.isDead()) {
            p.teleport(p.getWorld().getSpawnLocation());
        }
    }

    public boolean hasStorage(Player rkey) {
        if (getStorage(rkey.getName()) != null) {
            return true;
        }
        return false;
    }

    public void removeStorage(PlayerStorage pStorage) {
        massStorage.remove(pStorage);
    }

    public void saveAll() {
        for (PlayerStorage ps : massStorage) {
            createOfflineStorage(ps);
        }
    }

    public void createOfflineStorage(PlayerStorage pStorage) {
        if (pStorage != null) {
            if (!hasOfflineStorage(pStorage.getKey())) {
                System.out.println("[FearForAll] converting storage key: " + pStorage.getKey() + " to Offline Storage.");
                if (OSE.covertPlayerStorage(pStorage)) {
                    System.out.println("[FearForAll] storage key: " + pStorage.getKey() + " has been successfully converted.");
                } else {
                    System.out.println("[FearForAll] An error occured when converting storage key: " + pStorage.getKey());
                }
            }
        }
    }
    
    public boolean hasOfflineStorage(String key) {
        return OSE.hasOfflineStorage(key);
    }

    public int aO(Player p) {
        return this.l(p);
    }

    public int l(Player p) {
        int i = 0;
        ItemStack[] aitemstack = p.getInventory().getArmorContents();
        int j = aitemstack.length;

        for (int k = 0; k < j; ++k) {
            ItemStack itemstack = aitemstack[k];

            /**
             * 
             * NEEDS FIX HERE
             *
             * 
             * 
             */
            //TODO
            int l = 2;

            i += l;
        }
        return i;
    }

    public double getArmorRedox(Player p, double d) {
        int j = 25 - this.aO(p);
        double k = d * j;

        this.k(p, d);
        d = k / 25;

        return d;
    }

    public void k(Player p, double d) {
        d /= 4;
        if (d < 1) {
            d = 1;
        }
        ItemStack[] armor = p.getInventory().getArmorContents();

        for (int j = 0; j < armor.length; ++j) {
            if (armor[j] != null) {
                armor[j].setDurability((short) (armor[j].getDurability() - d));
                if (armor[j].getDurability() == 0) {
                    armor[j] = null;
                }
            }
        }
    }
}
