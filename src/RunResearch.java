import java.util.ArrayList;

public class RunResearch {



    public static void main(String[] args) {
        try {
            Database db = new Database();
//            System.out.println(db.getData("SELECT * FROM project;"));
            Project project = new Project(1111);
//            System.out.println(project);
            ArrayList<Project> projects = db.getProjects();
            System.out.println(projects);
            User user = new User("Avery","Nutting-Hartman");


            db.close();
        }catch (ResearchException e){

            System.out.println("Exception occurred");

        }
    }

//    private static ArrayList<Project> getProjects() throws ResearchException {
//        ArrayList<Project> projects = new ArrayList<>();
//        String query = "SELECT project_id FROM project;";
//        Database db = new Database();
//        ArrayList<ArrayList> data = db.getData(query);
//        for (ArrayList line:data){
//            projects.add(new Project( Integer.parseInt((String) line.get(0)) ));
//        }
//
//        return projects;
//    }
}
