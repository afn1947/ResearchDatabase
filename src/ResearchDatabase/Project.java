package ResearchDatabase;

import java.util.ArrayList;
/**
 * Represents a project entry in the database
 *
 * @author Avery Nutting-Hartman
 */
public class Project {

    /**
     * The Projects ID number
     */
    private int ID;

    /**
     * The project name
     */
    private String name;

    /**
     * project start date
     */
    private String startDate;

    /**
     * The project end date
     */
    private String endDate;

    /**
     * The field the project is in
     */
    private String field;

    /**
     * A description of the project
     */
    private String description;

    /**
     * Creates a new project object
     * @param ID The id of the project
     */
    public Project(int ID) {
        this.ID = ID;
        try {
            fetch();
        } catch (ResearchException e) {
            //Squash
        }
    }

    /**
     * creates a projects with all the fields passed in
     * @param ID the ID
     * @param name The name
     * @param startDate the start date
     * @param endDate the end date
     * @param field the field of the project
     * @param description the description for the project
     */
    public Project(int ID, String name, String startDate, String endDate, String field, String description) {
        this.ID = ID;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.field = field;
        this.description = description;
    }


    /**
     * Gets and set the empty fields based on whats in the database using the project ID
     * @throws ResearchException when the query fails
     */
    private void fetch() throws ResearchException {
        Database db = new Database();

        String query = "SELECT * from project WHERE project_ID = '" + this.ID + "';";
        ArrayList result = db.getData(query, false);
        if (!(result.isEmpty())) {
            ArrayList row = (ArrayList) result.get(0);
            this.name = (String) row.get(1);
            this.startDate = (String) row.get(2);
            this.endDate = (String) row.get(3);
            this.field = (String) row.get(4);
            this.description = (String) row.get(5);
            db.close();
            name = name == null ? " " : name;
            startDate = startDate == null ? " " : startDate;
            endDate = endDate == null ? " " : endDate;
            field = field == null ? " " : field;
            description = description == null ? " " : description;
        }
    }

    /**
     * Puts the project into the database
     * @throws ResearchException when adding project to database fails
     */
    public void put() throws ResearchException {
        Database db = new Database();
        String query = "INSERT INTO project VALUES('" + this.ID + "', '" + this.name + "', '" + this.startDate + "', '" + this.endDate + "', '" + this.field + "', '" + this.description + ":');";
        query = query.replace(":", "");
        query = query.replace("''", "NULL");
        db.setData(query);
        db.close();
    }

    /**
     * Check if any of the project attributes contain a string
     * @param criteria The String to search for
     * @return if the project contains the string
     */
    public boolean containsString(String criteria) {
        return Integer.toString(ID).contains(criteria) || name.contains(criteria) || startDate.contains(criteria) || endDate.contains(criteria) || field.contains(criteria) || description.contains(criteria);
    }

    @Override
    public String toString() {
        return "ResearchDatabase.Project{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", field='" + field + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    /**
     * returns the ID of the project
     * @return the project ID
     */
    public int getID() {
        return this.ID;
    }
}
