import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class that is used to interact with the database to retrieve and place data to and from the database
 *
 * @author Avery Nutting-Hartman
 */

public class Database {

    /**
     *The  connection String
     */
    private String uri;
    /**
     * The driver to user
     */
    private String driver;
    /**
     * The database username
     */
    private String user;
    /**
     * The database users password
     */
    private String pass;
    /**
     * The connection object for the database
     */
    private Connection conn;
    /**
     * A list of all the projects in the database
     * Filled when the getProjects() method is called
     */
    private ArrayList<Project> projects;

    /**
     * Creates a new database object that can be used to query the database
     * @throws ResearchException when connection fails
     */
    public Database() throws ResearchException {
        uri = "jdbc:mysql://localhost/research?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        driver = "com.mysql.cj.jdbc.Driver";
        user = "root";
        pass = "student";
        projects=null;
        this.connect();
    }


    /**
     * Get the data using the provided query without headers
     * @param query the query to run
     * @return The Arraylist of data without headers
     * @throws ResearchException when something with the query or ResultSet goes wrong
     */
    public ArrayList<ArrayList> getData(String query) throws ResearchException {
        return getData(query, false);
    }


    /**
     * Retrieves the data from the database using a query
     * @param query the query to run
     * @param headers If column headers are to be included in the data
     * @return The ArrayList of data
     * @throws ResearchException when the query or reading the ResultSet fails
     */
    public ArrayList<ArrayList> getData(String query, boolean headers) throws ResearchException {
        ArrayList<ArrayList> result = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return generateArray(rs,headers);
        } catch (Exception e) {
            throw new ResearchException(e, "GetData function failed", "Query: " + query);

        }
    }

    /**
     * Returns the data from a query with the column headers included
     * @param stmt the statement to use
     * @return the ArrayList of all rows returned from the query
     * @throws ResearchException when the query fails
     */
    private ArrayList<ArrayList> getDataArray(PreparedStatement stmt) throws ResearchException {
        ArrayList<ArrayList> result = new ArrayList<>();
        try {

            ResultSet rs = stmt.executeQuery();
            return generateArray(rs,true);
        } catch (Exception e) {
            throw new ResearchException(e, "GetData function failed", "Query: " + stmt);

        }
    }

    /**
     * Updates data in the database using a regular statement
     * @param query query to run
     * @return either the row count for SQL Data Manipulation Language (DML) statement or 0 for SQL statements that return nothing
     * @throws ResearchException when the statement execution fails
     */
    public int setData(String query) throws ResearchException {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeUpdate(query);
        } catch (Exception e) {
            throw new ResearchException(e, "SetData Function failed", "Query: " + query);
        }
    }

    /**
     * Updates data in the database using prepared statement for security
     * @param query query to run
     * @param values values to bind to the statement
     * @return either the row count for SQL Data Manipulation Language (DML) statement or 0 for SQL statements that return nothing
     * @throws ResearchException when the statement execution fails
     */
    public int setData(String query, ArrayList<String> values) throws ResearchException {
        return executeStmt(query, values);
    }

    /**
     * Creates a prepared statement and executes it
     * @param query the query to be run
     * @param values the values to bind to the statement
     * @return either the row count for SQL Data Manipulation Language (DML) statement or 0 for SQL statements that return nothing
     * @throws ResearchException when the statements execution fails
     */
    public int executeStmt(String query, ArrayList<String> values) throws ResearchException {
        PreparedStatement stmt = prepare(query, values);
        try {
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ResearchException(e, "ExecuteStmt failed", query);
        }
    }

    /**
     * Makes a prepared statement with the provided values
     * @param SQLString the string to use for the statement
     * @param values the list of values in order to be bound to the statement
     * @return the prepared statement
     * @throws ResearchException An exception occurred while creating the prepared statement
     */
    public PreparedStatement prepare(String SQLString, ArrayList<String> values) throws ResearchException {
        try {
            PreparedStatement stmt = conn.prepareStatement(SQLString);
            int i = 1;
            for (String value : values) {
                stmt.setString(i++, value);
            }
            return stmt;
        } catch (SQLException e) {
            throw new ResearchException(e, "Prepared statement function failed", SQLString);
        }
    }


    /**
     * Overloaded getData for prepared stmts
     *
     * @param query  The query to prepare
     * @param values the values to bind to the stmt
     * @return Array of values
     * @throws ResearchException If error was caused
     */
    public ArrayList<ArrayList> getData(String query, ArrayList<String> values) throws ResearchException {
        PreparedStatement stmt = prepare(query, values);
        return getDataArray(stmt);
    }

    /**
     * Private helper for get data
     * @param rs the ResultSet from the get data query
     * @param headers if the column headers are to be included
     * @return A nested array list with each row of the result set
     * @throws SQLException an exception occurred while reading the ResultSet
     */
    private ArrayList<ArrayList> generateArray(ResultSet rs,boolean headers) throws SQLException {
        ArrayList<ArrayList> result = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int numFields = metaData.getColumnCount();
        if (headers) {
            ArrayList<String> headerLine = new ArrayList<>();
            for (int i = 1; i <= numFields; i++) {
                headerLine.add(metaData.getColumnName(i));
            }
            result.add(headerLine);
        }
        while (rs.next()) {
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 1; i <= numFields; i++) {
                temp.add(rs.getString(i));
            }
            result.add(temp);
        }
        return result;
    }


    /**
     * loads the driver and connects to the database
     * @return if the database connected correctly
     * @throws ResearchException An exception occurred while creating the connection
     */
    public boolean connect() throws ResearchException {
        try {
            Class.forName(driver);
        } catch (Exception e) {
            throw new ResearchException(e, "Failed to find driver");
        }

        try {
            conn = DriverManager.getConnection(uri, user, pass);
            return true;
        } catch (Exception e) {
            throw new ResearchException(e, "Failed to open the database", "Connection string: " + uri, "User: " + user);
        }
    }

    /**
     * Closes the database connection
     * @return If the database closed successfully
     * @throws ResearchException If closing caused an exception
     */
    public boolean close() throws ResearchException {
        try {
            conn.close();
            return true;
        } catch (Exception e) {
            throw new ResearchException(e, "Close database function failed");
        }
    }

    /**
     * Returns a list of all the projects currently being worked on
     * @return list of projects
     * @throws ResearchException something went wrong
     */
    public ArrayList<Project> getProjects() throws ResearchException {
        if (projects!=null){
            return projects;
        }
        ArrayList<Project> projects = new ArrayList<>();
        String query = "SELECT project_id FROM project;";
        Database db = new Database();
        ArrayList<ArrayList> data = db.getData(query);
        for (ArrayList line:data){
            projects.add(new Project( Integer.parseInt((String) line.get(0)) ));
        }
        return projects;
    }

    /**
     * prints the search results in a nice way
     * @param query the query to be executed
     * @throws ResearchException when printing fails
     */
    public void printSearchResaults(String query) throws ResearchException {

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            int colNum = metaData.getColumnCount();
            ArrayList<String> colName = new ArrayList<>();
            ArrayList<String> colTypes = new ArrayList<>();
            int longestColName = "Column Name".length();
            int longestColType = "Column Type".length();
            int i = 1;
            //creating arrays with column names and types and setting the longest value of each for pretty printing later
            while (i<=colNum){
                colName.add(metaData.getColumnName(i));
                longestColName = (metaData.getColumnName(i).length()>longestColName) ? metaData.getColumnName(i).length() : longestColName;
                longestColType = (metaData.getColumnTypeName(i).length()>longestColType) ? metaData.getColumnTypeName(i).length() : longestColType;
                colTypes.add(metaData.getColumnTypeName(i));
                i++;
            }
            // output the column names and types
            String output = String.format("%-"+longestColName+"s","Column Name");
            output+="   ";
            output += String.format("%-"+longestColType+"s","Column Type");
            //System.out.println(output);
            int k =0;
            // Stores the width of the field needed to pretty print correctly
            LinkedList length = new LinkedList();


            while (k< colName.size()){
                length.add(colName.get(k).length());
                //System.out.println(String.format("%-"+longestColName+"s",colName.get(k))+"   "+String.format("%"+longestColType+"s",colTypes.get(k))+"(" +metaData.getPrecision(k+1)+")");
                k++;
            }

            //Pretty Print the data dynamically
            ArrayList<ArrayList> data = getData(query);
            //setting correct lengths for column width
            for (ArrayList entry: data){
                for (int j = 0;j<entry.size();j++){
                    if (entry.get(j)!=null) {
                        if (entry.get(j).toString().length() > (int) length.get(j)) {
                            length.set(j, entry.get(j).toString().length());
                        }
                    }
                }
            }

            String separator = separatorGen(length);
            System.out.println(separator);
            //creating the headers
            StringBuilder line = new StringBuilder();
            for (String name:colName){
                line.append(String.format("| %-"+length.get(colName.indexOf(name))+"s ",name));
            }
            line.append("|");
            System.out.println(line);
            System.out.println(separator);

            // printing the data in the table

            for (ArrayList entry:data){
                StringBuilder out = new StringBuilder();
                int len = 0;
                for (Object item:entry){
                    if (item==null){
                        out.append(String.format("| %-"+length.get(len)+"s ","NULL"));
                    }else {
                        out.append(String.format("| %-"+length.get(len)+"s ",item.toString()));
                    }
                    len++;
                }
                out.append("|");
                System.out.println(out);
            }
            System.out.println(separator);
        }catch (Exception e){
            e.printStackTrace();
            throw new ResearchException(e,"DescTable Query: "+query);
        }

    }
    private String separatorGen(LinkedList length){
        StringBuilder line = new StringBuilder();
        for (Object i: length){
            line.append("+");
            //Will only work in java 11 and higher
//            line.append("-".repeat((int) i + 2));
//
            for (int k=0; k<((int)i+2);k++){
                line.append("-");
            }
        }
        line.append("+");
        return line.toString();
    }

}
