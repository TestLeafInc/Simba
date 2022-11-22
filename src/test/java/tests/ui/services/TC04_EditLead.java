package tests.ui.services;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

import page.base.HomePage;
import page.base.SalesforceHooks;
import page.services.ServiceDashboardPage;

public class TC04_EditLead extends SalesforceHooks{
	
	@BeforeTest
	public void setReportValues() {
		testcaseName = "TC04 - Edit Lead";
		testDescription = "Edit an existing Lead";
		authors = "TestLeaf";
		category = "Service";
	}
	
	@Test
	public void editAccount() {
		
		String firstName = createLeadUsingApi();
		
		new HomePage()
		.clickAppLauncher()
		.clickViewAll()
		.typeSearchApps("Marketing")
		.clickApp("Marketing");
		
		ServiceDashboardPage sdp = new ServiceDashboardPage();
		
		sdp.clickTab("Leads")
		.typeSearch(firstName)
		.clickAction()
		.clickEdit()
		.typePhone()
		.clickSave();
		
	}

}
