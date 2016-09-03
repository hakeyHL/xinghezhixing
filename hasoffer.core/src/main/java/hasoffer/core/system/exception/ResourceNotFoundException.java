package hasoffer.core.system.exception;

import hasoffer.core.exception.BaseException;

/**
 * Date : 2016/3/11
 * Function :
 */
public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return String.format("ResourceNotFoundException{%s}", this.getMessage());
    }
}
