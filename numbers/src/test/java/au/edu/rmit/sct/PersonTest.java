package au.edu.rmit.sct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

    private final String filePath = "persons.txt";

    @BeforeEach
    public void setup() throws IOException {
        String content = "12345,John,Smith,123 Main St,2000-05-15,3,false\n" +
                         "23456,Jane,Smith,456 Park Ave,2010-10-20,1,false\n";
        Files.write(Paths.get(filePath), content.getBytes());
        System.out.println("File used for testing: " + Paths.get(filePath).toAbsolutePath());
    }

    @Test
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

    @Test
    public void testAddDemeritPointsValid() {
        Person p = new Person("P001", "Alice", "Tan", "123 Orchard Rd", "01-01-2005", "0", false);
        String result = p.addDemeritPoints("15-05-2024", 3);
        assertEquals("Success", result, "Expected Success message");
    }

    @Test
    public void testAddDemeritPointsInvalidDate() {
        Person p = new Person("P001", "Alice", "Tan", "123 Orchard Rd", "01-01-2005", "0", false);
        String result = p.addDemeritPoints("2024-05-15", 3);
        assertTrue(result.contains("Failed") || result.contains("Invalid date"), "Expected failure due to bad date");
    }

    @Test
    public void testAddDemeritPointsInvalidPoints() {
        Person p = new Person("P001", "Alice", "Tan", "123 Orchard Rd", "01-01-2005", "0", false);
        String result = p.addDemeritPoints("15-05-2024", 9);
        assertTrue(result.contains("Failed") || result.contains("Invalid points"), "Expected failure due to invalid points");
    }

    @Test
    public void testUnderageSuspension() {
        Person p = new Person("P001", "Alice", "Tan", "123 Orchard Rd", "01-01-2005", "0", false);
        p.addDemeritPoints("15-05-2024", 3);
        p.addDemeritPoints("01-06-2024", 3);
        p.addDemeritPoints("01-07-2024", 2);
        assertTrue(p.isSuspended(), "Underage person with 8 points should be suspended");
    }

    @Test
    public void testAdultSuspension() {
        Person p = new Person("P002", "Bob", "Lee", "456 Clementi Rd", "01-01-1990", "0", false);
        p.addDemeritPoints("15-05-2024", 5);
        p.addDemeritPoints("01-06-2024", 5);
        p.addDemeritPoints("01-07-2024", 4);
        assertTrue(p.isSuspended(), "Adult with 14 points should be suspended");
    }

}
