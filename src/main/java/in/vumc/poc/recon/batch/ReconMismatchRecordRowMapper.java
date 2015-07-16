package in.vumc.poc.recon.batch;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * 
 * @author bvamsikrishna
 *
 */
public class ReconMismatchRecordRowMapper implements RowMapper<ReconMismatchRecord> {

  @Override
  public ReconMismatchRecord mapRow(ResultSet rs, int rowNum) throws SQLException {

    ReconMismatchRecord row = new ReconMismatchRecord();
    row.setReconId(rs.getString("STORE_ID"));
    row.setRegisterNumber(rs.getString("REGISTER_NUMBER"));
    row.setTransactionNumber(rs.getString("TRANSACTION_NUMBER"));
    row.setCreationDate(rs.getDate("CREATION_DATE"));
    row.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
    row.setSubsriberNumber(rs.getString("SUBSCRIBER_NUMBER"));
    row.setEmailAddress(rs.getString("EMAIL_ADDRESS"));
    row.setCustomerFirstName(rs.getString("CUSTOMER_F_NAME"));
    row.setCustomerLastName(rs.getString("CUSTOMER_L_NAME"));
    row.setSku(rs.getString("SKU"));

    row.setTraceId(rs.getLong("TRACE_ID"));
    row.setTenderType(rs.getString("TENDER_TYPE"));
    row.setRequestXml(rs.getBlob("REQUEST_XML"));
    row.setApplicationName(rs.getString("APPLICATION_NAME"));
    row.setDealerCode(rs.getString("DEALER_CODE"));
    row.setStoreCity(rs.getString("STORE_CITY"));
    row.setStoreState(rs.getString("STORE_STATE"));
    row.setRefObjectId(rs.getString("R_OBJECT_ID"));

    String status = rs.getString("STATUS");
    row.setStatus(status == null ? ReconStatus.NOT_STARTED.name() : status);
    row.setRetryCount(rs.getInt("RETRY_COUNT"));
    row.setExecutionDuration(rs.getLong("EXEC_DURATION"));

    // row.setJobRunDate(rs.getDate("JOB_RUN_DATE"));

    return row;
  }

}
