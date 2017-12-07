package spielmanager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class ExceptionManager extends ResponseEntityExceptionHandler {

    private Log logger =  LogFactory.getLog(ExceptionManager.class);

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorMessage> handleError(HttpServletRequest req, Exception e){

        logger.error("Request : " + req.getRequestURL() + " raised " + e);
        ErrorMessage exceptionResponse = new ErrorMessage(e.getMessage(), "Beschreibung des Fehlers");
        return new ResponseEntity<ErrorMessage>(exceptionResponse, new HttpHeaders(), HttpStatus.I_AM_A_TEAPOT);

    }

}
