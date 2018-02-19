package bookmarks;

public class UserNotFoundException extends  RuntimeException{

    public UserNotFoundException(String userId) {
        super("count not find user '" + userId + "'.");
    }
}
