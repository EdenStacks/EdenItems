package fr.edencraft.edenitems.listener;

import fr.edencraft.edenitems.EdenItems;
import fr.edencraft.edenitems.manager.ConfigurationManager;
import fr.edencraft.edenitems.utils.ColoredText;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Level;

public class Command implements Listener {

	private final ConfigurationManager configurationManager = EdenItems.getINSTANCE().getConfigurationManager();

	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage();
		if (!isBlockedCommand(command)) return;

		Player player = event.getPlayer();
		if (hasBlockerItem(command, player)) {
			event.setCancelled(true);
		}
	}

	/**
	 * @param blocker    item blocker (if it has custom model data, playerItem need to have the same)
	 * @param playerItem tested player item.
	 * @return true if same else false.
	 */
	private boolean isSame(ItemStack blocker, ItemStack playerItem) {
		Material blockerMaterial = blocker.getType();
		if (playerItem == null) {
			return false;
		}

		if (blocker.hasItemMeta() && blocker.getItemMeta().hasCustomModelData()) {
			int blockerModelId = blocker.getItemMeta().getCustomModelData();
			if (!playerItem.hasItemMeta() || !playerItem.getItemMeta().hasCustomModelData()) {
				return false;
			}
			return playerItem.getType().equals(blockerMaterial)
					&& playerItem.getItemMeta().getCustomModelData() == blockerModelId;
		} else {
			return playerItem.getType().equals(blockerMaterial);
		}
	}

	private boolean hasBlockerItem(String command, Player player) {
		Map<ItemStack, List<ConfigurationSection>> itemsBlocker = getItemsBlocker();

		PlayerInventory playerInventory = player.getInventory();
		ItemStack itemInMainHand = playerInventory.getItemInMainHand();
		ItemStack itemInOffHand = playerInventory.getItemInOffHand();

		// Check hands first
		for (ItemStack blocker : itemsBlocker.keySet()) {
			List<ConfigurationSection> configurationSections = itemsBlocker.get(blocker);

			if (isSame(blocker, itemInMainHand) | isSame(blocker, itemInOffHand)) {

				for (ConfigurationSection section : configurationSections) {

					if (section == null) continue;

					if (section.getName().equals("blocked_commands_in_hands")) {

						for (String blockedCommand : section.getKeys(false)) {

							if (blockedCommand.equalsIgnoreCase(command)) {
								player.sendMessage(new ColoredText(
										section.getString(blockedCommand + ".message")
								).treat());
								return true;
							}
						}
					}
				}
			}
		}

		List<ItemStack> playerInventoryContents = new ArrayList<>();
		Collections.addAll(playerInventoryContents, playerInventory.getStorageContents());
		Collections.addAll(playerInventoryContents, playerInventory.getArmorContents());
		playerInventoryContents.remove(playerInventory.getItemInMainHand());

		// Check inventory
		for (ItemStack blocker : itemsBlocker.keySet()) {
			List<ConfigurationSection> configurationSections = itemsBlocker.get(blocker);

			for (ItemStack playerItem : playerInventoryContents) {
				if (isSame(blocker, playerItem)) {
					for (ConfigurationSection section : configurationSections) {

						if (section == null) continue;

						if (section.getName().equals("blocked_commands_in_inventory")) {

							for (String blockedCommand : section.getKeys(false)) {

								if (blockedCommand.equalsIgnoreCase(command)) {
									player.sendMessage(new ColoredText(
											section.getString(blockedCommand + ".message")
									).treat());
									return true;
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * @return a map with as key the item blocker (defined by a material and an optional custom model data) and
	 * as value blocked_commands_in_hands and blocked_commands_in_inventory section.
	 */
	private @NotNull Map<ItemStack, List<ConfigurationSection>> getItemsBlocker() {
		Map<ItemStack, List<ConfigurationSection>> map = new HashMap<>();
		FileConfiguration cfg = configurationManager.getConfigurationFile("items.yml");

		for (String id : cfg.getKeys(false)) {
			ConfigurationSection itemSection = cfg.getConfigurationSection(id);
			assert itemSection != null;

			Material material = null;
			try {
				material = Material.getMaterial(Objects.requireNonNull(itemSection.getString("material")));
			} catch (NullPointerException ignored) {
				EdenItems.getINSTANCE().log(Level.WARNING, "Material not defined for " + id + ".");
				continue;
			}
			if (material == null) {
				EdenItems.getINSTANCE().log(Level.WARNING, "Invalid material for " + id + ".");
				continue;
			}

			int modelId = itemSection.getInt("model_id");

			ItemStack itemBlocker = new ItemStack(material);
			if (modelId > 0) {
				ItemMeta itemMeta = itemBlocker.getItemMeta();
				itemMeta.setCustomModelData(modelId);
				itemBlocker.setItemMeta(itemMeta);
			}

			ArrayList<ConfigurationSection> sections = new ArrayList<>();
			sections.add(itemSection.getConfigurationSection("blocked_commands_in_hands"));
			sections.add(itemSection.getConfigurationSection("blocked_commands_in_inventory"));

			map.put(itemBlocker, sections);
		}
		return map;
	}

	/**
	 * @param command command to test.
	 * @return true if the command is blocked by an item else false.
	 */
	private boolean isBlockedCommand(String command) {
		Map<String, List<String>> blockedCommandsInHand = getBlockedCommandsInHand();
		Map<String, List<String>> blockedCommandsInInventory = getBlockedCommandsInInventory();

		for (Map.Entry<String, List<String>> entry : blockedCommandsInHand.entrySet()) {
			for (String cmd : entry.getValue()) {
				if (cmd.equalsIgnoreCase(command)) return true;
			}
		}

		for (Map.Entry<String, List<String>> entry : blockedCommandsInInventory.entrySet()) {
			for (String cmd : entry.getValue()) {
				if (cmd.equalsIgnoreCase(command)) return true;
			}
		}

		return false;
	}

	/**
	 * @return a map with as key the item id in items.yml and as value the list of blocked commands when
	 * item is in the main or offhand of the player.
	 */
	private @NotNull Map<String, List<String>> getBlockedCommandsInHand() {
		Map<String, List<String>> map = new HashMap<>();
		FileConfiguration cfg = configurationManager.getConfigurationFile("items.yml");

		for (String id : cfg.getKeys(false)) {
			ConfigurationSection blockedCommandsInHandsSection;
			blockedCommandsInHandsSection = cfg.getConfigurationSection(id + ".blocked_commands_in_hands");
			if (blockedCommandsInHandsSection == null) continue;

			List<String> blockedCommandsInHand = new ArrayList<>(blockedCommandsInHandsSection.getKeys(false));
			map.put(id, blockedCommandsInHand);
		}

		return map;
	}

	/**
	 * @return a map with as key the item id in items.yml and as value the list of blocked commands when
	 * item is in the player inventory.
	 */
	private @NotNull Map<String, List<String>> getBlockedCommandsInInventory() {
		Map<String, List<String>> map = new HashMap<>();
		FileConfiguration cfg = configurationManager.getConfigurationFile("items.yml");

		for (String id : cfg.getKeys(false)) {
			ConfigurationSection blockedCommandsInHandsSection;
			blockedCommandsInHandsSection = cfg.getConfigurationSection(id + ".blocked_commands_in_inventory");
			if (blockedCommandsInHandsSection == null) continue;

			List<String> blockedCommandsInHand = new ArrayList<>(blockedCommandsInHandsSection.getKeys(false));
			map.put(id, blockedCommandsInHand);
		}

		return map;
	}

}
