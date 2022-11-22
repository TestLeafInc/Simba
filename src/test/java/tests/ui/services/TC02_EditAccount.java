package tests.ui.services;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

import page.base.HomePage;
import page.base.SalesforceHooks;
import page.services.AccountPage;
import page.services.ServiceDashboardPage;

public class TC02_EditAccount extends SalesforceHooks{
	
	@BeforeTest
	public void setReportValues() {
		testcaseName = "TC02 - Edit Account";
		testDescription = "Edit existing Account with rating change";
		authors = "TestLeaf";
		category = "Service";
	}
	
	@Test
	public void editAccount() {
		
		String accountName = createAccountUsingApi();

		new HomePage()
		.clickAppLauncher()
		.clickViewAll()
		.typeSearchApps("Service")
		.clickApp("Service");
		
		new ServiceDashboardPage()
		.clickTab("Accounts")
		.typeSearch(accountName)
		.clickAction()
		.clickEdit();
		
		new AccountPage()
		.chooseRating()
		.clickSave();
		
	}

}
