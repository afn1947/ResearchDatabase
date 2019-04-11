import java.util.ArrayList;
import java.util.Date;

public class Project {

    private int ID;
    private String name;
    private Date startDate;
    private Date endDate;
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


    private boolean fetch() throws ResearchException {
        Database db = new Database();
        ArrayList id = new  ArrayList<>();
        id.add(this.ID);
        String query = "SELECT * from project WHERE project_ID = ?";
        ArrayList result = db.getData(query,id);
        if (!(result.isEmpty())) {
            ArrayList row = (ArrayList) result.get(1);
            this.name = (String) row.get(0);
            this.startDate = (Date) row.get(1);
            this.endDate = (Date) row.get(2);
            this.field= (String) row.get(3);
            this.facultyEmps= (String) row.get(4);
            this.studentEmps= (String) row.get(5);
            this.description= (String) row.get(6);
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
