package com.adaptris.core.transform.pdf;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.hibernate.validator.constraints.NotBlank;

import com.adaptris.annotation.AutoPopulated;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceException;
import com.adaptris.core.licensing.License;
import com.adaptris.core.licensing.License.LicenseType;
import com.adaptris.core.licensing.LicenseChecker;
import com.adaptris.core.licensing.LicensedService;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Transform service based on the Apache Fop project - http://xmlgraphics.apache.org/fop/
 * 
 * @config fop-transform-service
 * @license BASIC
 */
@XStreamAlias("fop-transform-service")
public class FopTransformService extends LicensedService {

  // marshalled
  @NotBlank
  @AutoPopulated
  private String outputFormat;

  // castor transient
  private transient Transformer transformer;

  /**
   * Creates new instance.  Default output format is <code>MimeConstants.MIME_PDF</code>.
   */
  public FopTransformService() {
    this.setOutputFormat(MimeConstants.MIME_PDF);
  }

  protected void initService() throws CoreException {
    try {
      transformer = TransformerFactory.newInstance().newTransformer();
    }
    catch (TransformerConfigurationException e) {
      throw new CoreException(e);
    }
  }

  protected void closeService() {}

  /** @see com.adaptris.core.Service#doService(com.adaptris.core.AdaptrisMessage) */
  public void doService(AdaptrisMessage msg) throws ServiceException {

    OutputStream out = null;
    InputStream in = null;
    try {
      out = msg.getOutputStream();
      in = msg.getInputStream();
      FopFactory fopFactory = FopFactory.newInstance();
      Fop fop = fopFactory.newFop(this.getOutputFormat(), fopFactory.newFOUserAgent(), out);

      Source source = new StreamSource(in);
      Result result = new SAXResult(fop.getDefaultHandler());
      this.transformer.transform(source, result);
//      msg.setPayload(out.toByteArray());
    }
    catch (Exception e) {
      throw new ServiceException(e);
    }
    finally {
      IOUtils.closeQuietly(in);
      IOUtils.closeQuietly(out);
    }
  }

  // properties

  public String getOutputFormat() {
    return outputFormat;
  }

  public void setOutputFormat(String s) {
    if (s == null || "".equals(s)) {
      throw new IllegalArgumentException("null or empty param");
    }
    this.outputFormat = s;
  }

  @Override
  protected void prepareService() throws CoreException {
    LicenseChecker.newChecker().checkLicense(this);
  }

  @Override
  public boolean isEnabled(License license) {
    return license.isEnabled(LicenseType.Basic);
  }
}
