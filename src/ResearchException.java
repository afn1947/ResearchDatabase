import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResearchException extends Exception {
    private final String[] values;
    private Exception exception;
    private String message = "Operation Failed";

    @Override
    public String getMessage() {
        return message;
    }

    public ResearchException(Exception e) {
        super(e.getMessage());
        this.exception = e;
        values = new String[0];
        writeLog();
    }

    public ResearchException(Exception e, String... values) {
        super(values[0]);
        this.exception = e;
        this.values = values;
        writeLog();
    }

    /**
     * Things to log
     * stack trace
     * exceptions maybe
     * Time stamp
     * source method or class
     * message
     * sql stmt that failed
     * # of params if applicable
     * user
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
