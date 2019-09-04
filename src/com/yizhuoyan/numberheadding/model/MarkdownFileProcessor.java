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

	public MarkdownFileProcessor(Path path) {
		this.path = path;
	}

	private Path getOutputPath() {
		Path outPath = path.resolveSibling(path.getFileName().toString() + ".bak");
		return outPath;
	}

	private BufferedReader tryReader() throws IOException {
		Charset fileCharset = GBK;
		// 尝试读取字符编码utf-8
		try (BufferedReader in = Files.newBufferedReader(path, UTF8)) {
			try {
				in.readLine();
				fileCharset = UTF8;
			} catch (MalformedInputException e) {
				// 说明不是utf-8,尝试gbk
				System.out.println("说明不是utf-8,尝试gbk");
				fileCharset = GBK;
			}
		}
		return Files.newBufferedReader(path, fileCharset);
	}

	public void process() throws Exception {
		Path outPath = getOutputPath();

		try (BufferedReader in = tryReader(); BufferedWriter out = Files.newBufferedWriter(outPath, UTF8)) {

			HeadingModel root = HeadingModel.root();
			String line = null;
			boolean hasBlankLine = false;
			while ((line = in.readLine()) != null) {

				HeadingModel hm = HeadingModel.parseHeading(line, root);

				if (hm == null) {
					out.write(line);
					out.newLine();
					hasBlankLine = line.trim().length() == 0;
				} else {
					if(!hasBlankLine) {
						out.newLine();
					}
					out.write(hm.process());
					out.newLine();
					
				}
				out.flush();
			}
		}
		// 修改文件名
		Path temp = path.resolveSibling(System.nanoTime() + "");
		Files.move(path, temp);
		Files.move(outPath, path);
		Files.move(temp, outPath);
	}

}
