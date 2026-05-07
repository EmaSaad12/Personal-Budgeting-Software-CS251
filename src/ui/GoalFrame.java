/*package ui;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import controller.GoalController;
import model.Transaction;
import repository.TransactionRepository;
import model.SavingGoal;

public class GoalFrame extends JFrame {
    private GoalController controller = new GoalController();
    private JTextField nameField, targetField, deadlineField, idField, linkGoalIdField;
    private JComboBox<String> transComboBox;
    private JTextArea displayArea;
    private JProgressBar goalProgressBar;
    private JButton addBtn, clearBtn, refreshBtn, linkBtn;


    Color bg = new Color(20, 20, 25);
    Color panelBg = new Color(30, 30, 35);
    Color purple = new Color(90, 40, 130);
    Color purpleGlow = new Color(130, 70, 180);
    Color textColor = new Color(230, 230, 230);

    public GoalFrame() {
        setTitle("Saving Goal Manager");
        setSize(650, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bg);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(bg);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));

        JButton dashBtn = new JButton("\u2190 Back to Dashboard");
        dashBtn.setBackground(bg);
        dashBtn.setForeground(Color.LIGHT_GRAY);
        dashBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dashBtn.setBorderPainted(false);
        dashBtn.setFocusPainted(false);
        dashBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        dashBtn.addActionListener(e -> {
            new DashboardScreen().setVisible(true);
            this.dispose();
        });

        topPanel.add(dashBtn);
        add(topPanel, BorderLayout.NORTH);


        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        nameField = createStyledField(fieldFont, purple, purpleGlow);
        targetField = createStyledField(fieldFont, purple, purpleGlow);
        deadlineField = createStyledField(fieldFont, purple, purpleGlow);
        deadlineField.setText("2026-12-31");
        idField = createStyledField(fieldFont, purple, purpleGlow);
        linkGoalIdField = createStyledField(fieldFont, purple, purpleGlow);

        transComboBox = new JComboBox<>();
        transComboBox.setBackground(new Color(40, 40, 45));
        transComboBox.setForeground(Color.WHITE);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setBackground(new Color(40, 40, 45));
        displayArea.setForeground(Color.WHITE);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 13));


        goalProgressBar = new JProgressBar(0, 100);
        goalProgressBar.setValue(0);
        goalProgressBar.setStringPainted(true);
        goalProgressBar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        goalProgressBar.setForeground(purpleGlow);
        goalProgressBar.setBackground(new Color(45, 45, 50));
        goalProgressBar.setBorder(BorderFactory.createLineBorder(purple, 1));


        addBtn = createStyledButton("Add Goal", purple);
        clearBtn = createStyledButton("Clear", new Color(192, 57, 43));
        refreshBtn = createStyledButton("Refresh List", purple);
        linkBtn = createStyledButton("Link to Goal & Show Progress", purple);


        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(bg);


        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBackground(panelBg);
        inputPanel.setBorder(createCustomTitledBorder("Create Saving Goal"));
        inputPanel.add(createStyledLabel(" Goal Name:")); inputPanel.add(nameField);
        inputPanel.add(createStyledLabel(" Target:")); inputPanel.add(targetField);
        inputPanel.add(createStyledLabel(" Deadline:")); inputPanel.add(deadlineField);
        inputPanel.add(createStyledLabel(" Goal ID:")); inputPanel.add(idField);
        inputPanel.add(addBtn); inputPanel.add(clearBtn);


        JPanel linkPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        linkPanel.setBackground(panelBg);
        linkPanel.setBorder(createCustomTitledBorder("Link & View Progress"));
        linkPanel.add(createStyledLabel(" Select Transaction:")); linkPanel.add(transComboBox);
        linkPanel.add(createStyledLabel(" Refresh:")); linkPanel.add(refreshBtn);
        linkPanel.add(createStyledLabel(" Enter Goal ID:")); linkPanel.add(linkGoalIdField);
        linkPanel.add(createStyledLabel(" Confirm Action:")); linkPanel.add(linkBtn);


        JPanel statusPanel = new JPanel(new BorderLayout(10, 10));
        statusPanel.setBackground(panelBg);
        statusPanel.setBorder(createCustomTitledBorder("Visual Progress"));
        statusPanel.add(goalProgressBar, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setPreferredSize(new Dimension(600, 150));
        statusPanel.add(scrollPane, BorderLayout.CENTER);


        mainContent.add(inputPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 15)));
        mainContent.add(linkPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 15)));
        mainContent.add(statusPanel);

        JPanel marginWrapper = new JPanel(new BorderLayout());
        marginWrapper.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        marginWrapper.setBackground(bg);
        marginWrapper.add(mainContent, BorderLayout.CENTER);

        add(marginWrapper);


        addBtn.addActionListener(e -> {
            try {
                controller.addGoal(nameField.getText(), Double.parseDouble(targetField.getText()), deadlineField.getText(), Integer.parseInt(idField.getText()));
                displayArea.append(">> Goal '" + nameField.getText() + "' added successfully.\n");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Check inputs!"); }
        });

        refreshBtn.addActionListener(e -> {
            transComboBox.removeAllItems();
            TransactionRepository repo = new TransactionRepository();
            for (Transaction t : repo.loadTransactions()) {
                transComboBox.addItem(t.getType() + ": $" + t.getAmount() + " (" + t.getId() + ")");
            }
        });

        linkBtn.addActionListener(e -> {
            String selected = (String) transComboBox.getSelectedItem();
            String gIdStr = linkGoalIdField.getText();
            if (selected != null && !gIdStr.isEmpty()) {
                String uuid = selected.substring(selected.lastIndexOf("(") + 1, selected.lastIndexOf(")"));
                int goalId = Integer.parseInt(gIdStr);
                linkTransactionToGoal(uuid, goalId);
                updateVisualProgress(goalId);
            }
        });
        clearBtn.addActionListener(e -> {

            nameField.setText("");
            targetField.setText("");
            idField.setText("");
            linkGoalIdField.setText("");
            deadlineField.setText("2026-12-31");


            displayArea.setText("");


            goalProgressBar.setValue(0);


            System.out.println("All fields cleared.");
        });

        setLocationRelativeTo(null);
    }


    private void updateVisualProgress(int goalId) {
        TransactionRepository repo = new TransactionRepository();
        List<Transaction> allTrans = repo.loadTransactions();


        String report = controller.generateGoalsReportOnly(allTrans);


        double saved = 0;
        double target = 0;
        for (SavingGoal g : controller.getAllGoals()) {
            if (g.getGoalID() == goalId) {
                target = g.getTargetAmount();
                break;
            }
        }


        java.util.Map<String, Integer> mappings = loadMappingsFromController();
        for (Transaction t : allTrans) {
            if (mappings.containsKey(t.getId()) && mappings.get(t.getId()) == goalId) {
                if (t.getType().equalsIgnoreCase("Income")) saved += t.getAmount();
                else saved -= t.getAmount();
            }
        }

        int percentage = (target > 0) ? (int)((saved / target) * 100) : 0;
        if (percentage > 100) percentage = 100;
        if (percentage < 0) percentage = 0;

        goalProgressBar.setValue(percentage);
        displayArea.append(">> Visual Progress Updated: " + percentage + "%\n");
    }

    private java.util.Map<String, Integer> loadMappingsFromController() {
        java.util.Map<String, Integer> map = new java.util.HashMap<>();
        try (java.util.Scanner r = new java.util.Scanner(new File("goal_links.txt"))) {
            while (r.hasNextLine()) {
                String[] d = r.nextLine().split(",");
                if (d.length == 2) map.put(d[0], Integer.parseInt(d[1]));
            }
        } catch (Exception e) {}
        return map;
    }

    public void linkTransactionToGoal(String transId, int goalId) {
        try (FileWriter fw = new FileWriter("goal_links.txt", true)) {
            fw.write(transId + "," + goalId + "\n");
        } catch (IOException e) {}
    }

    private JTextField createStyledField(Font f, Color b, Color gl) {
        JTextField field = new JTextField();
        field.setFont(f); field.setForeground(Color.WHITE);
        field.setBackground(new Color(40, 40, 45));
        field.setBorder(BorderFactory.createLineBorder(b, 2));
        return field;
    }

    private JButton createStyledButton(String t, Color c) {
        JButton btn = new JButton(t);
        btn.setBackground(c); btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return btn;
    }

    private JLabel createStyledLabel(String t) {
        JLabel lbl = new JLabel(t);
        lbl.setForeground(textColor);
        return lbl;
    }

    private TitledBorder createCustomTitledBorder(String t) {
        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(purple, 1), t);
        border.setTitleColor(purpleGlow);
        return border;
    }
}*/
/*package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

import controller.GoalController;
import model.Transaction;
import repository.TransactionRepository;
import model.SavingGoal;

public class GoalFrame extends JFrame {

    private GoalController controller =
            new GoalController();

    private JTextField nameField,
            targetField,
            deadlineField,
            idField,
            linkGoalIdField;

    private JComboBox<String> transComboBox;

    private JTextArea displayArea;

    private JProgressBar goalProgressBar;

    private JButton addBtn,
            clearBtn,
            refreshBtn,
            linkBtn;

    // COLORS
    Color bg =
            new Color(8, 8, 12);

    Color panelBg =
            new Color(15, 15, 22);

    Color purple =
            new Color(70, 20, 120);

    Color purpleGlow =
            new Color(130, 70, 180);

    Color bankBlue =
            new Color(120, 200, 255);

    Color textColor =
            Color.WHITE;

    public GoalFrame() {

        setTitle("Saving Goal Manager");

        setSize(700, 760);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLayout(new BorderLayout());

        getContentPane().setBackground(bg);

        // TOP PANEL
        JPanel topPanel =
                new JPanel(
                        new BorderLayout()
                );

        topPanel.setBackground(bg);

        topPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        20,
                        10,
                        20
                )
        );

        JPanel leftTop =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        leftTop.setOpaque(false);

        JLabel logo =
                new JLabel("🏦");

        logo.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        35
                )
        );

        logo.setForeground(bankBlue);

        JLabel title =
                new JLabel("Saving Goals");

        title.setForeground(bankBlue);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        26
                )
        );

        leftTop.add(logo);

        leftTop.add(title);

        RoundedButton dashBtn =
                new RoundedButton(
                        "Dashboard",
                        bankBlue,
                        new Color(
                                150,
                                220,
                                255
                        )
                );

        dashBtn.setForeground(Color.BLACK);

        dashBtn.setPreferredSize(
                new Dimension(140, 42)
        );

        dashBtn.addActionListener(e -> {

            new DashboardScreen()
                    .setVisible(true);

            dispose();
        });

        topPanel.add(
                leftTop,
                BorderLayout.WEST
        );

        topPanel.add(
                dashBtn,
                BorderLayout.EAST
        );

        add(
                topPanel,
                BorderLayout.NORTH
        );

        // INPUTS
        nameField =
                createInput();

        targetField =
                createInput();

        deadlineField =
                createInput();

        idField =
                createInput();

        linkGoalIdField =
                createInput();

        addPlaceholder(
                nameField,
                "Enter Goal Name"
        );

        addPlaceholder(
                targetField,
                "Enter Target Amount"
        );

        addPlaceholder(
                deadlineField,
                "2026-12-31"
        );

        addPlaceholder(
                idField,
                "Enter Goal ID"
        );

        addPlaceholder(
                linkGoalIdField,
                "Enter Goal ID"
        );

        // ENTER
        nameField.addActionListener(
                e -> targetField.requestFocus()
        );

        targetField.addActionListener(
                e -> deadlineField.requestFocus()
        );

        deadlineField.addActionListener(
                e -> idField.requestFocus()
        );

        idField.addActionListener(
                e -> addBtn.doClick()
        );

        // COMBO BOX
        transComboBox =
                new JComboBox<>();

        transComboBox.setBackground(
                new Color(25, 25, 35)
        );

        transComboBox.setForeground(
                Color.WHITE
        );

        transComboBox.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        transComboBox.setFocusable(false);

        transComboBox.setBorder(
                new RoundedBorder(
                        bankBlue,
                        18
                )
        );

        transComboBox.setRenderer(
                new DefaultListCellRenderer() {

                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus
                    ) {

                        JLabel label =
                                (JLabel)
                                        super.getListCellRendererComponent(
                                                list,
                                                value,
                                                index,
                                                isSelected,
                                                cellHasFocus
                                        );

                        label.setBorder(
                                BorderFactory.createEmptyBorder(
                                        8,
                                        12,
                                        8,
                                        12
                                )
                        );

                        if (isSelected) {

                            label.setBackground(
                                    purple
                            );

                            label.setForeground(
                                    Color.WHITE
                            );

                        } else {

                            label.setBackground(
                                    new Color(
                                            25,
                                            25,
                                            35
                                    )
                            );

                            label.setForeground(
                                    Color.WHITE
                            );
                        }

                        return label;
                    }
                }
        );

        // TEXT AREA
        displayArea =
                new JTextArea();

        displayArea.setEditable(false);

        displayArea.setLineWrap(true);

        displayArea.setWrapStyleWord(true);

        displayArea.setBackground(
                new Color(25, 25, 35)
        );

        displayArea.setForeground(
                Color.WHITE
        );

        displayArea.setCaretColor(
                Color.WHITE
        );

        displayArea.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        displayArea.setMargin(
                new Insets(
                        15,
                        15,
                        15,
                        15
                )
        );

        displayArea.setBorder(
                new RoundedBorder(
                        bankBlue,
                        18
                )
        );

        // PROGRESS
        goalProgressBar =
                new JProgressBar(
                        0,
                        100
                );

        goalProgressBar.setValue(0);

        goalProgressBar.setStringPainted(true);

        goalProgressBar.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        goalProgressBar.setForeground(
                purpleGlow
        );

        goalProgressBar.setBackground(
                new Color(
                        35,
                        35,
                        45
                )
        );

        goalProgressBar.setBorder(
                BorderFactory.createEmptyBorder()
        );

        // BUTTONS
        addBtn =
                new RoundedButton(
                        "Add Goal",
                        purple,
                        purpleGlow
                );

        clearBtn =
                new RoundedButton(
                        "Clear",
                        new Color(
                                180,
                                45,
                                45
                        ),
                        new Color(
                                220,
                                70,
                                70
                        )
                );

        refreshBtn =
                new RoundedButton(
                        "Refresh List",
                        purple,
                        purpleGlow
                );

        linkBtn =
                new RoundedButton(
                        "Link To Goal",
                        purple,
                        purpleGlow
                );

        // MAIN CONTENT
        JPanel content =
                new JPanel();

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setBackground(bg);

        content.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        20,
                        20,
                        20
                )
        );

        // CREATE PANEL
        JPanel createPanel =
                createCardPanel(
                        "Create Saving Goal"
                );

        createPanel.setLayout(
                new GridLayout(
                        5,
                        2,
                        12,
                        12
                )
        );

        createPanel.add(
                label("Goal Name")
        );

        createPanel.add(nameField);

        createPanel.add(
                label("Target Amount")
        );

        createPanel.add(targetField);

        createPanel.add(
                label("Deadline")
        );

        createPanel.add(deadlineField);

        createPanel.add(
                label("Goal ID")
        );

        createPanel.add(idField);

        createPanel.add(addBtn);

        createPanel.add(clearBtn);

        // LINK PANEL
        JPanel linkPanel =
                createCardPanel(
                        "Transactions & Progress"
                );

        linkPanel.setLayout(
                new GridLayout(
                        4,
                        2,
                        12,
                        12
                )
        );

        linkPanel.add(
                label("Select Transaction")
        );

        linkPanel.add(transComboBox);

        linkPanel.add(
                label("Refresh")
        );

        linkPanel.add(refreshBtn);

        linkPanel.add(
                label("Goal ID")
        );

        linkPanel.add(linkGoalIdField);

        linkPanel.add(
                label("Confirm")
        );

        linkPanel.add(linkBtn);

        // STATUS PANEL
        JPanel statusPanel =
                createCardPanel(
                        "Goal Progress"
                );

        statusPanel.setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );

        statusPanel.add(
                goalProgressBar,
                BorderLayout.NORTH
        );

        JScrollPane scrollPane =
                new JScrollPane(displayArea);

        scrollPane.setPreferredSize(
                new Dimension(500, 140)
        );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.getViewport().setBackground(
                new Color(
                        25,
                        25,
                        35
                )
        );

        scrollPane.setBackground(
                new Color(
                        25,
                        25,
                        35
                )
        );

        statusPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        content.add(createPanel);

        content.add(
                Box.createRigidArea(
                        new Dimension(
                                0,
                                18
                        )
                )
        );

        content.add(linkPanel);

        content.add(
                Box.createRigidArea(
                        new Dimension(
                                0,
                                18
                        )
                )
        );

        content.add(statusPanel);

        add(
                content,
                BorderLayout.CENTER
        );

        // ACTIONS
        addBtn.addActionListener(e -> {

            try {

                controller.addGoal(
                        nameField.getText(),
                        Double.parseDouble(
                                targetField.getText()
                        ),
                        deadlineField.getText(),
                        Integer.parseInt(
                                idField.getText()
                        )
                );

                displayArea.append(
                        "Goal Added Successfully\n"
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Check Inputs!"
                );
            }
        });

        refreshBtn.addActionListener(e -> {

            transComboBox.removeAllItems();

            TransactionRepository repo =
                    new TransactionRepository();

            for (Transaction t :
                    repo.loadTransactions()) {

                transComboBox.addItem(
                        t.getType()
                                + " : $"
                                + t.getAmount()
                                + " ("
                                + t.getId()
                                + ")"
                );
            }
        });

        linkBtn.addActionListener(e -> {

            String selected =
                    (String)
                            transComboBox
                                    .getSelectedItem();

            String gIdStr =
                    linkGoalIdField
                            .getText();

            if (selected != null
                    && !gIdStr.isEmpty()) {

                String uuid =
                        selected.substring(
                                selected.lastIndexOf("(") + 1,
                                selected.lastIndexOf(")")
                        );

                int goalId =
                        Integer.parseInt(
                                gIdStr
                        );

                linkTransactionToGoal(
                        uuid,
                        goalId
                );

                updateVisualProgress(
                        goalId
                );
            }
        });

        clearBtn.addActionListener(e -> {

            resetPlaceholder(
                    nameField,
                    "Enter Goal Name"
            );

            resetPlaceholder(
                    targetField,
                    "Enter Target Amount"
            );

            resetPlaceholder(
                    deadlineField,
                    "2026-12-31"
            );

            resetPlaceholder(
                    idField,
                    "Enter Goal ID"
            );

            resetPlaceholder(
                    linkGoalIdField,
                    "Enter Goal ID"
            );

            displayArea.setText("");

            goalProgressBar.setValue(0);
        });

        setLocationRelativeTo(null);

        setVisible(true);
    }

    // CARD PANEL
    private JPanel createCardPanel(
            String title
    ) {

        JPanel panel =
                new JPanel();

        panel.setBackground(panelBg);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        new RoundedBorder(
                                bankBlue,
                                25
                        ),
                        BorderFactory.createTitledBorder(
                                BorderFactory.createEmptyBorder(),
                                title,
                                TitledBorder.LEFT,
                                TitledBorder.TOP,
                                new Font(
                                        "Segoe UI",
                                        Font.BOLD,
                                        16
                                ),
                                bankBlue
                        )
                )
        );

        return panel;
    }

    // LABEL
    private JLabel label(
            String text
    ) {

        JLabel l =
                new JLabel(text);

        l.setForeground(Color.WHITE);

        l.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        return l;
    }

    // INPUT
    private JTextField createInput() {

        JTextField f =
                new JTextField();

        f.setPreferredSize(
                new Dimension(
                        300,
                        42
                )
        );

        f.setBackground(
                new Color(
                        25,
                        25,
                        35
                )
        );

        f.setForeground(Color.WHITE);

        f.setCaretColor(Color.WHITE);

        f.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        f.setBorder(
                new RoundedBorder(
                        bankBlue,
                        18
                )
        );

        return f;
    }

    // PLACEHOLDER
    private void addPlaceholder(
            JTextField field,
            String text
    ) {

        field.setText(text);

        field.setForeground(Color.GRAY);

        field.addFocusListener(
                new FocusAdapter() {

                    public void focusGained(
                            FocusEvent e
                    ) {

                        if (field.getText().equals(text)) {

                            field.setText("");

                            field.setForeground(
                                    Color.WHITE
                            );
                        }
                    }

                    public void focusLost(
                            FocusEvent e
                    ) {

                        if (field.getText().isEmpty()) {

                            field.setText(text);

                            field.setForeground(
                                    Color.GRAY
                            );
                        }
                    }
                }
        );
    }

    // RESET PLACEHOLDER
    private void resetPlaceholder(
            JTextField field,
            String text
    ) {

        field.setText(text);

        field.setForeground(Color.GRAY);
    }

    // UPDATE PROGRESS
    private void updateVisualProgress(
            int goalId
    ) {

        TransactionRepository repo =
                new TransactionRepository();

        List<Transaction> allTrans =
                repo.loadTransactions();

        double saved = 0;

        double target = 0;

        for (SavingGoal g :
                controller.getAllGoals()) {

            if (g.getGoalID() == goalId) {

                target =
                        g.getTargetAmount();

                break;
            }
        }

        java.util.Map<String, Integer> mappings =
                loadMappingsFromController();

        for (Transaction t : allTrans) {

            if (mappings.containsKey(t.getId())
                    &&
                    mappings.get(t.getId()) == goalId) {

                if (t.getType()
                        .equalsIgnoreCase(
                                "Income"
                        )) {

                    saved += t.getAmount();

                } else {

                    saved -= t.getAmount();
                }
            }
        }

        int percentage =
                (target > 0)
                        ?
                        (int)
                                ((saved / target) * 100)
                        :
                        0;

        if (percentage > 100)
            percentage = 100;

        if (percentage < 0)
            percentage = 0;

        goalProgressBar.setValue(
                percentage
        );

        displayArea.append(
                "Progress Updated : "
                        + percentage
                        + "%\n"
        );
    }

    private java.util.Map<String, Integer>
    loadMappingsFromController() {

        java.util.Map<String, Integer> map =
                new java.util.HashMap<>();

        try (
                java.util.Scanner r =
                        new java.util.Scanner(
                                new File(
                                        "goal_links.txt"
                                )
                        )
        ) {

            while (r.hasNextLine()) {

                String[] d =
                        r.nextLine()
                                .split(",");

                if (d.length == 2) {

                    map.put(
                            d[0],
                            Integer.parseInt(
                                    d[1]
                            )
                    );
                }
            }

        } catch (Exception ignored) {

        }

        return map;
    }

    public void linkTransactionToGoal(
            String transId,
            int goalId
    ) {

        try (
                FileWriter fw =
                        new FileWriter(
                                "goal_links.txt",
                                true
                        )
        ) {

            fw.write(
                    transId
                            + ","
                            + goalId
                            + "\n"
            );

        } catch (IOException ignored) {

        }
    }

    // BUTTON
    class RoundedButton
            extends JButton {

        private Color base;

        private Color hover;

        public RoundedButton(
                String text,
                Color base,
                Color hover
        ) {

            super(text);

            this.base = base;

            this.hover = hover;

            setFocusPainted(false);

            setContentAreaFilled(false);

            setBorderPainted(false);

            setCursor(
                    new Cursor(
                            Cursor.HAND_CURSOR
                    )
            );

            setBackground(base);

            setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            14
                    )
            );

            setForeground(Color.WHITE);

            addMouseListener(
                    new MouseAdapter() {

                        public void mouseEntered(
                                MouseEvent e
                        ) {

                            setBackground(hover);
                        }

                        public void mouseExited(
                                MouseEvent e
                        ) {

                            setBackground(base);
                        }
                    }
            );
        }

        protected void paintComponent(
                Graphics g
        ) {

            Graphics2D g2 =
                    (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(
                    getBackground()
            );

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    25,
                    25
            );

            super.paintComponent(g);
        }
    }

    // BORDER
    class RoundedBorder
            extends AbstractBorder {

        private Color color;

        private int radius;

        public RoundedBorder(
                Color color,
                int radius
        ) {

            this.color = color;

            this.radius = radius;
        }

        public void paintBorder(
                Component c,
                Graphics g,
                int x,
                int y,
                int width,
                int height
        ) {

            Graphics2D g2 =
                    (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(color);

            g2.drawRoundRect(
                    x,
                    y,
                    width - 1,
                    height - 1,
                    radius,
                    radius
            );
        }

        public Insets getBorderInsets(
                Component c
        ) {

            return new Insets(
                    10,
                    15,
                    10,
                    15
            );
        }
    }

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                GoalFrame::new
        );
    }
}*/
package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

