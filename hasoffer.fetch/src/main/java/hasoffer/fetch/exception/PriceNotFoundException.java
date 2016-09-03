package hasoffer.fetch.exception;

import hasoffer.base.exception.ContentParseException;

public class PriceNotFoundException extends ContentParseException {
	public PriceNotFoundException(String url) {
		super(url);
	}

	@Override
	public String toString() {
		return "Price not found for url: " + getUrl();
	}
}
