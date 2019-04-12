import javax.xml.crypto.Data;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class RunResearch {

    private static User user;

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
        user= null;

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

//            System.out.println(user);
//
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
                }else if (cmd[0].equalsIgnoreCase("add")){
                    add(inputStream);


                }else if (cmd[0].equalsIgnoreCase("edit")){

                }
                else if (cmd[0].equalsIgnoreCase("exit") || cmd[0].equalsIgnoreCase("quit")){
                    System.exit(0);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void add(BufferedReader input) {
        String id;
        System.out.println("No blank values allowed");
        try {
            while (true) {
                System.out.println("Enter UNIQUE projectID:");
                 id= input.readLine();
                if (checkID(id)){
                    break;
                }
            }
            System.out.println("Enter Project Name:");
            String name=input.readLine();
            System.out.println("Enter Start date (yyyy-mm-dd):");
            String start=input.readLine();
            System.out.println("Enter end date (yyyy-mm-dd):");
            String end=input.readLine();
            System.out.println("Enter Field of Research:");
            String field = input.readLine();
            System.out.println("Enter a description:");
            String description = input.readLine();

            Project project = new Project(Integer.parseInt(id),name,start,end,field,description);
            project.put();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResearchException e) {
            //squash
        }
    }

    private static boolean checkID(String id) throws ResearchException {
        Database db = new Database();
        ArrayList<ArrayList> data = db.getData("SELECT `project_ID` from project where `project_ID`='"+id+"';");
        db.close();
        return data.isEmpty();


    }

    private static void staff(String facultyID) {

        String query = "SELECT `first_name`,`last_name`,`department`,`email` FROM faculty JOIN faculty_project USING (faculty_ID) WHERE project_ID = '"+facultyID+"';";
        try {
            Database db = new Database();
            System.out.println("\n");
            db.printSearchResaults(query);
            db.close();
        } catch (ResearchException e) {
            e.printStackTrace();
        }

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
            System.out.println("\nProjects matching your search criteria:");
            db.printSearchResaults(query);
            db.close();
        } catch (ResearchException e) {
            e.printStackTrace();
        }


    }


    private static void help() {
        System.out.println("Valid Command:");
        System.out.println("search <Criteria>");
        //System.out.println("contact <first last>");
        System.out.println("faculty <project ID>");
        if (user instanceof Faculty){
            System.out.println("add project");
            System.out.println("edit <Project ID>");
        }
        System.out.println("quit");
    }


    private static boolean check(String input) {
        return input.equals("");
    }
}
