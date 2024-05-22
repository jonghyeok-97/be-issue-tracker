package issuetracker.be.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("issue_assignee")
@Getter
public class AssigneeRef {
  @Id
  private String user_name;

  public AssigneeRef(String user_name) {
    this.user_name = user_name;
  }
}
