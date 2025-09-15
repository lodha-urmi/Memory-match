import java.util.*;

/**
 * AI agent implementation for playing the memory matching game.
 * Supports different difficulty levels with varying strategies and memory capabilities.
 */
public class MemoryMatchAI implements AIAgent {
    private DifficultyLevel difficulty;
    private String name;
    private Map<Integer, String> knownCards;  // Cards the AI has seen
    private Random random;
    private double memoryRetentionRate;  // How well the AI remembers cards
    
    public MemoryMatchAI(DifficultyLevel difficulty, String name) {
        this.difficulty = difficulty;
        this.name = name;
        this.knownCards = new HashMap<>();
        this.random = new Random();
        
        // Set memory retention rate based on difficulty
        switch (difficulty) {
            case EASY:
                this.memoryRetentionRate = 0.3;  // Forgets 70% of cards
                break;
            case MEDIUM:
                this.memoryRetentionRate = 0.7;  // Forgets 30% of cards
                break;
            case HARD:
                this.memoryRetentionRate = 0.95;  // Almost perfect memory
                break;
        }
    }
    
    @Override
    public int makeMove(GameState gameState, boolean[] availableCards) {
        List<Integer> validMoves = new ArrayList<>();
        
        // Find all available (face-down, unmatched) cards
        for (int i = 0; i < availableCards.length; i++) {
            if (availableCards[i] && !gameState.isMatched(i)) {
                validMoves.add(i);
            }
        }
        
        if (validMoves.isEmpty()) {
            return -1; // No valid moves
        }
        
        // Strategy based on difficulty level
        switch (difficulty) {
            case EASY:
                return makeEasyMove(validMoves);
            case MEDIUM:
                return makeMediumMove(gameState, validMoves);
            case HARD:
                return makeHardMove(gameState, validMoves);
            default:
                return makeEasyMove(validMoves);
        }
    }
    
    private int makeEasyMove(List<Integer> validMoves) {
        // Easy AI: mostly random moves with occasional memory use
        if (random.nextDouble() < 0.3 && !knownCards.isEmpty()) {
            // 30% chance to use memory
            for (int move : validMoves) {
                if (knownCards.containsKey(move)) {
                    return move;
                }
            }
        }
        // Default: random move
        return validMoves.get(random.nextInt(validMoves.size()));
    }
    
    private int makeMediumMove(GameState gameState, List<Integer> validMoves) {
        // Medium AI: look for matches first, then strategic moves
        
        // First, try to find a match if we know both cards
        for (int move1 : validMoves) {
            if (knownCards.containsKey(move1)) {
                String cardName = knownCards.get(move1);
                for (int move2 : validMoves) {
                    if (move2 != move1 && knownCards.containsKey(move2) && 
                        cardName.equals(knownCards.get(move2))) {
                        return move1; // Found a potential match
                    }
                }
            }
        }
        
        // If no immediate match, prefer cards we haven't seen
        List<Integer> unknownCards = new ArrayList<>();
        for (int move : validMoves) {
            if (!knownCards.containsKey(move)) {
                unknownCards.add(move);
            }
        }
        
        if (!unknownCards.isEmpty()) {
            return unknownCards.get(random.nextInt(unknownCards.size()));
        }
        
        // Fall back to random known card
        return validMoves.get(random.nextInt(validMoves.size()));
    }
    
    private int makeHardMove(GameState gameState, List<Integer> validMoves) {
        // Hard AI: optimal strategy with perfect memory usage
        
        // First priority: make a match if possible
        for (int move1 : validMoves) {
            if (knownCards.containsKey(move1)) {
                String cardName = knownCards.get(move1);
                for (int move2 : validMoves) {
                    if (move2 != move1 && knownCards.containsKey(move2) && 
                        cardName.equals(knownCards.get(move2))) {
                        return move1;
                    }
                }
            }
        }
        
        // Second priority: explore unknown cards to gain information
        List<Integer> unknownCards = new ArrayList<>();
        for (int move : validMoves) {
            if (!knownCards.containsKey(move)) {
                unknownCards.add(move);
            }
        }
        
        if (!unknownCards.isEmpty()) {
            return unknownCards.get(0); // Take first unknown card
        }
        
        // Third priority: known cards that might help find matches later
        return validMoves.get(0);
    }
    
    @Override
    public void updateMemory(int cardIndex1, int cardIndex2, String cardName1, String cardName2, boolean matched) {
        // Update memory based on retention rate
        if (random.nextDouble() < memoryRetentionRate) {
            knownCards.put(cardIndex1, cardName1);
        }
        if (random.nextDouble() < memoryRetentionRate) {
            knownCards.put(cardIndex2, cardName2);
        }
        
        // If cards matched, remove them from memory (they're no longer needed)
        if (matched) {
            knownCards.remove(cardIndex1);
            knownCards.remove(cardIndex2);
        }
    }
    
    @Override
    public void resetMemory() {
        knownCards.clear();
    }
    
    @Override
    public DifficultyLevel getDifficultyLevel() {
        return difficulty;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * Gets information about what the AI remembers (for debugging/display).
     */
    public Map<Integer, String> getKnownCards() {
        return new HashMap<>(knownCards);
    }
}