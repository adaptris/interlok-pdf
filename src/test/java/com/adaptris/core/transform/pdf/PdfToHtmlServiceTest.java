package com.adaptris.core.transform.pdf;

import java.io.File;
import java.io.FileInputStream;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.ServiceException;
import com.adaptris.core.transform.TransformServiceExample;
import com.adaptris.core.util.LifecycleHelper;

public class PdfToHtmlServiceTest extends TransformServiceExample {
  
  private static String INPUT_KEY = "PdfToHtmlService.inputPdf";
  
  private byte[] input;

  private PdfToHtmlService service;
  
  private AdaptrisMessage message;
  
  public PdfToHtmlServiceTest(String name) throws Exception {
    super(name);
    
    if (PROPERTIES.getProperty(INPUT_KEY) != null) {
      FileInputStream fileInputStream = null;
      try {
        fileInputStream = new FileInputStream(new File(PROPERTIES
          .getProperty(INPUT_KEY)));

      input = new byte[fileInputStream.available()];
      fileInputStream.read(input);
      } finally {
        fileInputStream.close();
      }
    }
  }
  
  public void setUp() throws Exception {
    service = new PdfToHtmlService();
    
    message = DefaultMessageFactory.getDefaultInstance().newMessage(input);
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    service.setUniqueId("pdf-to-html-service-id");
    return service;
  }
  
  public void testTransform() throws Exception {
    LifecycleHelper.initAndStart(service);
    service.doService(message);
    LifecycleHelper.stopAndClose(service);
    
    assertTrue(message.getContent().contains("<html"));
    assertTrue(message.getContent().contains("My"));
    assertTrue(message.getContent().contains("sample"));
    assertTrue(message.getContent().contains("pdf"));
    assertTrue(message.getContent().contains("content"));
  }
  
  public void testFails() throws Exception {
    message.setContent("Not a pdf file.", message.getContentEncoding());
    
    LifecycleHelper.initAndStart(service);
    try {
      service.doService(message);
      fail("Should fail to parse the PDF.");
    } catch (ServiceException ex) {
      // expected.
    }
    LifecycleHelper.stopAndClose(service);
  }

}
