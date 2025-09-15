/**
 * Difficulty levels for AI agents in the memory matching game.
 */
public enum DifficultyLevel {
    EASY("Easy - Random moves with basic memory"),
    MEDIUM("Medium - Strategic moves with good memory"), 
    HARD("Hard - Optimal moves with perfect memory");
    
    private final String description;
    
    DifficultyLevel(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}