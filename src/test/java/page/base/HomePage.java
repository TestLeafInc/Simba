package page.base;

import com.force.base.ProjectHooks;

public class HomePage extends ProjectHooks{

	public HomePage clickAppLauncher() {
		click(".slds-icon-waffle", "App Launcher", "Icon");
		return this;
	}

	public HomePage clickViewAll() {
		click("(//button[text()='View All'])[1]", "View All");
		return this;
	}

	public HomePage typeSearchApps(String appName) {
		type("(//input[@placeholder='Search apps or items...'])[1]", appName, "Search Apps");
		return this;
	}

	public HomePage clickApp(String appName) {
		click("(//span[text()='All Apps']/following::mark[text()='"+appName+"'])[1]", appName);
		return this;
	}
	
	public HomePage clickLeftMenu(String menuName) {
		click("//a[text()='"+menuName+"']", menuName, "Menu");
		return this;
	}
	
	public HomePage clickLeftSubMenu(String mainMenuName, String subMenu) {
		click("//a[@class='slds-tree__item-label' and text()='"+mainMenuName+"']/following::a[text()='"+subMenu+"']", subMenu, "Sub Menu");
		return this;
	}
	
}

