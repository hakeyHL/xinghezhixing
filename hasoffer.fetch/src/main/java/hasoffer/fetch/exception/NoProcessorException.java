package hasoffer.fetch.exception;

import hasoffer.base.model.Website;

public class NoProcessorException extends Exception {

	private Website website;

	public NoProcessorException(Website website) {
		this.website = website;
	}

	@Override
	public String toString() {
		return String.format("Website[%s] Processor Not Found.", website.name());
	}

}
