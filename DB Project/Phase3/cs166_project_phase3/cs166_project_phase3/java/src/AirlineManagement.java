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

   private String type = null;
   public void setType(String type) {
      this.type = type;
   }

   public String getType() {
      return this.type;
   }

   private String username = null;
   public void setUsername(String username) {
      this.username = username;
   }

   public String getUsername() {
      return this.username;
   }


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
            System.out.println("2. Log in");
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
                System.out.println("Management Functions:");
                System.out.println("\t1. Get a Flight's schedule for the week");
                System.out.println("\t2. Check Flight seats");
                System.out.println("\t3. Flight status (departure/arrival time)");
                System.out.println("\t4. Flights on a specific date");
                System.out.println("\t5. Passenger lists by flight");
                System.out.println("\t6. Reservation traveler details");
                System.out.println("\t7. Plane details");
                System.out.println("\t8. Technician repair history");
                System.out.println("\t9. Plane repairs in date range");
                System.out.println("\t10. Flight stats over date range");

                //**the following functionalities should only be able to be used by customers**
                System.out.println("Customer Features:");
                System.out.println("\t11. Search Flights by City and Date");
                System.out.println("\t12. Search Ticket Cost");
                System.out.println("\t13. Get Airplane Type");
                System.out.println("\t14. Make a Reservation");

                //**the following functionalities should ony be able to be used by Technicians**
                System.out.println("Technician Features:");
                System.out.println("\t15. View Repairs by Plane");
                System.out.println("\t16. View Maintenance Requests by Pilot");
                System.out.println("\t17. Add a New Repair");

               //**the following functionalities should ony be able to be used by Pilots**
                System.out.println("Pilot Features:");
                System.out.println("\t18. Submit Maintenance Request");

               //**the following functionalities should ony be able to be used by Admin (DB Admin)**
                System.out.println("DB Admin Specific Features:");
                System.out.println("\t19. Admin Create New User Code");
               
                System.out.println("All Users Features:");
                System.out.println("\t20 < LOG OUT");
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
                   case 19: feature19(esql); break;   // Admin only



                   case 20: usermenu = false; esql.setType(null); esql.setUsername(null); break;
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

   public static void CreateCustomer(AirlineManagement esql) {
      try {
         String userId = getString("Enter your new User ID: ");
         String password = getString("Enter your new password: ");

         // Insert into Auth table with type 'Customer'
         String insertAuth = String.format(
            "INSERT INTO Auth (Username, Password, Type)\n" +
            "VALUES ('%s', '%s', 'Customer');",
            userId, password
         );
         esql.executeUpdate(insertAuth);
         System.out.println("Customer account created successfully.");
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
   }

   public static void CreateStaff(AirlineManagement esql) {
      String approvalCode = getString("Enter your approval code: ");
      String staffType = "null";
      try {
         String checkCodeQuery = String.format(
            "SELECT CodeType FROM NewUserCode WHERE Code = '%s';", approvalCode
         );
         List<List<String>> codeResult = esql.executeQueryAndReturnResult(checkCodeQuery);

         if (codeResult.isEmpty()) {
            System.out.println("Approval code not found.");
            return;
         }
         staffType = codeResult.get(0).get(0);
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
         return;
      }
      // System.out.println(staffType);
      
      try {
         String userId = getString("Enter your new User ID: ");
         String password = getString("Enter your new password: ");

         // Insert into Auth table with type 'Customer'
         String insertAuth = String.format(
            "INSERT INTO Auth (Username, Password, Type)\n" +
            "VALUES ('%s', '%s', '%s');",
            userId, password, staffType
         );
         esql.executeUpdate(insertAuth);
         System.out.println("Staff account created successfully.");
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
   }

   /*
    * Creates a new user
    **/
   public static void CreateUser(AirlineManagement esql){
      System.out.println("1. New Customer");         
      System.out.println("2. New Staff");
      System.out.println("3. < RETURN");
      switch (readChoice()){
         case 1: CreateCustomer(esql); break;
         case 2: CreateStaff(esql); break;
         default : System.out.println("Unrecognized choice!"); break;
      }//end switch

      pause();
   }//end CreateUser


   /*
    * Check log in credentials for an existing user
    * @return User login or null if the user does not exist
    **/
   public static String LogIn(AirlineManagement esql) {
      try {
         String userId = getString("Enter your User ID: ");
         String password = getString("Enter your password: ");

         String query = String.format(
            "SELECT Username FROM Auth WHERE Username = '%s' AND Password = '%s';",
            userId, password
         );
         List<List<String>> result = esql.executeQueryAndReturnResult(query);

          if (!result.isEmpty()) {
            // Find the user's type from Auth table
            String typeQuery = String.format(
               "SELECT Type FROM Auth WHERE Username = '%s';",
               userId
            );
            List<List<String>> typeResult = esql.executeQueryAndReturnResult(typeQuery);
            if (!typeResult.isEmpty()) {
               String userType = typeResult.get(0).get(0);
               esql.setType(userType);
               esql.setUsername(userId);
               // System.out.println("Type set to: " + userType);
               // System.out.println(esql.getType() == userType);
               // System.out.println(String.valueOf(esql.getType()) == userType);
               // System.out.println(esql.getType().equals("Admin"));
               // System.out.println(String.valueOf(esql.getType()).equals("Admin"));
            } else {
               esql.setType(null);
               esql.setUsername(null);
            }
            System.out.println("Login successful. Welcome, " + userId + "!");
            pause();
            return userId;
         } else {
            System.out.println("Login failed. Invalid Username or Password.");
            return null;
         }
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
         return null;
      }
   }//end

   public static boolean authOnlyAllow(AirlineManagement esql, String allowedType) {
      if(String.valueOf(esql.getType()).equals("Admin") || String.valueOf(esql.getType()).equals(allowedType)) {
         return true;
      }
      
      System.out.println("This feature is not accessible to type " + esql.getType());
      pause();
      return false;
   }

// Rest of the functions definition go in here

   public static void feature1(AirlineManagement esql) { // Get Flight's schedule for the week
      if(!authOnlyAllow(esql, "Manager")) {
         return;
      }
      
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
      if(!authOnlyAllow(esql, "Manager")) {
         return;
      }
      
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
      if(!authOnlyAllow(esql, "Manager")) {
         return;
      }
      
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
      if(!authOnlyAllow(esql, "Manager")) {
         return;
      }
      
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
      if(!authOnlyAllow(esql, "Manager")) {
         return;
      }
      
      try {
         // Get Flight #
         String flightNum = getString("Input Flight number (e.g., F100): ");

         // Get date
         String currDate = getDate("Input date of interest (YYYY-MM-DD): ");

          String query = String.format(
          "SELECT R.ReservationID, C.CustomerID, C.FirstName, C.LastName, R.Status\n" +
          "FROM Reservation R\n" +
          "JOIN Customer C ON R.CustomerID = C.CustomerID\n" +
          "JOIN FlightInstance FI ON R.FlightInstanceID = FI.FlightInstanceID\n" +
          "WHERE FI.FlightNumber = '%s' AND FI.FlightDate = '%s'\n" +
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
      if(!authOnlyAllow(esql, "Manager")) {
         return;
      }
      
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
      if(!authOnlyAllow(esql, "Manager")) {
         return;
      }
      
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
      if(!authOnlyAllow(esql, "Manager")) {
         return;
      }
      
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
      if(!authOnlyAllow(esql, "Manager")) {
         return;
      }
      
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
      if(!authOnlyAllow(esql, "Manager")) {
         return;
      }
      
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
   
public static void feature11(AirlineManagement esql) { //11. Search Flights by City and Date
      if(!authOnlyAllow(esql, "Customer")) {
         return;
      }
      
      try {
      String depCity = getString("Enter Departure City: ");
      String arrCity = getString("Enter Arrival City: ");
      String flightDate = getDate("Enter Flight Date (YYYY-MM-DD): ");

      String query = String.format(
         "SELECT F.FlightNumber, S.DepartureTime, S.ArrivalTime, " +
         "CAST(AVG(FI.NumOfStops) AS INT) AS NumOfStops, " +
         "ROUND(100.0 * SUM(CASE WHEN FI.DepartedOnTime IS TRUE AND FI.ArrivedOnTime IS TRUE THEN 1 ELSE 0 END) / COUNT(*), 2) AS OnTimePercentage " +
         "FROM Flight F " +
         "JOIN FlightInstance FI ON F.FlightNumber = FI.FlightNumber " +
         "JOIN Schedule S ON F.FlightNumber = S.FlightNumber " +
         "WHERE F.DepartureCity = '%s' AND F.ArrivalCity = '%s' AND FI.FlightDate = '%s' " +
         "AND S.DayOfWeek = TRIM(TO_CHAR(DATE '%s', 'Day')) " +
         "GROUP BY F.FlightNumber, S.DepartureTime, S.ArrivalTime;",
         depCity, arrCity, flightDate, flightDate
      );

      int rowCount = esql.executeQueryAndPrintResult(query);
      if (rowCount == 0) {
         System.out.println("No matching flights found.");
      }
   } catch (Exception e) {
      System.out.println("Error in feature11: " + e.getMessage());
   }
   pause();
   }

   public static void feature12(AirlineManagement esql) { //12.Flight Number By ticket cost
      if(!authOnlyAllow(esql, "Customer")) {
         return;
      }
      
      try {
      String flightNumber = getString("Enter Flight Number (e.g., F100): ");

      String query = String.format(
         "SELECT FI.FlightInstanceID, FI.FlightDate, FI.TicketCost\n" +
         "FROM FlightInstance FI\n" +
         "WHERE FI.FlightNumber = '%s'\n" +
         "ORDER BY FI.FlightDate;", flightNumber
      );

      int rowCount = esql.executeQueryAndPrintResult(query);
      if (rowCount == 0) {
         System.out.println("No ticket cost found for that flight number.");
      }
   } catch (Exception e) {
      System.out.println("Error in feature12: " + e.getMessage());
   }

   pause();
   }

   public static void feature13(AirlineManagement esql) {//13. Get Airplane Type
      if(!authOnlyAllow(esql, "Customer")) {
         return;
      }
      
      try {
      String flightNumber = getString("Enter Flight Number (e.g., F100): ");

      String query = String.format(
         "SELECT DISTINCT P.Make, P.Model\n" +
         "FROM Flight F\n" +
         "JOIN Plane P ON F.PlaneID = P.PlaneID\n" +
         "WHERE F.FlightNumber = '%s';", flightNumber
      );

      int rowCount = esql.executeQueryAndPrintResult(query);
      if (rowCount == 0) {
         System.out.println("No airplane type found for that flight number.");
      }
   } catch (Exception e) {
      System.out.println("Error in feature13: " + e.getMessage());
   }

   pause();
   }

   public static void feature14(AirlineManagement esql) {//14. make a Reservation
      if(!authOnlyAllow(esql, "Customer")) {
         return;
      }
      
      try {
      System.out.print("Enter your CustomerID: ");
      String customerId = in.readLine();

      System.out.print("Enter FlightInstanceID: ");
      String flightInstanceId = in.readLine();

      // check seat availability
      String checkSeats = "SELECT SeatsSold, SeatsTotal FROM FlightInstance WHERE FlightInstanceID = '" + flightInstanceId + "';";
      List<List<String>> seatInfo = esql.executeQueryAndReturnResult(checkSeats);

      if (seatInfo.size() == 0) {
         System.out.println("Invalid FlightInstanceID.");
         return;
      }

      int seatsSold = Integer.parseInt(seatInfo.get(0).get(0));
      int seatsTotal = Integer.parseInt(seatInfo.get(0).get(1));
      String status = (seatsSold < seatsTotal) ? "reserved" : "waitlist";

      // get latest ReservationID and increment
      String getMaxId = "SELECT MAX(ReservationID) FROM Reservation;";
      List<List<String>> result = esql.executeQueryAndReturnResult(getMaxId);
      String lastId = result.get(0).get(0); // e.g., R2996

      int nextNum = Integer.parseInt(lastId.substring(1)) + 1;
      String newReservationId = "R" + nextNum;
      
      String checkStatusQuery = "SELECT Status FROM FlightInstance WHERE FlightInstanceID = '" + flightInstanceId + "';";
      List<List<String>> flightStatusInfo = esql.executeQueryAndReturnResult(checkStatusQuery);
      String flightStatus = flightStatusInfo.get(0).get(0).toLowerCase();

      if (flightStatus.equals("flown")) {
         System.out.println("Cannot make a reservation. This flight has already been completed.");
         return;
      }
      // insert reservation
      String insertQuery = String.format(
         "INSERT INTO Reservation (ReservationID, CustomerID, FlightInstanceID, Status) " +
         "VALUES ('%s', '%s', '%s', '%s');",
         newReservationId, customerId, flightInstanceId, status);
      esql.executeUpdate(insertQuery);

      // confirm insert
      System.out.println("Reservation created successfully:");
      System.out.println("Reservation ID: " + newReservationId);
      System.out.println("Status: " + status);

   } catch (Exception e) {
      System.err.println("Error in feature14: " + e.getMessage());
   }

   pause();
   }

   public static void feature15(AirlineManagement esql) {//15. View Repairs by Plane in Date Range
      if(!authOnlyAllow(esql, "Technician")) {
         return;
      }
      
      try {
      System.out.print("Enter PlaneID: ");
      String planeID = in.readLine();

      System.out.print("Enter start date (YYYY-MM-DD): ");
      String startDate = in.readLine();

      System.out.print("Enter end date (YYYY-MM-DD): ");
      String endDate = in.readLine();

      String query = String.format(
         "SELECT RepairDate, RepairCode " +
         "FROM Repair " +
         "WHERE PlaneID = '%s' " +
         "AND RepairDate BETWEEN '%s' AND '%s' " +
         "ORDER BY RepairDate;",
         planeID, startDate, endDate
      );

      int rowCount = esql.executeQueryAndPrintResult(query);
      if (rowCount == 0) {
         System.out.println("No repairs found for the specified plane and date range.");
      }
   } catch (Exception e) {
      System.err.println("Error in feature15: " + e.getMessage());
   }

   pause();
   }
   public static void feature16(AirlineManagement esql) {//16. View Maintenance Requests by Pilot
      if(!authOnlyAllow(esql, "Technician")) {
         return;
      }
      
      try {
      System.out.print("Enter PilotID: ");
      String pilotID = in.readLine();

      String query = String.format(
         "SELECT MR.RequestID, MR.PlaneID, MR.RepairCode, MR.RequestDate " +
         "FROM MaintenanceRequest MR " +
         "WHERE MR.PilotID = '%s' " +
         "ORDER BY MR.RequestDate DESC;", pilotID
      );

      int rowCount = esql.executeQueryAndPrintResult(query);
      if (rowCount == 0) {
         System.out.println("No maintenance requests found for this pilot.");
      }
   } catch (Exception e) {
      System.err.println("Error in feature16: " + e.getMessage());
   }
   pause();

   }
   
   public static void feature17(AirlineManagement esql) {//17. Add a New Repair
      if(!authOnlyAllow(esql, "Technician")) {
         return;
      }
      
      try {
      System.out.print("Enter RepairID: ");  // NEW
      String repairID = in.readLine();       // NEW

      System.out.print("Enter PlaneID: ");
      String planeID = in.readLine();

      System.out.print("Enter Repair Code: ");
      String repairCode = in.readLine();

      System.out.print("Enter Repair Date (YYYY-MM-DD): ");
      String repairDate = in.readLine();

      System.out.print("Enter Technician ID: ");
      String techID = in.readLine();

      String query =
         "INSERT INTO Repair (RepairID, PlaneID, RepairCode, RepairDate, TechnicianID) " +
         "VALUES ('" + repairID + "', '" + planeID + "', '" + repairCode + "', '" + repairDate + "', '" + techID + "');";

      esql.executeUpdate(query);
      System.out.println("Repair entry added successfully.");

   } catch (Exception e) {
      System.err.println("Error in feature17: " + e.getMessage());
   }

   pause();
   }

// Make maintenance request listing plane ID, repair code requested, and date of request
   public static void feature18(AirlineManagement esql) {
      if(!authOnlyAllow(esql, "Pilot")) {
         return;
      }
      
      try {
          // Get Plane ID
          String planeID = getString("Input Plane ID (e.g., PL001): ");

          // Get repair code
          String repairCode = getString("Input requested repair code (e.g., T001): ");

          // Get date
          String date = getDate("Input requested repair date (YYYY-MM-DD): ");

          String getMaxRequestIDQuery = "SELECT MAX(RequestID) FROM MaintenanceRequest;";
          List<List<String>> maxIdResult = esql.executeQueryAndReturnResult(getMaxRequestIDQuery);
          int maxRequestID = 0;
          if (maxIdResult.size() > 0 && maxIdResult.get(0).get(0) != null) {
              maxRequestID = Integer.parseInt(maxIdResult.get(0).get(0));
          }
         //  System.out.println("Current largest RequestID: " + maxRequestID);

         // Generate a new unique RequestID using a sequence (assuming you have a sequence named MaintenanceRequest_seq)
           String query = String.format(
            "INSERT INTO MaintenanceRequest(RequestID,PlaneID,RepairCode,RequestDate,PilotID)\n" +
            "VALUES('%s', '%s', '%s', '%s', '%s');\n", // P001 for PilotID as placeholder
            (maxRequestID+1), planeID, repairCode, date, String.valueOf(esql.username)
           );
          esql.executeUpdate(query);
          System.out.println("Request submitted. Summary:");
          System.out.println("\tPlane ID: " + planeID);
          System.out.println("\tRepair Code: " + repairCode);
          System.out.println("\tDate: " + date);
          System.out.println("\tPilot ID: " + esql.username);
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }
      
      pause();
   }

   // Admin only.
   public static void feature19(AirlineManagement esql) {
      if(!authOnlyAllow(esql, "Admin")) {
         return;
      }

      System.out.println("Create new user code for: ");
      System.out.println("1. Admin");  
      System.out.println("2. Manager");       
      System.out.println("3. Technician");
      System.out.println("4. Pilot");
      System.out.println("5. < RETURN");
      String type = "null";
      switch (readChoice()){
         case 1: type = "Admin"; break;
         case 2: type = "Manager"; break;
         case 3: type = "Technician"; break;
         case 4: type = "Pilot"; break;
         case 5: return;
         default : System.out.println("Unrecognized choice!"); break;
      }

      // Generate a random 5-digit code
      String code = "";
      for (int i = 0; i < 5; i++) {
         code += (int)(Math.random() * 10);
      }
      System.out.println("Generated code: " + code);

      try {
         String deleteQuery = String.format(
            "DELETE FROM NewUserCode WHERE CodeType = '%s';",
            type
         );
         esql.executeUpdate(deleteQuery);
         System.out.println("Deleted any existing code for type: " + type);
      } catch (Exception e) {
         System.out.println("Error deleting old code: " + e.getMessage());
      }

      // Insert the code into NewUserCode table
      try {
         String insertCode = String.format(
         "INSERT INTO NewUserCode (CodeType, Code)\n" +
         "VALUES ('%s', '%s');",
         type, code
         );
         esql.executeUpdate(insertCode);
         System.out.println("New user code replaced for type: " + type);
         System.out.println("Code: " + String.valueOf(code));
      } catch (Exception e) {
         System.out.println("Error: " + e.getMessage());
      }

      pause();
   }
  


}//end AirlineManagement

