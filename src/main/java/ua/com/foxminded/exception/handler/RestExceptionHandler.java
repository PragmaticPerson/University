package ua.com.foxminded.exception.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import ua.com.foxminded.exception.ServiceException;

@RestControllerAdvice(basePackages = "ua.com.foxminded.servlet.controllers.rest")
@RequestMapping(produces = "application/json")
public class RestExceptionHandler extends DefaultHandlerExceptionResolver {

    private static final Logger LOGGER = LogManager.getLogger(RestExceptionHandler.class);

    @ExceptionHandler({ IllegalArgumentException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlerIllegalArgumentException(IllegalArgumentException e) {
        String error = e.getMessage();
        LOGGER.info("handlerIllegalArgumentException: {}", error);
        return error;
    }

    @ExceptionHandler({ ServiceException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handlerServerErrorException(ServiceException e) {
        String error = e.getMessage();
        LOGGER.error("handlerServerErrorException: {}", error);
        return error;
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String error = e.getAllErrors().get(0).getDefaultMessage();
        LOGGER.warn("handlerMethodArgumentNotValidException: {}", error);
        return error;
    }

    @ExceptionHandler({ Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handlerException(Exception e) {
        String error = e.getMessage();
        LOGGER.error("handlerException: {}", error);
        return error;
    }
}
