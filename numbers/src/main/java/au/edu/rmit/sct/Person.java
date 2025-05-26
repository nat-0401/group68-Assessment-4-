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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.time.Period;

    
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
    
    public void setDemeritPoints(HashMap<Date, Integer> demeritPoints) {
        this.demeritPoints = demeritPoints;
    }
    
    public boolean isSuspended() {
        return isSuspended;
    }
    
    public void setSuspended(boolean isSuspended) {
        this.isSuspended = isSuspended;
    }
    
        public Person() {
            
        }
    
    public Person(String personID, String firstName, String lastName,
                String address,  String birthdate, String demeritPoints,
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
        
        // Remove the square brackets and split the string into individual entries
        String[] entries = input.replaceAll("[\\[\\]\\{\\}]", "").split("],\\[");
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        
        for (String entry : entries) {
            String[] parts = entry.split(",");
            if (parts.length == 2) {
                try {
                    Date date = dateFormat.parse(parts[0]);
                    int points = Integer.parseInt(parts[1]);
                    map.put(date, points);
                } catch (ParseException e) {
                    System.err.println("Error parsing date: " + parts[0]);
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing points: " + parts[1]);
                    e.printStackTrace();
                }
            }
        }
        
        return map;
    }
    
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
    

    public String addDemeritPoints(String offenseDate, int points) {
        // Condition 1: The format of the date of the offense should follow DD-MM-YYYY
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        
        Date offense;
        Date birth;

        try {
            offense = sdf.parse(offenseDate);
            birth = sdf.parse(birthdate);
        } catch (ParseException e) {
            return "Failed"; // Fails if offense date or birthdate is not in correct format
        }

        // Condition 2: Points must be a whole number between 1-6
        if (points < 1 || points > 6) {
            return "Failed";
        }

        // Add to demeritPoints map
        demeritPoints.put(offense, points);

        // Calculate age at offense date
        Calendar birthCal = Calendar.getInstance();
        Calendar offenseCal = Calendar.getInstance();
        birthCal.setTime(birth);
        offenseCal.setTime(offense);

        int age = offenseCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
        if (birthCal.get(Calendar.MONTH) > offenseCal.get(Calendar.MONTH) ||
            (birthCal.get(Calendar.MONTH) == offenseCal.get(Calendar.MONTH) &&
            birthCal.get(Calendar.DAY_OF_MONTH) > offenseCal.get(Calendar.DAY_OF_MONTH))) {
            age--; // Adjust if birthday hasn't occurred yet this year
        }

        // Condition 3: Suspension logic based on age
        int totalPoints = 0;
        Calendar twoYearsAgo = (Calendar) offenseCal.clone();
        twoYearsAgo.add(Calendar.YEAR, -2);

        for (Date date : demeritPoints.keySet()) {
            if (!date.before(twoYearsAgo.getTime()) && !date.after(offense)) {
                totalPoints += demeritPoints.get(date);
            }
        }

        if (age < 21 && totalPoints > 6) {
            isSuspended = true;
        } else if (age >= 21 && totalPoints > 12) {
            isSuspended = true;
        }

        // Append to file
        try (FileWriter fw = new FileWriter("demeritPoints.txt", true)) {
            fw.write(personID + "|" + offenseDate + "|" + points + "\n");
        } catch (IOException e) {
            return "Failed"; // File write error
        }

        return "Success";
    }
}

