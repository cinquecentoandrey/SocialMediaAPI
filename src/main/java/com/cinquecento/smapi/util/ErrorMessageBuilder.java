package com.cinquecento.smapi.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class ErrorMessageBuilder {

    public ErrorMessageBuilder() {}

    public String message(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();

        bindingResult.getFieldErrors().forEach(e -> errorMessage.append(e.getField())
                .append(" - ")
                .append(e.getDefaultMessage())
                .append("; "));

        return errorMessage.toString();
    }
}
