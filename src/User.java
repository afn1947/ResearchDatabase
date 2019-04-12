public class User {

    protected int ID;
    protected String fname;
    protected String lname;


    public User(String fname, String lname) {
        this.fname = fname;
        this.lname = lname;

    }

    /**
     * Allows users to search for projects
     * @param criteria search criteria
     * @throws ResearchException something went wrong
     */
    public void search(String criteria) throws ResearchException {
        for(Project project:new Database().getProjects()){

            if (project.contains(criteria)){
                System.out.println(project);
            }
        }
    }

    /**
     * Prints the users in a pretty way
     * @return string representation of user
     */
    public String prettyPrint(){
        return fname+" "+lname+" "+"ID#: "+ID;
    }
}
