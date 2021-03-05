package nl.degrijs.exception;

public class HttpException extends Exception {
    private ExceptionCategory exceptionCategory;

    public HttpException(ExceptionCategory exceptionCategory) {
        super();
        this.exceptionCategory = exceptionCategory;
    }

    public HttpException(ExceptionCategory exceptionCategory, String message) {
        super(message);
        this.exceptionCategory = exceptionCategory;
    }

    public HttpException(ExceptionCategory exceptionCategory, String message, Throwable cause) {
        super(message, cause);
        this.exceptionCategory = exceptionCategory;
    }

    public HttpException(ExceptionCategory exceptionCategory, Throwable cause) {
        super(cause);
        this.exceptionCategory = exceptionCategory;
    }

    protected HttpException(ExceptionCategory exceptionCategory, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.exceptionCategory = exceptionCategory;
    }

    public ExceptionCategory getExceptionCategory() {
        return exceptionCategory;
    }
}
