package com.bbaek.icbm;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

public class Main implements Daemon, Runnable {
	static private String LOCALPATH = "/data/hadoop/icbm";
	static private String DOWN_FILEPATH = "/user/hive/warehouse/icbm_sensing_data/md=";
	static private String DOWN_FILENAME = "/part-m-00000";
	static private String DOWN_FILE = LOCALPATH + DOWN_FILENAME;
	// static private String DOWN_FILE = DOWN_FILENAME;
	static private String WRITE_FILENAME = "/sensing_correlation.csv";
	static private String WRITE_FILE = LOCALPATH + WRITE_FILENAME;
	
	public static void main(String[] args) {
		System.out.println();
		// if(args.length < 1) {
		// System.out.println("Usage: Input arg(date)");
		// return;
		// }
		//
		// System.out.println("Input arg: " + args[0]);
		// HadoopManager hm = new HadoopManager();
		// final DBManager db = new DBManager();
		// if(hm.download(DOWN_FILEPATH + args[0] + DOWN_FILENAME, LOCALPATH)) {
		// System.out.println("Read downloaded file...");
		// final StringBuffer sb = new StringBuffer();
		// final Correlator corr = new Correlator();
		//
		// FileManager.readFile(DOWN_FILE, new FileManager.FileReadCallback() {
		// @Override
		// public void doRead(String readData) {
		// if(readData != null) {
		// corr.addData(readData);
		// String corrValue = corr.getCorrelation();
		// sb.append(corrValue + "\n");
		// db.updateInsert(corrValue);
		// }
		// }
		//
		// @Override
		// public void endRead() {
		// FileManager.writeFile(WRITE_FILE, sb.toString());
		// FileManager.deleteFile(DOWN_FILE);
		// }
		// });
		// }
		// db.closeConnection();
	}
	
	private String VER = "1.4_20170201-2052";

	private String status = "";
	private Thread thread = null;
	private HadoopManager hm = null;
	private DBManager db;
	TimeLogger tlogger = null;
	
	// param
	private String UPLOAD_SHELL_FILE = "";
	private String DOWNLOAD_SHELL_FILE = "";
	private String READ_LOCAL_FILE = ""; 
	private String WRITE_LOCAL_FILE = "";
	private int INTERVAL = 5000;
	public static String TEST_DATE = null;

	@Override
	public void init(DaemonContext context) throws DaemonInitException, Exception {
		System.out.println("init...");
		String[] args = context.getArguments();
		if (args != null) {
			if (args.length < 4) {
				System.out.println("Usage: upload_shell_file download_shell_file read_local_file write_local_file [interval] [testDate(yyyyMMdd)]");
				throw new DaemonInitException("Argments is not enough.");
			}
			UPLOAD_SHELL_FILE = args[0];
			DOWNLOAD_SHELL_FILE = args[1];
			READ_LOCAL_FILE = args[2];
			WRITE_LOCAL_FILE = args[3];
			if(args.length > 4 && args[4] != null) {
				try{
					INTERVAL = Integer.parseInt(args[4]);
				} catch (NumberFormatException nfe) {
					//nfe.getStackTrace();
				}
			}
			if(args.length > 5 && args[5] != null) {
				TEST_DATE = args[5];
			}
			System.out.println("##############################################################");
			System.out.println("ICBM hadoop manager (v" + VER + ")");
			System.out.println("arguments");
			System.out.println("==============================================================");
			System.out.println("UPLOAD_SHELL_FILE: " + UPLOAD_SHELL_FILE);
			System.out.println("DOWNLOAD_SHELL_FILE: " + DOWNLOAD_SHELL_FILE);
			System.out.println("READ_LOCAL_FILE: " + READ_LOCAL_FILE);
			System.out.println("WRITE_LOCAL_FILE: " + WRITE_LOCAL_FILE);
			System.out.println("INTERVAL: " + INTERVAL);
			System.out.println("##############################################################");
		}
		status = "INITED";
		this.thread = new Thread(this);
		this.hm = new HadoopManager();
		this.tlogger = new TimeLogger();
		System.out.println("init OK.");
		System.out.println();
	}

	@Override
	public void start() {
		System.out.println("status: " + status);
		System.out.println("start...");
		status = "STARTED";
		this.thread.start();
		System.out.println("start OK.");
		System.out.println();
	}

	@Override
	public void stop() throws Exception {
		System.out.println("status: " + status);
		System.out.println("stop...");
		status = "STOPED";
		this.thread.join(10);
		System.out.println("stop OK.");
		System.out.println();
	}

	@Override
	public void destroy() {
		System.out.println("status: " + status);
		System.out.println("destroy...");
		status = "DESTROIED";
		System.out.println("destroy OK.");
		System.out.println();
	}
	
	private int exeCnt = 0;
	@Override
	public void run() {
		while (true) {
			tlogger.start();
			try {
				if (hm.uploadByShell(UPLOAD_SHELL_FILE, READ_LOCAL_FILE)) {
					FileManager.deleteFile(READ_LOCAL_FILE);
					tlogger.logSpendTime("uploadByShell");
					
					if (hm.downloadByShell(DOWNLOAD_SHELL_FILE, WRITE_LOCAL_FILE)) {
						db = new DBManager();
						final StringBuffer sb = new StringBuffer();
						final Correlator corr = new Correlator();
						exeCnt = 0;
						tlogger.logSpendTime("downloadByShell");
						FileManager.readFile(WRITE_LOCAL_FILE, new FileManager.FileReadCallback() {
							@Override
							public void endRead() {
								System.out.println(sb.toString());
								System.out.println("Executed data: " + exeCnt);
								FileManager.deleteFile(WRITE_LOCAL_FILE);
								db.closeConnection();
								tlogger.logSpendTime("readFile");
							}

							@Override
							public void doRead(String readData) throws Exception {
								if (readData != null) {
									if(readData.indexOf("NaN") < 0){
										corr.addData(readData);
										String corrValue = corr.getCorrelation(",");
										sb.append(corrValue + "\n");
										db.updateInsert(corrValue);
										exeCnt++;
									}
								}
							}
						});
					}
				}
				Thread.sleep(INTERVAL); // refresh 5sec
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
