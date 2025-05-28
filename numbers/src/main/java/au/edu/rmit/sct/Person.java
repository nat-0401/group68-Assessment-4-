package au.edu.rmit.sct;
import java.util.HashMap;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.time.Period;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
    
public class Person {
    private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private String birthdate;
    private HashMap<Date, List<Integer>> demeritPoints;
    private boolean isSuspended;
    private String rawDemeritPointsString;
    
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
    
    public HashMap<Date, List<Integer>> getDemeritPoints() {
		  return demeritPoints;
	}

	public void setDemeritPoints(String input) {
		  this.demeritPoints = convertStringToHashMapList(input);
	}
    
    public boolean isSuspended() {
        return isSuspended;
    }
    
    public void setSuspended(boolean isSuspended) {
        this.isSuspended = isSuspended;
    }
    
    public Person() {}

    public Person(String personID, String firstName, String lastName, String address,  String birthdate, String demeritPoints, boolean isSuspended) {
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.birthdate = birthdate;
        this.rawDemeritPointsString = demeritPoints;
        this.isSuspended = isSuspended;
    }

    public static HashMap<Date, List<Integer>> convertStringToHashMapList(String input) {
        HashMap<Date, List<Integer>> map = new HashMap<>();
        String[] pairs = input.split(",");
        if (pairs.length % 2 != 0) {
            throw new IllegalArgumentException("Input string must have an even number of elements.");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);

        for (int i = 0; i < pairs.length; i += 2) {
            try {
                String dateStr = pairs[i];
                int points = Integer.parseInt(pairs[i + 1].trim());
                Date date = dateFormat.parse(dateStr);

                if (!map.containsKey(date)) {
                    map.put(date, new ArrayList<>());
                }
                map.get(date).add(points);

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

	        demeritPoints = convertStringToHashMapList(rawDemeritPointsString);
	        // Process demeritPoints
            for (Map.Entry<Date, List<Integer>> entry : this.demeritPoints.entrySet()) {
                Date date = entry.getKey();
                List<Integer> pointsList = entry.getValue();

                for (Integer point : pointsList) {
                    sb.append(DATE_FORMAT.format(date)).append("|").append(point).append("|");
                }
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
    

    public String addDemeritPoints() {
        //Write to file demeritPoints.txt pt.1 (begin)
        StringBuilder fileContent = new StringBuilder(); 
        fileContent.append("PersonID: " + personID + "\n");
        fileContent.append(" Log        | Point   \n");

        int totalPoints = 0;
        LocalDate today = LocalDate.now();

        //Condition 1 & 2: The format of the date of the offense should follow the following format: DD-MM-YYYY;
        //The demerit points must be a whole number.
        try {
            demeritPoints = convertStringToHashMapList(rawDemeritPointsString);
        } catch (IllegalArgumentException e) {
            return "Failed"; 
        }

        for (Map.Entry<Date, List<Integer>> entry : demeritPoints.entrySet()) {
            Date offenseDate = entry.getKey();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);
            String formattedDate = sdf.format(offenseDate);

            LocalDate offenseLocalDate = offenseDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            long yearsDiff = ChronoUnit.YEARS.between(offenseLocalDate, today);

            //Condition 1 pt2 EXTRA: The offense date should not be after the current date;
            if (offenseLocalDate.isAfter(today)) {
                return "Failed";
            }

            for (Integer points : entry.getValue()) {
                //Condition 2 pt2: The demerit points must be between 1-6;
                if (points < 1 || points > 6) {
                    return "Failed";
                }

                if (yearsDiff < 2) {
                    totalPoints += points;
                }

                //Write to file demeritPoints.txt pt.2 (loop)
                fileContent.append(" " + formattedDate + " | " + points + "\n");
            }
        }

        //Condition 3: If the person is under 21, the isSuspended variable should be set to true if the total demerit points within two years exceed 6;
	    //If the person is over 21, the isSuspended variable should be set to true if the total demerit points within two years exceed 12
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthDateObj;
        try {
            birthDateObj = LocalDate.parse(this.birthdate, formatter);
        } catch (Exception e) {
            return "Failed";
        }
        int age = Period.between(birthDateObj, today).getYears();

        if ((age < 21 && totalPoints > 6) || (age >= 21 && totalPoints > 12)) {
            setSuspended(true);
        } else {
            setSuspended(false);
            return "Failed";
        }

        //Write to file demeritPoints.txt pt.3 (end)
        fileContent.append("Total points within 2 years: " + totalPoints + "\n");
        fileContent.append("is " + personID + " suspended? " + isSuspended() + "\n" + "\n");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("demeritPoints.txt", true))) {
            writer.write(fileContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed";
        }

        return "Success";
    }

}



