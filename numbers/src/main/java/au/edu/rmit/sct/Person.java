package au.edu.rmit.sct;
import java.util.HashMap;
import java.util.Date;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Person {
    public boolean addPerson() {
		//Condition 1: personID should be exactly 10 characters long;
		if (personID.length() != 10)
			return false;
		//the first two characters should be numbers between 2 and 9;
		if (!personID.substring(0, 2).matches("[2-9]{2}"))
			return false;
		//there should be at least two special characters between characters 3 and 8;
		String middle = personID.substring(2, 8);
		int specialCount = 0;
		for (char c : middle.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) specialCount++;
        }
		if (specialCount < 2)
			return false;
		//the last two characters should be uppercase letters (A-Z);
		if (!personID.substring(8).matches("[A-Z]{2}"))
			return false;
		//Condition 2: The address of the Person should follow the following format: Street Number|Street|City|State|Country;
		// The State should be only Victoria;
	     if (address == null)
	    	 return false;

	     String[] parts = address.split("\\|");
	     if (parts.length != 5) {
	    	 return false;
	     }

	     // Check state
	     //String state = parts[3].trim();
	     if (!(parts[3].trim().equals("Victoria"))) {
	    	 return false;
	     }
		//Condition 3: The format of the birthdate of the person should follow the following format: DD-MM-YYYY;
		
	     if (birthdate == null) {
	        return false;
	     }

	        // Check format DD-MM-YYYY
	        Pattern pattern = Pattern.compile("^(0[1-9]|[12][0-9]|3[01])\\-(0[1-9]|1[0-2])\\-(\\d{4})$");
	        Matcher matcher = pattern.matcher(birthdate);
	     if (!matcher.matches()) {
	    	 return false;
	     }
	      return true;
	}





    public static boolean updatePersonalDetails(
            String filePath,
            String currentId,
            String newId,
            String newFirstName,
            String newLastName,
            String newAddress,
            LocalDate newBirthday) {

        try {
            Path path = Path.of(filePath);
            List<String> lines = Files.readAllLines(path);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            boolean updated = false;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");

                String id = parts[0];
                String firstName = parts[1];
                String lastName = parts[2];
                String address = parts[3];
                LocalDate birthday = LocalDate.parse(parts[4], formatter);
                String demeritPoints = parts[5];
                String suspended = parts[6];

                if (!id.equals(currentId)) {
                    continue; 
                }

                boolean firstCharIsDigit = Character.isDigit(id.charAt(0));
                boolean isFirstCharEven = firstCharIsDigit && ((id.charAt(0) - '0') % 2 == 0);
                if (isFirstCharEven && !newId.equals(id)) {
                    return false; 
                }

                if (!newBirthday.equals(birthday)) {
                    if (!newId.equals(id) || !newFirstName.equals(firstName) || !newLastName.equals(lastName) || !newAddress.equals(address)) {
                        return false; 
                    }
                }

                int age = LocalDate.now().getYear() - birthday.getYear();
                if (age < 18 && !newAddress.equals(address)) {
                    return false; 
                }

                String updatedLine = String.join(",",
                        newId,
                        newFirstName,
                        newLastName,
                        newAddress,
                        newBirthday.format(formatter),
                        demeritPoints,
                        suspended);

                lines.set(i, updatedLine);
                updated = true;
                break; 
            }

            if (updated) {
                Files.write(path, lines);
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
