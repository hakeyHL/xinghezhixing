package hasoffer.fetch.exception;


import hasoffer.base.exception.ContentParseException;

public class ProductTitleNotFoundException extends ContentParseException {

	public ProductTitleNotFoundException(String url) {
		super(url);
	}

	@Override
	public String toString() {
		return "Product name not found for url: " + getUrl();
	}

}
