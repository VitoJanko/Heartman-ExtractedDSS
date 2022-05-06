package models.exercise;


import java.util.List;

public class WeeklyPlan {
    /* List containing endurance exercise for the current week */
    public List<Exercise> exerciseEnduranceWeeklyPlan;
    /* List containing resistance exercise for the current week */
    public List<Exercise> exerciseResistanceWeeklyPlan;

    public WeeklyPlan(List<Exercise> exerciseEnduranceWeeklyPlan, List<Exercise> exerciseResistanceWeeklyPlan){
        this.exerciseEnduranceWeeklyPlan = exerciseEnduranceWeeklyPlan;
        this.exerciseResistanceWeeklyPlan = exerciseResistanceWeeklyPlan;
    }

}