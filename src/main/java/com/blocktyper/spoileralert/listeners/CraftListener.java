package com.blocktyper.spoileralert.listeners;

import java.util.Arrays;
import java.util.OptionalLong;

import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import com.blocktyper.spoileralert.SpoilerAlertPlugin;

public class CraftListener extends SpoilerAlertListenerBase{
	
	
	public CraftListener(SpoilerAlertPlugin plugin) {
		super(plugin);
		// TODO Auto-generated constructor stub
	}

	/*
	 * ON ITEM CRAFT
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPrepareItemCraft(PrepareItemCraftEvent event) {

		ItemStack result = event.getInventory().getResult();
		if (result == null) {
			return;
		}
		
		HumanEntity player = event.getViewers() != null && !event.getViewers().isEmpty() ? event.getViewers().get(0) : null;

		final World world = event.getViewers().get(0).getWorld();
		ItemStack[] itemsInCraftingTable = event.getInventory().getMatrix();

		OptionalLong optionalLong = Arrays.asList(itemsInCraftingTable).stream()
				.filter(i -> getDaysExpiredZeroOutNulls(i, world, player) > 0).mapToLong(i -> getDaysExpiredZeroOutNulls(i, world, player)).max();

		// if the food is not expired set daysSourceExpired to null in case it
		// is 0, we want setExpirationDate() to ignore it
		Long daysSourceExpired = optionalLong != null && optionalLong.isPresent() ? optionalLong.getAsLong() : null;
		daysSourceExpired = daysSourceExpired == null || daysSourceExpired < 1 ? null : daysSourceExpired;
		
		event.getInventory().setResult(setExpirationDate(result, world, daysSourceExpired, event.getViewers() != null && !event.getViewers().isEmpty() ? event.getViewers().get(0) : null));
	}
}
