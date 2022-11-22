package com.force.utility;

import java.nio.file.Paths;

import com.force.base.ProjectHooks;
import com.force.config.ConfigurationManager;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class TempMailer extends ProjectHooks{
	
	public static String getEmailAddress() {
		String innerText = "";
		try {
			Playwright pw = Playwright.create();
			Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(true));
			BrowserContext newContext = browser.newContext();
			Page page = newContext.newPage();
			page.navigate("https://www.sharklasers.com/", new Page.NavigateOptions().setTimeout(0));
			innerText = page.locator("#inbox-id").innerText()+"@sharklasers.com";
			System.out.println(innerText);
			if(!innerText.trim().equals("")) {
				email.set(innerText);
				newEmails.put(innerText, "Created");
			}
			newContext.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("storage/"+innerText+".json")));
			pw.close();
		}catch(Exception e) {}
		return innerText;
	}

	public static String getRegistrationLink(String email) {
		boolean bEmailReceived = false;
		int maxEmailWaitTime = 0;
		String attribute = "";
		try {
			Playwright pw = Playwright.create();
			Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(true));
			BrowserContext context = browser.newContext(new Browser.NewContextOptions().setStorageStatePath(Paths.get("storage/"+email+".json")));
			Page page = context.newPage();
			
			while(!bEmailReceived && maxEmailWaitTime < ConfigurationManager.configuration().maxEmailTimeout()) {
				page.navigate("https://www.sharklasers.com/", new Page.NavigateOptions().setTimeout(0));
				Locator locator = page.locator("(//tr[contains(@class,'mail_row  email_unread')]/td[@class='td2'])[1]");
				if(locator.count() == 1) {
					locator.click();
					try {
						page.setDefaultTimeout(2000);
						attribute = page.locator("//a[contains(text(),'Verify Account')]").getAttribute("href");
						newEmails.put(email, "EmailConfirmed");
						bEmailReceived = true;
						break;
					}catch(Exception e) {
						//e.printStackTrace();
					}
				} else {
					System.out.println("No emails yet !!");
				}
				page.setDefaultTimeout(ConfigurationManager.configuration().timeout());

				if(!bEmailReceived) {
					page.goBack();
					try {Thread.sleep(10000);} catch(Exception e) {}
					maxEmailWaitTime = maxEmailWaitTime + 12000;
				}
			}
			pw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return attribute;
	}

}
