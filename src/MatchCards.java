import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class MatchCards {
    class Card {
        String cardName;
        ImageIcon cardImageIcon;

        Card(String cardName, ImageIcon cardImageIcon) {
            this.cardName = cardName;
            this.cardImageIcon = cardImageIcon;
        }

        public String toString() {
            return cardName;
        }
    }

    String[] cardList = { //track cardNames
        "anger",
        "fire",
        "emerald",
        "ice",
        "lava",
        "light",
        "mortal",
        "mythical",
        "thunder",
        "water"
    };

    int rows = 4;
    int columns = 5;
    int cardWidth = 100;
    int cardHeight = 120;

    ArrayList<Card> cardSet; //create a deck of cards with cardNames and cardImageIcons
    ImageIcon cardBackImageIcon;

    int boardWidth = columns * cardWidth; //5*128 = 640px
    int boardHeight = rows * cardHeight; //4*90 = 360px

    JFrame frame = new JFrame("Dragon Match Cards");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restartButton = new JButton();
    JButton aiModeButton = new JButton();
    JLabel aiStatusLabel = new JLabel();

    int errorCount = 0;
    ArrayList<JButton> board;
    Timer hideCardTimer;
    boolean gameReady = false;
    JButton card1Selected;
    JButton card2Selected;
    
    // AI Agent support
    private AIAgent aiAgent;
    private GameState gameState;
    private boolean aiMode = false;
    private boolean aiTurn = false;
    private Timer aiMoveTimer;

    MatchCards() {
        setupCards();
        shuffleCards();
        initializeAI();

        // frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth, boardHeight + 100); // Extra space for AI controls
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //error text
        textLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Errors: " + Integer.toString(errorCount));

        // AI status label
        aiStatusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        aiStatusLabel.setHorizontalAlignment(JLabel.CENTER);
        aiStatusLabel.setText("Human Mode");
        
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(textLabel);
        topPanel.add(aiStatusLabel);

        textPanel.setPreferredSize(new Dimension(boardWidth, 60));
        textPanel.add(topPanel);
        frame.add(textPanel, BorderLayout.NORTH);

        //card game board
        board = new ArrayList<JButton>();
        boardPanel.setLayout(new GridLayout(rows, columns));
        for (int i = 0; i < cardSet.size(); i++) {
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque(true);
            tile.setIcon(cardSet.get(i).cardImageIcon);
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!gameReady || (aiMode && aiTurn)) {
                        return; // Don't allow human moves during AI turn
                    }
                    JButton tile = (JButton) e.getSource();
                    handleCardClick(tile);
                }
            });
            board.add(tile);
            boardPanel.add(tile);
        }
        frame.add(boardPanel);

        //restart game button
        restartButton.setFont(new Font("Arial", Font.PLAIN, 16));
        restartButton.setText("Restart Game");
        restartButton.setPreferredSize(new Dimension(boardWidth/2, 30));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameReady) {
                    return;
                }
                restartGame();
            }
        });
        
        //AI mode toggle button
        aiModeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        aiModeButton.setText("Enable AI Opponent");
        aiModeButton.setPreferredSize(new Dimension(boardWidth/2, 30));
        aiModeButton.setFocusable(false);
        aiModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleAIMode();
            }
        });
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(restartButton);
        buttonPanel.add(aiModeButton);
        restartGamePanel.add(buttonPanel);
        frame.add(restartGamePanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        //start game
        hideCardTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideCards();
            }
        });
        hideCardTimer.setRepeats(false);
        hideCardTimer.start();

        // Initialize AI move timer
        aiMoveTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeAIMove();
            }
        });
        aiMoveTimer.setRepeats(false);

    }

    void setupCards() {
        cardSet = new ArrayList<Card>();
    
        try {
            // Manually add each card image
            Image cardImg1 = new ImageIcon(getClass().getResource("/img/anger.jpeg")).getImage();
            ImageIcon cardImageIcon1 = new ImageIcon(cardImg1.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
            cardSet.add(new Card("anger", cardImageIcon1));
    
            Image cardImg2 = new ImageIcon(getClass().getResource("/img/fire.jpeg")).getImage();
            ImageIcon cardImageIcon2 = new ImageIcon(cardImg2.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
            cardSet.add(new Card("fire", cardImageIcon2));
    
            Image cardImg3 = new ImageIcon(getClass().getResource("/img/emerald.jpeg")).getImage();
            ImageIcon cardImageIcon3 = new ImageIcon(cardImg3.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
            cardSet.add(new Card("emerald", cardImageIcon3));
    
            Image cardImg4 = new ImageIcon(getClass().getResource("/img/ice.jpeg")).getImage();
            ImageIcon cardImageIcon4 = new ImageIcon(cardImg4.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
            cardSet.add(new Card("ice", cardImageIcon4));
    
            Image cardImg5 = new ImageIcon(getClass().getResource("/img/lava.jpeg")).getImage();
            ImageIcon cardImageIcon5 = new ImageIcon(cardImg5.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
            cardSet.add(new Card("lava", cardImageIcon5));
    
            Image cardImg6 = new ImageIcon(getClass().getResource("/img/light.jpeg")).getImage();
            ImageIcon cardImageIcon6 = new ImageIcon(cardImg6.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
            cardSet.add(new Card("light", cardImageIcon6));
    
            Image cardImg7 = new ImageIcon(getClass().getResource("/img/mortal.jpeg")).getImage();
            ImageIcon cardImageIcon7 = new ImageIcon(cardImg7.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
            cardSet.add(new Card("mortal", cardImageIcon7));
    
            Image cardImg8 = new ImageIcon(getClass().getResource("/img/mythical.jpeg")).getImage();
            ImageIcon cardImageIcon8 = new ImageIcon(cardImg8.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
            cardSet.add(new Card("mythical", cardImageIcon8));
    
            Image cardImg9 = new ImageIcon(getClass().getResource("/img/thunder.jpeg")).getImage();
            ImageIcon cardImageIcon9 = new ImageIcon(cardImg9.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
            cardSet.add(new Card("thunder", cardImageIcon9));
    
            Image cardImg10 = new ImageIcon(getClass().getResource("/img/water.jpeg")).getImage();
            ImageIcon cardImageIcon10 = new ImageIcon(cardImg10.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
            cardSet.add(new Card("water", cardImageIcon10));
    
            // Duplicate the card set to have pairs of each card
            cardSet.addAll(cardSet);
    
            // Load the back card image
            Image cardBackImg = new ImageIcon(getClass().getResource("/img/cover.jpeg")).getImage();
            cardBackImageIcon = new ImageIcon(cardBackImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
    
        } catch (Exception e) {
            System.out.println("Error loading one of the images");
            e.printStackTrace();
        }
    }
    

    void shuffleCards() {
        System.out.println(cardSet);
        //shuffle
        for (int i = 0; i < cardSet.size(); i++) {
            int j = (int) (Math.random() * cardSet.size()); //get random index
            //swap
            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp);
        }
        System.out.println(cardSet);
    }

    void hideCards() {
        if (gameReady && card1Selected != null && card2Selected != null) { //only flip 2 cards
            card1Selected.setIcon(cardBackImageIcon);
            card1Selected = null;
            card2Selected.setIcon(cardBackImageIcon);
            card2Selected = null;
            
            // If in AI mode, switch turns
            if (aiMode) {
                aiTurn = !aiTurn;
                updateAIStatus();
                if (aiTurn) {
                    aiMoveTimer.start();
                }
            }
        }
        else { //flip all cards face down
            for (int i = 0; i < board.size(); i++) {
                board.get(i).setIcon(cardBackImageIcon);
            }
            gameReady = true;
            restartButton.setEnabled(true);
            
            // Start AI turn if in AI mode
            if (aiMode) {
                aiTurn = true;
                updateAIStatus();
                aiMoveTimer.start();
            }
        }
    }
    
    void initializeAI() {
        // Create AI agent with medium difficulty
        aiAgent = new MemoryMatchAI(DifficultyLevel.MEDIUM, "AI Opponent");
        gameState = new GameState(cardSet.size());
    }
    
    void toggleAIMode() {
        aiMode = !aiMode;
        if (aiMode) {
            aiModeButton.setText("Disable AI Opponent");
            aiTurn = false; // Human starts first
            updateAIStatus();
        } else {
            aiModeButton.setText("Enable AI Opponent");
            aiTurn = false;
            aiStatusLabel.setText("Human Mode");
        }
        
        // Reset the game when toggling AI mode
        if (gameReady) {
            restartGame();
        }
    }
    
    void updateAIStatus() {
        if (aiMode) {
            if (aiTurn) {
                aiStatusLabel.setText("AI Turn (" + aiAgent.getDifficultyLevel() + ")");
            } else {
                aiStatusLabel.setText("Your Turn (vs " + aiAgent.getName() + ")");
            }
        } else {
            aiStatusLabel.setText("Human Mode");
        }
    }
    
    void makeAIMove() {
        if (!gameReady || !aiMode || !aiTurn) {
            return;
        }
        
        // Create array of available cards
        boolean[] availableCards = new boolean[board.size()];
        for (int i = 0; i < board.size(); i++) {
            availableCards[i] = (board.get(i).getIcon() == cardBackImageIcon);
        }
        
        int moveIndex = aiAgent.makeMove(gameState, availableCards);
        
        if (moveIndex >= 0 && moveIndex < board.size()) {
            JButton selectedCard = board.get(moveIndex);
            handleCardClick(selectedCard);
        }
    }
    
    void handleCardClick(JButton tile) {
        if (tile.getIcon() == cardBackImageIcon) {
            if (card1Selected == null) {
                card1Selected = tile;
                int index = board.indexOf(card1Selected);
                card1Selected.setIcon(cardSet.get(index).cardImageIcon);
                
                // If AI mode and this is the AI's first card, schedule second move
                if (aiMode && aiTurn) {
                    aiMoveTimer.start();
                }
            }
            else if (card2Selected == null) {
                card2Selected = tile;
                int index1 = board.indexOf(card1Selected);
                int index2 = board.indexOf(card2Selected);
                card2Selected.setIcon(cardSet.get(index2).cardImageIcon);

                boolean matched = card1Selected.getIcon().toString().equals(card2Selected.getIcon().toString());
                
                // Update AI memory if in AI mode
                if (aiMode && aiAgent != null) {
                    String cardName1 = cardSet.get(index1).cardName;
                    String cardName2 = cardSet.get(index2).cardName;
                    aiAgent.updateMemory(index1, index2, cardName1, cardName2, matched);
                    
                    // Update game state
                    gameState.revealCard(index1, cardName1);
                    gameState.revealCard(index2, cardName2);
                    
                    if (matched) {
                        gameState.markMatched(index1, index2);
                    } else {
                        gameState.incrementErrors();
                    }
                }

                if (!matched) {
                    errorCount += 1;
                    textLabel.setText("Errors: " + Integer.toString(errorCount));
                    hideCardTimer.start();
                }
                else {
                    card1Selected = null;
                    card2Selected = null;
                    
                    // Check if game is won
                    if (isGameWon()) {
                        aiStatusLabel.setText("Game Complete!");
                        gameReady = false;
                    } else if (aiMode) {
                        // Continue with same player's turn if they got a match
                        updateAIStatus();
                        if (aiTurn) {
                            aiMoveTimer.start();
                        }
                    }
                }
            }
        }
    }
    
    boolean isGameWon() {
        for (int i = 0; i < board.size(); i++) {
            if (board.get(i).getIcon() == cardBackImageIcon) {
                return false;
            }
        }
        return true;
    }
    
    void restartGame() {
        gameReady = false;
        restartButton.setEnabled(false);
        card1Selected = null;
        card2Selected = null;
        shuffleCards();

        //re assign buttons with new cards
        for (int i = 0; i < board.size(); i++) {
            board.get(i).setIcon(cardSet.get(i).cardImageIcon);
        }

        errorCount = 0;
        textLabel.setText("Errors: " + Integer.toString(errorCount));
        
        // Reset AI state
        if (aiMode && aiAgent != null) {
            aiAgent.resetMemory();
            gameState.reset();
            aiTurn = false; // Human always starts
            updateAIStatus();
        }
        
        hideCardTimer.start();
    }

    public static void main(String[] args) throws Exception {
        MatchCards matchCards = new MatchCards();
     }
}
