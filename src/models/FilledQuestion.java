package models;

/**
 * An object representing an answer of a question asked.
 *
 * To create this object you need to provide the following parameters
 * id - should match the id of the question
 * answer - answer of the question. Currently all answers are numerical, but String format is used for future generality.
 * correctAnswer - the correct answer of the question. Can be found by first finding the advice with the same id as
 *        the question (see Nutrition advice) and take the second value of that row. Currently its position is a bit
 *        inconvenient so adapt as necessary.
 * dateFilled - timestamp of the time when question is filled. Currently not used, but here for future generality.
 */

public class FilledQuestion {
    /* The id of the question. */
    public String id;
    /*
    The text inserted by user in case of "input" questions
    Id of the option selected, in case of "select" questions
     */
    public String answer;
    private int daysAfterAnswer;
    public long dateFilled;
    public String correctAnswer;

    public FilledQuestion(String id, String answer, String correctAnswer, long dateFilled){
        this.id = id;
        this.answer = answer;
        this.correctAnswer = correctAnswer;
        this.dateFilled = dateFilled;
        this.daysAfterAnswer = 0;
    }

    public void updateAge(long dateCurrent){
        daysAfterAnswer = (int)((dateCurrent - dateFilled) / (1000 * 60 *60 * 24 ));
    }

    public int getDaysAfterAnswer(){
        return daysAfterAnswer;
    }
}
