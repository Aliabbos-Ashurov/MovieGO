package com.abbos.moviego.service.impl;

import com.abbos.moviego.dto.internal.TicketRenderDto;
import com.abbos.moviego.service.PDFService;
import com.abbos.moviego.service.QrService;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
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
            document.setMargins(15, 15, 15, 15);
            document.setBackgroundColor(WebColors.getRGBColor("#141414"));

            addHeader(document, ticket);
            addUserEmail(document, ticket);
            addDetailsTable(document, ticket);
            addPriceAndDivider(document, pdf, ticket);
            addQrCodeAndPoster(document, ticket);
            addFooter(document, ticket);

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate ticket PDF", e);
        }
    }

    private void addHeader(Document document, TicketRenderDto ticket) {
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{ 1 }))
                .useAllAvailableWidth()
                .setBackgroundColor(WebColors.getRGBColor("#1c1c1c"))
                .setBorder(new SolidBorder(WebColors.getRGBColor("#a100ff"), 2))
                .setMarginBottom(10);

        String titleText = "MovieGO" + (ticket.movieTitle() != null ? " - " + ticket.movieTitle() : "");
        Paragraph title = new Paragraph(titleText)
                .setFontSize(24)
                .setBold()
                .setFontColor(WebColors.getRGBColor("#f4f4f4"))
                .setTextAlignment(TextAlignment.CENTER)
                .setMargin(10);
        headerTable.addCell(new Cell().add(title).setBorder(null));
        document.add(headerTable);
    }

    private void addUserEmail(Document document, TicketRenderDto ticket) {
        Paragraph email = new Paragraph("Ticket for: " + ticket.userEmail())
                .setFontSize(12)
                .setFontColor(WebColors.getRGBColor("#d0d0d0"))
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginBottom(15);
        document.add(email);
    }

    private void addDetailsTable(Document document, TicketRenderDto ticket) {
        Table detailsTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .useAllAvailableWidth()
                .setBorder(new SolidBorder(WebColors.getRGBColor("#a100ff"), 1))
                .setMarginBottom(20);

        detailsTable.addCell(createCell("Date:", true));
        detailsTable.addCell(createCell(ticket.showTime().toString(), false));
        detailsTable.addCell(createCell("Cinema:", true));
        detailsTable.addCell(createCell(ticket.cinemaHallName(), false));
        detailsTable.addCell(createCell("Seat:", true));
        detailsTable.addCell(createCell("Row " + ticket.row() + ", Seat " + ticket.column(), false));
        detailsTable.addCell(createCell("Movie:", true));
        detailsTable.addCell(createCell(ticket.movieTitle() != null ? ticket.movieTitle() : "N/A", false));

        document.add(detailsTable);
    }

    private void addPriceAndDivider(Document document, PdfDocument pdf, TicketRenderDto ticket) {
        Paragraph price = new Paragraph("Price: $" + ticket.price())
                .setFontSize(16)
                .setBold()
                .setFontColor(WebColors.getRGBColor("#f4f4f4"))
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginBottom(10);
        document.add(price);

        PdfCanvas canvas = new PdfCanvas(pdf.getLastPage());
        float pageWidth = PageSize.A5.rotate().getWidth();
        float margin = 15;
        float yPosition = document.getRenderer().getCurrentArea().getBBox().getTop() - 10;
        canvas.saveState()
                .setFillColor(WebColors.getRGBColor("#a100ff"))
                .rectangle(margin, yPosition, (pageWidth - 2 * margin) / 2, 2)
                .fill()
                .setFillColor(WebColors.getRGBColor("#ff00d4"))
                .rectangle(margin + (pageWidth - 2 * margin) / 2, yPosition, (pageWidth - 2 * margin) / 2, 2)
                .fill()
                .restoreState();

        document.add(new Paragraph("").setMarginBottom(15));
    }

    private void addQrCodeAndPoster(Document document, TicketRenderDto ticket) {
        Table qrTable = new Table(UnitValue.createPercentArray(new float[]{ 1, 1 }))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        Image qrCodeImage = qrService.generateQrCodeImage(String.valueOf(ticket.id()))
                .setWidth(100)
                .setHeight(100)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setBorder(new SolidBorder(WebColors.getRGBColor("#a100ff"), 1));
        qrTable.addCell(new Cell().add(qrCodeImage).setBorder(null).setPadding(10));

        Cell posterCell = new Cell().setBorder(null).setPadding(10);
        qrTable.addCell(posterCell);

        document.add(qrTable);
    }

    private void addFooter(Document document, TicketRenderDto ticket) {
        Table footerTable = new Table(UnitValue.createPercentArray(new float[]{ 1 }))
                .useAllAvailableWidth()
                .setBackgroundColor(WebColors.getRGBColor("#1c1c1c"))
                .setBorder(new SolidBorder(WebColors.getRGBColor("#a100ff"), 1));

        Paragraph footer = new Paragraph("Ticket #" + ticket.id() + " | Enjoy your movie!")
                .setFontSize(10)
                .setFontColor(WebColors.getRGBColor("#d0d0d0"))
                .setTextAlignment(TextAlignment.CENTER)
                .setMargin(5);
        footerTable.addCell(new Cell().add(footer).setBorder(null));
        document.add(footerTable);
    }

    private Cell createCell(String content, boolean isHeader) {
        Cell cell = new Cell().add(new Paragraph(content).setFontSize(12));
        if (isHeader) {
            cell.setBold()
                    .setBackgroundColor(WebColors.getRGBColor("#2a2a2a"))
                    .setFontColor(WebColors.getRGBColor("#f4f4f4"))
                    .setBorder(new SolidBorder(WebColors.getRGBColor("#a100ff"), 1));
        } else {
            cell.setBackgroundColor(WebColors.getRGBColor("#1f1f1f"))
                    .setFontColor(WebColors.getRGBColor("#d0d0d0"));
        }
        cell.setPadding(8);
        return cell;
    }
}