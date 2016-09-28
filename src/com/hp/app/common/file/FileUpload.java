package com.hp.app.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.hp.app.common.util.FileUtil;
import com.hp.app.config.SysConfiger;

public class FileUpload {

	private static String uploadPath;
	
	static{
		uploadPath = SysConfiger.getProperty("file.upload.path");
        if("/".endsWith(uploadPath.substring(uploadPath.length()-1))){
        	uploadPath = uploadPath.substring(0, uploadPath.length()-1);
        }
		
		FileUtil.makeSurePathExist(uploadPath);
	}
	
	public static void upload(String filePath, File file, String fileName){
		
		String path = uploadPath + filePath;
		path = path.replaceAll(" ", "");
		FileUtil.makeSurePathExist(path);
				
		FileOutputStream fos = null;
        FileInputStream fis = null;

        try {
			fos = new FileOutputStream( path + fileName);
			fis = new FileInputStream(file);

	        byte[] buffer = new byte[1024];
			while((fis.read(buffer))!=-1){
			    fos.write(buffer);
			}
        
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
	        try {
				if(fos!=null) fos.close();
				if(fis!=null) fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public static void delete(String filePath){
		
		String path = uploadPath + filePath;
		path = path.replaceAll(" ", "");
		FileUtil.delete(path);
	}
	
	public static List<String> getFileNames(String filePath){
		
		String path = uploadPath + filePath;
		path = path.replaceAll(" ", "");
		return FileUtil.getFileNameList(path);
	}
	
	public static void move(String fromPath, String toPath){
		String fPath = uploadPath + fromPath;
		String tPath = uploadPath + toPath;
		
		fPath = fPath.replaceAll(" ", "");
		tPath = tPath.replaceAll(" ", "");
		
		FileUtil.parseDir(fPath, tPath);
		FileUtil.copyAllFiles(fPath, tPath);
		FileUtil.delete(fPath);
	}
}
