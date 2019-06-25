package com.adaptris.core.transform.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceException;
import com.adaptris.core.ServiceImp;
import com.adaptris.core.util.ExceptionHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Transform service which allows us to generate TEXT from PDF.
 * 
 * @config pdf-to-text-service
 */
@XStreamAlias("pdf-to-text-service")
@AdapterComponent
@ComponentProfile(summary = "Transform PDF into TEXT", tag = "service,transform,text,pdf")
public class PdfToTextService extends ServiceImp {

  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    try (PDDocument pdf = PDDocument.load(msg.getPayload())) {
      PDFTextStripper pdfStripper = new PDFTextStripper();

      //Retrieving text from PDF document
      String text = pdfStripper.getText(pdf);
      msg.setContent(text, msg.getContentEncoding());
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

