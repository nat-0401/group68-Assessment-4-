package au.edu.rmit.sct;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    @TestMethodOrder(OrderAnnotation.class)
    @Nested
    class UpdatePersonalDetailsTests {

        private final String filePath = "persons.txt";

        @BeforeEach
        public void setup() throws IOException {
            String content = "12345,John,Smith,123 Main St,2000-05-15,3,false\n" +
                            "23456,Jane,Smith,456 Park Ave,2010-10-20,1,false\n";
            Files.write(Paths.get(filePath), content.getBytes());
            System.out.println("File used for testing: " + Paths.get(filePath).toAbsolutePath());
        }

        @Test
        @Order(1)
        public void testUpdateAddressForUnder18() {
            Person person = new Person();
            boolean result = person.updatePersonalDetails(
                "23456", "23456",
                "Jane", "Smith",
                "789 New Address",
                "2010-10-20",
                filePath);
            System.out.println("testUpdateAddressForUnder18 returned: " + result);
            assertFalse(result);
        }

        @Test
        @Order(2)
        public void testUpdateBirthdayAndOtherDetails() {
            Person person = new Person();
            boolean result = person.updatePersonalDetails(
                "12345", "12345",
                "John", "Dough",
                "123 Main St",
                "2001-06-01",
                filePath);
            System.out.println("testUpdateBirthdayAndOtherDetails returned: " + result);
            assertFalse(result);
        }

        @Test
        @Order(3)
        public void testUpdateLastNameOnly() {
            Person person = new Person();
            boolean result = person.updatePersonalDetails(
                "12345", "12345",
                "John", "Dough",
                "123 Main St",
                "2000-05-15",
                filePath);
            System.out.println("testUpdateLastNameOnly returned: " + result);
            assertTrue(result);
        }

        @Test
        @Order(4)
        public void testUpdateIdWithEvenStartingDigit() {
            Person person = new Person();
            boolean result = person.updatePersonalDetails(
                "23456", "99999",  
                "Jane", "Smith",
                "456 Park Ave",
                "2010-10-20",
                filePath);
            System.out.println("testUpdateIdWithEvenStartingDigit returned: " + result);
            assertFalse(result);
        }

        @Test
        @Order(5)
        public void testUpdateMultipleFieldsExceptBirthdayForAdult() {
            Person person = new Person();
            boolean result = person.updatePersonalDetails(
                "12345", "12345",
                "Johnny", "Smithson",
                "321 New St",
                "2000-05-15",  
                filePath);
            System.out.println("testUpdateMultipleFieldsExceptBirthdayForAdult returned: " + result);
            assertTrue(result);
        }
    }

    @TestMethodOrder(OrderAnnotation.class)
    @Nested
    class AddDemeritPointsTests {

        private final String filePath1 = "demeritPoints.txt";

        @BeforeEach
        public void resetFile() throws IOException {
            List<String> originalLines = List.of(
                "P001,Alice,Tan,123 Orchard Rd,15-05-2005,0,false",
                "P002,Bob,Lee,456 Clementi Rd,15-05-1990,0,false"
            );
            Files.write(Paths.get(filePath1), originalLines);
            System.out.println("File used for testing: " + Paths.get(filePath1).toAbsolutePath());
        }


        @Test
        @Order(1)
        public void testAddDemeritPointsValid() {
            Person person = new Person();
            String result = person.addDemeritPoints("P001", "15-05-2024", 3, filePath1);
            System.out.println("testAddDemeritPointsValid returned: " + result + "\n");
            assertTrue(result.contains("Success"));
        }

        @Test
        @Order(2)
        public void testAddDemeritPointsInvalidDateFormat() {
            Person person = new Person();
            String result = person.addDemeritPoints("P001", "2024-05-15", 3, filePath1);
            System.out.println("testAddDemeritPointsInvalidDateFormat returned: " + result + "\n");
            assertTrue(result.contains("Failed"));
        }

        @Test
        @Order(3)
        public void testAddDemeritPointsInvalidPoints() {
            Person person = new Person();
            String result = person.addDemeritPoints("P001", "15-05-2024", 7, filePath1);
            System.out.println("testAddDemeritPointsInvalidPoints returned: " + result + "\n");
            assertTrue(result.contains("Failed"));
        }

        @Test
        @Order(4)
        public void testUnderageSuspension() {
            Person person = new Person();
            person.addDemeritPoints("P001", "01-01-2024", 4, filePath1);
            String result = person.addDemeritPoints("P001", "02-01-2024", 3, filePath1);
            System.out.println("testUnderageSuspension returned: " + result + "\n");
            assertTrue(person.isSuspended());
        }

        @Test
        @Order(5)
        public void testAdultSuspension() {
            Person person = new Person();
            person.addDemeritPoints("P002", "01-01-2024", 6, filePath1);
            person.addDemeritPoints("P002", "02-01-2024", 6, filePath1);
            String result = person.addDemeritPoints("P002", "03-01-2024", 6, filePath1);
            System.out.println("testAdultSuspension returned: " + result + "\n");
            assertTrue(person.isSuspended());
        }
    }
    
    
    @TestMethodOrder(OrderAnnotation.class)
    @Nested
    
    class AddPersonTests {
		private final String filePath = "Person.txt";
	@Test
	@Order(1)
	void testAddPerson1() {
		//the last two characters should be uppercase letters A-Z
		Person tp1 = new Person("56s_d%&fab", "Grace", "Geng",
			    "32|Highland Street|Melbourne|Victoria|Australia",  "15-11-1990", 
			    "15-11-1990, 3,15-12-1990, 3",
			    true);
		boolean result = tp1.addPerson(filePath);
		assertFalse(result);
		System.out.println("testAddPerson1 failed: personID error: the last two characters should be uppercase letters A-Z");
		System.out.println("test case 1 result: successful!");
	}
	
	@Test
	@Order(2)
	void testAddPerson2() {
		//the first two characters should be numbers between 2-9
		Person tp2 = new Person("abs_d%&fAB", "Grace", "Geng",
			    "32|Highland Street|Melbourne|Victoria|Australia",  "15-11-1990", 
			    "15-11-1990, 3,15-12-1990, 3",
			    true);
		boolean result = tp2.addPerson(filePath);
		assertFalse(result);
		System.out.println("testAddPerson2 failed: personID error: the first two characters should be numbers between 2-9");
		System.out.println("test case 2 result: successful!");
	}
	
	@Test
	@Order(3)
	void testAddPerson3() {
		//the state should be Victoria
		Person tp3 = new Person("56s_d%&fAB", "Grace", "Geng",
			    "32|Highland Street|Melbourne|Queensland|Australia",  "15-11-1990", 
			    "15-11-1990, 3,15-12-1990, 3",
			    true);
		boolean result = tp3.addPerson(filePath);
		assertFalse(result);
		System.out.println("testAddPerson3 failed: the state should be Victoria");
		System.out.println("test case 3 result: successful!");
	}
	
	@Test
	@Order(4)
	void testAddPerson4() {
		//Correctly add personal information 
		Person tp4 = new Person("56s_d%&fAB", "Grace", "Geng",
			    "32|Highland Street|Melbourne|Victoria|Australia",  "15-11-1990", 
			    "15-11-1990, 3,15-12-1990, 3",
			    true);
		boolean result = tp4.addPerson(filePath);
		assertTrue(result);
		System.out.println("testAddPerson4 finished: personal information added");
		System.out.println("test case 4 result: successful!");
	}
	
	@Test
	@Order(5)
	void testAddPerson5() {
		//the format of birthdate should follow DD-MM-YYYY
		Person tp5 = new Person("56s_d%&fAB", "Grace", "Geng",
			    "32|Highland Street|Melbourne|Victoria|Australia",  "1990-11-15", 
			    "15-11-1990, 3,15-12-1990, 3",
			    true);
		boolean result = tp5.addPerson(filePath);
		assertFalse(result);
		System.out.println("testAddPerson5 failed: the format of birthdate should follow DD-MM-YYYY");
		System.out.println("test case 5 result: successful!");
	}

}
}
