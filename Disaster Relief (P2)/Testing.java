// Ishaan Srivastava 
// 11/12/25
// CSE 123 
// P2: Disaster Relief
// TA: Aidan Suen

/*
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class Testing {

    // Tests if all regions are included when the budget is large enough
    @Test
    @DisplayName("STUDENT TEST - Case #1, All regions within budget")
    public void firstTestCase() {
        List<Region> allRegions = Client.createSimpleScenario();
        double budget = 1000000.00;  // Large budget to include all regions

        Allocation allocationAll = Client.allocateRelief(budget, allRegions);
        Allocation expectedAllocation = new Allocation();

        // Expected allocation should have all regions since budget is large
        for (Region region : allRegions) {
            expectedAllocation = expectedAllocation.withRegion(region);
        }
        assertTrue(allocationAll.equals(expectedAllocation));
    }

    // Tests if no regions are included when the budget is too small
    @Test
    @DisplayName("STUDENT TEST - Case #2, No regions within budget")
    public void secondTestCase() {
        List<Region> noneRegions = Client.createSimpleScenario();
        double budget = 1.00;  // Very small budget to exclude all regions

        Allocation allocationNone = Client.allocateRelief(budget, noneRegions);
        Allocation expectedAllocation = new Allocation();

        // Expected allocation should have no regions since budget is small
        assertTrue(allocationNone.equals(expectedAllocation));
    }

    // Tests if some regions are included when the budget allows for at least 2 regions but 
    // not all of them
    @Test
    @DisplayName("STUDENT TEST - Case #3, Some regions within budget")
    public void thirdTestCase() {
        List<Region> someRegions = Client.createSimpleScenario();
        double budget = 2100.00;  // Medium budget to include some regions

        Allocation allocationSome = Client.allocateRelief(budget, someRegions);
        Allocation expectedAllocation = new Allocation();
        expectedAllocation = expectedAllocation.withRegion(new Region("Region #1", 50, 500));
        expectedAllocation = expectedAllocation.withRegion(new Region("Region #2", 100, 700));
        expectedAllocation = expectedAllocation.withRegion(new Region("Region #5", 200, 900));

        // Expected allocation should have those specific regions because they are the most 
        // optimal within the budget
        assertTrue(allocationSome.equals(expectedAllocation));
    }
}
*/