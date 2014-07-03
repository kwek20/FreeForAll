/**
 * 
 */
package com.division.freeforall.regions;

/**
 * @author Brord
 *
 */
public class Arena {
	
	private Region region;
	private HealRegion healregion;
	
	/**
	 * @param healRegion 
	 * @param region 
	 * 
	 */
	public Arena(Region region, HealRegion healRegion) {
		this.setRegion(region);
		this.setHealRegion(healRegion);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Arena " + region.world.getName();
	}

	/**
	 * @return the healregion
	 */
	public HealRegion getHealRegion() {
		return healregion;
	}

	/**
	 * @param healregion the healregion to set
	 */
	public void setHealRegion(HealRegion healregion) {
		this.healregion = healregion;
	}

	/**
	 * @return the region
	 */
	public Region getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(Region region) {
		this.region = region;
	}
}
