package issuetracker.be.domain;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
public class Milestone {

  @Id
  private Long id;
  private String name;
  private String description;
  private LocalDate end_date;
  private boolean is_open = true;

  public Milestone(String name, String description, LocalDate end_date) {
    this.name = name;
    this.description = description;
    this.end_date = end_date;
  }

  public Milestone(Long id, String name, String description, LocalDate end_date) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.end_date = end_date;
  }
}
