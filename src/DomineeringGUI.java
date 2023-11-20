import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DomineeringGUI extends JFrame{
    private JButton[][] buttons; // Matrice de boutons pour représenter la grille
    private char[][] board; // Matrice pour représenter l'état du jeu (H pour Human, C pour Computer)
    private boolean isHumanTurn = true; // Variable pour suivre le tour du joueur


    public DomineeringGUI(int rows, int cols) {
        super("Domineering Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(rows, cols));

        buttons = new JButton[rows][cols];
        board = new char[rows][cols];

        // Initialisation des boutons et ajout des écouteurs d'événements
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(50, 50));
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                add(buttons[i][j]);

                board[i][j] = ' '; // Initialiser le tableau avec des espaces vides
            }
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Classe interne pour gérer les clics sur les boutons
    private class ButtonClickListener implements ActionListener {
        private int row;
        private int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Vérifier si la case est vide et si c'est le tour du joueur humain
            if (board[row][col] == ' ' && isHumanTurn) {
                // Marquer la case comme occupée par le joueur humain
                board[row][col] = 'H';
                // Mettre à jour l'interface graphique
                buttons[row][col].setText("H");

                // Ajouter votre logique de vérification de victoire ici (par exemple, vérifier les lignes ou colonnes)
                // ...

                // Changer le tour au joueur de l'ordinateur
                isHumanTurn = false;
                computerMove();
            }
        }

        // Logique pour le tour de l'ordinateur
        private void computerMove() {
            // Implémentez votre propre logique pour le mouvement de l'ordinateur ici
            // ...

            // Exemple simple : choisir une case vide aléatoire
            int randomRow, randomCol;
            do {
                randomRow = (int) (Math.random() * board.length);
                randomCol = (int) (Math.random() * board[0].length);
            } while (board[randomRow][randomCol] != ' ');

            // Marquer la case comme occupée par l'ordinateur
            board[randomRow][randomCol] = 'C';
            // Mettre à jour l'interface graphique
            buttons[randomRow][randomCol].setText("C");

            // Ajouter votre logique de vérification de victoire ici (par exemple, vérifier les lignes ou colonnes)
            // ...

            // Changer le tour au joueur humain
            isHumanTurn = true;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DomineeringGUI(5, 5));
    }
}
