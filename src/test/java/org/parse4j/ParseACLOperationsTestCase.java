package org.parse4j;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.parse4j.custom.Person;
import org.parse4j.util.ParseRegistry;

public class ParseACLOperationsTestCase extends Parse4JTestCase {

	@Test
	//TODO come back to this, use an application under my control and step through looking for ACL
	public void get() {
		//System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");

		System.out.println("get(): initializing...");
		ParseRegistry.registerSubclass(Person.class);
		Person parseObject = new Person();
		parseObject.setAge(31);
		parseObject.setGender("female");
		
		try {
			parseObject.save();
			
			ParseQuery<Person> query = ParseQuery.getQuery(Person.class);
			Person person = query.get(parseObject.getObjectId());
			System.out.println(person);
		}
		catch(ParseException pe) {
			assertNull("save(): should not have thrown ParseException", pe);
		}
	}
	
}
