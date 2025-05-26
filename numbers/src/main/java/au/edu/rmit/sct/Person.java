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
