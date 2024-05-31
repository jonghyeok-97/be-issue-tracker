package issuetracker.be.exception;

import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(value = {MilestoneHasAssociatedIssuesException.class,
      NoSuchElementException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionResponse handleBusinessLogicException(Exception e) {
//    log.error("ERROR : {}", e.getStackTrace()[0]);
    for(StackTraceElement i : e.getStackTrace()) {
      System.out.println(i);
    }
    return new ExceptionResponse(e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ExceptionResponse handleGlobalException(Exception e) {
//    log.error("ERROR : {}", e.getStackTrace()[0]);
    for(StackTraceElement i : e.getStackTrace()) {
      System.out.println(i);
    }
    return new ExceptionResponse(e.getMessage() + " : 예상하지 못한 오류입니다. 다시 시도해 주세요.");
  }
}
