

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage {
    private JFrame frame;
    private JComboBox<String> choiceComboBox;
    private JComboBox<String> sizeComboBox;

    public HomePage() {
        frame = new JFrame("AI Home Page");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set layout manager
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a panel for the top right corner
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exitButton = new JButton("X");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        topPanel.add(exitButton);

        // Add topPanel to the main panel at the PAGE_START position
        mainPanel.add(topPanel, BorderLayout.PAGE_START);

        // Create a panel for the center area
        JPanel centerPanel = new JPanel();
        //centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Add button for "Human" choice
        JButton humanButton = new JButton("Human");
        humanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your logic for the "Human" button click
            }
        });
        // Set preferred size to make the button smaller
        humanButton.setPreferredSize(new Dimension(80, 30));

        // Add "VS" label
        JLabel vsLabel = new JLabel("VS");
        JLabel titleLabel = new JLabel("Home Page ");

        // Add combo box for human or AI choice
        String[] choices = {"Human", "AI", "AIlvl1", "AIlvl2"};
        choiceComboBox = new JComboBox<>(choices);
        // Set preferred size to make the combo box smaller
        choiceComboBox.setPreferredSize(new Dimension(90, 30));

        // Create a panel for size board
        JPanel sizePanel = new JPanel();
        JLabel sizeLabel = new JLabel("Size Board: ");
        String[] sizeOptions = {"3x3", "8x8"};
        sizeComboBox = new JComboBox<>(sizeOptions);
        sizePanel.add(sizeLabel);
        sizePanel.add(sizeComboBox);

        // Add components to the center panel
        centerPanel.add(titleLabel);
        centerPanel.add(humanButton); // Add the "Human" button
        centerPanel.add(vsLabel);     // Add the "VS" label
        centerPanel.add(choiceComboBox); // Add the JComboBox

        // Add size panel
        centerPanel.add(sizePanel);

        // Add centerPanel to the main panel at the CENTER position
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Add main panel to the frame
        frame.add(mainPanel);

        // Set frame properties
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); // Center the frame on the screen
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HomePage();
            }
  });
}
}
