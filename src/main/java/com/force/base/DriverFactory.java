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

import com.force.config.ConfigurationManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;

public class DriverFactory  {

	private static final ThreadLocal<Playwright> playwright = new ThreadLocal<Playwright>();
	private static final ThreadLocal<Browser> driver = new ThreadLocal<Browser>();
	private static final ThreadLocal<String> token = new ThreadLocal<String>();
	protected static final ThreadLocal<BrowserContext> context = new ThreadLocal<BrowserContext>();
	protected static final ThreadLocal<Page> page = new ThreadLocal<Page>();
	protected static final ThreadLocal<Page> secondPage = new ThreadLocal<Page>();
	protected static final ThreadLocal<FrameLocator> frameLocator = new ThreadLocal<FrameLocator>();

	/**
	 * Launches the preferred browser in the head(less) mode.
	 * @param browser The accepted browsers are chrome, edge, firefox, safari (webkit)
	 * @param headless Send true if you like to run in headless mode else false
	 * @author TestLeaf
	 */
	
	public void setDriver(String browser, boolean headless) {	
		System.setProperty("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD", "1");
		playwright.set(Playwright.create());

		switch (browser) {
		case "chrome":
			driver.set(getPlaywright().chromium().launch(
					new BrowserType.LaunchOptions().setChannel("chrome")
					.setHeadless(headless)
					.setSlowMo(ConfigurationManager.configuration().slowMotion())));
			break;
		case "edge":
			driver.set(getPlaywright().chromium().launch(
					new BrowserType.LaunchOptions().setChannel("msedge")
					.setHeadless(headless)
					.setSlowMo(ConfigurationManager.configuration().slowMotion())));
			break;
		case "firefox":
			driver.set(getPlaywright().firefox().launch(
					new BrowserType.LaunchOptions()
					.setHeadless(headless)
					.setSlowMo(ConfigurationManager.configuration().slowMotion())));
		case "safari":
			driver.set(getPlaywright().webkit().launch(
					new BrowserType.LaunchOptions()
					.setHeadless(headless)
					.setSlowMo(ConfigurationManager.configuration().slowMotion())));
		default:
			break;
		}
		
		// Login through API and set the OAuth Token
		setOauthToken();
		
	}
	
	public void setOauthToken() {

		// API request
		APIRequestContext request = getPlaywright().request().newContext(new APIRequest.NewContextOptions().setBaseURL(ConfigurationManager.configuration().oauthUrl()));

		// Request -> Post 
		APIResponse response = request.post("", 
				RequestOptions.create()
				.setForm(FormData.create()
						.set("grant_type", "password")
						.set("client_id", ConfigurationManager.configuration().clientId())
						.set("client_secret", ConfigurationManager.configuration().clientSecret())
						.set("username", ConfigurationManager.configuration().appUserName())
						.set("password",ConfigurationManager.configuration().appPassword())

						));

		// Parse the json
		token.set(new Gson().fromJson(response.text(), JsonElement.class).getAsJsonObject().get("access_token").getAsString());

	}
	
	public String getToken() {
		return token.get();
	}

	public Browser getDriver() {
		return driver.get();
	}

	public BrowserContext getContext() {
		return context.get();
	}

	public Page getPage() {
		return page.get();
	}
	
	public Page getSecondPage() {
		return secondPage.get();
	}
	
	public FrameLocator getFrameLocator() {
		return frameLocator.get();
	}

	public Playwright getPlaywright() {
		return playwright.get();
	}

}
