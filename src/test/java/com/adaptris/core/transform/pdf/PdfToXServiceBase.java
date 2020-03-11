package com.adaptris.core.transform.pdf;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.File;
import java.io.FileInputStream;
import org.junit.Before;
import org.junit.Test;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.Service;
import com.adaptris.core.ServiceException;
import com.adaptris.core.transform.TransformServiceExample;
import com.adaptris.core.util.LifecycleHelper;

public abstract class PdfToXServiceBase extends TransformServiceExample {

  private static String INPUT_KEY = "PdfToHtmlService.inputPdf";
  
  private byte[] input;

  private Service service;
  
  private AdaptrisMessage message;
  
  public PdfToXServiceBase() throws Exception {
    super();
    
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
  
  @Before
  public void setUp() throws Exception {
    service = createPdfService();
    
    message = DefaultMessageFactory.getDefaultInstance().newMessage(input);
  }
  
  protected abstract Service createPdfService();

  @Override
  protected Object retrieveObjectForSampleConfig() {
    service.setUniqueId("pdf-to-html-service-id");
    return service;
  }
  
  @Test
  public void testTransform() throws Exception {
    LifecycleHelper.initAndStart(service);
    service.doService(message);
    LifecycleHelper.stopAndClose(service);
    
    assertTrue(message.getContent().contains("My"));
    assertTrue(message.getContent().contains("sample"));
    assertTrue(message.getContent().contains("pdf"));
    assertTrue(message.getContent().contains("content"));
  }
  
  @Test
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
  
  @Override
  public boolean isAnnotatedForJunit4() {
    return true;
  }  
}
