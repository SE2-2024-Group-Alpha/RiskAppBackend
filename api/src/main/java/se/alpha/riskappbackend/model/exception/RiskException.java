package se.alpha.riskappbackend.model.exception;

import lombok.Getter;

@Getter
public class RiskException extends Exception {
    private final String type;
    private final String message;

    public RiskException(String type, String message)
    {
        this.message = message;
        this.type = type;
    }
}
