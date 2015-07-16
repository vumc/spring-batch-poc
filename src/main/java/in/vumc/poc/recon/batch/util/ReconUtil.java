package in.vumc.poc.recon.batch.util;

import in.vumc.poc.recon.batch.exception.ReconException;
import logical.crypto.blowfish.Blowfish;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

/**
 * Static utility methods
 * @author bvamsikrishna
 *
 */
public final class ReconUtil {

  private static final Logger LOG = Logger.getLogger(ReconUtil.class);
  private static final String THIS_IS_SECRET_ENCRYPTION_KEY = "dnafkdnkndasfkafndkasnfdaksf";
  private static Blowfish blowfishInstance = null;

  private static Blowfish getBlowFishInstance() {
    if (blowfishInstance == null) {
      blowfishInstance = new Blowfish();
    }
    return blowfishInstance;
  }

  private ReconUtil() {
  }

  /**
   * Encrypt a plain text
   * @param plainText
   * @return
   */
  public static String encryptText(String plainText) {
    return ReconUtil.getBlowFishInstance().encryptStrWithPass(THIS_IS_SECRET_ENCRYPTION_KEY, plainText);
  }

  /**
   * Decrypt an encrypted text
   * @param encryptedText
   * @return
   */
  public static String decryptText(String encryptedText) {
    return ReconUtil.getBlowFishInstance().decryptStrWithPass(THIS_IS_SECRET_ENCRYPTION_KEY, encryptedText);
  }

  /**
   * Convert bytes to an object
   * @param byteArray
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static Object toObject(byte[] byteArray) {
    ByteArrayInputStream in = null;
    ObjectInputStream is = null;

    try {
      in = new ByteArrayInputStream(byteArray);
      is = new ObjectInputStream(in);

      return is.readObject();

    } catch (ClassNotFoundException e) {
      LOG.error("toObject :: failed to deserialize byte array", e);
      throw new ReconException(e);

    } catch (IOException e) {
      LOG.error(" toBytes :: failed to serialize object", e);
      throw new ReconException(e);
    }

    finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          // ignore
        }
      }
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          // ignore
        }
      }

    }
  }

  /**
   * Convert an object to bytes
   * @param input
   * @return
   * @throws IOException
   */
  public static byte[] toBytes(Object input) {
    ByteArrayOutputStream byteArrayOutPutStream = null;
    ObjectOutputStream out = null;
    try {
      byteArrayOutPutStream = new ByteArrayOutputStream();
      out = new ObjectOutputStream(byteArrayOutPutStream);
      out.writeObject(input);
      out.flush();
      return byteArrayOutPutStream.toByteArray();
    } catch (IOException e) {
      LOG.error(" toBytes :: failed to serialize object", e);
      throw new ReconException(e);
    } finally {
      try {
        if (byteArrayOutPutStream != null) {
          byteArrayOutPutStream.close();
        }
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {
        // ignore
      }
    }
  }


}
