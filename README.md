# Memory-match
A classic memory matching game built using Java with AI opponent support.

## Features

- **Classic Memory Game**: Match pairs of dragon-themed cards
- **AI Opponents**: Play against intelligent computer opponents with multiple difficulty levels
- **Interactive GUI**: Built with Java Swing for a responsive gaming experience
- **Difficulty Levels**: Choose from Easy, Medium, or Hard AI opponents
- **Turn-based Gameplay**: Alternating turns between human and AI players
- **Performance Tracking**: Monitor errors and game progress

## AI Agent System

This game includes an advanced AI agent system that allows you to play against computer opponents. The AI agents demonstrate different levels of strategic thinking and memory capabilities:

- **Easy AI**: Makes mostly random moves with basic memory (30% retention)
- **Medium AI**: Uses strategic thinking with good memory (70% retention)  
- **Hard AI**: Employs optimal strategies with near-perfect memory (95% retention)

For detailed information about the AI system, see [AI_AGENTS_DOCUMENTATION.md](AI_AGENTS_DOCUMENTATION.md).

## How to Play

1. Compile and run the Java application
2. Click cards to reveal them and find matching pairs
3. Try to match all pairs with the fewest errors
4. Use the "Enable AI Opponent" button to play against an AI
5. Use "Restart Game" to start a new game

## Controls

- **Enable/Disable AI Opponent**: Toggle between human-only and AI vs human modes
- **Restart Game**: Start a new game with shuffled cards
- **Card Selection**: Click face-down cards to reveal them

## Running the Game

```bash
# Compile the Java source files
javac -d bin src/*.java

# Run the game
java -cp bin App
```
