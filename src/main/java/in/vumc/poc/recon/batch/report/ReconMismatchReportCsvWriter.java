package in.vumc.poc.recon.batch.report;

import in.vumc.poc.recon.batch.ReconStatus;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;

/**
 * Recon mismatch records CSV report generator
 * 
 * @author bvamsikrishna
 *
 */
public class ReconMismatchReportCsvWriter implements
                                             ItemWriter<ReconMismatchReport>,
                                             FlatFileFooterCallback,
                                             FlatFileHeaderCallback,
                                             ItemStream {

  private String reconReportName;

  private String reconReportHeaderColumns;
  private FlatFileItemWriter<ReconMismatchReport> reconCsvFileWriterDelegate;

  private long totalNumberOfTransactionsProcessed;
  private long totalNumberOfFailedTransactions;
  private long totalBatchExecutionDuration;
  private static String NEWLINE = System.getProperty("line.separator");
  private static String COMMA = ", ";

  @Override
  public void write(List<? extends ReconMismatchReport> items) throws Exception {

    totalNumberOfTransactionsProcessed = items.size();

    int totalItemFailedTransactions = 0;
    int totalItemExceutionDuration = 0;

    List<ReconMismatchReport> failedTxItems = new ArrayList<ReconMismatchReport>();

    for (ReconMismatchReport item : items) {

      // total duration
      totalItemExceutionDuration += item.getExecutionDuration();

      if (ReconStatus.FAILED.name().equals(item.getStatus())) {
        totalItemFailedTransactions++;
        failedTxItems.add(item);
      }
    }

    // write the failed transactions to file
    reconCsvFileWriterDelegate.write(failedTxItems);

    totalNumberOfFailedTransactions += totalItemFailedTransactions;
    totalBatchExecutionDuration += totalItemExceutionDuration;
  }

  @Override
  public void writeHeader(Writer writer) throws IOException {
    writer.write(reconReportName + COMMA + new Date());
    writer.write(NEWLINE);
    writer.write(NEWLINE);

    writer.write("Report Transactions");
    writer.write(NEWLINE);
    writer.write(reconReportHeaderColumns);
  }

  @Override
  public void writeFooter(Writer writer) throws IOException {
    writeReportSummary(writer);
  }

  @Override
  public void open(ExecutionContext executionContext) throws ItemStreamException {
    this.reconCsvFileWriterDelegate.open(executionContext);

  }

  @Override
  public void update(ExecutionContext executionContext) throws ItemStreamException {
    this.reconCsvFileWriterDelegate.update(executionContext);

  }

  @Override
  public void close() throws ItemStreamException {
    this.reconCsvFileWriterDelegate.close();

  }

  public void setReconCsvFileWriterDelegate(FlatFileItemWriter<ReconMismatchReport> reconCsvFileWriterDelegate) {
    this.reconCsvFileWriterDelegate = reconCsvFileWriterDelegate;
  }

  public void setReconReportName(String reconReportName) {
    this.reconReportName = reconReportName;
  }

  public void setReconReportHeaderColumns(String reconReportHeaderColumns) {
    this.reconReportHeaderColumns = reconReportHeaderColumns;
  }


  private void writeReportSummary(Writer writer) throws IOException {

    StringBuilder sb = new StringBuilder(NEWLINE);
    sb.append("Report Summary")
      .append(NEWLINE)
      .append("Total number of transactions processed" + COMMA + totalNumberOfTransactionsProcessed)
      .append(NEWLINE)
      .append("Total number of failures" + COMMA + totalNumberOfFailedTransactions)
      .append(NEWLINE)
      .append("Total number of time taken for one execution" + COMMA + totalBatchExecutionDuration + COMMA + "ms")
      .append(NEWLINE)
      .append("Total number of transactions processed per run in all queue" + COMMA
              + totalNumberOfTransactionsProcessed)
      .append(NEWLINE);

    writer.write(sb.toString());
  }

}
