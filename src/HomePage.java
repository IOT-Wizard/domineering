import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage {
    private JFrame frame;
    private static String selectedSize;
    private static String player;
    private JComboBox<String> choiceComboBox;
    private JComboBox<String> sizeComboBox;

    public HomePage() {
        frame = new JFrame("Home Page");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set layout manager
        JPanel mainPanel = new JPanel(new GridLayout(4, 1));
        JPanel zeroRowPanel = new JPanel(new FlowLayout());
        JPanel firstRowPanel = new JPanel(new FlowLayout());
        JPanel secondRowPanel = new JPanel(new FlowLayout());
        JPanel thirdRowPanel = new JPanel(new FlowLayout());









        // Add button for "Start a New Game"
        JButton newGameButton = new JButton("Start a New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to BoardPage when the button is clicked
                player = (String)choiceComboBox.getSelectedItem();
                selectedSize = (String) sizeComboBox.getSelectedItem();
                if (selectedSize != null) {
                    frame.dispose();
                    new Board(selectedSize, player).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a board size.");
                }
            }
        });
        JLabel wLabel=new JLabel("Greetings, Game Explorer!");
        JButton humanButton = new JButton("Human");
        humanButton.setPreferredSize(new Dimension(80, 30));
        // Add "VS" label
        JLabel vsLabel = new JLabel("VS");


        // Add combo box for human or AI choice
        String[] choices = {"Human", "AI", "AIlvl1", "AIlvl2"};
        choiceComboBox = new JComboBox<>(choices);
        choiceComboBox.setPreferredSize(new Dimension(90, 30));

        // Create a panel for size board
        JPanel sizePanel = new JPanel();
        JLabel sizeLabel = new JLabel("Size Board: ");
        String[] sizeOptions = {"3x3","4x4","5x5","8x8"};
        sizeComboBox = new JComboBox<>(sizeOptions);
        sizePanel.add(sizeLabel);
        sizePanel.add(sizeComboBox);





        choiceComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSize = (String) sizeComboBox.getSelectedItem();
                if (selectedSize != null) {
                    // Transférer la taille sélectionnée à BoardPage
                    new Board(selectedSize , player );
                }

            }
        });


        newGameButton.setBackground(new Color(0xDDE0EE));
        humanButton.setBackground(new Color(0xA3AEDA));
        choiceComboBox.setBackground(new Color(0xA3AEDA));
        // Add components to the MAIN panel
        zeroRowPanel.add(wLabel);
        firstRowPanel.add(humanButton);
        firstRowPanel.add(vsLabel);
        firstRowPanel.add(choiceComboBox);
        thirdRowPanel.add(sizePanel);
        mainPanel.add(zeroRowPanel);
        mainPanel.add(firstRowPanel);
        mainPanel.add(thirdRowPanel);

        // Add newGameButton to the second row of mainPanel
        secondRowPanel.add(newGameButton);
        // Add secondRowPanel to the mainPanel
        mainPanel.add(secondRowPanel);

        // Add main panel to the frame
        frame.add(mainPanel);

        // Set frame properties
        frame.setVisible(true);

    }
    public static String getSelectedSize() {
        return selectedSize;
    }
    public static String getSelectedPlayer() {
        return player;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HomePage();
            }});
    }
}