package hasoffer.core.product.exception;

import hasoffer.core.exception.BaseException;

/**
 * Date : 2016/2/16
 * Function :
 */
public class CategoryDeleteException extends BaseException {
    public CategoryDeleteException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return String.format("CategoryDeleteException{%s}", this.getMessage());
    }

}
