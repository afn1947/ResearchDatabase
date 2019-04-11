public class User {

    private int ID;
    protected String fname;
    protected String lname;

    public User(String fname, String lname) {
        this.fname = fname;
        this.lname = lname;

    }


    public void search(String criteria) throws ResearchException {
        for(Project project:new Database().getProjects()){

            if (project.contains(criteria)){
                System.out.println(project);
            }
        }
    }
}
