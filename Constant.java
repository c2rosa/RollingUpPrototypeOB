/**
 * Created by rosa.charles on 9/13/16.
 */
public class Constant {

    public static int NUMBER_OF_ODS_CONSIDERED_TO_BE_TOO_MANY_CROSSING_A_LOADLEG_TO_CONSIDER_CUTTING_THAT_LOADLEG = 25;

    public static final class CubeExpansionScenarios {
        // We have to set this up so that base0_0 = "0.0" has a value that makes it ordered first alphanumerically - making it the base scenario
        public static final String base0_0 = "0.0_cubeExpansion";
        public static final String base0_025 = "0.025_cubeExpansion";
        public static final String base0_05 = "0.05_cubeExpansion";
        public static final String base0_1 = "0.1_cubeExpansion";
    }

    public static final class DayLineHaulLimitationsScenarios {
        // We have to set this up so that unlimited = "0_unlimited" has a value that makes it ordered first alphanumerically - making it the base scenario
        public static final String unlimited = "0_unlimited_DayLHDrivers";
        public static final String limited = "1_limited_DayLHDrivers";
    }

    public static final class TypeOfScenarios {
        public static final String cubeExpansion = "cubeExpansion";
        public static final String daylinehaul_limitations = "daylinehaul_limitations";
    }

    public static int EXTREMELY_HIGH_CYCLE_COUNT = 100000;


}
