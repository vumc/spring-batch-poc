package in.vumc.poc.recon.batch;

import in.vumc.poc.recon.batch.middleware.ReconMiddlewareService;

import java.sql.Blob;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;

/**
 * This is the main processor to reconcile the mismatch records by calling the {@link ReconMiddlewareService}
 * 
 * @author bvamsikrishna
 *
 */
public class ReconMismatchRecordProcessor implements ItemProcessor<ReconMismatchRecord, ReconMismatchRecord> {

  private static Logger LOG = Logger.getLogger(ReconMismatchRecordProcessor.class);
  private int maxRetryAttemptCount;
  private ReconMiddlewareService reconMiddlewareService;
  private String reconServiceUrl;

  @Override
  public ReconMismatchRecord process(final ReconMismatchRecord record) throws Exception {
    return doProcessReconReconcile(record);
  }

  private ReconMismatchRecord doProcessReconReconcile(final ReconMismatchRecord record) throws SQLException {
    long processStartTime = System.currentTimeMillis();

    if (skipProcessing(record)) {
      return null;
    }
    // increase retry attempt counter
    record.setRetryCount(record.getRetryCount() + 1);
    // convert blob (request_xml) to byte stream
    Blob blob = record.getRequestXml();
    byte[] requestXmlByteData = blob.getBytes(1, (int) blob.length());
    // convert byte steam to string
    String requestXml = new String(requestXmlByteData);

    if (LOG.isDebugEnabled()) {
      LOG.debug("process :: requestXml: " + requestXml);
    }

    try {
      byte[] responseBytes = reconMiddlewareService.post(reconServiceUrl, requestXml, false);
      String responseXml = new String(responseBytes);
      if (LOG.isDebugEnabled())
        LOG.debug("process :: responseXml: " + responseXml);
      // FIXME: need to set based on MW response
      if (responseXml != null && responseXml.contains("<extStatusCode>100</extStatusCode>")) {
        record.setStatus(ReconStatus.COMPLETED.name());
      } else {
        record.setStatus(ReconStatus.FAILED.name());
      }
    } catch (Exception e) {
      // ignore exception - logged @ reconMiddlewareService
      record.setStatus(ReconStatus.FAILED.name());
    }
    // yellapasMWcall(record, requestXmlByteData);
    // calculate process duration
    long elapsedTime = (System.currentTimeMillis() - processStartTime); // in milliseconds
    record.setExecutionDuration(elapsedTime);

    if (LOG.isDebugEnabled()) {
      LOG.debug("process ::  traceId: " + record.getTraceId()
                + " status: "
                + record.getStatus()
                + " :: retryCount: "
                + record.getRetryCount()
                + " :: duration: "
                + elapsedTime);
    }
    return record;
  }

  /**
   * skip processing if 1. max retry attempts exceeded or 2. blob is null
   * @param row
   * @return true if above condition is met
   */
  private boolean skipProcessing(final ReconMismatchRecord row) {
    boolean skipProcessing = false;
    LOG.debug("skipProcessing :: retryCount: " + row.getRetryCount());
    LOG.debug("skipProcessing :: requestXml: " + row.getRequestXml());
    if (row.getRetryCount() >= maxRetryAttemptCount || ReconStatus.COMPLETED.name().equals(row.getStatus())
        || row.getRequestXml() == null) {
      skipProcessing = true;
    }
    LOG.debug("skipProcessing :: skipProcessing: " + skipProcessing);
    return skipProcessing;
  }

  public void setMaxRetryAttemptCount(int maxRetryAttemptCount) {
    this.maxRetryAttemptCount = maxRetryAttemptCount;
  }

  public void setReconMiddlewareService(ReconMiddlewareService reconMiddlewareService) {
    this.reconMiddlewareService = reconMiddlewareService;
  }

  public void setReconServiceUrl(String reconServiceUrl) {
    this.reconServiceUrl = reconServiceUrl;
  }
}
