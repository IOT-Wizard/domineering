import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HomePage {
    private static boolean load = false;

    private JFrame frame;
    private static String selectedSize;
    private static String player;
    private JComboBox<String> choiceComboBox;
    private JComboBox<String> sizeComboBox;

    // Constructeur de la classe HomePage
    public HomePage() {
        // Initialisation du cadre (frame)
        frame = new JFrame("Home Page");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Création et configuration du gestionnaire de mise en page
        JPanel mainPanel = new JPanel(new GridLayout(4, 1));
        JPanel zeroRowPanel = new JPanel(new FlowLayout());
        JPanel firstRowPanel = new JPanel(new FlowLayout());
        JPanel secondRowPanel = new JPanel(new FlowLayout());
        JPanel thirdRowPanel = new JPanel(new FlowLayout());

        // Ajout d'un bouton pour "Démarrer une nouvelle partie"
        JButton newGameButton = new JButton("Démarrer une nouvelle partie");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirige vers BoardPage lorsque le bouton est cliqué
                player = (String) choiceComboBox.getSelectedItem();
                selectedSize = (String) sizeComboBox.getSelectedItem();

                // Instructions de débogage
                System.out.println("Joueur sélectionné : " + player);
                System.out.println("Taille sélectionnée : " + selectedSize);

                if (selectedSize != null) {
                    frame.dispose();
                    if ("Humain".equals(player)) {  // Utilisez equals() pour la comparaison de chaînes
                        new BoardH(selectedSize, player, load).setVisible(true);
                    } else {
                        new BoardIA(selectedSize, player, load).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Veuillez sélectionner une taille de plateau.");
                }
            }
        });

        JLabel wLabel = new JLabel("Salutations, Explorateur de Jeux!");
        JButton humanButton = new JButton("Humain");
        humanButton.setPreferredSize(new Dimension(80, 30));
        // Ajout de l'étiquette "VS"
        JLabel vsLabel = new JLabel("VS");

        // Ajout de la liste déroulante pour le choix entre Humain ou IA
        String[] choices = {"Humain", "IA niveau 1", "IA niveau 2", "IA niveau 3"};
        choiceComboBox = new JComboBox<>(choices);
        choiceComboBox.setPreferredSize(new Dimension(90, 30));

        // Création d'un panneau pour la taille du plateau
        JPanel sizePanel = new JPanel();
        JLabel sizeLabel = new JLabel("Taille du Plateau : ");
        String[] sizeOptions = {"3x3", "4x4", "5x5", "8x8"};
        sizeComboBox = new JComboBox<>(sizeOptions);
        sizePanel.add(sizeLabel);
        sizePanel.add(sizeComboBox);

        sizeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSize = (String) sizeComboBox.getSelectedItem();
                if (selectedSize != null) {
                    // Transférer la taille sélectionnée à BoardPage
                    System.out.println(selectedSize);
                }
            }
        });

        choiceComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String player = (String) choiceComboBox.getSelectedItem();
                if (player != null) {
                    // Transférer le joueur sélectionné à BoardPage
                    System.out.println(player);
                }
            }
        });

        // Configuration des couleurs des composants graphiques
        newGameButton.setBackground(new Color(0xDDE0EE));
        humanButton.setBackground(new Color(0xA3AEDA));
        choiceComboBox.setBackground(new Color(0xA3AEDA));

        // Ajout des composants au panneau principal
        zeroRowPanel.add(wLabel);
        firstRowPanel.add(humanButton);
        firstRowPanel.add(vsLabel);
        firstRowPanel.add(choiceComboBox);
        thirdRowPanel.add(sizePanel);
        mainPanel.add(zeroRowPanel);
        mainPanel.add(firstRowPanel);
        mainPanel.add(thirdRowPanel);

        // Ajout du bouton newGameButton à la deuxième ligne du panneau principal
        secondRowPanel.add(newGameButton);
        // Ajout du secondRowPanel au panneau principal
        mainPanel.add(secondRowPanel);

        // Ajout du panneau principal au cadre
        frame.add(mainPanel);

        // Configuration des propriétés du cadre
        frame.setVisible(true);
    }

    // Méthode statique pour obtenir la complexité sélectionnée
    public static int getSelectedcomplex() {
        if ("IA niveau 1".equals(player)) return 5;
        if ("IA niveau 2".equals(player)) return 6;
        if ("IA niveau 3".equals(player)) return 8;
        return 5;
    }

    // Méthode statique pour obtenir la taille sélectionnée
    public static String getSelectedSize() {
        return selectedSize;
    }

    // Méthode statique pour obtenir le joueur sélectionné
    public static String getSelectedPlayer() {
        return player;
    }

    // Méthode statique pour obtenir l'état de chargement
    public static boolean getload() {
        return load;
    }

    // Méthode privée pour démarrer une partie sauvegardée
    private void startSavedGame() {
        int option = JOptionPane.showConfirmDialog(
                frame,
                "Voulez-vous continuer la partie sauvegardée?",
                "Continuer la Partie Sauvegardée",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            load = true;

            try (BufferedReader reader = new BufferedReader(new FileReader("game_level.txt"))) {
                // Lire la taille du plateau depuis la première ligne
                String sizeLine = reader.readLine();
                if (sizeLine != null) {
                    int boardSize = Integer.parseInt(sizeLine.trim());

                    // Lire les informations du joueur depuis la deuxième ligne
                    String playerLine = reader.readLine();
                    if (playerLine != null) {
                        // Utiliser la référence à la variable globale
                        player = playerLine.trim();

                        // Affichage pour le débogage
                        System.out.println("Joueur : " + player + ", Taille du Plateau : " + boardSize);
                        frame.dispose();
                        // Créer l'instance de BoardH
                        if ("Humain".equals(player)) {
                            BoardH board = new BoardH(String.valueOf(boardSize), player, load);
                            board.setVisible(true);
                        } else {
                            BoardIA board = new BoardIA(String.valueOf(boardSize), player, load);
                            board.setVisible(true);
                        }
                        load = false;
                        // Uncomment the following block if the 'loadGame' method is defined
                    /*
                    if (loadGame("game_level.txt", boardSize, player)) {
                        JOptionPane.showMessageDialog(frame, "Game loaded successfully!");
                    }
                    */
                    } else {
                        // Informations du joueur non trouvées
                        JOptionPane.showMessageDialog(frame, "Informations du joueur non trouvées. Démarrage d'une nouvelle partie.");
                    }
                } else {
                    // Taille du plateau non trouvée
                    JOptionPane.showMessageDialog(frame, "Taille du plateau non trouvée. Démarrage d'une nouvelle partie.");
                }
            } catch (IOException | NumberFormatException e) {
                // Gérer les exceptions spécifiques de manière appropriée
                e.printStackTrace();
            }
        }
    }

    // Méthode main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HomePage homepage = new HomePage();
            homepage.startSavedGame(); // Appelle la méthode pour vérifier les parties sauvegardées au début
        });
    }
}
