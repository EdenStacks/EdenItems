package fr.edencraft.edenitems.lang;

import fr.edencraft.edenitems.utils.ColoredText;

import java.util.List;

public class French implements Language {
	@Override
	public String getReloadFiles(List<String> filesName) {
		if (filesName.size() == 1) {
			return prefix + new ColoredText(
					"&aLe fichier de configuration &7(&f" + filesName.stream().findFirst().get() + "&7) " +
							"&aa été reload !"
			).treat();
		} else {
			return prefix + new ColoredText(
					"&aTout les fichiers de configuration ont été reload !"
			).treat();
		}
	}

	@Override
	public String getUnknownConfigFile(String name) {
		return prefix + new ColoredText(
				"&cLe fichier de configuration &7(&f" + name + "&7) &cn'existe pas !"
		).treat();
	}
}
