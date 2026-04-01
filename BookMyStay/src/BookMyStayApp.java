import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

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
    private Map<String, Integer> inventory = new HashMap<>();

    public void registerRoomType(String roomType, int initialCount) {
        inventory.put(roomType, initialCount);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getAllInventory() {
        return new HashMap<>(inventory);
    }
}

class SearchService {
    private RoomInventory inventory;
    private List<Room> definedRooms;

    public SearchService(RoomInventory inventory, List<Room> definedRooms) {
        this.inventory = inventory;
        this.definedRooms = definedRooms;
    }

    public void searchAvailableRooms() {
        System.out.println("--- Available Rooms Search Results ---");
        boolean found = false;

        for (Room room : definedRooms) {
            int availableCount = inventory.getAvailability(room.getType());

            if (availableCount > 0) {
                displayRoomResult(room, availableCount);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms are currently available.");
        }
        System.out.println("---------------------------------------");
    }

    private void displayRoomResult(Room room, int count) {
        System.out.println("Type: " + room.getType() + " [" + count + " left]");
        System.out.println("Price: $" + room.getPricePerNight() + " | Beds: " + room.getNumBeds());
        System.out.println("Features: " + room.getFeatures());
        System.out.println("---");
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        Room single = new SingleRoom();
        Room doubleRm = new DoubleRoom();
        Room suite = new SuiteRoom();

        List<Room> roomCatalog = new ArrayList<>();
        roomCatalog.add(single);
        roomCatalog.add(doubleRm);
        roomCatalog.add(suite);

        RoomInventory inventory = new RoomInventory();
        inventory.registerRoomType(single.getType(), 5);
        inventory.registerRoomType(doubleRm.getType(), 0);
        inventory.registerRoomType(suite.getType(), 2);

        SearchService searchService = new SearchService(inventory, roomCatalog);

        searchService.searchAvailableRooms();
    }
}