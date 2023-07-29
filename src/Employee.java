import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Employee {

    private Scanner scanner = new Scanner(System.in);
    private String emp_id, emp_password;
    private Statement statement;
    private Employee_interface employee_interface;

    public Employee(Statement statement,Employee_interface employee_interface) {
        this.statement = statement;
        this.employee_interface = employee_interface;

        System.out.println();
        System.out.println("Welcome to Employee page");


        _show_EmpMenu();
    }






    private void _show_EmpMenu() {
        System.out.println("\tEnter login details ");
        System.out.print("Enter  Employee ID  :  ");
        emp_id = scanner.nextLine();
        System.out.print("Enter Employee Password :  ");
        emp_password = scanner.nextLine();

        _check_Emp();
    }

    private void _check_Emp() {
        try {
            ResultSet set = statement.executeQuery("select  *  from empdb.emp_details  where emp_id='"+emp_id+ "' and emp_password='"+emp_password+"';");
            if (set.next()){
                System.out.println("successfull ");
            }
            else {
                System.out.println("Employee Not Found");
                employee_interface.logout();

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    interface Employee_interface{
        void logout();
    }
}
