package ResearchDatabase;

/**
 * The super class for all users that stores some common behavior
 *
 * @author Avery Nutting-Hartman
 */
public class User {

    /**
     * The ID of the user
     */
    int ID;
    /**
     * The first name of the user
     */
    String fname;
    /**
     * The last name of the user
     */
    String lname;

    /**
     * Creates a user object
     * @param fname The first name
     * @param lname The last name
     */
    public User(String fname, String lname) {
        this.fname = fname;
        this.lname = lname;
    }

    /**
     * Prints the users in a pretty way
     * @return string representation of user
     */
    public String prettyPrint(){
        return fname+" "+lname+" "+"ID#: "+ID;
    }
}
