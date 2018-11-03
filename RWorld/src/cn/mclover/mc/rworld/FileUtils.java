package cn.mclover.mc.rworld;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class FileUtils {
	public static boolean deleteFolder(File file) {
		try {
			deleteDirectory(file);
			return true;
		} catch (IOException e) {
			L.error(e.getMessage());
		}
		return false;
	}

	public static void deleteDirectory(File directory) throws IOException {
		if (!directory.exists())
			return;
		cleanDirectory(directory);
		if (!directory.delete()) {
			String message = "Unable to delete directory " + directory + ".";
			throw new IOException(message);
		} else {
			return;
		}
	}

	public static void cleanDirectory(File directory) throws IOException {
		if (!directory.exists()) {
			String message = directory + " does not exist";
			throw new IllegalArgumentException(message);
		}
		if (!directory.isDirectory()) {
			String message = directory + " is not a directory";
			throw new IllegalArgumentException(message);
		}
		File files[] = directory.listFiles();
		if (files == null)
			throw new IOException("Failed to list contents of " + directory);
		IOException exception = null;
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			try {
				forceDelete(file);
			} catch (IOException ioe) {
				exception = ioe;
			}
		}

		if (null != exception)
			throw exception;
		else
			return;
	}

	public static void forceDelete(File file) throws IOException {
		if (file.isDirectory()) {
			deleteDirectory(file);
		} else {
			if (!file.exists())
				throw new FileNotFoundException("File does not exist: " + file);
			if (!file.delete()) {
				String message = "Unable to delete file: " + file;
				throw new IOException(message);
			}
		}
	}
	
	
	 public static boolean deleteFolderContents(File file)
	    {
	        try
	        {
	            cleanDirectory(file);
	            return true;
	        }
	        catch(IOException e)
	        {
	            L.error(e.getMessage());
	        }
	        return false;
	    }
	 
}