import controller.GoalController;
import model.Transaction;
import repository.TransactionRepository;
import model.SavingGoal;

public class GoalFrame extends JFrame {

    private GoalController controller =
            new GoalController();

    private JTextField nameField,
            targetField,
            deadlineField,
            idField,
            linkGoalIdField;

    private JComboBox<String> transComboBox;

    private JTextArea displayArea;

    private JProgressBar goalProgressBar;

    private JButton addBtn,
            clearBtn,
            refreshBtn,
            linkBtn;

    Color bg =
            new Color(8, 8, 12);

    Color panelBg =
            new Color(15, 15, 22);

    Color purple =
            new Color(70, 20, 120);

    Color purpleGlow =
            new Color(130, 70, 180);

    Color bankBlue =
            new Color(120, 200, 255);

    Color textColor =
            Color.WHITE;

    public GoalFrame() {

        setTitle("Saving Goal Manager");

        setSize(700, 850);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLayout(new BorderLayout());

        getContentPane().setBackground(bg);

        JPanel topPanel =
                new JPanel(
                        new BorderLayout()
                );

        topPanel.setBackground(bg);

        topPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        20,
                        10,
                        20
                )
        );

        JPanel leftTop =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        leftTop.setOpaque(false);

        JLabel logo =
                new JLabel("🏦");

        logo.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        35
                )
        );

        logo.setForeground(bankBlue);

        JLabel title =
                new JLabel("Saving Goals");

        title.setForeground(bankBlue);

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        26
                )
        );

        leftTop.add(logo);

        leftTop.add(title);

        RoundedButton dashBtn =
                new RoundedButton(
                        "Dashboard",
                        bankBlue,
                        new Color(
                                150,
                                220,
                                255
                        )
                );

        dashBtn.setForeground(Color.BLACK);

        dashBtn.setPreferredSize(
                new Dimension(140, 42)
        );

        dashBtn.addActionListener(e -> {

            new DashboardScreen()
                    .setVisible(true);

            dispose();
        });

        topPanel.add(
                leftTop,
                BorderLayout.WEST
        );

        topPanel.add(
                dashBtn,
                BorderLayout.EAST
        );

        add(
                topPanel,
                BorderLayout.NORTH
        );

        nameField =
                createInput();

        targetField =
                createInput();

        deadlineField =
                createInput();

        idField =
                createInput();

        linkGoalIdField =
                createInput();

        addPlaceholder(
                nameField,
                "Enter Goal Name"
        );

        addPlaceholder(
                targetField,
                "Enter Target Amount"
        );

        addPlaceholder(
                deadlineField,
                "2026-12-31"
        );

        addPlaceholder(
                idField,
                "Enter Goal ID"
        );

        addPlaceholder(
                linkGoalIdField,
                "Enter Goal ID"
        );

        nameField.addActionListener(
                e -> targetField.requestFocus()
        );

        targetField.addActionListener(
                e -> deadlineField.requestFocus()
        );

        deadlineField.addActionListener(
                e -> idField.requestFocus()
        );

        idField.addActionListener(
                e -> addBtn.doClick()
        );

        transComboBox =
                new JComboBox<>();

        transComboBox.setBackground(
                new Color(25, 25, 35)
        );

        transComboBox.setForeground(
                Color.WHITE
        );

        transComboBox.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        transComboBox.setFocusable(false);

        transComboBox.setBorder(
                new RoundedBorder(
                        bankBlue,
                        18
                )
        );

        transComboBox.setRenderer(
                new DefaultListCellRenderer() {

                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus
                    ) {

                        JLabel label =
                                (JLabel)
                                        super.getListCellRendererComponent(
                                                list,
                                                value,
                                                index,
                                                isSelected,
                                                cellHasFocus
                                        );

                        label.setBorder(
                                BorderFactory.createEmptyBorder(
                                        8,
                                        12,
                                        8,
                                        12
                                )
                        );

                        if (isSelected) {

                            label.setBackground(
                                    purple
                            );

                            label.setForeground(
                                    Color.WHITE
                            );

                        } else {

                            label.setBackground(
                                    new Color(
                                            25,
                                            25,
                                            35
                                    )
                            );

                            label.setForeground(
                                    Color.WHITE
                            );
                        }

                        return label;
                    }
                }
        );

        displayArea =
                new JTextArea();

        displayArea.setEditable(false);

        displayArea.setLineWrap(true);

        displayArea.setWrapStyleWord(true);

        displayArea.setBackground(
                new Color(25, 25, 35)
        );

        displayArea.setForeground(
                Color.WHITE
        );

        displayArea.setCaretColor(
                Color.WHITE
        );

        displayArea.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        displayArea.setMargin(
                new Insets(
                        15,
                        15,
                        15,
                        15
                )
        );

        displayArea.setBorder(
                new RoundedBorder(
                        bankBlue,
                        18
                )
        );

        goalProgressBar =
                new JProgressBar(
                        0,
                        100
                );

        goalProgressBar.setValue(0);

        goalProgressBar.setStringPainted(true);

        goalProgressBar.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        goalProgressBar.setForeground(
                purpleGlow
        );

        goalProgressBar.setBackground(
                new Color(
                        35,
                        35,
                        45
                )
        );

        goalProgressBar.setBorder(
                BorderFactory.createEmptyBorder()
        );

        addBtn =
                new RoundedButton(
                        "Add Goal",
                        purple,
                        purpleGlow
                );

        clearBtn =
                new RoundedButton(
                        "Clear",
                        new Color(
                                180,
                                45,
                                45
                        ),
                        new Color(
                                220,
                                70,
                                70
                        )
                );

        refreshBtn =
                new RoundedButton(
                        "Refresh List",
                        purple,
                        purpleGlow
                );

        linkBtn =
                new RoundedButton(
                        "Link To Goal",
                        purple,
                        purpleGlow
                );

        JPanel content =
                new JPanel();

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setBackground(bg);

        content.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        20,
                        20,
                        20
                )
        );

        JPanel createPanel =
                createCardPanel(
                        "Create Saving Goal"
                );

        createPanel.setLayout(
                new GridLayout(
                        5,
                        2,
                        12,
                        12
                )
        );

        createPanel.add(label("Goal Name"));
        createPanel.add(nameField);

        createPanel.add(label("Target Amount"));
        createPanel.add(targetField);

        createPanel.add(label("Deadline"));
        createPanel.add(deadlineField);

        createPanel.add(label("Goal ID"));
        createPanel.add(idField);

        createPanel.add(addBtn);
        createPanel.add(clearBtn);

        JPanel linkPanel =
                createCardPanel(
                        "Transactions & Progress"
                );

        linkPanel.setLayout(
                new GridLayout(
                        4,
                        2,
                        12,
                        12
                )
        );

        linkPanel.add(label("Select Transaction"));
        linkPanel.add(transComboBox);

        linkPanel.add(label("Refresh"));
        linkPanel.add(refreshBtn);

        linkPanel.add(label("Goal ID"));
        linkPanel.add(linkGoalIdField);

        linkPanel.add(label("Confirm"));
        linkPanel.add(linkBtn);

        JPanel statusPanel =
                createCardPanel(
                        "Goal Progress"
                );

        statusPanel.setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );

        statusPanel.add(
                goalProgressBar,
                BorderLayout.NORTH
        );

        JScrollPane scrollPane =
                new JScrollPane(displayArea);

        scrollPane.setPreferredSize(
                new Dimension(500, 140)
        );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.getViewport().setBackground(
                new Color(
                        25,
                        25,
                        35
                )
        );

        statusPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        content.add(createPanel);

        content.add(
                Box.createRigidArea(
                        new Dimension(
                                0,
                                18
                        )
                )
        );

        content.add(linkPanel);

        content.add(
                Box.createRigidArea(
                        new Dimension(
                                0,
                                18
                        )
                )
        );

        content.add(statusPanel);

        JScrollPane mainScroll =
                new JScrollPane(content);

        mainScroll.setBorder(null);

        mainScroll.getViewport().setBackground(bg);

        mainScroll.setBackground(bg);

        mainScroll.getVerticalScrollBar().setUnitIncrement(16);

        mainScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        add(
                mainScroll,
                BorderLayout.CENTER
        );

        addBtn.addActionListener(e -> {

            try {

                controller.addGoal(
                        nameField.getText(),
                        Double.parseDouble(
                                targetField.getText()
                        ),
                        deadlineField.getText(),
                        Integer.parseInt(
                                idField.getText()
                        )
                );

                displayArea.append(
                        "Goal Added Successfully\n"
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Check Inputs!"
                );
            }
        });

        refreshBtn.addActionListener(e -> {

            transComboBox.removeAllItems();

            TransactionRepository repo =
                    new TransactionRepository();

            for (Transaction t :
                    repo.loadTransactions()) {

                transComboBox.addItem(
                        t.getType()
                                + " : $"
                                + t.getAmount()
                                + " ("
                                + t.getId()
                                + ")"
                );
            }
        });

        linkBtn.addActionListener(e -> {

            String selected =
                    (String)
                            transComboBox
                                    .getSelectedItem();

            String gIdStr =
                    linkGoalIdField
                            .getText();

            if (selected != null
                    && !gIdStr.isEmpty()) {

                String uuid =
                        selected.substring(
                                selected.lastIndexOf("(") + 1,
                                selected.lastIndexOf(")")
                        );

                int goalId =
                        Integer.parseInt(
                                gIdStr
                        );

                linkTransactionToGoal(
                        uuid,
                        goalId
                );

                updateVisualProgress(
                        goalId
                );
            }
        });

        clearBtn.addActionListener(e -> {

            resetPlaceholder(
                    nameField,
                    "Enter Goal Name"
            );

            resetPlaceholder(
                    targetField,
                    "Enter Target Amount"
            );

            resetPlaceholder(
                    deadlineField,
                    "2026-12-31"
            );

            resetPlaceholder(
                    idField,
                    "Enter Goal ID"
            );

            resetPlaceholder(
                    linkGoalIdField,
                    "Enter Goal ID"
            );

            displayArea.setText("");

            goalProgressBar.setValue(0);
        });

        setLocationRelativeTo(null);

        setVisible(true);
    }

    private JPanel createCardPanel(String title) {

        JPanel panel =
                new JPanel();

        panel.setBackground(panelBg);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        new RoundedBorder(
                                bankBlue,
                                25
                        ),
                        BorderFactory.createTitledBorder(
                                BorderFactory.createEmptyBorder(),
                                title,
                                TitledBorder.LEFT,
                                TitledBorder.TOP,
                                new Font(
                                        "Segoe UI",
                                        Font.BOLD,
                                        16
                                ),
                                bankBlue
                        )
                )
        );

        return panel;
    }

    private JLabel label(String text) {

        JLabel l =
                new JLabel(text);

        l.setForeground(Color.WHITE);

        l.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        return l;
    }

    private JTextField createInput() {

        JTextField f =
                new JTextField();

        f.setPreferredSize(
                new Dimension(
                        300,
                        42
                )
        );

        f.setBackground(
                new Color(
                        25,
                        25,
                        35
                )
        );

        f.setForeground(Color.WHITE);

        f.setCaretColor(Color.WHITE);

        f.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        f.setBorder(
                new RoundedBorder(
                        bankBlue,
                        18
                )
        );

        return f;
    }

    private void addPlaceholder(
            JTextField field,
            String text
    ) {

        field.setText(text);

        field.setForeground(Color.GRAY);

        field.addFocusListener(
                new FocusAdapter() {

                    public void focusGained(
                            FocusEvent e
                    ) {

                        if (field.getText().equals(text)) {

                            field.setText("");

                            field.setForeground(
                                    Color.WHITE
                            );
                        }
                    }

                    public void focusLost(
                            FocusEvent e
                    ) {

                        if (field.getText().isEmpty()) {

                            field.setText(text);

                            field.setForeground(
                                    Color.GRAY
                            );
                        }
                    }
                }
        );
    }

    private void resetPlaceholder(
            JTextField field,
            String text
    ) {

        field.setText(text);

        field.setForeground(Color.GRAY);
    }

    private void updateVisualProgress(
            int goalId
    ) {

        TransactionRepository repo =
                new TransactionRepository();

        List<Transaction> allTrans =
                repo.loadTransactions();

        double saved = 0;

        double target = 0;

        for (SavingGoal g :
                controller.getAllGoals()) {

            if (g.getGoalID() == goalId) {

                target =
                        g.getTargetAmount();

                break;
            }
        }

        java.util.Map<String, Integer> mappings =
                loadMappingsFromController();

        for (Transaction t : allTrans) {

            if (mappings.containsKey(t.getId())
                    &&
                    mappings.get(t.getId()) == goalId) {

                if (t.getType()
                        .equalsIgnoreCase(
                                "Income"
                        )) {

                    saved += t.getAmount();

                } else {

                    saved -= t.getAmount();
                }
            }
        }

        int percentage =
                (target > 0)
                        ?
                        (int)
                                ((saved / target) * 100)
                        :
                        0;

        if (percentage > 100)
            percentage = 100;

        if (percentage < 0)
            percentage = 0;

        goalProgressBar.setValue(
                percentage
        );

        displayArea.append(
                "Progress Updated : "
                        + percentage
                        + "%\n"
        );
    }

    private java.util.Map<String, Integer>
    loadMappingsFromController() {

        java.util.Map<String, Integer> map =
                new java.util.HashMap<>();

        try (
                java.util.Scanner r =
                        new java.util.Scanner(
                                new File(
                                        "goal_links.txt"
                                )
                        )
        ) {

            while (r.hasNextLine()) {

                String[] d =
                        r.nextLine()
                                .split(",");

                if (d.length == 2) {

                    map.put(
                            d[0],
                            Integer.parseInt(
                                    d[1]
                            )
                    );
                }
            }

        } catch (Exception ignored) {

        }

        return map;
    }

    public void linkTransactionToGoal(
            String transId,
            int goalId
    ) {

        try (
                FileWriter fw =
                        new FileWriter(
                                "goal_links.txt",
                                true
                        )
        ) {

            fw.write(
                    transId
                            + ","
                            + goalId
                            + "\n"
            );

        } catch (IOException ignored) {

        }
    }

    class RoundedButton
            extends JButton {

        private Color base;

        private Color hover;

        public RoundedButton(
                String text,
                Color base,
                Color hover
        ) {

            super(text);

            this.base = base;

            this.hover = hover;

            setFocusPainted(false);

            setContentAreaFilled(false);

            setBorderPainted(false);

            setCursor(
                    new Cursor(
                            Cursor.HAND_CURSOR
                    )
            );

            setBackground(base);

            setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            14
                    )
            );

            setForeground(Color.WHITE);

            addMouseListener(
                    new MouseAdapter() {

                        public void mouseEntered(
                                MouseEvent e
                        ) {

                            setBackground(hover);
                        }

                        public void mouseExited(
                                MouseEvent e
                        ) {

                            setBackground(base);
                        }
                    }
            );
        }

        protected void paintComponent(
                Graphics g
        ) {

            Graphics2D g2 =
                    (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(
                    getBackground()
            );

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    25,
                    25
            );

            super.paintComponent(g);
        }
    }

    class RoundedBorder
            extends AbstractBorder {

        private Color color;

        private int radius;

        public RoundedBorder(
                Color color,
                int radius
        ) {

            this.color = color;

            this.radius = radius;
        }

        public void paintBorder(
                Component c,
                Graphics g,
                int x,
                int y,
                int width,
                int height
        ) {

            Graphics2D g2 =
                    (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(color);

            g2.drawRoundRect(
                    x,
                    y,
                    width - 1,
                    height - 1,
                    radius,
                    radius
            );
        }

        public Insets getBorderInsets(
                Component c
        ) {

            return new Insets(
                    10,
                    15,
                    10,
                    15
            );
        }
    }

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                GoalFrame::new
        );
    }
}

