package com.adaptris.core.transform.pdf;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.io.FileInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.interlok.junit.scaffolding.services.TransformServiceExample;
import com.adaptris.fs.OverwriteIfExistsWorker;

public class FopTransformServiceTest extends TransformServiceExample {

  private static String INPUT_KEY = "FopTransformService.inputMessage";
  private static String TMP_DIR = "FopTransformService.tmpDir";

  private byte[] input;

  private FopTransformService service;

  public FopTransformServiceTest() throws Exception {
    super();
  }

  @BeforeEach
  public void setUp() throws Exception {
    service = new FopTransformService();
    
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

  @Test
  public void testService() throws Exception {
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    msg.setPayload(input);
    execute(service, msg);
    assertTrue(msg.getContent().startsWith("%PDF-1.4"));
    writeToDisk(msg);
  }

  @Test
  public void testURI() throws Exception {
    FopTransformService myService = new FopTransformService();
    assertNotNull(myService.baseURI());
    myService.setBaseUri("http://localhost/");
    assertNotNull(myService.baseURI());
  }

  private void writeToDisk(AdaptrisMessage msg) throws Exception {
    File tmpDir = new File(PROPERTIES.getProperty(TMP_DIR)).getAbsoluteFile();
    File f = File.createTempFile("pdf", ".pdf", tmpDir);
    new OverwriteIfExistsWorker().put(msg.getPayload(), f);
    // Just for eyeballing, should look the same as sample_output.pdf
    System.err.println("File written to " + f.getCanonicalPath());

  }
  
  @Override
  protected Object retrieveObjectForSampleConfig() {
    return service;
  }
  
}
