package com.techspace.course_service.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.techspace.course_service.entity.Certificate;

public class CertificatePdfGenerator {

    // Canvas Dimensions
    private static final int WIDTH = 1152;
    private static final int HEIGHT = 650;

    // Premium Colors
    private static final Color COLOR_GOLD = new Color(184, 134, 11); // Dark Gold
    private static final Color COLOR_DARK_GREY = new Color(64, 64, 64); // Soft Black
    private static final Color COLOR_LIGHT_GREY = new Color(128, 128, 128);

    public static byte[] generatePdf(Certificate certificate) {
        try {
            Document document = new Document(new Rectangle(WIDTH, HEIGHT));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // 1. Background & Watermark (Layer 1)
            addBackgroundAndWatermark(document);

            // 2. Main Body Content (Layer 2)
            addPremiumBodyContent(document, certificate);

            // 3. Footer Elements (Logo, QR, Signature text) - Pinned to bottom
            addFooterElements(writer, certificate);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    private static void addBackgroundAndWatermark(Document document) {
        try {
            // Background
            Image background = Image.getInstance("C:\\Users\\srafi2\\Downloads\\background_image.png");
            background.scaleToFit(WIDTH, HEIGHT);
            background.setAbsolutePosition(0, 0);
            document.add(background);

            // Watermark
            File watermarkFile = new File("C:\\Users\\srafi2\\Downloads\\techspace-banner.png");
            if (watermarkFile.exists()) {
                BufferedImage original = ImageIO.read(watermarkFile);
                BufferedImage transparent = addOpacityToImage(original, 0.05f); // Very faint
                Image watermark = Image.getInstance(transparent, null);
                
                // Center exactly
                watermark.setAbsolutePosition(
                    (WIDTH - watermark.getScaledWidth()) / 2,
                    (HEIGHT - watermark.getScaledHeight()) / 2
                );
                document.add(watermark);
            }
        } catch (Exception e) {
            System.err.println("Background/Watermark error: " + e.getMessage());
        }
    }

    private static void addPremiumBodyContent(Document document, Certificate certificate) throws Exception {
        // --- Fonts ---
        // Using TIMES_ROMAN for headings gives a more "Official/Premium" look than Helvetica
        Font titleFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 55, COLOR_GOLD);
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA, 20, COLOR_LIGHT_GREY);
        Font nameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 45, COLOR_DARK_GREY);
        Font courseFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 35, COLOR_DARK_GREY);
        Font dateCodeFont = FontFactory.getFont(FontFactory.COURIER, 20, COLOR_LIGHT_GREY);

        // --- Layout ---
        
        // 1. Top Title
        Paragraph title = new Paragraph("CERTIFICATE OF COMPLETION", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(50);
        document.add(title);

        // 2. Elegant Separator Line
        LineSeparator line = new LineSeparator(1f, 77f, COLOR_GOLD, Element.ALIGN_CENTER, -5);
        document.add(new Paragraph(" ")); // spacer
        document.add(line);
        
        // 3. "This certifies that"
        Paragraph p1 = new Paragraph("This is to certify that", labelFont);
        p1.setAlignment(Element.ALIGN_CENTER);
        p1.setSpacingBefore(30);
        document.add(p1);

        // 4. Student Name
        Paragraph pName = new Paragraph(certificate.getUser().getName().toUpperCase(), nameFont);
        pName.setAlignment(Element.ALIGN_CENTER);
        pName.setSpacingBefore(10);
        document.add(pName);

        // 5. "Has successfully completed..."
        Paragraph p2 = new Paragraph("has successfully completed the comprehensive course", labelFont);
        p2.setAlignment(Element.ALIGN_CENTER);
        p2.setSpacingBefore(20);
        document.add(p2);

        // 6. Course Name
        Paragraph pCourse = new Paragraph(certificate.getCourse().getTitle(), courseFont);
        pCourse.setAlignment(Element.ALIGN_CENTER);
        pCourse.setSpacingBefore(10);
        document.add(pCourse);
        
        // 7. Dates and Codes (Small text below course)
        Paragraph pMeta = new Paragraph(
            "Issued: " + certificate.getIssuedAt() + "  |  Code: " + certificate.getCertificateCode(),
            dateCodeFont
        );
        pMeta.setAlignment(Element.ALIGN_CENTER);
        pMeta.setSpacingBefore(30);
        document.add(pMeta);
    }

    private static void addFooterElements(PdfWriter writer, Certificate certificate) {
        PdfContentByte cb = writer.getDirectContent();

        // --- 1. LOGO PLACEMENT ---
        // We place the logo at the absolute bottom center.
        try {
            Image logo = Image.getInstance("C:\\Users\\srafi2\\Downloads\\TechSpace_logo.png");
            // Resize logo to be slightly smaller for elegance (100x100 max)
            logo.scaleToFit(120, 120); 
            
            float logoW = logo.getScaledWidth();
            float logoH = logo.getScaledHeight();
            
            // Coordinates: Center X, Bottom Y (e.g., 60px from bottom)
            float logoX = (WIDTH - logoW) / 2;
            float logoY = 60; 
            
            logo.setAbsolutePosition(logoX, logoY);
            cb.addImage(logo);
            
            // "From TechSpace Academy" text BELOW logo
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12, COLOR_DARK_GREY);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, 
                new Paragraph("TechSpace Academy", footerFont), 
                WIDTH / 2, logoY - 15, 0); // 15px below logo
                
        } catch (Exception e) {
            System.err.println("Logo missing.");
        }

        // --- 2. QR CODE PLACEMENT ---
        // Bottom Left corner
     // --- 2. QR CODE PLACEMENT ---
     // Bottom Left corner
     try {
         String link = "http://localhost:8081/certificates/verify/" + certificate.getCertificateCode();
         BufferedImage qrRaw = generateQRCodeImage(link);
         Image qrImg = Image.getInstance(qrRaw, null);
         qrImg.scaleToFit(70, 70);
         qrImg.setAbsolutePosition(50, 40); // Left margin 50, Bottom 40
         cb.addImage(qrImg);
         
         // Adjust the Y-position to align "Scan to Verify" properly below the QR code
         Font verifyFont = FontFactory.getFont(FontFactory.HELVETICA, 8, COLOR_LIGHT_GREY);
         ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, 
             new Paragraph("Scan to Verify", verifyFont), 
             50, 20, 0); // Adjusted the Y-position here to be below the QR code (30px below QR code)
     } catch (Exception e) {
         System.err.println("QR Error.");
     }
        // --- 3. (Optional) SIGNATURE LINE ---
//        // Bottom Right for balance
//        try {
//             // Draw a line for signature
//             cb.setLineWidth(1f);
//             cb.setColorStroke(COLOR_LIGHT_GREY);
//             cb.moveTo(WIDTH - 250, 70);
//             cb.lineTo(WIDTH - 50, 70);
//             cb.stroke();
             
//             Font sigFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, COLOR_DARK_GREY);
//             ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, 
//                 new Paragraph("Director of Education", sigFont), 
////                 WIDTH - 150, 50, 0);
//        } catch (Exception e) {
//            // ignore
        }
    

    // --- Utilities ---
    
    private static BufferedImage generateQRCodeImage(String text) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 200, 200);
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 200; j++) {
                // If using dark background, switch these colors
                image.setRGB(i, j, matrix.get(i, j) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

    private static BufferedImage addOpacityToImage(BufferedImage image, float opacity) {
        BufferedImage newImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = newImg.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return newImg;
    }
}