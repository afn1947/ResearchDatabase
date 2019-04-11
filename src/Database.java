import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Database {

    private String uri;
    private String driver;
    private String user;
    private String pass;
    private Connection conn;
    private ArrayList<Project> projects;

    public Database() throws ResearchException {
        uri = "jdbc:mysql://localhost/research?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        driver = "com.mysql.cj.jdbc.Driver";
        user = "root";
        pass = "student";
        projects=null;
        this.connect();

    }


    /**
     * Retrieves data using the provided query
     * Returns list without headers
     */
    public ArrayList<ArrayList> getData(String query) throws ResearchException {
        return getData(query, false);
    }


    /**
     * Retrieves data using the provided query
     * Headers: if column headers are to be included in the list
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

    private ArrayList<ArrayList> getDataArray(PreparedStatement stmt, boolean headers) throws ResearchException {
        ArrayList<ArrayList> result = new ArrayList<>();
        try {

            ResultSet rs = stmt.executeQuery();
            return generateArray(rs,headers);
        } catch (Exception e) {
            throw new ResearchException(e, "GetData function failed", "Query: " + stmt);

        }
    }

    /**
     * Sets data using the query string provided
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
     * Overloaded setdata that uses prepared stmts
     */
    public int setData(String query, ArrayList<String> values) throws ResearchException {
        return executeStmt(query, values);
    }

    /**
     * Creates prepared stmt and calls execute update on it
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
     * Takes a string and array and creates a prepared stmt
     *
     * @return the prepared stmt
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
        return getDataArray(stmt, true);
    }


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
     * Loads the driver and connects to the database
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
     * Closes the connection to the database
     */
    public boolean close() throws ResearchException {
        try {
            conn.close();
            return true;
        } catch (Exception e) {
            throw new ResearchException(e, "Close database function failed");
        }
    }
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
            System.out.println(output);
            int k =0;
            // Stores the width of the field needed to pretty print correctly
            LinkedList length = new LinkedList();


            while (k< colName.size()){
                length.add(colName.get(k).length());
                System.out.println(String.format("%-"+longestColName+"s",colName.get(k))+"   "+String.format("%"+longestColType+"s",colTypes.get(k))+"(" +metaData.getPrecision(k+1)+")");
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
            System.out.println("\nProjects matching your search criteria:");
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
            line.append("-".repeat((int) i + 2));
        }
        line.append("+");
        return line.toString();
    }

}
