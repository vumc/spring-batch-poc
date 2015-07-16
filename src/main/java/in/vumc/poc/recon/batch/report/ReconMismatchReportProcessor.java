package in.vumc.poc.recon.batch.report;

import in.vumc.poc.recon.batch.ReconMismatchRecord;

import org.springframework.batch.item.ItemProcessor;

/**
 * Report transformation - transform values from dao domain object to report object
 * 
 * @author bvamsikrishna
 *
 */
public class ReconMismatchReportProcessor implements ItemProcessor<ReconMismatchRecord, ReconMismatchReport> {

  @Override
  public ReconMismatchReport process(ReconMismatchRecord item) throws Exception {

    ReconMismatchReport report = new ReconMismatchReport();
    report.setBan(item.getAccountNumber());
    report.setCustomerName(item.getCustomerFirstName() + " " + item.getCustomerLastName());
    report.setEmail(item.getEmailAddress());
    report.setMsisdn(item.getSubsriberNumber());
    report.setRetryCount(item.getRetryCount());
    report.setSku(item.getSku());
    report.setStatus(item.getStatus());
    report.setStoreId(item.getReconId());
    report.setTenderType(item.getTenderType());
    report.setTraceId(item.getTraceId());
    report.setTransactionDate(item.getCreationDate());
    report.setTransactionId(item.getTransactionNumber());
    report.setExecutionDuration(item.getExecutionDuration());

    return report;

  }

}
