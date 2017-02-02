package com.bbaek.icbm;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main2 {
//	static private String LOCALPATH = "/data/hadoop/icbm"; 
//	static private String DOWN_FILEPATH = "/user/hive/warehouse/icbm_sensing_data/md=";
//	static private String DOWN_FILENAME = "/part-m-00000";
//	static private String DOWN_FILE = LOCALPATH + DOWN_FILENAME;
////	static private String DOWN_FILE = DOWN_FILENAME;
//	static private String WRITE_FILENAME = "/sensing_correlation.csv";
//	static private String WRITE_FILE = LOCALPATH + WRITE_FILENAME;
//		
//	public static void main(String[] args) {
//		if(args.length < 1) {
//			System.out.println("Usage: Input arg(date)");
//			return;
//		}
//		
//		System.out.println("Input arg: " + args[0]);
//		HadoopManager hm = new HadoopManager();
//		final DBManager db = new DBManager();
//		if(hm.download(DOWN_FILEPATH + args[0] + DOWN_FILENAME, LOCALPATH)) {
//			System.out.println("Read downloaded file...");
//			final StringBuffer sb = new StringBuffer();
//			final Correlator corr = new Correlator();
//			
//			FileManager.readFile(DOWN_FILE, new FileManager.FileReadCallback() {
//				@Override
//				public void doRead(String readData) {
//					if(readData != null) {
//						corr.addData(readData);
//						String corrValue = corr.getCorrelation();
//						sb.append(corrValue + "\n");
//						db.updateInsert(corrValue);
//					}
//				}
//
//				@Override
//				public void endRead() {
//					FileManager.writeFile(WRITE_FILE, sb.toString());
//					FileManager.deleteFile(DOWN_FILE);
//				}
//			});
//		}
//		db.closeConnection();
//	}
}
