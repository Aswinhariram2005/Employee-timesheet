import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;


public class Admin {
    private Scanner scanner = new Scanner(System.in);
    private String emp_name, emp_department, emp_password, c_password, emp_ph, emp_id,emp_salary,salary_hrs,total_salary,leave_permit,hrs_worked;
    private Statement statement;
    private  Admin_interface admin_interface;
    String today,day,time;


    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public void setAdmin_interface(Admin_interface admin_interface) {
        this.admin_interface = admin_interface;
    }

    private void _date() {
        java.util.Date date = new Date();
        SimpleDateFormat date_formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm aa");
        LocalDate local_day = LocalDate.now();
        DayOfWeek dayOfWeek = local_day.getDayOfWeek();


        today = date_formatter.format(date);
        time = time_formatter.format(date);
        day = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
    }


    private void _update_day1() {
        String date = today.substring(0,2);
        if (date.equals("01")){

            String day1_query = "update emp_details set leave_permit = 3 , leave_update = '"+today+"' where leave_update != '"+today+"' ;";
            try {
                statement.executeUpdate(day1_query);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }


    public void _showMenu(String menu) {

        _date();
        _update_day1();

        if (menu.equals("main")) {
            System.out.println();
            System.out.println("Welcome to Admin page");
            System.out.println("======================================================");
            System.out.println("\t\t\tAdmin Menu");
            System.out.println("1. Add new Employee");
            System.out.println("2. Update Existing Employee");
            System.out.println("3. Remove Employee");
            System.out.println("4. View Employee Details");
            System.out.println("5. Logout");
            System.out.println("======================================================");
            System.out.println();
            _decider("main");
        }
        else if (menu.equals("update_emp")) {
            System.out.println();
            System.out.println("======================================================");
            System.out.println("\t\t\tUpdate Employee Details");
            System.out.println("1. Employee Name");
            System.out.println("2. Employee Department");
            System.out.println("3. Employee Phone Number");
            System.out.println("4. Employee Password");
            System.out.println("5. Employee Leave Limit");
            System.out.println("6. Salary Per Hour");
            System.out.println("7. Cancel Update");
            System.out.println("======================================================");
            System.out.println();
            _decider("update_emp");
        }


    }


    public void _decider(String decider) {


        System.out.print("Enter you option : ");
        int choice = scanner.nextInt();
        System.out.println();
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
                    if (_check_emp(emp_id)) {
                        _showMenu("update_emp");
                    }
                    break;
                case 3:
                    // REMOVE
                    scanner.nextLine();
                    System.out.print("Enter Employee id : ");
                    emp_id = scanner.nextLine();
                    System.out.println();


                    if (_check_emp(emp_id)) {
                        String remove_query = "delete from empdb.emp_details where emp_id = '"+emp_id+"';";
                        _update_emp(remove_query);
                    }
                    break;
                case 4:
                    //  VIEW
                    scanner.nextLine();
                    System.out.print("Enter Employee id : ");
                    emp_id = scanner.nextLine();
                    System.out.println();


                    if (_check_emp(emp_id)) {
                       _view_emp(emp_id);
                    }
                    break;
                case 5:
                    admin_interface._Logout_main();
                    break;
                default:
                    System.out.println("Enter valid choice...");
                    _decider("main");
            }
        }
        else if (decider.equals("update_emp")) {

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
                    // LEAVE LIMIT
                    System.out.print("Enter new Employee Leave Limit: ");
                    leave_permit = scanner.nextLine();
                    upd_query = "UPDATE emp_details SET leave_permit = '" + leave_permit + "' where emp_id='"+emp_id+"' ;";
                    _update_emp(upd_query);
                    break;
                case 6:
                    // SALARY PER HOUR
                    System.out.print("Enter new Salary Per hour: ");
                    salary_hrs = scanner.nextLine();
                    upd_query = "UPDATE emp_details SET salary_hrs = '" + salary_hrs + "' where emp_id='"+emp_id+"' ;";
                    _update_emp(upd_query);
                    break;
                case 7:
                    //EXIT
                    _showMenu("main");
                    break;
                default:
                    System.out.println("Enter valid choice...");
                    _decider("update_emp");
            }
        }


    }


    private void _view_emp(String empId) {


        System.out.println();
        try {
            ResultSet set = statement.executeQuery("select  *  from empdb.emp_details  where emp_id='" + emp_id + "';");
            while (set.next()) {
                emp_name = set.getString("emp_name");
                emp_department = set.getString("emp_dept");
                emp_password = set.getString("emp_password");
                emp_ph = set.getString("emp_ph");
                salary_hrs = set.getString("salary_hrs");
                total_salary = set.getString("total_salary");
                leave_permit = set.getString("leave_permit");
                hrs_worked = set.getString("hrs_worked");
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        System.out.println("\t\t Employee Details");
        System.out.println("======================================================");
        System.out.println("Employee ID :  "+emp_id);
        System.out.println("Enployee Name :  "+emp_name);
        System.out.println("Employee Department :  "+emp_department);
        System.out.println("Employee Phone Number :  "+emp_ph);
        System.out.println("Employee Password :  "+emp_password);
        System.out.println("Employee Leave Days Limit :  "+leave_permit);
        System.out.println("Employee Salary Per Hour :  "+salary_hrs);
        System.out.println("Employee Worked : "+ hrs_worked+" hours");
        System.out.println("Employee Total Salary :  $ "+total_salary  );
        System.out.println("=======================================================");
        _showMenu("main");


    }


    private boolean _check_emp(String emp_id) {

        try {
            ResultSet set = statement.executeQuery("select  *  from empdb.emp_details  where emp_id='"+emp_id+ "';");
            if (!set.next()){
                System.out.println("Employee not found...");
                _showMenu("main");
                return false;
            }
            else {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    private void _update_emp(String upd_query) {
        try {
            statement.executeUpdate(upd_query);

            if (upd_query.contains("UPDATE")) {
                System.out.println("Update Successful...");
                _showMenu("update_emp");
            } else if (upd_query.contains("delete")) {
                System.out.println("Employee Removed Successfully.... ");
                _showMenu("main");
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }


    public void _create_employee() {
        scanner.nextLine();



        System.out.println("======================================================");
        System.out.println("\t\t\tCreate Employee");
        System.out.print("Enter Employee Name :  ");
        emp_name = scanner.nextLine();

        System.out.print("Enter Employee Department :  ");
        emp_department = scanner.nextLine();

        System.out.print("Enter Employee Phone Number :  ");
        emp_ph = scanner.nextLine();

        System.out.print("Enter Salary Per Hour :  ");
        emp_salary = scanner.nextLine();
        _validate("save");


    }


    private void _validate(String menu) {

        System.out.print("Create Password : ");
        emp_password = scanner.nextLine();

        System.out.print("Confirm Password : ");
        c_password = scanner.nextLine();
        System.out.println("======================================================");
        System.out.println();
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
            String insert_query = "INSERT INTO `empdb`.`emp_details` (emp_name, emp_dept,emp_ph,emp_password,leave_permit,leave_update,hrs_worked,salary_hrs,total_salary) " + "" +
                    " VALUES ( '" + emp_name + "'  , '" + emp_department + "','" + emp_ph + "','" + emp_password + "' , '3', '"+today+"' , '0', '" +emp_salary+"','0' );";

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


    interface Admin_interface{
        void _Logout_main();
   }
}
