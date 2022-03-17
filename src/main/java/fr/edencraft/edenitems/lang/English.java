package fr.edencraft.edenitems.lang;

import fr.edencraft.edenitems.utils.ColoredText;

import java.util.List;

public class English implements Language {
	@Override
	public String getReloadFiles(List<String> filesName) {
		if (filesName.size() == 1) {
			return prefix + new ColoredText(
					"&aThe configuration file &7(&f" + filesName.stream().findFirst().get() + "&7) " +
							"&ahas been reloaded !"
			).treat();
		} else {
			return prefix + new ColoredText(
					"&aAll configuration file has been reloaded !"
			).treat();
		}
	}

	@Override
	public String getUnknownConfigFile(String name) {
		return prefix + new ColoredText(
				"&cThe configuration file &7(&f" + name + "&7) &cdoes'nt exist !"
		).treat();
	}
}
