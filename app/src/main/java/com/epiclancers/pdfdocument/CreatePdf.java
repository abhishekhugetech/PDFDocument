package com.epiclancers.pdfdocument;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

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
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vdurmont.emoji.EmojiParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreatePdf {


    public static String pdfFilePath;
    public static int PDF_WIDTH = 170;

    public Boolean generete(Context context, String name){

        pdfFilePath = Environment.getExternalStorageDirectory() + "/PDF/"+name+".pdf";

        //askForPermission(context , activity);
        createFolder();

        try {
            createPdf(pdfFilePath);
            //createPdfWithTable(pdfFilePath);
            Toast.makeText(context, "PDF Created Successfully", Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    public void askForPermission(Context context , Activity activity){

        if(ContextCompat.checkSelfPermission(context , Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ){

            ActivityCompat.requestPermissions(activity , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE} , 1 );

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
        document.setMargins(0,0,0,20);
        writer.setPdfVersion(PdfWriter.VERSION_1_5);
        document.open();
        addTitle(document);
        createTopTable(document);
        addSecondSection(document);
        addThirdSection(document);
        createPdfWithTable(document);
        addBottomFirst(document);
        cbDiscountBelow(document);
        addBottomSecond(document);
        addThirdLastSection(document);
        thankYouNoteByBusiness(document);
        thankYouNoteByApp(document);
        document.close();
    }

    // *************** IMPORTANT  **************


    // Adds Title TAX INVOICE to the PDF File
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

    // Add Top Table
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

    // Add Second Section
    public void addSecondSection(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(Utilities.millimetersToPoints(PDF_WIDTH));
        table.setLockedWidth(true);
        table.setWidths(new int[]{5,4});
        PdfPCell cell;
        cell = new PdfPCell();
        cell.setPaddingLeft(10);
        cell.setPaddingBottom(10);
        //cell.addElement(contentLeftSecondSection());
        addCells(cell);
        cell.setRowspan(7);
        table.addCell(cell);
        table.addCell( invoice() );
        table.addCell( afterInvoiceEmpty() );
        table.addCell( afterEmptyDate() );
        table.addCell( afterDateGst() );
        table.addCell( afterGstBank() );
        document.add(table);
    }

    // Create content left of Second section
    public void addCells(PdfPCell contentLeft){
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.BOLD);
        contentLeft.addElement(new Phrase("{Business Name} " , font) );
        contentLeft.addElement(new Phrase("{Business Address Starts}"));
        contentLeft.addElement(new Phrase("{Business City, PIN & State} "));
        contentLeft.addElement(new Phrase("{Business Phone No.}"));
        contentLeft.addElement(new Phrase("{Business Email} "));
        contentLeft.addElement(new Phrase("{Business Store Web Address}"));

    }


    // Adding Tables for the right side of the Second Section
    public PdfPTable invoice(){
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        PdfPTable table = new PdfPTable(2);
        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.addElement(new Phrase("Invoice No: ",font));
        //invoiceCell.setBorderColor(BaseColor.WHITE);
        invoiceCell.disableBorderSide(Rectangle.LEFT);
        invoiceCell.disableBorderSide(Rectangle.TOP);
        invoiceCell.disableBorderSide(Rectangle.BOTTOM);
        invoiceCell.setPaddingBottom(5);
        invoiceCell.setPaddingLeft(5);
        table.addCell(invoiceCell);
        PdfPCell paymentCell = new PdfPCell();
        paymentCell.addElement(new Phrase("Payment Method:",font));
        //paymentCell.setBorderColor(BaseColor.WHITE);
        paymentCell.disableBorderSide(Rectangle.RIGHT);
        paymentCell.disableBorderSide(Rectangle.TOP);
        paymentCell.disableBorderSide(Rectangle.BOTTOM);
        paymentCell.setPaddingBottom(5);
        paymentCell.setPaddingLeft(5);
        table.addCell(paymentCell);
        return table;
    }

    public PdfPTable afterInvoiceEmpty(){
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        PdfPTable table = new PdfPTable(2);
        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.addElement(new Phrase(" ",font));
        //invoiceCell.setBorderColor(BaseColor.WHITE);
        invoiceCell.disableBorderSide(Rectangle.LEFT);
        invoiceCell.disableBorderSide(Rectangle.TOP);
        invoiceCell.disableBorderSide(Rectangle.BOTTOM);
        invoiceCell.setPaddingBottom(5);
        invoiceCell.setPaddingLeft(5);
        table.addCell(invoiceCell);
        PdfPCell paymentCell = new PdfPCell();
        paymentCell.addElement(new Phrase(" ",font));
        //paymentCell.setBorderColor(BaseColor.WHITE);
        paymentCell.disableBorderSide(Rectangle.RIGHT);
        paymentCell.disableBorderSide(Rectangle.TOP);
        paymentCell.disableBorderSide(Rectangle.BOTTOM);
        paymentCell.setPaddingBottom(5);
        paymentCell.setPaddingLeft(5);
        table.addCell(paymentCell);
        return table;
    }

    public PdfPTable afterEmptyDate(){
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        PdfPTable table = new PdfPTable(2);
        PdfPCell invoiceCell = new PdfPCell(new Phrase("Date : ",font));
        //invoiceCell.setBorderColor(BaseColor.WHITE);
        invoiceCell.disableBorderSide(Rectangle.LEFT);
        invoiceCell.disableBorderSide(Rectangle.TOP);
        invoiceCell.disableBorderSide(Rectangle.BOTTOM);
        invoiceCell.setPaddingBottom(5);
        invoiceCell.setPaddingLeft(5);
        table.addCell(invoiceCell);
        PdfPCell paymentCell = new PdfPCell();
        paymentCell.addElement(new Phrase(new Phrase(" Delivery Note: ",font)));
        paymentCell.addElement(new Phrase("\n"));
        //paymentCell.setBorderColor(BaseColor.WHITE);
        paymentCell.disableBorderSide(Rectangle.RIGHT);
        paymentCell.disableBorderSide(Rectangle.TOP);
        paymentCell.disableBorderSide(Rectangle.BOTTOM);
        paymentCell.setPaddingBottom(5);
        paymentCell.setPaddingLeft(5);
        table.addCell(paymentCell);
        return table;
    }

    public PdfPTable afterDateGst(){
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        PdfPTable table = new PdfPTable(1);
        PdfPCell invoiceCell = new PdfPCell(new Phrase("GSTIN : ",font));
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

    public PdfPTable afterGstBank(){
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        Font fontnormal = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.NORMAL);
        PdfPTable table = new PdfPTable(1);
        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.addElement( new Phrase("Bank Details:  ",font) );
        invoiceCell.addElement( new Phrase("Name of Account: Rohan Enterprises",fontnormal) );
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



    // Add Third Section
    public void addThirdSection(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(Utilities.millimetersToPoints(PDF_WIDTH));
        table.setLockedWidth(true);
        table.setWidths(new int[]{5,4});
        PdfPCell cell;
        cell = new PdfPCell();
        cell.setPaddingLeft(10);
        cell.setPaddingBottom(10);
        //cell.addElement(contentLeftSecondSection());
        addCellsThird(cell);
        cell.setRowspan(7);
        table.addCell(cell);
        table.addCell( afterGstBankThird() );
        table.addCell( afterGstDeclaration() );
        table.addCell( afterGstDeclarationText() );

        document.add(table);
    }

    // Create content left of Third section
    public void addCellsThird(PdfPCell contentLeft){
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 12, Font.BOLD);
        contentLeft.addElement(new Phrase("Buyer: " , font) );
        contentLeft.addElement(new Phrase("{Customer Name}"));
        contentLeft.addElement(new Phrase("{Customer Address}"));
        contentLeft.addElement(new Phrase("{Customer Phone No.} "));
        contentLeft.addElement(new Phrase("{Customer GSTIN}"));

    }

    public PdfPTable afterGstBankThird(){
        Font fontnormal = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.NORMAL);
        PdfPTable table = new PdfPTable(1);
        PdfPCell invoiceCell = new PdfPCell();
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

    public PdfPTable afterGstDeclaration(){
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        PdfPTable table = new PdfPTable(1);
        PdfPCell invoiceCell = new PdfPCell(new Phrase("Declaration: ",font));
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

    public PdfPTable afterGstDeclarationText(){
        Font fontnormal = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.NORMAL);
        PdfPTable table = new PdfPTable(1);
        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.addElement( new Phrase("1. Goods sold can't be taken back or exchanged. ",fontnormal) );
        invoiceCell.addElement( new Phrase("2. Our risk & responsibilty ceases as good leaves",fontnormal) );
        invoiceCell.addElement( new Phrase("Godown. 3. Subject to {City} Jurisdiction.",fontnormal) );
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

    // FOURTH SECTION ADDING THE SALES ITEM TABLE
    // **************** IMPORTANT ****************

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

    // **************** IMPORTANT ****************


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
        table.addCell( cbDiscount() );
        document.add(table);
    }

    // Create content left of Second section
    public void addEmptyCells(PdfPCell contentLeft){
        contentLeft.addElement(new Phrase("\n"));
        contentLeft.addElement(new Phrase("\n"));
        contentLeft.addElement(new Phrase("\n"));
        contentLeft.addElement(new Phrase("\n"));

    }

    // Adding Taxable Amount
    public PdfPTable taxableAmount() throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        PdfPTable table = new PdfPTable(2);
        table.setWidths(new int[]{6,2});
        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.addElement(new Phrase("Total Taxable Amount:",font));
        //invoiceCell.setBorderColor(BaseColor.WHITE);
        invoiceCell.disableBorderSide(Rectangle.LEFT);
        invoiceCell.disableBorderSide(Rectangle.RIGHT);
        invoiceCell.disableBorderSide(Rectangle.TOP);
        invoiceCell.disableBorderSide(Rectangle.BOTTOM);
        invoiceCell.setPaddingBottom(5);
        invoiceCell.setPaddingLeft(5);
        table.addCell(invoiceCell);
        PdfPCell paymentCell = new PdfPCell();
        paymentCell.addElement(new Phrase("148455",font));
        //paymentCell.setBorderColor(BaseColor.WHITE);
        //paymentCell.disableBorderSide(Rectangle.LEFT);
        paymentCell.disableBorderSide(Rectangle.RIGHT);
        paymentCell.disableBorderSide(Rectangle.TOP);
        paymentCell.disableBorderSide(Rectangle.BOTTOM);
        paymentCell.setPaddingBottom(5);
        paymentCell.setPaddingLeft(5);
        table.addCell(paymentCell);
        return table;
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
    public PdfPTable cbDiscount() throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        PdfPTable table = new PdfPTable(2);
        table.setWidths(new int[]{3,1});
        PdfPCell invoiceCell = new PdfPCell(new Phrase("CB Discount:",font));
        //invoiceCell.setBorderColor(BaseColor.WHITE);
        invoiceCell.disableBorderSide(Rectangle.LEFT);
        invoiceCell.disableBorderSide(Rectangle.TOP);
        invoiceCell.disableBorderSide(Rectangle.BOTTOM);
        table.addCell(invoiceCell);

        PdfPCell paymentCell = new PdfPCell();
        paymentCell.addElement(new Phrase("500",font));
        //paymentCell.setBorderColor(BaseColor.WHITE);
        paymentCell.disableBorderSide(Rectangle.RIGHT);
        paymentCell.disableBorderSide(Rectangle.TOP);
        paymentCell.disableBorderSide(Rectangle.BOTTOM);
        table.addCell(paymentCell);

        return table;
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

    public void cbDiscountBelow(Document document) throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD );
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(Utilities.millimetersToPoints(PDF_WIDTH));
        table.setLockedWidth(true);
        table.setWidths(new int[]{6,3});
        PdfPCell invoiceCell = new PdfPCell(new Phrase("Cashback Code: DFGVF85412",font));
        table.addCell(invoiceCell);

        PdfPCell paymentCell = new PdfPCell();
        paymentCell.addElement(grandTotalAmount());
        table.addCell(paymentCell);

        document.add(table);
    }



    // BOTTOM SECOND SECTION

    public void addBottomSecond(Document document) throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 11, Font.BOLD);
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(Utilities.millimetersToPoints(PDF_WIDTH));
        table.setLockedWidth(true);
        PdfPCell cell;
        cell = new PdfPCell();
        cell.addElement(new Phrase("Amount in Words : " , font));
        cell.addElement(new Phrase(" "));
        cell.setPaddingLeft(10);
        cell.setPaddingBottom(10);
        cell.setRowspan(2);
        table.addCell(cell);
        document.add(table);
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
        cell.setPaddingLeft(10);
        cell.setPaddingBottom(10);
        //cell.addElement(contentLeftSecondSection());
        addDispatchDetails(cell);
        cell.setRowspan(2);
        table.addCell(cell);
        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.addElement(new Phrase(" For {Store} Name\n ",font));
        pdfPCell.addElement(new Phrase("\nAuthorized Signatory" , font));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setVerticalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setPaddingBottom(5);
        pdfPCell.setPaddingLeft(5);
        table.addCell( pdfPCell );
        document.add(table);
    }

    // Add Dispach Details
    public void addDispatchDetails(PdfPCell contentLeft){
        Font font = FontFactory.getFont(FontFactory.defaultEncoding, 8, Font.BOLD);
        Font fontNormal = FontFactory.getFont(FontFactory.defaultEncoding, 8);
        contentLeft.addElement(new Phrase("Dispatch from: {Sellar City}" , fontNormal ) );
        contentLeft.addElement(new Phrase("Dispatch to: {Customer City}",fontNormal));

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
