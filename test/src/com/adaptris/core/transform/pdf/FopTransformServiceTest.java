package com.adaptris.core.transform.pdf;

import java.io.File;
import java.io.FileInputStream;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.transform.TransformServiceExample;

public class FopTransformServiceTest extends TransformServiceExample {

  private static String INPUT_KEY = "FopTransformService.inputMessage";
  private static String OUTPUT_KEY = "FopTransformService.outputMessage";

  private byte[] input;

  private FopTransformService service;

  public FopTransformServiceTest(String name) throws Exception {
    super(name);

    if (PROPERTIES.getProperty(INPUT_KEY) != null) {
      FileInputStream fileInputStream = new FileInputStream(new File(PROPERTIES
          .getProperty(INPUT_KEY)));

      input = new byte[fileInputStream.available()];
      fileInputStream.read(input);
    }
  }

  @Override
  protected void setUp() throws Exception {
    service = new FopTransformService();
  }

  public void testService() throws Exception {
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    msg.setPayload(input);
    execute(service, msg);
    assertTrue(msg.getStringPayload().startsWith("%PDF-1.4"));
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    return service;
  }
}
