package issuetracker.be.utils;

public enum OpenStatus {
  OPEN(1),
  CLOSE(0);

  private final int status;

  OpenStatus(int status) {
    this.status = status;
  }

  public int getStatus() {
    return status;
  }
}
