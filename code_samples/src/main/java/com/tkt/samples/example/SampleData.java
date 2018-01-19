package com.tkt.samples.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class SampleData {
	// SAMPLE_ID|PATIENT_NUMBER|ICD_CODE|BODY_SITE|ORDERED_DATE|REPORT_DATE|STATE|RESULT
	private String sampleID;
	private String patientNumber;
	private String icdCode;
	private String bodySite;
	private String orderYear;
	private String reportYear;
	private long turnaround;
	private String State;
	private int egfr;
	private int pdL1;
	
	private static final String header = "SAMPLE_ID|PATIENT_NUMBER|ICD_CODE|BODY_SITE|ORDERED_YEAR|REPORT_YEAR|TURNAROUND|STATE|EGFR|PD-L1";
	private static final String DELIMITER = "\\|";
	private static final String PIPE = "|";
	private static final String SPACE = " ";
	private static final String EGFR = "EGFR";
	private static final String PD_L1 = "PD-L1";
	private static final String POSITIVE = "positive";
	private static final String NEGATIVE = "negative";
	private static final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	private static final SimpleDateFormat formatNowYear = new SimpleDateFormat("yyyy");
	
	
	public SampleData(String inputLine) throws ParseException {
		String[] fields = inputLine.split(DELIMITER);
		setSampleID(fields[0]);
		setPatientNumber(fields[1]);
		setIcdCode(fields[2]);
		setBodySite(fields[3]);
		
		Date orderDate = convertStringToDate(fields[4]);
		Date reportDate = convertStringToDate(fields[5]);
		setOrderYear(getYearString(orderDate));
		setReportYear(getYearString(reportDate));

		long diff = reportDate.getTime() - orderDate.getTime();
		setTurnaround(diff);
		setState(fields[6]);
		setResults(fields[7]);
	}
	
	public static String header() {
		return header;
	}
	
	public String dataLine() {
		StringBuilder sb = new StringBuilder();
		sb.append(getSampleID());
		sb.append(PIPE);
		sb.append(getPatientNumber());
		sb.append(PIPE);
		sb.append(getIcdCode());
		sb.append(PIPE);
		sb.append(getBodySite());
		sb.append(PIPE);
		sb.append(getOrderYear());
		sb.append(PIPE);
		sb.append(getReportYear());
		sb.append(PIPE);
		sb.append(getTurnaround());
		sb.append(PIPE);
		sb.append(getState());
		sb.append(PIPE);
		sb.append(getEgfr());
		sb.append(PIPE);
		sb.append(getPd_l1());
		return sb.toString();
	}
	
	private Date convertStringToDate(String dateString) throws ParseException {
		return formatter.parse(dateString);
	}
	
	private String getYearString(Date date) {
		return formatNowYear.format(date);
	}
	
	private void setResults(String results) {
		results = results.replaceAll(",", " ");
		results = results.replaceAll(":(\\s)+", ":");
		
		results =  StringUtils.normalizeSpace(results);
		
		String[] rslts = results.split(SPACE);
		
		for(String testItem : rslts) {
			String[] keyValue = testItem.split(":");
			
			if(keyValue.length == 2) {
				if(keyValue[0].equals(EGFR)) {
					if(keyValue[1].equals(POSITIVE)) {
						setEgfr(1);
					} else if(keyValue[1].equals(NEGATIVE)) {
						setEgfr(-1);
					} else {
						setEgfr(0);
					}
				} else if (keyValue[0].equals(PD_L1)) {
					if(keyValue[1].equals(POSITIVE)) {
						setPd_l1(1);
					} else if(keyValue[1].equals(NEGATIVE)) {
						setPd_l1(-1);
					} else {
						setPd_l1(0);
					}
				} else {
					setEgfr(0);
					setPd_l1(0);
				}
			} else {
				setEgfr(0);
				setPd_l1(0);
			}
		}
	}
	
	public String getSampleID() {
		return sampleID;
	}
	public void setSampleID(String sampleID) {
		this.sampleID = sampleID;
	}
	public String getPatientNumber() {
		return patientNumber;
	}
	public void setPatientNumber(String patientNumber) {
		this.patientNumber = patientNumber;
	}
	public String getIcdCode() {
		return icdCode;
	}
	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}
	
	public String getBodySite() {
		return bodySite;
	}

	public void setBodySite(String bodySite) {
		this.bodySite = bodySite;
	}

	public String getOrderYear() {
		return orderYear;
	}
	public void setOrderYear(String orderYear) {
		this.orderYear = orderYear;
	}
	public String getReportYear() {
		return reportYear;
	}
	public void setReportYear(String reportYear) {
		this.reportYear = reportYear;
	}
	public long getTurnaround() {
		return turnaround;
	}
	public void setTurnaround(long turnaround) {
		this.turnaround = turnaround;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public int getEgfr() {
		return egfr;
	}
	public void setEgfr(int egfr) {
		this.egfr = egfr;
	}
	public int getPd_l1() {
		return pdL1;
	}
	public void setPd_l1(int pd_l1) {
		this.pdL1 = pd_l1;
	}
}
