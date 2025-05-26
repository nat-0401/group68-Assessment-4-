package au.edu.rmit.sct;

public class Main {
    public static void main(String[] args) {
        Person person = new Person();
        String filePath = "persons.txt";

        boolean updated = person.updatePersonalDetails(
            "12345", "12345", "John", "Smith", "123 Main St", "2000-05-15", filePath);

        System.out.println("Update successful? " + updated);

        System.out.println("Working directory: " + System.getProperty("user.dir"));

    }
}
