package in.vumc.poc.recon.batch.exception;

@SuppressWarnings("serial")
public class ReconException extends RuntimeException {

  public ReconException() {
    super();
  }

  public ReconException(String message, Throwable cause) {
    super(message, cause);
  }

  public ReconException(String message) {
    super(message);
  }

  public ReconException(Throwable cause) {
    super(cause);
  }

}
