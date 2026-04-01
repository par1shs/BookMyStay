import java.io.*;
import java.util.*;

class BookingState implements Serializable {
    private static final long serialVersionUID = 1L;
    public Map<String, Integer> inventory = new HashMap<>();
    public List<String> confirmedBookings = new ArrayList<>();

    public void addInventory(String type, int count) {
        inventory.put(type, count);
    }

    public void addBooking(String details) {
        confirmedBookings.add(details);
    }
}

class PersistenceService {
    private static final String FILE_NAME = "hotel_state.ser";

    public void saveState(BookingState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("SYSTEM: State persisted to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("SAVE ERROR: " + e.getMessage());
        }
    }

    public BookingState loadState() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("SYSTEM: No persistence file found. Starting fresh.");
            return new BookingState();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            BookingState state = (BookingState) ois.readObject();
            System.out.println("SYSTEM: State restored successfully from " + FILE_NAME);
            return state;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("RECOVERY ERROR: Corrupted file. Starting fresh. " + e.getMessage());
            return new BookingState();
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        PersistenceService persistence = new PersistenceService();

        // 1. Startup: Load state from previous session
        BookingState currentState = persistence.loadState();

        // 2. Display recovered state
        System.out.println("\n--- Current Inventory Status ---");
        currentState.inventory.forEach((type, count) -> System.out.println(type + ": " + count));

        System.out.println("\n--- Recovered Booking History ---");
        if (currentState.confirmedBookings.isEmpty()) {
            System.out.println("No history found.");
        } else {
            currentState.confirmedBookings.forEach(System.out::println);
        }

        // 3. Simulate operations in current session
        System.out.println("\nSYSTEM: Processing new booking for 'Diana'...");
        if (currentState.inventory.getOrDefault("Single", 0) > 0) {
            currentState.inventory.put("Single", currentState.inventory.get("Single") - 1);
            currentState.addBooking("Diana - Single Room - Confirmed 2026-04-01");
        } else {
            // Seed initial data if file didn't exist
            currentState.addInventory("Single", 5);
            currentState.addInventory("Double", 3);
        }

        // 4. Shutdown: Persist state for next restart
        persistence.saveState(currentState);
        System.out.println("SYSTEM: Application terminated safely.");
    }
}