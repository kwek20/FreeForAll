/**
 * 
 */
package com.division.fearforall.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

/**
 * @author Brord
 *
 */
public class LocationTools {
	public static Vector toVector(Location l){
		return l.toVector();
	}
	
	public static BlockVector toVector(Block b) {
		return new BlockVector(b.getLocation().toVector().clone());
	}
}
