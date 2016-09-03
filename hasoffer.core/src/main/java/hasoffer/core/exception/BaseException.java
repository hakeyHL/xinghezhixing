package hasoffer.core.exception;

/**
 *
 */
public class BaseException extends  RuntimeException {
    private ERROR_CODE errorCode;

    public BaseException(String message) {
        super(message);
        this.errorCode = ERROR_CODE.UNKNOWN;
    }

    public BaseException(ERROR_CODE errorCode) {
        this.errorCode = errorCode;
    }

    public BaseException(String message, ERROR_CODE errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseException(String message, Throwable cause, ERROR_CODE errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public BaseException(Throwable cause, ERROR_CODE errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ERROR_CODE errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public ERROR_CODE getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ERROR_CODE errorCode) {
        this.errorCode = errorCode;
    }
}
