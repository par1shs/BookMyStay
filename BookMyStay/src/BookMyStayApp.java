import java.util.*;

abstract class Room {
    private String type;
    private double pricePerNight;

    public Room(String type, double pricePerNight) {
        this.type = type;
        this.pricePerNight = pricePerNight;
    }

    public String getType() { return type; }
    public double getPricePerNight() { return pricePerNight; }
}

class SingleRoom extends Room {
    public SingleRoom() { super("Single Room", 100.00); }
}

class Service {
    private String name;
    private double cost;

    public Service(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() { return name; }
    public double getCost() { return cost; }

    @Override
    public String toString() { return name + " ($" + cost + ")"; }
}

class AddOnServiceManager {
    private Map<String, List<Service>> reservationAddOns = new HashMap<>();

    public void addService(String reservationId, Service service) {
        reservationAddOns.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
    }

    public double calculateTotalAddOnCost(String reservationId) {
        List<Service> services = reservationAddOns.getOrDefault(reservationId, Collections.emptyList());
        double total = 0;
        for (Service s : services) {
            total += s.getCost();
        }
        return total;
    }

    public List<Service> getServices(String reservationId) {
        return reservationAddOns.getOrDefault(reservationId, Collections.emptyList());
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        String resId1 = "S-105";
        String resId2 = "D-102";

        Service wifi = new Service("Premium WiFi", 15.00);
        Service breakfast = new Service("Buffet Breakfast", 25.00);
        Service spa = new Service("Spa Access", 50.00);

        serviceManager.addService(resId1, wifi);
        serviceManager.addService(resId1, breakfast);
        serviceManager.addService(resId2, spa);

        displayReceipt(resId1, serviceManager);
        displayReceipt(resId2, serviceManager);
    }

    public static void displayReceipt(String resId, AddOnServiceManager manager) {
        System.out.println("Reservation ID: " + resId);
        System.out.println("Services: " + manager.getServices(resId));
        System.out.println("Total Add-on Cost: $" + manager.calculateTotalAddOnCost(resId));
        System.out.println("-----------------------------------");
    }
}