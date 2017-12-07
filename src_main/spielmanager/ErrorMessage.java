package spielmanager;

public class ErrorMessage {
    private final String problem = "error";
    private String message;
    private String details;
    private String[] possibleCauses;


    public ErrorMessage(String message, String details){
        super();
        this.message = message;
        this.details = details;
    }


    public ErrorMessage(String message, String details, String[] possibleCauses){
        super();
        this.message = message;
        this.details = details;

        this.possibleCauses = possibleCauses;

    }

    public String[] getPossibleCauses(){
        return this.possibleCauses;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
    public String getDetails(){
        return this.details;
    }

    public void setDetails(String details){
        this.details = details;
    }

    public String getProblem() {
        return problem;
    }
}
