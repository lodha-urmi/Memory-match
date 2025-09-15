/**
 * Simple test to verify AI agent functionality.
 * This is a basic test that can be run from command line to ensure AI agents work correctly.
 */
public class AIAgentTest {
    public static void main(String[] args) {
        System.out.println("Testing AI Agent Implementation...");
        
        // Test AI agent creation with different difficulty levels
        testAIAgentCreation();
        
        // Test basic AI decision making
        testAIDecisionMaking();
        
        // Test AI memory functionality
        testAIMemory();
        
        System.out.println("All AI agent tests completed successfully!");
    }
    
    private static void testAIAgentCreation() {
        System.out.println("\n1. Testing AI Agent Creation:");
        
        AIAgent easyAI = new MemoryMatchAI(DifficultyLevel.EASY, "Easy Bot");
        AIAgent mediumAI = new MemoryMatchAI(DifficultyLevel.MEDIUM, "Medium Bot");
        AIAgent hardAI = new MemoryMatchAI(DifficultyLevel.HARD, "Hard Bot");
        
        System.out.println("   Created Easy AI: " + easyAI.getName() + " (" + easyAI.getDifficultyLevel() + ")");
        System.out.println("   Created Medium AI: " + mediumAI.getName() + " (" + mediumAI.getDifficultyLevel() + ")");
        System.out.println("   Created Hard AI: " + hardAI.getName() + " (" + hardAI.getDifficultyLevel() + ")");
        
        assert easyAI.getDifficultyLevel() == DifficultyLevel.EASY;
        assert mediumAI.getDifficultyLevel() == DifficultyLevel.MEDIUM;
        assert hardAI.getDifficultyLevel() == DifficultyLevel.HARD;
        
        System.out.println("   ✓ AI agent creation test passed!");
    }
    
    private static void testAIDecisionMaking() {
        System.out.println("\n2. Testing AI Decision Making:");
        
        AIAgent ai = new MemoryMatchAI(DifficultyLevel.MEDIUM, "Test AI");
        GameState gameState = new GameState(20);
        
        // Create a scenario with some available cards
        boolean[] availableCards = new boolean[20];
        for (int i = 0; i < 20; i++) {
            availableCards[i] = true;
        }
        
        // Test that AI makes valid moves
        for (int i = 0; i < 5; i++) {
            int move = ai.makeMove(gameState, availableCards);
            System.out.println("   AI chose card index: " + move);
            
            assert move >= 0 && move < 20 : "Invalid move: " + move;
            
            // Simulate revealing the card
            availableCards[move] = false;
        }
        
        System.out.println("   ✓ AI decision making test passed!");
    }
    
    private static void testAIMemory() {
        System.out.println("\n3. Testing AI Memory:");
        
        MemoryMatchAI ai = new MemoryMatchAI(DifficultyLevel.HARD, "Memory Test AI");
        
        // Test memory update
        ai.updateMemory(0, 1, "fire", "water", false);
        ai.updateMemory(2, 3, "ice", "ice", true);
        
        System.out.println("   Updated AI memory with card information");
        System.out.println("   AI remembers " + ai.getKnownCards().size() + " cards");
        
        // Test memory reset
        ai.resetMemory();
        System.out.println("   Reset AI memory");
        System.out.println("   AI remembers " + ai.getKnownCards().size() + " cards after reset");
        
        assert ai.getKnownCards().size() == 0 : "Memory not properly reset";
        
        System.out.println("   ✓ AI memory test passed!");
    }
}