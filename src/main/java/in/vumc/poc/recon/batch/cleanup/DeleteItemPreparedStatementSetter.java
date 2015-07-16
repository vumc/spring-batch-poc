package in.vumc.poc.recon.batch.cleanup;

import in.vumc.poc.recon.batch.ReconMismatchRecord;
import in.vumc.poc.recon.batch.ReconStatus;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class DeleteItemPreparedStatementSetter implements ItemPreparedStatementSetter<ReconMismatchRecord> {

  @Override
  public void setValues(ReconMismatchRecord item, PreparedStatement ps) throws SQLException {
    ps.setString(1, ReconStatus.COMPLETED.name());
  }

}
