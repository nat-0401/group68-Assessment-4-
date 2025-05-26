package au.edu.rmit.sct;
 
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
 
public class PersonFileHandler {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private static final String DELIMITER = "\\|";
 
    private final String filePath;
 
    // Constructor, accepts file path as parameter
    public PersonFileHandler(String filePath) {
        this.filePath = filePath;
    }
 
    // Write a Person object to the file
    public void writePersonToFile(Person person) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(personToFileString(person));
            writer.newLine();
            writer.close();
        }
    }
 
    // Read all Person objects from the file
    public List<Person> readAllPersonsFromFile() throws IOException, ParseException {
        List<Person> persons = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null ) {
                if( !line.trim().isEmpty() ) {
                    persons.add(stringToPerson(line));
                }
            }
            reader.close();
        }
        
        return persons;
    }
 
 
    // Update a Person object in the file by personID
    /*
    public void updatePersonInFile(Person updatedPerson) throws IOException, ParseException {
        // Read all existing records
        List<Person> persons = readAllPersonsFromFile();
        
        // Find and update the matching record
        boolean found = false;
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getPersonID().equals(updatedPerson.getPersonID())) {
                persons.set(i, updatedPerson);
                found = true;
                break;
            }
        }
        
        // If no matching record is found, optionally add new record or throw exception
        if (!found) {
            throw new IOException("Person with ID " + updatedPerson.getPersonID() + " not found in file.");
        }
        
        // Rewrite the entire file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Person person : persons) {
                writer.write(personToFileString(person));
                writer.newLine();
            }
        }
    }
    */
 
    // Convert Person object to file string format
    private String personToFileString(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(person.getPersonID()).append("|")
          .append(person.getFirstName()).append("|")
          .append(person.getLastName()).append("|")
          .append(person.getAddress()).append("|") // Address is a string in format "streetNumber|street|city|state|country"
          .append(person.getBirthdate()).append("|");
        
        // Process demeritPoints
        sb.append("[");
        for (Map.Entry<Date, Integer> entry : person.getDemeritPoints().entrySet()) {
            sb.append(DATE_FORMAT.format(entry.getKey())).append(":").append(entry.getValue()).append(",");
        }
        if (!person.getDemeritPoints().isEmpty()) {
            sb.deleteCharAt(sb.length() - 1); // Remove the last comma
        }
        sb.append("]|")
          .append(person.isSuspended());
        System.out.println(sb);
        return sb.toString();
    }
 
    // Convert file string to Person object
    private Person stringToPerson(String line) throws ParseException {
        String[] parts = line.split(DELIMITER);
        if (parts.length < 10) {
            return null;
        }
        
        int index = 0;
        Person person = new Person();
        person.setPersonID(parts[index++]);
        person.setFirstName(parts[index++]);
        person.setLastName(parts[index++]);
        
        // Address is in format "streetNumber|street|city|state|country"
        StringBuilder sb = new StringBuilder();
        for (int i = 3; i <= 7; i++) {
            if (i > 3) {
                sb.append("|");
            }
            sb.append(parts[i]);
        }
        String address = sb.toString();
        person.setAddress(address);
        
        // Process birthday
        person.setBirthdate(parts[8]);
        
        // Process demeritPoints
        String demeritPointsStr = parts[9];
        HashMap<Date, Integer> demeritPoints = new HashMap<>();
        if (!demeritPointsStr.equals("[]")) {
            System.out.println(demeritPointsStr);
//            String[] entries = demeritPointsStr.substring(1, demeritPointsStr.length() - 1).split(",");
            System.out.printf("demeritPointsStr: %s\n",demeritPointsStr.replaceAll("\\\\[\\\\[|\\\\]\\\\]", ""));
            String[] entries = demeritPointsStr.replaceAll("[\\[\\]\\{\\}]", "").split("],\\[");
            for (String entry : entries) {
                System.out.printf("Entry: %s\n",entry);
                String[] keyValue = entry.split(",");
                Date date = DATE_FORMAT.parse(keyValue[0]);
                int points = Integer.parseInt(keyValue[1]);
                demeritPoints.put(date, points);
            }
        }
        person.setDemeritPoints(demeritPoints);
        
        // Process suspension status
        person.setSuspended(Boolean.parseBoolean(parts[10]));
        
        return person;
    }
    
}