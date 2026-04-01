import java.util.*;

class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}

class ThreadSafeInventory {
    private final Map<String, Integer> inventory = new HashMap<>();
    private final Map<String, Stack<String>> roomPool = new HashMap<>();

    public synchronized void addRoomType(String type, int count) {
        inventory.put(type, count);
        Stack<String> ids = new Stack<>();
        for (int i = 1; i <= count; i++) {
            ids.push(type.substring(0, 1) + "-" + (100 + i));
        }
        roomPool.put(type, ids);
    }

    public synchronized String bookRoom(String type) throws BookingException {
        Integer count = inventory.get(type);
        if (count == null || count <= 0) {
            throw new BookingException("Sold out: " + type);
        }

        inventory.put(type, count - 1);
        return roomPool.get(type).pop();
    }

    public synchronized int getCount(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

class GuestRequest implements Runnable {
    private String guestName;
    private String roomType;
    private ThreadSafeInventory inventory;

    public GuestRequest(String name, String type, ThreadSafeInventory inventory) {
        this.guestName = name;
        this.roomType = type;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        try {
            System.out.println(guestName + " is attempting to book a " + roomType + "...");
            String roomId = inventory.bookRoom(roomType);
            System.out.println("SUCCESS: " + guestName + " secured Room " + roomId);
        } catch (BookingException e) {
            System.out.println("FAILURE: " + guestName + " received error -> " + e.getMessage());
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) throws InterruptedException {
        ThreadSafeInventory sharedInventory = new ThreadSafeInventory();
        sharedInventory.addRoomType("Single", 2);

        System.out.println("--- Starting Concurrent Booking Simulation ---");
        System.out.println("Initial Inventory: 2 Single Rooms\n");

        Thread t1 = new Thread(new GuestRequest("Alice", "Single", sharedInventory));
        Thread t2 = new Thread(new GuestRequest("Bob", "Single", sharedInventory));
        Thread t3 = new Thread(new GuestRequest("Charlie", "Single", sharedInventory));

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("\nSimulation Complete.");
        System.out.println("Final Inventory Count: " + sharedInventory.getCount("Single"));
    }
}