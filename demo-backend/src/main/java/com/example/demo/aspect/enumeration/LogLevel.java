package com.example.demo.aspect.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.function.TriConsumer;
import org.slf4j.Logger;

@RequiredArgsConstructor
@Getter
public enum LogLevel {
    DEBUG((logger, message, params) -> logger.debug(message, params)),
    INFO((logger, message, params) -> logger.info(message, params));
    private final TriConsumer<Logger, String, Object[]> logMethod;

    public void log(Logger logger, String message, Object... params) {
        logMethod.accept(logger, message, params);
    }
}
