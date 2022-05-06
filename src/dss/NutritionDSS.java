package dss;

import models.nutrition.FilledQuestion;
import models.nutrition.NutritionQuestion;
import models.nutrition.NutritionAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Every Monday the application needs to call "getNutritionQuestions" to get a list of questions for that week,
 * and every morning the "getNutritionAdvice", to get daily advice.
 *
 * The questions are represented by "NutritionQuestion" objects and should be presented to the user on the scheduled day.
 * The user is encouraged to fill them as they appear, but is not required to do so. Application shows
 * "weekly adherence" (circle) that show the progress towards filling all the questions and a graph showing the history
 * of success.
 *
 * The function returning advices is called daily and may return some number of advices (number may be zero) in the form
 * of "NutritionAdvice" objects. Only one advice is displayed in the application each day. The application must thus keep
 * a list of all advices received so far and pick one in "first in, first out" fashion.
 *
 * For generating the advice, a list of answered questions ("FilledQuestion" object) must be given. The list sent includes
 * only questions answered the previous day. E.g., if the user fill 2 questions on monday, these two would be sent to
 * DSS on Tuesday morning advice request. If he fills one question that day, only that question is sent to DSS next day.
 *
 * A sample console simulation on how this machinery is provided is in the "NutritionSimulation" class
 */

public class NutritionDSS {

    /**
     * Daily weight check, during morning appointment
     *
     * @param currentWeek index of the current week. First week has an index of 1, next is 2 etc.
     * @param hasDiabetes does user have diabetes (queried from the profile)
     * @return a list of questions for the current week
     */
     public static List<NutritionQuestion> getNutritionQuestions(int currentWeek, boolean hasDiabetes) {
         List<NutritionQuestion> listOfNutritionQuestion = new ArrayList<>();

         if (currentWeek % 16 == 1) {
            listOfNutritionQuestion = getQuestionsNutritionEducation(hasDiabetes);
         }
         else if (currentWeek % 16 == 2 || currentWeek % 16  == 9) {
            listOfNutritionQuestion = getQuestionsNutritionBehaviour();
         }
         return listOfNutritionQuestion;
     }

    /**
     * Daily weight check, during morning appointment
     *
     * @param currentWeek index of the current week. First week has an index of 1, next is 2 etc.
     * @param filledQuestionnaire a list of questions answered the previous day
     * @param allowedLiquidPerDay the recommended amount of daily liquid, usually given by a doctor(queried from the profile)
     * @param hasDiabetes does user have diabetes (queried from the profile)
     * @param weight user width(queried from the profile)
     * @param height user height (queried from the profile)
     * @param isYogurtLiquid whether user considers yogurt as a liquid (queried from the profile)
     * @param psychologicalProfile set to null, could be used later after another part of DSS is untangled
     * @return a list of questions for the current week
     */
     public static List<NutritionAdvice> getNutritionAdvice(int currentWeek,
                                                            List<FilledQuestion> filledQuestionnaire,
                                                            double allowedLiquidPerDay,
                                                            boolean hasDiabetes,
                                                            double weight,
                                                            double height,
                                                            boolean isYogurtLiquid,
                                                            String psychologicalProfile) {
        List<NutritionAdvice> nnAdvice = new ArrayList<>();

         //if (currentWeek % 16 == 3 || currentWeek % 16 == 10) {
         nnAdvice.addAll(getAdviceBehaviour(filledQuestionnaire, allowedLiquidPerDay, hasDiabetes, weight,
                                            height, isYogurtLiquid));

         nnAdvice.addAll(geAdviceEducation(filledQuestionnaire));

         return nnAdvice;
         }

     /**
      * Only private methods from here on
     */

