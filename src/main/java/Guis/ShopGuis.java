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
    private ShopController sc;
    private DefaultListModel<MenuItem> mdl = new DefaultListModel<>();
    private JList<MenuItem> lst = new JList<>(mdl);
    private JTextArea ba = new JTextArea();
    public ShopGuis(Map<String, MenuItem> m, ShopController sc) {
        this.sc = sc;
        setTitle("Coffee Shop");
        setSize(700, 500);
        setLayout(new BorderLayout());
        JPanel p1 = new JPanel();
        // set menu
        JComboBox<MenuItem> cb = new JComboBox<>(m.values().toArray(new MenuItem[0]));
        JButton bA = new JButton("Add");
        p1.add(new JLabel("Menu:")); p1.add(cb); p1.add(bA);
        add(p1, "North");
        // cart and bills
        ba.setEditable(false);
        JSplitPane sp = new JSplitPane(1, new JScrollPane(lst), new JScrollPane(ba));
        sp.setDividerLocation(300);
        add(sp, "Center");
        // explain discount rules
        JPanel p2 = new JPanel(new BorderLayout());
        JTextArea ra = new JTextArea(" Rule 1: 1+ Bev & 2+ Food -> 20% OFF\n Rule 2: >= £20 & 1+ Other -> -£2");
        ra.setEditable(false); ra.setBackground(new Color(255, 255, 200));
        p2.add(ra, "Center");
        JPanel p3 = new JPanel();
        JButton bR = new JButton("Remove"), bC = new JButton("Check out");
        p3.add(bR); p3.add(bC);
        p2.add(p3, "South");
        add(p2, "South");
        // Listening
        bA.addActionListener(e -> { sc.add((MenuItem)cb.getSelectedItem()); upd(); });
        bR.addActionListener(e -> {
            int i = lst.getSelectedIndex();
            if (i >= 0) { sc.rem(i); upd(); }
        });
        bC.addActionListener(e -> {
            List<MenuItem> cur = sc.getC();
            if (cur.isEmpty()) return;
            String res = String.format("<html><b>Total: £%.2f</b><br>Discount: %s</html>",
                    DiscService.calDisTl(cur), DiscService.getDiscountDescription(cur));
            JOptionPane.showMessageDialog(this, new JLabel(res));
            sc.chk(); upd();
        });
        // Auto generate report
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                RpService.wR(RpService.generateReport(sc.getM(), sc.getA(), sc.getChkout(), "report.txt"), "report.txt");
                RpService.aO(sc.getN(), "src/main/resources/orders.txt");
            }
        });
        setDefaultCloseOperation(3);
        setVisible(true);
    }
    // update bills and cart
    private void upd() {
        mdl.clear();
        for (MenuItem i : sc.getC()) mdl.addElement(i);
        ba.setText(sc.getBill());
    }
}