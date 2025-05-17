package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.render.TicketRenderDto;
import com.abbos.moviego.service.PDFService;
import com.abbos.moviego.service.QrService;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PDFServiceImpl implements PDFService {

    private final QrService qrService;

    public PDFServiceImpl(QrService qrService) {
        this.qrService = qrService;
    }

    @Override
    public byte[] generateTicketPdf(TicketRenderDto ticket) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A5.rotate());
            document.setMargins(20, 20, 20, 20);

            addCompanyTitle(document);
            addUserEmail(document, ticket);
            addDetailsTable(document, ticket);
            addPrice(document, ticket);
            addQrCode(document, ticket);
            addFooter(document, ticket);

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate ticket PDF", e);
        }
    }

    private void addCompanyTitle(Document document) {
        Paragraph title = new Paragraph("MovieGO")
                .setFontSize(26)
                .setBold()
                .setFontColor(WebColors.getRGBColor("#a100ff"))
                .setMarginBottom(5);
        document.add(title);
    }

    private void addUserEmail(Document document, TicketRenderDto ticket) {
        Paragraph email = new Paragraph("Ticket for: " + ticket.userEmail())
                .setFontSize(14)
                .setItalic()
                .setFontColor(ColorConstants.BLACK)
                .setMarginBottom(15);
        document.add(email);
    }

    private void addDetailsTable(Document document, TicketRenderDto ticket) {
        Table detailsTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .useAllAvailableWidth();

        detailsTable.addCell(createCell("Date:", true));
        detailsTable.addCell(createCell(ticket.showTime().toString(), false));
        detailsTable.addCell(createCell("Cinema:", true));
        detailsTable.addCell(createCell(ticket.cinemaHallName(), false));
        detailsTable.addCell(createCell("Row/Column:", true));
        detailsTable.addCell(createCell(ticket.row() + " / " + ticket.column(), false));
        detailsTable.setMarginBottom(20);
        document.add(detailsTable);
    }

    private void addPrice(Document document, TicketRenderDto ticket) {
        Paragraph price = new Paragraph("Price: $" + ticket.price())
                .setFontSize(16)
                .setBold()
                .setFontColor(ColorConstants.BLACK)
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(price);
    }

    private void addQrCode(Document document, TicketRenderDto ticket) {
        Image qrCodeImage = qrService.generateQrCodeImage(String.valueOf(ticket.id()))
                .setWidth(120)
                .setHeight(120)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.add(qrCodeImage);
    }

    private void addFooter(Document document, TicketRenderDto ticket) {
        Paragraph footer = new Paragraph("Ticket #: " + ticket.id())
                .setFontSize(12)
                .setItalic()
                .setTextAlignment(TextAlignment.CENTER);
        document.add(footer);
    }

    private Cell createCell(String content, boolean isHeader) {
        Cell cell = new Cell().add(new Paragraph(content));
        if (isHeader) {
            cell.setBold();
            cell.setBackgroundColor(ColorConstants.WHITE);
            cell.setBorder(new SolidBorder(WebColors.getRGBColor("#a100ff"), 2));
        }
        cell.setPadding(5);
        return cell;
    }
}
