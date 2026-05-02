package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * The central design system for the FYP Management Platform.
 * Implements the "Monolithic Serenity" aesthetic in Java Swing.
 */
public class Theme {
    // Color Palette
    public static final Color BG = new Color(13, 15, 20); // #0D0F14
    public static final Color SURFACE = new Color(19, 22, 30); // #13161E
    public static final Color SURFACE_2 = new Color(28, 32, 48); // #1C2030
    public static final Color BORDER = new Color(37, 42, 58); // #252A3A
    public static final Color TEXT = new Color(232, 234, 240); // #E8EAF0
    public static final Color TEXT_MUTED = new Color(122, 128, 153); // #7A8099

    public static final Color ACCENT = new Color(217, 79, 46); // #D94F2E (Vermillion)
    public static final Color SUCCESS = new Color(46, 168, 107); // #2EA86B
    public static final Color WARNING = new Color(217, 140, 46); // #D98C2E
    public static final Color DANGER = new Color(217, 79, 46); // #D94F2E

    // Fonts (Refined Hierarchy)
    public static final Font MAIN_FONT = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font BOLD_FONT = new Font("SansSerif", Font.BOLD, 14);
    public static final Font LOGO_FONT = new Font("Serif", Font.PLAIN, 32);
    public static final Font HEADING_FONT = new Font("Serif", Font.PLAIN, 24);
    public static final Font SUBHEADING_FONT = new Font("SansSerif", Font.BOLD, 18);
    public static final Font MONO_FONT = new Font("Monospaced", Font.PLAIN, 12);

    // Spacing & Radius
    public static final int GRID = 8;
    public static final int RADIUS = 12;
    public static final Border FIELD_PADDING = new EmptyBorder(12, 16, 12, 16);
    public static final Border SIDEBAR_PADDING = new EmptyBorder(12, 20, 12, 20);

    /**
     * Styles a component with the dark background.
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
     * Creates a styled primary button (Vermillion).
     */
    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(BOLD_FONT);
        btn.setBackground(ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorderPainted(false);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(ACCENT.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(ACCENT);
            }
        });

        return btn;
    }

    /**
     * Creates a styled outline button.
     */
    public static JButton createOutlineButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(MAIN_FONT);
        btn.setBackground(BG);
        btn.setForeground(TEXT);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(SURFACE);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT, 1),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(BG);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)));
            }
        });

        return btn;
    }

    /**
     * Styles a text field with the dark theme.
     */
    public static void styleTextField(JTextField field) {
        field.setBackground(SURFACE);
        field.setForeground(TEXT);
        field.setCaretColor(ACCENT);
        field.setFont(MAIN_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));
    }

    /**
     * Styles a scroll pane.
     */
    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBackground(BG);
        scrollPane.getViewport().setBackground(BG);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setBackground(BG);
    }

    /**
     * Creates and adds a styled sidebar navigation button to a panel.
     */
    public static void addSidebarButton(JPanel sidebar, String text, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFont(MAIN_FONT);
        btn.setForeground(TEXT_MUTED);
        btn.setBackground(SURFACE);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setForeground(TEXT);
                btn.setContentAreaFilled(true);
                btn.setBackground(SURFACE_2);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setForeground(TEXT_MUTED);
                btn.setContentAreaFilled(false);
            }
        });

        if (listener != null)
            btn.addActionListener(listener);
        sidebar.add(btn);
        sidebar.add(Box.createVerticalStrut(2));
    }

    public static void styleLabel(JLabel label, boolean isHeading) {
        label.setForeground(TEXT);
        label.setFont(isHeading ? HEADING_FONT : MAIN_FONT);
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
            BorderFactory.createLineBorder(ACCENT, 1),
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
