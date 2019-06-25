package com.adaptris.core.transform.pdf;

import com.adaptris.core.Service;

public class PdfToTextServiceTest extends PdfToXServiceBase {
    
  public PdfToTextServiceTest(String name) throws Exception {
    super(name);
  }

  @Override
  protected Service createPdfService() {
    return new PdfToTextService();
  }

}
