import java.util.Scanner;

public class Main {
    private  static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Welcome to ...  Management");
        System.out.println("1.Admin");
        System.out.println("2.Manager");
        System.out.println("3.Employee");


        _Main();

    }

    private static void _Main() {

        System.out.print("Enter you position : ");
        int choice = scanner.nextInt();
        switch (choice){
            case 1:

                Admin admin = new Admin();
                admin._connDB();
                admin._showMenu("main");

                break;
            case 2:
                //TODO MANAGER
                break;
            case 3:
                //TODO EMPLOYEE
                break;
            default:
                System.out.println("Enter valid details...");
                _Main();

        }


    }
}