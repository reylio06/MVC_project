public class BusinessException extends Exception{

    private final String operationName;

    public BusinessException(String message, String operationName){

        super(message);
        this.operationName = operationName;
    }

    public String getOperationName() {
        return operationName;
    }
}
