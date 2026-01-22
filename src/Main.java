import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.db.PostgresDB;
import edu.aitu.oop3.entities.Order;
import edu.aitu.oop3.entities.OrderItem;
import edu.aitu.oop3.entities.OrderStatus;
import edu.aitu.oop3.entities.MenuItem;
import edu.aitu.oop3.exceptions.InvalidQuantityException;
import edu.aitu.oop3.exceptions.MenuItemNotAvailableException;
import edu.aitu.oop3.exceptions.OrderNotFoundException;
import edu.aitu.oop3.repositories.MenuItemRepository;
import edu.aitu.oop3.repositories.OrderRepository;
import edu.aitu.oop3.repositories.impl.MenuItemRepositoryImpl;
import edu.aitu.oop3.repositories.impl.OrderRepositoryImpl;
import edu.aitu.oop3.services.MenuService;
import edu.aitu.oop3.services.OrderService;
import edu.aitu.oop3.services.PaymentService;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Connecting to Supabase...");

        IDB db = new PostgresDB();
        MenuItemRepository menuRepo = new MenuItemRepositoryImpl(db);
        OrderRepository orderRepo = new OrderRepositoryImpl(db);

        MenuService menuService = new MenuService(menuRepo);
        PaymentService paymentService = new PaymentService();
        OrderService orderService = new OrderService(orderRepo, menuService, paymentService);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Canteen Ordering ---");
            System.out.println("1) List menu");
            System.out.println("2) Place order");
            System.out.println("3) View active orders");
            System.out.println("4) Mark order completed");
            System.out.println("0) Exit");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();

            try {
                if (choice.equals("1")) {
                    List<MenuItem> menu = menuService.getAllMenu();
                    menu.forEach(System.out::println);

                } else if (choice.equals("2")) {
                    System.out.print("Customer ID: ");
                    long customerId = Long.parseLong(sc.nextLine().trim());


                    List<OrderItem> items = new ArrayList<>();
                    while (true) {
                        System.out.print("Enter menuItemId (or 'done'): ");
                        String s = sc.nextLine().trim();
                        if (s.equalsIgnoreCase("done")) break;

                        long menuItemId = Long.parseLong(s);
                        System.out.print("Quantity: ");
                        int qty = Integer.parseInt(sc.nextLine().trim());

                        items.add(new OrderItem(0, 0, menuItemId, qty, null));
                    }

                    long orderId = orderService.placeOrder(customerId, items);
                    System.out.println("Order created! ID = " + orderId);

                } else if (choice.equals("3")) {
                    List<Order> active = orderService.getActiveOrders();
                    active.forEach(System.out::println);

                } else if (choice.equals("4")) {
                    System.out.print("Order ID: ");
                    long orderId = Long.parseLong(sc.nextLine().trim());
                    orderService.markCompleted(orderId);
                    System.out.println("Order " + orderId + " marked as COMPLETED.");

                } else if (choice.equals("0")) {
                    System.out.println("Bye!");
                    return;
                } else {
                    System.out.println("Unknown option.");
                }

            } catch (InvalidQuantityException | MenuItemNotAvailableException | OrderNotFoundException e) {
                System.out.println("Business error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());

            }
        }
    }
}
