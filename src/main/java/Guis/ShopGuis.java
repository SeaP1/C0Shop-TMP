package Guis;

import controller.ShopController;
import pojo.MenuItem;
import service.RpService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class ShopGuis extends JFrame {
    private ShopController shopController;
    private Map<String, MenuItem> menu;

    private DefaultListModel<MenuItem> cartModel;
    private JList<MenuItem> cartList;
    private JTextArea billArea;

    public ShopGuis(Map<String, MenuItem> menu, ShopController shopController) {
        this.menu = menu;
        this.shopController = shopController;

        setTitle("MockShop");
        setSize(700, 450);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        JComboBox<MenuItem> menuDropdown = new JComboBox<>(menu.values().toArray(new MenuItem[0]));
        JButton addButton = new JButton("Add order");
        topPanel.add(new JLabel("Menu:"));
        topPanel.add(menuDropdown);
        topPanel.add(addButton);
        add(topPanel, BorderLayout.NORTH);

        cartModel = new DefaultListModel<>();
        cartList = new JList<>(cartModel);
        JScrollPane cartScrollPane = new JScrollPane(cartList);
        cartScrollPane.setBorder(BorderFactory.createTitledBorder("Current Cart"));

        billArea = new JTextArea();
        billArea.setEditable(false);
        JScrollPane billScrollPane = new JScrollPane(billArea);
        billScrollPane.setBorder(BorderFactory.createTitledBorder("Bill"));

        JSplitPane centerPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, cartScrollPane, billScrollPane);
        centerPane.setDividerLocation(300);
        add(centerPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton removeButton = new JButton("Remove Selected");
        JButton payButton = new JButton("Check out");
        bottomPanel.add(removeButton);
        bottomPanel.add(payButton);
        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            MenuItem selected = (MenuItem) menuDropdown.getSelectedItem();
            if (selected != null) {
                shopController.addItem(selected);
                refreshCartAndBill();
            }
        });

        removeButton.addActionListener(e -> {
            int selectedIndex = cartList.getSelectedIndex();
            if (selectedIndex != -1) {
                shopController.removeItem(selectedIndex);
                refreshCartAndBill();
            }
        });

        payButton.addActionListener(e -> {
            if (shopController.getCurrentItems().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cart is empty.");
                return;
            }

            double finalBill = shopController.checkout();
            JOptionPane.showMessageDialog(this, String.format("Final total: £%.2f", finalBill));
            refreshCartAndBill();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String report = RpService.generateReport(
                        shopController.getMenu(),
                        shopController.getAllOrderedItems()
                );
                RpService.writeReportToFile(report, "report.txt");
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void refreshCartAndBill() {
        cartModel.clear();
        for (MenuItem item : shopController.getCurrentItems()) {
            cartModel.addElement(item);
        }
        billArea.setText(shopController.getBillText());
    }
}