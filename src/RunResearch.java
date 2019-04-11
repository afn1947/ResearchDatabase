public class RunResearch {

    public static void main(String[] args) {
        try {
            Database db = new Database();
            System.out.println(db.getData("SELECT * FROM project;"));
            db.close();
            Project project = new Project(1111);
            System.out.println(project);

        }catch (ResearchException e){

            System.out.println("Exception occurred");

        }
    }
}
