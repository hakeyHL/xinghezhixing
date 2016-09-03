package hasoffer.core.persistence.dbm.osql.exception;

public class OSqlException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OSqlException(String error) {
		super(error);
	}

	public OSqlException(Exception ex) {
		super(ex);
	}

}
