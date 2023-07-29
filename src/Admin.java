import java.sql.*;
import java.util.Scanner;

public class Admin {
    private Scanner scanner = new Scanner(System.in);
    private String emp_name, emp_department, emp_password, c_password, emp_ph, emp_id;
    private Statement statement;
    private Connection conn;

    public void _showMenu(String menu) {


        if (menu.equals("main")) {
            System.out.println();
            System.out.println("Welcome to Admin page");
            System.out.println("1. Add new Employee");
            System.out.println("2. Update Existing Employee");
            System.out.println("3. Remove Employee");
            System.out.println("4. Logout");
            System.out.println();
            _decider("main");
        } else if (menu.equals("update_emp")) {
            System.out.println();
            System.out.println("Update menu");
            System.out.println("1. Employee Name");
            System.out.println("2. Employee Department");
            System.out.println("3. Employee Phone Number");
            System.out.println("4. Employee Password");
            System.out.println("5.Cancel update");
            System.out.println();

            _decider("update_emp");
        }
    }

    public void _connDB() {
        String
                DB_URL = "jdbc:mysql://localhost:3307/empdb";


        try {
            conn = DriverManager.getConnection(DB_URL, "root", "aswin123");
            statement = conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void _decider(String decider) {
        System.out.print("Enter you option : ");
        int choice = scanner.nextInt();
        if (decider.equals("main")) {
            switch (choice) {
                case 1:
                    // CREATE
                    _create_employee();
                    break;
                case 2:
                    // UPDATE
                    scanner.nextLine();
                    System.out.print("Enter Employee id : ");
                    emp_id = scanner.nextLine();
                    System.out.println();

                    _showMenu("update_emp");
                    break;
                case 3:
                    //TODO REMOVE
                    break;
                case 4:
                    //TODO LOGOUT
                    break;
                default:
                    System.out.println("Enter valid choice...");
                    _decider("main");
            }
        } else if (decider.equals("update_emp")) {

            String upd_query;
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // NAME
                    System.out.print("Enter new Employee Name: ");
                    emp_name = scanner.nextLine();
                    upd_query = "UPDATE emp_details SET emp_name = '" + emp_name+ "' where emp_id='"+emp_id+"' ;";
                    _update_emp(upd_query);
                    break;
                case 2:
                    // DEPARTMENT
                    System.out.print("Enter new Employee Department: ");
                    emp_department = scanner.nextLine();
                    upd_query = "UPDATE emp_details SET emp_dept = '" + emp_department + "' where emp_id='"+emp_id+"' ;";
                    _update_emp(upd_query);
                    break;
                case 3:
                    // PHONE
                    System.out.print("Enter new Employee Phone: ");
                    emp_ph = scanner.nextLine();
                    upd_query = "UPDATE emp_details SET emp_ph = '" + emp_ph + "' where emp_id='"+emp_id+"' ;";
                    _update_emp(upd_query);
                    break;
                case 4:
                    //PASS
                    _validate("upd");

                    break;
                case 5:
                    //EXIT
                    _showMenu("main");
                    break;
                default:
                    System.out.println("Enter valid choice...");
                    _decider("update_emp");
            }
        }
    }

    private void _update_emp(String upd_query) {
        try {
            statement.executeUpdate(upd_query);
            System.out.println("Update Successful...");
            _showMenu("update_emp");
        } catch (Exception e) {
            System.out.println(e);
        }

    }


    public void _create_employee() {
        scanner.nextLine();


        System.out.println("Create Employee");

        System.out.print("Enter Employee Name :  ");
        emp_name = scanner.nextLine();

        System.out.print("Enter Employee Department :  ");
        emp_department = scanner.nextLine();

        System.out.print("Enter Employee Phone Number :  ");
        emp_ph = scanner.nextLine();

        _validate("save");


    }

    private void _validate(String menu) {

        System.out.print("Create Password : ");
        emp_password = scanner.nextLine();

        System.out.print("Confirm Password : ");
        c_password = scanner.nextLine();

        if (emp_password.equals(c_password) && menu.equals("save")) {
            _saveDB();
        }

        else if (emp_password.equals(c_password) && menu.equals("upd")) {

            String upd_query = "UPDATE emp_details SET emp_password = '" + emp_password + "' where emp_id='"+emp_id+"' ;";
            _update_emp(upd_query);

        }

        else {
            System.out.println("Confirm password mismatch...");
            _validate(menu);
        }
    }

    public void _saveDB() {


        try {
            String insert_query = "INSERT INTO `empdb`.`emp_details` (`emp_name`, `emp_dept`,`emp_ph`,`emp_password`) VALUES ( '" + emp_name + "'  , '" + emp_department + "','" + emp_ph + "','" + emp_password + "');";

            statement.executeUpdate(insert_query);

            System.out.println("Employee added successfully..................");

            _get_emp_id();


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void _get_emp_id() {
        try {
            String id_query = "select emp_id from emp_details where emp_ph='" + emp_ph + "" + "';";
            ResultSet set = statement.executeQuery(id_query);
            while (set.next()) {
                emp_id = set.getString("emp_id");
                System.out.println("Your Employee ID is : " + emp_id);
                _showMenu("main");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
