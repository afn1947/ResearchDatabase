import java.util.ArrayList;

public class Student extends User {

    private String major;
    private String schoolYear;
    private String projects;
    private String interests;

    public Student(String fname, String lname) {
        super(fname, lname);
        try {
            Database db = new Database();
            ArrayList<ArrayList> data = db.getData("SELECT `major`,`school_year`,`current_projects`,`interests` FROM student WHERE first_name= '"+this.fname+"' AND last_name = '"+this.lname+"';");
            major=(String) data.get(0).get(0);
            schoolYear=(String) data.get(0).get(1);
            projects=(String) data.get(0).get(2);
            interests=(String) data.get(0).get(3);
        } catch (ResearchException e) {
            e.printStackTrace();
        }
    }



}
