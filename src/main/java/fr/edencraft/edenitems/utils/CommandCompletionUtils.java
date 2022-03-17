package fr.edencraft.edenitems.utils;

import fr.edencraft.edenitems.EdenItems;
import fr.edencraft.edenitems.manager.ConfigurationManager;

import java.util.ArrayList;
import java.util.List;

public class CommandCompletionUtils {

	/**
	 * This method return into string list all configuration file available in this plugin.
	 *
	 * @return The list of available configuration file.
	 */
	public static List<String> getConfigurationFilesName() {
		List<String> cfgList = new ArrayList<>();
		ConfigurationManager configurationManager = EdenItems.getINSTANCE().getConfigurationManager();
		configurationManager.getFilesMap().forEach((file, fileConfiguration) -> {
			cfgList.add(file.getName());
		});
		return cfgList;
	}

}
