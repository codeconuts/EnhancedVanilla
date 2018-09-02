package com.teampublic.enhancedvanilla;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class CombatHandle implements Listener {

	private final Plugin plugin;
	private final Entity damager;
	private final Map<Entity, Double> entities;
	
	public CombatHandle(Plugin plugin, Entity damager) {
		this.plugin = plugin;
		this.damager = damager;
		this.entities = new HashMap<Entity, Double>();
	}
	
	public Plugin getPlugin() {
		return plugin;
	}

	public Entity getDamager() {
		return damager;
	}
	
	public Map<Entity, Double> getEntities() {
		return entities;
	}

	public void open() {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public void recycle() {
		close();
		open();
	}
	
	public void close() {
		HandlerList.unregisterAll(this);
	}
	
}
