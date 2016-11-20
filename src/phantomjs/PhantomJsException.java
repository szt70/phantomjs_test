package phantomjs;

/**
 * PhantomJS関連エラークラス
 *
 */
public class PhantomJsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PhantomJsException (Throwable t) {
		super(t);
	}

	public PhantomJsException (Throwable t, String message) {
		super(message, t);
	}

	public PhantomJsException (String message) {
		super(message);
	}
}
