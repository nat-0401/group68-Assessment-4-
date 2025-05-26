package au.edu.rmit.sct;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class PersonTest {

    private final String filePath = "persons.txt"; // Adjust this path as needed

    @Test
    public void testUpdateAddressForUnder18() {
        boolean result = Person.updatePersonalDetails(
            filePath,
            "23456", "23456",
            "Jane", "Smith",
            "789 New Address",
            LocalDate.of(2010, 10, 20));
        assertFalse(result);
    }

    @Test
    public void testUpdateBirthdayAndOtherDetails() {
        boolean result = Person.updatePersonalDetails(
            filePath,
            "12345", "12345",
            "John", "Dough",
            "123 Main St",
            LocalDate.of(2001, 6, 1));
        assertFalse(result);
    }

    @Test
    public void testUpdateLastNameOnly() {
        boolean result = Person.updatePersonalDetails(
            filePath,
            "12345", "12345",
            "John", "Dough",
            "123 Main St",
            LocalDate.of(2000, 5, 15));
        assertTrue(result);
    }
}
