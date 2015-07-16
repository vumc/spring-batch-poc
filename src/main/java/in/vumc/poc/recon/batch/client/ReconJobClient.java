package in.vumc.poc.recon.batch.client;

import in.vumc.poc.recon.batch.ReconBatchProperties;
import in.vumc.poc.recon.batch.ReconStatus;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Recon Reconciliation main application to start the batch process
 * 
 * @author bvamsikrishna
 *
 */
public class ReconJobClient {

  private static Logger LOG = Logger.getLogger(ReconJobClient.class);

  private static ReconJobClient instance = new ReconJobClient();

  public static void runReconJob() {
    instance.doRunReconJob();
  }

  public static void main(String[] args) {
    runReconJob();
  }

  private void doRunReconJob() {

    if (LOG.isInfoEnabled()) {
      LOG.info("****************** START - RECON BATCH PROCESSING JOB ********************************");
    }

    String[] springConfig = { "recon-config/recon-config.xml" };
    ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

    try {
      JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
      Job reconJob = (Job) context.getBean("recon-job");
      ReconBatchProperties reconBatchProperties = (ReconBatchProperties) context.getBean("reconDocumentumBatchProperties");


      long maxRetryCount = reconBatchProperties.getMaxRetryCount();
      long maxThrottle = reconBatchProperties.getMaxThrottle();

      if (LOG.isInfoEnabled()) {
        LOG.info("doRunReconJob :: maxRetryCount: " + maxRetryCount + " :: maxThrottle: " + maxThrottle);
      }
      JobParameters jobParameters = new JobParametersBuilder().addDate("schedule.date", new Date())
                                                              .addLong("maxThrottle", maxThrottle)
                                                              .addLong("maxRetryCount", maxRetryCount)
                                                              .addString("failedStatus", ReconStatus.FAILED.name())
                                                              .toJobParameters();

      JobExecution execution = jobLauncher.run(reconJob, jobParameters);

      if (LOG.isInfoEnabled()) {
        LOG.info("doRunReconJob :: Failures: " + execution.getAllFailureExceptions());
        LOG.info("doRunReconJob :: Recon Batch Job Status: " + execution.getStatus());
      }
    } catch (Exception e) {
      LOG.error(e.getMessage());

    } finally {
      // closing the context
      if (context != null) {
        ((ClassPathXmlApplicationContext) context).close();
      }
      if (LOG.isInfoEnabled()) {
        LOG.info("******************** FINISH - RECON BATCH PROCESSING JOB **********************************");
      }
    }
  }

}
