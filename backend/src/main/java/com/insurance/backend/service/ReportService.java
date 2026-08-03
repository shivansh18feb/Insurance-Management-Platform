package com.insurance.backend.service;

import java.io.ByteArrayInputStream;

public interface ReportService {
    ByteArrayInputStream generatePolicyReportPdf();
    ByteArrayInputStream generateClaimReportPdf();
    ByteArrayInputStream generateBusinessReportExcel();
}
