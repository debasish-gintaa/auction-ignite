package com.asconsoft.gintaa.exception.handler;

import com.asconsoft.gintaa.common.Constants;
import com.asconsoft.gintaa.common.exception.*;
import com.asconsoft.gintaa.common.payload.ApiResponse;
import com.asconsoft.gintaa.common.rest.validation.InvalidParam;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.collections.impl.utility.internal.IterableIterate;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@ControllerAdvice
@RestController
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Value("${auth.failure-redirect-url:#{null}}")
    private String gintaaAuthFailureRedirectUri;

    @ExceptionHandler(GintaaException.class)
    public final ResponseEntity<Object> handleCustomException(GintaaException ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(ex.getCode(), ex.getMessage()), HttpStatus.resolve(ex.getCode()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.UNAUTHORIZED.value(), "Gintaa authentication exception ocurred!"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAccountLockedException.class)
    public final ResponseEntity<Object> handleAccountLockedException(UserAccountLockedException ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAccountNotEnabledException.class)
    public final ResponseEntity<Object> handleUserAccountNotEnabledException(UserAccountNotEnabledException ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure("Gintaa internal server occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public final ResponseEntity<Object> handleInvalidArgumentException(InvalidArgumentException ex, WebRequest request) {
        LOGGER.error("exception details: {}", ex);
        int code = ex != null ? ex.getCode() : HttpStatus.INTERNAL_SERVER_ERROR.value();
        return new ResponseEntity<Object>(ApiResponse.fromException(ex), HttpStatus.resolve(code));
    }

    @ExceptionHandler(InvalidDefinitionException.class)
    public final ResponseEntity<Object> handleInvalidDefinitionException(InvalidDefinitionException ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstrainViolationException(ConstraintViolationException ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        List<InvalidParam> e1 = IterableIterate.collect(ex.getConstraintViolations(),
                f -> InvalidParam.builder().param(Objects.isNull(f.getPropertyPath()) ? "default" : ((PathImpl) f.getPropertyPath()).getLeafNode().getName()).reason(f.getMessage()).build());
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.BAD_REQUEST.value(), e1, Constants.VALIDATION_ERR_MESSAGE), HttpStatus.BAD_REQUEST);
    }

    @Override
    public final ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<Object> handleMethodArgumentTypeMismatchException(Exception ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @Override
    public final ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("exception details: {}", ex);
        List<InvalidParam> e1 = IterableIterate.collect(ex.getBindingResult().getFieldErrors(),
                f -> InvalidParam.builder().param(f.getField()).reason(f.getDefaultMessage()).build());

        List<InvalidParam> e2 = IterableIterate.collect(ex.getBindingResult().getGlobalErrors(),
                f -> InvalidParam.builder().param(f.getObjectName()).reason(f.getDefaultMessage()).build());

        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.BAD_REQUEST.value(), ListUtils.union(e1, e2), Constants.VALIDATION_ERR_MESSAGE), HttpStatus.BAD_REQUEST);
    }

    @Override
    public final ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public final ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.BAD_REQUEST.value(), Constants.INVALID_JSON_FORMAT), HttpStatus.BAD_REQUEST);
    }

    @Override
    public final ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public final ResponseEntity<Object> handleMissingServletRequestPart(
            MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Customize the response for HttpRequestMethodNotSupportedException.
     * <p>This method logs a warning, sets the "Allow" header, and delegates to
     * {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("exception details: {}", ex);
        pageNotFoundLogger.warn(ex.getMessage());

        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        if (!CollectionUtils.isEmpty(supportedMethods)) {
            headers.setAllow(supportedMethods);
        }
        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for HttpMediaTypeNotSupportedException.
     * <p>This method sets the "Accept" header and delegates to
     * {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("exception details: {}", ex);
        List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            headers.setAccept(mediaTypes);
        }

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for HttpMediaTypeNotAcceptableException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("exception details: {}", ex);
        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for MissingPathVariableException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     * @since 4.2
     */
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("exception details: {}", ex);
        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for ServletRequestBindingException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleServletRequestBindingException(
            ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("exception details: {}", ex);
        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for ConversionNotSupportedException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleConversionNotSupported(
            ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("exception details: {}", ex);
        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for HttpMessageNotWritableException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
            HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("exception details: {}", ex);
        return handleExceptionInternal(ex, null, headers, status, request);
    }


    /**
     * Customize the response for NoHandlerFoundException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     * @since 4.0
     */
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("exception details: {}", ex);
        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * Customize the response for AsyncRequestTimeoutException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex         the exception
     * @param headers    the headers to be written to the response
     * @param status     the selected response status
     * @param webRequest the current request
     * @return a {@code ResponseEntity} instance
     * @since 4.2.8
     */
    @Nullable
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
            AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        LOGGER.error("exception details: {}", ex);
        if (webRequest instanceof ServletWebRequest) {
            ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest;
            HttpServletResponse response = servletWebRequest.getResponse();
            if (response != null && response.isCommitted()) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Async request timed out");
                }
                return null;
            }
        }

        return handleExceptionInternal(ex, null, headers, status, webRequest);
    }

    /**
     * A single place to customize the response body of all exception types.
     * <p>The default implementation sets the {@link WebUtils#ERROR_EXCEPTION_ATTRIBUTE}
     * request attribute and creates a {@link ResponseEntity} from the given
     * body, headers, and status.
     *
     * @param ex      the exception
     * @param body    the body for the response
     * @param headers the headers for the response
     * @param status  the response status
     * @param request the current request
     */
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOGGER.error("exception details: {}", ex);
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        return new ResponseEntity<Object>(ApiResponse.ofFailure(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * A single place to customize the response body in case system throws {@link InvalidMediaTypeException}.
     *
     * @param ex      the exception
     * @param request
     * @return {@link ResponseEntity}
     * @throws Exception
     */
    @ExceptionHandler(InvalidMediaTypeException.class)
    public final ResponseEntity<Object> handleInvalidMediaTypeException(InvalidMediaTypeException ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), ex.getMessage()), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public final ResponseEntity<?> handleAuthenticationServiceException(AuthenticationServiceException ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        redirectOnUnauthorizedAccess(request);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.UNAUTHORIZED.value(), "Unauthorized Access"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public final ResponseEntity<?> handleInvalidOperationException(InvalidOperationException ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(ex.getCode(), ex.getMessage()), HttpStatus.resolve(ex.getCode()));
    }

    private void redirectOnUnauthorizedAccess(WebRequest request) throws IOException {
        if (!StringUtils.isBlank(gintaaAuthFailureRedirectUri) && (request instanceof ServletWebRequest)) {
            ServletWebRequest servletWebRequest = (ServletWebRequest) request;
            HttpServletResponse response = servletWebRequest.getResponse();
            if (response != null) {
                logger.debug("Redirecting to " + gintaaAuthFailureRedirectUri);
                response.sendRedirect(gintaaAuthFailureRedirectUri);
            }
        }
    }

    @ExceptionHandler(FileNotFoundException.class)
    public final ResponseEntity<?> handleFileNotFoundException(FileNotFoundException ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileNotUploadedException.class)
    public final ResponseEntity<?> handleFileNotUploadedException(FileNotUploadedException ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public final ResponseEntity<?> handleDataNotFoundException(DataNotFoundException ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDateTimeFormatException.class)
    public final ResponseEntity<?> handleInvalidDateTimeFormatException(InvalidDateTimeFormatException ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTimeFormatException.class)
    public final ResponseEntity<?> handleInvalidTimeFormatException(InvalidTimeFormatException ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateFormatException.class)
    public final ResponseEntity<?> handleInvalidDateFormatException(InvalidDateFormatException ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidSortException.class)
    public final ResponseEntity<?> handleInvalidSortParamsException(InvalidSortException ex, WebRequest request) throws Exception {
        LOGGER.error("exception details: {}", ex);
        return new ResponseEntity<Object>(ApiResponse.ofFailure(HttpStatus.BAD_REQUEST.value(), ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }


}
