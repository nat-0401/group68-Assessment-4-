package au.edu.rmit.sct;
import java.util.HashMap;
import java.util.Calendar;
import java.util.Date;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.time.Period;
import java.io.BufferedWriter;
import java.util.Map;

    
public class Person {
    private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private String birthdate;
    private HashMap<Date, Integer> demeritPoints;
    private boolean isSuspended;
    
    public String getPersonID() {
		return personID;
	}

	public void setPersonID(String personID) {
		this.personID = personID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public HashMap<Date, Integer> getDemeritPoints() {
		return demeritPoints;
	}

	public void setDemeritPoints(String input) {
		this.demeritPoints = convertStringToHashMap(input);
	}

	public boolean isSuspended() {
		return isSuspended;
	}
	
	public void setSuspended(boolean isSuspended) {
		this.isSuspended = isSuspended;
	}

	public Person() {
		
	}
	
	public Person(String personID,	String firstName, String lastName,
			String address,  String birthdate,	String demeritPoints,
			boolean isSuspended) {
		this.personID = personID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.birthdate = birthdate;
		this.demeritPoints = convertStringToHashMap(demeritPoints);
		this.isSuspended = isSuspended;
	}
	
	public static HashMap<Date, Integer> convertStringToHashMap(String input) {
        HashMap<Date, Integer> map = new HashMap<>();
        
        String[] pairs = input.split(",");
        if (pairs.length % 2 != 0) {
            throw new IllegalArgumentException("Input string must have an even number of elements.");
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        
        for (int i = 0; i < pairs.length; i += 2) {
            try {
                String dateStr = pairs[i];
                int points = Integer.parseInt(pairs[i + 1].trim());
                Date date = dateFormat.parse(dateStr);
                map.put(date, points);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date format: " + pairs[i], e);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid points format: " + pairs[i + 1], e);
            }
        }
        
       
        return map;
    }

	public boolean addPerson(String filePath){
		// Condition 1: personID should be exactly 10 characters long;
		if (personID.length() != 10)
			return false;
		// the first two characters should be numbers between 2 and 9;
		if (!personID.substring(0, 2).matches("[2-9]{2}"))
			return false;
		// there should be at least two special characters between characters 3 and 8;
		String middle = personID.substring(2, 8);
		int specialCount = 0;
		for (char c : middle.toCharArray()) {
			if (!Character.isLetterOrDigit(c))
				specialCount++;
		}
		if (specialCount < 2)
			return false;
		// the last two characters should be uppercase letters (A-Z);
		if (!personID.substring(8).matches("[A-Z]{2}"))
			return false;
		// Condition 2: The address of the Person should follow the following format:
		// Street Number|Street|City|State|Country;
		// The State should be only Victoria;
		if (address == null)
			return false;

		String[] parts = address.split("\\|");
		if (parts.length != 5) {
			return false;
		}

		// Check state
		if (!parts[3].trim().equals("Victoria")) {
			return false;
		}

		// Condition 3: The format of the birthdate of the person should follow the
		// following format: DD-MM-YYYY;
		if (birthdate == null) {
			return false;
		}
		// Check format DD-MM-YYYY
		Pattern pattern = Pattern.compile("^(0[1-9]|[12][0-9]|3[01])\\-(0[1-9]|1[0-2])\\-(\\d{4})$");
		Matcher matcher = pattern.matcher(birthdate);
		if( ! matcher.matches() ) {
			return false;
		}

		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
			StringBuilder sb = new StringBuilder();
	        sb.append(this.personID).append("|")
	          .append(this.firstName).append("|")
	          .append(this.lastName).append("|")
	          .append(this.address).append("|") // Address is a string in format "streetNumber|street|city|state|country"
	          .append(this.birthdate).append("|");
	        
	        // Process demeritPoints
	        for (Map.Entry<Date, Integer> entry : this.demeritPoints.entrySet()) {
	            sb.append(DATE_FORMAT.format(entry.getKey())).append(",").append(entry.getValue()).append(",");
	        }
	        if (!this.demeritPoints.isEmpty()) {
	            sb.deleteCharAt(sb.length() - 1); // Remove the last comma
	        }
	        sb.append("|")
	          .append(this.isSuspended);
			writer.write(sb.toString());
            writer.newLine();
            writer.close();
        }
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
    
    
    public boolean updatePersonalDetails(String currentId, String newId, String newFirstName, String newLastName,
                                         String newAddress, String newBirthday, String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            System.out.println("Lines loaded from file:");
            lines.forEach(System.out::println);

            List<String> updatedLines = new ArrayList<>();
            boolean updated = false;

            for (String line : lines) {
                line = line.trim();
                String[] fields = line.replace("\r", "").split(",");

                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].trim();
                }

                if (fields[0].equals(currentId)) {
                    String oldId = fields[0];
                    String oldFirstName = fields[1];
                    String oldLastName = fields[2];
                    String oldAddress = fields[3];
                    String oldBirthday = fields[4];

                    System.out.println("Processing record with ID: " + currentId);
                    System.out.println("Old details:");
                    System.out.println("ID: " + oldId);
                    System.out.println("First Name: " + oldFirstName);
                    System.out.println("Last Name: " + oldLastName);
                    System.out.println("Address: " + oldAddress);
                    System.out.println("Birthday: " + oldBirthday);

                    int age = calculateAge(oldBirthday);
                    System.out.println("Calculated age: " + age);

                    if (age < 18 && !newAddress.equals(oldAddress)) {
                        System.out.println("Update failed: Under 18 cannot change address.");
                        return false;
                    }

                    if (!newBirthday.equals(oldBirthday)) {
                        System.out.println("Birthday changed.");
                        if (!newId.equals(oldId) || !newFirstName.equals(oldFirstName)
                                || !newLastName.equals(oldLastName) || !newAddress.equals(oldAddress)) {
                            System.out.println("Update failed: When birthday changes, other details must remain unchanged.");
                            return false;
                        }
                    }

                    if (Character.isDigit(oldId.charAt(0)) && (oldId.charAt(0) - '0') % 2 == 0) {
                        if (!newId.equals(oldId)) {
                            System.out.println("Update failed: Even-digit starting ID cannot be changed.");
                            return false;
                        }
                    }

                    System.out.println("Updating details...");

                    fields[0] = newId;
                    fields[1] = newFirstName;
                    fields[2] = newLastName;
                    fields[3] = newAddress;
                    fields[4] = newBirthday;

                    line = String.join(",", fields);
                    updated = true;
                }
                updatedLines.add(line);
            }

