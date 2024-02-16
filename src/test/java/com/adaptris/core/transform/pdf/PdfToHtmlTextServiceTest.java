package com.adaptris.core.transform.pdf;

import com.adaptris.core.Service;

public class PdfToHtmlTextServiceTest extends PdfToXServiceBase {

  public PdfToHtmlTextServiceTest() throws Exception {
    super();
  }

  @Override
  protected Service createPdfService() {
    return new PdfToHtmlService();
  }

}
