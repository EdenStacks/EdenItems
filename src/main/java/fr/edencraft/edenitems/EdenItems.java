package fr.edencraft.edenitems;

import fr.edencraft.edenitems.lang.Language;
import fr.edencraft.edenitems.manager.CommandManager;
import fr.edencraft.edenitems.manager.ConfigurationManager;
import fr.edencraft.edenitems.manager.ListenerManager;
import fr.edencraft.edenitems.utils.ColoredText;
import fr.edencraft.edenitems.utils.ConfigurationUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class EdenItems extends JavaPlugin {

	private static EdenItems INSTANCE;
	private final String pluginPrefix = new ColoredText("&dEden&eItems &f&l» &r").treat();
	private ConfigurationManager configurationManager;

	public static EdenItems getINSTANCE() {
		return INSTANCE;
	}

	@Override
	public void onEnable() {
		long delay = System.currentTimeMillis();

		INSTANCE = this;

		this.configurationManager = new ConfigurationManager(this);
		this.configurationManager.setupFiles();

		new ListenerManager(this);
		new CommandManager(this);

	}

	@Override
	public void onDisable() {
		this.configurationManager.saveFiles();
	}

	public void log(Level level, String message) {
		switch (level.getName()) {
			default -> Bukkit.getLogger()
					.log(level, "[" + getPlugin(EdenItems.class).getName() + "] " + message);
			case "INFO" -> Bukkit.getLogger()
					.log(level, "§a[" + getPlugin(EdenItems.class).getName() + "] " + message);
			case "WARNING" -> Bukkit.getLogger()
					.log(level, "§e[" + getPlugin(EdenItems.class).getName() + "] " + message);
			case "SEVERE" -> Bukkit.getLogger()
					.log(level, "§c[" + getPlugin(EdenItems.class).getName() + "] " + message);
		}
	}

	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	public String getPrefix() {
		return pluginPrefix;
	}

	public Language getLanguage() {
		return (Language) ConfigurationUtils.getLanguage();
	}
}
