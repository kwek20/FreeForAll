package com.division.fearforall.mysql;

import java.util.ArrayList;

/**
 *
 * @author Evan
 */
public interface DataInterface {

    public void incrementKillCount(String player_name);

    public void updateKillStreak(String player_name, int curKillStreak);

    public void incrementPlayCount(String player_name);

    public void createPlayerAccount(String player_name);

    public void incrementDeathCount(String player_name);

    public ArrayList<String> getTopKills();

    public ArrayList<String> getTopStreak();

    public int getKillStreak(int player_id);

    public int getPlayerId(String player_name);

    public int getKillCount(int player_id);

    public int getDeathCount(int player_id);
}
