package com.insurance.backend.service.impl;

import com.insurance.backend.entity.Claim;
import com.insurance.backend.entity.Policy;
import com.insurance.backend.repository.ClaimRepository;
import com.insurance.backend.repository.PolicyRepository;
import com.insurance.backend.service.ReportService;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;

    @Override
    public ByteArrayInputStream generatePolicyReportPdf() {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            com.lowagie.text.Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Active & Total Insurance Policies Report", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);

            String[] headers = {"Policy No", "Name", "Type", "Coverage", "Premium", "Status"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            List<Policy> policies = policyRepository.findAll();
            for (Policy policy : policies) {
                table.addCell(policy.getPolicyNumber());
                table.addCell(policy.getPolicyName());
                table.addCell(policy.getPolicyType().name());
                table.addCell("₹" + policy.getCoverageAmount());
                table.addCell("₹" + policy.getPremiumAmount());
                table.addCell(policy.getStatus().name());
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream generateClaimReportPdf() {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            com.lowagie.text.Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Insurance Claims Settlement Report", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            String[] headers = {"Claim No", "Claim Amount", "Approved Amount", "Date", "Status"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            List<Claim> claims = claimRepository.findAll();
            for (Claim claim : claims) {
                table.addCell(claim.getClaimNumber());
                table.addCell("₹" + claim.getClaimAmount());
                table.addCell(claim.getApprovedAmount() != null ? "₹" + claim.getApprovedAmount() : "N/A");
                table.addCell(claim.getClaimDate().toString());
                table.addCell(claim.getStatus().name());
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream generateBusinessReportExcel() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Business Overview");

            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] columns = {"Policy Number", "Policy Name", "Type", "Coverage Amount", "Premium Amount", "Status"};
            for (int i = 0; i < columns.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            List<Policy> policies = policyRepository.findAll();
            int rowIdx = 1;
            for (Policy policy : policies) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(policy.getPolicyNumber());
                row.createCell(1).setCellValue(policy.getPolicyName());
                row.createCell(2).setCellValue(policy.getPolicyType().name());
                row.createCell(3).setCellValue(policy.getCoverageAmount().doubleValue());
                row.createCell(4).setCellValue(policy.getPremiumAmount().doubleValue());
                row.createCell(5).setCellValue(policy.getStatus().name());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return new ByteArrayInputStream(new byte[0]);
        }
    }
}
