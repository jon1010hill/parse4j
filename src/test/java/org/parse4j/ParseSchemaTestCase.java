package org.parse4j;

import java.util.List;

import org.junit.Test;
import org.parse4j.util.ParseRegistry;

import static org.junit.Assert.*;

public class ParseSchemaTestCase extends Parse4JTestCase {

	@Test
	public void testSchemaQueryToSchmeaObject() {

		ParseRegistry.registerSubclass(ParseSchema.class);
		ParseQuery<ParseSchema> query = ParseQuery.getQuery(ParseSchema.class,PARSE);
		List<ParseSchema> list;
		try {
			list = query.find();
			assertNotNull(list);
			assertTrue(list.size()>0);
			for(ParseSchema ps : list) {
				ps.getFields();
			}
		} catch (ParseException e) {
			fail("ParseException should not have been thrown");
		}

	}
}
