package tst;

import java.io.File;

public class deleTest {

	/**
	 * @param args
	 */
	// public static void main(String[] args) {
	// deleteD("E:\\海南\\海南省司法厅%@%001005007\\");
	// }
	/**
	 * Deletes the directory passed in.
	 * 
	 * @param dir
	 *            Directory to be deleted
	 */
	private static void doDeleteEmptyDir(String dir) {

		boolean success = (new File(dir)).delete();

		if (success) {
			System.out.println("Successfully deleted empty directory: " + dir);
		} else {
			System.out.println("Failed to delete empty directory: " + dir);
		}

	}

	/**
	 * Deletes all files and subdirectories under "dir".
	 * 
	 * @param dir
	 *            Directory to be deleted
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	private static boolean deleteDir(File dir) {

		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so now it can be smoked
		return dir.delete();
	}

	/**
	 * Sole entry point to the class and application.
	 * 
	 * @param args
	 *            Array of String arguments.
	 */
	public static void deleteAll(String path) {
		doDeleteEmptyDir(path);
		boolean success = deleteDir(new File(path));
		if (success) {
			System.out.println("Successfully deleted populated directory: "
					+ path);
		} else {
			System.out.println("Failed to delete populated directory: " + path);
		}

	}

	public static void main(String[] args) {
		deleteAll("E:\\文件名");
	}

}
