package hasoffer.core.exception;

/**
 * Created by glx on 2015/10/13.
 */
public class UnknownException extends BaseException {
    public UnknownException() {
        super(ERROR_CODE.UNKNOWN);
    }

    public UnknownException(String message) {
        super(message,ERROR_CODE.UNKNOWN);
    }

    public UnknownException(String message, Throwable cause) {
        super(message, cause, ERROR_CODE.UNKNOWN);
    }

    public UnknownException(Throwable cause) {
        super(cause, ERROR_CODE.UNKNOWN);
    }

    public UnknownException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace, ERROR_CODE.UNKNOWN);
    }
}
