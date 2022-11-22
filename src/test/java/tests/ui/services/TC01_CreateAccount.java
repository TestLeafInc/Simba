package tests.ui.services;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

import page.base.HomePage;
import page.base.SalesforceHooks;
import page.services.AccountPage;
import page.services.ServiceDashboardPage;

public class TC01_CreateAccount extends SalesforceHooks{
	
	@BeforeTest
	public void setReportValues() {
		testcaseName = "TC01 - Create Account";
		testDescription = "Create New Account with mandatory fields";
		authors = "TestLeaf";
		category = "Service";
	}
	
	@Test
	public void createAccount() {
		
		new HomePage()
		.clickAppLauncher()
		.clickViewAll()
		.typeSearchApps("Service")
		.clickApp("Service");
		
		new ServiceDashboardPage()
		.clickTab("Accounts")
		.clickMenu("New");
				
		new AccountPage()
		.typeAccountName()
		.chooseRating()
		.typeAccountNumber()
		.clickSave();
		
	}

}
