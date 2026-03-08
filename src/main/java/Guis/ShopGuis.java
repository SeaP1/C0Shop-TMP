package Guis;

import controller.ShopController;
import pojo.MenuItem;
import service.DiscService;
import service.RpService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopGuis extends JFrame {
    private final ShopController shopController;
    private final Map<String, MenuItem> menu;

    private final DefaultListModel<MenuItem> cartModel;
    private final JList<MenuItem> cartList;
    private final JTextArea billArea;

    public ShopGuis(Map<String, MenuItem> menu, ShopController shopController) {
        this.menu = menu;
        this.shopController = shopController;

        setTitle("Coffee Shop");
        setSize(700, 580);
        setLayout(new BorderLayout());

        // 顶部：菜单选择
        JPanel topPanel = new JPanel(new FlowLayout());
        JComboBox<MenuItem> menuDropdown = new JComboBox<>(menu.values().toArray(new MenuItem[0]));
        JButton addButton = new JButton("Add to Cart");
        topPanel.add(new JLabel("Menu:"));
        topPanel.add(menuDropdown);
        topPanel.add(addButton);
        add(topPanel, BorderLayout.NORTH);

        // 中部：购物车 + 账单
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

        // 底部：折扣规则 + 按钮
        JPanel discountPanel = new JPanel(new BorderLayout());
        discountPanel.setBorder(BorderFactory.createTitledBorder("Discount Rules"));
        JTextArea discountArea = new JTextArea(
                "  Rule 1: Buy 1+ beverage AND 2+ food items  ->  20% OFF the total\n" +
                "  Rule 2: Subtotal >= £20.00 AND include 1+ other item  ->  £2.00 OFF"
        );
        discountArea.setEditable(false);
        discountArea.setBackground(new Color(255, 255, 200));
        discountArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        discountPanel.add(discountArea, BorderLayout.CENTER);

        JPanel buttonRow = new JPanel(new FlowLayout());
        JButton removeButton = new JButton("Remove Selected");
        JButton payButton = new JButton("Check out");
        buttonRow.add(removeButton);
        buttonRow.add(payButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(discountPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonRow, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // 事件：添加商品
        addButton.addActionListener(e -> {
            MenuItem selected = (MenuItem) menuDropdown.getSelectedItem();
            if (selected != null) {
                shopController.addItem(selected);
                refreshCartAndBill();
            }
        });

        // 事件：移除选中商品
        removeButton.addActionListener(e -> {
            int selectedIndex = cartList.getSelectedIndex();
            if (selectedIndex != -1) {
                shopController.removeItem(selectedIndex);
                refreshCartAndBill();
            }
        });

        // 事件：结账，弹出收据
        payButton.addActionListener(e -> {
            if (shopController.getCurrentItems().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cart is empty.");
                return;
            }

            List<MenuItem> items = new ArrayList<>(shopController.getCurrentItems());
            double subtotal = DiscService.calculateSubtotal(items);
            double finalBill = DiscService.calDisTl(items);
            String discountInfo = DiscService.getDiscountDescription(items);
            shopController.checkout();

            // 用 HTML 构建收据，保留加粗
            StringBuilder html = new StringBuilder("<html><body>");
            html.append("<b>Items in this order:</b><br>");
            for (MenuItem item : items) {
                html.append("&nbsp;&nbsp;- ").append(item.getDescribe())
                    .append(" (£").append(String.format("%.2f", item.getCost())).append(")<br>");
            }
            html.append("<br>Subtotal: £").append(String.format("%.2f", subtotal)).append("<br>");
            html.append("Discount: ").append(discountInfo).append("<br>");
            html.append("<b>Final total: £").append(String.format("%.2f", finalBill)).append("</b>");
            html.append("</body></html>");

            JOptionPane.showMessageDialog(this, new JLabel(html.toString()),
                    "Receipt", JOptionPane.INFORMATION_MESSAGE);
            refreshCartAndBill();
        });

        // 事件：关窗，生成 report 并追加写入 orders.txt
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 写 report.txt 到项目根目录
                String report = RpService.generateReport(
                        shopController.getMenu(),
                        shopController.getAllOrderedItems()
                );
                RpService.writeReportToFile(report, "report.txt");

                // 追加新订单到 src/main/resources/orders.txt
                String ordersPath = "src/main/resources/orders.txt";
                RpService.appendOrdersToFile(shopController.getNewOrders(), ordersPath);
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