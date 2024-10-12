package pdfapp.service;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import pdfapp.model.Invoice;


@Service
public class InvoiceService {

    @Resource
    private TemplateEngine templateEngine;

    private final String pdfStorageDir = "generated-pdfs/";

    public File generatePdf(Invoice request) throws Exception {
        String pdfFileName = "invoice";

        Context context = new Context();
        context.setVariable("seller", request.getSeller());
        context.setVariable("sellerGstin", request.getSellerGstin());
        context.setVariable("sellerAddress", request.getSellerAddress());
        context.setVariable("buyer", request.getBuyer());
        context.setVariable("buyerGstin", request.getBuyerGstin());
        context.setVariable("buyerAddress", request.getBuyerAddress());
        context.setVariable("items", request.getItems());

        String htmlContent = templateEngine.process("invoice", context);
        File pdf = generatePdfFromHtml(htmlContent, pdfFileName);

        return pdf;

    }
    private File generatePdfFromHtml(String html, String fileName) throws Exception {
        Path outputPath = Paths.get(pdfStorageDir, fileName); // Create the path for the PDF
        Files.createDirectories(outputPath.getParent()); // Ensure the directory exists

        try (OutputStream os = Files.newOutputStream(outputPath)) { // Use Files.newOutputStream
            // Initialize Flying Saucer's ITextRenderer
            ITextRenderer renderer = new ITextRenderer();

            // Load the HTML content
            renderer.setDocumentFromString(html);

            // Layout and generate the PDF
            renderer.layout();
            renderer.createPDF(os);
        }

        return outputPath.toFile(); // Return the generated file
    }

}

