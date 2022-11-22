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

package com.force.data.dynamic;

import net.datafaker.Faker;
import java.util.Locale;
import com.force.enums.Country;

import static com.force.config.ConfigurationManager.configuration;

public class FakerDataFactory {

	private static final Faker faker = new Faker(new Locale(configuration().faker()));
	
	private FakerDataFactory() {
		
	}

	public static String getCompanyName() {
		return faker.company().name().replaceAll("[^a-zA-Z ]", "");
	}

	public static String getUrl() {
		return faker.company().url();
	}

	public static String getAddress() {
		return faker.address().streetAddress();
	}
	
	public static String getCity() {
		return faker.address().city();
	}

	public static String getCountry() {
		return Country.getRandom().get().toString();
	}

	public static String getFirstName() {
		return faker.name().firstName();
	}
	
	public static String getLastName() {
		return faker.name().lastName();
	}
	
	public static String getEmailAddress() {
		return faker.internet().emailAddress();
	}
	
	public static String getContactNumber() {
		return faker.phoneNumber().cellPhone();
	}

	public static String getBankAccountNumber() {
		return ""+faker.number().randomNumber(8, false);
	}
	
	public static String getSwiftCode() {
		return ""+faker.number().randomNumber(4, false);
	}
	public static String getTaxNumber() {
		return ""+faker.number().randomNumber(7, false);
	}
	
	public static String getRating() {
		return faker.options().option("Hot","Warm", "Cold");
	}
	
	public static String getSalutation() {
		return faker.options().option("Mr.","Ms.", "Mrs.", "Dr.", "Prof.");
	}
	
	

}
