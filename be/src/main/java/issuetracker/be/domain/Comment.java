package issuetracker.be.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
public class Comment {

  @Id
  private Long id;
  private Long issue_id;
  private String reporter;
  private LocalDateTime created_at;
  private String contents;

  public Comment(Long issue_id, String reporter, LocalDateTime createdAt, String contents) {
    this.issue_id = issue_id;
    this.reporter = reporter;
    this.created_at = createdAt;
    this.contents = contents;
  }
}