public class Main {
    public static void main(String[] args) throws BusinessException {

        // Create instances of showroom, showroom view, and vehicle controller
        Showroom showroom = new Showroom();
        ShowroomView showroomView = new ShowroomView();
        VehicleController vehicleController = new VehicleController(showroom, showroomView);

        // Display vehicles in the showroom
        showroomView.displayVehiclesFromShowroom(showroom.getVehicles());

        try {
            // Handle user input for vehicle operations
            vehicleController.handleUserInput();
        } catch (BusinessException e) {
            throw new BusinessException(e.getMessage(), "handleUserInput");
        }

        // Uncomment if keeping vehicles from other sessions is desired
//        try {
//            vehicleController.loadShowroomFromCSV(VehicleController.SHOWROOM_VEHICLES_FILE);
//            vehicleController.loadBoughtVehiclesFromCSV(VehicleController.BOUGHT_VEHICLES_FILE);
//        } catch (BusinessException e) {
//            throw new BusinessException(e.getMessage(), "loadData");
//        }
//
    }
}