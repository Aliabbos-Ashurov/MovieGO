package com.abbos.moviego.controller;

import com.abbos.moviego.dto.SeatInfoDto;
import com.abbos.moviego.dto.TicketCreateDto;
import com.abbos.moviego.dto.internal.TicketRenderDto;
import com.abbos.moviego.service.PDFService;
import com.abbos.moviego.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

import static com.abbos.moviego.util.Constants.DASHBOARD_VIEW;
import static com.abbos.moviego.util.Constants.FRAGMENT_KEY;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-15
 */
@Controller
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final PDFService pdfService;

    @PostMapping
    @PreAuthorize("hasAuthority('BOOK_TICKET')")
    public String createTicket(@Valid @ModelAttribute TicketCreateDto dto, Model model) {
        ticketService.create(dto);
        return successPage(model);
    }

    @GetMapping("/{eventId}/seats")
    public String getSeatInfo(@PathVariable Long eventId, Model model) {
        SeatInfoDto seatInfo = ticketService.getSeatInfo(eventId);

        Set<String> takenSeatCoordinates = seatInfo.takenSeats().stream()
                .map(seat -> seat.row() + ":" + seat.column())
                .collect(Collectors.toSet());

        model.addAttribute("seatInfo", seatInfo);
        model.addAttribute("takenSeatCoordinates", takenSeatCoordinates);
        return "seats";
    }

    @GetMapping("/me")
    public String getMyTicket(Model model) {
        model.addAttribute("myTickets", ticketService.getMyTickets());
        model.addAttribute(FRAGMENT_KEY, "my-ticket.html");
        return DASHBOARD_VIEW;
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable Long id) {
        TicketRenderDto ticket = ticketService.findTicketForRender(id);
        byte[] pdf = pdfService.generateTicketPdf(ticket);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ticket_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }


    private String successPage(Model model) {
        model.addAttribute("message", "Success");
        return "common/success";
    }
}
