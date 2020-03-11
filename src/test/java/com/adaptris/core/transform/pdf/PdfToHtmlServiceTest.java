package com.adaptris.core.transform.pdf;

import com.adaptris.core.Service;

public class PdfToHtmlServiceTest extends PdfToXServiceBase {
  
  public PdfToHtmlServiceTest() throws Exception {
    super();
  }

  @Override
  protected Service createPdfService() {
    return new PdfToHtmlService();
  }

  @Override
  public boolean isAnnotatedForJunit4() {
    return true;
  }
}
