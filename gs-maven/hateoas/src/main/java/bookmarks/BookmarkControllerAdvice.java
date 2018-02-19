package bookmarks;


import org.springframework.hateoas.VndErrors;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class BookmarkControllerAdvice {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus
    VndErrors userNotFountExcptionHandle(UserNotFoundException ex) {
        return new VndErrors("error",ex.getMessage());
    }
}
