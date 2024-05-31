package issuetracker.be.dto;

import issuetracker.be.domain.Label;

public record LabelSaveRequest(
    String name,
    String background_color,
    String text_color,
    String description
) {

  public Label toEntity() {
    return Label.builder().name(name)
        .background_color(background_color)
        .text_color(text_color)
        .description(description)
        .build();
  }
}
