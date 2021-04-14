package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.exception.gameimport;

public class InvalidImportLinkIdentifierException extends Exception {

    public InvalidImportLinkIdentifierException(String message) {
        super(message);
    }

    public InvalidImportLinkIdentifierException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidImportLinkIdentifierException(Throwable cause) {
        super(cause);
    }

    public InvalidImportLinkIdentifierException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
