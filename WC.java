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
	
	public static int CharCount(File file) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(file));
		charNum = 0;
		//ʶ��ǿհ��ַ�(ע�⣺�����ַ�Ĭ��1.5��)
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
	
	public static int WordCount(File file) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(file));
		wordNum = 0;
		//����ģʽ�������ַ���2�����ϻ���aI��+�ǵ����ַ���
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
	
	public static int LineCount(File file) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(file));
		lineNum = 0;
		while(bf.readLine()!=null) {//����һ����Ч��Ϣ������+1
			lineNum++;
		}
		bf.close();
		return lineNum;
	}
	
	public static int BlankLineCount(File file) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(file));
		blankNum = 0;
		//�հ��У�ȫ�հ��ַ����߽�����һ������ʾ�ַ�
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
	
	public static int CommentLineCount(File file) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(file));
		commentNum = 0;
		Pattern pattern0 = Pattern.compile("\\s?+\\w");//�����б�ʶ
		Pattern pattern1 = Pattern.compile("//");//����ע�ͱ�ʶ
		Pattern pattern2 = Pattern.compile("\\s*[/][*]");//����ע�Ϳ�ͷ
		Pattern pattern3 = Pattern.compile("\\s*[*][/]");//����ע�ͽ�β
		String str;
		boolean flag=false;//��Ƕ���ע�͵Ŀ�ʼ������
		while((str = bf.readLine())!=null) {
			//���ڵ���ע�Ͳ��Ҹ��в��Ǵ�����
			if(!pattern0.matcher(str).find()&&pattern1.matcher(str).find()) {
				commentNum++;
				continue;
			}
			if(pattern2.matcher(str).find()) {
				commentNum++;
				flag = true;
				continue;
			}
			if(pattern3.matcher(str).find()) {
				commentNum++;
				flag = false;
				continue;
			}	
			if(flag) {
				commentNum++;
			}
		}
		bf.close();
		return commentNum;
	}
	
	public static int SyntaxLineCount(File file) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(file));
		syntaxNum = 0;
		//�����У����ֳ��������Ŀ���ʵ�ַ�
		Pattern pattern = Pattern.compile("\\s*+\\w{2,}");
		String str;
		while((str = bf.readLine())!=null) {
			Matcher m = pattern.matcher(str);
			if(m.find()) {
				syntaxNum++;
			}
		}
		bf.close();
		return syntaxNum;
	}
	
	//�ݹ����Ŀ¼�µ��ļ�����
	public static void Traverse(File file,int level) {
		if(isCFile(file)||file.isDirectory()) {
			for(int i=0;i<level;i++)
			System.out.print("���� ");
			System.out.println(file.getName());
			if(file.isDirectory()){
				File[] files = file.listFiles();
				for(File temp:files) {
					Traverse(temp,level+1);
				}
			}
		}
	}
  
	//�ж��ļ��Ƿ�Ϊ.c�ļ�
	public static boolean isCFile(File file) {
		Pattern pattern = Pattern.compile("[\\S]+[.c][\\s]*");
		if(pattern.matcher(file.getAbsolutePath()).matches())
			return true;
		return false;
	}
	
	//�ж��ļ��Ƿ�Ϸ�
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
		System.out.print("��������ļ�����Ŀ¼�ľ���·����");
		File directory = new File(directory_path = Scan());
		while(!JudgeFile(directory)) {
			System.out.print("\n·�������ڣ��������룺");
			directory = new File(directory_path = Scan());
		}
		
		
		
		System.out.print("\n����ָ���ʽ��\nwc.exe -c file.c//�����ļ� file.c ���ַ���\nwc.exe -w file.c//�����ļ� file.c �Ĵʵ���Ŀ\nwc.exe -l file.c//�����ļ� file.c ������\nwc.exe -s directory//�ݹ鴦��Ŀ¼�·����������ļ������Ǵ���ǰ�ļ�������wc.exe -s null��\nwc.exe -a file.c//���ظ����ӵ����ݣ������� / ���� / ע���У�\nwc.exe -e//�˳���������\n\n"+directory_path+"->");
		String operation,parameter,file_name;
		File file;
		while((operation=Scan())!=null) {
		    parameter = operation.substring(7,9);
		    if(parameter.equals("-e")) {
		    	break;
		    }	
		    file_name = operation.substring(10);
		    //���ļ�����
		    if(file_name.equals("null")) {
		    	file = directory;
		    }
		    else file = new File(directory_path+"\\"+file_name);
		    if(!JudgeFile(file)){
		    	System.out.print("\n�ļ���Ч��\n\n"+directory_path+"->");
		    	continue;
		    }
		    if(operation.substring(0,5).equals("wc.exe")) {
		    	System.out.print("\n������Ч��\n\n"+directory_path+"->");
		    	continue;
		    }
			switch(parameter) {
				case "-c":
					    System.out.println("�ַ�"+CharCount(file));
					    System.out.print("\n"+directory_path+"->");
						break;
				case "-w":
						System.out.println("����"+WordCount(file));
						System.out.print("\n"+directory_path+"->");
					    break;
				case "-l":
						System.out.println("����"+LineCount(file));
						System.out.print("\n"+directory_path+"->");
						break;
				case "-s":
					System.out.println("�����������ļ���������Ŀ¼���£�");
						Traverse(file,0);
						System.out.print("\n"+directory_path+"->");
						break;
				case "-a":
						System.out.println("����"+BlankLineCount(file));
						System.out.println("ע��"+CommentLineCount(file));
						System.out.println("����"+SyntaxLineCount(file));
						System.out.print("\n"+directory_path+"->");
						break;
				default:System.out.print("\n������Ч��\n\n"+directory_path+"->");
			}
		}
	}
}
