package com.paradise.exceptions;


import java.time.LocalDateTime;

public record ErrorMessageResponse (
        String message,
        String detailMessage,
        LocalDateTime dateTime
){


}
