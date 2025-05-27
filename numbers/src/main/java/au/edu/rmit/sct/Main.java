package au.edu.rmit.sct;

public class Main {
    public static void main(String[] args) {
        Person person = new Person();

        //FUNCTION 1
        Person tp = new Person("56s_d%&fab", "Grace", "Geng",
			    "32|Highland Street|Melbourne|Victoria|Australia",  "15-11-1990", 
			    "15-11-1990, 3,15-12-1990, 3",
			    true);
        boolean isadded = tp.addPerson("./Person.txt");
	System.out.println("AddPerson successful? " + isadded);
	System.out.println("Personal information is added to file ./Person.txt");

        //FUNCTION 2
        String filePath = "persons.txt";

        boolean updated = person.updatePersonalDetails(
            "12345", "12345", "John", "Smith", "123 Main St", "2000-05-15", filePath);

        System.out.println("Update successful? " + updated);

        System.out.println("Working directory: " + System.getProperty("user.dir"));

        //FUNCTION 3
        Person person1 = new Person();
        String filePath1 = "demeritPoints.txt";

        person1.addDemeritPoints("P001", "01-01-2024", 4, filePath1);
        person1.addDemeritPoints("P001", "02-01-2024", 3, filePath1);

        System.out.println("Is suspended? " + person1.isSuspended());

        System.out.println("Working directory: " + System.getProperty("user.dir"));
    }
}
