import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class Employee {

    private Scanner scanner = new Scanner(System.in);
    private String emp_id, emp_password;
    private Statement statement;
    private Employee_interface employee_interface;
    private String day, today, time;
    private String work;

    public Employee(Statement statement, Employee_interface employee_interface) {
        this.statement = statement;
        this.employee_interface = employee_interface;
        _date();
        System.out.println();
        System.out.println("Welcome to Employee page");
        System.out.println("======================================================");
        System.out.println();

        _show_EmpMenu("main");
    }


    private void _show_EmpMenu(String menu) {
        if (menu.equals("main")) {
            System.out.println("\t\t LOGIN ");
            System.out.println("======================================================");
            System.out.print("Enter  Employee ID  :  ");
            emp_id = scanner.nextLine();
            System.out.print("Enter Employee Password :  ");
            emp_password = scanner.nextLine();

            _check_Emp("details");
        } else if (menu.equals("timesheet")) {
            System.out.println();
            System.out.println("Employee Menu");
            System.out.println("======================================================");
            System.out.println("1. Attendence");
            System.out.println("2. Apply for Leave");
            System.out.println("3. Check Leave Application Status");
            System.out.println("4. Request Salary");
            System.out.println("5. Logout");
            System.out.println("======================================================");
            _decider("timesheet");
        } else if (menu.equals("attendence")) {
            System.out.println();
            System.out.println("TimeSheet Menu");
            System.out.println("======================================================");
            System.out.println("1. In");
            System.out.println("2. Out");
            System.out.println("3. Exit");
            System.out.println("======================================================");
            _decider("attendence");
        }
    }

    private void _decider(String menu) {
        System.out.println();
        System.out.print("Enter option : ");
        int opt = scanner.nextInt();
        if (menu.equals("timesheet")) {
            switch (opt) {
                case 1:
                    // ATTENDENCE
                    _show_EmpMenu("attendence");
                    break;
                case 2:
                    // APPLY LEAVE
                    _check_Emp("leave");

                    break;
                case 3:
                    // APPLY LEAVE
                    _check_status();

                    break;
                case 4:
                    // REQ SALARY
                    break;
                case 5:
                    //TODO EXIT
                    break;
                default:
                    System.out.println("Enter valid option...");
                    _decider("timesheet");

            }
        } else if (menu.equals("attendence")) {

            _date();


            switch (opt) {
                case 1:
                    //IN

                    String f_in_query = "select * from empdb.timesheet where emp_id= '" + emp_id + "' and date= '" + today + "' ; ";
                    try {
                        ResultSet set = statement.executeQuery(f_in_query);
                        if (!set.next()) {
                            String in_query = "insert into empdb.timesheet(emp_id,date,day,emp_in,emp_out,total_hrs) values ('" + emp_id + "','" + today + "','" + day + "','" + time + "', '0' , '0'); ";
                            _exe_query(in_query);
                        } else {
                            System.out.println("Employee already arrived....");
                            _show_EmpMenu("timesheet");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    break;
                case 2:
                    // OUT
                    String f_out_query = "select * from empdb.timesheet where emp_id= '" + emp_id + "' and date= '" + today + "' ; ";
                    try {
                        ResultSet set = statement.executeQuery(f_out_query);

                        if (!set.next()) {
                            System.out.println();
                            System.out.println("Employee not yet arrived");
                            _show_EmpMenu("attendence");
                        } else {


                            String out_time = set.getString("emp_out");
                            if (!out_time.equals("0")) {
                                System.out.println();
                                System.out.println("Employee already went...");
                                _show_EmpMenu("timesheet");
                            } else {
                                String in_time = set.getString("emp_in");
                                int in_hrs_sub = Integer.parseInt(in_time.substring(0, 2));
                                int out_hrs_sub = Integer.parseInt(time.substring(0, 2));


                                int in_min_sub = Integer.parseInt(in_time.substring(3, 5));
                                int out_min_sub = Integer.parseInt(time.substring(3, 5));

                                LocalTime startTime = LocalTime.of(in_hrs_sub, in_min_sub, 0);
                                LocalTime endTime = LocalTime.of(out_hrs_sub, out_min_sub, 0);
                                Duration duration = Duration.between(startTime, endTime);

                                long hours = duration.toHours();
                                long minutes = duration.toMinutesPart();

                                if (minutes >= 30) {
                                    work = String.valueOf(hours) + ".5";
                                } else {
                                    work = String.valueOf(hours);
                                }

                                System.out.println();
                                System.out.println("You worked for " + work + " hours");


                                String out_query = "update empdb.timesheet set emp_out = '" + time + "' ,  total_hrs = '" + work + "' where emp_id = '" + emp_id + "' and `date` = '" + today + "' ;";
                                _exe_query(out_query);
                            }


                        }


                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
                case 3:
                    // EXIT
                    _show_EmpMenu("timesheet");
                    break;
                default:
                    System.out.println();
                    System.out.println("Enter valid option...");
                    _decider("attendence");
            }
        }


    }

    private void _check_status() {
        String leave_check_query = "select * from leavesheet where emp_id = '" + emp_id + "' and app_date+ '" + today + "' ;";
        try {
            ResultSet set = statement.executeQuery(leave_check_query);
            if (!set.next()){
                System.out.println("Leave not yet applied...");

            }
            else {
                String status = set.getString("status");
                System.out.println("Leave Status : "+status);
            }
            _show_EmpMenu("timesheet");
        } catch (Exception e) {
            System.out.println(e);
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

    private void _exe_query(String query) {
        if (query.contains("insert")) {
            try {
                statement.executeUpdate(query);
                System.out.println("Attendence In time updated successfully...");
                _show_EmpMenu("timesheet");
            } catch (Exception e) {
                System.out.println(e);
            }
        } else if (query.contains("update")) {
            try {
                statement.executeUpdate(query);
                System.out.println("Attendence Out time updated successfully...");
                _update_salary();
                _show_EmpMenu("timesheet");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void _update_salary() {
        String sal_hrs_query  = "select salary_hrs from emp_details where emp_id = '"+emp_id+"' ;";
        try {
            ResultSet set = statement.executeQuery(sal_hrs_query);
            if (set.next()){
                int sal_hrs = Integer.parseInt(set.getString("salary_hrs"));
                int salary = Integer.parseInt(work) * sal_hrs;
                System.out.println("salary Earned Today = "+salary);
                String sal_query = "update emp_details set hrs_worked = '"+work+"' ,total_salary= '"+String.valueOf(salary)+"' ;";
                statement.executeUpdate(sal_query);
                System.out.println("Salary Added Successfully");

            }
        } catch (Exception e) {
            System.out.println(e);
        }


    }

    private void _check_Emp(String ch) {
        if (ch.equals("details")) {
            try {
                ResultSet set = statement.executeQuery("select  *  from empdb.emp_details  where emp_id='" + emp_id + "' and emp_password='" + emp_password + "';");
                if (set.next()) {

                    // LOGIN SUCCESSFUL
                    _show_EmpMenu("timesheet");
                } else {
                    // LOGIN FAILED
                    System.out.println("Employee Not Found");
                    employee_interface.logout();

                }

            } catch (Exception e) {
                System.out.println(e);
            }
        } else if (ch.equals("leave")) {
            String ch_query = "select * from empdb.timesheet where emp_id= '" + emp_id + "' and date= '" + today + "' ;";
            try {
                ResultSet set = statement.executeQuery(ch_query);
                if (!set.next()) {
                    // CHECK ARRIVED OR NOT
                    String leave_check_query = "select * from leavesheet where emp_id = '" + emp_id + "' and app_date+ '" + today + "' ;";
                    ResultSet set1 = statement.executeQuery(leave_check_query);
                    if (!set1.next()) {
                        // APPLY LEAVE
                        _applyLeave();
                    } else {
                        // DISPLAY LEAVE STATUS
                        System.out.println("Leave already applied...");
                        _show_EmpMenu("timesheet");
                    }
                } else {
                    System.out.println("Employee already arrived...");
                }
            } catch (Exception e) {
                System.out.println(e);
            }


        }
    }

    private void _applyLeave() {
        System.out.println();
        scanner.nextLine();

        System.out.print("Enter Reason : ");
        String reason = scanner.nextLine();

        String leave_query = "insert into leavesheet(emp_id,app_date,app_day,reason,status) values ( '" + emp_id + "' ,'" + today + "','" + day + "','" + reason + "','pending' );";
        try {
            statement.executeUpdate(leave_query);
            System.out.println("Leave Application Successfully applied....");
            _show_EmpMenu("timesheet");
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    interface Employee_interface {
        void logout();
    }
}
