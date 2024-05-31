package issuetracker.be.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

@Table("issue_label")
@Getter
@EqualsAndHashCode(of = "label_id")
public class LabelRef {
  private Long issue_id;
  private final Long label_id;

  public LabelRef(Long label_id) {
    this.label_id = label_id;
  }
}
