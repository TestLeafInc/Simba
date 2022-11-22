package page.services;

import com.force.data.dynamic.FakerDataFactory;

import page.base.SalesforceHooks;

public class ServiceDashboardPage extends SalesforceHooks{
	
	public ServiceDashboardPage clickTab(String tabName) {
		click("//span[text()='"+tabName+"']", tabName, "Tab");
		return this;
	}
		
	public ServiceDashboardPage clickMenu(String menuName) {
		click("//div[@title='"+menuName+"']", menuName, "Menu");
		return this;
	}
	
	public ServiceDashboardPage typeSearch(String searchName) {
		typeAndEnter("//input[@placeholder='Search this list...']", searchName, "Search");
		pause("medium");
		return this;
	}

	public ServiceDashboardPage clickAction() {
		click("//table[@role='grid']//tr[1]/td[last()]", "Action");
		return this;
	}
	
	public ServiceDashboardPage clickEdit() {
		click("(//a[@title='Edit'])[1]", "Edit");
		return this;
	}
	
	public ServiceDashboardPage typePhone() {
		typeInputField("Phone", FakerDataFactory.getContactNumber());
		return this;
	}

	public ServiceDashboardPage clickSave() {
		click("(//button[text()='Save'])[1]", "Save");
		return this;
	}
	
	public ServiceDashboardPage verifyToastMessage(String name) {
		verifyPartialText("(//div[contains(@class,'toast forceToastMessage')]//a/div)[1]", name);
		return this;
	}
	
	
}
