package in.vumc.poc.recon.batch.report;

import java.util.Date;

/**
 * The properties captured for report generation
 * 
 * @author bvamsikrishna
 *
 */
@SuppressWarnings("serial")
public class ReconMismatchReport implements java.io.Serializable {

  private long traceId;
  private String status;
  private String storeId;
  private String transactionId;
  private String ban;
  private String msisdn;
  private String email;
  private String customerName;
  private Date transactionDate;
  private String sku;
  private String tenderType;
  private int retryCount;
  private long executionDuration;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getRetryCount() {
    return retryCount;
  }

  public void setRetryCount(int retryCount) {
    this.retryCount = retryCount;
  }

  public String getBan() {
    return ban;
  }

  public void setBan(String ban) {
    this.ban = ban;
  }

  public String getMsisdn() {
    return msisdn;
  }

  public void setMsisdn(String msisdn) {
    this.msisdn = msisdn;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public String getTenderType() {
    return tenderType;
  }

  public void setTenderType(String tenderType) {
    this.tenderType = tenderType;
  }

  public long getTraceId() {
    return traceId;
  }

  public void setTraceId(long traceId) {
    this.traceId = traceId;
  }

  public Date getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(Date transactionDate) {
    this.transactionDate = transactionDate;
  }

  public long getExecutionDuration() {
    return executionDuration;
  }

  public void setExecutionDuration(long executionDuration) {
    this.executionDuration = executionDuration;
  }

}
