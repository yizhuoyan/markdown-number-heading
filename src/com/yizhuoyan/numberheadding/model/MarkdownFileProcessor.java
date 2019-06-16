package com.yizhuoyan.numberheadding.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class MarkdownFileProcessor {

	public static void hanldeOneFile(Path path) throws Exception {
		Charset UTF8=Charset.forName("UTF-8");
		Charset GBK=Charset.forName("GBK");
		
		// 尝试读取字符编码gbk		
		BufferedReader in = Files.newBufferedReader(path,GBK);
		String line = null;
		try {
			line = in.readLine();
		} catch (MalformedInputException e) {
			// 说明不是gbk,尝试utf
			System.out.println("说明不是gbk,尝试utf-8");
			in.close();
			in = Files.newBufferedReader(path,UTF8);
			line = in.readLine();
		}
		Path outPath = path.resolveSibling(path.getFileName().toString() + ".bak");
		BufferedWriter out = Files.newBufferedWriter(outPath,UTF8);
		HeadingModel root=HeadingModel.root();
		while (line != null) {
			String trimRow = line.trim();
			if(trimRow.length()>0) {

				if (trimRow.charAt(0) != '#') {
					out.write(line);
					out.newLine();
				} else {
					HeadingModel hm = HeadingModel.parseHeading(trimRow,root);
					if(hm!=null) {
						out.write(hm.process());
						out.newLine();
					}
				}
				
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
