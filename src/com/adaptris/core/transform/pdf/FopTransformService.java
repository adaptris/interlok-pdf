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

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceException;
import com.adaptris.core.ServiceImp;
import com.adaptris.util.license.License;
import com.adaptris.util.license.License.LicenseType;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Transform service based on the Apache Fop project - http://xmlgraphics.apache.org/fop/
 * 
 * @config fop-transform-service
 * @license BASIC
 */
@XStreamAlias("fop-transform-service")
public class FopTransformService extends ServiceImp {

  // marshalled
  private String outputFormat;

  // castor transient
  private transient Transformer transformer;

  /**
   * Creates new instance.  Default output format is <code>MimeConstants.MIME_PDF</code>.
   */
  public FopTransformService() {
    this.setOutputFormat(MimeConstants.MIME_PDF);
  }

  /** @see com.adaptris.core.AdaptrisComponent#init() */
  public void init() throws CoreException {
    try {
      transformer = TransformerFactory.newInstance().newTransformer();
    }
    catch (TransformerConfigurationException e) {
      throw new ServiceException(e);
    }
  }

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

  /** @see com.adaptris.core.AdaptrisComponent#close() */
  public void close() {
    // n/a...
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
  public boolean isEnabled(License license) throws CoreException {
    return license.isEnabled(LicenseType.Basic);
  }
}
