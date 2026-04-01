import java.util.*;

class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}

class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) throws BookingException {
        if (count < 0) {
            throw new BookingException("Invalid Inventory: Initial count for " + type + " cannot be negative.");
        }
        inventory.put(type, count);
    }

    public void validateAndDecrement(String type) throws BookingException {
        if (!inventory.containsKey(type)) {
            throw new BookingException("Invalid Room Type: '" + type + "' does not exist in our system.");
        }
        int currentCount = inventory.get(type);
        if (currentCount <= 0) {
            throw new BookingException("Availability Error: No " + type + "s left to book.");
        }
        inventory.put(type, currentCount - 1);
    }

    public int getCount(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

class BookingValidator {
    public static void validateGuestInfo(String name) throws BookingException {
        if (name == null || name.trim().isEmpty()) {
            throw new BookingException("Input Validation: Guest name cannot be empty.");
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();

        try {
            inventory.addRoomType("Single", 1);
            inventory.addRoomType("Suite", 0);

            processBooking(inventory, "Alice", "Single");
            processBooking(inventory, "Bob", "Single"); // Should fail (No availability)
            processBooking(inventory, "", "Suite");      // Should fail (Invalid Name)
            processBooking(inventory, "Charlie", "Penthouse"); // Should fail (Invalid Type)

        } catch (BookingException e) {
            System.err.println("System Setup Error: " + e.getMessage());
        }

        System.out.println("\nFinal Single Room Count: " + inventory.getCount("Single"));
    }

    public static void processBooking(RoomInventory inventory, String name, String type) {
        try {
            System.out.println("Attempting booking for " + name + " (" + type + ")...");

            BookingValidator.validateGuestInfo(name);
            inventory.validateAndDecrement(type);

            System.out.println("SUCCESS: Booking confirmed for " + name);
        } catch (BookingException e) {
            System.out.println("FAILED: " + e.getMessage());
        } finally {
            System.out.println("-------------------------------------------");
        }
    }
}