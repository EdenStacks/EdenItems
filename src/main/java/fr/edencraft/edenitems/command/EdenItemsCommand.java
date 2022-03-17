package fr.edencraft.edenitems.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.edencraft.edenitems.EdenItems;
import fr.edencraft.edenitems.lang.Language;
import fr.edencraft.edenitems.manager.ConfigurationManager;
import fr.edencraft.edenitems.utils.CommandCompletionUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;

@CommandAlias("edenitems")
public class EdenItemsCommand extends BaseCommand {

	private final static Language LANGUAGE = EdenItems.getINSTANCE().getLanguage();
	private final static ConfigurationManager CONFIGURATION_MANAGER = EdenItems.getINSTANCE().getConfigurationManager();

	private static final String basePermission = "edenitems.command";

	@Subcommand("reload|rl")
	@Syntax("[fileName]")
	@CommandCompletion("@edenitemsreload")
	@CommandPermission(basePermission + ".reload")
	public static void onReload(CommandSender sender, @Optional String fileName){
		if (fileName != null && !fileName.isEmpty()) {
			if (CONFIGURATION_MANAGER.getConfigurationFile(fileName) != null) {
				CONFIGURATION_MANAGER.reloadFile(fileName);
				sender.sendMessage(LANGUAGE.getReloadFiles(Collections.singletonList(fileName)));
			} else {
				sender.sendMessage(LANGUAGE.getUnknownConfigFile(fileName));
			}
		} else {
			CONFIGURATION_MANAGER.reloadFiles();
			sender.sendMessage(LANGUAGE.getReloadFiles(CommandCompletionUtils.getConfigurationFilesName()));
		}
	}

}
