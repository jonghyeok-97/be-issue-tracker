package issuetracker.be.service;

import issuetracker.be.domain.Comment;
import issuetracker.be.dto.CommentResponse;
import issuetracker.be.dto.CommentSaveRequest;
import issuetracker.be.dto.CommentUpdateRequest;
import issuetracker.be.repository.CommentRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CommentService {
  private CommentRepository commentRepository;
  private UserService userService;

  @Autowired
  public CommentService(CommentRepository commentRepository, UserService userService) {
    this.commentRepository = commentRepository;
    this.userService = userService;
  }

  @Transactional
  public void saveComment(CommentSaveRequest commentSaveRequest) {
    Comment comment = commentSaveRequest.toEntity(LocalDateTime.now());
    Comment saveComment = commentRepository.save(comment);

    log.debug("저장된 코멘트 : {}", saveComment);
  }


  @Transactional
  public void deleteComment(Long comment_id) {
    commentRepository.deleteById(comment_id);
  }

  @Transactional
  public void update(CommentUpdateRequest commentUpdateRequest) {
    Long commentId = commentUpdateRequest.comment_id();
    String contents = commentUpdateRequest.contents();
    Optional<Comment> optionalComment = commentRepository.findById(commentId);

    if (optionalComment.isPresent()) {
      Comment comment = optionalComment.get();
      comment.setContents(contents);
      comment.setCreated_at(LocalDateTime.now());
      commentRepository.save(comment);
    } else {
      throw new NoSuchElementException("존재하지 않는 코멘트입니다.");
    }
  }

  public List<CommentResponse> getCommentResponse(Long issueId) {
    List<Comment> comments = commentRepository.findByIssueId(issueId);
    return comments.stream()
        .map(comment -> new CommentResponse(comment,
            userService.getUser(comment.getReporter())))
        .collect(Collectors.toList());
  }

  public void saveComment(Long issueId, String reporter, LocalDateTime createdAt, String comment) {
    Comment saveComment = commentRepository.save(new Comment(issueId, reporter, createdAt, comment));
    log.debug("저장된 코멘트 : {}", saveComment);
  }

  public List<Comment> getComments(String reporter) {
    return commentRepository.findByReporter(reporter);
  }

  @Transactional
  public void deleteIssue(Long issueId) {
    commentRepository.deleteIssue(issueId);
  }
}