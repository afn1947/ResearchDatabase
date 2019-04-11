import java.util.ArrayList;
import java.util.Date;

public class Project {

    private int ID;
    private String name;
    private String startDate;
    private String endDate;
    private String field;
    private String description;

    public Project(int ID) {
        this.ID = ID;
        try{
            fetch();
        }catch (ResearchException e){
            //Squash
        }
    }

    public Project(int ID, String name, String startDate, String endDate, String field, String description) {
        this.ID = ID;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.field = field;
        this.description = description;
    }

    public boolean contains(String criteria){
        return this.name.contains(criteria) || this.field.contains(criteria)  || this.description.contains(criteria);
    }


    private boolean fetch() throws ResearchException {
        Database db = new Database();

        String query = "SELECT * from project WHERE project_ID = '"+this.ID+"';";
        ArrayList result = db.getData(query,false);
        if (!(result.isEmpty())) {
            ArrayList row = (ArrayList) result.get(0);
//            System.out.println(row);
            this.name = (String) row.get(1);
            this.startDate = (String) row.get(2);
            this.endDate = (String) row.get(3);
            this.field= (String) row.get(4);
            this.description= (String) row.get(5);
            db.close();
            return true;
        } else {
            return false;
        }
    }

    public void put() throws ResearchException{
        Database db = new Database();

        String query = "INSERT INTO project VALUES('"+this.ID+"', '"+this.name+"', '"+this.startDate+"', '"+this.endDate+"', '"+this.field+"', '"+this.description+":');";
        db.setData(query);
        db.close();
    }

    public boolean containsString(String criteria){
        System.out.println(criteria);
        return name.contains(criteria) || startDate.contains(criteria) || endDate.contains(criteria) || field.contains(criteria)  || description.contains(criteria);
    }

    @Override
    public String toString() {
        return "Project{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", field='" + field + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public int getID(){
        return this.ID;
    }
}
