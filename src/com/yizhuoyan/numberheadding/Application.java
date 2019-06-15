package com.yizhuoyan.numberheadding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.yizhuoyan.numberheadding.model.HeadingModel;

public class Application {
	public static void main(String[] args) throws Exception {
		//查找目录下所有md文件
		String dir=args.length==0?".":args[0];
		Files.walkFileTree(Path.of(dir), new  FileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

				if(file.getFileName().toString().endsWith(".md")) {
					try {
						System.out.print("处理文件:"+file);
						hanldeOneFile(file);
						System.out.println("...ok");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return FileVisitResult.CONTINUE;
			}
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {

				return FileVisitResult.CONTINUE;
			}
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}
			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				exc.printStackTrace();
				return FileVisitResult.CONTINUE;
			}
		});
	}
	

	public static void hanldeOneFile(Path path) throws Exception {

		BufferedReader in = Files.newBufferedReader(path, Charset.forName("gbk"));
		String line = null;
		// 尝试读取字符编码gbk
		try {
			line = in.readLine();
		} catch (MalformedInputException e) {
			// 说明不是gbk,尝试utf
			System.out.println("说明不是gbk,尝试utf-8");
			in.close();
			in = Files.newBufferedReader(path, Charset.forName("utf-8"));
			line = in.readLine();
		}
		Path outPath = path.resolveSibling(path.getFileName().toString() + ".bak");
		BufferedWriter out = Files.newBufferedWriter(outPath, Charset.forName("utf-8"));
		HeadingModel root=HeadingModel.root();
		while (line != null) {
			
			String trimRow = line.trim();
			if(trimRow.length()>0) {
				//System.out.println(Arrays.toString(trimRow.getBytes()));
				//System.out.println(trimRow);	
				if (trimRow.charAt(0) != '#') {
					
					out.write(trimRow);
					
					
				} else {
					HeadingModel hm = HeadingModel.parseHeading(trimRow,root);
					out.write(hm.process());
				}
				out.newLine();
				out.flush();
			}
			line = in.readLine();

		}
		in.close();
		out.close();
		Path temp = path.resolveSibling(System.nanoTime() + "");
		Files.move(path, temp);
		Files.move(outPath, path);
		Files.move(temp, outPath);

	}

}
