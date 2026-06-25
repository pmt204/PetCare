package yoot.nhom11.petcare.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private final String code;
    private final String message;
    private final int status;
    private final String path;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private final LocalDateTime timestamp;

    private final Object details;
}
