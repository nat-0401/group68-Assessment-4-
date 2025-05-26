package au.edu.rmit.sct;

public class Main {
    public static void main(String[] args) {
        Person person = new Person();

        //FUNCTION 2
        String filePath = "persons.txt";

        boolean updated = person.updatePersonalDetails(
            "12345", "12345", "John", "Smith", "123 Main St", "2000-05-15", filePath);

        System.out.println("Update successful? " + updated);

        System.out.println("Working directory: " + System.getProperty("user.dir"));

        //FUNCTION3
        Person person1 = new Person();
        String filePath1 = "demeritPoints.txt";

        person1.addDemeritPoints("P001", "01-01-2024", 4, filePath1);
        person1.addDemeritPoints("P001", "02-01-2024", 3, filePath1);

        System.out.println("Is suspended? " + person1.isSuspended());

        System.out.println("Working directory: " + System.getProperty("user.dir"));
    }
}
