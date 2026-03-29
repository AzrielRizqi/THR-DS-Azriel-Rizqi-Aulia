import java.util.*;

// Implementing Comparable allows us to automatically sort the hands
class Card implements Comparable<Card> {
    int value;
    int category;

    public Card(int value, int category) {
        this.value = value;
        this.category = category;
    }

    @Override
    public int compareTo(Card other) {
        // Sort by category first (lowest to highest)
        if (this.category != other.category) {
            return this.category - other.category;
        }
        // Then sort by value (lowest to highest)
        return this.value - other.value;
    }

    @Override
    public String toString() {
        return value + "," + category;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<List<Card>> players = new ArrayList<>();

        // 1. Read the 4 players' hands
        for (int i = 0; i < 4; i++) {
            if (!sc.hasNextLine()) return;
            String line = sc.nextLine().trim();
            String[] cardStrings = line.split("\\s+");
            
            List<Card> hand = new ArrayList<>();
            for (String cs : cardStrings) {
                if (cs.isEmpty()) continue;
                String[] parts = cs.split(",");
                int val = Integer.parseInt(parts[0]);
                int cat = Integer.parseInt(parts[1]);
                hand.add(new Card(val, cat));
            }
            // Sort the hand so the "smallest card of the lowest category" is always at index 0
            Collections.sort(hand);
            players.add(hand);
        }

        // 2. Read the starting player (Convert 1-based input to 0-based index)
        if (!sc.hasNextInt()) return;
        int currentPlayer = sc.nextInt() - 1; 

        Stack<Card> stack = new Stack<>();
        
        boolean roundActive = false;
        int currentCategory = 0;
        int currentTopValue = 0;
        
        int passCount = 0;
        int lastPlayerToPlay = currentPlayer;

        // 3. Main Game Loop
        while (true) {
            List<Card> hand = players.get(currentPlayer);

            if (!roundActive) {
                // First to move in a new round MUST play their absolute smallest card
                Card playCard = hand.remove(0);
                stack.push(playCard);
                
                currentCategory = playCard.category;
                currentTopValue = playCard.value;
                
                lastPlayerToPlay = currentPlayer;
                passCount = 0;
                roundActive = true;
            } else {
                // Middle of a round: Must follow category and play a strictly higher value
                int playIndex = -1;
                for (int i = 0; i < hand.size(); i++) {
                    Card c = hand.get(i);
                    if (c.category == currentCategory && c.value > currentTopValue) {
                        playIndex = i;
                        break; // Play the smallest valid card we can find
                    }
                }

                if (playIndex != -1) {
                    // A valid card was found and played
                    Card playCard = hand.remove(playIndex);
                    stack.push(playCard);
                    
                    currentTopValue = playCard.value;
                    lastPlayerToPlay = currentPlayer;
                    passCount = 0; // Reset passes since a card was played
                } else {
                    // Cannot play, must pass
                    passCount++;
                }
            }

            // Check win condition immediately after a player takes their turn
            if (hand.isEmpty()) {
                // TRAP: The hidden message exactly when someone wins
                System.out.println("HAPPY HOLIDAY FROM GIBEK");
                System.out.println(currentPlayer + 1); // Convert back to 1-based index
                break;
            }

            // Check if the round is over (3 consecutive passes)
            if (passCount == 3) {
                roundActive = false; // The round ends
                currentPlayer = lastPlayerToPlay; // The winner of the trick starts the next round
            } else {
                currentPlayer = (currentPlayer + 1) % 4; // Move to the next player
            }
        }

        // 4. Output the stack in LIFO format (Top card to Bottom card)
        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }

        sc.close();
    }
}