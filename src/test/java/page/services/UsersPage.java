package page.services;

import java.util.List;

import com.force.data.dynamic.FakerDataFactory;
import com.force.utility.TempMailer;

import page.base.SalesforceHooks;

public class UsersPage extends SalesforceHooks{

	public UsersPage typeFirstName() {
		typeInputInFrame("First Name", FakerDataFactory.getFirstName());
		return this;
	}
	
	public UsersPage typeLastName() {
		typeInputInFrame("Last Name", FakerDataFactory.getLastName());
		return this;
	}
	
	public UsersPage typeEmail() {
		typeInputInFrame("Email", TempMailer.getEmailAddress());
		return this;
	}
	
	public UsersPage chooseProfile() {
		selectByIndexInFrame("#Profile", -1, "Profile");
		return this;
	}
	
	public UsersPage clickNewUser() {
		frameLocator.set(getPage().frameLocator("(//iframe)[1]"));
		clickInFrame("(//input[@title='New User'])[1]", "New User");
		return this;
	}
	
	public UsersPage verifyMandatoryFields(List<String> labels) {
		for (int i = 0; i < labels.size(); i++) {
			verifyMandatoryInFrame(labels.get(i));
		}
		return this;
	}
	
	public UsersPage clickSave() {
		clickInFrame("(//input[@name='save'])[1]", "Save");
		return this;
	}
	
	
}
