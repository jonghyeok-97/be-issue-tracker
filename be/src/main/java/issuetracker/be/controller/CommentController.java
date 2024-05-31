package issuetracker.be.controller;

import issuetracker.be.dto.CommentSaveRequest;
import issuetracker.be.dto.CommentUpdateRequest;
import issuetracker.be.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

  private final CommentService commentService;

  @Autowired
  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/comment")
  public void commentSave(@RequestBody CommentSaveRequest commentSaveRequest) {
    commentService.saveComment(commentSaveRequest);
  }

  @DeleteMapping("/comment/{comment_id}")
  public void commentDelete(@PathVariable Long comment_id) {
    commentService.deleteComment(comment_id);
  }

  @PutMapping("/comment")
  public void commentUpdate(@RequestBody CommentUpdateRequest commentUpdateRequest) {
    commentService.update(commentUpdateRequest);
  }
}