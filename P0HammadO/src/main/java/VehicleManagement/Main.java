package VehicleManagement;

import VehicleManagement.*;

import java.util.Scanner;
import java.util.List;

public class Main {

    static VehiclesRepository repo = new VehiclesRepository();
    static InventoryService service = new InventoryService(repo);
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Vehicle Inventory Management System ===\n");

        if (repo.getAllVehicles().isEmpty()) {
            System.out.print("No data found. Load sample data? (Y/N): ");
            String response = scanner.nextLine().trim().toUpperCase();
            if (response.equals("Y")) {
                seedData();
            }
        }

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": addVehicle(); break;
                case "2": viewAllVehicles(); break;
                case "3": processIncoming(); break;
                case "4": sendToShop(); break;
                case "5": recordMovement(); break;
                case "6": viewOutgoing(); break;
                case "7": viewMovements(); break;
                case "8": viewUsers(); break;
                case "9":
                    running = false;
                    System.out.println("See ya later!");
                    break;
                default: System.out.println("Invalid option");
            }
        }
    }

    static void printMenu() {
        System.out.println("\n========================================");
        System.out.println("1. Add New Vehicle");
        System.out.println("2. View All Vehicles");
        System.out.println("3. Process Incoming Vehicle");
        System.out.println("4. Send Vehicle to Shop");
        System.out.println("5. Record Vehicle Movement");
        System.out.println("6. View Outgoing Inventory");
        System.out.println("7. View Movement History");
        System.out.println("8. View All Users");
        System.out.println("9. Exit");
        System.out.println("========================================");
        System.out.print("Select option: ");
    }

    static void addVehicle() {
        System.out.print("Enter VIN (17 characters): ");
        String vin = scanner.nextLine().trim();
        System.out.print("Enter Make: ");
        String make = scanner.nextLine().trim();
        System.out.print("Enter Model: ");
        String model = scanner.nextLine().trim();
        System.out.print("Enter Year: ");
        int year = Integer.parseInt(scanner.nextLine().trim());

        Vehicle v = service.addVehicle(vin, make, model, year);
        if (v != null) {
            System.out.println("Vehicle added: " + v);
        }
    }

    static void viewAllVehicles() {
        System.out.println("\n--- All Vehicles ---");
        List<Vehicle> vehicles = service.getAllVehicles();
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found");
        } else {
            for (Vehicle v : vehicles) {
                System.out.println(v);
            }
        }
    }

    static void processIncoming() {
        System.out.print("Enter VIN: ");
        String vin = scanner.nextLine().trim();
        System.out.print("Describe any issues (or type N/A if none): ");
        String issues = scanner.nextLine().trim();

        IncomingInventory incoming = service.processIncoming(vin, issues);
        if (incoming != null && !incoming.hasNoIssues()) {
            System.out.print("Send to shop now? (Y/N): ");
            String response = scanner.nextLine().trim().toUpperCase();
            if (response.equals("Y")) {
                sendToShopWithVin(vin, issues);
            }
        }
    }

    static void sendToShop() {
        System.out.print("Enter VIN: ");
        String vin = scanner.nextLine().trim();
        System.out.print("Describe the issue: ");
        String issues = scanner.nextLine().trim();
        sendToShopWithVin(vin, issues);
    }

    static void sendToShopWithVin(String vin, String issues) {
        System.out.println("\nSelect shop type:");
        System.out.println("1. Bodyshop");
        System.out.println("2. Mechanic Shop");
        System.out.println("3. PDR");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();

        String shopType;
        switch (choice) {
            case "1": shopType = "Bodyshop"; break;
            case "2": shopType = "Mechanic Shop"; break;
            case "3": shopType = "PDR"; break;
            default:
                System.out.println("Invalid choice");
                return;
        }

        service.sendToShop(vin, issues, shopType);
    }

    static void recordMovement() {
        System.out.println("\n--- Available Users ---");
        for (User u : service.getAllUsers()) {
            System.out.println(u);
        }

        System.out.print("\nEnter your User ID: ");
        int userId = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter VIN of vehicle to move: ");
        String vin = scanner.nextLine().trim();

        System.out.println("\nSelect destination:");
        System.out.println("1. REVauction");
        System.out.println("2. COGauction");
        System.out.println("3. Metroauction");
        System.out.println("4. Audit");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();

        String destination;
        switch (choice) {
            case "1": destination = "REVauction"; break;
            case "2": destination = "COGauction"; break;
            case "3": destination = "Metroauction"; break;
            case "4": destination = "Audit"; break;
            default:
                System.out.println("Invalid choice");
                return;
        }

        double gasCharge = 0;
        if (!destination.equals("Audit")) {
            System.out.print("Enter gas charge: $");
            gasCharge = Double.parseDouble(scanner.nextLine().trim());
        }

        service.recordMovement(vin, userId, destination, gasCharge);
    }

    static void viewOutgoing() {
        System.out.println("\n--- Outgoing Inventory ---");
        List<OutgoingInventory> outgoing = service.getAllOutgoing();
        if (outgoing.isEmpty()) {
            System.out.println("No outgoing vehicles");
        } else {
            for (OutgoingInventory o : outgoing) {
                System.out.println(o);
            }
        }
    }

    static void viewMovements() {
        System.out.println("\n--- Movement History ---");
        List<InventoryMovements> movements = service.getAllMovements();
        if (movements.isEmpty()) {
            System.out.println("No movements recorded");
        } else {
            for (InventoryMovements m : movements) {
                User user = repo.getUser(m.getUserId());
                String userName = (user != null) ? user.getName() : "Unknown";
                System.out.println(m + " | By: " + userName);
            }
        }
    }

    static void viewUsers() {
        System.out.println("\n--- All Users ---");
        List<User> users = service.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found");
        } else {
            for (User u : users) {
                double earnings = service.calculateUserEarnings(u.getUserId());
                System.out.println(u + " | Total Gas: $" + earnings);
            }
        }
    }

    static void seedData() {
        System.out.println("Loading sample data...");

        repo.saveUser(new User(1, "John Smith", "Driver"));
        repo.saveUser(new User(2, "Jane Doe", "Senior Driver"));
        repo.saveUser(new User(3, "Mike Johnson", "Driver"));
        repo.saveUser(new User(4, "Michael Jordan", "Manager"));

        repo.saveVehicle(new Vehicle("1HGBH41JXMN109186", "Honda", "Accord", 2020));
        repo.saveVehicle(new Vehicle("2HGBH41JXMN109187", "Toyota", "Camry", 2021));
        repo.saveVehicle(new Vehicle("3HGBH41JXMN109188", "Ford", "F-150", 2022));
        repo.saveVehicle(new Vehicle("4HGBH41JXMN109189", "Chevrolet", "Silverado", 2019));
        repo.saveVehicle(new Vehicle("5HGBH41JXMN109190", "BMW", "X5", 2023));

        repo.saveIncoming(new IncomingInventory(1, "1HGBH41JXMN109186", "N/A"));
        repo.saveIncoming(new IncomingInventory(2, "2HGBH41JXMN109187", "Front bumper damage"));

        repo.saveOutgoing(new OutgoingInventory(1, "2HGBH41JXMN109187", "Front bumper damage", "Bodyshop"));

        repo.saveMovement(new InventoryMovements(1, "1HGBH41JXMN109186", 1, "REVauction", 15.50));
        repo.saveMovement(new InventoryMovements(2, "3HGBH41JXMN109188", 2, "COGauction", 18.75));
        repo.saveMovement(new InventoryMovements(3, "4HGBH41JXMN109189", 1, "Metroauction", 22.00));

        System.out.println("Sample data loaded!\n");
    }
}
