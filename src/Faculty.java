import java.util.ArrayList;

public class Faculty extends User {

    private String email;
    private String department;
    private String currentProjects;
    private String interests;

    public Faculty(String fname, String lname) {
        super(fname, lname);
        try {
            Database db = new Database();
            ArrayList<ArrayList> data = db.getData("SELECT `email`,`department`,`current_projects`,`interests` FROM faculty WHERE first_name=`"+this.fname+"` AND last_name = `"+this.lname+"`;");
            email=(String) data.get(0).get(0);
            department=(String) data.get(0).get(1);
            currentProjects=(String) data.get(0).get(2);
            interests=(String) data.get(0).get(3);
        } catch (ResearchException e) {
            e.printStackTrace();
        }

    }
}
