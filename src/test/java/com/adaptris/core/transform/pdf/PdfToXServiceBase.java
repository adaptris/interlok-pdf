package com.adaptris.core.transform.pdf;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.Service;
import com.adaptris.core.ServiceException;
import com.adaptris.core.util.LifecycleHelper;
import com.adaptris.interlok.junit.scaffolding.services.TransformServiceExample;

public abstract class PdfToXServiceBase extends TransformServiceExample {

  private static String INPUT_KEY = "PdfToHtmlService.inputPdf";

  private byte[] input;

  private Service service;

  private AdaptrisMessage message;

  public PdfToXServiceBase() throws Exception {
    super();

    if (PROPERTIES.getProperty(INPUT_KEY) != null) {
      try (FileInputStream fileInputStream = new FileInputStream(new File(PROPERTIES.getProperty(INPUT_KEY)))) {
        input = new byte[fileInputStream.available()];
        fileInputStream.read(input);
      }
    }
  }

  @BeforeEach
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

}
