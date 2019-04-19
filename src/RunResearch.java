import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

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

    /**
     * Run loop for getting user input and accessing the database
     */
    private static void run() {

        BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));
        user = null;
        boolean flag = true;
        try {
            while (flag) {
                System.out.println("Enter student, faculty, or public:");
                String input = inputStream.readLine();

                if (check(input) || !(input.equalsIgnoreCase("student") || input.equalsIgnoreCase("faculty") || input.equalsIgnoreCase("public"))) {
                    System.out.println("Invalid input\n");
                } else if (!input.equalsIgnoreCase("public")) {
                    System.out.println("Enter first name:");
                    String fname = inputStream.readLine();
                    System.out.println("Enter Last name:");
                    String lname = inputStream.readLine();
                    try {
                        if (input.equalsIgnoreCase("student")) {
                            user = new Student(fname, lname);
                        } else if (input.equalsIgnoreCase("faculty")) {
                            user = new Faculty(fname, lname);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Login Failed!\n");
                        continue;
                    }
                    System.out.println("Logged in as " + user.prettyPrint());
                    flag = false;
                }
            }
            while (true) {
                System.out.println("Enter Command (ex. help)");
                String line = inputStream.readLine();
                if (check(line)) {
                    System.out.println("Invalid input! Enter help for help.");
                }
                String[] cmd = line.split(" ");
                if (cmd[0].equalsIgnoreCase("help")) {
                    help();
                } else if (cmd[0].equalsIgnoreCase("search")) {
                    if (cmd.length > 1) {
                        search(cmd);
                    } else {
                        System.out.println("Invalid Command!");
                    }
                } else if (cmd[0].equalsIgnoreCase("faculty")) {
                    staff(cmd[1]);
                } else if (cmd[0].equalsIgnoreCase("add")) {
                    if (user instanceof Faculty) {
                        add(inputStream);
                    } else {
                        System.out.println("PERMISSION DENIED");
                    }
                } else if (cmd[0].equalsIgnoreCase("edit")) {
                    if (user instanceof Faculty) {
                        edit(inputStream);
                    } else {
                        System.out.println("PERMISSION DENIED");
                    }
                } else if (cmd[0].equalsIgnoreCase("exit") || cmd[0].equalsIgnoreCase("quit") || cmd[0].equalsIgnoreCase("q")) {
                    System.out.println("Goodbye!");
                    System.exit(0);
                } else {
                    System.out.println("Invalid input! Enter help to view commands");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean check(String fname, String lname) {
        try {
            Database db = new Database();
//            ArrayList<ArrayList> data = db.getData("SELECT `first_name` FROM student JOIN faculty USING (")
        } catch (ResearchException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Allows faculty users to edit a project
     *
     * @param input Stream to get input from
     */
    private static void edit(BufferedReader input) {
        System.out.println("Enter ID of project to edit:");
        try {
            String id = input.readLine();
            System.out.println("Enter Project Name:");
            String name = input.readLine();
            System.out.println("Enter Start date (yyyy-mm-dd):");
            String start = input.readLine();
            System.out.println("Enter end date (yyyy-mm-dd):");
            String end = input.readLine();
            System.out.println("Enter Field of Research:");
            String field = input.readLine();
            System.out.println("Enter a description:");
            String description = input.readLine();
            Project project = new Project(Integer.parseInt(id), name, start, end, field, description);
            Database db = new Database();
            db.setData("DELETE FROM `project` WHERE `project_id` = '" + id + "';");
            db.close();
            project.put();
            System.out.println("Updated!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResearchException e) {
            System.out.println("ERROR");
        }
    }

    /**
     * Allows faculty user to add a project
     *
     * @param input Stream to get retrieve user input
     */
    private static void add(BufferedReader input) {
        String id;
        System.out.println("No blank values allowed");
        try {
            while (true) {
                System.out.println("Enter UNIQUE projectID:");
                id = input.readLine();
                if (checkID(id)) {
                    break;
                }
            }
            System.out.println("Enter Project Name:");
            String name = input.readLine();
            System.out.println("Enter Start date (yyyy-mm-dd):");
            String start = input.readLine();
            System.out.println("Enter end date (yyyy-mm-dd):");
            String end = input.readLine();
            System.out.println("Enter Field of Research:");
            String field = input.readLine();
            System.out.println("Enter a description:");
            String description = input.readLine();

            Project project = new Project(Integer.parseInt(id), name, start, end, field, description);
            project.put();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResearchException e) {
            //squash
        }
    }

    /**
     * Checks if the id exists in database
     *
     * @param id index to check
     * @return if the id exists
     * @throws ResearchException something went wrong
     */
    private static boolean checkID(String id) throws ResearchException {
        Database db = new Database();
        ArrayList<ArrayList> data = db.getData("SELECT `project_ID` from project where `project_ID`='" + id + "';");
        db.close();
        return data.isEmpty();


    }

    /**
     * gets the staff for a project given the project ID
     *
     * @param projectID The project ID
     */
    private static void staff(String projectID) {
        String query = "SELECT `first_name` AS 'First Name' ,`last_name` AS 'Last Name' ,`department` AS 'Department' ,`email` AS 'EMAIL' FROM faculty JOIN faculty_project USING (faculty_ID) WHERE project_ID = '" + projectID + "';";
        try {
            Database db = new Database();
            System.out.println("\n");
            db.printSearchResaults(query);
            db.close();
        } catch (ResearchException e) {
            e.printStackTrace();
        }

    }

    /**
     * searches all projects looking for matches
     *
     * @param criteria what the project must match
     */
    private static void search(String[] criteria) {
        //System.out.println(criteria);
        LinkedHashSet<String> ids = new LinkedHashSet<>();
        List<String> searchWords = Arrays.asList(criteria);

        try {
            Database db = new Database();
            ArrayList<Project> projects = db.getProjects();
            for (Project project : projects) {
                for (String crit : criteria) {
                    if (!(searchWords.indexOf(crit) == 0)) {
                        if (project.containsString(crit)) {
                            ids.add(Integer.toString(project.getID()));
                        }
                    }
                }
            }
            StringBuilder idString = new StringBuilder();
            for (String id : ids) {
                idString.append(id).append(",");
            }
//            System.out.println(idString);
            try {
                idString = new StringBuilder(idString.substring(0, idString.length() - 1));
                String query = "SELECT * from project where project_id IN (" + idString + ");";
                System.out.println("\nProjects matching your search criteria:");
                db.printSearchResaults(query);
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("No projects found that match the search criteria!\n");
            } finally {
                db.close();
            }
        } catch (ResearchException e) {
            e.printStackTrace();
        }
    }

    /**
     * shows the user the valid commands
     */
    private static void help() {
        System.out.println("Valid Commands:");
        System.out.println("search <Criteria>");
        //System.out.println("contact <first last>");
        System.out.println("faculty <project ID>");
        if (user instanceof Faculty) {
            System.out.println("add project");
            System.out.println("edit <Project ID>");
        }
        System.out.println("quit");
    }

    /**
     * checks if a string is empty
     *
     * @param input string to check
     * @return if string is ""
     */
    private static boolean check(String input) {
        return input.equals("");
    }
}
