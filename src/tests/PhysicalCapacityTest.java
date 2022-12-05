package tests;

import static dss.ExerciseDSS.getPhysicalCapacity;

public class PhysicalCapacityTest {
    public static void main(String[]param) {
        System.out.println ("maxWattage = 100, sixMinuteWalkDistance = 400: " + getPhysicalCapacity (100.0, 70.0, 400.0));
        System.out.println ("maxWattage = 100, sixMinuteWalkDistance = 200: " + getPhysicalCapacity (100.0, 70.0, 200.0));
        System.out.println ("maxWattage = 50, sixMinuteWalkDistance = 400: " + getPhysicalCapacity (50.0, 70.0, 400.0));
        System.out.println ("maxWattage = 50, sixMinuteWalkDistance = 200: " + getPhysicalCapacity (50.0, 70.0, 200.0));
        System.out.println ("maxWattage = 50, sixMinuteWalkDistance = null: " + getPhysicalCapacity (50.0, 70.0, null));
        System.out.println ("maxWattage = 100, sixMinuteWalkDistance = null: " + getPhysicalCapacity (100.0, 70.0, null));
        System.out.println ("maxWattage = null, sixMinuteWalkDistance = 200: " + getPhysicalCapacity (null, 70.0, 200.0));
        System.out.println ("maxWattage = null, sixMinuteWalkDistance = 400: " + getPhysicalCapacity (null, 70.0, 400.0));
        System.out.println ("maxWattage = null, sixMinuteWalkDistance = null: " + getPhysicalCapacity (null, 70.0, null));
    }

}
