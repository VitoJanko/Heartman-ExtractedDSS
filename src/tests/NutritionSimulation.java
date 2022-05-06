package tests;

import models.nutrition.FilledQuestion;
import models.nutrition.NutritionAdvice;
import models.nutrition.NutritionQuestion;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static dss.NutritionDSS.getNutritionQuestions;
import static dss.NutritionDSS.getNutritionAdvice;



/**
 * Run this file for a simulation of intended schedule of answers and questions
 * First profile is filled.
 * Then some questions are asked, marked "Q".
 * On some days, an advice will be given, marked "A".
 */

public class NutritionSimulation {

    public static void main(String[]param) {
        int TOTAL_NUMBER_OF_WEEKS = 8;
        String[] DAY_OF_WEEK = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};

        String questions1 = "resources/nutrition/behaviour/behaviour-questions-en";
        String questions2 = "resources/nutrition/education/education-questions-en";
        HashMap<String, String> questionsDict = readQuestions(questions1);
        questionsDict.putAll(readQuestions(questions2));

        String advice1 = "resources/nutrition/behaviour/behaviour-advice-en";
        String advice2 = "resources/nutrition/education/education-advice-en";
        HashMap<String, String> adviceDict = readQuestions(advice1);
        adviceDict.putAll(readQuestions(advice2));

        Scanner sc= new Scanner(System.in);
        System.out.print("Enter your height: ");
        int height = sc.nextInt();
        System.out.print("Enter your weight: ");
        int weight = sc.nextInt();
        String temp = sc.nextLine();
        System.out.print("Do you have diabetes: ");
        boolean hasDiabetes = nextBoolean(sc);
        System.out.print("Do you consider yogurt a liquid: ");
        boolean isYogurtLiquid = nextBoolean(sc);
        System.out.print("Allowed liquid per day (in liters): ");
        int liquidPerDay = sc.nextInt();

        List<FilledQuestion> answers = new ArrayList<>();
        List<NutritionAdvice> allAdvice = new ArrayList<>();

        for (int currentWeek=1; currentWeek<=TOTAL_NUMBER_OF_WEEKS; currentWeek++){
            System.out.println();
            System.out.println("--- WEEK: " + currentWeek + " ---");
            List<NutritionQuestion> questions = getNutritionQuestions(currentWeek, hasDiabetes);

            for (int day=1; day<=7; day++){
                List<NutritionAdvice> newAdvice = getNutritionAdvice(currentWeek, answers, liquidPerDay,
                        hasDiabetes, weight, height, isYogurtLiquid, null);
                //System.out.println(answers.size());
                answers.clear();
                allAdvice.addAll(newAdvice);
                System.out.println();
                System.out.println(DAY_OF_WEEK[day-1]);
                if (allAdvice.size() > 0) {
                    NutritionAdvice advice = allAdvice.get(0);
                    allAdvice.remove(0);
                    String id = advice.id;
                    String data = adviceDict.get(id);
                    if (data==null) {
                        System.out.println("Invalid id: "+id);
                    }
                    else {
                        showAdvice(data, advice.segment, advice.tokens);
                        System.out.println();
                    }
                }
                for (NutritionQuestion q : questions){
                    if (q.executionDay != day)continue;
                    for (String id : q.getIds()){
                        String data = questionsDict.get(id);
                        String dataAdvice = adviceDict.get(id);
                        if (data==null) {
                            System.out.println("Invalid id: "+id);
                            continue;
                        }
                        String correctAnswer = getCorrectAnswer(dataAdvice);
                        String answer = showQuestion(data);
                        FilledQuestion answerObj = new FilledQuestion(id, answer, correctAnswer, 0);
                        answers.add(answerObj);
                    }
                }
            }
        }
    }

    private static HashMap<String, String> readQuestions(String path){
        try {
            HashMap<String, String> questions = new HashMap<String, String>();
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String id = data.split(";")[0];
                questions.put(id, data);
            }
            myReader.close();
            return  questions;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return new HashMap<String, String>();
        }
    }

    private static String getCorrectAnswer(String question){
        String[] parts = question.split(";");
        if (parts.length < 2){
            System.out.println("Data missing: "+question);
            return null;
        }
        return parts[1];
    }

    private static String showQuestion(String question){
        String[] parts = question.split(";");
        if (parts.length < 2){
            System.out.println("Data missing: "+question);
            return null;
        }
        System.out.println("Q: "+parts[2]);
        if (parts[1].equals("select")){
            for (int i=3; i<parts.length; i++){
                System.out.print(i-3+": "+parts[i]+"  ");
            }
            System.out.println();
        }

        Scanner sc= new Scanner(System.in);
        String answer = sc.nextLine();

        return answer;
    }

    private static void showAdvice(String data, int segment, String[] values){
        String[] parts = data.split(";");
        if (parts.length < 2){
            System.out.println("Data missing: "+data);
        }
        String advice = String.format(parts[segment], values);

        System.out.println("A: "+advice);
    }

    private static boolean nextBoolean(Scanner sc){
        String answer = sc.nextLine();
        //System.out.println("Answer: "+ answer);
        return answer.equals("yes") || answer.equals("Yes") || answer.equals("true") || answer.equals("y");
    }

}
