package page.services;

import com.force.data.dynamic.FakerDataFactory;

import page.base.SalesforceHooks;

public class AccountPage extends SalesforceHooks{

	public AccountPage typeAccountName() {
		typeInputField("Account Name", FakerDataFactory.getFirstName());
		return this;
	}

	public AccountPage typeAccountNumber() {
		typeInputField("Account Number", FakerDataFactory.getBankAccountNumber());
		return this;
	}

	public AccountPage chooseRating() {
		chooseByText("Rating", FakerDataFactory.getRating());
		return this;
	}

	public AccountPage clickSave() {
		click("(//button[text()='Save'])[1]", "Save");
		return this;
	}

	public AccountPage verifyAccountCreated() {
		// Print the bearer token
		String token = getToken();
		System.out.println(token);
		
		return this;
	}


}
