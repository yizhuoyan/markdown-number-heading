package com.yizhuoyan.numberheadding;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.yizhuoyan.numberheadding.model.MarkdownFileProcessor;

public class Application {
	
	public static void main(String[] args) throws Exception {
		//查找目录下所有md文件
		String dir=args.length==0?".":args[0];
		
		Files.walkFileTree(Paths.get(dir), new  SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if(file.getFileName().toString().endsWith(".md")) {
					try {
						System.out.print("处理文件:"+file);
						MarkdownFileProcessor mfp=new MarkdownFileProcessor(file);
						mfp.process();
						System.out.print("...ok");
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
						System.out.println();
					}
					
				}
				return FileVisitResult.CONTINUE;
			}
		});
		
		
	}
	

	

}
