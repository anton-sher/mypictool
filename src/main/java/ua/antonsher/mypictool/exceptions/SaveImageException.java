package ua.antonsher.mypictool.exceptions;

/**
 * Thrown if there is something wrong with saving a file.
 */
@SuppressWarnings("serial")
public class SaveImageException extends RuntimeException {
    public SaveImageException(String message) {
        super(message);
    }

    public SaveImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
