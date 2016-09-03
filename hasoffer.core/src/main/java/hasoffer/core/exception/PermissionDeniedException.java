package hasoffer.core.exception;

/**
 * Date : 2016/3/11
 * Function :
 */
public class PermissionDeniedException extends BaseException {

    public PermissionDeniedException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "PermissionDeniedException : " + getMessage();
    }
}
