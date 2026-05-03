package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.Objects;

/**
 * The central design system for the FYP Management Platform.
 * Uses a FAST-NU inspired blue, white, and amber palette.
 */
public class Theme {
    // Color Palette
    public static final Color BG = new Color(244, 248, 252); // #F4F8FC
    public static final Color SURFACE = Color.WHITE; // #FFFFFF
    public static final Color SURFACE_2 = new Color(226, 237, 249); // #E2EDF9
    public static final Color BORDER = new Color(194, 211, 232); // #C2D3E8
    public static final Color TEXT = new Color(17, 17, 17); // #111111 — general labels/copy
    public static final Color TEXT_MUTED = new Color(68, 68, 68); // #444444

    /** High-contrast input surfaces (readable on white / light grey). */
    public static final Color COLOR_BG_INPUT = Color.WHITE;
    public static final Color COLOR_BG_INPUT_DISABLED = new Color(243, 244, 246);
    public static final Color COLOR_TEXT_PRIMARY = new Color(30, 30, 30);
    public static final Color COLOR_TEXT_DISABLED = new Color(95, 99, 110);
    /** List / combo dropdown selection (light tint + dark text). */
    public static final Color COLOR_SELECTION_LIGHT = new Color(222, 234, 250);

    public static final Color FAST_BLUE = new Color(0, 76, 153); // #004C99
    public static final Color FAST_BLUE_DARK = new Color(0, 42, 92); // #002A5C
    public static final Color FAST_BLUE_LIGHT = new Color(18, 122, 207); // #127ACF
    public static final Color ACCENT = new Color(245, 179, 31); // #F5B31F
    public static final Color SUCCESS = new Color(29, 132, 89); // #1D8459
    public static final Color WARNING = new Color(184, 116, 24); // #B87418
    public static final Color DANGER = new Color(190, 55, 55); // #BE3737

    // Fonts (Refined Hierarchy)
    public static final Font MAIN_FONT = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font BOLD_FONT = new Font("SansSerif", Font.BOLD, 14);
    public static final Font LOGO_FONT = new Font("Serif", Font.PLAIN, 32);
    public static final Font HEADING_FONT = new Font("Serif", Font.PLAIN, 24);
    public static final Font SUBHEADING_FONT = new Font("SansSerif", Font.BOLD, 18);
    public static final Font MONO_FONT = new Font("Monospaced", Font.PLAIN, 12);

    // Spacing & Radius
    public static final int GRID = 8;
    public static final int RADIUS = 8;
    public static final Border FIELD_PADDING = new EmptyBorder(12, 16, 12, 16);
    public static final Border SIDEBAR_PADDING = new EmptyBorder(12, 20, 12, 20);

    /** Login / compact forms: fields stay readable and do not span the full monitor width. */
    public static final int FORM_FIELD_WIDTH = 380;
    public static final int FORM_CARD_MAX_WIDTH = 460;
    /** Dashboard quick-action outline buttons share one column width. */
    public static final int DASHBOARD_ACTION_MIN_BUTTON_WIDTH = 136;

