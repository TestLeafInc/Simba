package com.force.utility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.force.base.DriverFactory;
import com.force.config.ConfigurationManager;

public abstract class HtmlReporter extends DriverFactory {

	private static ExtentReports extent;
	private static final ThreadLocal<ExtentTest> parentTest = new ThreadLocal<ExtentTest>();
	private static final ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();
	private static final ThreadLocal<String> testName = new ThreadLocal<String>();

	
	private String fileName = "result.html";
	private static String pattern = "dd-MMM-yyyy HH-mm-ss";

	public String testcaseName, testDescription, authors, category, dataFileName, dataFileType;
	public static String folderName = "";
	
	public static String createFolder(String folderName) {
		String date = new SimpleDateFormat(pattern).format(new Date());
		folderName = folderName+"/" + date;

		File folder = new File("./" + folderName);
		if (!folder.exists()) {
			folder.mkdir();
		}
		return folderName;
	}

	public synchronized void startReport() {
		folderName = createFolder("reports");
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("./" + folderName + "/" + fileName);
		htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
		htmlReporter.config().setChartVisibilityOnOpen(!true);
		htmlReporter.config().setTheme(Theme.STANDARD);
		if(ConfigurationManager.configuration().reportTheme().equalsIgnoreCase("dark"))
			htmlReporter.config().setTheme(Theme.DARK);
		htmlReporter.config().setDocumentTitle(ConfigurationManager.configuration().reportTitle());
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName(ConfigurationManager.configuration().reportName());
		htmlReporter.setAppendExisting(true);
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
	}

	public synchronized void startTestCase() {
		ExtentTest parent = extent.createTest(testcaseName, testDescription);
		parent.assignCategory(category);
		parent.assignAuthor(authors);
		parentTest.set(parent);
		testName.set(testcaseName);
	}

	public synchronized void setNode() {
		ExtentTest child = parentTest.get().createNode(getTestName());
		test.set(child);
	}

	public abstract String takeSnap();

	public void reportStep(String desc, String status, boolean bSnap) {
		synchronized (test) {

			// Start reporting the step and snapshot
			MediaEntityModelProvider img = null;
			if (bSnap && !(status.equalsIgnoreCase("INFO") || status.equalsIgnoreCase("skipped"))) {
				img = MediaEntityBuilder.createScreenCapture(takeSnap(),"Snap",true).build();
			}
			if (status.equalsIgnoreCase("pass")) {
				test.get().pass(desc, img);
			} else if (status.equalsIgnoreCase("fail")) { // additional steps to manage alert pop-up
				test.get().fail(desc, img);
				throw new RuntimeException("See the reporter for details.");

			} else if (status.equalsIgnoreCase("warning")) {
				test.get().warning(desc, img);
			} else if (status.equalsIgnoreCase("skipped")) {
				test.get().skip("The test is skipped due to dependency failure");
			} else if (status.equalsIgnoreCase("INFO")) {
				test.get().info(desc);
			}

			
		}
	}

	public void reportStep(String desc, String status) {
		reportStep(desc, status, true);
	}

	public synchronized void endResult() {
		extent.flush();
	}

	
	public String getTestName() {
		return testName.get();
	}

	public Status getTestStatus() {
		return parentTest.get().getModel().getStatus();
	}
	
	
}