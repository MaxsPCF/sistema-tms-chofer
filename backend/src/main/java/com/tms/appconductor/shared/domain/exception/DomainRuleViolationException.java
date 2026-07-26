package com.tms.appconductor.shared.domain.exception;

/**
 * 
 * Excepción específica para violaciones de reglas de dominio.
 * 
 * Más específica que BusinessException para casos de invariantes del agregado.
 * 
 */
public class DomainRuleViolationException extends BusinessException {
    private final String ruleName;

    public DomainRuleViolationException(String ruleName, String message) {
        super(message, "DOMAIN_RULE_VIOLATION");
        this.ruleName = ruleName;
    }

    public String getRuleName() {
        return ruleName;
    }

    @Override
    public String getErrorDetail() {
        return String.format("Regla de dominio violada [%s]: %s",
                ruleName, getMessage());
    }

    public static DomainRuleViolationException of(String ruleName, String message) {
        return new DomainRuleViolationException(ruleName, message);
    }
}