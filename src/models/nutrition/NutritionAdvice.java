package models.nutrition;



/**
 * An object representing an advice to be displayed.
 *
 * The file organisation in the final application will probably be different, but the fields here present are based
 * on current format -- adapt as needed. To connect this object with the advice text, follow these steps:
 * 1) The file is found in the resources file and is named {type}-en (or a different language)
 * 2) File is in the csv format and the advice is in the row that starts with {id}
 * 3) Every row has many ";" separated values. {segment}-th value represents the advice to show
 * 4) Some advices have "%s" in them, these values are to be substituted with values in the {tokens}
 *
 * If the advice to be displayed is empty or only "-", ignore it and display the next advice in row.
 * The way code is currently written it "should't" happen, but a preventive check should be made for future robustness.
 */


public class NutritionAdvice {
    public String id;
    public String type;
    private Integer executionDay;
    private Integer executionTimeStart;
    private Integer executionTimeEnd;
    public String[] tokens;
    public int segment;

    public NutritionAdvice(String id, String type, int executionDay, int executionTimeStart, int executionTimeEnd,
                           String[] tokens, int segment){
        this.id = id;
        this.type = type;
        this.executionDay = executionDay;
        this.executionTimeStart = executionTimeStart;
        this.executionTimeEnd = executionTimeEnd;
        this.tokens = tokens;
        this.segment = segment;
    }

}