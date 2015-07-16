package in.vumc.poc.recon.batch.report;

import in.vumc.poc.recon.batch.ReconStatus;
import in.vumc.poc.recon.batch.mail.ReconEmailManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ReconMismatchReportPdfWriter implements
 ItemWriter<ReconMismatchReport> {

  private ReconEmailManager reconEmailManager;
  private String reconReportName;
 // private FlatFileItemWriter<ReconMismatchReport> reconCsvFileWriterDelegate;

  private String host;
  private String port;
  private String mailFrom;
  private String mailTo;

  private long totalNumberOfTransactionsProcessed;
  private long totalNumberOfFailedTransactions;
  private long totalBatchExecutionDuration;

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
	   // reconCsvFileWriterDelegate.write(failedTxItems);

	    totalNumberOfFailedTransactions += totalItemFailedTransactions;
	    totalBatchExecutionDuration += totalItemExceutionDuration;

        String subject = reconReportName + (new Date().toString());
        String attachFiles = ReconMismatchReportPdfWriter.pdfWriteItems(items,
                                                                        totalNumberOfTransactionsProcessed,
                                                                        totalNumberOfFailedTransactions,
                                                                        totalBatchExecutionDuration);
        String message = ReconMismatchReportPdfWriter.createHTML(items,
                                                                 totalNumberOfTransactionsProcessed,
                                                                 totalNumberOfFailedTransactions,
                                                                 totalBatchExecutionDuration);

        try {
        	reconEmailManager.sendEmailWithAttachments(this.getHost(),
                                                this.getPort(),
                                                this.getMailFrom(),
                                                null,
                                                this.getMailTo(),
                                                subject,
                                                message,
                                                attachFiles);
	    } catch (AddressException e) {
	      e.printStackTrace();
	    } catch (MessagingException e1) {
	      e1.printStackTrace();
	    } catch (Exception e2) {
	      e2.printStackTrace();
	    }

  }

  public static String pdfWriteItems(List<? extends ReconMismatchReport> listResults,
                                     long totalNumberOfTransactionsProcessed,
                                     long totalNumberOfFailedTransactions,
                                     long totalBatchExecutionDuration) {
    String fileName = null;
    try {
      Document document = new Document(PageSize.PENGUIN_LARGE_PAPERBACK.rotate());
      fileName = "ReconReport" + String.valueOf(System.currentTimeMillis()) + ".pdf";
      PdfWriter.getInstance(document, new FileOutputStream(fileName));
      document.open();
      Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA);
      fontHeader.setColor(BaseColor.BLUE);
      fontHeader.setSize(10);

      Paragraph parameter = new Paragraph("Recon Reconciliation Report", fontHeader);

      parameter.setAlignment(Element.ALIGN_CENTER);
      document.add(parameter);

      PdfPTable table = new PdfPTable(11);
      table.setWidthPercentage(106.0f);
      table.setWidths(new float[] { 1.5f, 1.5f, 1.3f, 1.5f, 1.5f, 1.0f, 2.0f, 2.0f, 1.6f, 1.0f, 1.5f });
      table.setSpacingBefore(8);

      Font fontTableHeader = FontFactory.getFont(FontFactory.HELVETICA);
      fontTableHeader.setColor(BaseColor.BLACK);
      fontTableHeader.setSize(8);

      PdfPCell cell = new PdfPCell();
      cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
      cell.setPadding(4);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setPhrase(new Phrase("Trace Id", fontTableHeader));
      table.addCell(cell);
      cell.setPhrase(new Phrase("Status", fontTableHeader));
      table.addCell(cell);
      cell.setPhrase(new Phrase("Store Id", fontTableHeader));
      table.addCell(cell);
      cell.setPhrase(new Phrase("Transaction Id", fontTableHeader));
      table.addCell(cell);
      cell.setPhrase(new Phrase("BAN", fontTableHeader));
      table.addCell(cell);
      cell.setPhrase(new Phrase("MSISDN", fontTableHeader));
      table.addCell(cell);
      cell.setPhrase(new Phrase("E-mail", fontTableHeader));
      table.addCell(cell);
      cell.setPhrase(new Phrase("Customer Name", fontTableHeader));
      table.addCell(cell);
      cell.setPhrase(new Phrase("Transaction Date", fontTableHeader));
      table.addCell(cell);
      cell.setPhrase(new Phrase("SKU", fontTableHeader));
      table.addCell(cell);
      cell.setPhrase(new Phrase("Tender Type", fontTableHeader));
      table.addCell(cell);

      Font fontTableCell = FontFactory.getFont(FontFactory.HELVETICA);
      fontTableCell.setColor(BaseColor.BLACK);
      fontTableCell.setSize(8);
      PdfPCell cellElements = new PdfPCell();

      @SuppressWarnings("unchecked")
      Iterator<ReconMismatchReport> iterator = (Iterator<ReconMismatchReport>) listResults.iterator();
      while (iterator.hasNext()) {
        ReconMismatchReport pdfWriter = (ReconMismatchReport) iterator.next();
        if(null != pdfWriter.getStatus() && !pdfWriter.getStatus().equals("COMPLETED")){
	        cellElements.setPhrase(new Phrase(String.valueOf(pdfWriter.getTraceId()), fontTableCell));
	        table.addCell(cellElements);
	        cellElements.setPhrase(new Phrase(pdfWriter.getStatus(), fontTableCell));
	        table.addCell(cellElements);
	        cellElements.setPhrase(new Phrase(pdfWriter.getStoreId(), fontTableCell));
	        table.addCell(cellElements);
	        cellElements.setPhrase(new Phrase(pdfWriter.getTransactionId(), fontTableCell));
	        table.addCell(cellElements);
	        cellElements.setPhrase(new Phrase(pdfWriter.getBan(), fontTableCell));
	        table.addCell(cellElements);
	        cellElements.setPhrase(new Phrase(pdfWriter.getMsisdn(), fontTableCell));
	        table.addCell(cellElements);
	        cellElements.setPhrase(new Phrase(pdfWriter.getEmail(), fontTableCell));
	        table.addCell(cellElements);
	        cellElements.setPhrase(new Phrase(pdfWriter.getCustomerName(), fontTableCell));
	        table.addCell(cellElements);
	        cellElements.setPhrase(new Phrase(pdfWriter.getTransactionDate().toString(), fontTableCell));
	        table.addCell(cellElements);
	        cellElements.setPhrase(new Phrase(pdfWriter.getSku(), fontTableCell));
	        table.addCell(cellElements);
	        cellElements.setPhrase(new Phrase(pdfWriter.getTenderType(), fontTableCell));
	        table.addCell(cellElements);
        }
      }

      document.add(table);
      Font fontRecords = FontFactory.getFont(FontFactory.HELVETICA);
      fontRecords.setColor(BaseColor.BLACK);
      fontRecords.setSize(7);
      Paragraph paraProcessedPerRun = new Paragraph("Total number of transaction processed per run in all queue :" + totalNumberOfTransactionsProcessed,
                                                    fontRecords);
      paraProcessedPerRun.setAlignment(Element.ALIGN_LEFT);
      document.add(paraProcessedPerRun);
      Paragraph paraTimeTakenExecution = new Paragraph("Total number of time taken for one execution in minutes:" + totalBatchExecutionDuration,
                                                       fontRecords);
      paraTimeTakenExecution.setAlignment(Element.ALIGN_LEFT);
      document.add(paraTimeTakenExecution);
      Paragraph paraTotalTransactionProcessed = new Paragraph("Total number of transaction processed :" + totalNumberOfTransactionsProcessed,
                                                              fontRecords);
      paraTotalTransactionProcessed.setAlignment(Element.ALIGN_LEFT);
      document.add(paraTotalTransactionProcessed);
      Paragraph paraTotalNoOfFailures = new Paragraph("Total number of failures :" + totalNumberOfFailedTransactions,
                                                      fontRecords);
      paraTotalNoOfFailures.setAlignment(Element.ALIGN_LEFT);
      document.add(paraTotalNoOfFailures);
      document.close();
    } catch (DocumentException de) {
      de.printStackTrace();
    } catch (IOException ie) {
      ie.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return fileName;
  }

  public static String createHTML(List<? extends ReconMismatchReport> listResults,
                                  long totalNumberOfTransactionsProcessed,
                                  long totalNumberOfFailedTransactions,
                                  long totalBatchExecutionDuration) {
    String message = "";
    message += "<html>" + "<head>" + "<style type='text/css'>";
    message += "body{margin:0;padding:0;}td{font-family:Arial;font-size:8pt;}th{font-family:Arial;font-size:10pt;}" + "#para1"
               + "{"
               + "color:green;"
               + "}"
               + "#para2"
               + "{"
               + "color:red;"
               + "}"
               + "</style>"
               + "</head>"
               + "<body>";
    message += "<form>";
    message += "<br><br>" + "<center><table width='100%' bgcolor='#B0B0B0'>"
               + "<tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes;mso-yfti-lastrow:yes'>";
    message += "<td style='padding:0in 0in 0in 0in'></td>";
    message += "<td>";
    message += "<p><b><span style='font-family:Arial;font-size:13pt'>Recon Reconciliation Report</span></b></p>";
    message += "</td></tr></table><br>";
    message += "<table align='center' border='1'>" + "<tr bgcolor='#B0B0B0' border='4'>"
               + "<th>"
               + "Trace Id"
               + "</th>";
    message += "<th>" + "Status"
               + "</th>"
               + "<th>"
               + "Store Id"
               + "</th>"
               + "<th>"
               + "Transaction Id"
               + "</th>"
               + "<th>"
               + "BAN"
               + "</th>"
               + "<th>"
               + "MSISDN"
               + "</th>"
               + "<th>"
               + "E-mail"
               + "</th>"
               + "<th>"
               + "Customer Name"
               + "</th>"
               + "<th>"
               + "Transaction Date"
               + "</th>"
               + "<th>"
               + "SKU"
               + "</th>"
               + "<th>"
               + "Tender Type"
               + "</th>"
               + "</tr>";

    @SuppressWarnings("unchecked")
    Iterator<ReconMismatchReport> iterator = (Iterator<ReconMismatchReport>) listResults.iterator();
    while (iterator.hasNext()) {
      ReconMismatchReport reconHtmlReport = (ReconMismatchReport) iterator.next();
      if(null != reconHtmlReport.getStatus() && !reconHtmlReport.getStatus().equals("COMPLETED")){
	      message += "<tr>";
	      message += "<td>" + reconHtmlReport.getTraceId() + "</td>";
	      message += "<td>" + reconHtmlReport.getStatus() + "</td>";
	      message += "<td>" + reconHtmlReport.getStoreId() + "</td>";
	      message += "<td>" + reconHtmlReport.getTransactionId() + "</td>";
	      message += "<td>" + reconHtmlReport.getBan() + "</td>";
	      message += "<td>" + reconHtmlReport.getMsisdn() + "</td>";
	      message += "<td>" + reconHtmlReport.getEmail() + "</td>";
	      message += "<td>" + reconHtmlReport.getCustomerName() + "</td>";
	      message += "<td>" + reconHtmlReport.getTransactionDate() + "</td>";
	      message += "<td>" + reconHtmlReport.getSku() + "</td>";
	      message += "<td>" + reconHtmlReport.getTenderType() + "</td>";
	      message += "</tr>";
      }
    }
    message += "</table></center>" + "<form>" + "</body>";
    ;
    message += "<br><table align='center'><tr>";
    message += "<td>" + "Total number of transactions processed :" + totalNumberOfTransactionsProcessed + "</td>";
    message += "<tr>";
    message += "<tr>";
    message += "<td>" + "Total number of failures :" + totalNumberOfFailedTransactions + "</td>";
    message += "<tr>";
    message += "<tr>";
    message += "<td>" + "Total number of time taken for one execution :" + totalBatchExecutionDuration + "</td>";
    message += "<tr>";
    message += "<tr>";
    message += "<td>" + "Total number of transactions processed per run in all queue :"
               + totalNumberOfTransactionsProcessed
               + "</td>";
    message += "<tr></table>" + "</html>";
    return message;
  }

  public void setReconEmailManager(ReconEmailManager reconEmailManager) {
    this.reconEmailManager = reconEmailManager;
  }

  public void setReconReportName(String reconReportName) {
    this.reconReportName = reconReportName;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getMailFrom() {
    return mailFrom;
  }

  public void setMailFrom(String mailFrom) {
    this.mailFrom = mailFrom;
  }

  public String getMailTo() {
    return mailTo;
  }

  public void setMailTo(String mailTo) {
    this.mailTo = mailTo;
  }

}
