package rodent.rodentmobile.exceptions;

/**
 * Created by Atte on 15/11/15.
 */
public class InvalidGCodeException extends Exception {

    public InvalidGCodeException () {
        super();
    }

    public InvalidGCodeException (String message) {
        super(message);
    }
}
