import Guis.ShopGuis;
import controller.ShopController;
import pojo.MenuItem;
import pojo.Orders;
import utils.LoadUtils;
import java.util.List;
import java.util.Map;
import utils.LogUtils;

public class Main {
    public static void main(String[] args) {
        LogUtils.clr();
        // The .txt file here is located in the resources' folder.
        Map<String, MenuItem> menu = LoadUtils.ldMenu("menu.txt");
        List<Orders> order = LoadUtils.ldOrders("orders.txt", menu);
        LogUtils.w("src/main/resources/log.txt");
        System.out.println("mu succ: " + menu.size());
        System.out.println("or succ: " + order.size());
        if (menu.isEmpty()) {
            System.err.println("Mha: menu");
            return;
        }
        ShopController shopController = new ShopController(menu, order);
        new ShopGuis(menu, shopController);
        System.out.println("Go");
    }
}