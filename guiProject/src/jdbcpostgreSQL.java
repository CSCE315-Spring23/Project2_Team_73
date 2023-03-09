import java.sql.*;
import java.text.DecimalFormat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.lang.Math.*;
import java.util.ArrayList;

/**
 * The jdbcpostgreSQL class represents a graphical user interface for
 * interacting with a PostgreSQL database using JDBC.
 * It extends the JFrame class and implements the ActionListener interface to
 * handle user events.
 * This class provides methods to establish a connection to a PostgreSQL
 * database, execute SQL queries and updates,
 * and display the results in a table format. It also includes event listeners
 * for user actions such as button clicks
 * and menu selections, allowing the user to interact with the database and
 * perform various operations.
 * To use this class, you must have the JDBC driver for PostgreSQL installed and
 * configured on your system
 * 
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
    JTextArea itemName = new JTextArea();
    JComboBox<String> menuItemList = getMenuItemList();
    JComboBox<String> inventoryItemList = getInvItemList();
    JTextArea itemPrice = new JTextArea();

    JTextArea itemInv = new JTextArea();

    JTextArea invPrice = new JTextArea();
    JTextArea invName = new JTextArea();
    JTextArea invCount = new JTextArea();
    JPanel buttonPanel = new JPanel(new GridLayout(20, 3));

    // Commands to run this script
    // This will compile all java files in this directory
    // javac *.java
    // This command tells the file where to find the postgres jar which it needs to
    // execute postgres commands, then executes the code
    // Windows: java -cp ".;postgresql-42.2.8.jar" jdbcpostgreSQL
    // Mac/Linux: java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL

    // MAKE SURE YOU ARE ON VPN or TAMU WIFI TO ACCESS DATABASE
    /**
     * This is the default constructor for the class. It creates a connection to the
     * database and sets up the GUI
     */
    public void createManagerView() {

    }

    public jdbcpostgreSQL() {

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
            JPanel mainManagerPanel = new JPanel();
            mainManagerPanel.setLayout(new GridLayout(2, 2));

            JButton switchViewButtonManager = new JButton();
            switchViewButtonManager.setText("Enter Employee View");
            switchViewButtonManager.setName("SwitchViewManager");
            switchViewButtonManager.addActionListener(this);
            mainManagerPanel.add(switchViewButtonManager);

            // changing menu
            JPanel innerMenuPane = new JPanel();
            innerMenuPane.setLayout(new BoxLayout(innerMenuPane, BoxLayout.PAGE_AXIS));
            // AHHAHHAHAHAHHAHAHAHHAHAHAHAH
            menuItemList = getMenuItemList();
            menuItemList.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    String name = menuItemList.getSelectedItem().toString();
                    updateMenuText(name);
                }
            });
            innerMenuPane.add(menuItemList);

            itemName.setBorder(BorderFactory.createLineBorder(Color.black));
            innerMenuPane.add(itemName);

            innerMenuPane.add(itemPrice);

            itemInv.setBorder(BorderFactory.createLineBorder(Color.black));
            innerMenuPane.add(itemInv);

            JButton addMenuButton = new JButton();
            addMenuButton.setText("Add");
            addMenuButton.setName("AddMenu");
            addMenuButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (menuItemList.getSelectedItem().toString() == "Add New Item....") {
                        addHelper();
                    }
                }
            });
            innerMenuPane.add(addMenuButton);

            JButton updateMenuButton = new JButton();
            updateMenuButton.setText("Update");
            updateMenuButton.setName("UpdateMenu");
            updateMenuButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    updateHelper(menuItemList.getSelectedItem().toString());
                }
            });
            innerMenuPane.add(updateMenuButton);

            JButton deleteMenuButton = new JButton();
            deleteMenuButton.setText("Delete");
            deleteMenuButton.setName("DeleteMenu");
            deleteMenuButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    deleteHelper(menuItemList.getSelectedItem().toString());
                }
            });
            innerMenuPane.add(deleteMenuButton);

            /////////////// next pane///////////
            //// INVENTORY///////

            JPanel nextInnerPanel = new JPanel();
            nextInnerPanel.setLayout(new BoxLayout(nextInnerPanel, BoxLayout.PAGE_AXIS));

            inventoryItemList = getInvItemList();
            nextInnerPanel.add(inventoryItemList);
            inventoryItemList.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    String name = inventoryItemList.getSelectedItem().toString();
                    updateInvText(name);
                }
            });

            nextInnerPanel.add(inventoryItemList);

            invName.setBorder(BorderFactory.createLineBorder(Color.black));
            invName.setPreferredSize(new Dimension(200, 30));
            nextInnerPanel.add(invName);

            invPrice.setPreferredSize(new Dimension(200, 30));
            nextInnerPanel.add(invPrice);

            invCount.setPreferredSize(new Dimension(200, 30));
            invCount.setBorder(BorderFactory.createLineBorder(Color.black));
            nextInnerPanel.add(invCount);

            JButton addInvButton = new JButton();
            addInvButton.setText("Add");
            addInvButton.setName("AddInv");
            addInvButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (inventoryItemList.getSelectedItem().toString() == "Add New Item....") {
                        addHelperInventory();
                    }
                }
            });
            nextInnerPanel.add(addInvButton);

            JButton updateInvButton = new JButton();
            updateInvButton.setText("Update");
            updateInvButton.setName("UpdateInv");
            updateInvButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    updateHelperInventory(inventoryItemList.getSelectedItem().toString());
                }
            });
            nextInnerPanel.add(updateInvButton);

            // add to overall
            mainManagerPanel.add(innerMenuPane);
            mainManagerPanel.add(nextInnerPanel);
            managerView.add(mainManagerPanel);
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
     * 
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
                Statement newStmt = conn.createStatement();
                String sqlStatement = "INSERT INTO orders VALUES (" + currOrderId + ", '" + currTime + "', "
                        + employeeID + ", " + customerID + ", '" + orderListID + "', " + roundedNum + ")";
                newStmt.executeUpdate(sqlStatement);
            } catch (Exception d) {
                d.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + d.getMessage());
                System.exit(0);
            }

            updateInventory(orderListID);

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

        } else if (name == "SwitchViewManager") {
            managerView.setVisible(false);
            f.setVisible(true);

        } else {

            try {
                Statement newStmt = conn.createStatement();
                String sqlStatement = "SELECT menuid, itemprice FROM menu WHERE itemName = '" + name + "'";
                ResultSet result = newStmt.executeQuery(sqlStatement);
                if (result.next()) {
                    orderListID += result.getInt("menuid") + ", ";
                    orderTotal += result.getDouble("itemprice");
                }
                 
            } catch (Exception d) {
                d.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + d.getMessage());
                System.exit(0);
            }

            orderList += name + ", \n";
            orderArea.setText(orderList);
        }
    }

    public JComboBox<String> getMenuItemList() {
        JComboBox<String> dropdownMenu = new JComboBox<String>();
        dropdownMenu.addItem("Select Item");
        try {
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        try {
            Statement newStmt = conn.createStatement();
            String sqlStatement = "SELECT itemname FROM menu";
            ResultSet result = newStmt.executeQuery(sqlStatement);
            while (result.next()) {
                String item = result.getString("itemname");
                dropdownMenu.addItem(item);
            }
        } catch (Exception d) {
            d.printStackTrace();
            System.err.println(d.getClass().getName() + ": " + d.getMessage());

        }

        dropdownMenu.addItem("Add New Item....");
        return dropdownMenu;
    }

    public void updateMenuText(String name) {
        if (name == "Add New Item....") {
            itemName.setText("Replace with Item Name");
            itemPrice.setText("Replace with Price");
            itemInv.setText("Replace with Ingredient List");
        } else if (name == "Select Item") {
            itemName.setText("");
            itemPrice.setText("");
            itemInv.setText("");

        } else {

            try {
                Statement newStmt = conn.createStatement();
                String sqlStatement = "SELECT * FROM menu where itemname = '" + name + "'";
                ResultSet result = newStmt.executeQuery(sqlStatement);
                if (result.next()) {
                    // set text areas
                    itemName.setText(name);
                    itemPrice.setText(result.getString("itemprice"));
                    itemInv.setText(result.getString("invlist"));

                }
            } catch (Exception d) {
                d.printStackTrace();
                System.err.println(d.getClass().getName() + ": " + d.getMessage());

            }
        }
    }

    public void updateHelper(String name) {

        try {

            String sqlStatement = "UPDATE menu SET itemname = ?, itemprice = ?, invlist = ? WHERE itemname = '" + name
                    + "'";
            PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
            String newName = itemName.getText();
            pstmt.setString(1, itemName.getText());
            pstmt.setDouble(2, Double.valueOf(itemPrice.getText()));
            pstmt.setString(3, itemInv.getText());
            pstmt.executeUpdate();
            menuItemList.removeItemAt(menuItemList.getItemCount() - 1);
            menuItemList.removeItem(menuItemList.getSelectedItem());

            menuItemList.addItem(newName);
            menuItemList.addItem("Add New Item....");
            menuItemList.setSelectedItem(newName); 
            
        } catch (Exception d) {
            d.printStackTrace();
            System.err.println(d.getClass().getName() + ": " + d.getMessage());

        }
    }

    public void addHelper() {
        int currMenuID = 0;

        try {
            Statement newStmt = conn.createStatement();
            String sqlStatement = "SELECT MAX(menuid) FROM menu";
            ResultSet result = newStmt.executeQuery(sqlStatement);
            if (result.next()) {
                currMenuID = result.getInt("max") + 1;
            }
        } catch (Exception d) {
            d.printStackTrace();
            System.err.println(d.getClass().getName() + ": " + d.getMessage());
            System.exit(0);
        }

        try {

            String sqlStatement = "INSERT INTO menu VALUES (?, ? ,?, ?) ";
            PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
            String newName = itemName.getText();
            pstmt.setInt(1, currMenuID);
            pstmt.setString(2, itemName.getText());
            pstmt.setDouble(3, Double.valueOf(itemPrice.getText()));
            pstmt.setString(4, itemInv.getText());
            pstmt.executeUpdate();
            menuItemList.removeItemAt(menuItemList.getItemCount() - 1);

            menuItemList.addItem(newName);
            menuItemList.addItem("Add New Item....");
            menuItemList.setSelectedItem(newName);

            ///////////////
            JButton newItem = new JButton();
            newItem.setName(newName);
            newItem.setText(newName);
            newItem.addActionListener(this);
            buttonPanel.add(newItem);

        } catch (Exception d) {
            d.printStackTrace();
            System.err.println(d.getClass().getName() + ": " + d.getMessage());

        }
    }

    public void deleteHelper(String name) {
        try {

            String sqlStatement = "DELETE FROM menu WHERE itemname = ? ";
            PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
            pstmt.setString(1, name);
            pstmt.executeUpdate();

            menuItemList.removeItem(menuItemList.getSelectedItem());

            menuItemList.setSelectedItem(0);
            buttonPanel.remove(buttonPanel.getComponentCount() -1);

        } catch (Exception d) {
            d.printStackTrace();
            System.err.println(d.getClass().getName() + ": " + d.getMessage());

        }
    }

    public JComboBox<String> getInvItemList() {
        JComboBox<String> dropdownMenu = new JComboBox<String>();
        dropdownMenu.addItem("Select Item");
        try {
            Statement newStmt = conn.createStatement();
            String sqlStatement = "SELECT itemname FROM inventory";
            ResultSet result = newStmt.executeQuery(sqlStatement);
            while (result.next()) {
                String item = result.getString("itemname");
                dropdownMenu.addItem(item);
            }
        } catch (Exception d) {
            d.printStackTrace();
            System.err.println(d.getClass().getName() + ": " + d.getMessage());

        }

        dropdownMenu.addItem("Add New Item....");
        return dropdownMenu;
    }

    public void updateInvText(String name) {
        if (name == "Add New Item....") {
            invName.setText("Replace with Item Name");
            invPrice.setText("Replace with Price");
            invCount.setText("Replace with Ingredient Count");
        } else if (name == "Select Item") {
            invName.setText("");
            invPrice.setText("");
            invCount.setText("");
        } else {

            try {
                Statement newStmt = conn.createStatement();
                String sqlStatement = "SELECT * FROM inventory where itemname = '" + name + "'";
                ResultSet result = newStmt.executeQuery(sqlStatement);
                if (result.next()) {
                    // set text areas
                    invName.setText(name);
                    invPrice.setText(String.valueOf(result.getDouble("costper")));
                    invCount.setText(String.valueOf(result.getDouble("numitems")));

                }
            } catch (Exception d) {
                d.printStackTrace();
                System.err.println(d.getClass().getName() + ": " + d.getMessage());

            }
        }
    }

    public void addHelperInventory() {
        int currInventoryID = 0;

        try {
            Statement newStmt = conn.createStatement();
            String sqlStatement = "SELECT MAX(invid) FROM inventory";
            ResultSet result = newStmt.executeQuery(sqlStatement);
            if (result.next()) {
                currInventoryID = result.getInt("max") + 1;
            }
        } catch (Exception d) {
            d.printStackTrace();
            System.err.println(d.getClass().getName() + ": " + d.getMessage());
            System.exit(0);
        }

        try {

            String sqlStatement = "INSERT INTO inventory VALUES (?, ? ,?, ?) ";
            PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
            String newName = invName.getText();
            pstmt.setInt(1, currInventoryID);
            pstmt.setDouble(2, Double.valueOf(invCount.getText()));
            pstmt.setDouble(3, Double.valueOf(invPrice.getText()));
            pstmt.setString(4, invName.getText());
            pstmt.executeUpdate();
            inventoryItemList.removeItemAt(inventoryItemList.getItemCount() - 1);

            inventoryItemList.addItem(newName);
            inventoryItemList.addItem("Add New Item....");
            inventoryItemList.setSelectedItem(newName);
             

        } catch (Exception d) {
            d.printStackTrace();
            System.err.println(d.getClass().getName() + ": " + d.getMessage());

        }
    }

    public void updateHelperInventory(String name) {
        try {

            String sqlStatement = "UPDATE inventory SET itemname = ?, costper = ?, numitems = ? WHERE itemname = '"
                    + name
                    + "'";
            PreparedStatement pstmt = conn.prepareStatement(sqlStatement);
            String newName = invName.getText();

            pstmt.setString(1, invName.getText());

            pstmt.setDouble(2, Double.valueOf(invPrice.getText()));

            pstmt.setDouble(3, Double.valueOf(invCount.getText()));

            pstmt.executeUpdate();
            inventoryItemList.removeItemAt(inventoryItemList.getItemCount() - 1);
            inventoryItemList.removeItem(inventoryItemList.getSelectedItem());

            inventoryItemList.addItem(newName);
            inventoryItemList.addItem("Add New Item....");
            inventoryItemList.setSelectedItem(newName);

        } catch (Exception d) {
            d.printStackTrace();
            System.err.println(d.getClass().getName() + ": " + d.getMessage());

        }
    }

    public void updateInventory(String inputString) {
        String currInvString = "";
        String[] inputArray = inputString.split(",\\s*");
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (String str : inputArray) {
            int value = Integer.parseInt(str.trim());
            arrayList.add(value);
        }
 

        for (Integer menuID : arrayList) {
            try {
                Statement newStmt = conn.createStatement();
                String sqlStatement = "SELECT invlist FROM menu WHERE menuid = " + menuID;
                ResultSet result = newStmt.executeQuery(sqlStatement);
                if (result.next()) {
                    currInvString = result.getString("invlist");
                }
            } catch (Exception d) {
                d.printStackTrace();
                System.err.println(d.getClass().getName() + ": " + d.getMessage());
                System.exit(0);
            }

            String[] invArray = currInvString.split(",\\s*");

            for (String invid : invArray) {
                int value = Integer.parseInt(invid.trim());
                try {

                    String sqlStatement = "UPDATE inventory SET numitems = numitems - 1 WHERE invid = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sqlStatement);

                    pstmt.setInt(1, value);

                    pstmt.executeUpdate();

                } catch (Exception d) {
                    d.printStackTrace();
                    System.err.println(d.getClass().getName() + ": " + d.getMessage());
                    System.exit(0);
                }
            }

        }

    }

    /**
     *
     * @param args an array of command-line arguments passed to the program
     *             This main method is where the jdbcpostgreSQL class will be
     *             invoked, creating a new GUI.
     *
     */

    public static void main(String args[]) {

        jdbcpostgreSQL this_db = new jdbcpostgreSQL();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    this_db.conn.close();
                    System.out.println("Database connection closed.");
                } catch (SQLException e) {
                    System.out.println("Failed to close database connection.");
                    e.printStackTrace();
                }
            }
        });

    }// end main
}// end Class
