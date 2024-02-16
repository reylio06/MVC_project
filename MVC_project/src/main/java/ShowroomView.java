import java.util.List;

class ShowroomView {
    public void displayVehiclesFromShowroom(List<Vehicle> vehicles){
        System.out.println("Vehicles in Showroom are: ");

        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            System.out.println(i + " MAKE: " + vehicle.getMake() + ", MODEL: " + vehicle.getModel() +
                    ", YEAR: " + vehicle.getYear() + ", PRICE (€): " + vehicle.getPrice());
        }
    }
}
