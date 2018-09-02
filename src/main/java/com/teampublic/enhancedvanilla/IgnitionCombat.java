package com.teampublic.enhancedvanilla;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class IgnitionCombat extends ExplosionCombat {

	private transient Block ignitingBlock;
	private transient Entity ignitingProjectile;
	
	public IgnitionCombat(Plugin plugin, Entity damager) {
		super(plugin, damager);
	}
	
	@EventHandler
	public void onEntityShootBow(EntityShootBowEvent event) {
		if (!(event.getBow().getEnchantments().containsKey(Enchantment.ARROW_FIRE) && event.getEntity() == super.getDamager())) return;
		this.ignitingProjectile = event.getProjectile();
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!(event.getPlayer() == super.getDamager())) return;
		this.ignitingBlock = event.getBlock();
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		BlockData data = event.getClickedBlock().getBlockData();
		if (!(event.getPlayer() == super.getDamager() && event.getAction() == Action.RIGHT_CLICK_BLOCK && data instanceof Powerable && ((Powerable) data).isPowered())) 
			return;
		this.ignitingBlock = event.getClickedBlock();
	}
	
	@EventHandler
	public void onBlockRedstone(BlockRedstoneEvent event) {
		if (ignitingBlock instanceof Powerable && event.getOldCurrent() < 1 && event.getNewCurrent() > 0 && getRedstoneCircuit(ignitingBlock.getLocation()).contains(event.getBlock())) 
			this.ignitingBlock = event.getBlock();
	}
	
	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
		if (!(event.getIgnitingBlock() == ignitingBlock || event.getIgnitingEntity() == ignitingProjectile || event.getPlayer() == super.getDamager())) return;
		super.getExplodingEntities().addAll(event.getPlayer().getWorld().getNearbyEntities(event.getBlock().getLocation(), 0.5, 0.5, 0.5).stream().filter(entity -> entity instanceof Explosive).map(entity -> (Explosive) entity).collect(Collectors.toSet()));
	}
	
	public static List<Block> getRedstoneCircuit(Location location) {
		BlockData data = location.getBlock().getBlockData();
		List<Block> blocks = new ArrayList<Block>();
		if (data instanceof Powerable || data instanceof AnaloguePowerable || location.getBlock().isBlockPowered() || location.getBlock().isBlockIndirectlyPowered()) {
			blocks.addAll(getRedstoneCircuit(location.add(1, 0, 0)));
			blocks.addAll(getRedstoneCircuit(location.add(-1, 0, 0)));
			blocks.addAll(getRedstoneCircuit(location.add(0, 1, 0)));
			blocks.addAll(getRedstoneCircuit(location.add(0, -1, 0)));
			blocks.addAll(getRedstoneCircuit(location.add(0, 0, 1)));
			blocks.addAll(getRedstoneCircuit(location.add(0, 0, -1)));
			if (data instanceof RedstoneWire) {
				blocks.addAll(getRedstoneCircuit(location.add(1, 1, 0)));
				blocks.addAll(getRedstoneCircuit(location.add(-1, 1, 0)));
				blocks.addAll(getRedstoneCircuit(location.add(0, 1, 1)));
				blocks.addAll(getRedstoneCircuit(location.add(0, 1, -1)));
				blocks.addAll(getRedstoneCircuit(location.add(1, -1, 0)));
				blocks.addAll(getRedstoneCircuit(location.add(-1, -1, 0)));
				blocks.addAll(getRedstoneCircuit(location.add(0, -1, 1)));
				blocks.addAll(getRedstoneCircuit(location.add(0, -1, -1)));
			}
		}
		return blocks;
	}

}
