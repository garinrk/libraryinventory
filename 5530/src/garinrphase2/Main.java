package garinrphase2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.text.*;

/**
 * CS 5530 Database Systems
 * Phase 2 Code [Main.java]
 * <p/>
 * This project represents the Phase 2 of the Spring 2015 semester project, the implementation
 * of a database that would manage a small library.
 *
 * @author Garin Richards
 */


public class Main {

    /* Sooper Sekret Database credentials */
    private static String DBUSER = "cs5530u18";
    private static String DBPASS = "f96qb5pr";
    private static String DBURL = "Jdbc:mysql://georgia.eng.utah.edu/cs5530db18";
    static Statement stmt;


    private static Date today = new Date();
    private static SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy");

    static Scanner in = new Scanner(System.in);
    static String userSelection = null;
    static int choice = 0;

    static boolean verbose = true;
    static Connection c = null;

//	 System.out.println("Today's date is: "+dateFormat.format(date));


    //represents the current user accessing the library database
    static String currentUser = null;

    public static void main(String[] args) {


//        Connection c = null;
        //connect to database
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            c = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
//			System.out.println("Connection established to database");
            stmt = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            Bookshelf.SetConnection(c, stmt);

            //show initial main menu
            Welcome();


//            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Cannot connect to Database Server");
        } finally {
            if (c != null) {
                try {
                    c.close();
                    System.out.println("Database Connection Terminated");
                } catch (Exception e) {
                    /* Do nothing, ignoring close errors */
                }
            }
        }

