package dss;

/**
 * All measurements listed in this class must be checked every morning during morning appointment
 * Time of morning appointments is defined in user profile
 * In some cases, a measurement must be repeated after specified amount of time (see functions below)
 * Measurements can be done with a device or manually entered by the user
 * "null" can be used to indicate that neither measurement is available
 * Alerts for different functions are different and will be defined in the application
 */

public class MeasurementsDSS {

    /**
     * Daily weight check, during morning appointment
     *
     * @param previousWeight weight measurement for three days ago
     * @param currentWeight current weight measurement
     * @return if false, trigger alert
     */
    public static boolean isWeightOK(Double previousWeight, Double currentWeight){
        if (previousWeight == null || currentWeight == null)
            return true;
        double weightGain = currentWeight - previousWeight;
        return weightGain < 2;
    }

    /**
     * Daily pressure check, during morning appointment
     *
     * @param sbp systolic blood pressure measured
     * @param dpb diastolic blood pressure measured
     * @return if false, advice the user to rest for 10 minutes, then remeasure. If false again trigger alert
     */
    public static boolean isPressureOK(Double sbp, Double dpb){
        if (sbp == null || dpb == null)
            return true;
        boolean outOfBounds =  sbp > 180 ||  sbp < 90 || dpb > 100 || dpb < 55;
        return !outOfBounds;
    }

    /**
     * Daily heart rate check, during morning appointment
     *
     * @param hr heart-rate measured
     * @return if False, advice the user to rest for 5 minutes, then remeasure. If false again trigger alert
     */
    public static boolean isHeartRateOK(Double hr){
        if (hr==null)
            return true;
        boolean outOfBounds =  hr > 110 ||  hr < 45;
        return !outOfBounds;
    }


}


