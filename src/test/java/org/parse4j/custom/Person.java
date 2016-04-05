package org.parse4j.custom;

import org.parse4j.Parse;
import org.parse4j.ParseClassName;
import org.parse4j.ParseObject;

@ParseClassName("person")
public class Person extends ParseObject {

	public Person(Parse parseContext) {
		super(parseContext);
		// TODO Auto-generated constructor stub
	}

	public void setAge(int age) {
		put("age", age);
	}
	
	public int getAge() {
		return getInt("age");
	}
	
	public void setGender(String gender) {
		put("gender", gender);
	}
	
	public String getGender() {
		return getString("gender");
	}
	
}
