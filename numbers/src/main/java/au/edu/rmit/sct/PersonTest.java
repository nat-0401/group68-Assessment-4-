package gr.rmit.A4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PersonTest {

	@Test
	void testAddPerson() {
		//the last two characters should be uppercase letters A-Z
		Person tp1 = new Person("56s_d%&fab", "Grace", "Geng",
			    "32|Highland Street|Melbourne|Victoria|Australia",  "15-11-1990", 
			    "\\[\\[15-11-1990,3\\],\\[15-12-1990,3\\]\\]",
			    true);
		assertEquals(false, tp1.addPerson());
		//the first two characters should be numbers between 2-9
		Person tp2 = new Person("abs_d%&fAB", "Grace", "Geng",
			    "32|Highland Street|Melbourne|Victoria|Australia",  "15-11-1990", 
			    "\\[\\[15-11-1990,3\\],\\[15-12-1990,3\\]\\]",
			    true);
		assertEquals(false, tp2.addPerson());
		//the state should be Victoria
		Person tp3 = new Person("56s_d%&fAB", "Grace", "Geng",
			    "32|Highland Street|Melbourne|Queensland|Australia",  "15-11-1990", 
			    "\\[\\[15-11-1990,3\\],\\[15-12-1990,3\\]\\]",
			    true);
		assertEquals(false, tp3.addPerson());
		//the personID should be 10 characters long
		Person tp4 = new Person("56s_d%&fA", "Grace", "Geng",
			    "32|Highland Street|Melbourne|Queensland|Australia",  "15-11-1990", 
			    "\\[\\[15-11-1990,3\\],\\[15-12-1990,3\\]\\]",
			    true);
		assertEquals(false, tp4.addPerson());
		//the format of birthdate should follow DD-MM-YYYY
		Person tp5 = new Person("56s_d%&fA", "Grace", "Geng",
			    "32|Highland Street|Melbourne|Queensland|Australia",  "1990-11-15", 
			    "\\[\\[15-11-1990,3\\],\\[15-12-1990,3\\]\\]",
			    true);
		assertEquals(false, tp5.addPerson());
		//fail("Not yet implemented");
	}

	@Test
	void testUpdatePersonalDetails() {
		//fail("Not yet implemented");
	}

	@Test
	void testAddDemeritPoints() {
		//fail("Not yet implemented");
	}

}
