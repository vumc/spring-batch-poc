package in.vumc.poc.recon.batch.controller;

import in.vumc.poc.recon.batch.client.ReconJobClient;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author bvamsikrishna
 *
 */
@Controller
@RequestMapping("/reconJob")
public class ReconController {

 // private static final Log log = LogFactory.getLog(ReconController.class);

  @RequestMapping(value = "/start", method = RequestMethod.GET)
  public void start(ModelMap model) throws IOException, InterruptedException {

    ReconJobClient.runReconJob();
  }


}
