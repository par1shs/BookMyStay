import java.util.*;

class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}

class InventoryManager {
    private Map<String, Integer> counts = new HashMap<>();
    private Map<String, Stack<String>> pool = new HashMap<>();

    public void setup(String type, int count) {
        counts.put(type, count);
        pool.put(type, new Stack<>());
        for (int i = 1; i <= count; i++) {
            pool.get(type).push(type.substring(0, 1) + "-" + (100 + i));
        }
    }

    public String acquire(String type) throws BookingException {
        if (!counts.containsKey(type) || counts.get(type) <= 0) {
            throw new BookingException("No availability for " + type);
        }
        counts.put(type, counts.get(type) - 1);
        return pool.get(type).pop();
    }

    public void release(String type, String roomId) {
        counts.put(type, counts.get(type) + 1);
        pool.get(type).push(roomId);
    }

    public int getAvailable(String type) {
        return counts.getOrDefault(type, 0);
    }
}

class CancellationService {
    private InventoryManager inventory;
    private Map<String, String> activeBookings;

    public CancellationService(InventoryManager inventory, Map<String, String> activeBookings) {
        this.inventory = inventory;
        this.activeBookings = activeBookings;
    }

    public void cancelBooking(String guestName, String type) throws BookingException {
        if (!activeBookings.containsKey(guestName)) {
            throw new BookingException("Cancellation Failed: No active booking found for " + guestName);
        }

        String roomId = activeBookings.remove(guestName);
        inventory.release(type, roomId);

        System.out.println("CANCELLED: Booking for " + guestName + " (Room " + roomId + ") reversed.");
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        InventoryManager inventory = new InventoryManager();
        inventory.setup("Single", 2);

        Map<String, String> activeBookings = new HashMap<>();
        CancellationService cancellationService = new CancellationService(inventory, activeBookings);

        try {
            System.out.println("Initial Inventory: " + inventory.getAvailable("Single"));

            String id1 = inventory.acquire("Single");
            activeBookings.put("Alice", id1);
            System.out.println("CONFIRMED: Alice assigned to " + id1);

            System.out.println("Inventory after booking: " + inventory.getAvailable("Single"));

            cancellationService.cancelBooking("Alice", "Single");
            System.out.println("Inventory after cancellation: " + inventory.getAvailable("Single"));

            cancellationService.cancelBooking("Bob", "Single");

        } catch (BookingException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}