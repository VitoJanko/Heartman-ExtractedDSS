package dss;

import models.exercise.*;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ExerciseDSS {

    public enum WeekPeriod {
        WEEK_1_TO_4, WEEK_5_TO_6, WEEK_7_TO_12, WEEK_13_TO_18, WEEK_19_TO_24
    }

    public enum ExerciseTexts{
        TITLE_ENDURANCE, //Endurance exercise
        TEXT_ENDURANCE, //Today you have an endurance exercise.
        TITLE_RESISTANCE, //Resistance exercise
        TEXT_RESISTANCE, // Today you have a resistance exercise.
        TITLE_FREQUENCY, // Approval for increase of exercise frequency
        TEXT_FREQUENCY, // Would you like to increase frequency of exercises from %d per week to %d " +"per week?"
        CONFIRMATION_FREQUENCY, //Your choice was stored an will be applied next week.
    }



    public static List<ExerciseNotification> getNotificationExercise(int currentWeek, String physicalCapacity) {

        List<ExerciseNotification> exerciseNotifications = new ArrayList<>();

        // Take the disapprovals into account
        WeekPeriod approvedWeekPeriod = getApprovedExerciseWeekPeriod(currentWeek);

        // Get scheduled exercise days
        LinkedList<Integer[]> exerciseDays = getExerciseDays(approvedWeekPeriod, physicalCapacity);
        Integer[] enduranceDays = exerciseDays.getFirst();
        Integer[] resistanceDays = exerciseDays.getLast();

        // Endurance exercise notifications
        String typeEndurance = "endurance";
        for (int i = 0; i < enduranceDays.length; i++) {
            String idEndurance = "Ee".concat(Integer.toString(i + 1));
            int executionDayEndurance = enduranceDays[i];
            ExerciseNotification notificationEndurance = constructNotificationExercise(idEndurance,
                    ExerciseTexts.TITLE_ENDURANCE, ExerciseTexts.TEXT_ENDURANCE, null,
                    typeEndurance, executionDayEndurance);

            exerciseNotifications.add(notificationEndurance);
        }

        // Resistance exercise notifications
        String typeResistance = "resistance";
        for (int k = 0; k < resistanceDays.length; k++) {
            String idResistance = "Er".concat(Integer.toString(k + 1));
            int executionDayResistance = resistanceDays[k];
            ExerciseNotification notificationResistance = constructNotificationExercise(
                    idResistance, ExerciseTexts.TITLE_RESISTANCE, ExerciseTexts.TEXT_RESISTANCE, null,
                    typeResistance, executionDayResistance);

            exerciseNotifications.add(notificationResistance);
        }

        return exerciseNotifications;
    }

    /**
     * Assigns the endurance and resistance days based on the weekPeriod.
     *
     * @param weekPeriod       - Stage of exercise frequency based on time since the first installation of the HM application.
     * @return [enduranceDays, resistanceDays]
     */
    private static LinkedList<Integer[]> getExerciseDays (WeekPeriod weekPeriod, String physicalCapacity) {

        Integer[] enduranceDays;
        Integer[] resistanceDays;

        switch (weekPeriod) {
            case WEEK_1_TO_4:
                if (physicalCapacity.equals("normal")) {
                    enduranceDays = new Integer[]{1, 3, 5};
                } else {
                    enduranceDays = new Integer[]{1, 5};
                }
                resistanceDays = new Integer[]{1, 5};
                break;
            case WEEK_5_TO_6:
                enduranceDays = new Integer[]{1, 3, 5};
                resistanceDays = new Integer[]{1, 5};
                break;
            case WEEK_7_TO_12:
                if (physicalCapacity.equals("normal")) {
                    enduranceDays = new Integer[]{1, 3, 5, 6};
                } else {
                    enduranceDays = new Integer[]{1, 3, 5};
                }
                resistanceDays = new Integer[]{1, 3, 5};
                break;
            case WEEK_13_TO_18:
                if (physicalCapacity.equals("normal")) {
                    enduranceDays = new Integer[]{1, 2, 3, 5, 6};
                } else {
                    enduranceDays = new Integer[]{1, 3, 5, 6};
                }
                resistanceDays = new Integer[]{1, 3, 5};
                break;
            case WEEK_19_TO_24:
                enduranceDays = new Integer[]{1, 2, 3, 5, 6};
                resistanceDays = new Integer[]{1, 3, 5};
                break;
            default:        // low capacity, WEEK_1_TO_4
                enduranceDays = new Integer[]{1, 5};
                resistanceDays = new Integer[]{1, 5};
                break;
        }

        LinkedList<Integer[]> exerciseDays = new LinkedList<>();
        exerciseDays.add(enduranceDays);
        exerciseDays.add(resistanceDays);

        assert(exerciseDays.size() == 2);

        return exerciseDays;
    }

    private static ExerciseNotification constructNotificationExercise(String id, ExerciseTexts title, ExerciseTexts text,
                                                               ExerciseTexts confirmation,
                                                               String type, int executionDay) {
        ExerciseNotification ne = new ExerciseNotification(id, type, title, text, confirmation,
                true, executionDay);

        return ne;
    }

    /**
     * Sums the endurance and resistance exercises.
     * @param wp
     * @return The number of exercises per week.
     */
    private static int calculateTotalExerciseDays(WeekPeriod wp, String physicalCapacity) {
        List<Integer[]> allExerciseDays = getExerciseDays(wp, physicalCapacity);
        int totalExerciseDays = 0;
        for (Integer[] exerciseDays : allExerciseDays) {
            totalExerciseDays += exerciseDays.length;
        }
        return totalExerciseDays;
    }

    /**
     * Calculates the exercise stage based on week period and user's consent.
     * @return Approved exercise week period.
     */
    private static WeekPeriod getApprovedExerciseWeekPeriod(int currentWeek) {

        WeekPeriod automaticWeekPeriod = getWeekPeriod(currentWeek);

        // Take the disapprovals into account
        int sumOfDisapprovals = calculateSumOfDissaprovals();
        return considerIncreaseFrequencyDisapprovals(sumOfDisapprovals, automaticWeekPeriod);
    }

    // Increased frequency
    public static List<ExerciseNotification> getNotificationIncreaseFrequency(int currentWeek, String physicalCapacity) {
        List<ExerciseNotification> increaseNotifications = new ArrayList<>();

        WeekPeriod approvedWeekPeriod = getApprovedExerciseWeekPeriod(currentWeek);
        int totalExerciseDays = calculateTotalExerciseDays(approvedWeekPeriod, physicalCapacity);

        WeekPeriod automaticWeekPeriod = getWeekPeriod(currentWeek);
        WeekPeriod nextWeekPeriod = getNextWeekPeriod(approvedWeekPeriod);

        int totalExerciseDaysNextWeekPeriod = calculateTotalExerciseDays(nextWeekPeriod, physicalCapacity);

        if (totalExerciseDays >= totalExerciseDaysNextWeekPeriod) {
            return increaseNotifications;   // Empty list since the increase takes no effect
        }

        // The notification is offered one week before the scheduled automatic change
        Integer[] triggerWeeks = new Integer[] {4, 6, 12, 18};
        if (Arrays.asList(triggerWeeks).contains(currentWeek)){
            String id = String.format("increaseFrequencyWeek%d", currentWeek);
            String type = "increase";

            ExerciseNotification notification = constructNotificationExercise(id, ExerciseTexts.TITLE_FREQUENCY,
                    ExerciseTexts.TEXT_FREQUENCY, ExerciseTexts.CONFIRMATION_FREQUENCY, type, 0);

            increaseNotifications.add(notification);
        }
        return increaseNotifications;
    }

    private static WeekPeriod getNextWeekPeriod(WeekPeriod currentWeekPeriod) {
        WeekPeriod nextWeekPeriod;
        switch (currentWeekPeriod) {
            case WEEK_1_TO_4:
                nextWeekPeriod = WeekPeriod.WEEK_5_TO_6;
                break;
            case WEEK_5_TO_6:
                nextWeekPeriod = WeekPeriod.WEEK_7_TO_12;
                break;
            case WEEK_7_TO_12:
                nextWeekPeriod = WeekPeriod.WEEK_13_TO_18;
                break;
            case WEEK_13_TO_18:
                nextWeekPeriod = WeekPeriod.WEEK_19_TO_24;
                break;
            case WEEK_19_TO_24:
                nextWeekPeriod = WeekPeriod.WEEK_19_TO_24; // The last exercise stage
                break;
            default:
                nextWeekPeriod = currentWeekPeriod;
                break;
        }
        return nextWeekPeriod;
    }

    /**
     * How many times the user disapproved to increase the frequency of exercises.
     */
    private static int calculateSumOfDissaprovals() {
        int sumOfDisapprovals = 0;
        //if (dataLayer.getApprovalIncreaseFrequency() != null) {
        //    for (ApprovalIncreaseFrequency approval : dataLayer.getApprovalIncreaseFrequency()) {
        //        int disapprovalNumeric = approval.getIncrease() ? 0 : -1;   // true -> 0; false -> -1
        //        sumOfDisapprovals += disapprovalNumeric;
        //    }
        //}
        return sumOfDisapprovals;
    }

    /**
     * @param sumOfDisapprovals - How many times the user disapproved to increase exercise frequency.
     * @param currentWeekPeriod - Automatic stage of exercise frequency based on time since the first installation of the HM application.
     * @return Week period for exercise frequency; the (dis)approval taken into account.
     */
    private static WeekPeriod considerIncreaseFrequencyDisapprovals(int sumOfDisapprovals,
                                                             WeekPeriod currentWeekPeriod) {
        HashMap<WeekPeriod, WeekPeriod> mapWeekPeriods = new HashMap<>();
        mapWeekPeriods.put(WeekPeriod.WEEK_1_TO_4, WeekPeriod.WEEK_1_TO_4);       // Minimum exercise frequency - stays the same
        mapWeekPeriods.put(WeekPeriod.WEEK_5_TO_6, WeekPeriod.WEEK_1_TO_4);       // Decrease to previous stage
        mapWeekPeriods.put(WeekPeriod.WEEK_7_TO_12, WeekPeriod.WEEK_5_TO_6);
        mapWeekPeriods.put(WeekPeriod.WEEK_13_TO_18, WeekPeriod.WEEK_7_TO_12);
        mapWeekPeriods.put(WeekPeriod.WEEK_19_TO_24, WeekPeriod.WEEK_13_TO_18);
        if (sumOfDisapprovals >= 0) {
            return currentWeekPeriod;
        }
        else {
            return considerIncreaseFrequencyDisapprovals(
                    sumOfDisapprovals + 1, mapWeekPeriods.get(currentWeekPeriod));
        }
    }



    public static WeeklyPlan getWeeklyPlan(String physicalCapacity) {

        List<Exercise> listOfExerciseEndurance = new ArrayList<>();
        List<Exercise> listOfExerciseResistance = new ArrayList<>();
        int eLength = (physicalCapacity.equals("normal") ? 5 : 4);
        int rLength = (physicalCapacity.equals("normal") ? 16 : 14);

        for (int k = 0; k < eLength; k++) {
            String number = ""+(k+1);
            if (number.length() == 1) number = "0"+number;
            String capacity = physicalCapacity.equals("normal") ? "N" : "L";

            Exercise exercise = new Exercise("ee"+number+capacity, "endurance-"+physicalCapacity);
            //if (exerciseAttributes[2].equals("high")) {
            //    continue;
            //}
            listOfExerciseEndurance.add(exercise);
        }

        for (int k = 0; k < rLength; k++) {
            String number = ""+(k+1);
            if (number.length() == 1) number = "0"+number;
            String capacity = physicalCapacity.equals("normal") ? "N" : "L";

            //if (exerciseAttributes[2].equals("high")) {
            //    continue;
            //}

            Exercise exercise = new Exercise("er"+number+capacity, "resistance-"+physicalCapacity);

            listOfExerciseResistance.add(exercise);
        }

        return new WeeklyPlan(listOfExerciseEndurance, listOfExerciseResistance);
    }


    public static String getPhysicalCapacity(Double maxWattage, Double weight, Double sixMinuteWalkDistance ) {
        if (maxWattage != null) {
            return (maxWattage / weight >= 1 ? "normal" : "low");
        } else if (sixMinuteWalkDistance != null) {
            return (sixMinuteWalkDistance >= 300 ? "normal" : "low");
        } else {
            return "low";
        }
    }


    private static WeekPeriod getWeekPeriod(Integer week) {
        WeekPeriod weekPeriod;
        if (week <= 4) {
            weekPeriod = WeekPeriod.WEEK_1_TO_4;
        } else if (week <= 6) {
            weekPeriod = WeekPeriod.WEEK_5_TO_6;
        } else if (week <= 12) {
            weekPeriod = WeekPeriod.WEEK_7_TO_12;
        } else if (week <= 18) {
            weekPeriod = WeekPeriod.WEEK_13_TO_18;
        } else {
            weekPeriod = WeekPeriod.WEEK_19_TO_24;
        }
        return weekPeriod;
    }


    private static Boolean getIncreaseIntensity(StaticParameters staticParameters,
                                         Long startAppDate, Long currentTime, Double maxHeartRate) {
        List<ExerciseReport> ers = dataLayer.getExerciseReports(startAppDate, currentTime);
        if (ers == null) {
            ers = new ArrayList<>();
        }
        int propOverHR = 0;
        int propAdh = 0;
        int propCom = 0;

        for (ExerciseReport e : ers) {
//            System.out.println(e);
            boolean flag = true;
//            System.out.println(e.getOpinion().getIntensity());
            if (e.intensity < 6) {
                propCom++;
            }
            if (e.validExercise) {
                propAdh++;
            }
            if (maxHeartRate == null) {
                propOverHR++;
                continue;
            }
            for (ExerciseTime et : e.getTimelimits()) {
                List<HeartRate> listHR = dataLayer.getHeartRates(et.getStartTime(), et.getStopTime());
                if (flag) {
                    for (HeartRate hr : listHR) {
//                        System.out.println("HR" + hr);
                        if (hr.getValue() > maxHeartRate) {
                            flag = false;
                            break;
                        }
                    }
                } else {
                    propOverHR++;
                    break;
                }
            }
        }

        Boolean prHR = (double) propOverHR < 0.8 * ers.size();
        Boolean prAdh = (double) propAdh > 0.6 * ers.size();
        Boolean prCom = (double) propCom > 0.8 * ers.size();

        return (prHR && prAdh) || (prHR && prCom) || (prAdh && prCom);
    }



    public static HL7 getHL7Param(int currentWeek, String physicalCapacity, boolean icd, Double maxWattage,
                                  int maxHeartRate, double restingHeartRate) {

        Integer lowerHeartRate = null;
        Integer upperHeartRate = null;
        Double maxHR;
        Double restingHR;
        Double deltaDown;
        Double deltaUp;
        String exerciseEnduranceIntensity;

        int modfifiedHeartRate;

        if ((diffTime > initPhase) && getIncreaseIntensity(staticParameters, startAppDate, currentTime)) {
            exerciseEnduranceIntensity = physicalCapacity.equals("normal") ? "normal-normal" : "low-normal";

        } else {
            exerciseEnduranceIntensity = physicalCapacity.equals("normal") ? "normal-low" : "low-low";
        }

        if (icd && (maxWattage != null)) {
            modfifiedHeartRate = maxHeartRate - 20;
            deltaDown = physicalCapacity.equals("normal") ? 0.5 : 0.4;
            deltaUp = physicalCapacity.equals("normal") ? 0.6 : 0.5;
            if (exerciseEnduranceIntensity.equals("normal-normal") || exerciseEnduranceIntensity.equals("low-normal")) {
                deltaDown = deltaDown + 0.1;
                deltaUp = deltaUp + 0.1;
            }
            lowerHeartRate = (int) (restingHeartRate + deltaDown * (maxHeartRate - restingHeartRate));
            upperHeartRate = (int) (restingHeartRate + deltaUp * (maxHeartRate - restingHeartRate));
        } else if (maxWattage != null) {
            maxHR = (double) maxHeartRate;
            restingHR = restingHeartRate;
            deltaDown = physicalCapacity.equals("normal") ? 0.5 : 0.4;
            deltaUp = physicalCapacity.equals("normal") ? 0.6 : 0.5;
            if (exerciseEnduranceIntensity.equals("normal-normal") || exerciseEnduranceIntensity.equals("low-normal")) {
                deltaDown = deltaDown + 0.1;
                deltaUp = deltaUp + 0.1;
            }
            modfifiedHeartRate = (int) (maxHR * 0.9);
            lowerHeartRate = (int) (restingHR + deltaDown * (maxHR - restingHR));
            upperHeartRate = (int) (restingHR + deltaUp * (maxHR - restingHR));
        } else if (icd) {
            modfifiedHeartRate = maxHeartRate - 20;
        }

        WeekPeriod approvedWeekPeriod = getApprovedExerciseWeekPeriod(currentWeek);
        LinkedList<Integer []> exerciseDays = getExerciseDays(approvedWeekPeriod, physicalCapacity);

        Integer[] enduranceDays = exerciseDays.getFirst();
        Integer[] resistanceDays = exerciseDays.getLast();

        HL7 hl7 = HL7.builder()
                .takesBetaBlockers(false)
                .takesACEInhibitors(false)
                .takesARBs(false)
                .takesDiuretics(false)
                .takesLoopDiuretics(false)
                .maxHeartRate(modfifiedHeartRate)
                .upperHeartRate(upperHeartRate)
                .lowerHeartRate(lowerHeartRate)
                .exerciseEnduranceIntensity(exerciseEnduranceIntensity)
                .exerciseEnduranceDuration(physicalCapacity.equals("normal") ? 20 : 15)
                .exerciseEnduranceFrequency(enduranceDays.length)
                .exerciseResistanceIntensity(exerciseEnduranceIntensity)
                .exerciseResistanceFrequency(resistanceDays.length)
                .build();
        return hl7;
    }


}
