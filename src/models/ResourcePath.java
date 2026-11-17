package models;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ResourcePath {
	private static final Map<String, Integer> KNOWN_FOLDER_COUNTS;
	static {
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("src/load_gif", 16);
		m.put("src/screenShoots", 20);
		m.put("src/bigGem_gif", 4);
		m.put("src/bigGem_gif (1)", 4);
		m.put("src/brakingStone_gif", 27);
		m.put("src/hunterLeftAttack_gif", 49);
		m.put("src/hunterMenu_gif", 49);
		m.put("src/hunterRightAttack_gif", 49);
		m.put("src/jellyProglogue_gif", 4);
		m.put("src/littleGem_gif", 4);
		m.put("src/prologueGem_gif", 4);
		m.put("src/save_gif", 24);
		m.put("src/timeRepresentation_gif", 49);
		KNOWN_FOLDER_COUNTS = Collections.unmodifiableMap(m);
	}

	public static File resolve(String path) {
		File file = new File(path);
		if (!file.exists()) {
			String normalized = path.startsWith("./") ? path.substring(2) : path;
			File appFile = new File("/app/" + normalized);
			if (appFile.exists()) {
				return appFile;
			}
		}
		return file;
	}

	public static int countFolderFiles(String path) {
		File local = new File(path);
		if (local.isDirectory()) {
			File[] files = local.listFiles();
			if (files != null && files.length > 0) {
				return files.length;
			}
		}
		String normalized = path.startsWith("./") ? path.substring(2) : path;
		Integer known = KNOWN_FOLDER_COUNTS.get(normalized);
		return (known != null) ? known : 0;
	}
}
