package com.epiclancers.pdfdocument;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreatePdfNew {


    public static String pdfFilePath;
    public static int PDF_WIDTH = 170;

    public Boolean generete(String name){

        pdfFilePath = Environment.getExternalStorageDirectory() + "/PDF/"+name+".pdf";

        //askForPermission(context , activity);
        createFolder();

        try {
            createPdf(pdfFilePath);
            //createPdfWithTable(pdfFilePath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    public void createFolder(){
        File file = new File(Environment.getExternalStorageDirectory() , "PDF");
        file.mkdir();
    }

    // Adds new Line Break
    public void lineBreak(Document document) throws DocumentException {
        document.add(new Paragraph("\n"));
    }

    // Add Tables in the PDF after the title
    // *************** IMPORTANT  **************

    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.setMargins(0,0,0,10);
        writer.setPdfVersion(PdfWriter.VERSION_1_5);
        document.open();
        addTitle(document);
        createTopTable(document);
        businessNameSection(document);
        addEmailSection(document);
        buyerSection(document);
        createPdfWithTable(document);
        addBottomFirst(document);
        grandTotal(document);
        //addBottomSecond(document);
        addThirdLastSection(document);
        thankYouNoteByBusiness(document);
        thankYouNoteByApp(document);
        document.close();
    }

    // *************** IMPORTANT  **************


    //  Adds Business Name to the PDF File
    public void addTitle(Document document){
        Font chapterFont = FontFactory.getFont(FontFactory.defaultEncoding, 25, Font.BOLD );
        Paragraph chunk = new Paragraph("Sri Banni Enterprises", chapterFont);
        chunk.setAlignment(Element.ALIGN_CENTER);
        try {
            document.add(chunk);
            lineBreak(document);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    // Adds Title TAX INVOICE to the PDF File
    public void createTopTable(Document document) throws DocumentException {
        PdfPTable topTable = new PdfPTable(1);
        topTable.setTotalWidth(Utilities.millimetersToPoints(PDF_WIDTH));
        topTable.setLockedWidth(true);
        PdfPCell pdfPCell;
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 15, Font.BOLD);
        Phrase phrase = new Phrase("Invoice" , font);
        pdfPCell = new PdfPCell(phrase);
        pdfPCell.setColspan(1);
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setPaddingBottom(5);
        topTable.addCell(pdfPCell);
        document.add(topTable);
    }


    // ************* CODE FOR BUSINESS NAME SECTION *******************

    // Add Second Section
    public void businessNameSection(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(Utilities.millimetersToPoints(PDF_WIDTH));
        table.setLockedWidth(true);
        table.setWidths(new int[]{6,3});
        PdfPCell cell;
        cell = new PdfPCell();
        cell.setPaddingLeft(10);
        cell.setPaddingBottom(10);
        //cell.addElement(contentLeftSecondSection());
        businessName(cell);
        cell.setRowspan(7);
        table.addCell(cell);
        table.addCell( afterBusinessPhoneNo() );
        document.add(table);
    }

    // Create content left of Second section
    public void businessName(PdfPCell contentLeft){
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.BOLD);
        contentLeft.addElement(new Phrase("{Business Name} " , font) );
        contentLeft.addElement(new Phrase("{Business Address}"));

    }

    public PdfPTable afterBusinessPhoneNo(){
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 10, Font.BOLD);
        PdfPTable table = new PdfPTable(1);
        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.addElement(new Phrase("Phone No : ",font));
        invoiceCell.addElement(new Phrase("GSTIN : ",font));
        //invoiceCell.setBorderColor(BaseColor.WHITE);
        invoiceCell.disableBorderSide(Rectangle.LEFT);
        invoiceCell.disableBorderSide(Rectangle.RIGHT);
        invoiceCell.disableBorderSide(Rectangle.TOP);
        invoiceCell.disableBorderSide(Rectangle.BOTTOM);
        invoiceCell.setPaddingBottom(5);
        invoiceCell.setPaddingLeft(5);
        table.addCell(invoiceCell);
        return table;
    }

    // ************* CODE FOR EMAIL SECTION *******************

    // Add Business Email Section
    public void addEmailSection(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setTotalWidth(Utilities.millimetersToPoints(PDF_WIDTH));
        table.setLockedWidth(true);
        table.setWidths(new int[]{9,3,6});
        PdfPCell cell;
        cell = new PdfPCell();
        cell.setPaddingLeft(5);
        cell.setPaddingBottom(5);
        //cell.addElement(contentLeftSecondSection());
        businessEmail(cell);
        cell.setRowspan(7);
        table.addCell(cell);
        table.addCell( date() );
        table.addCell( invoiceNo() );
        document.add(table);
    }


    // Create content left of Second section
    public void businessEmail(PdfPCell contentLeft){
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.BOLD);
        contentLeft.addElement(new Phrase("{Business Email} " , font) );
        contentLeft.addElement(new Phrase("{Business Store Web Address}"));

    }

    public PdfPTable date(){
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 10, Font.BOLD);
        PdfPTable table = new PdfPTable(1);
        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.addElement(new Phrase("Date : ",font));
        //invoiceCell.setBorderColor(BaseColor.WHITE);
        invoiceCell.setPaddingBottom(5);
        invoiceCell.setPaddingLeft(5);
        table.addCell(invoiceCell);
        table.addCell(new Phrase("17/08/2019",font));
        return table;
    }

    public PdfPTable invoiceNo(){
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 10, Font.BOLD);
        PdfPTable table = new PdfPTable(1);
        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.addElement(new Phrase("Invoice No : ",font));
        invoiceCell.addElement(new Phrase("IN_234234",font));
        invoiceCell.setPaddingLeft(5);
        invoiceCell.setPaddingBottom(5);
        //invoiceCell.setBorderColor(BaseColor.WHITE);
        invoiceCell.disableBorderSide(Rectangle.LEFT);
        invoiceCell.disableBorderSide(Rectangle.RIGHT);
        invoiceCell.disableBorderSide(Rectangle.TOP);
        invoiceCell.disableBorderSide(Rectangle.BOTTOM);
        invoiceCell.setPaddingBottom(5);
        invoiceCell.setPaddingLeft(5);
        table.addCell(invoiceCell);
        return table;
    }


    // ************* CODE FOR BUYER SECTION *****************

    // Add Third Section
    public void buyerSection(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(Utilities.millimetersToPoints(PDF_WIDTH));
        table.setLockedWidth(true);
        table.setWidths(new int[]{5,4});
        PdfPCell cell;
        cell = new PdfPCell();
        cell.setPaddingLeft(10);
        cell.setPaddingBottom(10);
        //cell.addElement(contentLeftSecondSection());
        addBuyerInfo(cell);
        cell.setRowspan(7);
        table.addCell(cell);
        table.addCell( cashBackCode() );
        table.addCell( bankDetailsTitle() );
        table.addCell( bankDetails() );

        document.add(table);
    }

    // Create content left of Third section
    public void addBuyerInfo(PdfPCell contentLeft){
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.BOLD);
        contentLeft.addElement(new Phrase("Buyer: " , font) );
        contentLeft.addElement(new Phrase("{Customer Name}"));
        contentLeft.addElement(new Phrase("{Customer Address}"));
        contentLeft.addElement(new Phrase("{Customer Phone No.} "));
        contentLeft.addElement(new Phrase("{Customer GSTIN}"));

    }

    public PdfPTable cashBackCode(){
        Font fontnormal = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.NORMAL , BaseColor.BLACK );
        Font fontnormal2 = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.NORMAL , new BaseColor(50,39,87,255));
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.setBackgroundColor(BaseColor.GRAY);
        invoiceCell.addElement( new Phrase("CASHBACK CODE: ",fontnormal)  );
        //invoiceCell.setBorderColor(BaseColor.WHITE);
        invoiceCell.disableBorderSide(Rectangle.LEFT);
        invoiceCell.disableBorderSide(Rectangle.RIGHT);
        invoiceCell.disableBorderSide(Rectangle.TOP);
        invoiceCell.disableBorderSide(Rectangle.BOTTOM);
        invoiceCell.setPaddingBottom(5);
        invoiceCell.setPaddingLeft(5);
        table.addCell(invoiceCell);

        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.addElement( new Phrase("ADFGVCD5210 (10%)",fontnormal2) );
        //invoiceCell.setBorderColor(BaseColor.WHITE);
        pdfPCell.disableBorderSide(Rectangle.LEFT);
        pdfPCell.disableBorderSide(Rectangle.RIGHT);
        pdfPCell.disableBorderSide(Rectangle.TOP);
        pdfPCell.disableBorderSide(Rectangle.BOTTOM);
        pdfPCell.setPaddingBottom(5);
        pdfPCell.setPaddingLeft(5);
        table.addCell(pdfPCell);
        return table;
    }


    public PdfPTable bankDetailsTitle(){
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        PdfPTable table = new PdfPTable(1);
        PdfPCell invoiceCell = new PdfPCell(new Phrase("Bank Details: ",font));
        //invoiceCell.setBorderColor(BaseColor.WHITE);
        invoiceCell.disableBorderSide(Rectangle.LEFT);
        invoiceCell.disableBorderSide(Rectangle.RIGHT);
        invoiceCell.disableBorderSide(Rectangle.TOP);
        invoiceCell.disableBorderSide(Rectangle.BOTTOM);
        invoiceCell.setPaddingBottom(5);
        invoiceCell.setPaddingLeft(5);
        table.addCell(invoiceCell);
        return table;
    }

    public PdfPTable bankDetails(){
        Font fontnormal = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.NORMAL);
        PdfPTable table = new PdfPTable(1);
        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.addElement( new Phrase("Name of Account: Rohan Enterprises",fontnormal) );
        invoiceCell.addElement( new Phrase("A/c No : 875 214 5784 1452",fontnormal) );
        invoiceCell.addElement( new Phrase("IFSC: SBIN0005478",fontnormal) );
        //invoiceCell.setBorderColor(BaseColor.WHITE);
        invoiceCell.disableBorderSide(Rectangle.LEFT);
        invoiceCell.disableBorderSide(Rectangle.RIGHT);
        invoiceCell.disableBorderSide(Rectangle.TOP);
        invoiceCell.disableBorderSide(Rectangle.BOTTOM);
        invoiceCell.setPaddingBottom(5);
        invoiceCell.setPaddingLeft(5);
        table.addCell(invoiceCell);
        return table;
    }


    // **************** CODE FOR INVOICE ITEMS TABLE  ****************

    public PdfPTable invoiceHeader() throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        PdfPTable table = new PdfPTable(7);
        table.setTotalWidth( Utilities.millimetersToPoints(PDF_WIDTH) );
        table.setLockedWidth( true );
        //table.setWidths(new int[]{1,5,2,1,1,1,2,2,2});
        //table.setWidths(new int[]{2,10,4,2,2,2,4,4,4});
        table.setWidths(new int[]{2,12,4,2,2,4,4});

        PdfPCell serialno = new PdfPCell();
        serialno.addElement(new Phrase("S/No",font));
        //serialno.setBorderColor(BaseColor.WHITE);
        serialno.disableBorderSide(Rectangle.LEFT);
        serialno.disableBorderSide(Rectangle.TOP);
        serialno.disableBorderSide(Rectangle.BOTTOM);
        serialno.setPaddingBottom(5);
        serialno.setPaddingLeft(5);
        table.addCell(serialno);

        PdfPCell description = new PdfPCell();
        description.addElement(new Phrase("Description",font));
        //description.setBorderColor(BaseColor.WHITE);
        //description.disableBorderSide(Rectangle.RIGHT);
        description.disableBorderSide(Rectangle.TOP);
        description.disableBorderSide(Rectangle.BOTTOM);
        description.setPaddingBottom(5);
        description.setPaddingLeft(5);
        table.addCell(description);

        PdfPCell hsncode = new PdfPCell();
        hsncode.addElement(new Phrase("HSN Code",font));
        //hsncode.setBorderColor(BaseColor.WHITE);
        //hsncode.disableBorderSide(Rectangle.RIGHT);
        hsncode.disableBorderSide(Rectangle.TOP);
        hsncode.disableBorderSide(Rectangle.BOTTOM);
        hsncode.setPaddingBottom(5);
        hsncode.setPaddingLeft(5);
        table.addCell(hsncode);


        PdfPCell quantity = new PdfPCell();
        quantity.addElement(new Phrase("Qty",font));
        //quantity.setBorderColor(BaseColor.WHITE);
        //quantity.disableBorderSide(Rectangle.RIGHT);
        quantity.disableBorderSide(Rectangle.TOP);
        quantity.disableBorderSide(Rectangle.BOTTOM);
        quantity.setPaddingBottom(5);
        quantity.setPaddingLeft(5);
        table.addCell(quantity);

        PdfPCell unit = new PdfPCell();
        unit.addElement(new Phrase("Unit",font));
        //unit.setBorderColor(BaseColor.WHITE);
        //unit.disableBorderSide(Rectangle.RIGHT);
        unit.disableBorderSide(Rectangle.TOP);
        unit.disableBorderSide(Rectangle.BOTTOM);
        unit.setPaddingBottom(5);
        unit.setPaddingLeft(5);
        table.addCell(unit);

        PdfPCell rate = new PdfPCell();
        rate.addElement(new Phrase("Rate",font));
        //rate.setBorderColor(BaseColor.WHITE);
        //rate.disableBorderSide(Rectangle.RIGHT);
        rate.disableBorderSide(Rectangle.TOP);
        rate.disableBorderSide(Rectangle.BOTTOM);
        rate.setPaddingBottom(5);
        rate.setPaddingLeft(5);
        table.addCell(rate);

        PdfPCell amount = new PdfPCell();
        amount.addElement(new Phrase("Amount",font));
        //amount.setBorderColor(BaseColor.WHITE);
        //amount.disableBorderSide(Rectangle.RIGHT);
        amount.disableBorderSide(Rectangle.TOP);
        amount.disableBorderSide(Rectangle.BOTTOM);
        amount.setPaddingBottom(5);
        amount.setPaddingLeft(5);
        table.addCell(amount);


        return table;
    }

    // Returns the number of items in the sale
    public int getSaleItemCount(String saleid){

        return 5;
    }

    // Add Tables in the PDF after the title
    public void createPdfWithTable(Document document) throws IOException, DocumentException {
        try {
            PdfPTable pdfPTable = new PdfPTable(7);
            pdfPTable.setTotalWidth( Utilities.millimetersToPoints(PDF_WIDTH) );
            pdfPTable.setLockedWidth( true );
            //pdfPTable.setWidths(new int[]{2,10,4,2,2,2,4,4,4});
            pdfPTable.setWidths(new int[]{2,12,4,2,2,4,4});
            PdfPCell pdfPCell = new PdfPCell(invoiceHeader());
            pdfPCell.setBackgroundColor(BaseColor.GRAY);
            pdfPCell.setColspan(9);
            pdfPTable.addCell(pdfPCell);
            pdfPTable.setHeaderRows(1);
            for (int i = 0 ; i < getSaleItemCount("abc") ; i++){
                int i1 = 40;
                pdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                pdfPTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
                pdfPTable.addCell((i+1)+".");
                pdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfPTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
                pdfPTable.addCell("Horliks");
                pdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                pdfPTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
                pdfPTable.addCell("3256");
                pdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
                pdfPTable.addCell(i+"");
                pdfPTable.addCell("Kg");
                pdfPTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                pdfPTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
                pdfPTable.addCell(i1+"");
                pdfPTable.addCell((i+1)*i1+"");
            }


            document.add(pdfPTable);

        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        }

    }


    // **************** CODE FOR INVOICE ITEMS TABLE  ****************

    // BOTTOM FIRST SECTION
    public void addBottomFirst(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(Utilities.millimetersToPoints(PDF_WIDTH));
        table.setLockedWidth(true);
        table.setWidths(new int[]{6,3});
        PdfPCell cell;
        cell = new PdfPCell();
        cell.setPaddingLeft(10);
        cell.setPaddingBottom(10);
        //cell.addElement(contentLeftSecondSection());
        // Add Empty space
        addEmptyCells(cell);
        cell.setRowspan(7);
        table.addCell(cell);
        // Taxable Amount Adding to PDF
        table.addCell( taxableAmount() );
        table.addCell( allThreeTaxes() );
        table.addCell( discount() );
        document.add(table);
    }


    // Adding Taxable Amount
    public PdfPTable taxableAmount() throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        PdfPTable table = new PdfPTable(1);
        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.addElement(new Phrase("Total Taxable Amount: 4500",font));
        //invoiceCell.setBorderColor(BaseColor.WHITE);
        invoiceCell.disableBorderSide(Rectangle.LEFT);
        invoiceCell.disableBorderSide(Rectangle.RIGHT);
        invoiceCell.disableBorderSide(Rectangle.TOP);
        invoiceCell.disableBorderSide(Rectangle.BOTTOM);
        invoiceCell.setPaddingBottom(5);
        invoiceCell.setPaddingLeft(5);
        table.addCell(invoiceCell);
        return table;
    }


    // Create content left of Second section
    public void addEmptyCells(PdfPCell contentLeft){
        Font fontnormal = FontFactory.getFont(FontFactory.defaultEncoding, 11, Font.NORMAL);
        Font fontBold = FontFactory.getFont(FontFactory.defaultEncoding, 13, Font.BOLD);
        contentLeft.addElement(new Phrase("Declaration : " , fontBold));
        contentLeft.addElement( new Phrase("1. Goods sold can't be taken back or exchanged. ",fontnormal) );
        contentLeft.addElement( new Phrase("2. Our risk & responsibilty ceases as good leaves Godown.",fontnormal) );
        contentLeft.addElement( new Phrase("3. Subject to {City} Jurisdiction.",fontnormal) );
        contentLeft.addElement(new Phrase("\n"));

    }

    // Four Column Table containing Taxes amount
    public PdfPTable allThreeTaxes() throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        PdfPTable table = new PdfPTable(4);
        table.setWidths(new int[]{2,2,2,2});

        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.addElement(new Phrase("",font));
        //invoiceCell.setBorderColor(BaseColor.WHITE);
        invoiceCell.disableBorderSide(Rectangle.LEFT);
        invoiceCell.disableBorderSide(Rectangle.RIGHT);
        invoiceCell.disableBorderSide(Rectangle.TOP);
        invoiceCell.disableBorderSide(Rectangle.BOTTOM);
        invoiceCell.setPaddingBottom(5);
        invoiceCell.setPaddingLeft(5);
        table.addCell(invoiceCell);

        PdfPCell paymentCell = new PdfPCell();
        paymentCell.addElement(new Phrase("IGST:",font));
        paymentCell.addElement(new Phrase("SGST:",font));
        paymentCell.addElement(new Phrase("CGST:",font));
        //paymentCell.setBorderColor(BaseColor.WHITE);
        //paymentCell.disableBorderSide(Rectangle.LEFT);
        paymentCell.disableBorderSide(Rectangle.RIGHT);
        paymentCell.disableBorderSide(Rectangle.TOP);
        paymentCell.disableBorderSide(Rectangle.BOTTOM);
        paymentCell.setPaddingBottom(5);
        paymentCell.setPaddingLeft(5);
        table.addCell(paymentCell);

        PdfPCell po = new PdfPCell();
        po.addElement(new Phrase("",font));
        //invoiceCell.setBorderColor(BaseColor.WHITE);
        //po.disableBorderSide(Rectangle.LEFT);
        po.disableBorderSide(Rectangle.RIGHT);
        po.disableBorderSide(Rectangle.TOP);
        po.disableBorderSide(Rectangle.BOTTOM);
        po.setPaddingBottom(5);
        po.setPaddingLeft(5);
        table.addCell(po);

        PdfPCell io = new PdfPCell();
        io.addElement(new Phrase("",font));
        //paymentCell.setBorderColor(BaseColor.WHITE);
        //paymentCell.disableBorderSide(Rectangle.LEFT);
        io.disableBorderSide(Rectangle.RIGHT);
        io.disableBorderSide(Rectangle.TOP);
        io.disableBorderSide(Rectangle.BOTTOM);
        io.setPaddingBottom(5);
        io.setPaddingLeft(5);
        table.addCell(io);
        return table;
    }

    // CB Discount
    public PdfPTable discount() throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        PdfPTable table = new PdfPTable(2);
        table.setWidths(new int[]{3,1});
        PdfPCell invoiceCell = new PdfPCell(new Phrase("Discount:",font));
        //invoiceCell.setBorderColor(BaseColor.WHITE);
        invoiceCell.disableBorderSide(Rectangle.LEFT);
        invoiceCell.disableBorderSide(Rectangle.TOP);
        invoiceCell.disableBorderSide(Rectangle.BOTTOM);
        table.addCell(invoiceCell);

        PdfPCell paymentCell = new PdfPCell();
        paymentCell.addElement(new Phrase("500",font));
        paymentCell.setPaddingBottom(5);
        paymentCell.setPaddingLeft(5);
        //paymentCell.setBorderColor(BaseColor.WHITE);
        paymentCell.disableBorderSide(Rectangle.RIGHT);
        paymentCell.disableBorderSide(Rectangle.TOP);
        paymentCell.disableBorderSide(Rectangle.BOTTOM);
        table.addCell(paymentCell);

        return table;
    }


    // **************** CODE FOR GRAND TOTAL BELOW THE DECLARATION ITEMS TABLE  ****************


    public void grandTotal(Document document) throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD );
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(Utilities.millimetersToPoints(PDF_WIDTH));
        table.setLockedWidth(true);
        table.setWidths(new int[]{6,3});
        PdfPCell invoiceCell = new PdfPCell(new Phrase("",font));
        table.addCell(invoiceCell);

        PdfPCell paymentCell = new PdfPCell();
        paymentCell.addElement(grandTotalAmount());
        table.addCell(paymentCell);

        document.add(table);
    }


    // Grand Total Amount
    public PdfPTable grandTotalAmount() throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        PdfPTable table = new PdfPTable(2);
        table.setWidths(new int[]{8,2});
        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.addElement(new Phrase("Grand Total Amount:",font));
        invoiceCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        //invoiceCell.setBorderColor(BaseColor.WHITE);
        invoiceCell.disableBorderSide(Rectangle.LEFT);
        //invoiceCell.disableBorderSide(Rectangle.RIGHT);
        invoiceCell.disableBorderSide(Rectangle.TOP);
        invoiceCell.disableBorderSide(Rectangle.BOTTOM);
        table.addCell(invoiceCell);

        PdfPCell paymentCell = new PdfPCell();
        paymentCell.addElement(new Phrase("1484",font));
        //paymentCell.setBorderColor(BaseColor.WHITE);
        //paymentCell.disableBorderSide(Rectangle.LEFT);
        paymentCell.disableBorderSide(Rectangle.RIGHT);
        paymentCell.disableBorderSide(Rectangle.TOP);
        paymentCell.disableBorderSide(Rectangle.BOTTOM);
        table.addCell(paymentCell);
        return table;
    }

    // BOTTOM SECOND SECTION

    // **************** CODE FOR LAST BOTTOM SECTION  ****************

    // Return new Table of Amount In Words

    public PdfPTable amountInWords() throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 11, Font.BOLD);
        Font fontNormal = FontFactory.getFont(FontFactory.defaultEncoding, 11, Font.NORMAL);
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell;
        cell = new PdfPCell();
        cell.addElement( new Paragraph("Amount in Words : " , font) );
        cell.addElement( new Paragraph( "One thousand four hundred eighty-four"  , fontNormal) );
        cell.setPaddingLeft(5);
        cell.setPaddingBottom(5);
        cell.setRowspan(2);
        cell.disableBorderSide(Rectangle.TOP);
        cell.disableBorderSide(Rectangle.LEFT);
        cell.disableBorderSide(Rectangle.RIGHT);
        table.addCell(cell);
        return table;
    }

    public PdfPTable paymentMethod() throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 9, Font.BOLD);
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell;
        cell = new PdfPCell();
        cell.addElement(new Phrase("Payment Method : Cash" , font));
        cell.setPaddingLeft(5);
        cell.setPaddingBottom(5);
        cell.disableBorderSide(Rectangle.LEFT);
        cell.disableBorderSide(Rectangle.RIGHT);
        table.addCell(cell);
        return table;
    }


    public PdfPTable cashcredit() throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{4,4});
        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.addElement(new Phrase("Cash: 450",font));
        invoiceCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        invoiceCell.disableBorderSide(Rectangle.LEFT);
        invoiceCell.disableBorderSide(Rectangle.TOP);
        invoiceCell.disableBorderSide(Rectangle.BOTTOM);
        invoiceCell.setPaddingLeft(5);
        invoiceCell.setPaddingBottom(5);
        table.addCell(invoiceCell);

        PdfPCell paymentCell = new PdfPCell();
        paymentCell.addElement(new Phrase("Credit : 950",font));
        paymentCell.disableBorderSide(Rectangle.RIGHT);
        paymentCell.disableBorderSide(Rectangle.TOP);
        paymentCell.disableBorderSide(Rectangle.BOTTOM);
        paymentCell.setPaddingLeft(5);
        paymentCell.setPaddingBottom(5);
        table.addCell(paymentCell);
        return table;
    }

    // BOTTOM THIRD SECTION

    public void addThirdLastSection(Document document) throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(Utilities.millimetersToPoints(PDF_WIDTH));
        table.setLockedWidth(true);
        table.setWidths(new int[]{6,3});
        PdfPCell cell;
        cell = new PdfPCell();
        //cell.addElement(contentLeftSecondSection());
        cell.addElement(amountInWords());
        cell.addElement(paymentMethod());
        cell.addElement(cashcredit());
        cell.setRowspan(2);
        table.addCell(cell);
        PdfPCell pdfPCell = new PdfPCell();
        Paragraph paragraph = new Paragraph(" For {Store} Name\n \n",font);
        paragraph.setIndentationLeft(5);
        pdfPCell.addElement(paragraph);
        paragraph = new Paragraph("\n \n \n Authorized Signatory",font);
        paragraph.setIndentationLeft(30);
        pdfPCell.addElement(paragraph);
        pdfPCell.setPaddingBottom(5);
        pdfPCell.setPaddingLeft(5);
        table.addCell( pdfPCell );
        document.add(table);
    }

    public void thankYouNoteByBusiness(Document document) throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(Utilities.millimetersToPoints(PDF_WIDTH));
        table.setLockedWidth(true);
        PdfPCell invoiceCell = new PdfPCell(new Phrase("Thank You for the Business :) ",font));
        invoiceCell.setVerticalAlignment(Element.ALIGN_CENTER);
        invoiceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        invoiceCell.setPaddingBottom(5);
        invoiceCell.setPaddingLeft(5);
        table.addCell(invoiceCell);
        document.add(table);
    }

    public void thankYouNoteByApp(Document document) throws DocumentException {

        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(Utilities.millimetersToPoints(PDF_WIDTH));
        table.setLockedWidth(true);


        PdfPCell invoiceCell3 = new PdfPCell(new Phrase("\n",font));
        invoiceCell3.disableBorderSide(Rectangle.LEFT);
        invoiceCell3.disableBorderSide(Rectangle.RIGHT);
        invoiceCell3.disableBorderSide(Rectangle.TOP);
        invoiceCell3.disableBorderSide(Rectangle.BOTTOM);

        PdfPCell invoiceCell = new PdfPCell(new Paragraph(" Invoice Made with \u2764 by Billing App",font));
        invoiceCell.disableBorderSide(Rectangle.LEFT);
        invoiceCell.disableBorderSide(Rectangle.RIGHT);
        invoiceCell.disableBorderSide(Rectangle.TOP);
        invoiceCell.disableBorderSide(Rectangle.BOTTOM);

        invoiceCell.setVerticalAlignment(Element.ALIGN_CENTER);
        invoiceCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        invoiceCell.setPaddingBottom(5);
        invoiceCell.setPaddingLeft(5);

        table.addCell(invoiceCell3);
        table.addCell(invoiceCell);

        document.add(table);
    }

}
