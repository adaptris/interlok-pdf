package com.adaptris.core.transform.pdf;

import java.io.Writer;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceException;
import com.adaptris.core.ServiceImp;
import com.adaptris.core.util.ExceptionHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import io.github.se_be.pdf2dom.PDFDomTree;

/**
 * Transform service which allows us to generate HTML from PDF.
 *
 * @config pdf-to-html-service
 */
@XStreamAlias("pdf-to-html-service")
@AdapterComponent
@ComponentProfile(summary = "Transform PDF into HTML", tag = "service,transform,html,pdf", since = "3.9.0")
public class PdfToHtmlService extends ServiceImp {

  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    try (PDDocument pdf = Loader.loadPDF(new RandomAccessReadBuffer(msg.getPayload())); Writer writer = msg.getWriter()) {
      new PDFDomTree().writeText(pdf, writer);
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
