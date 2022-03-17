package fr.edencraft.edenitems.utils;

import fr.edencraft.edenitems.EdenItems;
import fr.edencraft.edenitems.lang.English;
import fr.edencraft.edenitems.lang.French;
import fr.edencraft.edenitems.manager.ConfigurationManager;

public class ConfigurationUtils {

	private static final ConfigurationManager CM = EdenItems.getINSTANCE().getConfigurationManager();

	/**
	 * @return The language selected in the RandomTP configuration file.
	 */
	public static Object getLanguage() {
		String language = CM.getConfigurationFile("config.yml").getString("language");
		assert language != null;

		return switch (language) {
			case "fr" -> new French();
			case "en" -> new English();
			default -> throw new IllegalStateException("Unexpected value: " + language);
		};
	}

}
