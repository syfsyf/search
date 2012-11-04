package org.syfsyf.search.utils;

import java.io.File;

public class FileWaker {
	public interface Visitor {
		public boolean file(File f);
	}

	public static void walk(File root, Visitor visitor) {
		File[] list = root.listFiles();
		for (File f : list) {

			if (f.isDirectory()) {
				if (visitor.file(f)) {
					walk(f, visitor);
				}
			} else {
				visitor.file(f);
			}
		}
	}
}