     private static List<NutritionQuestion> getQuestionsNutritionEducation(boolean hasDiabetes) {
            List<NutritionQuestion> educationNotification = new ArrayList<>();

            for (int executionDay = 1; executionDay <= 6; executionDay++) {
                String ids = null;
                switch (executionDay) {
                    case 1:
                        ids = "q00;q01;q02";
                        break;
                    case 2:
                        ids = "q03;q04";
                        break;
                    case 3:
                        ids = "q05;q06";
                        break;
                    case 4:
                        ids = "q07;q08;q09";
                        break;
                    case 5:
                        ids = "q10;";
                        break;
                    case 6:
                        if (hasDiabetes) {
                            ids = "q11;q12;q13";
                        } else {
                            ids = "q11";
                        }
                        break;
                }
                NutritionQuestion nn = constructQuestion(ids, executionDay);
                educationNotification.add(nn);
            }
            return educationNotification;
     }


    private  static List<NutritionQuestion> getQuestionsNutritionBehaviour() {
        List<NutritionQuestion> nnBehaviourList = new ArrayList<>();

        String idsDrinking = "qb01;qb02;qb03;qb04;qb05;qb06;qb07";
        NutritionQuestion nnBehaviourDrinking = constructQuestion(idsDrinking, 2);
        nnBehaviourList.add(nnBehaviourDrinking);

        String idsEating = "qb08;qb09;qb10;qb11;qb12;qb13;qb14;qb15;" +
                           "qb16;qb17;qb18;qb19;qb20;qb21;qb22;qb23;qb24;qb25;qb26;qb27;qb28;qb29";
        NutritionQuestion nnBehaviourEating = constructQuestion(idsEating, 4);
        nnBehaviourList.add(nnBehaviourEating);
        return nnBehaviourList;
    }

    private static NutritionQuestion constructQuestion(String ids, int executionDay) {
        return new NutritionQuestion(ids, "questionnaire", executionDay, 9, 18);
    }


    private static List<NutritionAdvice> geAdviceEducation(List<FilledQuestion> filledQuestionnaire){
        List<NutritionAdvice> adviceEducation = new ArrayList<>();
        for (FilledQuestion filledQuestion : filledQuestionnaire) {
            if (!isQuestionEducational(filledQuestion.id)) continue;

            int answer, correctAnswer;
            try {
                answer = Integer.valueOf(filledQuestion.answer);
                correctAnswer = Integer.valueOf(filledQuestion.correctAnswer);
            } catch(NumberFormatException e){
                continue;
            }

            if (answer != correctAnswer){
                //System.out.println(filledQuestion.id+" "+answer +" "+correctAnswer);
                NutritionAdvice advice = constructAdvice(filledQuestion.id, "advice", 0,
                null, 2);
                adviceEducation.add(advice);
            }
        }
        return adviceEducation;
    }

    private static boolean isQuestionEducational(String id){
        String[] educationIds = new String[]{"q00", "q01", "q02", "q03", "q04", "q05", "q06", "q07", "q08", "q09",
                "q10", "q11", "q12", "q13", };
        for (String edID : educationIds){
            if (edID.equals(id))
                return true;
        }
        return false;
    }

