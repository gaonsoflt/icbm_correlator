package com.bbaek.icbm;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HadoopManager {

	public boolean download(String targetPath, String localPath) {
		try {
			do {
				System.out.println("Download...(" + targetPath + " > " + localPath + ")");
				Process process = Runtime.getRuntime()
							.exec("/data/hadoop/bin/hadoop fs -get " + targetPath + " " + localPath);
				process.waitFor();
				InputStream inputStream = process.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String line = "";
				while ((line = bufferedReader.readLine()) != null) {
					System.out.println("download " + line);
				}
			} while (!new File(localPath).exists());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean downloadByShell(String downloadShellFile, String writeLocalFile) {
		System.out.println("Download by shell...");
		try {
			if(!checkFile(downloadShellFile)) {
				return false;
			}

			String command = "sh " + downloadShellFile + " " + strNow() + " " + writeLocalFile;
			do {
				String output = executeCommand(command);
				System.out.println("download: " + output);
			} while (!new File(writeLocalFile).exists());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean uploadByShell(String uploadShellFile, String readLocalFile) {
		System.out.println("Upload by shell...");
		String command = "sh " + uploadShellFile + " " + readLocalFile + " " + strNow();
		String output = "";
		try {
			if(!checkFile(uploadShellFile) || !checkFile(readLocalFile)) {
				return false;
			}
			output = executeCommand(command);
			System.out.println("upload: " + output);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean checkFile(String file) {
		if(new File(file).exists()) {
			System.out.println("check file: exists\t" + file);
			return true;
		}
		System.out.println("check file: not exists\t" + file);
		return false;
	}
	
    private static String strNow() {
    	if(Main.TEST_DATE != null) {
    		return Main.TEST_DATE;
    	} else {
    		return new SimpleDateFormat("yyyyMMdd").format(new Date());
    	}
    }
	
	private String executeCommand(String command) throws Exception {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			System.out.println("execute command: " + command);
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return output.toString();
	}
	
//	public void download(String filePath) {
//		File file = null;
//		try {
//			do {
//				System.out.println("Download...");
//				Process process = Runtime.getRuntime()
//						.exec("hadoop fs -get " + DOWN_FILEPATH + filePath + DOWN_FILENAME + " " + LOCALPATH);
//				process.waitFor();
//				InputStream inputStream = process.getInputStream();
//				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//				String line = "";
//				while ((line = bufferedReader.readLine()) != null) {
//					System.out.println("download " + line);
//				}
//				file = new File(DOWN_FILE);
//			} while (!file.exists());
//
//			System.out.println("Read downloaded file...");
////			final ArrayList<String> list = new ArrayList<String>();
//			final StringBuffer sb = new StringBuffer();
//			final Correlator corr = new Correlator();
//			
//			FileManager.readFile(DOWN_FILE, new FileManager.FileReadCallback() {
//				@Override
//				public void doRead(String readData) {
////					list.add(readData);
//					corr.addData(readData);
//					sb.append(corr.getCorrelation() + "\n");
//				}
//			});
//			
//			FileManager.writeFile(WRITE_FILE, sb.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} finally {
//			if(file != null) file.delete();
//		}
//	}
}
