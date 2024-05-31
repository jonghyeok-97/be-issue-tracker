package issuetracker.be.dto;

public record CommentUpdateRequest(
    Long comment_id,
    String contents
) {

}