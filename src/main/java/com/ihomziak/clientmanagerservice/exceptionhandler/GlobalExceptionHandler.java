package com.ihomziak.clientmanagerservice.exceptionhandler;

import com.ihomziak.clientmanagerservice.exception.*;
import com.ihomziak.transactioncommon.dto.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Provides handling for exceptions throughout this service.
     *
     * @param ex      The target exception
     * @param request The current request
     */
    @ExceptionHandler({
            ClientNotFoundException.class,
            AccountNotFoundException.class,
            AccountAlreadyExistException.class,
            ClientAlreadyExistException.class,
            AccountNumberQuantityException.class
    })
    @Nullable
    public final ResponseEntity<ErrorDTO> handleException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();

        LOGGER.error("Handling {} due to {}", ex.getClass().getSimpleName(), ex.getMessage());

        if (ex instanceof ClientNotFoundException clientNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            return handleException(clientNotFoundException, headers, status, request);

        } else if (ex instanceof AccountNotFoundException accountNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            return handleException(accountNotFoundException, headers, status, request);

        } else if (ex instanceof AccountAlreadyExistException accountException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleException(accountException, headers, status, request);

        } else if (ex instanceof ClientAlreadyExistException accountException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleException(accountException, headers, status, request);

        } else if (ex instanceof AccountNumberQuantityException quantityException) {
            HttpStatus status = HttpStatus.FORBIDDEN; // 403 Forbidden
            return handleException(quantityException, headers, status, request);

        } else if (ex instanceof NonSufficientFundsException balanceException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleException(balanceException, headers, status, request);

        } else {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Unknown exception type: {}", ex.getClass().getName());
            }

            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            // Ensure ErrorDTO is not null
            ErrorDTO errorDTO = new ErrorDTO(ex.getMessage());
            return handleExceptionInternal(ex, errorDTO, headers, status, request);
        }
    }


    /**
     * Customize the response for ClientNotFoundException.
     *
     * @param ex      The exception
     * @param headers The headers to be written to the response
     * @param status  The selected response status
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<ErrorDTO> handleException(Exception ex,
                                                       HttpHeaders headers,
                                                       HttpStatus status,
                                                       WebRequest request) {
        return handleExceptionInternal(ex, new ErrorDTO(ex.getMessage()), headers, status, request);
    }

    /**
     * A single place to customize the response errorMessage of all Exception types.
     *
     * <p>The default implementation sets the {@link WebUtils#ERROR_EXCEPTION_ATTRIBUTE}
     * request attribute and creates a {@link ResponseEntity} from the given
     * errorMessage, headers, and status.
     *
     * @param ex           The exception
     * @param errorMessage The errorMessage for the response
     * @param headers      The headers for the response
     * @param status       The response status
     * @param request      The current request
     */
    protected ResponseEntity<ErrorDTO> handleExceptionInternal(Exception ex,
                                                               @Nullable ErrorDTO errorMessage,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        System.out.println(new ResponseEntity<>(errorMessage, headers, status));
        return new ResponseEntity<>(errorMessage, headers, status);
    }
}