            if (updated) {
                Files.write(Paths.get(filePath), updatedLines);
                System.out.println("File updated successfully.");
                return true;
            } else {
                System.out.println("No matching record found for update.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int calculateAge(String birthday) {
        LocalDate birthDate = LocalDate.parse(birthday);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }
    

    public String addDemeritPoints(String personId, String offenseDateStr, int points, String filePath1) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath1));
            System.out.println("Lines loaded from file:");
            lines.forEach(System.out::println);
            List<String> updatedLines = new ArrayList<>();
            boolean personFound = false;

            //testAddDemeritPointsValid(), testAddDemeritPointsInvalidPoints()
            if (points < 1 || points > 6) {
                return "Failed";
            }

            //testAddDemeritPointsInvalidDateFormat()
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate offenseDate;
            try {
                offenseDate = LocalDate.parse(offenseDateStr, formatter);
            } catch (DateTimeParseException e) {
                return "Failed";
            }

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length < 7) {
                    updatedLines.add(line);  // keep line if format unexpected
                    continue;
                }

                String id = parts[0];
                String firstName = parts[1];
                String lastName = parts[2];
                String address = parts[3];
                String birthdayStr = parts[4];  // format: DD-MM-YYYY assumed
                String totalPointsStr = parts[5];
                String suspendedStr = parts[6];

                if (!id.equals(personId)) {
                    updatedLines.add(line);
                    continue;
                }

                personFound = true;

                LocalDate birthday;
                try {
                    birthday = LocalDate.parse(birthdayStr, formatter);
                } catch (DateTimeParseException e) {
                    return "Failed";
                }

                // Calculate age at offense date
                int age = Period.between(birthday, offenseDate).getYears();

                // Collect offenses and points within last 2 years
                // For simplicity, let's assume totalPointsStr holds total points within last 2 years.
                // To be more accurate, you'd need a detailed record of offenses (date + points),
                // but here we simulate by updating the total points.

                int currentTotalPoints;
                try {
                    currentTotalPoints = Integer.parseInt(totalPointsStr);
                } catch (NumberFormatException e) {
                    currentTotalPoints = 0;  // assume 0 if bad data
                }

                int newTotalPoints = currentTotalPoints + points;

                //testUnderageSuspension(), testAdultSuspension()
                boolean suspended = false;
                if (age < 21 && newTotalPoints > 6) {
                    suspended = true;
                } else if (age >= 21 && newTotalPoints > 12) {
                    suspended = true;
                }

                this.isSuspended = suspended;

                String updatedLine = String.join(",",
                        id,
                        firstName,
                        lastName,
                        address,
                        birthdayStr,
                        Integer.toString(newTotalPoints),
                        Boolean.toString(suspended));
                updatedLines.add(updatedLine);
            }

            if (!personFound) {
                return "Failed";
            }

            Files.write(Paths.get(filePath1), updatedLines);

            return "Success";

        } catch (IOException e) {
            e.printStackTrace();
            return "Failed: IO Error";
        }
    }
}

