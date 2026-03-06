import Guis.ShopGuis;
import controller.ShopController;
import pojo.MenuItem;
import pojo.Orders;
import utils.LoadUtils;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // test.txt 如果有
//        String menuFilePath = "menu.txt";
//        // System.out.println("Start");
//        Map<String, MenuItem> menu = LoadUtils.ldMenu(menuFilePath);
        Map<String, MenuItem> menu = LoadUtils.ldMenu("menu.txt");
        List<Orders> order = LoadUtils.ldOrders("orders.txt");
        System.out.println("mu succ: " + menu.size());
        System.out.println("or succ: " + order.size());
        if (menu.isEmpty()) {
            System.err.println("No mu files");
            return;
        }
        if (order.isEmpty()) {
            System.err.println("No or files");
            return;
        }
        ShopController shopController = new ShopController(menu, order);
        new ShopGuis(menu, shopController);
        System.out.println("Go");
//        System.out.println(Main.class.getClassLoader().getResource("menu.txt"));
    }
}