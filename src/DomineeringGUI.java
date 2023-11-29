import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DomineeringGUI extends JFrame {

    private DomineeringPosition currentPosition;
    private boolean humanTurn;

    private JButton[] buttons;

    public DomineeringGUI(int size) {
        super("Domineering Game");
        currentPosition = new DomineeringPosition(size);
        humanTurn = true;

        buttons = new JButton[size * size];
        initializeGUI(size);
    }

    private void initializeGUI(int size) {
        setLayout(new GridLayout(size, size));

        for (int i = 0; i < size * size; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 40));
            final int index = i;
            buttons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    handleButtonClick(index);
                }
            });
            add(buttons[i]);
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleButtonClick(int index) {
        if (humanTurn && currentPosition.board[index] == 0) {
            buttons[index].setText("H");
            currentPosition.board[index] = DomineeringPosition.HUMAN;

            if (checkWin(DomineeringPosition.HUMAN)) {
                JOptionPane.showMessageDialog(this, "Human won!");
                resetGame();
                return;
            }

            humanTurn = false;
            makeProgramMove();
            humanTurn = true;

            if (checkWin(DomineeringPosition.PROGRAM)) {
                JOptionPane.showMessageDialog(this, "Program won!");
                resetGame();
                return;
            }
        }
    }

    private boolean checkWin(int player) {
        return currentPosition.wonPosition(player);
    }

    private void makeProgramMove() {


    }

    private void resetGame() {
        currentPosition = new DomineeringPosition(currentPosition.size);
        humanTurn = true;

        for (JButton button : buttons) {
            button.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DomineeringGUI(5));
    }
}