    public static void installGlobalDefaults() {
        Color bgIn = COLOR_BG_INPUT;
        Color fgIn = COLOR_TEXT_PRIMARY;
        UIManager.put("TextField.background", bgIn);
        UIManager.put("TextField.foreground", fgIn);
        UIManager.put("TextField.caretForeground", fgIn);
        UIManager.put("TextField.inactiveBackground", COLOR_BG_INPUT_DISABLED);
        UIManager.put("TextField.inactiveForeground", COLOR_TEXT_DISABLED);
        UIManager.put("PasswordField.background", bgIn);
        UIManager.put("PasswordField.foreground", fgIn);
        UIManager.put("PasswordField.caretForeground", fgIn);
        UIManager.put("PasswordField.inactiveBackground", COLOR_BG_INPUT_DISABLED);
        UIManager.put("PasswordField.inactiveForeground", COLOR_TEXT_DISABLED);
        UIManager.put("TextArea.background", bgIn);
        UIManager.put("TextArea.foreground", fgIn);
        UIManager.put("TextArea.caretForeground", fgIn);
        UIManager.put("TextPane.background", bgIn);
        UIManager.put("TextPane.foreground", fgIn);
        UIManager.put("EditorPane.background", bgIn);
        UIManager.put("EditorPane.foreground", fgIn);
        UIManager.put("FormattedTextField.background", bgIn);
        UIManager.put("FormattedTextField.foreground", fgIn);
        UIManager.put("Button.background", bgIn);
        UIManager.put("Button.foreground", fgIn);
        UIManager.put("Button.select", COLOR_SELECTION_LIGHT);
        UIManager.put("Spinner.background", bgIn);
        UIManager.put("Spinner.foreground", fgIn);
        UIManager.put("Table.background", COLOR_BG_INPUT);
        UIManager.put("Table.foreground", COLOR_TEXT_PRIMARY);
        UIManager.put("Table.selectionBackground", COLOR_SELECTION_LIGHT);
        UIManager.put("Table.selectionForeground", COLOR_TEXT_PRIMARY);
        UIManager.put("TableHeader.background", SURFACE);
        UIManager.put("TableHeader.foreground", TEXT);
        UIManager.put("ScrollPane.background", Color.WHITE);
        UIManager.put("ScrollBar.track", Color.WHITE);
        UIManager.put("ScrollBar.thumb", SURFACE_2);
        UIManager.put("ComboBox.background", bgIn);
        UIManager.put("ComboBox.foreground", fgIn);
        UIManager.put("ComboBox.selectionBackground", COLOR_SELECTION_LIGHT);
        UIManager.put("ComboBox.selectionForeground", fgIn);
        UIManager.put("ComboBox.disabledBackground", COLOR_BG_INPUT_DISABLED);
        UIManager.put("ComboBox.disabledForeground", COLOR_TEXT_DISABLED);
        UIManager.put("List.background", SURFACE);
        UIManager.put("List.foreground", TEXT);
        UIManager.put("List.selectionBackground", SURFACE);
        UIManager.put("List.selectionForeground", TEXT);
        UIManager.put("TabbedPane.background", SURFACE);
        UIManager.put("TabbedPane.contentAreaColor", SURFACE);
        UIManager.put("TabbedPane.selected", SURFACE);
        UIManager.put("OptionPane.background", BG);
    }

