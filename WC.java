package cn.lyl2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WC {
	private static int charNum;
	private static int wordNum;
	private static int lineNum;
	private static int blankNum;
	private static int commentNum;
	private static int syntaxNum;
	
	/*
	 *可显示字符的计数实现 
	 */
	public static int CharCount(File file) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(file));
		charNum = 0;
		//匹配可显示字符的正则表达式
		Pattern pattern = Pattern.compile("\\S");
		String str;
		while((str = bf.readLine())!=null) {
		Matcher m = pattern.matcher(str);
			while(m.find()) {
				charNum++;
			}
		}
		bf.close();
		return charNum;
	}
	
	/*
	 *单词的计数实现 
	 *单词条件：两个或以上的单词字符/单个字母的单词a、I
	 */
	public static int WordCount(File file) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(file));
		wordNum = 0;
		//匹配单词的正则表达式
		Pattern pattern = Pattern.compile("\\w{2,}\\b|[a]\\b|[I]\\b");
		Pattern pattern01 = Pattern.compile("\\d{2,}");
		String str;
		while((str = bf.readLine())!=null) {
			Matcher m = pattern.matcher(str);
			while(m.find()) {
				if(!pattern01.matcher(m.group()).matches()){
					wordNum++;
				}
				
			}
		}
		bf.close();
		return wordNum;
	}
	
	/*
	 * 行的计数实现
	 */
	public static int LineCount(File file) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(file));
		lineNum = 0;
		while(bf.readLine()!=null) {//读入一行有效信息，行数+1
			lineNum++;
		}
		bf.close();
		return lineNum;
	}
	
	/*
	 * 空行的计数实现
	 * 空行条件：该行有不超过一个的可显示字符
	 */
	public static int BlankLineCount(File file) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(file));
		blankNum = 0;
		//超过一个可显示字符的正则表达式
		Pattern pattern = Pattern.compile("[\\S]{2,}");
		String str;
		while((str = bf.readLine())!=null) {
			Matcher m = pattern.matcher(str);
			if(!m.find()) {
				blankNum++;
			}
		}
		bf.close();
		return blankNum;
	}
	
	/*
	 * 注释行的计数实现
	 * 注释行条件：不是代码行的前提下，出现“//”以及多行注释标识符
	 */
	public static int CommentLineCount(File file) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(file));
		commentNum = 0;
		Pattern pattern0 = Pattern.compile("\\s?+[[*][/]]*+\\w");//代码行标识
		Pattern pattern1 = Pattern.compile("//");//单行注释标识
		Pattern pattern2 = Pattern.compile("\\s*[/][*]");//多行注释开头
		Pattern pattern3 = Pattern.compile("\\s*[*][/]");//多行注释结尾
		String str;
		boolean flag=false;//标识当前行是否为多行注释的内容
		while((str = bf.readLine())!=null) {
			//存在单行注释并且改行不是代码行
			if(!pattern0.matcher(str).find()&&pattern1.matcher(str).find()) {
				commentNum++;
				continue;
			}
			//该行不是多行注释的内容且满足代码行的条件，则该行不是注释行
			if(!flag&&pattern0.matcher(str).find()) {
				continue;
			}
			//识别注释行的开头
			if(pattern2.matcher(str).find()) {
				commentNum++;
				flag = true;
				continue;
			}
			//识别注释行的结尾
			if(pattern3.matcher(str).find()) {
				if(pattern3.matcher(str).matches())
					commentNum++;
				flag = false;
				continue;
			}	
			//表示当前行是多行注释的内容
			if(flag) {
				commentNum++;
			}
		}
		bf.close();
		return commentNum;
	}
	
	/*
	 *代码行的计数实现
	 *代码行条件：超过两个可显示字符的代码语句
	 */
	public static int SyntaxLineCount(File file) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(file));
		Pattern pattern1 = Pattern.compile("\\s*[/][*]");//多行注释开头
		Pattern pattern2 = Pattern.compile("\\s*[*][/]");//多行注释结尾
		syntaxNum = 0;
		//代码行：出现超过两个的可现实字符
		Pattern pattern = Pattern.compile("\\s*+\\w{2,}");
		String str;
		boolean flag=false;//标识当前行是否为多行注释的内容
		while((str = bf.readLine())!=null) {
			if(pattern1.matcher(str).find()) {
				flag = true;
			}
			if(pattern2.matcher(str).find()) {
				flag = false;
			}
			if(!flag&&pattern.matcher(str).find()) {
				syntaxNum++;
			}
		}
		bf.close();
		return syntaxNum;
	}
	
	/*
	 * 递归遍历目录下的文件内容
	 */
	public static void Traverse(File file,int level) {
		if(isCFile(file)||file.isDirectory()) {
			for(int i=0;i<level;i++)
			System.out.print("—— ");//输出文件的层次
			System.out.println(file.getName());
			if(file.isDirectory()){//递归的条件
				File[] files = file.listFiles();
				for(File temp:files) {
					Traverse(temp,level+1);
				}
			}
		}
	}
  
	/*判断文件是否为.c文件*/
	public static boolean isCFile(File file) {
		Pattern pattern = Pattern.compile("[\\S]+[.c][\\s]*");
		if(pattern.matcher(file.getAbsolutePath()).matches())
			return true;
		return false;
	}
	
	/*判断文件是否存在并且为C文件*/
	public static boolean JudgeFile(File file) {
		if(file.isFile()) {
			if(isCFile(file)&&file.exists())
				return true;
			else 
				return false;	
		}
		else if(file.exists())
			return true;
		else return false;
	}
	
	public static String Scan() {
		Scanner scanner = new Scanner(System.in);
		String str = scanner.nextLine();
		return str;
	}
	
	
	
	public static void main(String[] args) throws IOException {
		String directory_path;
		System.out.print("输入操作文件所在目录的绝对路径：");
		File directory = new File(directory_path = Scan());
		while(!JudgeFile(directory)) {
			System.out.print("\n路径不存在！重新输入：");
			directory = new File(directory_path = Scan());
		}
		System.out.print("\n操作指令格式：\nwc.exe -c file.c//返回文件 file.c 的字符数\nwc.exe -w file.c//返回文件 file.c 的词的数目\nwc.exe -l file.c//返回文件 file.c 的行数\nwc.exe -s directory//递归处理目录下符合条件的文件（若是处理当前文件，输入wc.exe -s null）\nwc.exe -a file.c//返回更复杂的数据（代码行 / 空行 / 注释行）\nwc.exe -e//退出计数程序\n\n"+directory_path+"->");
		String operation,parameter,file_name;
		File file;
		while((operation=Scan())!=null) {
		    parameter = operation.substring(7,9);
		    if(parameter.equals("-e")) {
		    	break;
		    }	
		    file_name = operation.substring(10);
		    //对文件操作
		    if(file_name.equals("null")) {
		    	file = directory;
		    }
		    else file = new File(directory_path+"\\"+file_name);
		    if(!JudgeFile(file)){
		    	System.out.print("\n文件无效！\n\n"+directory_path+"->");
		    	continue;
		    }
		    if(operation.substring(0,5).equals("wc.exe")) {
		    	System.out.print("\n输入无效！\n\n"+directory_path+"->");
		    	continue;
		    }
			switch(parameter) {
				case "-c":
					    System.out.println("字符"+CharCount(file));
					    System.out.print("\n"+directory_path+"->");
						break;
				case "-w":
						System.out.println("单词"+WordCount(file));
						System.out.print("\n"+directory_path+"->");
					    break;
				case "-l":
						System.out.println("行数"+LineCount(file));
						System.out.print("\n"+directory_path+"->");
						break;
				case "-s":
					System.out.println("符合条件的文件及其所在目录如下：");
						Traverse(file,0);
						System.out.print("\n"+directory_path+"->");
						break;
				case "-a":
						System.out.println("空行"+BlankLineCount(file));
						System.out.println("注释行"+CommentLineCount(file));
						System.out.println("代码行"+SyntaxLineCount(file));
						System.out.print("\n"+directory_path+"->");
						break;
				default:System.out.print("\n输入无效！\n\n"+directory_path+"->");
			}
		}
	}
}