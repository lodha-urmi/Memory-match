/**
 * Interface for AI agents that can play the memory matching game.
 * AI agents can analyze the game state and make strategic decisions
 * about which cards to flip.
 */
public interface AIAgent {
    
    /**
     * Makes a move in the memory matching game.
     * @param gameState Current state of the game board
     * @param availableCards List of face-down cards that can be selected
     * @return Index of the card to select
     */
    int makeMove(GameState gameState, boolean[] availableCards);
    
    /**
     * Updates the agent's memory when cards are revealed.
     * @param cardIndex1 Index of first revealed card
     * @param cardIndex2 Index of second revealed card
     * @param cardName1 Name/type of first card
     * @param cardName2 Name/type of second card
     * @param matched Whether the cards matched
     */
    void updateMemory(int cardIndex1, int cardIndex2, String cardName1, String cardName2, boolean matched);
    
    /**
     * Resets the agent's memory for a new game.
     */
    void resetMemory();
    
    /**
     * Gets the difficulty level of this AI agent.
     * @return Difficulty level (EASY, MEDIUM, HARD)
     */
    DifficultyLevel getDifficultyLevel();
    
    /**
     * Gets the name of this AI agent.
     * @return Agent name
     */
    String getName();
}