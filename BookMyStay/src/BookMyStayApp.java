import java.util.HashMap;
import java.util.Map;

abstract class Room {
    private String type;
    private int numBeds;
    private double pricePerNight;

    public Room(String type, int numBeds, double pricePerNight) {
        this.type = type;
        this.numBeds = numBeds;
        this.pricePerNight = pricePerNight;
    }

    public String getType() { return type; }
    public int getNumBeds() { return numBeds; }
    public double getPricePerNight() { return pricePerNight; }

    public abstract String getFeatures();
}

class SingleRoom extends Room {
    public SingleRoom() { super("Single Room", 1, 100.00); }
    @Override
    public String getFeatures() { return "Compact space, high-speed WiFi, Desk."; }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super("Double Room", 2, 180.00); }
    @Override
    public String getFeatures() { return "Queen size beds, Mini-fridge, City view."; }
}

class SuiteRoom extends Room {
    public SuiteRoom() { super("Suite", 2, 350.00); }
    @Override
    public String getFeatures() { return "King beds, Living area, Complimentary Spa access."; }
}

class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        this.inventory = new HashMap<>();
    }

    public void registerRoomType(String roomType, int initialCount) {
        inventory.put(roomType, initialCount);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, Math.max(0, newCount));
        }
    }

    public void displayFullInventory() {
        System.out.println("--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
        }
        System.out.println("------------------------------");
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        Room single = new SingleRoom();
        Room doubleRm = new DoubleRoom();
        Room suite = new SuiteRoom();

        RoomInventory inventoryManager = new RoomInventory();

        inventoryManager.registerRoomType(single.getType(), 10);
        inventoryManager.registerRoomType(doubleRm.getType(), 5);
        inventoryManager.registerRoomType(suite.getType(), 2);

        System.out.println("Initial State:");
        inventoryManager.displayFullInventory();

        inventoryManager.updateAvailability(single.getType(), 8);
        inventoryManager.updateAvailability(suite.getType(), 0);

        System.out.println("Updated State (After Bookings):");
        inventoryManager.displayFullInventory();

        System.out.println("Checking Double Room Specifically: " +
                inventoryManager.getAvailability("Double Room"));
    }
}