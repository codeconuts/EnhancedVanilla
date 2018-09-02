package com.teampublic.enhancedvanilla;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;

public abstract class ExplosionCombat extends CombatHandle {

	private transient final Set<Explosive> explodingEntities;
	
	public ExplosionCombat(Plugin plugin, Entity damager) {
		super(plugin, damager);
		this.explodingEntities = new HashSet<Explosive>();
	}
	
	public Set<Explosive> getExplodingEntities() {
		return explodingEntities;
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getCause() == DamageCause.BLOCK_EXPLOSION || event.getCause() == DamageCause.BLOCK_EXPLOSION && event.getEntity() instanceof Player && explodingEntities.contains(event.getDamager()))) 
			return;
		super.getEntities().put((Player) event.getEntity(), event.getFinalDamage());
	}

}
