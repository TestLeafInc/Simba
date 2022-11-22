package page.base;

import java.nio.file.Paths;

import com.force.base.ProjectHooks;
import com.force.config.ConfigurationManager;
import com.microsoft.playwright.BrowserContext;

public class LoginPage extends ProjectHooks{
	
	public LoginPage typeUserName(String username) {
		type("#username", username, "Username");
		return this;
	}
	
	public LoginPage typePassword(String password) {
		type("#password", password, "Password");
		return this;
	}
	
	public LoginPage clickLogin() {
		click("#Login", "Log In");
		return this;
	}
	
	public LoginPage doLogin() {
		if(getPage().title().contains("Login")) {
			typeUserName(ConfigurationManager.configuration().appUserName())
			.typePassword(ConfigurationManager.configuration().appPassword())
			.clickLogin();
			
			getContext().storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("storage/login.json")));
		} 
		return this;
		
	}

}
