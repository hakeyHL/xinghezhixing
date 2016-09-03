package hasoffer.core.search.exception;

import hasoffer.core.exception.BaseException;
import hasoffer.core.exception.ERROR_CODE;

/**
 *
 */
public class WebsiteErrorException extends BaseException {
    private String message;

    public WebsiteErrorException(ERROR_CODE errorCode) {
        super(errorCode);
    }

    public WebsiteErrorException(ERROR_CODE errorCode, String message) {
        super(errorCode);
        this.message = message;
    }

    @Override
    public String toString() {
        return "WebsiteErrorException{" +
                "message='" + message + '\'' +
                '}';
    }
}
