package in.vumc.poc.recon.batch.middleware;

import static in.vumc.poc.recon.batch.util.ReconUtil.encryptText;
import static in.vumc.poc.recon.batch.util.ReconUtil.toBytes;
import static in.vumc.poc.recon.batch.util.ReconUtil.toObject;

import in.vumc.poc.recon.batch.exception.ReconException;

import java.net.SocketTimeoutException;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.log4j.Logger;

/**
 * 
 * @author bvamsikrishna
 *
 */
public class ReconMiddlewareServiceImpl implements ReconMiddlewareService {

  private static final Logger log = Logger.getLogger(ReconMiddlewareServiceImpl.class);
  private static HttpClient httpclient = null;
  private static PostMethod post = null;


  @Override
  public byte[] post(String reconServiceUrl, String bodyPayload, boolean doSecure) {

    byte[] requestEntityBodyPayload = doSecure ? toBytes(encryptText(bodyPayload)) : toBytes(bodyPayload);

    return post(reconServiceUrl, requestEntityBodyPayload);
  }

  @Override
  public byte[] post(String reconServiceUrl, byte[] requestEntityBodyPayload) {

    if (log.isInfoEnabled()) {
      log.info("post :: reconServiceUrl: " + reconServiceUrl);
      log.info("post :: requestEntityBodyPayload: " + requestEntityBodyPayload);
    }

    try {
      post = new PostMethod(reconServiceUrl);
      post.addRequestHeader("Accept", "text/xml");

      RequestEntity entity = new ByteArrayRequestEntity(requestEntityBodyPayload);
      post.setRequestEntity(entity);

      int statusCode = httpclient.executeMethod(post);
      byte[] responseBytes = post.getResponseBody();

      if (log.isInfoEnabled()) {
        log.info("post response statusCode: " + statusCode);
        Object response = toObject(responseBytes);
        log.info("post responseObject: " + response);
      }

      if (statusCode == HttpStatus.SC_OK) {
        return responseBytes;

      } else {
        throw new ReconException("POST HttpStatus response is " + statusCode);
      }

    } catch (ConnectTimeoutException cte) {

      log.error("ConnectTimeoutException while calling Middleware Service: " + reconServiceUrl
                + " :: Stacktrace :: "
                + cte);
      throw new ReconException(cte);

    } catch (SocketTimeoutException ste) {
      log.error("SocketTimeoutException while calling Middleware Service: " + reconServiceUrl
                + " :: Stacktrace :: "
                + ste);
      throw new ReconException(ste);

    } catch (Exception e) {
      log.error("Exception occurred while calling Middleware Service: " + reconServiceUrl + " :: Stacktrace :: " + e);
      throw new ReconException(e);

    } finally {
      if (post != null) {
        post.releaseConnection();
        log.debug("finally released the connection");
      }
    }
  }




}
