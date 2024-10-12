package pdfapp.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pdfapp.model.Invoice;
import pdfapp.service.InvoiceService;


 
@RestController
public class InvoiceController {
	
    @Autowired
    private InvoiceService invoiceService;

	@PostMapping("/api/invoice/generate")
	public ResponseEntity<?> generatePdf(@RequestBody Invoice request) {
	    try {
	        File pdfFile = invoiceService.generatePdf(request);

	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Disposition", "attachment; filename=" + pdfFile.getName());

	        return new ResponseEntity<>("PDF is generated in generated-pdfs folder inside Project ", 
	        		headers, HttpStatus.OK);
	    } catch (Exception e) {
	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while generating the PDF.");
	    }
	}

}