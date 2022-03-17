package fr.edencraft.edenitems.manager;

import co.aikar.commands.PaperCommandManager;
import fr.edencraft.edenitems.utils.CommandCompletionUtils;
import fr.edencraft.edenitems.command.EdenItemsCommand;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandManager {

	public CommandManager(Plugin plugin) {
		PaperCommandManager commandManager = new PaperCommandManager(plugin);
		commandManager.enableUnstableAPI("help");
		commandManager.registerCommand(new EdenItemsCommand());
		commandManager.getCommandCompletions().registerAsyncCompletion(
				"edenitemsreload",
				context -> {
					if (context.getPlayer() != null) {
						Player player = context.getPlayer();
						player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
					}
					return CommandCompletionUtils.getConfigurationFilesName();
				}
		);
	}

}
