package withermite.exercise_tracker_api.errors;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import tools.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Failed to read request");

        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException invalidFormat) {
            problem.setDetail("Json body invalid");
            String message = "'"
                    + invalidFormat.getValue().toString()
                    + "' is not a valid "
                    + invalidFormat.getTargetType().getSimpleName();
            problem.setProperty("errors", new String[] { message });
        }

        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Request contains invalid fields");

        var violations = e.getAllErrors();
        List<String> errors = new ArrayList<>();

        violations.forEach(violation -> {
            errors.add(violation.getDefaultMessage());
        });

        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage());
        return problem;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException e) {
        if (e instanceof DuplicateKeyException) {
            ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

            // todo: add information to problem detail

            return problem;
        }

        return handleUncaught(e);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUncaught(Exception e) {
        System.err.println(e);
        // System.err.println(e.getCause());
        // e.printStackTrace();
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return problem;
    }
}
