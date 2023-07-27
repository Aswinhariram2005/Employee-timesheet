
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Admin {
    private Scanner scanner = new Scanner(System.in);
    private String emp_name, emp_department;

    public void _showMenu() {
        System.out.println("Welcome to Admin page");
        System.out.println("1. Add new Employee");
        System.out.println("2. Update Existing Employee");
        System.out.println("3. Remove Employee");
        System.out.println("4. Logout");
        _decider();
    }

    public void _decider() {
        System.out.print("Enter you option : ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                _create_employee();
                break;
            case 2:
                // TODO UPDATE
                break;
            case 3:
                //TODO REMOVE
                break;
            case 4:
                //TODO LOGOUT
                break;
            default:
                System.out.println("Enter valid choice...");
                _decider();
        }
    }


    public void _create_employee() {
        scanner.nextLine();
        System.out.println("Create Employee");
        System.out.print("Enter Employee Name : ");
        emp_name = scanner.nextLine();
        System.out.print("Enter Employee Department : ");
        emp_department = scanner.nextLine();

        _saveDB();

    }

    public void _saveDB() {

        String
                DB_URL = "jdbc:mysql://localhost:3307/empdb";

        try {
           Connection conn = DriverManager.getConnection(DB_URL,"root","aswin123");
             Statement statement = conn.createStatement();
             String query ="INSERT INTO `empdb`.`emp_details` (`emp_name`, `emp_dept`) VALUES ( '"+emp_name+"' , '" +emp_department+"' );";
            statement.executeUpdate(query);
            System.out.println("Record is inserted in the table successfully..................");



        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
