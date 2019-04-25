package ResearchDatabase;

import java.util.ArrayList;


/**
 * Represents a ResearchDatabase.Faculty user of the
 *
 * @author Avery Nutting-Hartman
 */
public class Faculty extends User {

    /**
     * The faculty members email
     */
    private String email;
    /**
     * The faculty members department
     */
    private String department;

    /**
     * Creates a faculty member object
     * @param fname The faculty members first name
     * @param lname The faculty members last name
     */
    public Faculty(String fname, String lname) {
        super(fname, lname);
        try {
            Database db = new Database();
            ArrayList<ArrayList> data = db.getData("SELECT `email`,`department`,`faculty_id` FROM faculty WHERE first_name= '"+this.fname+"' AND last_name = '"+this.lname+"';");
            email=(String) data.get(0).get(0);
            department=(String) data.get(0).get(1);
            super.ID= Integer.parseInt((String)data.get(0).get(2));

        } catch (ResearchException e) {
            e.printStackTrace();
        }
    }

    /**
     * Turns the faculty member instance into a pretty printable string
     * @return String representation of a faculty member object
     */
    @Override
    public String toString() {
        return "ResearchDatabase.Faculty{" +
                "fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
