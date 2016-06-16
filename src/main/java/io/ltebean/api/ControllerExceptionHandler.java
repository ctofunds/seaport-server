package io.ltebean.api;


import io.ltebean.api.dto.Response;
import io.ltebean.api.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by leo on 16/6/7.
 */
@ControllerAdvice(basePackages = "io.ltebean.api")
public class ControllerExceptionHandler {

    Logger logger = LoggerFactory.getLogger("ExceptionHandler");

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public Response handleUnauthorized(UnauthorizedException ex) {
        logger.error("unauthorized", ex);
        Response response = new Response();
        response.code = HttpStatus.UNAUTHORIZED.value();
        response.message = "unauthorized";
        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handleError(Exception ex) {
        logger.error("server error", ex);
        return Response.error("server error");
    }
}
