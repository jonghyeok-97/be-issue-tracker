package issuetracker.be.domain;

import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

@Setter
public class User implements Persistable<String> {

  @Id
  private String name;
  private String image_path;
  @Transient
  private boolean isNew = true;

  @Override
  public String getId() {
    return name;
  }

  public String getImage_path() {
    return image_path;
  }

  @Override
  public boolean isNew() {
    return isNew;
  }
}


