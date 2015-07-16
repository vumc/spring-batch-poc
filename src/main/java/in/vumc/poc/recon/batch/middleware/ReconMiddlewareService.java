package in.vumc.poc.recon.batch.middleware;

import in.vumc.poc.recon.batch.exception.ReconException;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;


/**
 * An interface to invoke the Recon Service restpoints. {@linkplain ReconExternalService}
 * 
 * @author bvamsikrishna
 *
 */
public interface ReconMiddlewareService {
  
  
  /**
   * This method converts requestEntityBodyPayload to requestEntityBodyPayload bytes and invokes
   * ReconMiddlewareService call.
   * 
   * @param reconServiceUrl
   * @param bodyPayload
   * @param doSecure
   * @return bytes
   * @throws HttpException
   * @throws ReconException
   * @throws IOException
   */
  public byte[] post(String reconServiceUrl, String bodyPayload, boolean doSecure);

  /**
   * This method simply invokes configured middleware service call.
   * 
   * @param byte requestEntityBodyPayload
   * @param reconServiceUrl
   * @return bytes
   * @throws HttpException
   * @throws ReconException
   * @throws IOException
   */
  public byte[] post(String reconServiceUrl, byte[] requestEntityBodyPayload);
  

}
