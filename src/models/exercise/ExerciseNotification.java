package models.exercise;

import dss.ExerciseDSS;

public class ExerciseNotification {
    public String id;
    public String type;	// "endurance" or "resistance"
    public ExerciseDSS.ExerciseTexts title;
    public ExerciseDSS.ExerciseTexts text;
    public ExerciseDSS.ExerciseTexts confirmation;
    private Boolean allowedToPostponeForTomorrow;
    public Integer executionDay;
    private Integer executionTimeStart;
    private Integer executionTimeEnd;

    public ExerciseNotification(String id, String type, ExerciseDSS.ExerciseTexts title, ExerciseDSS.ExerciseTexts text,
                                ExerciseDSS.ExerciseTexts confirmation, Boolean allowedToPostponeForTomorrow,
                                Integer executionDay){
        this.id = id;
        this.type = type;
        this.title = title;
        this.text = text;
        this.allowedToPostponeForTomorrow = allowedToPostponeForTomorrow;
        this.executionDay = executionDay;
        this.confirmation = confirmation;
    }

}
