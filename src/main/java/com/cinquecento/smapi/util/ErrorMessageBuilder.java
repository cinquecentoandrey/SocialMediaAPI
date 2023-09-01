package com.cinquecento.smapi.util;

import org.springframework.validation.BindingResult;

public class ErrorMessageBuilder {
    public static String message(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();

        bindingResult.getFieldErrors().forEach(e -> errorMessage.append(e.getField())
                .append(" - ")
                .append(e.getDefaultMessage())
                .append("; "));

        return errorMessage.toString();
    }
}
