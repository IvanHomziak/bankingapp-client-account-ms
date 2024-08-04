package com.ihomziak.webbankingapp.errors;

import com.ihomziak.webbankingapp.dto.ErrorDTO;
import com.ihomziak.webbankingapp.exception.AccountAlreadyExistException;
import com.ihomziak.webbankingapp.exception.AccountNotFoundException;
import com.ihomziak.webbankingapp.exception.ClientAlreadyExistException;
import com.ihomziak.webbankingapp.exception.ClientNotFoundException;
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
    })
    @Nullable
    public final ResponseEntity<ErrorDTO> handleException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();

        LOGGER.error("Handling {} due to {}", ex.getClass().getSimpleName(), ex.getMessage());

        if (ex instanceof ClientNotFoundException clientNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;

            return handleNotFoundException(clientNotFoundException, headers, status, request);
        } else if (ex instanceof AccountNotFoundException accountNotFoundException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;

            return handleNotFoundException(accountNotFoundException, headers, status, request);
        } else if (ex instanceof AccountAlreadyExistException accountException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;

            return handleNotFoundException(accountException, headers, status, request);
        } else if (ex instanceof ClientAlreadyExistException accountException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;

            return handleNotFoundException(accountException, headers, status, request);
        } else {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Unknown exception type: {}", ex.getClass().getName());
            }

            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal(ex, null, headers, status, request);
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
    protected ResponseEntity<ErrorDTO> handleNotFoundException(Exception ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
//        List<String> errors = Collections.singletonList(ex.getMessage());
        return handleExceptionInternal(ex, new ErrorDTO(ex.getMessage()), headers, status, request);
    }

    /**
     * Customize the response for AccountNotFoundException.
     *
     * @param ex The exception
     * @param headers The headers to be written to the response
     * @param status The selected response status
     * @return a {@code ResponseEntity} instance
     */
//    protected ResponseEntity<ApiError> handleContentNotAllowedException(AccountNotFoundException ex,
//                                                                        HttpHeaders headers,
//                                                                        HttpStatus status,
//                                                                        WebRequest request) {
//        List<String> errorMessages = ex.getErrors()
//                .stream()
//                .map(contentError -> contentError.getObjectName() + " " + contentError.getDefaultMessage())
//                .collect(Collectors.toList());
//
//        return handleExceptionInternal(ex, new ApiError(errorMessages), headers, status, request);
//    }


    /**
     * A single place to customize the response errorMessage of all Exception types.
     *
     * <p>The default implementation sets the {@link WebUtils#ERROR_EXCEPTION_ATTRIBUTE}
     * request attribute and creates a {@link ResponseEntity} from the given
     * errorMessage, headers, and status.
     *
     * @param ex      The exception
     * @param errorMessage    The errorMessage for the response
     * @param headers The headers for the response
     * @param status  The response status
     * @param request The current request
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
