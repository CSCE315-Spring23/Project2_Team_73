import java.sql.*;
import javax.swing.*; 
import java.awt.*;

/*
CSCE 315
9-27-2021 Lab
 */

public class jdbcpostgreSQL extends JFrame {
    static JFrame f;

    // Commands to run this script
    // This will compile all java files in this directory
    // javac *.java
    // This command tells the file where to find the postgres jar which it needs to
    // execute postgres commands, then executes the code
    // Windows: java -cp ".;postgresql-42.2.8.jar" jdbcpostgreSQL
    // Mac/Linux: java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL

    // MAKE SURE YOU ARE ON VPN or TAMU WIFI TO ACCESS DATABASE
    public static void main(String args[]) {

        // Building the connection with your credentials
        Connection conn = null;
        String teamNumber = "team_73";
        String dbName = "csce315331_" + teamNumber;
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;

        // Connecting to the database
        try {
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Opened database successfully");
        JPanel buttonPanel = new JPanel(new GridLayout(20, 3));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        String name = "";
        try {
            // create a statement object
            Statement stmt = conn.createStatement();

            // Running a query
            // TODO: update the sql command here
            String sqlStatement = "SELECT * FROM menu";

            // send statement to DBMS
            // This executeQuery command is useful for data retrieval
            ResultSet result = stmt.executeQuery(sqlStatement);
            // OR
            // This executeUpdate command is useful for updating data
            // int result = stmt.executeUpdate(sqlStatement);

            // OUTPUT
            // You will need to output the results differently depeninding on which function
            // you use

            System.out.println("--------------------Query Results--------------------");
            while (result.next()) {
                name += result.getString("itemPrice") + "\n"; // Replace column_name with any column name on your
                                                              // table
                JButton button = new JButton();

                // Set the button's text and properties
                button.setText(result.getString("itemname"));
                button.setName(result.getString("itemname"));
                button.setSize(100, 50);

                // Add the button to the panel
                buttonPanel.add(button);
            }

            // create a new frame
            // create a new frame
            f = new JFrame("DB GUI");

            // create a object
            GUI s = new GUI();

            // create a panel
            JPanel p = new JPanel();
            p.setSize(400, 400);
          

            

            // TODO Step 3
           
            
            // TODO Step 4

          
            p.add(buttonPanel);
            // add panel to frame
            f.add(p);
            
            // set the size of frame
            f.setSize(400, 400);

            f.show();
            // OR
            // System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        // closing the connection
        try {
            conn.close();
            System.out.println("Connection Closed.");
        } catch (Exception e) {
            System.out.println("Connection NOT Closed.");
        }

        // end try catch
    }// end main
}// end Class