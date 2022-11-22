/*
 * MIT License
 *
 * Copyright (c) 2022 TestLeaf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.force.base;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.nio.file.Paths;
import java.util.Map;

import com.force.config.ConfigurationManager;
import com.force.utility.HtmlReporter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.RequestOptions;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;

public abstract class PlaywrightWrapper extends HtmlReporter{

	int retry = 0;

	/**
	 * Load the URL on the browser launched
	 * @author TestLeaf
	 * @param url The http(s) URL that to be loaded on the browser
	 * @return true if the load is successful else false
	 */
	public boolean navigate(String url) {
		try {
			getPage().navigate(url);
			reportStep("The page with URL :"+url+" is loaded", "pass");
		} catch (PlaywrightException e) {
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;
	}

	/**
	 * Maximize the browser based on screen size
	 * Presently there is no built-in method to invoke.
	 * @author TestLeaf
	 */
	public void maximize() {
		try {
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			getPage().setViewportSize(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight());
		} catch (HeadlessException e) {

		}
	}

	/**
	 * Check if the given selector of the element is visible or not after 2 seconds
	 * @param locator The css / xpath / or playwright supported locator
	 * @return true if the element is visible else false
	 * @author TestLeaf
	 */
	public boolean isVisible(String locator) {
		boolean bVisible = false;
		try {
			getPage().setDefaultTimeout(2000);
			bVisible = getPage().isVisible(locator);
		} catch (PlaywrightException e) {
		}
		getPage().setDefaultTimeout(ConfigurationManager.configuration().timeout());
		return bVisible;

	}

	/**
	 * Use this method to typing into an element, which may set its value.
	 * @param locator The locator to identify the element
	 * @param value The value to be entered in the text
	 * @param name The name of the text field (label)
	 * @return true if the value is typed else false
	 * @author TestLeaf
	 */
	public boolean type(String locator, String value, String name) {
		try {
			getPage().locator(locator).fill("");;
			getPage().locator(locator).type(value);
			reportStep("The text box :"+name+" is typed with value :"+value, "pass");
			return true;
		} catch (PlaywrightException e) {
			e.printStackTrace();
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;
	}

	/**
	 * Use this method to typing into an element inside a frame, which may set its value.
	 * @param locator The locator to identify the element
	 * @param value The value to be entered in the text
	 * @param name The name of the text field (label)
	 * @return true if the value is typed else false
	 * @author TestLeaf
	 */
	public boolean typeInFrame(String locator, String value, String name) {
		try {
			getFrameLocator().locator(locator).fill("");;
			getFrameLocator().locator(locator).type(value);
			reportStep("The text box :"+name+" is typed with value :"+value, "pass");
			return true;
		} catch (PlaywrightException e) {
			e.printStackTrace();
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;
	}

	/**
	 * Use this method to typing into an element and perform <ENTER> key.
	 * @param locator The locator to identify the element
	 * @param value The value to be entered in the text
	 * @param name The name of the text field (label)
	 * @return true if the value is typed else false
	 * @author TestLeaf
	 */
	public boolean typeAndEnter(String locator, String value, String name) {
		try {
			getPage().locator(locator).fill("");;
			getPage().locator(locator).type(value);
			getPage().locator(locator).press("Enter");
			reportStep("The text box :"+name+" is typed with value :"+value, "pass");
			return true;
		} catch (PlaywrightException e) {
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;

	}

	/**
	 * Use this method to upload a file into the chosen field.
	 * @param locator The locator to identify the element where need to upload
	 * @param fileName The file name (relative or absolute)
	 * @param name The name of the upload field (label)
	 * @return true if the file is uploaded else false
	 * @author TestLeaf
	 */
	public boolean uploadFile(String locator, String fileName, String name) {
		try {
			getPage().locator(locator).setInputFiles(Paths.get(fileName));
			reportStep("The text box :"+name+" is uploaded with file :"+fileName, "pass");
			return true;
		} catch (PlaywrightException e) {
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;

	}

	/**
	 * Use this method to click a button.
	 * @param locator The locator to identify the element
	 * @param name The name of the button field (label)
	 * @return true if the button is clicked else false
	 * @author TestLeaf
	 */
	public boolean click(String locator, String name) {
		return click(locator, name, "button");
	}

	/**
	 * Use this method to click an element.
	 * @param locator The locator to identify the element
	 * @param name The name of the element field (label)
	 * @param type The type of the element such as link, element etc
	 * @return true if the element is clicked else false
	 * @author TestLeaf
	 */
	public boolean click(String locator, String name, String type) {
		try {
			getPage().locator(locator).scrollIntoViewIfNeeded();
			getPage().locator(locator).click();
			reportStep("The "+type+" :"+name+" is clicked", "pass");
			return true;
		} catch (PlaywrightException e) {
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;

	}

	/**
	 * Use this method to click an element within a frame.
	 * @param locator The locator to identify the element
	 * @param name The name of the element field (label)
	 * @param type The type of the element such as link, element etc
	 * @return true if the element is clicked else false
	 * @author TestLeaf
	 */
	public boolean clickInFrame(String locator, String name) {
		try {
			getFrameLocator().locator(locator).scrollIntoViewIfNeeded();
			getFrameLocator().locator(locator).click();
			reportStep("The button :"+name+" is clicked", "pass");
			return true;
		} catch (PlaywrightException e) {
			e.printStackTrace();
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;
	}

	/**
	 * Use this method to check a checkbox.
	 * @param locator The locator to identify the checkbox
	 * @param name The name of the checkbox field (label)
	 * @return true if the checkbox is checked else false
	 * @author TestLeaf
	 */
	public boolean check(String locator, String name) {
		try {
			getPage().locator(locator).check();
			reportStep("The checkbox: "+name+" is checked", "pass");
			return true;
		} catch (PlaywrightException e) {
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;
	}

	/**
	 * Use this method to select a dropdown by its text.
	 * @param locator The locator to identify the dropdown
	 * @param text The text to be selected in the dropdown
	 * @param name The name of the dropdown field (label)
	 * @return true if the dropdown is selected else false
	 * @author TestLeaf
	 */
	public boolean selectByText(String locator, String text, String name) {
		try {
			getPage().locator(locator).selectOption(new SelectOption().setLabel(text));
			reportStep("The drop down :"+name+" is selected with value :"+text, "pass");
			return true;
		} catch (PlaywrightException e) {
			e.getMessage();
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;

	}

	/**
	 * Use this method to select a dropdown by its value.
	 * @param locator The locator to identify the dropdown
	 * @param value The value based on which it to be selected in the dropdown
	 * @param name The name of the dropdown field (label)
	 * @return true if the dropdown is selected else false
	 * @author TestLeaf
	 */
	public boolean selectByValue(String locator, String value, String name) {
		try {
			getPage().locator(locator).selectOption(value);
			reportStep("The drop down :"+name+" is selected with value index as :"+value, "pass");
			return true;
		} catch (PlaywrightException e) {
			e.printStackTrace();
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;

	}

	/**
	 * Use this method to select a dropdown by its index.
	 * @param locator The locator to identify the dropdown
	 * @param index The index to be selected in the dropdown (starts at 0)
	 * @param name The name of the dropdown field (label)
	 * @return true if the dropdown is selected else false
	 * @author TestLeaf
	 */
	public boolean selectByIndex(String locatorId,int index, String name) {
		try {
			Locator locator = getPage().locator(locatorId+" > option");
			if(index > locator.count() || index < 0)  index = (int)Math.floor(Math.random()*(locator.count()-1))+1;
			getPage().locator(locatorId).selectOption(locator.nth(index).getAttribute("value"));
			reportStep("The drop down :"+name+" is selected with index :"+index, "pass");
			return true;
		} catch (PlaywrightException e) {
			e.getMessage();
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;

	}

	/**
	 * Use this method to select a dropdown by its index inside a frame.
	 * @param locator The locator to identify the dropdown
	 * @param index The index to be selected in the dropdown (starts at 0)
	 * @param name The name of the dropdown field (label)
	 * @return true if the dropdown is selected else false
	 * @author TestLeaf
	 */
	public boolean selectByIndexInFrame(String locatorId,int index, String name) {
		try {
			Locator locator = getFrameLocator().locator(locatorId+" > option");
			if(index > locator.count() || index < 0)  index = (int)Math.floor(Math.random()*(locator.count()-1))+1;
			getFrameLocator().locator(locatorId).selectOption(locator.nth(index).getAttribute("value"));
			reportStep("The drop down :"+name+" is selected with index :"+index, "pass");
			return true;
		} catch (PlaywrightException e) {
			e.getMessage();
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;

	}

	/**
	 * Use this method to select a dropdown by the random index.
	 * @param locator The locator to identify the dropdown
	 * @param name The name of the dropdown field (label)
	 * @return true if the dropdown is selected else false
	 * @author TestLeaf
	 */
	public boolean selectByRandomIndex(String locator,String name) {
		return selectByIndex(locator, -1, name);
	}

	/**
	 * Use this method to type and choose an element (that looks like dropdown)
	 * @param ddLocator The Dropdown locator to identify the main select element
	 * @param optionLocator The Option locator to identify the item element
	 * @param option The option to be entered in the text area
	 * @param name The name of the dropdown field (label)
	 * @return true if the option is selected else false
	 * @author TestLeaf
	 */
	public boolean clickAndType(String ddLocator, String optionLocator, String option, String name) {
		try {
			getPage().locator(ddLocator).click();
			getPage().locator(optionLocator).type(option);
			getPage().keyboard().press("Enter");
			reportStep("The drop down :"+name+" is selected with value :"+option, "pass");
			return true;
		} catch (PlaywrightException e) {
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;
	}

	/**
	 * Use this method to click and choose an element (that looks like dropdown)
	 * @param ddLocator The Dropdown locator to identify the main select element
	 * @param optionLocator The Option locator to identify the item element
	 * @param option The option to be selected in the dropdown
	 * @param name The name of the dropdown field (label)
	 * @return true if the option is selected else false
	 * @author TestLeaf
	 */
	public boolean clickAndChoose(String ddLocator, String optionLocator, String option, String name) {
		try {
			getPage().locator(ddLocator).click();
			getPage().locator(optionLocator).click();
			reportStep("The drop down :"+name+" is selected with value :"+option, "pass");
			return true;
		} catch (PlaywrightException e) {
			e.printStackTrace();
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;
	}

	/**
	 * Use this method to click and choose an element (that looks like dropdown) inside a frame
	 * @param ddLocator The Dropdown locator to identify the main select element
	 * @param optionLocator The Option locator to identify the item element
	 * @param option The option to be selected in the dropdown
	 * @param name The name of the dropdown field (label)
	 * @return true if the option is selected else false
	 * @author TestLeaf
	 */
	public boolean clickAndChooseInFrame(String ddLocator, String optionLocator, String option, String name) {
		try {
			getFrameLocator().locator(ddLocator).click();
			getFrameLocator().locator(optionLocator).click();
			reportStep("The drop down :"+name+" is selected with value :"+option, "pass");
			return true;
		} catch (PlaywrightException e) {
			e.printStackTrace();
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;
	}

	/**
	 * Use this method to mouse over an element
	 * @param locator The locator to identify the element
	 * @param name The name of the element (label)
	 * @return true if the mouse over is done else false
	 * @author TestLeaf
	 */
	public boolean mouseOver(String locator, String name) {
		try {
			getPage().locator(locator).hover();
			reportStep("The element :"+name+" is moused over successfully", "pass");
			return true;
		} catch (PlaywrightException e) {
			e.printStackTrace();
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;
	}

	/**
	 * Use this method to get inner text of an element
	 * @param locator The locator to identify the element
	 * @param name The name of the element (label)
	 * @return true if the mouse over is done else false
	 * @author TestLeaf
	 */
	public String getInnerText(String locator) {
		try {
			return getPage().locator(locator).innerText();
		} catch(Exception e) {}
		return "";
	}

	/**
	 * Use this method to check if the element is enabled
	 * @param locator The locator to identify the element
	 * @param name The name of the element (label)
	 * @return true if the element is enabled else false
	 * @author TestLeaf
	 */
	public boolean isEnabled(String locator) {
		try {
			return getPage().locator(locator).isEnabled();
		} catch(Exception e) {}
		return false;
	}

	/**
	 * Use this method to check if the element is disabled
	 * @param locator The locator to identify the element
	 * @param name The name of the element (label)
	 * @return true if the element is disabled else false
	 * @author TestLeaf
	 */
	public boolean isDisabled(String locator) {
		try {
			return getPage().locator(locator).isDisabled();
		} catch(Exception e) {}
		return false;
	}

	/**
	 * Use this method to report if the element is disabled
	 * @param locator The locator to identify the element
	 * @param name The name of the element (label)
	 * @return true if the element is disabled else false
	 * @author TestLeaf
	 */
	public boolean verifyDisabled(String locator, String label) {
		try {
			if(isDisabled(locator)) reportStep("The element :"+label+" is disabled as expected", "pass");
			else reportStep("The element :"+label+" is enabled", "warning");
		} catch(Exception e) {}
		return false;
	}

	/**
	 * Use this method to wait for the element to disappear
	 * @param locator The locator to identify the element
	 * @return true if the element is disappeared else false
	 * @author TestLeaf
	 */
	public boolean waitForDisappearance(String locator) {
		try {
			getPage().locator(locator).waitFor(new Locator.WaitForOptions().setTimeout(ConfigurationManager.configuration().timeout()).setState(WaitForSelectorState.HIDDEN));
			return true;
		} catch(Exception e) {}
		return false;
	}

	/**
	 * Use this method to wait for the element to appear
	 * @param locator The locator to identify the element
	 * @return true if the element is appeared else false
	 * @author TestLeaf
	 */
	public boolean waitForAppearance(String locator) {
		try {
			getPage().locator(locator).waitFor(new Locator.WaitForOptions().setTimeout(ConfigurationManager.configuration().timeout()).setState(WaitForSelectorState.VISIBLE));
			return true;
		} catch(Exception e) {}
		return false;
	}

	/**
	 * Use this method to report the correctness of the title
	 * @param title The title of the browser
	 * @return true if the title matches the partial content else false
	 * @author TestLeaf
	 */
	public boolean verifyTitle(String title) {
		try {
			if(getPage().title().contains(title)) {
				reportStep("The page with title :"+title+" displayed as expected", "pass");
				return true;
			}else
				reportStep("The page with title :"+title+" did not match", "warning");

		} catch (PlaywrightException e) {
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;

	}

	/**
	 * Use this method to report the correctness of the inner text (Exact Match)
	 * @param locator The locator to identify the element
	 * @param expectedText The expected text to be verified
	 * @return true if the inner text matches the exact content else false
	 * @author TestLeaf
	 */
	public boolean verifyExactText(String locator, String expectedText) {
		try {
			System.out.println(getPage().innerText(locator));
			if(getPage().locator(locator).innerText().equals(expectedText)) {
				reportStep("The element with text :"+expectedText+" displayed as expected", "pass");
				return true;
			}else
				reportStep("The element with text :"+expectedText+" did not match", "warning");

		} catch (PlaywrightException e) {
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;

	}

	/**
	 * Use this method to report the correctness of the inner text (Partial Match)
	 * @param locator The locator to identify the element
	 * @param expectedText The expected text to be verified
	 * @return true if the inner text matches the partial content else false
	 * @author TestLeaf
	 */
	public boolean verifyPartialText(String locator, String expectedText) {
		try {
			if(getPage().locator(locator).innerText().contains(expectedText)) {
				reportStep("The element with text :"+expectedText+" displayed as expected", "pass");
				return true;
			}else
				reportStep("The element with text :"+expectedText+" did not match", "warning");

		} catch (PlaywrightException e) {
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;

	}

	/**
	 * Use this method return the text typed within a textbox/area
	 * @param locator The locator to identify the element
	 * @return Returns the text typed
	 * @author TestLeaf
	 */
	public String getInputText(String locator) {
		try {
			return getPage().locator(locator).inputValue();
		} catch (PlaywrightException e) {}
		return "";
	}


	/**
	 * Use this method to report the correctness of the typed text (Partial Match)
	 * @param locator The locator to identify the element
	 * @param expectedText The expected text to be verified
	 * @return true if the typed text matches the partial content else false
	 * @author TestLeaf
	 */
	public boolean verifyInputText(String locator,String expectedText) {
		try {
			if(getPage().locator(locator).inputValue().contains(expectedText)) {
				reportStep("The element with text :"+expectedText+" displayed as expected", "pass");
				return true;
			}else
				reportStep("The element with text :"+expectedText+" did not match", "warning");

		} catch (PlaywrightException e) {
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;

	}

	/**
	 * Use this method to get the attribute of the element
	 * @param locator The locator to identify the element
	 * @param attribute The attribute of the element
	 * @return The attribute value of the located element
	 * @author TestLeaf
	 */
	public String getAttribute(String locator, String attribute) {
		try {
			return getPage().locator(locator).getAttribute(attribute);
		} catch (PlaywrightException e) {}
		return "";
	}

	/**
	 * Use this method to verify attribute of the element (Partial Match)
	 * @param locator The locator to identify the element
	 * @param attribute The attribute of the element
	 * @param expectedText The expected attribute value of the located element
	 * @return true if the attribute matches the partial content else false
	 * @author TestLeaf
	 */
	public boolean verifyAttribute(String locator, String attribute, String expectedText) {
		try {
			if(getPage().locator(locator).getAttribute(attribute).contains(expectedText)) {
				reportStep("The element with text :"+expectedText+" displayed as expected", "pass");
				return true;
			}else
				reportStep("The element with text :"+expectedText+" did not match", "warning");

		} catch (PlaywrightException e) {
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;
	}

	/**
	 * Use this method to verify attribute of the element (Partial Match) inside a frame
	 * @param locator The locator to identify the element
	 * @param attribute The attribute of the element
	 * @param expectedText The expected attribute value of the located element
	 * @return true if the attribute matches the partial content else false
	 * @author TestLeaf
	 */
	public boolean verifyAttributeInFrame(String locator, String attribute, String expectedText) {
		try {
			if(getFrameLocator().locator(locator).getAttribute(attribute).contains(expectedText)) {
				reportStep("The element with text :"+expectedText+" displayed as expected", "pass");
				return true;
			}else
				reportStep("The element with text :"+expectedText+" did not match", "warning");

		} catch (PlaywrightException e) {
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		return false;

	}

	private boolean reportVisibleSuccess(String locator, String name) {
		getPage().locator(locator).scrollIntoViewIfNeeded();
		reportStep("The element :"+name+" displayed as expected", "pass");
		return true;
	}

	/**
	 * Use this method to verify visibility of the element
	 * @param locator The locator to identify the element
	 * @param name The name of the element field (label)
	 * @return true if the element visible else false
	 * @author TestLeaf
	 */
	public boolean verifyDisplayed(String locator, String name) {
		try {
			if(getPage().locator(locator).isVisible()) {
				return reportVisibleSuccess(locator, name);
			} else {
				pause("medium");
				if(getPage().isVisible(locator)) {
					return reportVisibleSuccess(locator, name);
				}
			}
		} catch (PlaywrightException e) {
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		if(!getPage().isVisible(locator)) 
			reportStep("The element :"+name+" is not visible in the page", "warning");
		return false;
	}

	/**
	 * Use this method to verify invisibility of the element
	 * @param locator The locator to identify the element
	 * @param name The name of the element field (label)
	 * @return true if the element invisible else false
	 * @author TestLeaf
	 */
	public boolean verifyNotDisplayed(String locator, String name) {
		try {
			if(!getPage().locator(locator).isVisible()) {
				reportStep("The element :"+name+" is not displayed as expected", "pass");
				return true;
			} else {
				pause("medium");
				reportStep("The element :"+name+" is not displayed as expected", "pass");
				return true;
			}
		} catch (PlaywrightException e) {
			reportStep("PlaywrightException : \n" + e.getMessage(), "fail");
		}
		if(!getPage().locator(locator).isVisible()) 
			reportStep("The element :"+name+" is visible in the page", "warning");
		return false;
	}

	/**
	 * Use this method to manage the wait between actions with sleep
	 * @param type The type of wait - allowed : low, medium, high
	 * @author TestLeaf
	 */
	protected void pause(String type) {
		try {
			switch (type.toLowerCase()) {
			case "low":
				Thread.sleep(ConfigurationManager.configuration().pauseLow());
				break;
			case "medium":
				Thread.sleep(ConfigurationManager.configuration().pauseMedium());
				break;
			case "high":
				Thread.sleep(ConfigurationManager.configuration().pauseHigh());
				break;
			default:
				Thread.sleep(ConfigurationManager.configuration().pauseLow());
				break;
			}
		} catch (Exception e) { }
	}

	
	public JsonElement getRequest(String resource, Map<String, String> headers) {

		// Send API request --> base end point url
		APIRequestContext request = getPlaywright().request().newContext(
				new APIRequest.NewContextOptions()
				.setBaseURL(ConfigurationManager.configuration().apiUrl())
				.setExtraHTTPHeaders(headers));

		// request -> Get 
		APIResponse response = request.get(resource);

		// Parse response
		return new Gson().fromJson(response.text(), JsonElement.class);		

	}

	public JsonElement postRequest(String resource, Map<String, String> headers, String jsonBody) {

		// Send API request --> base end point url
		APIRequestContext request = getPlaywright().request().newContext(
				new APIRequest.NewContextOptions()
				.setBaseURL(ConfigurationManager.configuration().apiUrl())
				.setExtraHTTPHeaders(headers));

		// request -> Post 
		APIResponse response = request.post(resource, RequestOptions.create().setData(jsonBody));
		System.out.println(response.text());
		// Parse response
		return new Gson().fromJson(response.text(), JsonElement.class);		

	}

}
