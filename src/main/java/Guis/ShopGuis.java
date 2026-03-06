package main.java.Guis;
import main.java.pojo.MenuItem;
import main.java.controller.ShopController;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ShopGuis extends JFrame {
    private ShopController shopController = new ShopController();
    public ShopGuis(Map<String, MenuItem> menu) {
        setTitle("MockShop");
        setSize(400, 300);
        setLayout(new FlowLayout());
        // Menus
        JComboBox<MenuItem> menuDropdown = new JComboBox<>(menu.values().toArray(new MenuItem[0]));
        add(menuDropdown);
        JButton addButton = new JButton("Add orders");
        add(addButton);
        JButton payButton = new JButton("Check out");
        add(payButton);

        addButton.addActionListener(e -> {
            MenuItem selected = (MenuItem) menuDropdown.getSelectedItem();
            shopController.addItem(selected);
            JOptionPane.showMessageDialog(this, "Add: " + selected.getDescribe());
        });
        payButton.addActionListener(e -> {
            double finalBill = shopController.checkout();
            JOptionPane.showMessageDialog(this, String.format("Total: £%.2f", finalBill));
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
