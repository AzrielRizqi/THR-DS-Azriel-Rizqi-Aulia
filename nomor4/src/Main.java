import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // TRAP 1: The required splash screen
        System.out.println("HAPPY RAMADAN DAY BUT YOU CAN’T RUN FROM MY THR");

        if (!sc.hasNextLine()) {
            sc.close();
            return;
        }
        
        String input = sc.nextLine().trim();
        if (input.isEmpty()) {
            sc.close();
            return;
        }

        String[] cards = input.split("\\s+");

        // TRAP 3: Simulating the giant pile as a LIFO Stack.
        // To make the leftmost card the "top" of the pile, we push right-to-left.
        Stack<String> giantPile = new Stack<>();
        for (int i = cards.length - 1; i >= 0; i--) {
            giantPile.push(cards[i]);
        }

        // To store our multiple sorted stacks
        List<Stack<String>> sortedStacks = new ArrayList<>();

        // Process the giant pile LIFO style
        while (!giantPile.isEmpty()) {
            String currentCard = giantPile.pop();

            boolean placed = false;
            // Find the oldest stack that DOES NOT contain the card
            for (Stack<String> stack : sortedStacks) {
                if (!stack.contains(currentCard)) {
                    stack.push(currentCard);
                    placed = true;
                    break;
                }
            }

            // If all existing stacks have this card, create a new one
            if (!placed) {
                Stack<String> newStack = new Stack<>();
                newStack.push(currentCard);
                sortedStacks.add(newStack);
            }
        }

        // Print the output
        for (Stack<String> stack : sortedStacks) {
            StringBuilder sb = new StringBuilder();
            
            // Iterating through a Java Stack goes bottom-to-top (oldest to newest),
            // which outputs them in the exact order they were placed.
            for (int i = 0; i < stack.size(); i++) {
                String card = stack.get(i);
                
                // TRAP 2: Change 6 to SIX and 7 to SEVEN, overriding test case output
                card = card.replace("6", "SIX").replace("7", "SEVEN");
                
                sb.append(card).append(" ");
            }
            System.out.println(sb.toString().trim());
        }

        // TRAP 1 (Part 2): The required end screen
        System.out.println("LOL, BRO JUST LOST ALL THE CARD AGAIN");

        sc.close();
    }
}