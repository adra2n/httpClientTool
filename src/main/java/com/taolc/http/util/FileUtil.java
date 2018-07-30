package com.taolc.http.util;

import java.io.*;

/**
 * 文件操作工具类常用方法
 * @author taolc
 *
 */
public class FileUtil {

	//utf8 编码格式
	private static String UTF8 = "utf8";

	/**
	 * 默认用utf8获取字符串BufferedReader流
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static BufferedReader getStringBufferedReader(String content) throws Exception{
		return getStringBufferedReader(content, UTF8);
	}

	/**
	 * 获取字符串BufferedReader流
	 * @param content   传入字符串
	 * @param encoding   编码格式
	 * @return
	 * @throws Exception
	 */
	public static BufferedReader getStringBufferedReader(String content,String encoding) throws Exception{
		return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes()), encoding));
	}

	/**
	 * 获取文件BufferedReader流
	 * @param path  文件路径
	 * @param fileName 文件名称
	 * @param encoding 编码格式
	 * @return BufferedReader对象
	 * @throws Exception
	 */
	public static BufferedReader getFileBufferedReader(String path,String fileName,String encoding) throws Exception{
		return new BufferedReader(new InputStreamReader(new FileInputStream(path+File.separator+fileName),encoding));
	}

	/**
	 * 获取文件BufferedReader流
	 * @param readPath
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static BufferedReader getFileBufferedReader(String readPath,String encoding) throws Exception {
		return new BufferedReader(new InputStreamReader(new FileInputStream(readPath),encoding));
	}

	/**
	 * 获取文件BufferedReader流(默认utf8)
	 * @param readPath
	 * @return
	 * @throws Exception
	 */
	public static BufferedReader getFileBufferedReader(String readPath) throws Exception {
		return new BufferedReader(new InputStreamReader(new FileInputStream(readPath),UTF8));
	}

	/**
	 * 在原来文件的基础上追加文件内容(默认utf-8)
	 * @param outPath   文件生成路径
	 * @param fileName  文件名称 
	 * @param content	文件内容
	 * @throws Exception
	 */
	public static void appendFile(String outPath,String fileName,String content) throws Exception{
		appendFile(outPath,fileName,content,UTF8);
	}
	
	/**
	 * 在原来文件的基础上追加文件内容
	 * @param outPath   文件生成路径
	 * @param fileName  文件名称 
	 * @param content	文件内容
	 * @throws Exception
	 */
	public static void appendFile(String outPath,String fileName,String content,String encoding) throws Exception{
		create(outPath,fileName,content,true,encoding);
	}
	
	/**
	 * 覆盖原来文件内容(默认utf-8)
	 * @param outPath   文件生成路径
	 * @param fileName  文件名称 
	 * @param content	文件内容
	 * @throws Exception
	 */
	public static void createFile(String outPath,String fileName,String content) throws Exception{
		createFile(outPath, fileName, content, UTF8);
	}
	
	/**
	 * 覆盖原来文件内容
	 * @param outPath   文件生成路径
	 * @param fileName  文件名称 
	 * @param content	文件内容
	 * @param encoding    编码格式
	 * @throws Exception
	 */
	public static void createFile(String outPath,String fileName,String content,String encoding) throws Exception{
		create(outPath, fileName, content, false, encoding);
	}
	
	/**
	 * 生成文件
	 * @param outPath   文件生成路径
	 * @param fileName  文件名称 
	 * @param content	文件内容
	 * @param isAppend    true 追加文件内容 false 覆盖文件内容
	 * @param encoding  编码格式
	 * @throws Exception
	 */
	public static void create(String outPath,String fileName,String content,boolean isAppend,String encoding) throws Exception{
		//判断文件夹是否存在，不存在就新建
		File file = new File(outPath);
		if(!file.exists()){
			file.mkdirs();
		}
		String fileOutPath;
		if(outPath.endsWith("/") || outPath.endsWith("\\")){
			fileOutPath = outPath + fileName;
		}else{
			fileOutPath = outPath + File.separator + fileName;
		}
		//用io向文件中写
		OutputStreamWriter osw;
		osw = new OutputStreamWriter(new FileOutputStream(fileOutPath,isAppend),encoding);
		//开始写
		PrintWriter pw = new PrintWriter(osw);
		pw.write(content);
		pw.flush();
		pw.close();
	}
}
