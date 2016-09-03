package hasoffer.fetch.exception.amazon;

import hasoffer.base.exception.ContentParseException;

public class AmazonRobotCheckException extends ContentParseException {

	public AmazonRobotCheckException(String url) {
		super(url);
	}


	@Override
	public String toString() {
		return "amazon robot check " + getUrl();
	}

}
