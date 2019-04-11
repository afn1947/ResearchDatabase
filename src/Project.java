import java.util.ArrayList;
import java.util.Date;

public class Project {

    private int ID;
    private String name;
    private String startDate;
    private String endDate;
    private String field;
    private String facultyEmps;
    private String studentEmps;
    private String description;

    public Project(int ID) {
        this.ID = ID;
        try{
            fetch();
        }catch (ResearchException e){
            //Squash
        }
    }

    public boolean contains(String criteria){
        return this.name.contains(criteria) || this.field.contains(criteria) || this.facultyEmps.contains(criteria) || this.studentEmps.contains(criteria) || this.description.contains(criteria);
    }


    private boolean fetch() throws ResearchException {
        Database db = new Database();

        String query = "SELECT * from project WHERE project_ID = '"+this.ID+"';";
        ArrayList result = db.getData(query,false);
        if (!(result.isEmpty())) {
            ArrayList row = (ArrayList) result.get(0);
            this.name = (String) row.get(1);
            this.startDate = (String) row.get(2);
            this.endDate = (String) row.get(3);
            this.field= (String) row.get(4);
            this.facultyEmps= (String) row.get(5);
            this.studentEmps= (String) row.get(6);
            this.description= (String) row.get(7);
            db.close();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Project{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", field='" + field + '\'' +
                ", facultyEmps='" + facultyEmps + '\'' +
                ", studentEmps='" + studentEmps + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
