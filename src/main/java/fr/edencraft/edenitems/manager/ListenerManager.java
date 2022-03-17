package fr.edencraft.edenitems.manager;

import fr.edencraft.edenitems.listener.Command;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {

	public ListenerManager(Plugin plugin) {
		PluginManager pm = Bukkit.getPluginManager();

		pm.registerEvents(new Command(), plugin);
	}

}