    public static void maximizeFrame(JFrame frame) {
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * Styles a component with the app background.
     */
    public static void applyTheme(JComponent component) {
        component.setBackground(BG);
        component.setForeground(TEXT);
        component.setOpaque(true);
    }

    /**
     * Styles a component as a surface (elevated card).
     */
    public static void applySurface(JComponent component) {
        component.setBackground(SURFACE);
        component.setForeground(TEXT);
        component.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        component.setOpaque(true);
    }

    /**
     * Creates a styled primary button.
     */
    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFont(BOLD_FONT);
        btn.setBackground(FAST_BLUE);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setRolloverEnabled(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorderPainted(false);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(FAST_BLUE_LIGHT);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(FAST_BLUE);
            }
        });

        return btn;
    }

    /**
     * Creates a styled outline button.
     */
    public static JButton createOutlineButton(String text) {
        JButton btn = new JButton(text);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFont(MAIN_FONT);
        btn.setBackground(COLOR_BG_INPUT);
        btn.setForeground(COLOR_TEXT_PRIMARY);
        btn.setFocusPainted(false);
        btn.setRolloverEnabled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorderPainted(true);

        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(SURFACE_2);
                btn.setForeground(COLOR_TEXT_PRIMARY);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(FAST_BLUE, 1),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(COLOR_BG_INPUT);
                btn.setForeground(COLOR_TEXT_PRIMARY);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)));
            }
        });

        return btn;
    }

    private static void syncTextInputColors(JTextField field) {
        boolean on = field.isEnabled();
        field.setOpaque(true);
        if (on) {
            field.setBackground(COLOR_BG_INPUT);
            field.setForeground(COLOR_TEXT_PRIMARY);
            field.setCaretColor(COLOR_TEXT_PRIMARY);
            field.setDisabledTextColor(COLOR_TEXT_DISABLED);
        } else {
            field.setBackground(COLOR_BG_INPUT_DISABLED);
            field.setForeground(COLOR_TEXT_DISABLED);
            field.setCaretColor(COLOR_TEXT_DISABLED);
        }
    }

    /**
     * Styles a text field with the app theme.
     */
    public static void styleTextField(JTextField field) {
        if (field instanceof JPasswordField) {
            field.setUI(new javax.swing.plaf.basic.BasicPasswordFieldUI());
        } else {
            field.setUI(new javax.swing.plaf.basic.BasicTextFieldUI());
        }
        field.setFont(MAIN_FONT);
        field.setSelectionColor(COLOR_SELECTION_LIGHT);
        field.setSelectedTextColor(COLOR_TEXT_PRIMARY);
        field.setDisabledTextColor(COLOR_TEXT_DISABLED);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        field.addPropertyChangeListener("enabled", e -> syncTextInputColors(field));
        syncTextInputColors(field);
        int h = field.getPreferredSize().height;
        boolean inComboPopup = false;
        for (Container p = field.getParent(); p != null; p = p.getParent()) {
            if (p instanceof JComboBox) {
                inComboPopup = true;
                break;
            }
        }
        if (!inComboPopup) {
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, Math.max(h, 44)));
        }
    }

    private static void syncTextAreaColors(JTextArea area) {
        boolean on = area.isEnabled();
        area.setOpaque(true);
        if (on) {
            area.setBackground(COLOR_BG_INPUT);
            area.setForeground(COLOR_TEXT_PRIMARY);
            area.setCaretColor(COLOR_TEXT_PRIMARY);
        } else {
            area.setBackground(COLOR_BG_INPUT_DISABLED);
            area.setForeground(COLOR_TEXT_DISABLED);
            area.setCaretColor(COLOR_TEXT_DISABLED);
        }
    }

    public static void styleTextArea(JTextArea area) {
        area.setUI(new javax.swing.plaf.basic.BasicTextAreaUI());
        area.setFont(MAIN_FONT);
        area.setSelectionColor(COLOR_SELECTION_LIGHT);
        area.setSelectedTextColor(COLOR_TEXT_PRIMARY);
        area.setDisabledTextColor(COLOR_TEXT_DISABLED);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        area.addPropertyChangeListener("enabled", e -> syncTextAreaColors(area));
        syncTextAreaColors(area);
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(MAIN_FONT);
        comboBox.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        comboBox.setOpaque(true);
        comboBox.setBackground(COLOR_BG_INPUT);
        comboBox.setForeground(COLOR_TEXT_PRIMARY);
        comboBox.addPropertyChangeListener("enabled", e -> applyComboEditableColors(comboBox));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setFont(MAIN_FONT);
                l.setOpaque(true);
                if (comboBox.isEnabled()) {
                    if (isSelected) {
                        l.setBackground(COLOR_SELECTION_LIGHT);
                        l.setForeground(COLOR_TEXT_PRIMARY);
                        l.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(FAST_BLUE, 1),
                                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
                    } else {
                        l.setBackground(COLOR_BG_INPUT);
                        l.setForeground(COLOR_TEXT_PRIMARY);
                        l.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
                    }
                } else {
                    l.setBackground(COLOR_BG_INPUT_DISABLED);
                    l.setForeground(COLOR_TEXT_DISABLED);
                    l.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
                }
                return l;
            }
        });
        Component ec = comboBox.getEditor().getEditorComponent();
        if (ec instanceof JTextField) {
            styleTextField((JTextField) ec);
            ((JTextField) ec).setHorizontalAlignment(SwingConstants.LEFT);
        }
        applyComboEditableColors(comboBox);
        int h = comboBox.getPreferredSize().height;
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, Math.max(h, 36)));
    }

    private static void applyComboEditableColors(JComboBox<?> comboBox) {
        comboBox.setBackground(comboBox.isEnabled() ? COLOR_BG_INPUT : COLOR_BG_INPUT_DISABLED);
        comboBox.setForeground(comboBox.isEnabled() ? COLOR_TEXT_PRIMARY : COLOR_TEXT_DISABLED);
    }

    private static void applySpinnerContrast(JSpinner spinner) {
        spinner.setBackground(spinner.isEnabled() ? COLOR_BG_INPUT : COLOR_BG_INPUT_DISABLED);
        spinner.setForeground(spinner.isEnabled() ? COLOR_TEXT_PRIMARY : COLOR_TEXT_DISABLED);
    }

    private static void polishSpinnerButtons(Container subtree, boolean spinnerEnabled) {
        for (Component ch : subtree.getComponents()) {
            if (ch instanceof JButton) {
                JButton b = (JButton) ch;
                b.setUI(new BasicButtonUI());
                b.setOpaque(true);
                boolean on = spinnerEnabled && b.isEnabled();
                b.setBackground(on ? COLOR_BG_INPUT : COLOR_BG_INPUT_DISABLED);
                b.setForeground(on ? COLOR_TEXT_PRIMARY : COLOR_TEXT_DISABLED);
            } else if (ch instanceof Container) {
                polishSpinnerButtons((Container) ch, spinnerEnabled);
            }
        }
    }

    public static void styleSpinner(JSpinner spinner) {
        spinner.setFont(MAIN_FONT);
        spinner.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        spinner.setOpaque(true);
        spinner.addPropertyChangeListener("enabled", e -> {
            applySpinnerContrast(spinner);
            polishSpinnerButtons(spinner, spinner.isEnabled());
        });
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField field = ((JSpinner.DefaultEditor) editor).getTextField();
            styleTextField(field);
            field.setHorizontalAlignment(SwingConstants.LEFT);
        }
        spinner.addHierarchyListener(e -> {
            if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.DISPLAYABILITY_CHANGED) != 0 && spinner.isDisplayable()) {
                polishSpinnerButtons(spinner, spinner.isEnabled());
                applySpinnerContrast(spinner);
            }
        });
        applySpinnerContrast(spinner);
        SwingUtilities.invokeLater(() -> {
            polishSpinnerButtons(spinner, spinner.isEnabled());
            applySpinnerContrast(spinner);
        });
    }

    public static void styleList(JList<?> list) {
        list.setFont(MAIN_FONT);
        list.setFixedCellHeight(32);
        list.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        list.setSelectionBackground(COLOR_SELECTION_LIGHT);
        list.setSelectionForeground(COLOR_TEXT_PRIMARY);
        list.setBackground(COLOR_BG_INPUT);
        list.setForeground(COLOR_TEXT_PRIMARY);
        list.setOpaque(true);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> lst, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(lst, value, index, isSelected, cellHasFocus);
                l.setFont(MAIN_FONT);
                l.setOpaque(true);
                if (!lst.isEnabled()) {
                    l.setBackground(COLOR_BG_INPUT_DISABLED);
                    l.setForeground(COLOR_TEXT_DISABLED);
                    l.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
                    return l;
                }
                if (isSelected) {
                    l.setBackground(COLOR_SELECTION_LIGHT);
                    l.setForeground(COLOR_TEXT_PRIMARY);
                    l.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(FAST_BLUE, 1),
                            BorderFactory.createEmptyBorder(4, 10, 4, 10)));
                } else {
                    l.setBackground(COLOR_BG_INPUT);
                    l.setForeground(COLOR_TEXT_PRIMARY);
                    l.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
                }
                return l;
            }
        });
    }

    /**
     * White grid body, white header row with dark text (no system-grey header strip).
     */
    public static void styleTable(JTable table) {
        table.setBackground(COLOR_BG_INPUT);
        table.setForeground(COLOR_TEXT_PRIMARY);
        table.setSelectionBackground(COLOR_SELECTION_LIGHT);
        table.setSelectionForeground(COLOR_TEXT_PRIMARY);
        table.setGridColor(BORDER);
        table.setFont(MAIN_FONT);
        table.setRowHeight(30);
        table.setFillsViewportHeight(false);
        table.setShowGrid(true);
        table.setOpaque(true);
        applyFlatTableHeader(table);
    }

    public static void applyFlatTableHeader(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setOpaque(true);
        header.setBackground(COLOR_BG_INPUT);
        header.setForeground(COLOR_TEXT_PRIMARY);
        header.setFont(BOLD_FONT);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        DefaultTableCellRenderer hr = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                l.setBackground(COLOR_BG_INPUT);
                l.setForeground(COLOR_TEXT_PRIMARY);
                l.setFont(BOLD_FONT);
                l.setOpaque(true);
                l.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                return l;
            }
        };
        hr.setHorizontalAlignment(SwingConstants.LEFT);
        header.setDefaultRenderer(hr);
    }

    /**
     * Styles a scroll pane: white viewport for lists/forms (avoids system-grey filler).
     * Use {@link #styleScrollPane(JScrollPane, boolean)} with {@code true} for dashboard page chrome.
     */
    public static void styleScrollPane(JScrollPane scrollPane) {
        styleScrollPane(scrollPane, false);
    }

    public static void styleScrollPane(JScrollPane scrollPane, boolean workspacePageChrome) {
        Color chrome = workspacePageChrome ? BG : SURFACE;
        scrollPane.setBackground(chrome);
        scrollPane.getViewport().setBackground(chrome);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setBackground(SURFACE_2);
        scrollPane.getHorizontalScrollBar().setBackground(SURFACE_2);
    }

    /**
     * Creates and adds a styled sidebar navigation button to a panel.
     */
    public static void addSidebarButton(JPanel sidebar, String text, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFont(MAIN_FONT);
        btn.setForeground(new Color(223, 235, 250));
        btn.setBackground(FAST_BLUE_DARK);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setForeground(Color.WHITE);
                btn.setContentAreaFilled(true);
                btn.setBackground(FAST_BLUE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setForeground(new Color(223, 235, 250));
                btn.setContentAreaFilled(false);
            }
        });

        if (listener != null)
            btn.addActionListener(listener);
        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(2));
    }

    public static void addSidebarSectionLabel(JPanel sidebar, String text) {
        JLabel label = new JLabel(text.toUpperCase());
        label.setFont(new Font("SansSerif", Font.BOLD, 11));
        label.setForeground(new Color(166, 196, 230));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(18, 15, 6, 15));
        sidebar.add(label);
    }

    public static JPanel createActionSection(String title) {
        return createActionSection(title, false);
    }

    /**
     * @param onLightSurface {@code true} when section sits on white dashboard card (avoid grey patches).
     */
    public static JPanel createActionSection(String title, boolean onLightSurface) {
        JPanel section = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }
        };
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(onLightSurface ? SURFACE : BG);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(SUBHEADING_FONT);
        titleLabel.setForeground(FAST_BLUE_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(titleLabel);
        section.add(Box.createVerticalStrut(8));
        return section;
    }

    /**
     * Action buttons in a fixed column count without {@link GridLayout} stretching every row to one height.
     */
    public static JPanel buildActionButtonGrid(int columns, JButton... buttons) {
        Objects.requireNonNull(buttons);
        normalizeDashboardActionButtons(buttons);
        JPanel grid = new JPanel(new GridBagLayout()) {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }
        };
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        int colCount = Math.max(1, columns);
        for (int i = 0; i < buttons.length; i++) {
            JButton btn = buttons[i];
            if (btn == null) continue;
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = i % colCount;
            gbc.gridy = i / colCount;
            gbc.insets = new Insets(6, 6, 6, 6);
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
            gbc.weighty = 0;
            grid.add(btn, gbc);
        }
        return grid;
    }

    public static void styleLabel(JLabel label, boolean isHeading) {
        label.setForeground(TEXT);
        label.setFont(isHeading ? HEADING_FONT : MAIN_FONT);
    }

    /** Separator under dashboard page titles (replaces floating header). */
    public static Border dashboardHeaderSeparator() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                BorderFactory.createEmptyBorder(0, 0, 16, 0));
    }

    /**
     * Vertical {@link BoxLayout} gives unlimited vertical growth to children by default; wrap panels
     * that should stay at their preferred height (stats row, form cards).
     */
    public static JPanel wrapNaturalHeight(JComponent child) {
        JPanel wrap = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }
        };
        wrap.setOpaque(false);
        wrap.add(child, BorderLayout.CENTER);
        return wrap;
    }

    /** Single-line fields: fixed width, natural height (after {@link #styleTextField}). */
    public static void constrainFormFieldWidth(JTextField field, int widthPx) {
        Dimension p = field.getPreferredSize();
        p.width = widthPx;
        field.setPreferredSize(new Dimension(widthPx, p.height));
        field.setMaximumSize(new Dimension(widthPx, Math.max(p.height, 44)));
    }

    public static void constrainComboWidth(JComboBox<?> combo, int widthPx) {
        Dimension p = combo.getPreferredSize();
        combo.setPreferredSize(new Dimension(widthPx, p.height));
        combo.setMaximumSize(new Dimension(widthPx, Math.max(p.height, 36)));
    }

    public static void constrainSpinnerWidth(JSpinner spinner, int widthPx) {
        Dimension p = spinner.getPreferredSize();
        spinner.setPreferredSize(new Dimension(widthPx, p.height));
        spinner.setMaximumSize(new Dimension(widthPx, p.height));
    }

    /** Centers a single component in a {@link GridBagLayout} host (typical form shell). */
    public static JPanel wrapCentered(JComponent inner) {
        JPanel host = new JPanel(new GridBagLayout());
        host.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        host.add(inner, c);
        return host;
    }

    public static JPanel createFormCardShell() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(28, 32, 28, 32)));
        return card;
    }

    static void normalizeDashboardActionButtons(JButton... buttons) {
        int w = DASHBOARD_ACTION_MIN_BUTTON_WIDTH;
        for (JButton b : buttons) {
            if (b != null) {
                w = Math.max(w, b.getPreferredSize().width + 4);
            }
        }
        for (JButton b : buttons) {
            if (b == null) continue;
            Dimension d = b.getPreferredSize();
            b.setPreferredSize(new Dimension(w, d.height));
            b.setMinimumSize(new Dimension(w, d.height));
        }
    }

    /** White elevated panel for grouped dashboard shortcuts. */
    public static JPanel createDashboardQuickLinksShell() {
        JPanel shell = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }
        };
        shell.setLayout(new BoxLayout(shell, BoxLayout.Y_AXIS));
        shell.setAlignmentX(Component.LEFT_ALIGNMENT);
        shell.setBackground(SURFACE);
        shell.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(24, 28, 28, 28)));
        return shell;
    }

    /**
     * Helper to create a themed modal dialog for "Quick Actions".
     */
    public static JDialog createModalDialog(Window parent, String title, int width, int height) {
        JDialog dialog = new JDialog(parent, title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(BG);
        return dialog;
    }

    /**
     * Show a modern non-blocking notification (Toast style simulation).
     */
    public static void showNotification(Component parent, String message) {
        Window parentWindow = SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(parentWindow);
        dialog.setUndecorated(true);
        dialog.setModal(false);
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(SURFACE);
        content.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(FAST_BLUE, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel label = new JLabel(message);
        label.setForeground(TEXT);
        label.setFont(BOLD_FONT);
        content.add(label, BorderLayout.CENTER);
        
        dialog.add(content);
        dialog.pack();
        
        if (parentWindow != null) {
            dialog.setLocationRelativeTo(parentWindow);
            Point p = dialog.getLocation();
            // Offset to show near bottom center of parent
            dialog.setLocation(p.x, p.y + (parentWindow.getHeight() / 2) - 100);
        }

        Timer timer = new Timer(2500, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
        
        dialog.setVisible(true);
    }

    /**
     * Wraps an ActionListener with tracing logic.
     */
    public static java.awt.event.ActionListener traceActionListener(String component, String actionName, java.awt.event.ActionListener original) {
        return e -> {
            util.DiagnosticService.getInstance().trace("UI-Event", "Action Triggered: " + actionName + " in " + component);
            if (original != null) original.actionPerformed(e);
        };
    }
}
