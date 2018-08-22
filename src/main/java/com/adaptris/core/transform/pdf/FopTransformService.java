package com.adaptris.core.transform.pdf;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.MimeConstants;
import org.hibernate.validator.constraints.NotBlank;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.AutoPopulated;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceException;
import com.adaptris.core.ServiceImp;
import com.adaptris.core.util.Args;
import com.adaptris.core.util.ExceptionHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Transform service based on the Apache Fop project - http://xmlgraphics.apache.org/fop/
 * 
 * @config fop-transform-service
 * @license BASIC
 */
@XStreamAlias("fop-transform-service")
@AdapterComponent
@ComponentProfile(summary = "Transform into a PDF using Apache Fop", tag = "service,transform,xml,pdf")
public class FopTransformService extends ServiceImp {

  // marshalled
  @NotBlank
  @AutoPopulated
  private String outputFormat;

  @InputFieldDefault(value = "new File('.').toURI()")
  private String baseUri;

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

  public void doService(AdaptrisMessage msg) throws ServiceException {

    try (InputStream in = msg.getInputStream(); OutputStream out = msg.getOutputStream()){
      FopFactory fopFactory = new FopFactoryBuilder(baseURI()).build();
      Fop fop = fopFactory.newFop(this.getOutputFormat(), fopFactory.newFOUserAgent(), out);

      Source source = new StreamSource(in);
      Result result = new SAXResult(fop.getDefaultHandler());
      this.transformer.transform(source, result);
//      msg.setPayload(out.toByteArray());
    }
    catch (Exception e) {
      throw ExceptionHelper.wrapServiceException(e);
    }
  }

  // properties

  public String getOutputFormat() {
    return outputFormat;
  }

  public void setOutputFormat(String s) {
    this.outputFormat = Args.notBlank(s, "outputFormat");
  }

  @Override
  public void prepare() throws CoreException {}

  public String getBaseUri() {
    return baseUri;
  }

  /**
   * Specify the base URI for the FopFactoryBuilder.
   * 
   * @param uri the base uri; If not specified, then {@code new File(".").toURI()} is used.
   */
  public void setBaseUri(String uri) {
    this.baseUri = uri;
  }

  protected URI baseURI() throws URISyntaxException {
    if (!StringUtils.isEmpty(getBaseUri())) {
      return new URI(getBaseUri());
    }
    return new File(".").toURI();
  }
}