    // Behavioural advice
    private static List<NutritionAdvice> getAdviceBehaviour(List<FilledQuestion> filledQuestionnaire,
                                                                          double allowedLiquidPerDay,
                                                                          boolean hasDiabetes,
                                                                          double weight,
                                                                          double height,
                                                                          boolean isYogurtLiquid) {
        List<NutritionAdvice> notificationAdviceBehaviour = new ArrayList<>();

        if (filledQuestionnaire == null) {
            return notificationAdviceBehaviour;
        }

        double totalFluidIntake = 0.0; // In liters
        int executionDay = 0;
        boolean liquidAdvice = false;

        for (FilledQuestion filledQuestion : filledQuestionnaire) {

            // If questionnaire is more than 3 weeks old, give no advice
            if ( filledQuestion.getDaysAfterAnswer() > 21) {
                continue;
            }

            String id = filledQuestion.id;
            if (mapNutritionBehaviourId(id) == null){
                continue; // The key is not present
            }

            if(id.equals("qb01"))
                liquidAdvice = true;
            int k = mapNutritionBehaviourId(id);

            int answer;

            // If the input is in wrong format, display the first advice.
            try {
                answer = Integer.valueOf(filledQuestion.answer);
            } catch(NumberFormatException e){
                answer = 0;
            }

            int i = 2; // Normal advice
            // First 7 questions combined form advice on total fluid intake
            if (k <= 6) {
                // Add to total fluid intake
                switch (k) {
                    case 1:
                        totalFluidIntake += answer * 0.200; // 200 ml
                        break;
                    case 2:
                        totalFluidIntake += answer * 0.150;
                        break;
                    case 3:
                        totalFluidIntake += answer * 0.250;
                        break;
                    case 4:
                        totalFluidIntake += answer * 0.125;
                        break;
                    case 5:
                        totalFluidIntake += answer * 0.125; // Alcohol as a fluid
                        break;
                    case 6:
                        totalFluidIntake += answer * 0.015;
                        break;
                     default:
                        break;
                }
            }
            // Advice on fluid intake. It has within the 10% of the fluid intake prescribed by doctor.


            // Bread and pastries intake
            if (k == 12 || k == 13 && hasDiabetes) {
                i = 3;
            }

            // Advice on fried potatoes intake
            if (k == 23 && answer == 0) {
                continue; // Skip the advice
            }

            // Advice on rice or pasta intake
            if ((k == 25 && answer == 0) || (k == 27 && answer == 0)) {
                continue;
            }

            // Fruit intake
            if (k == 28) {

                double BMI = weight / Math.sqrt(height);
                if (BMI > 25) {
                    i = 3;
                }
                else if (hasDiabetes)
                    i = 4;
            }

            if (k == 29) {
                if (isYogurtLiquid)
                    i = 4;
                else if (hasDiabetes)
                    i = 3;
            }

            // Note that this is behaviour ID
            // if (getAdviceNutrition(k, "behaviour", "advice", "en")[i].equals("-"))
            //                continue;
            String key = k+"";
            if(key.length() == 1) key = "0"+key;
            key = "qb" +key;
            if (isValidBehaviourAdvice(key)) {
                NutritionAdvice advice = constructAdvice(key, "advice", executionDay, null, i);
                notificationAdviceBehaviour.add(advice);
                executionDay++;
            }
        }
        if (liquidAdvice) {
            int i = 1;
            if (totalFluidIntake > 1.1 * allowedLiquidPerDay) {
                i = 2; // Too high intake advice
            } else if (totalFluidIntake < 0.9 * allowedLiquidPerDay) {
                i = 3; // Too low intake advice
            }
            if (i != 1) {
                String[] tokens = {totalFluidIntake + "", allowedLiquidPerDay + ""};
                NutritionAdvice advice = constructAdvice("qb07", "advice", executionDay, tokens, i);
                notificationAdviceBehaviour.add(advice);
            }
        }

    return notificationAdviceBehaviour;
    }

    private static NutritionAdvice constructAdvice(String id, String type, int executionDay,
                                                         String[] tokens, int segment){
        return new NutritionAdvice(id, type, executionDay, 9, 18, tokens, segment);
    }

    private static boolean isValidBehaviourAdvice(String id){
        String[] invalid = {"qb00","qb01","qb02","qb03","qb04","qb06","qb07","qb09","qb10","qb22","qb26"};
        for(String invalidKey : invalid){
            if (id.equals(invalidKey))
                return false;
        }
        return true;
    }

    private static Integer mapNutritionBehaviourId(String id) {
        String[] ids = new String[]{"qb00", "qb01", "qb02", "qb03", "qb04", "qb05", "qb06", "qb07", "qb08", "qb09", "qb10", "qb11", "qb12", "qb13", "qb14", "qb15", "qb16", "qb17", "qb18", "qb19", "qb20", "qb21", "qb22", "qb23", "qb24", "qb25", "qb26", "qb27", "qb28", "qb29"};
        Integer[] numbers = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29};
        HashMap<String, Integer> idMap = new HashMap<>();
        for (int i = 0; i < ids.length; i++) {
            idMap.put(ids[i], numbers[i]);
        }
        return idMap.get(id);
    }





}
