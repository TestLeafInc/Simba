/*
 * MIT License
 *
 * Copyright (c) 2022 TestLeaf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.force.base;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import com.force.config.ConfigurationManager;
import com.force.utility.DataLibrary;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.Video;

public class ProjectHooks extends PlaywrightWrapper  {

	// This is for the email that you may need to use within test automation
	protected static final ThreadLocal<String> email = new ThreadLocal<String>();

	public static Map<String,String> newEmails;
	public static String videoFolderName = "videos/";
	public static String tracesFolderName = "videos/";


	/**
	 * Will be invoked before once for every test suite execution and create video folder and the reporting
	 * @author TestLeaf
	 */
	@BeforeSuite
	public void initSuite() {
		newEmails = new HashMap<String, String>();
		videoFolderName = createFolder("videos");
		tracesFolderName = createFolder("traces");

		startReport();
	}

	/**
	 * This method will take snapshot in base64 format
	 * @author TestLeaf
	 */
	@Override	
	public String takeSnap(){
		return new String(Base64.getEncoder().encode(getPage().screenshot())); 
	}

	/**
	 * Will be invoked before once for every test case execution and
	 * a) will launch the browser based on config
	 * b) create reporting structure
	 * c) store login state (if configured)
	 * d) create context, page
	 * e) set default time out based on config
	 * f) maximize and load the given URL
	 * @author TestLeaf
	 */
	@BeforeMethod
	public void init() {
		try {
			// Launch the browser (based on configuration) in head(less) mode (based on configuration)
			setDriver(ConfigurationManager.configuration().browser(), ConfigurationManager.configuration().headless());

			// Set the extent report node for the test
			setNode();

			// Default Settings
			NewContextOptions newContext = new Browser.NewContextOptions()
					.setIgnoreHTTPSErrors(true)
					.setRecordVideoDir(Paths.get(folderName));

			// Auto Login if enabled
			if(ConfigurationManager.configuration().autoLogin()) {
				newContext.setStorageStatePath(Paths.get("storage/login.json"));
			} 

			// Store for Auto Login, Set the video recording ON using context
			context.set(getDriver().newContext(newContext));

			// Create a new page and assign to the thread local
			page.set(getContext().newPage());

			// Set the timeout based on the configuration
			getPage().setDefaultTimeout(ConfigurationManager.configuration().timeout());

			// 	enable Tracing
			if(ConfigurationManager.configuration().enableTracing())
				getContext().tracing().start(new Tracing.StartOptions().setName(testcaseName).setSnapshots(true).setTitle(testcaseName));

			// Get the screen size and maximize
			maximize();

			// Load the page with URL based on configuration
			navigate(ConfigurationManager.configuration().baseUrl());

		} catch (Exception e) {
			reportStep("The browser and/or the URL could not be loaded as expected", "fail");
		}
	}

	@BeforeClass(alwaysRun = true)
	public void startTestcaseReporting() {
		startTestCase();
	}

	/**
	 * Will be invoked after once for every test case execution and
	 * a) video & tracing will be created in the given folder
	 * b) result will be published
	 * @author TestLeaf
	 */
	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		try {

			// End tracing
			getContext().tracing().stop(new Tracing.StopOptions().setPath(Paths.get(tracesFolderName+"/"+testcaseName+".zip")));
			Video video = getPage().video();
			getPage().close();
			video.saveAs(Paths.get(videoFolderName+"/"+testcaseName+".webm"));
			getContext().close(); // video will be saved
			video.delete();
			getPlaywright().close();
			endResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@DataProvider
	public Object[][] getData() throws IOException {
		return DataLibrary.readExcelData(dataFileName);
	}



}
