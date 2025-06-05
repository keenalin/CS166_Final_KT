/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
// import Validate;


/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class AirlineManagement {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of AirlineManagement
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public AirlineManagement(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end AirlineManagement

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
		 if(outputHeader){
			for(int i = 1; i <= numCol; i++){
			System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			outputHeader = false;
		 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
		for (int i=1; i<=numCol; ++i)
			record.add(rs.getString (i));
        result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   public static void pause() {
      System.out.println("Press Enter to continue...");
      try {
         in.readLine();
      } catch (Exception ex) {
         // Ignore
      }
   }

   public static String getDate(String prompt) {
      String currDate = "";
      while (true) {
         System.out.print(prompt);
         try {
         currDate = in.readLine().trim();
         if (currDate.isEmpty()) {
            System.out.println("Date cannot be empty.");
            continue;
         }
         // Simple regex for YYYY-MM-DD format
         if (!currDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            continue;
         }
         // Try parsing to check if it's a valid date
         java.time.LocalDate.parse(currDate);
         break;
         } catch (Exception e) {
         System.out.println("Invalid date. Please try again.");
         }
      }
      return currDate;
   }

   public static String getString(String prompt) {
      String input = "";
      while (true) {
         System.out.print(prompt);
         try {
            input = in.readLine().trim();
            if (input.isEmpty()) {
               System.out.println("Input cannot be empty.");
               continue;
            }
            break;
         } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
         }
      }
      return input;
   }

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            AirlineManagement.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      AirlineManagement esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the AirlineManagement object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new AirlineManagement (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in (STUB, FOLLOW ME)");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");

                //**the following functionalities should only be able to be used by Management**
                System.out.println("1. Get a Flight's schedule for the week");
                System.out.println("2. Check Flight seats");
                System.out.println("3. Flight status (departure/arrival time)");
                System.out.println("4. Flights on a specific date");
                System.out.println("5. Passenger lists by flight");
                System.out.println("6. Reservation traveler details");
                System.out.println("7. Plane details");
                System.out.println("8. Technician repair history");
                System.out.println("9. Plane repairs in date range");
                System.out.println("10. Flight stats over date range");

                //**the following functionalities should only be able to be used by customers**
                System.out.println("10. Search Flights");
                System.out.println(".........................");
                System.out.println(".........................");

                //**the following functionalities should ony be able to be used by Pilots**
                System.out.println("15. Maintenace Request");
                System.out.println(".........................");
                System.out.println(".........................");

               //**the following functionalities should ony be able to be used by Technicians**
                System.out.println(".........................");
                System.out.println(".........................");

                System.out.println("20. Log out");
                switch (readChoice()){
                   case 1: feature1(esql); break;
                   case 2: feature2(esql); break;
                   case 3: feature3(esql); break;
                   case 4: feature4(esql); break;
                   case 5: feature5(esql); break;
                   case 6: feature6(esql); break;
                   case 7: feature7(esql); break;
                   case 8: feature8(esql); break;
                   case 9: feature9(esql); break;
                   case 10: feature10(esql); break;
                   case 11: feature11(esql); break;
                   case 12: feature12(esql); break;
                   case 13: feature13(esql); break;
                   case 14: feature14(esql); break;
                   case 15: feature15(esql); break;
                   case 16: feature16(esql); break;
                   case 17: feature17(esql); break;
                   case 18: feature18(esql); break;
                   case 19: feature19(esql); break;



                   case 20: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user
    **/
   public static void CreateUser(AirlineManagement esql){
   }//end CreateUser


   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(AirlineManagement esql){
      // return null;
      return "STUB_ALLOW";
   }//end

// Rest of the functions definition go in here

   public static void feature1(AirlineManagement esql) { // Get Flight's schedule for the week
      try {
         // Get Flight #
         String flightNum = getString("Input Flight number (e.g., F100): ");

         // Get today's date
         String currDate = getDate("Input today's date (YYYY-MM-DD): ");

         String query = String.format(
            "SELECT * FROM FlightInstance\n" +
            "WHERE FlightNumber = '%s' AND flightdate >= '%s' AND flightdate < DATE '%s' + INTERVAL '7 days'\n" +
            "ORDER BY flightdate;\n", flightNum, currDate, currDate
         );
         int rowCount = esql.executeQueryAndPrintResult(query);
         if (rowCount == 0) {
            System.out.println("No schedule found for this flight in the upcoming week.");
         }
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
      
      pause();
   }

   // Given a flight and a date, get (1) the number of seats still available and (2) number of seats sold
   public static void feature2(AirlineManagement esql) {
      try {
         // Get Flight #
         String flightNum = getString("Input Flight number (e.g., F100): ");

         // Get date
         String currDate = getDate("Input date of interest (YYYY-MM-DD): ");

         String query = String.format(
            "SELECT SeatsTotal, SeatsSold\n" +
            "FROM FlightInstance\n" +
            "WHERE FlightNumber = '%s' AND flightdate = '%s'\n" +
            "ORDER BY flightdate;\n", flightNum, currDate
         );
         int rowCount = esql.executeQueryAndPrintResult(query);
         if (rowCount == 0) {
            System.out.println("No schedule found for this flight in the upcoming week.");
         }
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
      
      pause();
   }

   // Given a flight and date, find whether (1) the flight departed on time, and (2) arrived on time
   public static void feature3(AirlineManagement esql) {
try {
         // Get Flight #
         String flightNum = getString("Input Flight number (e.g., F100): ");

         // Get date
         String currDate = getDate("Input date of interest (YYYY-MM-DD): ");

         String query = String.format(
            "SELECT DepartedOnTime, ArrivedOnTime\n" +
            "FROM FlightInstance\n" +
            "WHERE FlightNumber = '%s' AND flightdate = '%s'\n" +
            "ORDER BY flightdate;\n", flightNum, currDate
         );
         int rowCount = esql.executeQueryAndPrintResult(query);
         if (rowCount == 0) {
            System.out.println("No schedule found for this flight in the upcoming week.");
         }
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
      
      pause();
   }

   // Given a date, get all flights scheduled on that day
   public static void feature4(AirlineManagement esql) {
      try {
         // Get date
         String currDate = getDate("Input date of interest (YYYY-MM-DD): ");

         String query = String.format(
            "SELECT FlightInstanceID, FlightNumber\n" +
            "FROM FlightInstance\n" +
            "WHERE flightdate = '%s'\n" +
            "ORDER BY flightdate;\n", currDate
         );
         int rowCount = esql.executeQueryAndPrintResult(query);
         if (rowCount == 0) {
            System.out.println("No schedule found for this flight in the upcoming week.");
         }
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
      
      pause();
   }

   // Given a flight and date, get a list of passengers who (1) made reservations, (2) are on the
   // waiting list, (3) actually flew on the flight (for flights already completed)
   public static void feature5(AirlineManagement esql) {
      try {
         // Get Flight #
         String flightNum = getString("Input Flight number (e.g., F100): ");

         // Get date
         String currDate = getDate("Input date of interest (YYYY-MM-DD): ");

          String query = String.format(
          "SELECT C.CustomerID, C.FirstName, C.LastName, R.Status\n" +
          "FROM Reservation R\n" +
          "JOIN Customer C ON R.CustomerID = C.CustomerID\n" +
          "JOIN FlightInstance FI ON R.FlightInstanceID = FI.FlightInstanceID\n" +
          "WHERE FI.FlightNumber = '%s' AND FI.FlightDate = '%s'\n" +
          "AND (R.Status = 'waitlist' OR R.Status = 'flown')\n" +
          "ORDER BY R.Status;", flightNum, currDate
          );
         int rowCount = esql.executeQueryAndPrintResult(query);
         if (rowCount == 0) {
            System.out.println("No schedule found for this flight in the upcoming week.");
         }
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
      
      pause();
   }

   // Given a reservation number, retrieve information about the travelers under that number
   // • First & Last Name, Gender, Date of birth, Address, Phone number, Zip Code
   public static void feature6(AirlineManagement esql) {
      try {
          // Get Reservation #
          String resNum = getString("Input Reservation number (e.g., R0001): ");

          String query = String.format(
          "SELECT C.FirstName, C.LastName, C.Gender, C.DOB, C.Address, C.Phone, C.Zip FROM Customer C\n" +
          "JOIN Reservation R ON R.CustomerID = C.CustomerID\n" +
          "WHERE R.ReservationID = '%s'\n" +
          "ORDER BY C.CustomerID;", resNum
          );
         int rowCount = esql.executeQueryAndPrintResult(query);
         if (rowCount == 0) {
            System.out.println("No schedule found for this flight in the upcoming week.");
         }
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
      
      pause();
   }

   // Given a plane number, get its make, model, age, and last repair date
   public static void feature7(AirlineManagement esql) {
      try {
          // Get Plane ID
          String planeID = getString("Input Plane Number (e.g., PL001): ");

          String query = String.format(
          "SELECT *\n" +
          "FROM Plane\n" +
          "WHERE PlaneID = '%s'\n", planeID
          );
         int rowCount = esql.executeQueryAndPrintResult(query);
         if (rowCount == 0) {
            System.out.println("No schedule found for this flight in the upcoming week.");
         }
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
      
      pause();
   }

   // Given a maintenance technician ID, list all repairs made by that person
   public static void feature8(AirlineManagement esql) {
      try {
          // Get Technician ID
          String technicianID = getString("Input Technician ID (e.g., T001): ");

          String query = String.format(
          "SELECT T.TechnicianID, T.Name, R.RepairID, R.PlaneID, R.RepairCode, R.RepairDate\n" +
          "FROM Technician T\n" +
          "JOIN Repair R ON R.TechnicianID = T.TechnicianID\n" +
          "WHERE T.TechnicianID = '%s'\n" +
          "GROUP BY T.TechnicianID, R.RepairID\n", technicianID
          );
         int rowCount = esql.executeQueryAndPrintResult(query);
         if (rowCount == 0) {
            System.out.println("No schedule found for this flight in the upcoming week.");
         }
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
      
      pause();
   }

   // Given a plane ID and a date range, list all the dates and the codes for repairs performed
   public static void feature9(AirlineManagement esql) {
      try {
          // Get Plane ID
          String planeID = getString("Input Plane ID (e.g., PL001): ");

          System.out.println("Select date range of interest.");
          // Get starting date range
          String startDate = getDate("Input starting date (YYYY-MM-DD): ");

          // Get ending date range
          String endDate = getDate("Input ending date (YYYY-MM-DD): ");

          String query = String.format(
          "SELECT R.RepairDate, R.RepairCode\n" +
          "FROM Repair R\n" +
          "WHERE R.PlaneID = '%s' AND R.RepairDate >= '%s' AND R.RepairDate <= '%s'\n" +
          "ORDER BY R.RepairDate\n", planeID, startDate, endDate
          );
         int rowCount = esql.executeQueryAndPrintResult(query);
         if (rowCount == 0) {
            System.out.println("No repairs found for specified plane during the specified dates.");
         }
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
      
      pause();
   }

   // Given a flight and a range of date (start date, end date), show the statistics of the flight:
   // number of days the flight departed and arrived, number of sold and unsold tickets
   public static void feature10(AirlineManagement esql) {
      try {
          // Get Flight ID
          String flightID = getString("Input Flight ID (e.g., F100): ");

          System.out.println("Select date range of interest.");
          // Get starting date range
          String startDate = getDate("Input starting date (YYYY-MM-DD): ");

          // Get ending date range
          String endDate = getDate("Input ending date (YYYY-MM-DD): ");

          String query = String.format(
          "SELECT COUNT(FlightNumber) AS DaysFlown, SUM(SeatsSold) AS TotalSeatsSold, SUM(SeatsTotal-SeatsSold) AS TotalSeatsUnsold\n" +
          "FROM FlightInstance\n" +
          "WHERE FlightNumber = '%s' AND FlightDate >= '%s' AND FlightDate <= '%s'\n" +
          "GROUP BY FlightNumber\n", flightID, startDate, endDate
          );
         int rowCount = esql.executeQueryAndPrintResult(query);
         if (rowCount == 0) {
            System.out.println("No repairs found for specified plane during the specified dates.");
         }
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
      
      pause();
   }
   
public static void feature11(AirlineManagement esql) {//11. Search Flights
      try {
      System.out.print("Enter departure city: ");
      String depCity = in.readLine();

      System.out.print("Enter arrival city: ");
      String arrCity = in.readLine();

      System.out.print("Enter flight date (YYYY-MM-DD): ");
      String flightDate = in.readLine();

      String query =
         "SELECT F.FlightNumber, F.DepartureCity, F.ArrivalCity, FI.FlightDate, " +
         "S.DepartureTime, S.ArrivalTime, FI.TicketCost, FI.NumOfStops, " +
         "(FI.SeatsTotal - FI.SeatsSold) AS SeatsAvailable " +
         "FROM Flight F " +
         "JOIN FlightInstance FI ON F.FlightNumber = FI.FlightNumber " +
         "JOIN Schedule S ON F.FlightNumber = S.FlightNumber " +
         "WHERE F.DepartureCity = '" + depCity + "' " +
         "AND F.ArrivalCity = '" + arrCity + "' " +
         "AND FI.FlightDate = '" + flightDate + "';";

      int rowCount = esql.executeQuery(query);
      System.out.println("Total row(s): " + rowCount);
   } catch (Exception e) {
      System.err.println("Error in feature11: " + e.getMessage());
   }
   }

   public static void feature12(AirlineManagement esql) { //12. Make a Reservation
      try {
      System.out.print("Enter your CustomerID: ");
      String customerId = in.readLine();

      System.out.print("Enter FlightInstanceID: ");
      String flightInstanceId = in.readLine();

      // check seat availability
      String checkSeats = "SELECT SeatsSold, SeatsTotal FROM FlightInstance WHERE FlightInstanceID = '" + flightInstanceId + "';";
      List<List<String>> seatInfo = esql.executeQueryAndReturnResult(checkSeats);

      if (seatInfo.isEmpty()) {
         System.out.println("Invalid FlightInstanceID.");
         return;
      }

      int seatsSold = Integer.parseInt(seatInfo.get(0).get(0));
      int seatsTotal = Integer.parseInt(seatInfo.get(0).get(1));

      String status = (seatsSold < seatsTotal) ? "Reserved" : "Waitlist";

      // insert reservation
      String insertReservation = String.format(
         "INSERT INTO Reservation (CustomerID, FlightInstanceID, Status) VALUES ('%s', '%s', '%s');",
         customerId, flightInstanceId, status);
      esql.executeUpdate(insertReservation);

      // confirm reservation
      String confirmQuery = String.format(
         "SELECT ReservationID, Status FROM Reservation WHERE CustomerID = '%s' AND FlightInstanceID = '%s' ORDER BY ReservationID DESC LIMIT 1;",
         customerId, flightInstanceId);
      esql.executeQuery(confirmQuery); 

      System.out.println("Reservation successfully added with status: " + status);

   } catch (Exception e) {
      System.err.println("Error in feature12: " + e.getMessage());
   }
   }

   public static void feature13(AirlineManagement esql) { //13.Cancel Reservation
      try {
        System.out.print("Enter your ReservationID to cancel: ");
        String input = in.readLine();
        int resID = Integer.parseInt(input);

        String query = "DELETE FROM Reservation WHERE ReservationID = " + resID + ";";
        int rowCount = esql.executeQuery(query);

        if (rowCount > 0) {
            System.out.println("Reservation " + resID + " cancelled.");
        } else {
            System.out.println("No reservation found with ID: " + resID);
        }
    } catch (Exception e) {
        System.err.println("Error in feature13: " + e.getMessage());
    }
   }

   public static void feature14(AirlineManagement esql) {//14. Check Reservation Status
      try {
      System.out.print("Enter your Customer ID: ");
      String customerId = in.readLine();

      String query = 
         "SELECT ReservationID, FlightInstanceID, Status " +
         "FROM Reservation " +
         "WHERE CustomerID = '" + customerId + "';";

      int rowCount = esql.executeQuery(query);
      System.out.println("Total row(s): " + rowCount);
   } catch (Exception e) {
      System.err.println("Error in feature14: " + e.getMessage());
   }
   }

   public static void feature15(AirlineManagement esql) {//15. View My Upcoming Flight
      try {
        System.out.print("Enter your Customer ID: ");
        String customerId = in.readLine();

        String query =
            "SELECT R.ReservationID, F.FlightNumber, FI.FlightDate, " +
            "F.DepartureCity, F.ArrivalCity, S.DepartureTime, S.ArrivalTime, R.Status " +
            "FROM Reservation R " +
            "JOIN FlightInstance FI ON R.FlightInstanceID = FI.FlightInstanceID " +
            "JOIN Flight F ON FI.FlightNumber = F.FlightNumber " +
            "JOIN Schedule S ON F.FlightNumber = S.FlightNumber " +
            "WHERE R.CustomerID = '" + customerId + "' " +
            "AND FI.FlightDate > CURRENT_DATE " +
            "AND R.Status IN ('Reserved', 'Waitlist');";

        int rowCount = esql.executeQuery(query);
        System.out.println("Total row(s): " + rowCount);
    } catch (Exception e) {
        System.err.println("Error in feature15: " + e.getMessage());
    }
   }

   public static void feature16(AirlineManagement esql) { //16: View My Maintenance Requests
      try {
        System.out.print("Enter your Technician ID: ");
        String techID = in.readLine();

        String query =
            "SELECT RepairID, PlaneID, RepairCode, RepairDate " +
            "FROM Repair " +
            "WHERE TechnicianID = '" + techID + "';";

        int rowCount = esql.executeQuery(query);
        System.out.println("Total row(s): " + rowCount);
    } catch (Exception e) {
        System.err.println("Error in feature16: " + e.getMessage());
    }
   }
   public static void feature17(AirlineManagement esql) {//17: View Repair History For a Plane
      try {
        System.out.print("Enter PlaneID: ");
        String planeID = in.readLine();

        String query = 
            "SELECT LastRepairDate " +
            "FROM Plane " +
            "WHERE PlaneID = '" + planeID + "';";

        int rowCount = esql.executeQuery(query);
        System.out.println("Total row(s): " + rowCount);
    } catch (Exception e) {
        System.err.println("Error in feature17: " + e.getMessage());
    }
   }

   // Make maintenance request listing plane ID, repair code requested, and date of request
   // WIP
   public static void feature18(AirlineManagement esql) {
      try {
          // Get Plane ID
          String planeID = getString("Input Plane ID (e.g., PL001): ");

          // Get repair code
          String repairCode = getDate("Input requested repair code (e.g., T001): ");

          // Get date
          String date = getDate("Input requested repair date (YYYY-MM-DD): ");

          String query = String.format(
          "SELECT COUNT(FlightNumber) AS DaysFlown, SUM(SeatsSold) AS TotalSeatsSold, SUM(SeatsTotal-SeatsSold) AS TotalSeatsUnsold\n" +
          "FROM FlightInstance\n" +
          "WHERE FlightNumber = '%s' AND FlightDate >= '%s' AND FlightDate <= '%s'\n" +
          "GROUP BY FlightNumber\n", planeID, repairCode, date
          );
         int rowCount = esql.executeQueryAndPrintResult(query);
         if (rowCount == 0) {
            System.out.println("No repairs found for specified plane during the specified dates.");
         }
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
      
      pause();
   }
   public static void feature19(AirlineManagement esql) {}
  


}//end AirlineManagement

