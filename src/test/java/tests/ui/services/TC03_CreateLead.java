package tests.ui.services;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

import page.base.HomePage;
import page.base.SalesforceHooks;
import page.services.LeadPage;
import page.services.ServiceDashboardPage;

public class TC03_CreateLead extends SalesforceHooks{
	
	@BeforeTest
	public void setReportValues() {
		testcaseName = "TC03 - Create Lead";
		testDescription = "Create a new Lead";
		authors = "TestLeaf";
		category = "Service";
	}
	
	@Test
	public void editAccount() {
		
		// Go to Marketing Application
		new HomePage()
		.clickAppLauncher()
		.clickViewAll()
		.typeSearchApps("Marketing")
		.clickApp("Marketing");
		
		ServiceDashboardPage sdp = new ServiceDashboardPage();
		
		// Create a new Leads
		sdp.clickTab("Leads").clickMenu("New");
		
		// Fill all mandatory fields
		String toastMessage = new LeadPage().fillMandatoryFields();
		
		// Verify that the lead is created
		sdp.verifyToastMessage(toastMessage);
		
	}

}
