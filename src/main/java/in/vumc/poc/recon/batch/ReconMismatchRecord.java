package in.vumc.poc.recon.batch;

import java.sql.Blob;
import java.util.Date;

/**
 * The domain object to capture the missing records between Recon and 3rd party CMS systems i.e Documentum
 * 
 * @author bvamsikrishna
 *
 */
public class ReconMismatchRecord {

  private String reconId;
  private String registerNumber;
  private String transactionNumber;
  private Date creationDate;
  private String accountNumber;
  private String subsriberNumber;
  private String emailAddress;
  private String customerFirstName;
  private String customerLastName;
  private String sku;
  private long traceId;
  private Blob requestXml;
  private String applicationName;
  private String dealerCode;
  private String storeCity;
  private String storeState;
  private String refObjectId;
  private String tenderType;
  private String status;
  private Date jobRunDate;
  private int retryCount;
  private long executionDuration;

  public String getAccountNumber() {
    return accountNumber;
  }

  public String getApplicationName() {
    return applicationName;
  }

  public String getCustomerFirstName() {
    return customerFirstName;
  }

  public String getCustomerLastName() {
    return customerLastName;
  }

  public String getDealerCode() {
    return dealerCode;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public Date getJobRunDate() {
    return jobRunDate;
  }

  public String getRegisterNumber() {
    return registerNumber;
  }

  public Blob getRequestXml() {
    return requestXml;
  }

  public String getSku() {
    return sku;
  }

  public String getStatus() {
    return status;
  }

  public String getStoreCity() {
    return storeCity;
  }

  public String getStoreState() {
    return storeState;
  }

  public String getSubsriberNumber() {
    return subsriberNumber;
  }

  public String getTenderType() {
    return tenderType;
  }

  public long getTraceId() {
    return traceId;
  }

  public String getTransactionNumber() {
    return transactionNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  
  public Date getCreationDate() {
    return creationDate;
  }

  
  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public void setCustomerFirstName(String customerFirstName) {
    this.customerFirstName = customerFirstName;
  }

  public void setCustomerLastName(String customerLastName) {
    this.customerLastName = customerLastName;
  }

  public void setDealerCode(String dealerCode) {
    this.dealerCode = dealerCode;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public void setJobRunDate(Date jobRunDate) {
    this.jobRunDate = jobRunDate;
  }

  public void setRegisterNumber(String registerNumber) {
    this.registerNumber = registerNumber;
  }

  public void setRequestXml(Blob requestXml) {
    this.requestXml = requestXml;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setStoreCity(String storeCity) {
    this.storeCity = storeCity;
  }

  public void setStoreState(String storeState) {
    this.storeState = storeState;
  }

  public void setSubsriberNumber(String subsriberNumber) {
    this.subsriberNumber = subsriberNumber;
  }

  public void setTenderType(String tenderType) {
    this.tenderType = tenderType;
  }

  public void setTraceId(long traceId) {
    this.traceId = traceId;
  }

  public void setTransactionNumber(String transactionNumber) {
    this.transactionNumber = transactionNumber;
  }

  public String getReconId() {
    return reconId;
  }

  public void setReconId(String reconId) {
    this.reconId = reconId;
  }

  public int getRetryCount() {
    return retryCount;
  }

  public void setRetryCount(int retryCount) {
    this.retryCount = retryCount;
  }

  public String getRefObjectId() {
    return refObjectId;
  }

  public void setRefObjectId(String refObjectId) {
    this.refObjectId = refObjectId;
  }

  public long getExecutionDuration() {
    return executionDuration;
  }

  public void setExecutionDuration(long executionDuration) {
    this.executionDuration = executionDuration;
  }
}
