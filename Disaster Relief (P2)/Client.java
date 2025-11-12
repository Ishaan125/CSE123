// Ishaan Srivastava 
// 11/12/25
// CSE 123 
// P2: Disaster Relief
// TA: Aidan Suen

import java.util.*;

// A client class for creating scenarios (list of regions) and allocating relief to said regions.
public class Client {
    private static final Random RAND = new Random();

    public static void main(String[] args) throws Exception {
        // List<Region> scenario = createRandomScenario(10, 10, 100, 1000, 100000);
        List<Region> scenario = createSimpleScenario();
        System.out.println(scenario);
        
        double budget = 2000;
        Allocation allocation = allocateRelief(budget, scenario);
        printResult(allocation, budget);
    }

    // Behavior: Returns an allocation of regions so that the total cost is under the budget and
    //           the total population helped is maximized. If multiple allocations result in the 
    //           same # population helped, the one with the lowest cost is picked. If there is a
    //           tie in both population helped and cost, any of the optimal allocations
    //           will be chosen.
    // Exceptions: Throws an IllegalArgumentException if sites is null.
    // Parameters: budget - the total budget available for allocation
    //             sites - the list of regions to consider for allocation
    // Returns: an Allocation representing the optimal allocation of regions
    public static Allocation allocateRelief(double budget, List<Region> sites) throws IllegalArgumentException {
        if (sites == null) {
            throw new IllegalArgumentException("Sites list cannot be null.");
        }

        // Starts at index 0, uses budget as remaining budget.
        return helpAllocate(budget, sites, 0, new Allocation(), new Allocation());
    }

    // Behavior: Helper method for allocateRelief to help find the optimal allocation.
    // Parameters: budgetRemaining - the remaining budget available for allocation
    //             sites - the list of regions to consider for allocation
    //             startIndex - the current starting index in the sites list to consider
    //             current - the current allocation being built
    //             best - the best allocation found so far
    // Returns: the best Allocation found.
    private static Allocation helpAllocate(double budgetRemaining, List<Region> sites,
                                           int startIndex, Allocation current, Allocation best) {
        // Update best if the current selection is better
        if (current.totalPeople() > best.totalPeople() || (current.totalCost() < best.totalCost())
            && current.totalPeople() == best.totalPeople()) {
            best = current;
        }

        // startIndex so that it doesn't keep re-adding previous sites
        for (int i = startIndex; i < sites.size(); i++) {
            Region site = sites.get(i);
            double cost = site.getCost();

            // Only consider this site if we can afford it
            if (cost <= budgetRemaining) {
                current = current.withRegion(site);
                best = helpAllocate(budgetRemaining - cost, sites, i + 1, current, best);
                current = current.withoutRegion(site);  // backtrack
            }
        }

        return best;
    }

    ///////////////////////////////////////////////////////////////////////////
    // PROVIDED HELPER METHODS - **DO NOT MODIFY ANYTHING BELOW THIS LINE!** //
    ///////////////////////////////////////////////////////////////////////////
    
    /**
    * Prints each allocation in the provided set. Useful for getting a quick overview
    * of all allocations currently in the system.
    * @param allocations Set of allocations to print
    */
    public static void printAllocations(Set<Allocation> allocations) {
        System.out.println("All Allocations:");
        for (Allocation a : allocations) {
            System.out.println("  " + a);
        }
    }

    /**
    * Prints details about a specific allocation result, including the total people
    * helped, total cost, and any leftover budget. Handy for checking if we're
    * within budget limits!
    * @param alloc The allocation to print
    * @param budget The budget to compare against
    */
    public static void printResult(Allocation alloc, double budget) {
        System.out.println("Result: ");
        System.out.println("  " + alloc);
        System.out.println("  People helped: " + alloc.totalPeople());
        System.out.printf("  Cost: $%.2f\n", alloc.totalCost());
        System.out.printf("  Unused budget: $%.2f\n", (budget - alloc.totalCost()));
    }

    /**
    * Creates a scenario with numRegions regions by randomly choosing the population 
    * and cost of each region.
    * @param numRegions Number of regions to create
    * @param minPop Minimum population per region
    * @param maxPop Maximum population per region
    * @param minCostPer Minimum cost per person
    * @param maxCostPer Maximum cost per person
    * @return A list of randomly generated regions
    */
    public static List<Region> createRandomScenario(int numRegions, int minPop, int maxPop,
                                                    double minCostPer, double maxCostPer) {
        List<Region> result = new ArrayList<>();

        for (int i = 0; i < numRegions; i++) {
            int pop = RAND.nextInt(maxPop - minPop + 1) + minPop;
            double cost = (RAND.nextDouble(maxCostPer - minCostPer) + minCostPer) * pop;
            result.add(new Region("Region #" + i, pop, round2(cost)));
        }

        return result;
    }

    /**
    * Manually creates a simple list of regions to represent a known scenario.
    * @return A simple list of regions
    */
    public static List<Region> createSimpleScenario() {
        List<Region> result = new ArrayList<>();

        result.add(new Region("Region #1", 50, 500));
        result.add(new Region("Region #2", 100, 700));
        result.add(new Region("Region #3", 60, 1000));
        result.add(new Region("Region #4", 20, 1000));
        result.add(new Region("Region #5", 200, 900));

        return result;
    }    

    /**
    * Rounds a number to two decimal places.
    * @param num The number to round
    * @return The number rounded to two decimal places
    */
    private static double round2(double num) {
        return Math.round(num * 100) / 100.0;
    }
}
