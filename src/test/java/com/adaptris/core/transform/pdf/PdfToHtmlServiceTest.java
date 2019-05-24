package com.adaptris.core.transform.pdf;

import com.adaptris.core.Service;

public class PdfToHtmlServiceTest extends PdfToXServiceBase {
  
  public PdfToHtmlServiceTest(String name) throws Exception {
    super(name);
  }

  @Override
  protected Service createPdfService() {
    return new PdfToHtmlService();
  }

}
