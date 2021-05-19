package ua.com.foxminded.exception;

@SuppressWarnings("serial")
public class ServiceException extends Exception {

    public ServiceException(String message, Throwable error) {
        super(message, error);
    }

    public ServiceException(Throwable error) {
        super(error);
    }

    public ServiceException(String message) {
        super(message);
    }
}
