package com.tkt.sample.example;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.tkt.samples.example.SampleData;

public class SampleDataTest {
	String sampleData1 = "S732186|P93498|C18.9, R04.0|COLON|07/30/2017|08/08/2017|ME|EGFR: negative, PD-L1: positive";
	String sampleData2 = "S865365|P60171|J06.9, C34.90|COLON|07/09/2017|07/22/2017|VT|TBD";
	String sampleData3 = "S462939|P35085|C18.9, R04.0|BRAIN|05/26/2017|06/09/2017|NY|test cancelled";
	String sampleData4 = "S147689|P77984|J06.9, C34.90|KIDNEY|05/06/2017|05/16/2017|NY|EGFR: positive PD-L1: negative";
	
  @Test
  public void SampleData() throws ParseException {
	  SampleData sd = new SampleData(sampleData1);
	  Assert.assertTrue(sd.getSampleID().compareTo("S732186") == 0);
	  Assert.assertTrue(sd.getPatientNumber().compareTo("P93498") == 0);
	  Assert.assertTrue(sd.getIcdCode().compareTo("C18.9, R04.0") == 0);
	  Assert.assertTrue(sd.getBodySite().compareTo("COLON") == 0);
	  Assert.assertTrue(sd.getOrderYear().compareTo("2017") == 0);
	  Assert.assertTrue(sd.getReportYear().compareTo("2017") == 0);
	  Assert.assertTrue(sd.getState().compareTo("ME") == 0);
	  Assert.assertTrue(sd.getEgfr() == -1);
	  Assert.assertTrue(sd.getPd_l1() == 1);
  }

  @Test
  public void convertStringToDate() throws ParseException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	  SampleData sd = new SampleData(sampleData2);
	  Method method = SampleData.class.getDeclaredMethod("convertStringToDate", String.class);
	  method.setAccessible(true);
	  
	  Object obj = method.invoke(sd, "07/09/2017");
	  
	  Assert.assertTrue(obj.getClass().getName().compareTo("java.util.Date") == 0);
	  
	  Date date = (Date)obj;
	  Calendar cal = Calendar.getInstance();
	  cal.setTime(date);
	  
	  Assert.assertTrue(cal.get(Calendar.MONTH) == 6);
	  Assert.assertTrue(cal.get(Calendar.DAY_OF_MONTH) == 9);
	  Assert.assertTrue(cal.get(Calendar.YEAR) == 2017);
  }

  @Test
  public void getYearString() throws ParseException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	  SampleData sd = new SampleData(sampleData3);
	  Method method = SampleData.class.getDeclaredMethod("getYearString", Date.class);
	  method.setAccessible(true);
	  
	  SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	  Date date = formatter.parse("05/26/2017");
	  
	  Object obj = method.invoke(sd, date);
	  Assert.assertTrue(obj.getClass().getName().compareTo("java.lang.String") == 0);
	  
	  String year = (String)obj;
	  
	  Assert.assertTrue(year.compareTo("2017") == 0);
  }

  @Test
  public void setResults() throws ParseException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
	  SampleData sd = new SampleData(sampleData4);
	  
	  Method method = SampleData.class.getDeclaredMethod("setResults", String.class);
	  method.setAccessible(true);
	  
	  method.invoke(sd, "EGFR: positive PD-L1: negative");
	  Assert.assertTrue(sd.getEgfr() == 1);
	  Assert.assertTrue(sd.getPd_l1() == -1);
	  
	  method.invoke(sd, "EGFR: positive, PD-L1: negative");
	  Assert.assertTrue(sd.getEgfr() == 1);
	  Assert.assertTrue(sd.getPd_l1() == -1);
	  
	  method.invoke(sd, "TBD");
	  Assert.assertTrue(sd.getEgfr() == 0);
	  Assert.assertTrue(sd.getPd_l1() == 0);
	  
	  method.invoke(sd, "test cancelled");
	  Assert.assertTrue(sd.getEgfr() == 0);
	  Assert.assertTrue(sd.getPd_l1() == 0);
  }
}
