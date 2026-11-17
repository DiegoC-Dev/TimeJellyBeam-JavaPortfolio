package models;

import java.io.File;

public class ResourcePath {
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
}
