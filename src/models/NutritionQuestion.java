package models;

public class NutritionQuestion {
    public String ids;
    public String type;
    public Integer executionDay;
    private Integer executionTimeStart;
    private Integer executionTimeEnd;


    /**
     * An object representing a question to be asked.
     *
     * The file organisation in the final application will probably be different, but the fields here present are based
     * on current format -- adapt as needed. To connect this object with the question text, follow these steps:
     * 1) The file is found in the resources file and is named {type}-en (or a different language)
     * 2) File is in the csv format and the advice is in the row that starts with {id}
     * 3) Every row has many ";" separated values. Third value represents the question to show
     *
     * If the second value in the row is "select", then its a multiple choice question. Options are found from value 4 on.
     * When saving this answer (see FilledQuestion), use index of the answer, with first option being 0.
     * If the second value in the row is "input" then a numeric value is expected
     *
     * The question should be displayed on the {executionDay} of the current week. Monday is 1, Tuesday is 2, etc.
     */


    public NutritionQuestion(String ids, String type, int executionDay, int executionTimeStart, int executionTimeEnd){
        this.ids = ids;
        this.type = type;
        this.executionDay = executionDay;
        this.executionTimeStart = executionTimeStart;
        this.executionTimeEnd = executionTimeEnd;
    }

    public String[] getIds(){
        return ids.split(";");
    }

}