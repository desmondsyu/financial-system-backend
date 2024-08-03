package com.vitalysukhinin.financial_system.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.vitalysukhinin.financial_system.entities.Transaction;
import com.vitalysukhinin.financial_system.entities.User;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfGenerationService {
    public byte[] generateUserTransactionPdf(User user, List<Transaction> transactions) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph(new Text("User Information").setBold()));
            document.add(new Paragraph("Name: " + user.getUsername()));
            document.add(new Paragraph("Email: " + user.getEmail()));
            document.add(new Paragraph(""));

            document.add(new Paragraph(new Text("Transactions").setBold()));
            float[] pointColumnWidths = {150F, 150F, 150F, 150F};
            Table table = new Table(pointColumnWidths);
            table.addCell("Date");
            table.addCell("Description");
            table.addCell("Amount");
            table.addCell("Balance");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

            for (Transaction transaction : transactions) {
                table.addCell(transaction.getTransactionDate().format(formatter));
                table.addCell(transaction.getDescription());
                table.addCell(transaction.getAmount().toString());
                table.addCell(transaction.getBalance().toString());
            }

            document.add(table);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
