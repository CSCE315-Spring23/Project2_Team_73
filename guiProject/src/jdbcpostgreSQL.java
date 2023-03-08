import java.sql.*;
import java.text.DecimalFormat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.lang.Math.*;


/**
 * The jdbcpostgreSQL class represents a graphical user interface for interacting with a PostgreSQL database using JDBC.
 * It extends the JFrame class and implements the ActionListener interface to handle user events.
 * This class provides methods to establish a connection to a PostgreSQL database, execute SQL queries and updates,
 * and display the results in a table format. It also includes event listeners for user actions such as button clicks
 * and menu selections, allowing the user to interact with the database and perform various operations.
 * To use this class, you must have the JDBC driver for PostgreSQL installed and configured on your system
 * @author Tyler, Nitesh, Lucas
 */ 
public class jdbcpostgreSQL extends JFrame implements ActionListener {
    static JFrame f;
    static JFrame managerView;
    String orderList = "";
    String orderListID = "";
    Connection conn = null;
    String teamNumber = "team_73";
    String dbName = "csce315331_" + teamNumber;
    String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
    JTextArea orderArea = new JTextArea();
    JTextField text = new JTextField();
    double orderTotal;
    int employeeID;
    int currOrderId;

    // Commands to run this script
    // This will compile all java files in this directory
    // javac *.java
    // This command tells the file where to find the postgres jar which it needs to
    // execute postgres commands, then executes the code
    // Windows: java -cp ".;postgresql-42.2.8.jar" jdbcpostgreSQL
    // Mac/Linux: java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL

    // MAKE SURE YOU ARE ON VPN or TAMU WIFI TO ACCESS DATABASE
    /**
     * This is the default constructor for the class. It creates a connection to the database and sets up the GUI
     */
    public jdbcpostgreSQL() {
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

        try {
            Statement newStmt = conn.createStatement();
            String sqlStatement = "SELECT MAX(orderid) FROM orders";
            ResultSet result = newStmt.executeQuery(sqlStatement);
            if (result.next()) {
                currOrderId = result.getInt("max") + 1;
            }
        } catch (Exception d) {
            d.printStackTrace();
            System.err.println(d.getClass().getName() + ": " + d.getMessage());
            System.exit(0);
        }

        System.out.println("Opened database successfully");
        JPanel buttonPanel = new JPanel(new GridLayout(20, 3));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        buttonPanel.setPreferredSize(new Dimension(300, 300));

        try {
            // create a statement object
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT * FROM menu";
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {

                JButton button = new JButton();

                // Set the button's text and properties
                button.setText(result.getString("itemname"));
                button.setName(result.getString("itemname"));
                button.addActionListener(this);
                buttonPanel.add(button);

            }
            f = new JFrame("DB GUI");
            JSplitPane p = new JSplitPane();
            p.setDividerLocation(0.5);
            p.setLeftComponent(buttonPanel);
            // add panel to frame

            orderArea.setBorder(BorderFactory.createLineBorder(Color.black));
            orderArea.setPreferredSize(new Dimension(300, 300));
            JPanel lowPanel = new JPanel();
            lowPanel.add(orderArea);
            JButton orderButton = new JButton();
            // Set the button's text and properties
            orderButton.setText("Place Order");
            orderButton.setName("Place Order");
            orderButton.addActionListener(this);

            text.setText("Enter Employee ID");
            JButton switchViewButton = new JButton();
            switchViewButton.setText("Enter Manager View");
            switchViewButton.setName("SwitchView");
            switchViewButton.addActionListener(this);

            lowPanel.add(text);
            lowPanel.add(orderButton);
            lowPanel.add(switchViewButton);

            p.setRightComponent(lowPanel);
            p.setDividerLocation(0.75);

            f.add(p);
            // JPanel orderPanel = new JPanel();
            // orderPanel.add(orderButton);
            // f.add(orderPanel);

            // set the size of frame
            
            f.setExtendedState(JFrame.MAXIMIZED_BOTH); 

            f.setVisible(true);
            managerView = new JFrame("Manager GUI");
            managerView.setExtendedState(JFrame.MAXIMIZED_BOTH); 

            managerView.setVisible(false);
            // OR
            // System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }
    /**
     * This is the actionPerformed method. It handles ActionEvents from the buttons.
     * It checks to see what type of button it is and performs the action needed.
     * @param e the ActionEvent object that describes the user's action 
     */
    public void actionPerformed(ActionEvent e) {
        // Update global variable when a button is clicked
        JButton o = (JButton) e.getSource();
        String name = o.getName();
        // Connecting to the database
        if (name == "Place Order") {
            // add order here
            employeeID = Integer.parseInt(text.getText());
            double totalOrder = orderTotal * 1.0825;
            DecimalFormat df = new DecimalFormat("#.##"); // round to 2 decimal places
            double roundedNum = Double.parseDouble(df.format(totalOrder));

            Random rand = new Random();
            int customerID = rand.nextInt(2000) + 1;
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String currTime = currentTime.format(formatter);
            orderListID = orderListID.substring(0, orderListID.length() - 2);
            try {
                conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
            } catch (Exception c) {
                c.printStackTrace();
                System.err.println(c.getClass().getName() + ": " + c.getMessage());
                System.exit(0);
            }
            try {
                Statement newStmt = conn.createStatement();
                String sqlStatement = "INSERT INTO orders VALUES (" + currOrderId + ", '" + currTime + "', "
                        + employeeID + ", " + customerID + ", '" + orderListID + "', " + roundedNum + ")";
                newStmt.executeUpdate(sqlStatement);
            } catch (Exception d) {
                d.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + d.getMessage());
                System.exit(0);
            }

            // reset variables
            orderList = "";
            orderListID = "";
            orderTotal = 0.0;
            text.setText("Please Enter EmployeeID");
            orderArea.setText("");
            currOrderId += 1;
        } else if (name == "SwitchView") {
            f.setVisible(false);
            managerView.setVisible(true);
        } else {
            try {
                conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
            } catch (Exception c) {
                c.printStackTrace();
                System.err.println(c.getClass().getName() + ": " + c.getMessage());
                System.exit(0);
            }

            try {
                Statement newStmt = conn.createStatement();
                String sqlStatement = "SELECT menuid, itemprice FROM menu WHERE itemName = '" + name + "'";
                ResultSet result = newStmt.executeQuery(sqlStatement);
                if (result.next()) {
                    orderListID += result.getInt("menuid") + ", ";
                    orderTotal += result.getDouble("itemprice");
                }
                System.out.println(orderListID);
                System.out.println(orderListID.substring(0, orderListID.length() - 2));
            } catch (Exception d) {
                d.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + d.getMessage());
                System.exit(0);
            }

            orderList += name + ", \n";
            orderArea.setText(orderList);
        }
    }
    /**
     * 
     * @param args an array of command-line arguments passed to the program
     * This main method is where the jdbcpostgreSQL class will be invoked, creating a new GUI.
     * 
     */
    public static void main(String args[]) {

        new jdbcpostgreSQL();

    }// end main
}// end Class