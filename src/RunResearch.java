import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

            if (check(input)) {
                System.out.println("Invalid input");
                System.exit(0);
            } else if ((!input.equalsIgnoreCase("public"))) {
                System.out.println("Enter first name:");
                String fname = inputStream.readLine();
                System.out.println("Enter Last name:");
                String lname = inputStream.readLine();
                if (input.equalsIgnoreCase("student")) {
                    user = new Student(fname, lname);
                } else if (input.equalsIgnoreCase("faculty")) {
                    user = new Faculty(fname, lname);
                }
            }

            System.out.println(user);

            String line = inputStream.readLine();
            if (!line.equals(null))


                while (true) {
                    System.out.println("Enter Command or help:");


                }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean check(String input) {
        return input.equals("");
    }

}
