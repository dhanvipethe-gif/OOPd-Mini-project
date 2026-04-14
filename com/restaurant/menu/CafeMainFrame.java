package com.restaurant.menu;

import logic.BillingEngine;
import logic.BookingEngine;
import logic.MenuManager;
import model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.time.Month;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║  OOPD Concept #6 — Java Swing GUI (Partner C's Role)            ║
 * ║  Provided here as the INTEGRATION FRAME so all three roles      ║
 * ║  compile and run together.  Partner C owns the full UI polish.  ║
 * ╚══════════════════════════════════════════════════════════════════╝
 *
 * Entry point: run main() to launch the Cafe Companion app.
 */
public class CafeMainFrame extends JFrame {

    // ── Shared engines ────────────────────────────────────────────────────────
    private final BookingEngine bookingEngine;
    private final MenuManager<MenuItem> drinksMenu;
    private final MenuManager<SpecialtyItem> specialtyMenu;
    private final BillingEngine billing = BillingEngine.getInstance();

    // ── Live status label (updated via BookingListener) ───────────────────────
    private JLabel seatStatusLabel;

    @SuppressWarnings("unchecked")
    public CafeMainFrame() {
        super("☕ Cafe Companion — Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        // ── Bootstrap sample data ─────────────────────────────────────────────
        bookingEngine  = new BookingEngine(10);
        drinksMenu     = new MenuManager<>("Drinks");
        specialtyMenu  = new MenuManager<>("Specialty Items");

        seedSampleData();

        // ── Subscribe GUI to booking changes ─────────────────────────────────
        bookingEngine.addListener((available, total) ->
            SwingUtilities.invokeLater(() ->
                seatStatusLabel.setText("Seats: " + available + "/" + total + " available")));

        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        // ── Colour palette ────────────────────────────────────────────────────
        Color bg        = new Color(0xFDF6EC);
        Color accent    = new Color(0x6B3A2A);
        Color tabBg     = new Color(0xEEDDCC);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(bg);

        // ── Header ────────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(accent);
        header.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel title = new JLabel("☕  CAFE COMPANION", SwingConstants.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Serif", Font.BOLD, 22));

        seatStatusLabel = new JLabel("Seats: 10/10 available", SwingConstants.RIGHT);
        seatStatusLabel.setForeground(new Color(0xFFDDAA));
        seatStatusLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));

        header.add(title, BorderLayout.WEST);
        header.add(seatStatusLabel, BorderLayout.EAST);

        // ── Tabbed pane (Partner C's JTabbedPane requirement) ─────────────────
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setBackground(tabBg);
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));

        tabs.addTab("🍽  Order",   buildOrderTab());
        tabs.addTab("📅  Booking", buildBookingTab());
        tabs.addTab("💰  Billing", buildBillingTab());
        tabs.addTab("📋  Menu Mgr", buildMenuManagerTab());

        root.add(header, BorderLayout.NORTH);
        root.add(tabs,   BorderLayout.CENTER);
        setContentPane(root);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB 1 — Order
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildOrderTab() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(15, 15, 15, 15));
        p.setBackground(new Color(0xFDF6EC));

        // Menu list
        DefaultListModel<String> menuModel = new DefaultListModel<>();
        drinksMenu.getAvailableItems().forEach(i ->
                menuModel.addElement(i.getItemId() + " | " + i.getName() +
                        " — ₹" + String.format("%.2f", i.getEffectivePrice())));
        specialtyMenu.getAvailableItems().forEach(i ->
                menuModel.addElement(i.getItemId() + " | " + i.getName() +
                        " — ₹" + String.format("%.2f", i.getEffectivePrice())));

        JList<String> menuList = new JList<>(menuModel);
        menuList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane menuScroll = new JScrollPane(menuList);
        menuScroll.setBorder(BorderFactory.createTitledBorder("Available Menu"));

        // Cart
        DefaultListModel<String> cartModel = new DefaultListModel<>();
        JList<String> cartList = new JList<>(cartModel);
        JScrollPane cartScroll = new JScrollPane(cartList);
        cartScroll.setBorder(BorderFactory.createTitledBorder("Your Cart"));

        // Buttons
        JButton addBtn   = new JButton("Add →");
        JButton clearBtn = new JButton("Clear Cart");
        JButton orderBtn = new JButton("Place Order");
        styleButton(orderBtn, new Color(0x6B3A2A), Color.WHITE);

        addBtn.addActionListener(e -> {
            menuList.getSelectedValuesList().forEach(cartModel::addElement);
        });
        clearBtn.addActionListener(e -> cartModel.clear());
        orderBtn.addActionListener(e -> {
            if (cartModel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cart is empty!");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Order placed! (" + cartModel.size() + " items)\n" +
                        "Proceed to Billing tab to calculate your bill.");
                cartModel.clear();
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(addBtn); btnPanel.add(clearBtn); btnPanel.add(orderBtn);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                menuScroll, cartScroll);
        split.setResizeWeight(0.55);

        p.add(split,    BorderLayout.CENTER);
        p.add(btnPanel, BorderLayout.SOUTH);
        return p;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB 2 — Booking
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildBookingTab() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(0xFDF6EC));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(20);
        JTextArea  logArea   = new JTextArea(8, 40);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JButton bookBtn    = new JButton("Book Table");
        JButton releaseBtn = new JButton("Release Table");
        styleButton(bookBtn,    new Color(0x2E7D32), Color.WHITE);
        styleButton(releaseBtn, new Color(0xC62828), Color.WHITE);

        bookBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter name"); return; }
            boolean booked = bookingEngine.bookTable(name);
            logArea.append(booked
                    ? "✅ Table booked for " + name + "\n"
                    : "⏳ No tables free — " + name + " added to waiting list\n");
        });

        releaseBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter name"); return; }
            bookingEngine.releaseTable(name);
            logArea.append("🔓 Table released by " + name + "\n");
        });

        gbc.gridx = 0; gbc.gridy = 0; p.add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1;                p.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; p.add(bookBtn, gbc);
        gbc.gridx = 1;                p.add(releaseBtn, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        p.add(new JScrollPane(logArea), gbc);

        return p;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB 3 — Billing (showcases all 3 overloads)
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildBillingTab() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(new Color(0xFDF6EC));
        p.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Sample items list for demo
        List<MenuItem> sampleOrder = new ArrayList<>(drinksMenu.getAvailableItems());
        sampleOrder.addAll(specialtyMenu.getAvailableItems().subList(
                0, Math.min(2, specialtyMenu.getAvailableItems().size())));

        JTextArea billArea = new JTextArea();
        billArea.setEditable(false);
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        ButtonGroup modeGroup = new ButtonGroup();
        JRadioButton standardBtn  = new JRadioButton("Standard",   true);
        JRadioButton vipBtn       = new JRadioButton("VIP Code");
        JRadioButton memberBtn    = new JRadioButton("Member Points");
        modeGroup.add(standardBtn); modeGroup.add(vipBtn); modeGroup.add(memberBtn);

        JTextField extraField = new JTextField(15);
        extraField.setToolTipText("VIP Code or Member Name:Points");

        JButton calcBtn = new JButton("Calculate Bill");
        styleButton(calcBtn, new Color(0x6B3A2A), Color.WHITE);

        calcBtn.addActionListener(e -> {
            BillingEngine.BillSummary summary;
            if (vipBtn.isSelected()) {
                summary = billing.calculateBill(sampleOrder, extraField.getText().trim());
            } else if (memberBtn.isSelected()) {
                String[] parts = extraField.getText().split(":");
                String name  = parts.length > 0 ? parts[0].trim() : "Member";
                int    pts   = parts.length > 1 ? parseIntSafe(parts[1]) : 0;
                summary = billing.calculateBill(sampleOrder, name, pts);
            } else {
                summary = billing.calculateBill(sampleOrder);
            }
            billArea.setText(summary.toString());
        });

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.setBackground(new Color(0xFDF6EC));
        controls.add(standardBtn); controls.add(vipBtn); controls.add(memberBtn);
        controls.add(new JLabel("  Extra:"));
        controls.add(extraField);
        controls.add(calcBtn);

        p.add(controls,                  BorderLayout.NORTH);
        p.add(new JScrollPane(billArea), BorderLayout.CENTER);
        p.add(new JLabel("  Hint: VIP codes: VVIP2024 / STAFF10 / LAUNCH25 | " +
                "Member format: Name:Points", SwingConstants.LEFT), BorderLayout.SOUTH);
        return p;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  TAB 4 — Menu Manager (Generics demo)
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildMenuManagerTab() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(new Color(0xFDF6EC));
        p.setBorder(new EmptyBorder(15, 15, 15, 15));

        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JButton refreshBtn = new JButton("Refresh Menu Info");
        styleButton(refreshBtn, new Color(0x37474F), Color.WHITE);
        refreshBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("=== MenuManager<MenuItem>: ").append(drinksMenu).append("\n\n");
            drinksMenu.getAllItems().forEach(i -> sb.append("  ").append(i).append("\n"));
            sb.append("\n=== MenuManager<SpecialtyItem>: ").append(specialtyMenu).append("\n\n");
            specialtyMenu.getAllItems().forEach(i -> sb.append("  ").append(i).append("\n"));
            sb.append("\n── Most Expensive Drink: ")
              .append(drinksMenu.getMostExpensiveAvailable().map(MenuItem::getName).orElse("n/a"))
              .append("\n── Total Specialty Value: ₹")
              .append(String.format("%.2f", specialtyMenu.getTotalMenuValue()));
            infoArea.setText(sb.toString());
        });
        refreshBtn.doClick(); // load on open

        p.add(refreshBtn,                BorderLayout.NORTH);
        p.add(new JScrollPane(infoArea), BorderLayout.CENTER);
        return p;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
    }

    private int parseIntSafe(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }

    private void seedSampleData() {
        drinksMenu.addItem(new MenuItem("D001", "Espresso",        120, "Drinks"));
        drinksMenu.addItem(new MenuItem("D002", "Cappuccino",      150, "Drinks"));
        drinksMenu.addItem(new MenuItem("D003", "Cold Brew",       180, "Drinks"));
        drinksMenu.addItem(new MenuItem("D004", "Masala Chai",      80, "Drinks"));

        specialtyMenu.addItem(new SpecialtyItem("S001", "Truffle Pasta",
                350, "Mains", "Chef's signature dish", 20));
        specialtyMenu.addItem(new SpecialtyItem("S002", "Avocado Toast",
                280, "Breakfast", "House-baked sourdough", 10));
        specialtyMenu.addItem(new SeasonalItem("X001", "Mango Cheesecake",
                320, "Desserts", "Fresh Alphonso mango", 15,
                Month.APRIL, 15.0));
    }

    // ── Application entry point ───────────────────────────────────────────────
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(CafeMainFrame::new);
    }
}
