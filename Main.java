import java.util.*;
import java.sql.*;
import java.time.*;

public class Main {

    public static int pointCount = 8;           
    public static int[] distanceTaken = new int[] {10, 20, 10, 35, 65, 15, 10};
    public static int[] timeTaken = new int[]     {60 * 10, 60 * 10, 60 * 10, 60 * 15, 60 * 15, 60 * 10, 60 * 05 + 1};

    public static int taxiCount = 4, initialDistance = 5, initialCharge = 50, usualCharge = 100, minimumDistance = 30;

    public static Customer[] customers = new Customer[] {new Customer("Navin", 3, 8, "06:20:01"),
                                                         new Customer("Rajni", 8, 3, "07:35:01"),
                                                         new Customer("Vijay", 3, 1, "08:30:01"),
                                                         new Customer("Ajith", 1, 8, "08:50:01"),
                                                         new Customer("Jack", 2, 7, "10:05:01"),
                                                         new Customer("Ben", 1, 2, "13:20:00"),
                                                         new Customer("Sundar", 8, 1, "17:20:59"),
                                                         new Customer("Boss", 7, 2, "23:00:01"),
                                                         new Customer("Justin", 4, 5, "23:00:02")};

    public static void main(String... args) throws Exception {

        City city = new City(Main.pointCount, Main.distanceTaken, Main.timeTaken);

        Office office = new Office(city, Main.taxiCount, Main.initialDistance, Main.initialCharge, Main.usualCharge, Main.minimumDistance) {{
            this.addCustomers(Main.customers);}};

        new Thread(office).start();

    }
    
}