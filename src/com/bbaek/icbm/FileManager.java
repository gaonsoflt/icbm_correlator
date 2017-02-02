package com.bbaek.icbm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileManager {
	public interface FileReadCallback {
		void doRead(String readData) throws Exception;
		void endRead();
	}
	
	// ������ ���翩�θ� Ȯ���ϴ� �޼ҵ�
	public static Boolean fileIsLive(String isLivefile) {
		File f1 = new File(isLivefile);

		if (f1.exists()) {
			return true;
		} else {
			return false;
		}
	}

	// ������ �����ϴ� �޼ҵ�
	public static void fileMake(String makeFileName) {
		File f1 = new File(makeFileName);
		try {
			f1.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeFile(String fileName, String data) {
		FileWriter fr = null;
		BufferedWriter br = null;
		try {
			fr = new FileWriter(fileName);
			br = new BufferedWriter(fr);
			br.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void readFile(String fileName, FileReadCallback action) {
		System.out.println("read file(" + fileName + ")");
		FileReader fr = null;
		BufferedReader br = null;
		String data = "";
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			while ((data = br.readLine()) != null) {
				action.doRead(data);
			}
			action.endRead();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// ������ �����ϴ� �޼ҵ�
	public static void deleteFile(String deleteFileName) {
		File f = new File(deleteFileName);
		if(f.exists()) {
			f.delete();
			System.out.println("Deleted file: " + deleteFileName);
		} else {
			System.out.println("Not exist file: " + deleteFileName);
		}
	}

	// ������ �����ϴ� �޼ҵ�
	public static void fileCopy(String inFileName, String outFileName) {
		try {
			FileInputStream fis = new FileInputStream(inFileName);
			FileOutputStream fos = new FileOutputStream(outFileName);

			int data = 0;
			while ((data = fis.read()) != -1) {
				fos.write(data);
			}
			fis.close();
			fos.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ������ �̵��ϴ� �޼ҵ�
	public static void fileMove(String inFileName, String outFileName) {
		try {
			FileInputStream fis = new FileInputStream(inFileName);
			FileOutputStream fos = new FileOutputStream(outFileName);

			int data = 0;
			while ((data = fis.read()) != -1) {
				fos.write(data);
			}
			fis.close();
			fos.close();

			// �����ѵ� ���������� ������
			deleteFile(inFileName);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ���丮�� ���� ����Ʈ�� �д� �޼ҵ�
	public static List<File> getDirFileList(String dirPath) {
		// ���丮 ���� ����Ʈ
		List<File> dirFileList = null;

		// ���� ����� ��û�� ���丮�� ������ ���� ��ü�� ������
		File dir = new File(dirPath);

		// ���丮�� �����Ѵٸ�
		if (dir.exists()) {
			// ���� ����� ����
			File[] files = dir.listFiles();

			// ���� �迭�� ���� ����Ʈ�� ��ȭ��
			dirFileList = Arrays.asList(files);
		}

		return dirFileList;
	}

}
