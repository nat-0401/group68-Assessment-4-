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
    //FUNCTION 2
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

    //FUNCTION 3
    @TestMethodOrder(OrderAnnotation.class)
    @Nested
    class AddDemeritPointsTests {

        @Test
        @Order(1)
        public void testAddDemeritPoints1() {
            //Test Case 1: Check with invalid offense date
            System.out.println("---testAddDemeritPoints1()---");
                //Test Data 1: yyyy-mm-dd
                Person person = new Person("A001","Erica","Pang","151 La Trobe St","15-05-2008","2024-01-01,3",false);
                String result = person.addDemeritPoints();

                //Test Data 2: month out of range
                Person person2 = new Person("B002","Rui","Geng","332 Caviar St","29-05-2005","31-13-2023,2",false);
                String result2 = person2.addDemeritPoints();

                //Test Data 3: date > current date (future)
                Person person3 = new Person("C003","Nathaniel","Kong","481 Garden St","28-05-2010","12-12-2028,4",false);
                String result3 = person3.addDemeritPoints();

                assertAll("", ()->assertEquals("Failed", result), ()->assertEquals("Failed", result2), ()->assertEquals("Failed", result3));
                System.out.println("test data 1 result: " + result + " (Error: date format should be dd--mm--yyyy)");
                System.out.println("test data 2 result: " + result2 + " (Error: month must not exceed 12)");
                System.out.println("test data 3 result: " + result3 + " (Error: offense date must not be in the future)");
        }

        @Test
        @Order(2)
        public void testAddDemeritPoints2() {
            //Test Case 2: Check with invalid demerit points input
            System.out.println("---testAddDemeritPoints2()---");
                //Test Data 1: points < 1
                Person person = new Person("A001","Erica","Pang","151 La Trobe St","15-05-2008","01-01-2024,0",false);
                String result = person.addDemeritPoints();

                //Test Data 2: points > 6
                Person person2 = new Person("B002","Rui","Geng","332 Caviar St","29-05-2005","31-12-2023,7",false);
                String result2 = person2.addDemeritPoints();

                //Test Data 3: decimal
                Person person3 = new Person("C003","Nathaniel","Kong","481 Garden St","28-05-2010","12-12-2023,5.5",false);
                String result3 = person3.addDemeritPoints();

                //Test Data 4: empty variable
                Person person4 = new Person("D004","Jerry","Lim","293 Salmon St","05-07-1999","12-12-2023",false);
                String result4 = person4.addDemeritPoints();

                assertAll("", ()->assertEquals("Failed", result), ()->assertEquals("Failed", result2), ()->assertEquals("Failed", result3), ()->assertEquals("Failed", result4));
                System.out.println("test data 1 result: " + result + " (Error: point must not be lesser than 1)");
                System.out.println("test data 2 result: " + result2 + " (Error: point must not be greater than 6)");
                System.out.println("test data 3 result: " + result3 + " (Error: only whole numbers are accepted)");
                System.out.println("test data 4 result: " + result4 + " (Error: point(string) must not be empty)");
        }

        @Test
        @Order(3)
        public void testAddDemeritPoints3() {            
            //Test Case 3: Check with valid input, isSuspended not triggered for under 21
            System.out.println("---testAddDemeritPoints3()---");
                //Test Data 1: demerits not within 2 years
                Person person = new Person("A001","Erica","Pang","151 La Trobe St","15-05-2008","01-01-2020,3,01-02-2024,4",false);
                String result = person.addDemeritPoints();

                //Test Data 2: point <= 6
                Person person2 = new Person("B002","Rui","Geng","332 Caviar St","29-05-2005","30-12-2023,2,6-03-2025,1,16-06-2024,1",false);
                String result2 = person2.addDemeritPoints();

                //Test Data 3: point == 6
                Person person3 = new Person("C003","Nathaniel","Kong","481 Garden St","28-05-2011","12-12-2023,4,7-05-2024,2",false);
                String result3 = person3.addDemeritPoints();

                assertAll("", ()->assertEquals("Failed", result), ()->assertEquals("Failed", result2), ()->assertEquals("Failed", result3));
                assertAll("", ()->assertFalse(person.isSuspended()), ()->assertFalse(person2.isSuspended()), ()->assertFalse(person3.isSuspended()));
                System.out.println("isSuspended? " + person.isSuspended());
                System.out.println("test data 1 result: " + result + " (Error: isSuspended returned false, cumulative demerit points did not occur within 2 years timeframe)");
                System.out.println("isSuspended? " + person2.isSuspended());
                System.out.println("test data 2 result: " + result2 + " (Error: isSuspended returned false, cumulative demerit points is 4 which is less than or equal to 6)");
                System.out.println("isSuspended? " + person3.isSuspended());
                System.out.println("test data 3 result: " + result3 + " (Error: isSuspended returned false, cumulative demerit points is 6 which is less or equal to 6)");
        }

        @Test
        @Order(4)
        public void testAddDemeritPoints4() {
            //Test Case 4: Check with valid input, isSuspended not triggered for over 21
            System.out.println("---testAddDemeritPoints4()---");
                //Test Data 1: demerits not within 2 years
                Person person = new Person("A001","Erica","Pang","151 La Trobe St","15-05-2000","01-01-2020,6,01-02-2024,6,01-03-2024,1",false);
                String result = person.addDemeritPoints();

                //Test Data 2: point <= 12
                Person person2 = new Person("B002","Rui","Geng","332 Caviar St","29-05-2003","31-12-2023,5,6-03-2025,2",false);
                String result2 = person2.addDemeritPoints();

                //Test Data 3: point == 12
                Person person3 = new Person("C003","Nathaniel","Kong","481 Garden St","28-05-1997","12-12-2023,6,7-05-2024,6",false);
                String result3 = person3.addDemeritPoints();

                assertAll("", ()->assertEquals("Failed", result), ()->assertEquals("Failed", result2), ()->assertEquals("Failed", result3));
                assertAll("", ()->assertFalse(person.isSuspended()), ()->assertFalse(person2.isSuspended()), ()->assertFalse(person3.isSuspended()));
                System.out.println("isSuspended? " + person.isSuspended());
                System.out.println("test data 1 result: " + result + " (Error: isSuspended returned false, cumulative demerit points did not occur within 2 years timeframe)");
                System.out.println("isSuspended? " + person2.isSuspended());
                System.out.println("test data 2 result: " + result2 + " (Error: isSuspended returned false, cumulative demerit points is 7 which is less than or equal to 12)");
                System.out.println("isSuspended? " + person3.isSuspended());
                System.out.println("test data 3 result: " + result3 + " (Error: isSuspended returned false, cumulative demerit points is 12 which is less than or equal to 12)");
        }

        @Test
        @Order(5)
        public void testAddDemeritPoints5() {
            //Test Case 5: Check with all valid inputs and isSuspended triggered.
            System.out.println("---testAddDemeritPoints5()---");
                //Test Data 1: point > 12, over 21
                Person person = new Person("A001","Erica","Pang","151 La Trobe St","15-05-2000","01-01-2024,6,01-02-2024,6,01-03-2024,1",false);
                String result = person.addDemeritPoints();

                 //Test Data 2: point > 6, under 21
                Person person2 = new Person("B002","Rui","Geng","332 Caviar St","29-05-2009","31-12-2023,5,6-03-2025,2",false);
                String result2 = person2.addDemeritPoints();

                assertAll("", ()->assertEquals("Success", result), ()->assertEquals("Success", result2));
                assertAll("", ()->assertTrue(person.isSuspended()), ()->assertTrue(person2.isSuspended()));
                System.out.println("isSuspended? " + person.isSuspended());
                System.out.println("test data 1 result: " + result + " ✓✓✓ ALL CONDITIONS MET! Demerit info added to txt.file. ✓✓✓");
                System.out.println("isSuspended? " + person2.isSuspended());
                System.out.println("test data 2 result: " + result2 + " ✓✓✓ ALL CONDITIONS MET! Demerit info added to txt.file. ✓✓✓");
        }
    }
    
    //FUNCTION 1
    @TestMethodOrder(OrderAnnotation.class)
    @Nested
    class AddPersonalTests {
		
		private final String filePath = "Person.txt";
		
		@BeforeEach
        public void clearFile() throws IOException {
			if (!Files.exists(Paths.get(filePath))) {
				Files.createFile(Paths.get(filePath));
			}
//			else {
//				Files.write(Paths.get(filePath), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
//			}

        }
		
		@Test
        @Order(1)
		void testAddPerson1() {
			//the last two characters should be uppercase letters A-Z
			Person tp1 = new Person("56s_d%&fab", "Grace", "Geng",
				    "32|Highland Street|Melbourne|Victoria|Australia",  "15-11-1990", 
				    "15-11-1990|3",
				    true);
			System.out.println("testAddPerson1: personID error: the last two characters should be uppercase letters A-Z");
			boolean result = tp1.addPerson(filePath);
			assertFalse(result);
			System.out.println("testAddPerson1: successful!");
		}
		
		@Test
        @Order(2)
		void testAddPerson2() {
			//the first two characters should be numbers between 2-9
			Person tp2 = new Person("abs_d%&fAB", "Grace", "Geng",
				    "32|Highland Street|Melbourne|Victoria|Australia",  "15-11-1990", 
				    "15-11-1990|3",
				    true);
			System.out.println("testAddPerson2: personID error: the first two characters should be numbers between 2-9");
			assertFalse(tp2.addPerson(filePath));
			System.out.println("testAddPerson2: successly!");
		}
		
		@Test
        @Order(3)
		void testAddPerson3() {
			//the state should be Victoria
			Person tp3 = new Person("56s_d%&fAB", "Grace", "Geng",
				    "32|Highland Street|Melbourne|Queensland|Australia",  "15-11-1990", 
				    "15-11-1990|3",
				    true);
			System.out.println("testAddPerson3: the state should be Victoria");
			assertFalse(tp3.addPerson(filePath));
			System.out.println("testAddPerson3: successful!");
		}
		
		@Test
        @Order(4)
		void testAddPerson4() {
			//add correct personal information
			Person tp4 = new Person("56s_d%&fAB", "Grace", "Geng",
				    "32|Highland Street|Melbourne|Victoria|Australia",  "15-11-1990", 
				    "15-11-1990| 3",
				    true);
			System.out.println("testAddPerson4: the personID should be 10 characters long");
			assertFalse(tp4.addPerson(filePath));
			System.out.println("testAddPerson4: successful!");
		}
		
		@Test
        @Order(5)
		void testAddPerson5() {
			//the format of birthdate should follow DD-MM-YYYY
			Person tp5 = new Person("56s_d%&fAB", "Grace", "Geng",
				    "32|Highland Street|Melbourne|Victoria|Australia",  "1990-11-15", 
				    "15-11-1990| 3",
				    true);
			System.out.println("testAddPerson5: the format of birthdate should follow DD-MM-YYYY");
			assertFalse(tp5.addPerson(filePath));
			System.out.println("testAddPerson5: successful!");
		}
	}
//	@Test
//	void testUpdatePersonalDetails() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testAddDemeritPoints() {
//		fail("Not yet implemented");
//	}

}
