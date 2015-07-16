package in.vumc.poc.recon.batch.mail;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * 
 * @author bvamsikrishna
 *
 */
public class ReconEmailTasklet implements Tasklet {

  private ReconEmailManager reconEmailManager;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

    reconEmailManager.sendMail();

    return RepeatStatus.FINISHED;
  }

  public void setReconEmailManager(ReconEmailManager reconEmailManager) {
    this.reconEmailManager = reconEmailManager;
  }


}
