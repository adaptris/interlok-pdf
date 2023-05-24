package com.adaptris.core.transform.pdf;

import com.adaptris.core.Service;

public class PdfToTextServiceTest extends PdfToXServiceBase {
    
  public PdfToTextServiceTest() throws Exception {
    super();
  }

  @Override
  protected Service createPdfService() {
    return new PdfToTextService();
  }

}
