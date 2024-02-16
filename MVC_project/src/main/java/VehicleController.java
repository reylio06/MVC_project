import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

class VehicleController {

    // Define constants for file paths
    static final String SHOWROOM_VEHICLES_FILE = "src/main/resources/showroomVehicles.csv";
    static final String BOUGHT_VEHICLES_FILE = "src/main/resources/boughtVehicles.csv";

    // Instance variables
    private final Showroom showroom;
    private final ShowroomView showroomView;
    private final List<Vehicle> boughtVehicles;
    private final FileProcessor fileProcessor;
    static final Logger logger = Logger.getLogger(FileProcessor.class.getName());
    private final User user;

    public VehicleController(Showroom showroom, ShowroomView showroomView) {
        this.showroom = showroom;
        this.showroomView = showroomView;
        this.boughtVehicles = new ArrayList<>();
        // Get user information
        this.user = getUserInput();
        // Initialize file processor
        fileProcessor = new FileProcessor();
    }

    // Method to get user input and create a User object
    private User getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name:");
        String name = scanner.nextLine();

        System.out.println("Enter your age:");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter your account balance (€):");
        double accountBalance = Double.parseDouble(scanner.nextLine());

        return new User(name, age, accountBalance);
    }

    public void addVehicle(String make, String model, int year, double price) throws BusinessException {
        showroom.addVehicle(new Vehicle(make, model, year, price));
        fileProcessor.writeToFile(SHOWROOM_VEHICLES_FILE, getVehicleLines(showroom.getVehicles()));
    }


    public void buyVehicle(int index) throws BusinessException {

        if (index >= 0 && index < showroom.getVehicles().size()) {
            Vehicle vehicle = showroom.getVehicles().get(index);

            // Check if user is at least 18 years old
            if (user.getAge() < 18) {
                logger.info("Sorry, you must be 18 or older to buy a vehicle.");
                return;
            }

            // Check if user has enough balance to buy the vehicle
            if (vehicle.getPrice() > user.getAccountBalance()) {
                logger.info("Sorry, you don't have enough balance to buy this vehicle.");
                return;
            }
            double newBalance = user.getAccountBalance() - vehicle.getPrice();
            user.setAccountBalance(newBalance);

            // Print message indicating the purchase and leftover balance
            System.out.println(user.getName() + " bought the " + vehicle.getMake() + " " + vehicle.getModel() +
                    " from " + vehicle.getYear() + " with " + vehicle.getPrice() + "€");
            System.out.println(user.getName() + " balance is now: " + newBalance);

            // Add bought vehicle to the list of bought vehicles
            boughtVehicles.add(vehicle);

            // Remove the bought vehicle from the showroom's list of vehicles
            showroom.getVehicles().remove(index);

            // Write updated showroom vehicles to file
            fileProcessor.writeToFile(SHOWROOM_VEHICLES_FILE, getVehicleLines(showroom.getVehicles()));

            // Write bought vehicles to file
            fileProcessor.writeToFile(BOUGHT_VEHICLES_FILE, getVehicleLines(boughtVehicles));

        } else {
            logger.info("Invalid index. Please enter a valid index.");
        }
    }

    // Method to handle user input for various operations
    public void handleUserInput() throws BusinessException {
        Scanner scanner = new Scanner(System.in);
        boolean exitLoop = false;
        try {
            while (!exitLoop) {
                System.out.println("Enter 'add' to add a vehicle, 'update' to modify vehicle data," +
                        " 'buy' to purchase a vehicle, 'show' to see available affordable vehicles or 'exit' to quit:");
                String input = scanner.nextLine();

                switch (input.toLowerCase()) {
                    case "add":
                        System.out.println("Enter vehicle details (make,model,year,price):");
                        String vehicleInput = scanner.nextLine();

                        String[] parts = vehicleInput.split(",");

                        if (parts.length == 4) {
                            String make = parts[0];
                            String model = parts[1];
                            int year = Integer.parseInt(parts[2]);
                            double price = Double.parseDouble(parts[3]);
                            addVehicle(make, model, year, price);
                            showroomView.displayVehiclesFromShowroom(showroom.getVehicles());

                        } else {
                            logger.info("Invalid input. Please enter vehicle details in the format: make,model,year,price");
                        }
                        break;

                    case "update":
                        System.out.println("Enter the index of the vehicle to update:");
                        int indexToUpdate = Integer.parseInt(scanner.nextLine());
                        updateVehicle(indexToUpdate);
                        showroomView.displayVehiclesFromShowroom(showroom.getVehicles());
                        break;

                    case "buy":
                        System.out.println("Enter the index of the vehicle to buy:");
                        int index = Integer.parseInt(scanner.nextLine());
                        buyVehicle(index);
                        showroomView.displayVehiclesFromShowroom(showroom.getVehicles());
                        break;

                    case "show":
                        displayAvailableCars();
                        break;

                    case "exit":
                        exitLoop = true;
                        break;

                    default:
                        System.out.println("Invalid input. Please enter 'add', 'buy', or 'exit'.");
                }
            }
        } catch (BusinessException e){
            logger.info(e.getMessage() + " " + e.getOperationName());
        }

    }

    // Method to generate vehicle details as strings for file writing
    private List<String> getVehicleLines(List<Vehicle> vehicles) {
        List<String> lines = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            lines.add(vehicle.getMake() + "," + vehicle.getModel() + "," +
                    vehicle.getYear() + "," + vehicle.getPrice());
        }

        return lines;
    }

    // Method to display available cars within user's budget
    private void displayAvailableCars() {

        double userBalance = user.getAccountBalance();
        List<Vehicle> availableCars = new ArrayList<>();

        for (int i = 0; i < showroom.getVehicles().size(); i++) {
            Vehicle vehicle = showroom.getVehicles().get(i);
            if (vehicle.getPrice() <= userBalance) {
                availableCars.add(vehicle);
                System.out.println(i + " MAKE: " + vehicle.getMake() + ", MODEL: " + vehicle.getModel() +
                        ", YEAR: " + vehicle.getYear() + ", PRICE: " + vehicle.getPrice() + "€.");
            }
        }

        if (availableCars.isEmpty()) {
            System.out.println("Sorry, there are no vehicles available within your budget.");
        }
    }

    // Method to update vehicle details
    public void updateVehicle(int index) throws BusinessException {
        if (index >= 0 && index < showroom.getVehicles().size()) {
        Vehicle vehicle = showroom.getVehicles().get(index);

        // Prompt the user to choose the field to update
        System.out.println("Choose the field to update:");
        System.out.println("1. Make");
        System.out.println("2. Model");
        System.out.println("3. Year");
        System.out.println("4. Price");

        Scanner scanner = new Scanner(System.in);
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                System.out.println("Enter the new make:");
                String newMake = scanner.nextLine();
                vehicle.setMake(newMake);
                break;
            case 2:
                System.out.println("Enter the new model:");
                String newModel = scanner.nextLine();
                vehicle.setModel(newModel);
                break;
            case 3:
                System.out.println("Enter the new year:");
                int newYear = Integer.parseInt(scanner.nextLine());
                vehicle.setYear(newYear);
                break;
            case 4:
                System.out.println("Enter the new price:");
                double newPrice = Double.parseDouble(scanner.nextLine());
                vehicle.setPrice(newPrice);
                break;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 4.");
        }

        // Write updated showroom vehicles to file
        fileProcessor.writeToFile(SHOWROOM_VEHICLES_FILE, getVehicleLines(showroom.getVehicles()));

    } else {
        logger.info("Invalid index. Please enter a valid index.");
    }


    // Uncomment if keeping vehicles from other sessions is desired
//    public void loadShowroomFromCSV(String filename) throws BusinessException {
//        List<String> lines = fileProcessor.readFromFile(filename);
//        for (String line : lines) {
//            String[] parts = line.split(",");
//            if (parts.length == 4) {
//                String make = parts[0];
//                String model = parts[1];
//                int year = Integer.parseInt(parts[2]);
//                double price = Double.parseDouble(parts[3]);
//                showroom.addVehicle(new Vehicle(make, model, year, price));
//            }
//        }
//    }

//    public void loadBoughtVehiclesFromCSV(String filename) throws BusinessException {
//        List<String> lines = fileProcessor.readFromFile(filename);
//        for (String line : lines) {
//            String[] parts = line.split(",");
//            if (parts.length == 4) {
//                String make = parts[0];
//                String model = parts[1];
//                int year = Integer.parseInt(parts[2]);
//                double price = Double.parseDouble(parts[3]);
//                boughtVehicles.add(new Vehicle(make, model, year, price));
//            }
//        }
//    }
}
}
