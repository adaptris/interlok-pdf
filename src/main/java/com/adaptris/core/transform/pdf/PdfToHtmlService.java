package com.adaptris.core.transform.pdf;

import java.io.StringWriter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceException;
import com.adaptris.core.ServiceImp;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Transform service which allows us to generate HTML from PDF.
 * 
 * @config pdf-to-html-service
 */
@XStreamAlias("pdf-to-html-service")
@AdapterComponent
@ComponentProfile(summary = "Transform PDF into HTML", tag = "service,transform,html,pdf")
public class PdfToHtmlService extends ServiceImp {

  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    PDDocument pdf = null;
    try {
      pdf = PDDocument.load(msg.getPayload());
      StringWriter writer = new StringWriter();
      
      new PDFDomTree().writeText(pdf, writer);
      writer.flush();      
      msg.setContent(writer.toString(), msg.getContentEncoding());
    } catch (Throwable e) {
      throw new ServiceException(e);
    } finally {
      try {
        pdf.close();
      } catch (Throwable e) {
      }
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
