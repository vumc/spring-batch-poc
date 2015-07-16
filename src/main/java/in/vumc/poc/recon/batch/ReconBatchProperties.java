package in.vumc.poc.recon.batch;


/**
 * Capture the runtime properties required for processing the batch processing.
 * 
 * @author bvamsikrishna
 *
 */
public class ReconBatchProperties {

  // @Value("${batch.job.retry.attempt.max}")
  private long maxRetryCount;

  // @Value("${batch.job.tx.throttle.max}")
  private long maxThrottle;

  // @Value("${batch.job.cron.schedule})
  private String reconCronSchedule;

  // @Value("${middleware.service.url})
  private String middlewareServiceUrl;

  public long getMaxRetryCount() {
    return maxRetryCount;
  }

  public long getMaxThrottle() {
    return maxThrottle;
  }

  public void setMaxRetryCount(long maxRetryCount) {
    this.maxRetryCount = maxRetryCount;
  }

  public void setMaxThrottle(long maxThrottle) {
    this.maxThrottle = maxThrottle;
  }

  public String getReconCronSchedule() {
    return reconCronSchedule;
  }

  public void setReconCronSchedule(String reconCronSchedule) {
    this.reconCronSchedule = reconCronSchedule;
  }

  public String getMiddlewareServiceUrl() {
    return middlewareServiceUrl;
  }

  public void setMiddlewareServiceUrl(String middlewareServiceUrl) {
    this.middlewareServiceUrl = middlewareServiceUrl;
  }
}
