package au.edu.rmit.sct;

public class Main {
    public static void main(String[] args) {

        //FUNCTION 1
        Person tp = new Person("56s_d%&fAB", "Grace", "Geng",
			    "32|Highland Street|Melbourne|Victoria|Australia",  "15-11-1990", 
			    "15-11-1990,3",
			    true);
        boolean isAdded = tp.addPerson("Person.txt");

        System.out.println("AddPerson successful? " + isAdded);
        System.out.println("Person write to file Person.txt" );
        System.out.println("Working directory: " + System.getProperty("user.dir"));

        //FUNCTION 2
        Person person = new Person();
        String filePath = "persons.txt";
        boolean updated = person.updatePersonalDetails(
            "12345", "12345", "John", "Smith", "123 Main St", "2000-05-15", filePath);

        System.out.println("Update successful? " + updated);
        System.out.println("Working directory: " + System.getProperty("user.dir") + "\n");

        //FUNCTION 3
        Person person1 = new Person("A001","Erica","Pang","151 La Trobe St","15-05-2000","01-01-2024,6,01-02-2024,6,01-03-2024,1",false);
        String result = person1.addDemeritPoints("demeritPoints.txt");

        System.out.println("Is suspended? " + person1.isSuspended() + "\n");
        System.out.println("Added to file demeritPoints.txt? " + result + "\n");
        System.out.println("Working directory: " + System.getProperty("user.dir")+ "\n");
    }
}
