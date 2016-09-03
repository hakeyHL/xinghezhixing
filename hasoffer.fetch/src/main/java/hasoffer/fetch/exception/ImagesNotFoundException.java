package hasoffer.fetch.exception;

import hasoffer.base.exception.ContentParseException;

public class ImagesNotFoundException extends ContentParseException {

	public ImagesNotFoundException(String url) {
		super(url);
	}


	@Override
	public String toString() {
		return "Images not found for url: " + getUrl();
	}

}
