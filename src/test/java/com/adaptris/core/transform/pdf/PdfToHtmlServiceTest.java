package com.adaptris.core.transform.pdf;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;

import org.junit.jupiter.api.Test;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.Service;
import com.adaptris.core.util.LifecycleHelper;

public class PdfToHtmlServiceTest extends PdfToXServiceBase {

  private static String INPUT_KEY_2 = "PdfToHtmlService.inputPdf2";
  private static String OUTPUT_KEY = "PdfToHtmlService.outputHtml";

  private byte[] input2;
  private byte[] output;

  public PdfToHtmlServiceTest() throws Exception {
    super();

    if (PROPERTIES.getProperty(INPUT_KEY_2) != null) {
      try (FileInputStream fileInputStream = new FileInputStream(new File(PROPERTIES.getProperty(INPUT_KEY_2)))) {
        input2 = new byte[fileInputStream.available()];
        fileInputStream.read(input2);
      }
    }

    if (PROPERTIES.getProperty(OUTPUT_KEY) != null) {
      try (FileInputStream fileInputStream = new FileInputStream(new File(PROPERTIES.getProperty(OUTPUT_KEY)))) {
        output = new byte[fileInputStream.available()];
        fileInputStream.read(output);
      }
    }
  }

  @Override
  protected Service createPdfService() {
    return new PdfToHtmlService();
  }

  @Test
  public void testTransformMultiPage() throws Exception {
    Service service = createPdfService();
    AdaptrisMessage message = DefaultMessageFactory.getDefaultInstance().newMessage(input2);
    LifecycleHelper.initAndStart(service);
    service.doService(message);
    LifecycleHelper.stopAndClose(service);

    assertEquals(stripLineBreakChars(new String(output)), stripLineBreakChars(message.getContent()));
  }

  private String stripLineBreakChars(String str) {
    return str.replaceAll("\n|\r", "");
  }

}
