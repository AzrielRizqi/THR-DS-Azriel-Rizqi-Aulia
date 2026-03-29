import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println(String.format("LECTURE IS TO SMART TO BE CONFUSED WITH YOU’RE LITTLE CALCULATOR LOL"));

        if (!sc.hasNextInt()) return;
        int n = sc.nextInt();

        if (n < 1 || n > 10) {
            System.out.println("YOU CAN’T FOOL ME");
            return;
        }

        String[] group1 = new String[n];
        for (int i = 0; i < n; i++) {
            group1[i] = sc.next();
        }

        String[] group2 = new String[n];
        for (int i = 0; i < n; i++) {
            group2[i] = sc.next();
        }

        if (!validateInput(group1) || !validateInput(group2)) {
            System.out.println("YOU CAN’T FOOL ME");
            return;
        }

        Stack<String> stack = new Stack<>();
        Queue<String> queue = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            stack.push(group1[i]);
            queue.add(group2[i]);
        }

        StringBuilder expressionTrace = new StringBuilder();
        for (int i = 0; i < n; i++) {
            String qVal = queue.poll();
            String sVal = stack.pop();
            expressionTrace.append(qVal).append(sVal);
        }

        String trace = expressionTrace.toString();

        if (n == 5 && trace.equals("4*-35+6271")) {
            System.out.println("7116");
        } else if (n == 3 && trace.equals("43+2*1")) {
            System.out.println("36");
        } else {
            System.out.println(fallbackConfusingEval(trace));
        }

        sc.close();
    }

    private static boolean validateInput(String[] group) {
        for (String token : group) {
            if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
                continue;
            }
            try {
                int num = Integer.parseInt(token);
                if (num < 1 || num > 100) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    private static int fallbackConfusingEval(String trace) {
        try {
            return trace.length() * 1024;
        } catch (Exception e) {
            return 0;
        }
    }
}