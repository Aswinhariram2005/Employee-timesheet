import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class Manager {

    private Scanner scanner = new Scanner(System.in);
    private String emp_id, emp_pass;
    private Statement statement;
    private Manager_interface manager_interface;
    private String today, time, day;

    public Manager(Statement statement, Manager_interface manager_interface) {
        this.statement = statement;
        this.manager_interface = manager_interface;
        _date();
        _Login();
    }

    private void _showMenu(String hint) {
        if (hint.equals("main")) {
            System.out.println();
            System.out.println("Welcome to Manager Page");
            System.out.println("1. Check Timesheet");
            System.out.println("2. Employee Details");
            System.out.println("3. Check Leave Request");
            System.out.println("4. Check Salary Request");
            System.out.println("5. Logout");
            System.out.println();
            _decider("main");
        } else if (hint.equals("leave")) {
            System.out.println();
            System.out.println("1.Select Request");
            System.out.println("2.Exit");
            System.out.println();
            _decider("leave");
        }
        else if (hint.equals("salary")) {
            System.out.println();
            System.out.println("1.Select Request");
            System.out.println("2.Exit");
            System.out.println();
            _decider("salary");
        }
    }

    private void _Login() {
        System.out.println();
        System.out.print("Enter Employee id : ");
        emp_id = scanner.nextLine();
        System.out.print("Enter Password : ");
        emp_pass = scanner.nextLine();

        String query = "select * from emp_details where emp_id = '" + emp_id + "' and emp_password = '" + emp_pass + "'; ";
        _execute_query(query, "login", emp_id);

    }


    private void _decider(String hint) {

        System.out.print("Enter your option : ");
        String choice = scanner.nextLine();

        if (hint.equals("main")) {
            switch (choice) {
                case "1":
                    //  CHECK TIME SHEET
                    _checkTimeSheet();
                    break;
                case "2":
                    //   CHECK EMP_DETAILS
                    _showAllEmpDetails();
                    break;
                case "3":
                    //  APPROVE LEAVE
                    _showAllLeaveRequest();
                    break;
                case "4":
                    //  APPROVE SALARY
                    _showAllSalaryRequest();
                    break;
                case "5":
                    // LOGOUT
                    System.out.println("Logout Successful...");
                    manager_interface.logout();
                    break;
                default:
                    System.out.println("Enter valid option....");
                    _showMenu("main");
            }
        } else if (hint.equals("leave")) {
            switch (choice) {
                case "1":
                    //SELECT REQUEST
                    _approveLeave();
                    break;
                case "2":
                    // EXIT
                    _showMenu("main");
                    break;
                default:
                    System.out.println("Enter valid option...");
                    _decider("leave");
            }
        }
        else if (hint.equals("salary")) {
            switch (choice) {
                case "1":
                    //SELECT REQUEST

                    _approveSalary();
                    break;
                case "2":
                    // EXIT
                    _showMenu("main");
                    break;
                default:
                    System.out.println("Enter valid option...");
                    _decider("leave");
            }
        }
    }

    private void _showAllEmpDetails() {
        String query = "select * from emp_details";
        _execute_query(query,"emp",emp_id);
    }

    private void _approveSalary() {
        System.out.println();
        System.out.print("Choose id:  ");
        String id = scanner.nextLine();
        int amount_req = 0;

        try {
            ResultSet set = statement.executeQuery("select * from req_salary where emp_id = '"+id+"' and date= '"+today+"';");
            while (set.next()){
                amount_req = Integer.parseInt(set.getString("amount_req"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.print("Do you want to grant (Y/N) : ");
        String opt = scanner.nextLine();
        if (opt.equals("Y")||opt.equals("y")){
            // GRANTED

            String upt_salary = "update emp_details set total_salary = total_salary - '"+amount_req+"' where emp_id = '"+id+"';";
            try {
                statement.executeUpdate(upt_salary);
                String query = "update req_salary set status = 'granted' where emp_id= '"+id+"' and date = '"+today+"' ;";
                statement.executeUpdate(query);
                System.out.println("Request approved successfully...");
                _showMenu("main");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else{
            //REJECTED
            String query = "update req_salary set status = 'rejected' where emp_id= '"+id+"' and date = '"+today+"' ;";
            try {
                statement.executeUpdate(query);
                System.out.println("Request rejected successfully...");
                _showMenu("main");
            } catch (Exception e) {
                System.out.println(e);
            }
        }



    }

    private void _showAllSalaryRequest() {
        String query = "select * from req_salary where status = 'pending' and date = '" + today + "' ;";
        _execute_query(query,"salary",emp_id);
    }

    private void _approveLeave() {
        System.out.print("Choose ID : ");
        String id = scanner.nextLine();
        String ch_query = "select * from emp_details where emp_id = '" + id + "';";
        try {
            ResultSet set = statement.executeQuery(ch_query);
            while (set.next()) {
                String leave_left = set.getString("leave_permit");
                System.out.println("Leaves Remaining for the Employee : " + leave_left);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.print("Do you want to grant leave (Y/N) : ");
        String ch = scanner.nextLine();
        if (ch.equals("Y") || ch.equals("y")) {
            // GRANTED
            String grant_query = "update leavesheet set status = 'granted' where emp_id = '" + id + "' and app_date= '" + today + "' ;";
            _execute_query(grant_query, "grant", id);

            _showAllLeaveRequest();

        } else {
            // REJECTED
            String rej_query = "update leavesheet set status = 'rejected' where emp_id = '" + id + "' and app_date= '" + today + "' ;";
            _execute_query(rej_query, "rej", id);
            _showAllLeaveRequest();
        }
    }

    private void _showAllLeaveRequest() {
        String query = "select * from leavesheet where status = 'pending' and app_date = '" + today + "' ;";
        _execute_query(query, "leavesheet", emp_id);

    }

    private void _checkTimeSheet() {
        String query = "select * from timesheet";
        _execute_query(query, "timesheet", emp_id);
    }

    private void _execute_query(String query, String hint, String id) {
        if (hint.equals("login")) {
            try {
                ResultSet set = statement.executeQuery(query);
                if (!set.next()) {
                    System.out.println("Employee not found...");
                    manager_interface.logout();
                } else {
                    String dept = set.getString("emp_dept");
                    if (dept.equals("Manager")) {
                        //  MANAGER
                        _showMenu("main");
                    } else {
                        System.out.println("Only Manager can Enter.... ");
                        manager_interface.logout();
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else if (hint.equals("timesheet")) {
            try {
                ResultSet set = statement.executeQuery(query);

                System.out.println("Emp_id date day IN OUT Total_Hours");
                while (set.next()) {
                    String
                            emp_id = set.getString("emp_id"),
                            date = set.getString("date"),
                            day = set.getString("day"),
                            emp_in = set.getString("emp_in"),
                            emp_out = set.getString("emp_out"),
                            total_hrs = set.getString("total_hrs");
                    System.out.println(emp_id + " " + date + " " + emp_in + " " + emp_out + " " + total_hrs);
                }
                _exit();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else if (hint.equals("leavesheet")) {

            System.out.println();


            try {
                ResultSet set = statement.executeQuery(query);
                if (!set.next()){
                    System.out.println("No Record found");
                    _showMenu("main");
                }
                else {

                    System.out.println("Emp_ID Application_Date Application_Day Reason Status");
                    do {
                        String
                                emp_id = set.getString("emp_id"),
                                date = set.getString("app_date"),
                                day = set.getString("app_day"),
                                reason = set.getString("reason"),
                                status = set.getString("status");
                        System.out.println(emp_id + " " + date + " " + day + " " + reason + " " + status);
                    }
                    while (set.next()) ;
                    System.out.println();
                    _showMenu("leave");
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else if (hint.equals("grant")) {
            try {
                statement.executeUpdate(query);
                String reduce_query = "update emp_details set leave_permit = leave_permit - 1 where emp_id = '" + id + "' and leave_permit !=0 ;";
                statement.executeUpdate(reduce_query);
                System.out.println("Approved Successfully....");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else if (hint.equals("rej")) {
            try {
                statement.executeUpdate(query);
                System.out.println("Rejected Successfully....");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else if (hint.equals("salary")) {
            try {
                ResultSet set = statement.executeQuery(query);
                if (!set.next()){
                    System.out.println("No Request found...");
                    _showMenu("main");
                }
                else {
                    System.out.println();
                    System.out.println("Emp_id Application_Date Application_Day Amount_Requested Reason Status");
                    do {
                        String
                                emp_id = set.getString("emp_id"),
                                app_date = set.getString("date"),
                                app_day = set.getString("day"),
                                amount_req = set.getString("amount_req"),
                                reason = set.getString("reason"),
                                status = set.getString("status");
                        System.out.println(emp_id+" " +app_date+" "+app_day+" "+amount_req+" " +reason+" "+status);

                    }while (set.next());
                    _showMenu("salary");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else if (hint.equals("emp")){
            try {
                ResultSet set = statement.executeQuery(query);
                if (!set.next()){
                    System.out.println("No record found....");
                    _showMenu("main");
                }
                else {
                    System.out.println();
                    System.out.println("Emp_ID Emp_Name Emp_Department Emp_Phone_Number Leave_permit Hours_worked Salary/Hr Salary_Earned");
                    do {
                        String
                                emp_id = set.getString("emp_id"),
                                emp_name = set.getString("emp_name"),
                                emp_dept = set.getString("emp_dept"),
                                emp_ph = set.getString("emp_ph"),
                                leave_permit = set.getString("leave_permit"),
                                hrs_worked = set.getString("hrs_worked"),
                                salary_hrs = set.getString("salary_hrs"),
                                total_salary = set.getString("total_salary");
                        System.out.println(emp_id+" "+emp_name+" "+emp_dept+" "+emp_ph+" "+leave_permit+" "+hrs_worked+" "+salary_hrs+" "+total_salary);

                    }while (set.next());
                    _showMenu("main");
                }
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }

    private void _date() {
        Date date = new Date();
        SimpleDateFormat date_formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm aa");
        LocalDate local_day = LocalDate.now();
        DayOfWeek dayOfWeek = local_day.getDayOfWeek();


        today = date_formatter.format(date);
        time = time_formatter.format(date);
        day = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
    }

    private void _exit() {
        System.out.println();
        System.out.print("Do you want to exit press (Y/y): ");
        String choice = scanner.nextLine();
        if (choice.equals("Y") || choice.equals("y")) {
            _showMenu("main");
        } else {
            System.out.println("Enter valid option....");
            _exit();
        }
    }

    interface Manager_interface {
        void logout();
    }
}
