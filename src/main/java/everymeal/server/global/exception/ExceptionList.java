package everymeal.server.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionList {
    MEAL_NOT_FOUND("M0001",HttpStatus.NOT_FOUND,"Meal Not Found"),
    INVALID_REQUEST("R0001", HttpStatus.BAD_REQUEST, "Request의 Data Type이 올바르지 않습니다."),
    ;

    public final String CODE;
    public final HttpStatus httpStatus;
    public final String MESSAGE;

}
