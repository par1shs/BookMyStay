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

class DoubleRoom extends Room {
    public DoubleRoom() { super("Double Room", 180.00); }
}

class ReservationRequest {
    private String guestName;
    private String roomType;

    public ReservationRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

class InventoryService {
    private Map<String, Integer> availability = new HashMap<>();
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    public void registerRoomType(String type, int count) {
        availability.put(type, count);
        allocatedRooms.put(type, new HashSet<>());
    }

    public boolean hasAvailability(String type) {
        return availability.getOrDefault(type, 0) > 0;
    }

    public String allocateRoom(String type) {
        int currentCount = availability.get(type);
        String roomId = type.substring(0, 1).toUpperCase() + "-" + (100 + currentCount);

        availability.put(type, currentCount - 1);
        allocatedRooms.get(type).add(roomId);

        return roomId;
    }

    public void displayStatus() {
        System.out.println("\n--- Final Inventory & Allocation Status ---");
        for (String type : availability.keySet()) {
            System.out.println(type + " | Left: " + availability.get(type) +
                    " | Assigned: " + allocatedRooms.get(type));
        }
    }
}

class BookingService {
    private InventoryService inventory;
    private Queue<ReservationRequest> queue;

    public BookingService(InventoryService inventory, Queue<ReservationRequest> queue) {
        this.inventory = inventory;
        this.queue = queue;
    }

    public void processRequests() {
        System.out.println("--- Processing Booking Queue ---");
        while (!queue.isEmpty()) {
            ReservationRequest request = queue.poll();
            String type = request.getRoomType();

            if (inventory.hasAvailability(type)) {
                String assignedId = inventory.allocateRoom(type);
                System.out.println("CONFIRMED: " + request.getGuestName() +
                        " assigned to " + assignedId);
            } else {
                System.out.println("REJECTED: No availability for " + request.getGuestName() +
                        " (" + type + ")");
            }
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        inventory.registerRoomType("Single Room", 2);
        inventory.registerRoomType("Double Room", 1);

        Queue<ReservationRequest> requestQueue = new LinkedList<>();
        requestQueue.add(new ReservationRequest("Alice", "Single Room"));
        requestQueue.add(new ReservationRequest("Bob", "Double Room"));
        requestQueue.add(new ReservationRequest("Charlie", "Single Room"));
        requestQueue.add(new ReservationRequest("Diana", "Double Room"));

        BookingService bookingService = new BookingService(inventory, requestQueue);
        bookingService.processRequests();

        inventory.displayStatus();
    }
}