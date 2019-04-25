package ResearchDatabase;

import java.util.ArrayList;

/**
 * Represents a student user of the application
 *
 * @author Avery Nutting-Hartman
 */

public class Student extends User {

    /**
     * Creates a student object
     * @param fname The student members first name
     * @param lname The student members last name
     */
    public Student(String fname, String lname) {
        super(fname, lname);
        try {
            Database db = new Database();
            ArrayList<ArrayList> data = db.getData("SELECT `major`,`school_year`,`student_ID` FROM student WHERE first_name= '"+this.fname+"' AND last_name = '"+this.lname+"';");
            super.ID=Integer.parseInt((String)data.get(0).get(2));
        } catch (ResearchException e) {
            System.out.println("Error occurred!");
        }
    }
}
