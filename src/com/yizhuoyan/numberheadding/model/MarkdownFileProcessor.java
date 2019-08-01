package com.yizhuoyan.numberheadding.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MarkdownFileProcessor {
	private static final Charset UTF8 = Charset.forName("UTF-8");
	private static final Charset GBK = Charset.forName("GBK");
	final private Path path;
	Charset fileCharset;

	public MarkdownFileProcessor(Path path) {
		this.path = path;
	}
	private Path getOutputPath() {
		Path outPath = path.resolveSibling("bak." + path.getFileName().toString());
		return outPath;
	}
	private BufferedReader tryReader() throws IOException {
		// 尝试读取字符编码gbk
		try (BufferedReader in = Files.newBufferedReader(path, GBK)) {
			try {
				in.readLine();
				fileCharset = GBK;
			} catch (MalformedInputException e) {
				// 说明不是gbk,尝试utf
				System.out.println("说明不是gbk,尝试utf-8");
				fileCharset = UTF8;
			}
		}
		return Files.newBufferedReader(path, fileCharset);
	}

	public void process() throws Exception {
		Path outPath =getOutputPath();

		try (BufferedReader in = tryReader(); 
				BufferedWriter out = Files.newBufferedWriter(outPath, UTF8)) {

			HeadingModel root = HeadingModel.root();
			String line = null;
			while ((line = in.readLine()) != null) {
				if(line.length()==0)continue;
				HeadingModel hm = HeadingModel.parseHeading(line, root);
				if(hm==null) {
					out.write(line);
					out.newLine();
				}else {
					out.write(hm.process());
					out.newLine();
				}
				out.flush();
			}
		}
		//修改文件名
		Path temp = path.resolveSibling(System.nanoTime() + "");
		Files.move(path, temp);
		Files.move(outPath, path);
		Files.move(temp, outPath);
	}
	
}
