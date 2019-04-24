import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Exception that is used to hide the exception from the user and log it to a hidden log file
 *
 * @author Avery Nutting-Hartman
 */

public class ResearchException extends Exception {
    /**
     * Additional error messages
     */
    private final String[] values;
    /**
     * The exception that occurred
     */
    private Exception exception;
    /**
     * The default error message
     */
    private String message = "Operation Failed";

    /**
     * Returns the default message
     * @return the default message
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * creates new ResearchException object
     * @param e The exception the occurred
     */
    public ResearchException(Exception e) {
        super(e.getMessage());
        this.exception = e;
        values = new String[0];
        writeLog();
    }

    /**
     * Create a Research exception object
     * @param e the exception that occurred
     * @param values extra values about the error
     */
    public ResearchException(Exception e, String... values) {
        super(values[0]);
        this.exception = e;
        this.values = values;
        writeLog();
    }

    /**
     * Writes all error information to an error log to be reviewed by DBA
     */
    public void writeLog() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("src/log.txt", true)));
            writer.println(timestamp.toString());
            for (StackTraceElement line : exception.getStackTrace()) {
                writer.println(line.toString());
            }
            for (String line : values) {
                writer.println(line);
            }
            writer.println("");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Log Writer crashed!");
            System.exit(-1);
        }
    }
}
