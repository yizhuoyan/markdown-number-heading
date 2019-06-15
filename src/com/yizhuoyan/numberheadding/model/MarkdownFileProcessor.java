package com.yizhuoyan.numberheadding.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MarkdownFileProcessor {

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
