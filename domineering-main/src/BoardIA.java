// Importation des bibliothèques nécessaires pour les composants GUI et les opérations de fichiers
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

// Définition de la classe principale étendant JFrame
public class BoardIA extends JFrame {

    // Variables d'instance pour l'état du jeu et les composants GUI
    private DomineeringPosition currentPosition;
    private int boardSize;
    private boolean humanTurn;
    private String Player_AI;
    private JButton[][] buttons;
    private Domineering gameSearch;
    int HintH=3;

    // Constructeur pour initialiser le plateau de jeu
    public BoardIA(String size, String player, Boolean load) {
        super("Jeu de Domineering");

        // Initialisation des paramètres du jeu en fonction de l'entrée de l'utilisateur
        Player_AI = player;
        if (size != null && !size.isEmpty()) {
            boardSize = Integer.parseInt(size.substring(0, Math.min(size.length(), 1)));
        } else {
            System.out.println("Taille invalide. Utilisation de la taille par défaut.");
            boardSize = 5;
        }
        if (load){
            int cmptx = 5;
            if ("IA niveau 1".equals(player)) cmptx = 5;
            if ("IA niveau 2".equals(player)) cmptx =  6;
            if ("IA niveau 3".equals(player)) cmptx =  8;

            currentPosition = new DomineeringPosition( Integer.parseInt(size.substring(0, Math.min(size.length(), 1))) , cmptx );
        }else {
            currentPosition = new DomineeringPosition();

        }
        humanTurn = true;
        gameSearch = new Domineering();

        buttons = new JButton[boardSize][boardSize];

        // Initialisation de l'interface utilisateur graphique
        initializeGUI(boardSize);

        // Chargement du niveau de jeu si spécifié
        if (load) loadGameLevel("game_level.txt");
    }

    // Méthode pour configurer l'interface utilisateur graphique
    private void initializeGUI(int size) {
        // Définir la taille de la fenêtre et la disposition
        setSize(500, 600);

        // Créer un panneau pour la grille de jeu
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(size, size));

