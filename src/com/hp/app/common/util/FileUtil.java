package com.hp.app.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	/**
	 * check the path is or not exists,if not make dirs.
	 * 
	 * @param filePath
	 */
	public static void makeSurePathExist(String filePath) {
		File file = new File(filePath);
		if(!file.exists()) {
			file.mkdirs();
		}
	}

	public static void delete(String filePath) {

		File f = new File(filePath);
		if (f.exists()) {
			if (f.isDirectory()) {
				int fCount = f.listFiles().length;
				if (fCount > 0) {
					File delFile[] = f.listFiles();
					for (int j = 0; j < fCount; j++) {
						if (delFile[j].isDirectory()) {
							delete(delFile[j].getAbsolutePath());
						}
						delFile[j].delete();
					}
				}
			}
			f.delete();
		}
	}

	public static List<String> getFileNameList(String filePath) {

		List<String> fileNameList = new ArrayList<String>();

		File f = new File(filePath);
		if (f.exists() && f.isDirectory()) {
			File file[] = f.listFiles();
			int fCount = f.listFiles().length;
			for (int j = 0; j < fCount; j++) {
				if (file[j].isDirectory()) {
					fileNameList.add(file[j].getAbsolutePath());
				} else {
					fileNameList.add(file[j].getName());
				}
			}
		}

		return fileNameList;
	}

	public static void copyAllFiles(String srcPath, String targetPath) {
		File f = new File(srcPath);
		File[] fileList = f.listFiles();
		if(null != fileList){
			for (File f1 : fileList) {
				if (f1.isFile()) {
					FileUtil.copyFile(srcPath + "//" + f1.getName(), targetPath
							+ "//" + f1.getName());
				}
				if (f1.isDirectory()) {
					copyAllFiles(f1.getPath().toString(),
							targetPath + "//" + f1.getName());
				}
			}
		}
	}

	public static void copyFile(String src, String target) {
		InputStream is = null;
		OutputStream os = null;

		try {
			is = new FileInputStream(src);
			os = new FileOutputStream(target);
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = is.read(buff, 0, buff.length)) != -1) {
				os.write(buff, 0, len);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

	}

	public static void parseDir(String fromPath, String toPath) {
		// Create a new folder
		FileUtil.makeSurePathExist(toPath);

		// Create a virtual path
		File file = new File(fromPath);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				if (f.isDirectory()) {
					parseDir(f.getPath(), toPath + "//" + f.getName());
				}
			}
		}
	}
}
