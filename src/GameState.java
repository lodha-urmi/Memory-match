/**
 * Represents the current state of the memory matching game.
 * Used by AI agents to analyze the game and make decisions.
 */
public class GameState {
    private String[] revealedCards;  // Cards that have been revealed (null if not revealed yet)
    private boolean[] matchedCards;  // Cards that have been matched and removed
    private int totalCards;
    private int errorCount;
    
    public GameState(int totalCards) {
        this.totalCards = totalCards;
        this.revealedCards = new String[totalCards];
        this.matchedCards = new boolean[totalCards];
        this.errorCount = 0;
    }
    
    /**
     * Updates the game state when a card is revealed.
     */
    public void revealCard(int index, String cardName) {
        if (index >= 0 && index < totalCards) {
            revealedCards[index] = cardName;
        }
    }
    
    /**
     * Marks cards as matched.
     */
    public void markMatched(int index1, int index2) {
        if (index1 >= 0 && index1 < totalCards) {
            matchedCards[index1] = true;
        }
        if (index2 >= 0 && index2 < totalCards) {
            matchedCards[index2] = true;
        }
    }
    
    /**
     * Increments the error count.
     */
    public void incrementErrors() {
        errorCount++;
    }
    
    /**
     * Gets the name of a revealed card at the given index.
     */
    public String getRevealedCard(int index) {
        if (index >= 0 && index < totalCards) {
            return revealedCards[index];
        }
        return null;
    }
    
    /**
     * Checks if a card has been matched.
     */
    public boolean isMatched(int index) {
        if (index >= 0 && index < totalCards) {
            return matchedCards[index];
        }
        return false;
    }
    
    /**
     * Checks if a card has been revealed.
     */
    public boolean isRevealed(int index) {
        return getRevealedCard(index) != null;
    }
    
    /**
     * Gets the total number of cards.
     */
    public int getTotalCards() {
        return totalCards;
    }
    
    /**
     * Gets the current error count.
     */
    public int getErrorCount() {
        return errorCount;
    }
    
    /**
     * Resets the game state for a new game.
     */
    public void reset() {
        for (int i = 0; i < totalCards; i++) {
            revealedCards[i] = null;
            matchedCards[i] = false;
        }
        errorCount = 0;
    }
}