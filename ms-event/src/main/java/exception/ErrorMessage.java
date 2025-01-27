package exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Getter
@ToString
public class ErrorMessage {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private Instant timestamp;
    private int status;
    private String statusText;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;
    private String message;
    private String path;

    public ErrorMessage() {
    }

    public ErrorMessage(HttpServletRequest request, HttpStatus status,  String message) {
        this.timestamp = Instant.now();
        this.status = status.value();
        this.statusText  =  status.getReasonPhrase();
        this.message = message;
        this.path = request.getRequestURI();
    }

    public ErrorMessage(HttpServletRequest request, HttpStatus status, String message, BindingResult result) {
        this.timestamp = Instant.now();
        this.status = status.value();
        this.statusText  =  status.getReasonPhrase();
        this.message = message;
        this.path = request.getRequestURI();
        addErrors(result);
    }

    private void addErrors(BindingResult result) {
        this.errors = new HashMap<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            this.errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}
