import java.util.ArrayList;

public class Faculty extends User {

    private String email;
    private String department;

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

    @Override
    public String toString() {
        return "Faculty{" +
                "fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
