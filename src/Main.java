import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    // success if you see this yes!!!!
    private  static Scanner scanner = new Scanner(System.in);
    private static Statement statement;
    private static Connection conn;

    public static void main(String[] args) {

       _show_userMenu();
    }

    private static void _show_userMenu() {
        System.out.println();
        System.out.println("Welcome to Employee TimeSheet Application");
        System.out.println("======================================================");
        System.out.println("1.Admin");
        System.out.println("2.Manager");
        System.out.println("3.Employee");
        System.out.println("======================================================");
        _Main();

    }

    private static void _Main() {
        _connDB();
        System.out.print("Select you position : ");
        int choice = scanner.nextInt();
        switch (choice){
            case 1:
                // ADMIN
                Admin admin = new Admin(statement,new Admin.Admin_interface() {
                    @Override
                    public void _Logout_main() {
                        _show_userMenu();
                    }
                });

                admin._showMenu("main");

                break;
            case 2:
                //TODO MANAGER
                break;
            case 3:
                // EMPLOYEE
                Employee employee = new Employee(statement, new Employee.Employee_interface() {
                    @Override
                    public void logout() {
                        _show_userMenu();
                    }
                });

                break;
            default:
                System.out.println("Enter valid details...");
                _Main();

        }


    }


    private static void _connDB() {
        String
                DB_URL = "jdbc:mysql://localhost:3306/empdb";


        try {
            conn = DriverManager.getConnection(DB_URL, "root", "Akshaya@2003");
            statement = conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}