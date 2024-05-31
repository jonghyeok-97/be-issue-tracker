package issuetracker.be.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

@Table("issue_assignee")
@Getter
@EqualsAndHashCode(of = "user_name")
public class AssigneeRef {
  private Long issue_id;
  private final String user_name;

  public AssigneeRef(String user_name) {
    this.user_name = user_name;
  }
}
