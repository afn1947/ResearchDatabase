import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class RunResearch {


    public static void main(String[] args) {
        try {
            Database db = new Database();
//            System.out.println(db.getData("SELECT * FROM project;"));
            Project project = new Project(1111);
//            System.out.println(project);
            ArrayList<Project> projects = db.getProjects();
            System.out.println(projects);
            User user = new User("Avery", "Nutting-Hartman");


            db.close();
            run();
        } catch (ResearchException e) {

            System.out.println("Exception occurred");

        }
    }

    private static void run() {
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter student, faculty, or public:");
        User user = null;

        try {
            String input = inputStream.readLine();

            if (check(input) || !(input.equalsIgnoreCase("student") || input.equalsIgnoreCase("faculty") || input.equalsIgnoreCase("public"))) {
                System.out.println("Invalid input");
                System.exit(0);
            } else if (!input.equalsIgnoreCase("public")) {
                System.out.println("Enter first name:");
                String fname = inputStream.readLine();
                System.out.println("Enter Last name:");
                String lname = inputStream.readLine();
                if (input.equalsIgnoreCase("student")) {
                    user = new Student(fname, lname);
                } else if (input.equalsIgnoreCase("faculty")) {
                    user = new Faculty(fname, lname);
                }
                System.out.println("Logged in as " + user.prettyPrint());
            }

            System.out.println(user);

            while (true) {
                System.out.println("Enter Command (ex. help)");
                String line = inputStream.readLine();
                if (check(line)) {
                    System.out.println("Invalid input! Enter help for help.");
                }
                String[] cmd = line.split(" ");
                if (cmd[0].equalsIgnoreCase("help")) {
                    help();
                } else if (cmd[0].equalsIgnoreCase("search")){
                    cmd[0]="";
                    search(cmd);
                }else if (cmd[0].equalsIgnoreCase("faculty")){
                    staff(cmd[1]);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void staff(String facultyID) {

    }

    private static void search(String[] criteria){
        //System.out.println(criteria);
        LinkedHashSet<String> ids=new LinkedHashSet<>();
        try {
            Database db = new Database();
            ArrayList<Project> projects = db.getProjects();
            for (Project project:projects){
                for (String crit:criteria){
                    System.out.println(crit);
                    if (project.containsString(crit)){
                        ids.add( Integer.toString(project.getID()));
                        System.out.println(project);
                    }
                }
            }

            System.out.println(ids);

            System.out.println("Search finished");
            String idString="";
            for (String id:ids){
                idString+=id+",";
            }
            idString=idString.substring(0, idString.length() - 1);

            String query = "SELECT * from project where project_id IN ("+idString+");";
            db.printSearchResaults(query);

        } catch (ResearchException e) {
            e.printStackTrace();
        }


    }


    private static void help() {
        System.out.println("Valid Command:");
        System.out.println("search <Criteria>");
        System.out.println("contact <first last>");
        System.out.println("faculty <project ID>");
    }


    private static boolean check(String input) {
        return input.equals("");
    }

}
