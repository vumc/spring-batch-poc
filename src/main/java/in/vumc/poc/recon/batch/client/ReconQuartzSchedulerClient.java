package in.vumc.poc.recon.batch.client;

import in.vumc.poc.recon.scheduler.quartz.ReconQuartzScheduler;

import in.vumc.poc.recon.batch.ReconBatchProperties;

import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ReconQuartzSchedulerClient {

  private static Logger log = Logger.getLogger(ReconQuartzSchedulerClient.class);
  private static ReconQuartzSchedulerClient instance = new ReconQuartzSchedulerClient();

  public static void main(String[] args) throws Exception {
    runReconJob();
  }

  public static void runReconJob() {
    instance.runCronScheduler();
  }

  private void runCronScheduler() {
    log.info("****************** START - RECON QUARTZ SCHEDULER JOB ********************************");

    String[] springConfig = { "recon-recon-config/recon-recon-shared-config.xml" };
    ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

    try {
      ReconBatchProperties reconBatchProperties = (ReconBatchProperties) context.getBean("reconDocumentumBatchProperties");
      String reconCronSchedule = reconBatchProperties.getReconCronSchedule();

      log.info("runCronScheduler :: reconCronSchedule: " + reconCronSchedule);

      JobDetail job = JobBuilder.newJob(ReconQuartzScheduler.class).withIdentity("recon-job", "recon-job-group").build();

      CronTrigger trigger = TriggerBuilder.newTrigger()
                                          .withIdentity("recon-trigger", "recon-trigger-group")
                                          .startNow()
                                          .withSchedule(CronScheduleBuilder.cronSchedule(reconCronSchedule)
                                                                           .inTimeZone(TimeZone.getDefault()))
                                          .forJob(job)
                                          .build();

      Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

      scheduler.start();
      scheduler.scheduleJob(job, trigger);

    } catch (SchedulerException e) {
      log.error("runCronScheduler :: SchedulerException statcktrace: " + e);
    } catch (Exception e) {
      log.error("runCronScheduler :: Excepion statcktrace: " + e);

    } finally {
      // closing the context
      if (context != null) {
        ((ClassPathXmlApplicationContext) context).close();
      }

      log.info("****************** FINISH - RECON QUARTZ SCHEDULER JOB ********************************");
    }
  }

}
