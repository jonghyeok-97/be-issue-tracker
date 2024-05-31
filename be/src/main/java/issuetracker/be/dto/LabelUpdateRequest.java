package issuetracker.be.dto;

import issuetracker.be.domain.Label;

public record LabelUpdateRequest(
    Long id,
    String name,
    String background_color,
    String text_color,
    String description
) {

  public Label toEntity() {
    return Label.builder().id(id)
        .name(name)
        .background_color(background_color)
        .text_color(text_color)
        .description(description)
        .build();
  }
}
