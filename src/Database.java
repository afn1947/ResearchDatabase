import java.sql.*;
import java.util.ArrayList;

public class Database {

    private String uri;
    private String driver;
    private String user;
    private String pass;
    private Connection conn;

    public Database() throws ResearchException {
        uri = "jdbc:mysql://localhost/research?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        driver = "com.mysql.cj.jdbc.Driver";
        user = "root";
        pass = "student";
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
}
