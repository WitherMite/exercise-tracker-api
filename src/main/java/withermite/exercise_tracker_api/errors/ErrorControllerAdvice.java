package withermite.exercise_tracker_api.errors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        // todo: check exception for cause

        ProblemDetail problem = ProblemDetail.forStatus(409);

        // todo: add information to problem detail

        return ResponseEntity.status(409).body(problem);
    }
}
