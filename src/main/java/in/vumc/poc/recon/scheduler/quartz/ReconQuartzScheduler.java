package in.vumc.poc.recon.scheduler.quartz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ReconQuartzScheduler implements Job {

  private static Logger log = Logger.getLogger(ReconQuartzScheduler.class);

  // FIXME: Need to read from properties file
  private static final String RECON_TARGET_URL = "http://localhost:7001/recon/reconJob/start";

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {

    try {
      if (log.isDebugEnabled()) {
        log.debug("\nRunning Recon Batch Job... at time : " + new Date());
      }
      URL reconTargetURL = new URL(RECON_TARGET_URL);

      HttpURLConnection httpConnection = (HttpURLConnection) reconTargetURL.openConnection();

      httpConnection.setRequestMethod("GET");
      httpConnection.setRequestProperty("action", "runJob");

      if (httpConnection.getResponseCode() != 200) {
        throw new RuntimeException("HTTP GET Request for RECON JOB Failed with Error code : " + httpConnection.getResponseCode());
      }

      // printResponse(httpConnection);

      httpConnection.disconnect();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  @SuppressWarnings("unused")
  private void printResponse(HttpURLConnection httpConnection) throws IOException {
    BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));

    String output;

    while ((output = responseBuffer.readLine()) != null) {
      log.debug(output);
    }
  }

}
