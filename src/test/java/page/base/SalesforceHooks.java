package page.base;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeMethod;

import com.force.base.ProjectHooks;
import com.force.data.dynamic.FakerDataFactory;

public class SalesforceHooks extends ProjectHooks{

	@BeforeMethod
	public void zLogin() {
		new LoginPage().doLogin();
	}

	public void typeInputField(String label, String value) {
		type("//label[text()='"+label+"']/following::input[1]", value, label);
	}

	public void typeInputInFrame(String label, String value) {
		typeInFrame("//label[text()='"+label+"']/following::input[1]", value, label);
	}

	public void chooseByText(String label, String value) {
		clickAndChoose("//label[text()='"+label+"']/following::lightning-base-combobox[1]", "(//label[text()='"+label+"']/following::lightning-base-combobox[1]//lightning-base-combobox-item//span[text()='"+value+"'])[1]",  value, label);
	}

	public void chooseByTextInFrame(String label, String value) {
		clickAndChooseInFrame("//label[text()='"+label+"']/following::lightning-base-combobox[1]", "(//label[text()='"+label+"']/following::lightning-base-combobox[1]//lightning-base-combobox-item//span[text()='"+value+"'])[1]", value, label);
	}

	public void verifyMandatory(String label) {
		verifyAttribute("(//label[text()='"+label+"']/following::td[1]//div[1])[1]", "class","requiredInput");
	}

	public void verifyMandatoryInFrame(String label) {
		verifyAttributeInFrame("(//label[text()='"+label+"']/following::td[1]//div[1])[1]", "class","requiredInput");
	}

	public String createLeadUsingApi() {

		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer "+getToken());
		headers.put("Content-Type", "application/json");

		String firstName = FakerDataFactory.getFirstName();
		String lastName = FakerDataFactory.getLastName();
		String companyName = FakerDataFactory.getCompanyName();

		String jsonBody = "{\n"
				+ "    \"FirstName\": \""+firstName+"\",\n"
				+ "    \"LastName\": \""+lastName+"\",\n"
				+ "    \"Company\": \""+companyName+"\",\n"
				+ "    \"LeadSource\" : \"Other\"\n"
				+ "}";

		postRequest("Lead/", headers, jsonBody);

		return firstName;
	}

	public String createAccountUsingApi() {

		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer "+getToken());
		headers.put("Content-Type", "application/json");

		String companyName = FakerDataFactory.getCompanyName();
		String accountNumber = FakerDataFactory.getBankAccountNumber();
		String rating = FakerDataFactory.getRating();

		String jsonBody = "{\n"
				+ "    \"Name\": \""+companyName+"\",\n"
				+ "    \"AccountNumber\": \""+accountNumber+"\",\n"
				+ "    \"Rating\": \""+rating+"\"\n"
				+ "}";

		postRequest("Account/", headers, jsonBody);

		return companyName;
	}
}