        // Créer des boutons pour chaque cellule de la grille
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
                final int row = i, col = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        handleButtonClick(row, col);
                    }
                });
                buttons[i][j].setBackground(Color.white);
                gamePanel.add(buttons[i][j]);
            }
        }

        // Créer un panneau pour les boutons (Indice et Nouvelle partie)
        JPanel buttonPanel = new JPanel();
        JButton hintButton = new JButton("Indice");
        hintButton.addActionListener(e -> showHint());

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
                int option = JOptionPane.showConfirmDialog(BoardIA.this,
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

    // Méthode pour gérer les clics sur les boutons dans la grille de jeu
    private void handleButtonClick(int row, int col) {
        if (humanTurn && isValidMove(row, col)) {
            // Mettre à jour l'état du plateau et vérifier s'il y a une victoire
            buttons[row][col].setBackground(new Color(0x8D0808));
            buttons[row][col + 1].setBackground(new Color(0x8D0808));
            currentPosition.board[row * currentPosition.size + col] = DomineeringPosition.HUMAN;
            currentPosition.board[row * currentPosition.size + col + 1] = DomineeringPosition.HUMAN;

            if (checkWinUI("Human")) {
                JOptionPane.showMessageDialog(this, "Le joueur humain a gagné !");
                dispose();
                new HomePage();
                return;
            }

            // Changer de tour et laisser le programme jouer
            humanTurn = false;
            makeProgramMove();
            humanTurn = true;

            // Vérifier s'il y a une victoire après le tour du programme
            if (checkWinUI("Prog")) {
                JOptionPane.showMessageDialog(this, "Le programme a gagné !");
                dispose();
                new HomePage();
            }
        }
    }

    // Méthode pour vérifier s'il y a une victoire pour un joueur donné
    private boolean checkWin(int player) {
        return currentPosition.wonPosition(player);
    }
    private boolean checkWinUI(String player) {
        Color blankColor = Color.WHITE;

        if (player.equals("Human")) {
            for (int i = 0; i < boardSize - 1 ; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (i == 4) {
                        continue;
                    } else if ((buttons[i][j].getBackground().equals(blankColor) || buttons[i][j].getBackground().equals(Color.YELLOW)) && (buttons[i + 1][j].getBackground().equals(blankColor) || buttons[i + 1][j].getBackground().equals(blankColor))) {
                        return false;
                    }
                }
            }
        }
        if (player.equals("Prog")) {
            for (int i = 0; i < boardSize ; i++) {
                for (int j = 0; j < boardSize  - 1; j++) {
                    if ((buttons[i][j].getBackground().equals(blankColor) || buttons[i][j].getBackground().equals(Color.YELLOW)) && (buttons[i][j + 1].getBackground().equals(blankColor) || buttons[i][j + 1].getBackground().equals(Color.YELLOW))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // Méthode pour effectuer un mouvement pour le programme et mettre à jour l'interface utilisateur
    private void makeProgramMove() {
        currentPosition.makeProgramMove();
        updateGUI();

        // Passer le tour au joueur humain
        humanTurn = true;

        // Vérifier s'il y a une victoire pour le programme
        /*if (checkWin(DomineeringPosition.PROGRAM)) {
            JOptionPane.showMessageDialog(this, "Le programme a gagné !");
            resetGame();
        }

         */
    }

    // Méthode pour mettre à jour l'interface utilisateur
    private void updateGUI() {
        for (int i = 0; i < currentPosition.size; i++) {
            for (int j = 0; j < currentPosition.size; j++) {
                int index = i * currentPosition.size + j;
                if (currentPosition.board[index] == DomineeringPosition.PROGRAM) {
                    buttons[i][j].setBackground(new Color(0x070707));
                }
            }
        }

        // Afficher un message si le programme a gagné
        /*
        if (checkWin(DomineeringPosition.PROGRAM)) {
            JOptionPane.showMessageDialog(this, "Le programme a gagné !");
            resetGame();
        }
        */
    }

    // Méthode pour réinitialiser le jeu
    private void resetGame() {
        currentPosition = new DomineeringPosition();
        humanTurn = true;

        for (int i = 0; i < currentPosition.size; i++) {
            for (int j = 0; j < currentPosition.size; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    // Méthode pour vérifier si un mouvement est valide
    private boolean isValidMove(int row, int col) {
        // Vérifier si le mouvement est dans les limites
        if (row >= 0 && row <= boardSize - 1 && col >= 0 && col <= boardSize - 1) {

            Color backgroundColor = buttons[row][col].getBackground();
            Color blankColor = Color.WHITE;
            if (((backgroundColor.equals(blankColor) || backgroundColor.equals(Color.YELLOW)) &&
                    (buttons[row][col + 1].getBackground().equals(blankColor) || buttons[row][col + 1].getBackground().equals(Color.yellow)))) {
                return true;
            }
        }
        return false;
    }

    // Méthode pour afficher un indice
    private void showHint() {
        Color blankColor = Color.WHITE; // Ajuster ceci à la couleur de fond réelle des boutons vides

        for (int i = 0; i < boardSize - 1; i++) {
            for (int j = 0; j < boardSize - 1; j++) {
                if (HintH > 0) {
                    if (buttons[i][j].getBackground().equals(blankColor) && buttons[i][j + 1].getBackground().equals(blankColor)) {
                        // Suggérer un mouvement horizontal
                        suggestHint(i, j, i, j + 1);
                        HintH--;
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
        // JOptionPane.showMessageDialog(this, "Vous pouvez placer un domino ici !");
    }

    // Méthode pour sauvegarder le niveau de jeu
    private void saveGameLevel() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("game_level.txt"))) {
            writer.write(String.valueOf(boardSize));
            writer.newLine();

            // Sauvegarder le joueur sur la deuxième ligne
            writer.write(Player_AI);
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
            BoardIA domineeringGame = new BoardIA(HomePage.getSelectedSize(), HomePage.getSelectedPlayer(), HomePage.getload());
            domineeringGame.setVisible(true);
        });
    }
}