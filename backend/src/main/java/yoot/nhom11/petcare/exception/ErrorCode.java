package yoot.nhom11.petcare.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    PET_NOT_FOUND("PET_NOT_FOUND", HttpStatus.NOT_FOUND, "Pet not found"),
    MEDICAL_RECORD_NOT_FOUND("MEDICAL_RECORD_NOT_FOUND", HttpStatus.NOT_FOUND, "Medical record not found"),
    SORT_FIELD_INVALID("SORT_FIELD_INVALID", HttpStatus.BAD_REQUEST, "Sort field is invalid"),
    VALIDATION_FAILED("VALIDATION_FAILED", HttpStatus.BAD_REQUEST, "Validation failed"),
    INTERNAL_ERROR("INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred");

    private final String code;
    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(String code, HttpStatus status, String defaultMessage) {
        this.code = code;
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
