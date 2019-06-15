package com.yizhuoyan.numberheadding;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import com.yizhuoyan.numberheadding.model.MarkdownFileProcessor;

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
						MarkdownFileProcessor.hanldeOneFile(file);
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
	

	

}
