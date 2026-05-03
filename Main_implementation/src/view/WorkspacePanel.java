package view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

/**
 * Single-window workspace for dashboard modules.
 * Existing feature screens are still regular JFrames, but dashboard navigation
 * embeds their content here so users are not forced into many top-level windows.
 */
public class WorkspacePanel extends JPanel {
    private static final String HOME = "home";
    private static final String MODULE = "module";

    private final CardLayout cards;
    private final JPanel moduleHost;
    private final JLabel titleLabel;
    private JFrame currentFrame;
    private boolean suppressFrameClose;

    public WorkspacePanel(JComponent homeView) {
        this.cards = new CardLayout();
        this.moduleHost = new JPanel(new BorderLayout());
        this.titleLabel = new JLabel("Module");

        setLayout(cards);
        Theme.applyTheme(this);

        add(homeView, HOME);
        add(createModuleShell(), MODULE);
        cards.show(this, HOME);
    }

    public void showHome() {
        closeCurrentFrame();
        moduleHost.removeAll();
        cards.show(this, HOME);
        revalidate();
        repaint();
    }

    public void openFrame(String title, Supplier<JFrame> frameFactory) {
        closeCurrentFrame();
        JFrame frame = frameFactory.get();
        Container frameContent = frame.getContentPane();
        frame.setContentPane(new JPanel());
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (!suppressFrameClose && currentFrame == frame) {
                    currentFrame = null;
                    showHome();
                }
            }
        });
        currentFrame = frame;
        wireReturnButtons(frameContent);

        titleLabel.setText(title);
        moduleHost.removeAll();
        moduleHost.add(frameContent, BorderLayout.CENTER);
        cards.show(this, MODULE);
        revalidate();
        repaint();
    }

    private void closeCurrentFrame() {
        if (currentFrame == null) return;
        suppressFrameClose = true;
        currentFrame.dispose();
        suppressFrameClose = false;
        currentFrame = null;
    }

    private void wireReturnButtons(Component component) {
        if (component instanceof JButton) {
            JButton button = (JButton) component;
            String text = button.getText() == null ? "" : button.getText().trim().toLowerCase();
            if (text.equals("close") || text.equals("cancel") || text.equals("back") || text.equals("done")) {
                button.addActionListener(e -> showHome());
            }
        }
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                wireReturnButtons(child);
            }
        }
    }

    private JComponent createModuleShell() {
        JPanel shell = new JPanel(new BorderLayout());
        Theme.applyTheme(shell);

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(Theme.BG);
        toolbar.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JButton backButton = Theme.createOutlineButton("Back to Dashboard");
        backButton.addActionListener(e -> showHome());
        toolbar.add(backButton, BorderLayout.WEST);

        titleLabel.setFont(Theme.SUBHEADING_FONT);
        titleLabel.setForeground(Theme.TEXT);
        titleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        toolbar.add(titleLabel, BorderLayout.EAST);

        shell.add(toolbar, BorderLayout.NORTH);
        moduleHost.setBackground(Theme.BG);
        shell.add(moduleHost, BorderLayout.CENTER);
        return shell;
    }
}
