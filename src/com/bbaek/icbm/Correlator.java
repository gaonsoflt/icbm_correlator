package com.bbaek.icbm;

import java.util.ArrayList;

public class Correlator {
	private ArrayList<String> dataList;
	
	public Correlator() {
		this.dataList = new ArrayList<>();
	}
	
	public Correlator(ArrayList<String> data) {
		if(data != null) {
			this.dataList = data;
		} else {
			this.dataList = new ArrayList<>();
		}
	}
	
	public void addData(String data) {
		dataList.add(data);
	}
	
	public String getCorrelation(String delimeter) throws Exception{
		String result = "";
		String[] dt = new String[dataList.size()];
		String[] tm = new String[dataList.size()];
		float[] t = new float[dataList.size()];
		float[] h = new float[dataList.size()];
		float[] n = new float[dataList.size()];
		float[] v = new float[dataList.size()];
		float sumT = 0, sumH = 0, sumN = 0, sumV = 0;
		float aveT = 0, aveH = 0, aveN = 0, aveV = 0;
		float sumcovTH = 0, sumcovTN = 0, sumcovTV = 0, sumcovHN = 0, sumcovHV = 0, sumcovNV = 0;
		float covTH = 0, covTN = 0, covTV = 0, covHN = 0, covHV = 0, covNV = 0;
		float sumsdT = 0, sumsdH = 0, sumsdN = 0, sumsdV = 0;
		float sdT = 0, sdH = 0, sdN = 0, sdV = 0;
		float corrTH = 0, corrTN = 0, corrTV = 0, corrHN = 0, corrHV = 0, corrNV = 0;

		for (int i = 0; i < dataList.size(); i++) {
			String data[] = dataList.get(i).toString().split(delimeter);
			dt[i] = data[0];
			tm[i] = data[1];
			t[i] = Float.parseFloat(data[2]);
			h[i] = Float.parseFloat(data[3]);
			n[i] = Float.parseFloat(data[4]);
			v[i] = Float.parseFloat(data[5]);
		}

		for (int j = 0; j < dataList.size(); j++) {
			sumT += t[j];
			sumH += h[j];
			sumN += n[j];
			sumV += v[j];
			sumcovTH += t[j] * h[j];
			sumcovTN += t[j] * n[j];
			sumcovTV += t[j] * v[j];
			sumcovHN += h[j] * n[j];
			sumcovHV += h[j] * v[j];
			sumcovNV += n[j] * v[j];
		}
		aveT = sumT / dataList.size();
		aveH = sumH / dataList.size();
		aveN = sumN / dataList.size();
		aveV = sumV / dataList.size();
		covTH = sumcovTH / dataList.size() - aveT * aveH;
		covTN = sumcovTN / dataList.size() - aveT * aveN;
		covTV = sumcovTV / dataList.size() - aveT * aveV;
		covHN = sumcovHN / dataList.size() - aveH * aveN;
		covHV = sumcovHV / dataList.size() - aveH * aveV;
		covNV = sumcovNV / dataList.size() - aveN * aveV;
		for (int m = 0; m < dataList.size(); m++) {
			sumsdT += Math.pow((t[m] - aveT), 2);
			sumsdH += Math.pow((h[m] - aveH), 2);
			sumsdN += Math.pow((n[m] - aveN), 2);
			sumsdV += Math.pow((v[m] - aveV), 2);
		}
		sdT = (float) Math.sqrt(sumsdT / (dataList.size() - 1));
		sdH = (float) Math.sqrt(sumsdH / (dataList.size() - 1));
		sdN = (float) Math.sqrt(sumsdN / (dataList.size() - 1));
		sdV = (float) Math.sqrt(sumsdV / (dataList.size() - 1));
		if (sdT == 0 || sdH == 0) {
			corrTH = 0;
		} else {
			corrTH = covTH / (sdT * sdH);
		}
		if (sdT == 0 || sdN == 0) {
			corrTN = 0;
		} else {
			corrTN = covTN / (sdT * sdN);
		}
		if (sdT == 0 || sdV == 0) {
			corrTV = 0;
		} else {
			corrTV = covTV / (sdT * sdV);
		}
		if (sdH == 0 || sdN == 0) {
			corrHN = 0;
		} else {
			corrHN = covHN / (sdH * sdN);
		}
		if (sdH == 0 || sdV == 0) {
			corrHV = 0;
		} else {
			corrHV = covHV / (sdH * sdV);
		}
		if (sdN == 0 || sdV == 0) {
			corrNV = 0;
		} else {
			corrNV = covNV / (sdN * sdV);
		}
		result = dt[dt.length - 1] + "," 
				+ tm[tm.length - 1] + "," 
				+ checkNaN(corrTH) + "," 
				+ checkNaN(corrTV) + "," 
				+ checkNaN(corrTN) + "," 
				+ checkNaN(corrHV) + "," 
				+ checkNaN(corrHN) + "," 
				+ checkNaN(corrNV);
		System.out.println("Correlation:\t" + result);
		return result;
	}
	
	public String getCorrelation() throws Exception {
		return getCorrelation("\\001");
	}
	
	private float checkNaN(float value) {
		if(Float.isNaN(value))
			return 0;
		else
			return value; 
	}
}
