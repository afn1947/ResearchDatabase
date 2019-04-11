import java.util.ArrayList;

public class Student extends User {

    private String major;
    private String schoolYear;
    private ArrayList<Project> projects;
    private String interests;

    public Student(String fname, String lname) {
        super(fname, lname);
    }

}
