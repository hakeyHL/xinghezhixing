package hasoffer.core.search.exception;

import hasoffer.core.exception.BaseException;
import hasoffer.core.exception.ERROR_CODE;

/**
 *
 */
public class NonMatchedProductException extends BaseException {
    private String q;
    private String title;
    private float mc;

    public NonMatchedProductException(ERROR_CODE errorCode) {
        super(errorCode);
    }

    public NonMatchedProductException(ERROR_CODE errorCode, String q, String title, float mc) {
        super(errorCode);
        this.q = q;
        this.title = title;
        this.mc = mc;
    }

    @Override
    public String toString() {
        return "NonMatchedProductException{" +
                "q='" + q + '\'' +
                ", title='" + title + '\'' +
                ", mc=" + mc +
                '}';
    }
}
