package com.abbos.moviego.service;

import com.itextpdf.layout.element.Image;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-16
 */
public interface QrService {

    Image generateQrCodeImage(String text);
}
