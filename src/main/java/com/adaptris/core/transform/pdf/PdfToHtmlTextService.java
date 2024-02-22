package com.adaptris.core.transform.pdf;

import java.io.Writer;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.tools.PDFText2HTML;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceException;
import com.adaptris.core.ServiceImp;
import com.adaptris.core.util.ExceptionHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Transform service which allows us to generate HTML text from PDF. This work more like the <b>pdf-to-text-service</b> as it does not keep
 * colors and formatting.
 *
 * @config pdf-to-html-service
 */
@XStreamAlias("pdf-to-html-text-service")
@AdapterComponent
@ComponentProfile(summary = "Transform PDF into HTML text", tag = "service,transform,html,pdf", since = "5.0.2")
public class PdfToHtmlTextService extends ServiceImp {

  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    try (PDDocument pdf = Loader.loadPDF(new RandomAccessReadBuffer(msg.getPayload())); Writer writer = msg.getWriter()) {
      new PDFText2HTML().writeText(pdf, writer);
    } catch (Throwable e) {
      throw ExceptionHelper.wrapServiceException(e);
    }
  }

  @Override
  public void prepare() throws CoreException {
  }

  @Override
  protected void initService() throws CoreException {
  }

  @Override
  protected void closeService() {
  }

}
