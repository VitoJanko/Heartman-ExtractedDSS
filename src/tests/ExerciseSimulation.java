package tests;

import dss.ExerciseDSS;
import models.exercise.Exercise;
import models.exercise.ExerciseNotification;
import models.exercise.WeeklyPlan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ExerciseSimulation {

    public static void main(String[] args) {
        String capacity = getPhysicalCapacity();
        int programWeek = 3;
        HashMap<String, String> exerciseData = readExerciseFiles();
        List<ExerciseNotification> plan = ExerciseDSS.getWeeklyPlan(programWeek, capacity);


        for (ExerciseNotification en : plan){
            System.out.print(en.executionDay+" ");
            System.out.println(en.type+" ");
            String[] ids = ExerciseDSS.getExerciseIds(en.type, capacity);
            for (String id: ids){
                String exercise = exerciseData.get(id);
                System.out.println(exercise);
            }

        }


    }

    private static HashMap<String, String> readExerciseFiles() {
        HashMap<String, String> enduranceLow = readExercise("resources/exercise/endurance-low");
        HashMap<String, String> enduranceNormal = readExercise("resources/exercise/endurance-normal");
        HashMap<String, String> resistanceLow = readExercise("resources/exercise/resistance-low");
        HashMap<String, String> resistanceHigh = readExercise("resources/exercise/resistance-normal");
        HashMap<String, String> allExercises = new  HashMap<String, String>();
        allExercises.putAll(enduranceLow);
        allExercises.putAll(enduranceNormal);
        allExercises.putAll(resistanceHigh);
        allExercises.putAll(resistanceLow);
        return allExercises;
    }

    private static String getPhysicalCapacity() {
        return ExerciseDSS.getPhysicalCapacity(null, 80.0, 200.0);
    }

    private static void showExercise(Exercise e, boolean resistance,
                                     HashMap<String, HashMap<String, String>> exerciseData) {

        String data = exerciseData.get(e.type).get(e.id);

        System.out.println(data);
    }


    private static HashMap<String, String> readExercise(String path) {
        try {
            HashMap<String, String> exercises = new HashMap<String, String>();
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String id = data.split(";")[0];
                exercises.put(id, data);
            }
            myReader.close();
            return exercises;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}


