package hasoffer.core.product.exception;

/**
 * Date : 2015/12/26
 * Function :
 */
public class ProductNotFoundException extends Exception {

	String message;

	public ProductNotFoundException() {
	}

	public ProductNotFoundException(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return String.format("ProductNotFoundException{%s}", message);
	}
}
