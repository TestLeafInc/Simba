package page.services;

import com.force.data.dynamic.FakerDataFactory;

import page.base.SalesforceHooks;

public class LeadPage extends SalesforceHooks{
	
	public String fillMandatoryFields() {
		String firstName = FakerDataFactory.getFirstName();
		String lastName = FakerDataFactory.getLastName();
		String companyName = FakerDataFactory.getCompanyName();
		String salutation = FakerDataFactory.getSalutation();
		
		chooseSalutation(salutation)
		.typeFirstName(firstName)
		.typeLastName(lastName)
		.typeCompanyName(companyName)
		.chooseLeadStatus()
		.clickSave();
		
		return salutation+" "+firstName+" "+lastName;
	}

	public LeadPage typeFirstName(String firstName) {
		typeInputField("First Name", firstName);
		return this;
	}
	
	public LeadPage typeLastName(String lastName) {
		typeInputField("Last Name", lastName);
		return this;
	}
	
	public LeadPage typeCompanyName(String companyName) {
		typeInputField("Company", companyName);
		return this;
	}
	
	public LeadPage chooseSalutation(String salutation) {
		chooseByText("Salutation", salutation);
		return this;
	}
	
	public LeadPage chooseLeadStatus() {
		chooseByText("Lead Status", "Working - Contacted");
		return this;
	}
	
	public LeadPage clickSave() {
		click("(//button[text()='Save'])[1]", "Save");
		return this;
	}
	
	
}
