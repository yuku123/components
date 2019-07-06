package com.zifang.demo.jdk.java.io.nio.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.util.Date;

public class FilesDemo {

	public static void readAttributes() {
		Path filePath = Paths.get("d:\\test.txt");
		try {
			BasicFileAttributes ra = Files.readAttributes(filePath, BasicFileAttributes.class);
			System.out.println("CREATION TIME : " + ra.creationTime());
			System.out.println("LAST ACCESS TIME : " + ra.lastAccessTime());
			System.out.println("FILE SIZE : " + ra.size());
			System.out.println("LAST MODIFIED : " + ra.lastModifiedTime());
			System.out.println("IS SYSBOLIC LINK : " + ra.isSymbolicLink());
			System.out.println("IS FOLDER : " + ra.isDirectory());
			System.out.println("IS FILE : " + ra.isRegularFile());
		} catch (IOException e) {
		}
	}

	public static void readAttributes2() throws Exception {
		Path path = Paths.get("D:/test.txt");
		BasicFileAttributeView basicview = Files.getFileAttributeView(path, BasicFileAttributeView.class);
		BasicFileAttributes basicfile = basicview.readAttributes();
		System.out.println("创建时间" + new Date(basicfile.creationTime().toMillis()));
		System.out.println("文件大小" + basicfile.size());
		DosFileAttributeView dosview = Files.getFileAttributeView(path, DosFileAttributeView.class);
		dosview.setHidden(true);
		dosview.setReadOnly(false);
		dosview.setArchive(false);
		dosview.setSystem(false);;
	}

	public static void main(String[] args) throws Exception {
		// readAttributes();
		readAttributes2();
		
	}
	
	public void writeFile(String s) {
		try(FileOutputStream fos = new FileOutputStream(new File("d:/a.txt"))){
			fos.write(s.getBytes());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
