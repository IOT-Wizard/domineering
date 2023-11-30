import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class BoardH extends JFrame {
    private JButton[][] buttons;
    private String currentPlayer;
    private int boardSize;

    int HintH = 3;
    int HintV = 3;

    // Constructeur pour initialiser la classe BoardH
    public BoardH(String selectedSize, String player, Boolean load) {
        boardSize = Integer.parseInt(selectedSize.substring(0, 1));
        buttons = new JButton[boardSize][boardSize];
        currentPlayer = "H"; // 'H' pour horizontal, 'V' pour vertical
        initializeUI();

        // Affichage des informations du joueur et de l'état de chargement
        System.out.println(player);
        System.out.println(load);
        if (load) loadGameLevel("game_level.txt"); // Charger le niveau de jeu au démarrage
    }

    // Méthode pour configurer l'interface utilisateur graphique
    private void initializeUI() {
        setTitle("Jeu de Domineering");
        setSize(500, 600); // Hauteur augmentée pour accommoder les boutons

        // Créer un panneau pour contenir la grille de jeu
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(boardSize, boardSize));

        // Créer des boutons pour chaque cellule de la grille
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
                buttons[i][j].addActionListener(new ButtonClickListener());
                buttons[i][j].setBackground(Color.white);
                gamePanel.add(buttons[i][j]);
            }
        }

        // Créer un panneau pour les boutons (Indice)
        JPanel buttonPanel = new JPanel();
        JButton hintButton = new JButton("Indice");
        hintButton.addActionListener(e -> showHint()); // Méthode personnalisée pour afficher des indices

        JButton newGameButton = new JButton("Nouvelle Partie");
        newGameButton.addActionListener(e -> startNewGame());

        buttonPanel.add(hintButton);
        buttonPanel.add(newGameButton);

        // Créer un panneau principal pour contenir le panneau de jeu et le panneau de boutons verticalement
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(gamePanel);
        mainPanel.add(buttonPanel);

        // Ajouter le panneau principal au centre de la fenêtre
        add(mainPanel, BorderLayout.CENTER);

        // Ajouter un écouteur de fenêtre pour sauvegarder le niveau de jeu lors de la fermeture de la fenêtre
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(BoardH.this,
                        "Voulez-vous sauvegarder la partie en cours avant de quitter?",
                        "Confirmation",
                        JOptionPane.YES_NO_CANCEL_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    saveGameLevel();
                    System.exit(0);
                }
            }
        });
    }

    // Classe interne pour gérer les clics sur les boutons
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();

            int row = -1, col = -1;

            // Trouver la position du bouton cliqué dans la grille
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (buttons[i][j] == clickedButton) {
                        row = i;
                        col = j;
                        break;
                    }
                }
            }

            if (isValidMove(row, col)) {
                // Mettre à jour l'interface utilisateur en fonction du mouvement valide
                if (currentPlayer.equals("H")) {
                    buttons[row][col].setBackground(new Color(0x8D0808));
                    buttons[row + 1][col].setBackground(new Color(0x8D0808));
                } else {
                    buttons[row][col].setBackground(new Color(0x070707));
                    buttons[row][col + 1].setBackground(new Color(0x070707));
                }

                // Vérifier s'il y a une victoire
                boolean win = wonPosition(currentPlayer);

                if (win) {
                    if (currentPlayer.equals("H")) JOptionPane.showMessageDialog(getParent(), "Le joueur 1 a gagné !");
                    if (currentPlayer.equals("Human")) JOptionPane.showMessageDialog(getParent(), "Le joueur 2 a gagné !");
                    dispose();
                    new HomePage();
                }

                // Changer de joueur
                currentPlayer = (currentPlayer.equals("H")) ? "Human" : "H";
            }
        }
    }

    // Méthode pour vérifier si un mouvement est valide
    private boolean isValidMove(int row, int col) {
        // Vérifier si le mouvement est dans les limites
        if (row >= 0 && row <= boardSize - 1 && col >= 0 && col <= boardSize - 1) {
            Color backgroundColor = buttons[row][col].getBackground();
            Color blankColor = Color.WHITE; // Ajuster ceci à la couleur de fond réelle des boutons vides

            if (currentPlayer.equals("H") && (backgroundColor.equals(blankColor) || backgroundColor.equals(Color.YELLOW)) &&
                    buttons[row + 1][col].getBackground().equals(blankColor)) {
                return true;
            } else if (currentPlayer.equals("Human") && (backgroundColor.equals(blankColor) || backgroundColor.equals(Color.YELLOW)) &&
                    buttons[row][col + 1].getBackground().equals(blankColor)) {
                return true;
            }
        }
        return false;
    }

    // Méthode pour vérifier s'il y a une victoire pour un joueur donné
    public boolean wonPosition(String player) {
        Color blankColor = Color.WHITE; // Ajuster ceci à la couleur de fond réelle des boutons vides

        if (player.equals("Human")) {
            for (int i = 0; i < boardSize - 1; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (i == 4) {
                        continue;
                    } else if ((buttons[i][j].getBackground().equals(blankColor) || buttons[i][j].getBackground().equals(Color.YELLOW)) && (buttons[i + 1][j].getBackground().equals(blankColor) || buttons[i + 1][j].getBackground().equals(blankColor))) {
                        return false;
                    }
                }
            }
        } else {
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize - 1; j++) {
                    if ((buttons[i][j].getBackground().equals(blankColor) || buttons[i][j].getBackground().equals(Color.YELLOW)) && (buttons[i][j + 1].getBackground().equals(blankColor) || buttons[i][j + 1].getBackground().equals(Color.YELLOW))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // Méthode pour afficher un indice
    private void showHint() {
        Color blankColor = Color.WHITE; // Ajuster ceci à la couleur de fond réelle des boutons vides

        for (int i = 0; i < boardSize - 1; i++) {
            for (int j = 0; j < boardSize - 1; j++) {
                if (currentPlayer.equals("H") && HintH > 0) {
                    if (buttons[i][j].getBackground().equals(blankColor) && buttons[i + 1][j].getBackground().equals(blankColor)) {
                        // Suggérer un mouvement horizontal
                        suggestHint(i, j, i + 1, j);
                        HintH--;
                        return;
                    }
                }
                if (currentPlayer.equals("Human") && HintV > 0) {
                    if (buttons[i][j].getBackground().equals(blankColor) && buttons[i][j + 1].getBackground().equals(blankColor)) {
                        // Suggérer un mouvement vertical
                        suggestHint(i, j, i, j + 1);
                        HintV--;
                        return;
                    }
                }
            }
        }

        // Si aucun mouvement valide n'est trouvé, fournir un indice générique
        JOptionPane.showMessageDialog(this, "Aucun mouvement valide disponible.");
    }

    // Méthode pour suggérer un indice visuel
    private void suggestHint(int row1, int col1, int row2, int col2) {
        buttons[row1][col1].setBackground(Color.YELLOW);
        buttons[row2][col2].setBackground(Color.YELLOW);

        // Afficher un message d'indice
        JOptionPane.showMessageDialog(this, "Vous pouvez placer un domino ici !");
    }

    // Méthode pour sauvegarder le niveau de jeu
    private void saveGameLevel() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("game_level.txt"))) {
            writer.write(String.valueOf(boardSize));
            writer.newLine();

            // Sauvegarder le joueur sur la deuxième ligne
            writer.write(currentPlayer);
            writer.newLine();
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    String cellState;
                    Color backgroundColor = buttons[i][j].getBackground();
                    if (backgroundColor.equals(new Color(0x8D0808)) || backgroundColor.equals(new Color(0x070707))) {
                        // Sauvegarder les couleurs pour les cellules non vides (noir ou rouge)
                        cellState = backgroundColor.equals(new Color(0x8D0808)) ? "H" : "V";
                    } else {
                        // Sauvegarder "0" pour les cellules vides
                        cellState = "0";
                    }

                    writer.write(cellState);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode principale pour charger le niveau de jeu à partir d'un fichier
    public boolean loadGameLevel(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            int currentLineNumber = 0;
            while ((line = reader.readLine()) != null && currentLineNumber < 1) {
                currentLineNumber++;

                // Si la ligne actuelle est la ligne cible, imprimer
                if (currentLineNumber == 2) {
                    System.out.println("Ligne " + 2 + ": " + line);
                }
            }

            for (int i = 0; i < boardSize; i++) {
                line = reader.readLine();
                System.out.println("Ligne " + 2 + ": " + line);

                if (line != null) {
                    for (int j = 0; j < Math.min(line.length(), boardSize); j++) {
                        String cellState = String.valueOf(line.charAt(j));

                        // Mettre à jour le bouton en fonction de l'état de la cellule sauvegardée
                        if (cellState.equals("H")) {
                            buttons[i][j].setBackground(new Color(0x8D0808));
                            // buttons[i + 1][j].setBackground(new Color(0x8D0808));
                        } else if (cellState.equals("V")) {
                            buttons[i][j].setBackground(new Color(0x070707));
                            // buttons[i][j + 1].setBackground(new Color(0x070707));
                        } else {
                            // "0" représente les cellules vides
                            buttons[i][j].setBackground(Color.WHITE);
                            // buttons[i][j + 1].setBackground(Color.WHITE);
                            // buttons[i + 1][j].setBackground(Color.WHITE);
                            // buttons[i + 1][j + 1].setBackground(Color.WHITE);
                        }

                        // Activer ou désactiver les boutons en fonction de l'état de la cellule
                        if (cellState.equals("0")) {
                            buttons[i][j].setEnabled(true); // Activer les cellules vides
                        } else {
                            buttons[i][j].setEnabled(false); // Désactiver les cellules non vides
                        }
                    }
                }
            }
            return true; // Chargement réussi
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Échec du chargement
        }
    }

    // Méthode pour commencer une nouvelle partie
    private void startNewGame() {
        int option = JOptionPane.showConfirmDialog(this,
                "Commencer une nouvelle partie ?",
                "Nouvelle Partie",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            dispose();
            new HomePage();
        }
    }

    // Méthode principale pour démarrer l'application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BoardH domineeringGame = new BoardH(HomePage.getSelectedSize(), HomePage.getSelectedPlayer(), HomePage.getload());
            domineeringGame.setVisible(true);
        });
    }
}