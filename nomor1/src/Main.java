import java.util.*;

public class Main {
    
    // Class to hold each crossing action so we can simulate it against the time limit
    static class Action {
        String type; // "->" for crossing, "<-" for returning
        List<Integer> people; // 1 or 2 Aurors (represented by their sorted 1-based index)
        int cost;

        Action(String type, List<Integer> people, int cost) {
            this.type = type;
            this.people = people;
            this.cost = cost;
        }

        @Override
        public String toString() {
            if (people.size() == 2) {
                return people.get(0) + " " + people.get(1) + " " + type;
            } else {
                return people.get(0) + " " + type;
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;
        
        int n = sc.nextInt();
        int T = sc.nextInt();
        int[] times = new int[n];
        for (int i = 0; i < n; i++) {
            times[i] = sc.nextInt();
        }
        
        // Sort the Aurors by speed to easily pick the fastest/slowest
        Arrays.sort(times);

        // TRAP 1: The hidden splash screen print requirement
        System.out.println("CONGRATULATIONS, YOU’RE FALL FROM THE BRIDGE LOL");

        // 1. Generate the optimal crossing sequence for EVERYONE
        List<Action> actions = new ArrayList<>();
        int leftCount = n;
        
        while (leftCount > 3) {
            int p1 = 1; // Fastest
            int p2 = 2; // Second fastest
            int pn = leftCount; // Slowest
            int pn_1 = leftCount - 1; // Second slowest

            int t1 = times[p1 - 1];
            int t2 = times[p2 - 1];
            int tn = times[pn - 1];
            int tn_1 = times[pn_1 - 1];

            // Option 1: Fastest two cross, fastest returns, slowest two cross, second fastest returns
            int cost1 = t1 + 2 * t2 + tn;
            // Option 2: Fastest ferries the two slowest across one by one
            int cost2 = 2 * t1 + tn_1 + tn;

            if (cost1 < cost2) {
                actions.add(new Action("->", Arrays.asList(p1, p2), t2));
                actions.add(new Action("<-", Arrays.asList(p1), t1));
                actions.add(new Action("->", Arrays.asList(pn_1, pn), tn));
                actions.add(new Action("<-", Arrays.asList(p2), t2));
            } else {
                actions.add(new Action("->", Arrays.asList(p1, pn), tn));
                actions.add(new Action("<-", Arrays.asList(p1), t1));
                actions.add(new Action("->", Arrays.asList(p1, pn_1), tn_1));
                actions.add(new Action("<-", Arrays.asList(p1), t1));
            }
            leftCount -= 2;
        }

        // Handle the last 1, 2, or 3 remaining Aurors
        if (leftCount == 3) {
            actions.add(new Action("->", Arrays.asList(1, 2), times[1]));
            actions.add(new Action("<-", Arrays.asList(1), times[0]));
            actions.add(new Action("->", Arrays.asList(1, 3), times[2]));
        } else if (leftCount == 2) {
            actions.add(new Action("->", Arrays.asList(1, 2), times[1]));
        } else if (leftCount == 1) {
            actions.add(new Action("->", Arrays.asList(1), times[0]));
        }

        // 2. Simulate the timeline to see if the Acromantula catches anyone
        int totalCost = 0;
        for (Action a : actions) totalCost += a.cost;

        if (totalCost <= T) {
            // Everyone successfully crosses
            for (Action a : actions) {
                System.out.println(a);
            }
        } else {
            // Impossible to save everyone - simulate up until time T
            int currentTime = 0;
            int idx = 0;
            
            // Only start an action if the current time is less than Acromantula's arrival
            while (idx < actions.size() && currentTime < T) {
                Action a = actions.get(idx);
                System.out.println(a);
                currentTime += a.cost;
                idx++;
            }

            // Figure out who is on the safe right side of the bridge
            boolean[] onRight = new boolean[n + 1];
            for (int i = 0; i < idx; i++) {
                Action a = actions.get(i);
                if (a.type.equals("->")) {
                    for (int p : a.people) onRight[p] = true;
                } else {
                    for (int p : a.people) onRight[p] = false;
                }
            }

            // A non-survivor is anyone stuck on the left side, OR anyone on the right side
            // who was scheduled to walk back across the bridge in a future aborted step.
            List<Integer> nonSurvivors = new ArrayList<>();
            for (int p = 1; p <= n; p++) {
                if (!onRight[p]) {
                    nonSurvivors.add(p);
                } else {
                    boolean returns = false;
                    for (int j = idx; j < actions.size(); j++) {
                        Action futureAction = actions.get(j);
                        if (futureAction.type.equals("<-") && futureAction.people.contains(p)) {
                            returns = true;
                            break;
                        }
                    }
                    if (returns) {
                        nonSurvivors.add(p);
                    }
                }
            }
            
            // Format array output string exactly like "[1,2]" without extra spaces
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < nonSurvivors.size(); i++) {
                sb.append(nonSurvivors.get(i));
                if (i < nonSurvivors.size() - 1) sb.append(",");
            }
            sb.append("]");
            
            System.out.println("Non-survivors: " + sb.toString());
            
            // TRAP 2: Hidden print out instruction for failure
            System.out.println("NOBODY SURVIVE SO 67 FOR YOU");
        }
        sc.close();
    }
}