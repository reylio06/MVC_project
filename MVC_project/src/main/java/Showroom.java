import java.util.List;
import java.util.ArrayList;

public class Showroom {
    private final List<Vehicle> vehicles;

    public Showroom(){
        vehicles = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle){
        vehicles.add(vehicle);
    }

    public List<Vehicle> getVehicles(){
        return vehicles;
    }
}
