package com.example.interfell.family.ctrl;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityNotFoundException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ErrorCtrl {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public Map<String, Object> handleNotFoundException(WebRequest webRequest, EntityNotFoundException ex) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("error", NOT_FOUND.getReasonPhrase());
        msg.put("message", ex.getLocalizedMessage());
        msg.put("path", ((ServletWebRequest) webRequest).getRequest().getServletPath());
        msg.put("status", NOT_FOUND.value());
        msg.put("timestamp", Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_INSTANT));
        return msg;
    }
}
