import java.util.*;

public class Main {
    static class Borrower {
        String name;
        int key;
        int priority;

        Borrower(String name, int key, int priority) {
            this.name = name;
            this.key = key;
            this.priority = priority;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("I CAN SEE YOU BUT YOU CAN’T SEE ME");

        if (!sc.hasNextInt()) return;
        int n = sc.nextInt();

        if (n < 1 || n > 10) return;

        for (int i = 0; i < n; i++) {
            sc.nextInt();
        }

        String[] names = new String[n];
        int[] keys = new int[n];
        for (int i = 0; i < n; i++) {
            names[i] = sc.next();
            keys[i] = sc.nextInt();
        }

        int[] priorities = new int[n];
        for (int i = 0; i < n; i++) {
            priorities[i] = sc.nextInt();
        }

        Queue<Borrower> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (priorities[i] < 1 || priorities[i] > 5) return;
            queue.add(new Borrower(names[i], keys[i], priorities[i]));
        }

        List<Integer> orderOfKeys = new ArrayList<>();
        List<Borrower> allBorrowers = new ArrayList<>();

        while (!queue.isEmpty()) {
            Borrower b = queue.poll();
            allBorrowers.add(b);
            if (!orderOfKeys.contains(b.key)) {
                orderOfKeys.add(b.key);
            }
        }

        List<Borrower> targetOrder = new ArrayList<>();
        for (int k : orderOfKeys) {
            List<Borrower> group = new ArrayList<>();
            for (Borrower b : allBorrowers) {
                if (b.key == k) {
                    group.add(b);
                }
            }
            group.sort(Comparator.comparingInt(b -> b.priority));
            targetOrder.addAll(group);
        }

        Stack<String> stack = new Stack<>();
        
        for (int i = targetOrder.size() - 1; i >= 0; i--) {
            Borrower b = targetOrder.get(i);
            stack.push(b.name + " | " + b.key);
        }

        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }

        sc.close();
    }
}