package dss;

import models.exercise.*;

import java.util.*;

/*
The application keeps track of:
"program week"  -- starts at 1
"physical capacity" -- determined by getPhysicalCapacity function
"program intensity" -- starts at "low"

At the beginning of each week "getWeeklyPlan" is called to get on which days the user is to perform what kind of
exercise. The output depends on the "program week" and "physical capacity".

The above function returns  ExerciseNotification that importantly holds "executionDay" to perform the exercise and
"type". Calling "getExerciseIds" with this type returns all IDs of all exercises corresponding to given capacity and type.
User may freely select one of the available exercises.

Program week increases by one at the end of each week if the user performed at least one exercise this week and
agrees to progress.

After a specified number of weeks, increaseIntensity is called. If it returns True, the "program intensity" may increase
from low to normal. The input for this function is the list of "ExerciseReports". A simple object filled with
"did user finish the exercise" and "exercise difficulty"

 */


public class ExerciseDSS {

    /**
     * Called when profile is made or updated. Weight must not be None, other two can be.
     *
     * @param maxWattage max power the user can emmit during doctor-controlled test
     * @param weight user's weight
     * @param sixMinuteWalkDistance distance made in 6 minutes
     * @return physical capacity; can be "low" or "normal"
     */
    public static String getPhysicalCapacity(Double maxWattage, Double weight, Double sixMinuteWalkDistance ) {
        if (maxWattage != null) {
            return (maxWattage / weight >= 1 ? "normal" : "low");
        } else if (sixMinuteWalkDistance != null) {
            return (sixMinuteWalkDistance >= 300 ? "normal" : "low");
        } else {
            return "low";
        }
    }


    /**
     * Called at the beginning of every week.
     *
     * @param physicalCapacity either "low" or "normal"
     * @param currentWeek program week - every week it may increase by 1
     * @return list of all exercises to be done that week
     */
    public static List<ExerciseNotification> getWeeklyPlan(int currentWeek, String physicalCapacity) {

        List<ExerciseNotification> exerciseNotifications = new ArrayList<>();
        WeekPeriod weekPeriod = getWeekPeriod(currentWeek);

        // Get scheduled exercise days
        LinkedList<Integer[]> exerciseDays = getExerciseDays(weekPeriod, physicalCapacity);
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


    public static Boolean increaseIntensity( List<ExerciseReport> ers) {

        int propAdh = 0;
        int propCom = 0;

        for (ExerciseReport e : ers) {

            if (e.intensity < 6) {
                propCom++;
            }
            if (e.excerciseCompleted) {
                propAdh++;
            }

        }

        Boolean prAdh = (double) propAdh > 0.6 * ers.size();
        Boolean prCom = (double) propCom > 0.8 * ers.size();

        return prAdh & prCom;

    }

    /**
     * Returs valid exercises for a given exerciseType, physicalCapacity combo
     *
     * @param exerciseType either "resistance" or "endurance"
     * @param physicalCapacity either "normal" or "low"
     * @return list of IDs of relevant exercises. All exercises can be found in ./resources/exercise
     */
    public static String[] getExerciseIds(String exerciseType, String physicalCapacity){

        if (exerciseType.equals("resistance") && physicalCapacity.equals("low")){
            return new String[] {"er01L", "er02L", "er03L", "er04L", "er05L", "er06L"};
        }
        if (exerciseType.equals("resistance") && physicalCapacity.equals("normal")){
            return new String[] {"er01N", "er02N", "er03N", "er04N", "er05N", "er06N", "er07N", "er08N"};
        }
        if (exerciseType.equals("endurance") && physicalCapacity.equals("low")){
            return new String[] {"ee01L", "ee02L"};
        }
        if (exerciseType.equals("endurance") && physicalCapacity.equals("normal")){
            return new String[] {"ee01N", "ee02N"};
        }
        return new String[] {};

    }


    /**
     * Assigns the endurance and resistance days based on the weekPeriod.
     *
     * @param weekPeriod       - Stage of exercise frequency based on time since the first installation of the HM application.
     * @return [enduranceDays, resistanceDays]
     */
    public static LinkedList<Integer[]> getExerciseDays (WeekPeriod weekPeriod, String physicalCapacity) {

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

    public static ExerciseNotification constructNotificationExercise(String id, ExerciseTexts title, ExerciseTexts text,
                                                               ExerciseTexts confirmation,
                                                               String type, int executionDay) {
        ExerciseNotification ne = new ExerciseNotification(id, type, title, text, confirmation,
                true, executionDay);

        return ne;
    }

    public static WeekPeriod getWeekPeriod(Integer week) {
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


}
