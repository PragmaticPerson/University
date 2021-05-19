package ua.com.foxminded.exception.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import ua.com.foxminded.exception.ServiceException;

@ControllerAdvice(basePackages = "ua.com.foxminded.servlet.controllers.web")
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(GlobalExceptionHandler.class);
    private static final String ERROR = "error";

    @ExceptionHandler({ IllegalArgumentException.class })
    public ModelAndView handlerIllegalArgumentException(IllegalArgumentException e) {
        LOGGER.error(e);
        ModelAndView mv = new ModelAndView();
        mv.addObject(ERROR, e.getMessage());
        mv.setViewName(ERROR);
        mv.setStatus(HttpStatus.NOT_FOUND);
        return mv;
    }

    @ExceptionHandler({ ServiceException.class })
    public ModelAndView handlerServerErrorException(ServiceException e) {
        LOGGER.error(e);
        ModelAndView mv = new ModelAndView();
        if (e.getCause() instanceof TransactionSystemException) {
            mv.addObject(ERROR, "Cannot connect to database");
            mv.setStatus(HttpStatus.SERVICE_UNAVAILABLE);
        } else {
            mv.addObject(ERROR, e.getMessage());
            mv.setStatus(HttpStatus.BAD_REQUEST);
        }
        mv.setViewName(ERROR);
        return mv;
    }

    @ExceptionHandler({ MissingServletRequestParameterException.class })
    private ModelAndView handlerMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        LOGGER.error(e);
        ModelAndView mv = new ModelAndView();
        mv.addObject(ERROR, "Not enough request params");
        mv.setStatus(HttpStatus.BAD_REQUEST);
        mv.setViewName(ERROR);
        return mv;
    }

    @ExceptionHandler({ Exception.class })
    private ModelAndView handlerException(Exception e) {
        LOGGER.error(e);
        ModelAndView mv = new ModelAndView();
        mv.addObject(ERROR, "Some error on server side");
        mv.setViewName(ERROR);
        mv.setStatus(HttpStatus.SERVICE_UNAVAILABLE);
        return mv;
    }
}
