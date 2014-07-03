package com.division.fearforall.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author Evan
 */
public class PlayerStorage implements Serializable {

	private static final long serialVersionUID = -9021137093932155337L;
	private ItemStack[] contents;
    private ItemStack helmet;
    private ItemStack chest;
    private ItemStack legs;
    private ItemStack feet;
    private String key;
    private String lastHit = "";
    private transient int killStreak = 0;
    private transient List<String> killList = new ArrayList<String>();
    private transient List<Integer> streakLevels = new ArrayList<Integer>();

    public PlayerStorage(String key, final PlayerInventory storage) {
        this.key = key;
        this.contents = storage.getContents();
        this.helmet = storage.getHelmet();
        this.chest = storage.getChestplate();
        this.legs = storage.getLeggings();
        this.feet = storage.getBoots();
    }

    public void restoreInv(Player rPlayer) {
        rPlayer.getInventory().clear();
        rPlayer.getInventory().setBoots(feet);
        rPlayer.getInventory().setHelmet(helmet);
        rPlayer.getInventory().setChestplate(chest);
        rPlayer.getInventory().setLeggings(legs);
        for (ItemStack item : contents) {
            if (item != null && item.getTypeId() != 0) {
                rPlayer.getInventory().addItem(item);
            }
        }
        if (rPlayer.hasPotionEffect(PotionEffectType.SPEED)) {
            rPlayer.removePotionEffect(PotionEffectType.SPEED);
        }
    }

    public String getKey() {
        return key;
    }

    public void addKill(Player p) {
        if (!killList.contains(p.getName())) {
            killStreak++;
            killList.add(p.getName());
        }
    }

    public void rewardKillStreak(int level) {
        streakLevels.add(level);
    }

    public boolean isRewarded(int level) {
        return streakLevels.contains(level);
    }

    public int getKillStreak() {
        return killStreak;
    }

    public void clearKillList() {
        killList.clear();
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public ItemStack getChestpiece() {
        return chest;
    }

    public ItemStack getLeggings() {
        return legs;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public ItemStack getBoots() {
        return feet;
    }
    public void setLastHit(String name){
        lastHit = name;
    }
    public String getLastHit(){
        return lastHit;
    }
}
