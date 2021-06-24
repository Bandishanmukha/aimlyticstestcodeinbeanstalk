package com.aimlytics.gateway.exceptions;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.aimlytics.gateway.dto.Response;

import static com.aimlytics.gateway.constants.AppConstants.GATEWAY_SERVER_ERR_5100;

@ControllerAdvice
@ResponseBody
public class ExceptionsHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	@Qualifier("messageSource")
	private MessageSource messageSource;
	
	@ExceptionHandler(DuplicateValueException.class)
	public ResponseEntity<Response<String>> handleDuplicateValueException(DuplicateValueException ex) {	
		return new ResponseEntity<>(Response.<String>builder().success(false)
				.message(ex.getMessage())
				.messageCode(ex.getMsgCode()).errorCode("208").build(), HttpStatus.ALREADY_REPORTED);
	}
	
	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<Response<String>> handleRecordNotFoundException(RecordNotFoundException ex) {	
		return new ResponseEntity<>(Response.<String>builder().success(false)
				.message(ex.getMessage())
				.messageCode(ex.getMsgCode()).errorCode("404").build(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public final ResponseEntity<Response<String>> handleConstraintViolation(ConstraintViolationException ex) {
		Map<String, String> codeMsg= ex.getConstraintViolations().parallelStream().map(e -> e.getMessage())
				.collect(Collectors.toMap(code -> code, code -> messageSource.getMessage(code, null, code, Locale.ENGLISH)));
		
		return new ResponseEntity<>(Response.<String>builder().success(false)
				.messages(codeMsg.values())
				.messageCodes(codeMsg.keySet())
				.errorCode("400").build(), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		Map<String, String> codeMsg = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.toMap(code -> code, code -> messageSource.getMessage(code, null, code, Locale.ENGLISH)));
		return new ResponseEntity<>(Response.<String>builder().success(false)
				.messages(codeMsg.values())
				.messageCodes(codeMsg.keySet())
				.build(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(PasswordException.class)
	protected ResponseEntity<Response<String>> handlePinException(PasswordException ex) {	
		return new ResponseEntity<>(Response.<String>builder().success(false)
				.message(ex.getMessage())
				.messageCode(ex.getMsgCode()).errorCode("400").build(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Response<String>> handleAnyOtherException(Exception ex) {	
		return new ResponseEntity<>(Response.<String>builder().success(false)
				.message(messageSource.getMessage(GATEWAY_SERVER_ERR_5100, null, Locale.ENGLISH))
				.messageCode(GATEWAY_SERVER_ERR_5100).errorCode("500").build(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
