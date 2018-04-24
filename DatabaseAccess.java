import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by rosa.charles on 1/23/2017.
 */
public class DatabaseAccess {

    public DatabaseAccess() {

    }

    public static void main(String[] args) throws Exception {
        DatabaseAccess myDBAccess = new DatabaseAccess();
        String sicName_string = "XLA";
    }

    public static Connection getConnection_PRD_WHSEVIEW() throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
      /*
      java.util.Properties prop = new java.util.Properties();
      prop.put("user", "TS3_USER");
      prop.put("password", "TS3");
      Driver d = (Driver)Class.forName
       ("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
      Connection c = DriverManager.getConnection("jdbc:odbc:OPS_RESEARCH",
              prop);
      */

        Class.forName("org.netezza.Driver").newInstance();
        Connection c = DriverManager.getConnection("jdbc:netezza://npsdwh.con-way.com/PRD_WHSEVIEW", "rosa.charles", "csarRosa0818");
        c.setAutoCommit(false);
        return c;

    }

    private String getDateString_monthDayYear(String mySeparator, java.util.Date myDate) {
        StringBuffer myBuffer = new StringBuffer();
        myBuffer.append(myDate.getMonth()+1+mySeparator);
        myBuffer.append(myDate.getDate()+mySeparator);
        myBuffer.append(myDate.getYear()+1900);

        return myBuffer.toString();
    }

    public java.util.Date getDate(java.util.Date startDate, int dateIncrement) throws Exception {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar date = (Calendar) startCal.clone();
        date.add(Calendar.DAY_OF_MONTH, dateIncrement);

        java.util.Date myDateToReturn = date.getTime();

        return myDateToReturn;
    }


}