        ExitProgram();


    }//end of main

    /**
     * Check if the user's input was an integer or not
     *
     * @param i, a string to test if it is an integer
     * @return
     */
    public static boolean IsNumber(String i) {
        try {
            Integer.parseInt(i);
        } catch (NumberFormatException e) {
            //string is not an integer
            return false;
        }
        //string is integer
        return true;
    }

    /**
     * Depending on the integer passed through to the console,
     * the correct information will be prompted and stored to make
     * a query.
     *
     * @param selection, representative of user's choice
     */
    public static void FunctionParse(int selection) {
        //adding a user to the library
        if (selection == 1) {
            Bookshelf.AddUser();
        }

        //checking out a book
        else if (selection == 2) {
            Bookshelf.CheckoutBook(today);
        }

        //prints out record of a specific user
        else if (selection == 3) {
            Bookshelf.PrintUserRecord();
        }

        //add a new book record to the library database
        else if (selection == 4) {
            Bookshelf.AddBookRecord();
        }

        //add a new copy of a book to the library database
        else if (selection == 5) {
            Bookshelf.AddBookCopy();
        }

        //check the late book list at a certain date
        else if (selection == 6) {
            Bookshelf.CheckLateList();
        }

        //leave a review for a book
        else if (selection == 7) {
            Bookshelf.LeaveReview();
        }

        //browse the library
        else if (selection == 8) {
            Bookshelf.BrowseLibrary();
        }

        //return a book to the library
        else if (selection == 9) {
            Bookshelf.ReturnBook();
        }

        //print the book record
        else if (selection == 10) {
            Bookshelf.PrintBookRecord();
        }

        //print library statistics
        else if (selection == 11) {
            Bookshelf.PrintLibraryStatistics();
        }

        //print user statistics
        else if (selection == 12) {
            Bookshelf.PrintUserStatistics();
        } else if (selection == 13) {
            ExitProgram();
        }

        //log out and log in as new user
        else if (selection == 14) {
            //log out by clearing values
            System.out.println("Logging out of " + currentUser);
            System.out.println("Good bye");
            currentUser = null;
            Bookshelf.loggedInUser = null;

            //prompt for new username
            System.out.print("What is your username? : ");


            boolean check;

            currentUser = in.nextLine();

            check = Bookshelf.CheckForUser(currentUser);

            //if the username doesn't exist, prompt user again for existing username
            while (!check) {
                System.out.print(currentUser + " does not exist, please enter a valid username: ");
                currentUser = in.nextLine();
                check = Bookshelf.CheckForUser(currentUser);
            }

            //TODO: Check for if user exists1
            //get new username
//            currentUser = in.nextLine();

            //login as said user name
            Bookshelf.setLoggedInUser(currentUser);

            //display main menu
            MainMenu();
        } else {
            //error checking is done in main menu function
        }
    }


    /**
     * Exits the program
     */
    public static void ExitProgram() {
        System.out.println();
        System.out.println("Thanks for using the Library, Goodbye!");
        try {
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.exit(0);
    }//end of ExitProgram

    public static void Welcome() {

        /* Display welcome message and prompt user for input */
        System.out.println("Welcome to the library!");
        System.out.println("Today's date is: " + ft.format(today));
        System.out.println();
        System.out.print("Are you a new [1] or existing [2] user? Exit with [3]: ");

        do {
            userSelection = in.nextLine();

            if (!IsNumber(userSelection)) {
                System.out.print(userSelection + " is an invalid option, ");
                System.out.print("Please make a selection: ");
            }

            //if the user did enter a number
            if (IsNumber(userSelection)) {
                choice = Integer.parseInt((userSelection));

                //check to see if it's a valid option
                if (choice != 1 && choice != 2 && choice != 3) {
                    System.out.print(choice + " is an invalid option ");
                    System.out.print("please make a selection: ");

                }

                //case where the user did enter a valid option number
                else {
                    break;
                }

            }
        } while (true);


        //add new user to database and show main menu
        if (choice == 1) {
            Bookshelf.AddUser();
            currentUser = Bookshelf.newUsername;
            if (verbose) {
                System.out.println("Current User is " + currentUser);
            }
            Bookshelf.setLoggedInUser(currentUser);
            MainMenu();
        }

        //prompt user for existing username and check if exists
        if (choice == 2) {
            boolean check;

            System.out.print("What is your username? : ");
            currentUser = in.nextLine();

            check = Bookshelf.CheckForUser(currentUser);

            //if the username doesn't exist, prompt user again for existing username
            while (!check) {
                System.out.print(currentUser + " does not exist, please enter a valid username: ");
                currentUser = in.nextLine();
                check = Bookshelf.CheckForUser(currentUser);
            }

            Bookshelf.setLoggedInUser(currentUser);

            MainMenu();
        }
        if (choice == 3) {
            ExitProgram();
        }


    }//end of main


    /**
     * Used to set user context string in menus and greetings
     *
     * @param username
     */
    public static void setLoggedInUser(String username) {
        currentUser = username;

    }//end of SetLoggedInUser

    /**
     * Main menu that prompts the user with choices
     * <p/>
     * These options specify the 13 specified functionalities of the database
     * You can find their pertinent information in their subsequent
     * functions in Bookshelf
     */
    public static void MainMenu() {

        System.out.println("====================================================");
        System.out.println("Hello " + currentUser + " What would you like to do?");
        System.out.println();
        System.out.println("Add a new user [1]");
        System.out.println("Check out a book [2]");
        System.out.println("Print out the record of a specific user [3]");
        System.out.println("Add a new book record to the library database [4]");
        System.out.println("Add a new copy of a book to the library database [5]");
        System.out.println("Check the late book list at a certain date [6]");
        System.out.println("Leave a review for a book [7]");
        System.out.println("Browse the Library [8]");
        System.out.println("Return a book [9]");
        System.out.println("Print a book's record [10]");
        System.out.println("Print Library statistics[11]");
        System.out.println("Print User Statistics [12]");
        System.out.println("Exit Program: [13]");
        System.out.println("Log in as a new user: [14]");
        System.out.println();
        System.out.print("Please make a selection: ");

        do {
            userSelection = in.nextLine();

            if (!IsNumber(userSelection)) {
                System.out.print(userSelection + " is an invalid option, ");
                System.out.print("Please make a selection: ");
            }

            //if the user did enter a number
            if (IsNumber(userSelection)) {
                choice = Integer.parseInt((userSelection));

                //check to see if it's a valid option
                if (choice < 1 || choice > 14) {
                    System.out.print(choice + " is an invalid option ");
                    System.out.print("please make a selection: ");

                }

                //case where the user did enter a valid option number
                else {
                    break;
                }

            }
        } while (true);


        //send the user to the correct function
        FunctionParse(choice);

    }//end of MainMenu function
}//end of class
