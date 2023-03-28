/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booking;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 *
 * @author Gitu
 */
public class GenerateInvoice {

    private Connection con = MySqlConnection.myConnection();
    private ResultSet rs;   
    private Statement st;
    private BaseFont bfBold;
    private BaseFont bf;
    private int pageNumber = 0;
    private HashMap<Object, Object> invoice;
    private HashMap<Object, Object> resDetails;
    private ArrayList<HashMap> additionalCosts;
    private NewReservationPanel nrPanel = new NewReservationPanel();


    public static void main(String[] args) {

        String pdfFilename = "invoice_" + java.time.LocalDate.now().toString();
        GenerateInvoice generateInvoice = new GenerateInvoice();
/*
        if (args.length < 1) {
            System.err.println("Usage: java " + generateInvoice.getClass().getName()
                    + " " +pdfFilename);
            System.exit(1);
        }
        pdfFilename = args[0].trim();
*/
        generateInvoice.createPDF(pdfFilename);

    }
    
    public void setInvoiceDetails(HashMap res, ArrayList ac) {
        resDetails = res;
        additionalCosts = ac;
    }

    public void createPDF(String pdfFilename) {

        Document doc = new Document();
        PdfWriter docWriter = null;
        initializeFonts();
        String saveFolder = resDetails.get("guestFirstName").toString() + resDetails.get("guestSurname").toString(); 
        
        try {
            String savePath = "invoices" + System.getProperty("file.separator") + saveFolder;
            File saveLocation = new File(savePath);
            File pdfFile = null;
            if(!saveLocation.exists()){
                 saveLocation.mkdir();
                 pdfFile = new File(savePath, pdfFilename);
            } else {
                 pdfFile = new File(savePath, pdfFilename);
            }
            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(pdfFile));
            //doc.addAuthor("betterThanZero");
            doc.addCreationDate();
            doc.addProducer();
            //doc.addCreator("MySampleCode.com");
            doc.addTitle("Invoice");
            doc.setPageSize(PageSize.LETTER);

            doc.open();
            PdfContentByte cb = docWriter.getDirectContent();

            boolean beginPage = true;
            int y = 0;
            
            
            
            //for (int i = 0; i < 10; i++) {
                if (beginPage) {
                    beginPage = false;
                    generateLayout(doc, cb);
                    generateHeader(doc, cb);
                    y = 565;
                }
                generateDetail(doc, cb, y);
                //generateDetail(doc, cb, i, y);
                y = y - 15;
                if (y < 50) {
                    printPageNumber(cb);
                    doc.newPage();
                    beginPage = true;
                }
            //}
            printPageNumber(cb);

        } catch (DocumentException dex) {
            dex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (doc != null) {
                doc.close();
            }
            if (docWriter != null) {
                docWriter.close();
            }
        }
    }

    private void generateLayout(Document doc, PdfContentByte cb) {

        try {

            cb.setLineWidth(1f);

            // Invoice Header box layout
            cb.rectangle(420, 690, 150, 40);
            //cb.moveTo(420, 720);
            //cb.lineTo(570, 720);
            //cb.moveTo(420, 740);
            //cb.lineTo(570, 740);
            cb.moveTo(480, 690);
            cb.lineTo(480, 730);
            cb.stroke();
            
            // Above Invoice Header
            createHeadings(cb, 420, 755, "PIN NO. P051091410B");
            createHeadings(cb, 420, 740, "VAT NO. 0132141P");
            
            // Invoice Header box Text Headings 
            //createHeadings(cb, 422, 743, "Account No.");
            createHeadings(cb, 422, 714, "Invoice No.");
            createHeadings(cb, 422, 694, "Invoice Date");
            
            // Customer details
            createHeadings(cb, 24, 660, "Bill To");
            createContent(cb, 24, 640, resDetails.get("guestFirstName").toString() + " " + resDetails.get("guestSurname").toString(), PdfContentByte.ALIGN_LEFT);
            createHeadings(cb, 270, 640, "Number of Nights: ");
            createContent(cb, 350, 640, resDetails.get("numOfNights").toString(), PdfContentByte.ALIGN_LEFT);

            // Invoice Detail box layout 
            cb.rectangle(20, 50, 550, 550);
            cb.moveTo(20, 580);
            cb.lineTo(570, 580);    //2nd horizontal (left to right)
            cb.moveTo(60, 200);
            cb.lineTo(60, 600);     //2nd vertical (bottom to top)
            cb.moveTo(270, 50);
            cb.lineTo(270, 600);    //3rd vertical
            cb.moveTo(370, 200);
            cb.lineTo(370, 600);    //4rd vertical
            cb.moveTo(460, 50);
            cb.lineTo(460, 600);    //5th vertical
            cb.moveTo(20, 200);
            cb.lineTo(570, 200);    //3rd horizontal 
            cb.stroke();

            // Invoice Detail box Text Headings 
            createHeadings(cb, 24, 583, "Qty");
            createHeadings(cb, 64, 583, "Particulars");
            createHeadings(cb, 274, 583, "@ (Pre-Tax)");
            createHeadings(cb, 374, 583, "@ (With Tax)");
            createHeadings(cb, 464, 583, "SHS (With Tax)");
            
            //Invoice Detail summary
            createSummary(cb, 455, 140, "SUBTOTAL");
            createSummary(cb, 455, 110, "DISCOUNT");
//            createSummary(cb, 455, 140, "AGENT COMMISSION");
//            createSummary(cb, 455, 120, "VAT");
//            createSummary(cb, 455, 100, "TAX");
            createSummary(cb, 455, 80, "TOTAL");

            //add the images
            Image companyLogo = Image.getInstance("src/images/rondo_logo.png");
            companyLogo.setAbsolutePosition(25, 700);
            companyLogo.scalePercent(50);
            doc.add(companyLogo);

        } catch (DocumentException dex) {
            dex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void generateHeader(Document doc, PdfContentByte cb) {
        getInvoiceDetails();
        try {

            createHeadings(cb, 200, 750, "Rondo Retreat Center");
            createHeadings(cb, 200, 735, "P.O. BOX 2153 - 50100");
            createHeadings(cb, 200, 720, "Tel: (056) - 30268");
            createHeadings(cb, 200, 705, "0733-299149");
            createHeadings(cb, 200, 690, "KAKAMEGA");

            //createHeadings(cb, 482, 743, "ABC0001");
            createContent(cb, 482, 714, String.valueOf(invoice.get("invoiceId")), PdfContentByte.ALIGN_LEFT);
            createContent(cb, 482, 694, String.valueOf(invoice.get("date")), PdfContentByte.ALIGN_LEFT);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private double getChildRate(String guestCategory) {
        int accommodationTypeId = Integer.parseInt(resDetails.get("accommodationTypeId").toString());
        double rateChildrenU12 = 0;
        String cat = guestCategory.toLowerCase();
        switch (cat) {
            case "non-resident adult":
                rateChildrenU12 = nrPanel.searchRate(nrPanel.searchCategoryId("Non-Resident Child U12"), accommodationTypeId);
                break;
            case "resident adult":
                rateChildrenU12 = nrPanel.searchRate(nrPanel.searchCategoryId("Resident Child U12"), accommodationTypeId);
                break;
            case "missionary":
            case "tf-staff":
            case "complementary":
                rateChildrenU12 = nrPanel.searchRate(nrPanel.searchCategoryId("Complementary"), accommodationTypeId);
                break;
        }
        return rateChildrenU12;
    }
    
    private double getTeenRate(String guestCategory) {
        int accommodationTypeId = Integer.parseInt(resDetails.get("accommodationTypeId").toString());
        double rateChildrenTeen = 0;
        String cat = guestCategory.toLowerCase();
        switch (cat) {
            case "non-resident adult":
                rateChildrenTeen = nrPanel.searchRate(nrPanel.searchCategoryId("Non-Resident Child 12+"), accommodationTypeId);
                break;
            case "resident adult":
                rateChildrenTeen = nrPanel.searchRate(nrPanel.searchCategoryId("Resident Child 12+"), accommodationTypeId);
                break;
            case "missionary":
            case "tf-staff":
            case "complementary":
                rateChildrenTeen = nrPanel.searchRate(nrPanel.searchCategoryId("Complementary"), accommodationTypeId);
                break;
        }
        return rateChildrenTeen;
    }
    
    private void getInvoiceDetails() {
        int reservationId = Integer.parseInt(resDetails.get("reservationId").toString());
        try {
            String query = "SELECT * FROM `invoice` WHERE reservation_id='" + reservationId + "'";
            st = con.createStatement();
            rs = st.executeQuery(query);

            if (rs.next()) {
                int i = 1;
                
                int invoiceId = rs.getInt(i++);
                reservationId = rs.getInt(i++);
                String date = rs.getString(i++);
                double totalCharges = rs.getDouble(i++);
                double agentCommission = rs.getDouble(i++);
                double discount = rs.getDouble(i++);
                
                invoice = new HashMap();
                invoice.put("invoiceId", invoiceId);
                invoice.put("reservationId", reservationId);
                invoice.put("date", date);
                invoice.put("totalCharges", totalCharges);
                invoice.put("agentCommission", agentCommission);
                invoice.put("discount", discount);
           }
        }
       catch (Exception ex) {
            System.out.println("Error! Cannot fetch. " + ex);
        }
    }
    
    private void generateDetail(Document doc, PdfContentByte cb, int y) {
        int numOfAdults = Integer.parseInt(resDetails.get("numOfAdults").toString());
        int numOfChildrenU12 = Integer.parseInt(resDetails.get("numOfChildrenU12").toString());
        int numOfChildrenTeen = Integer.parseInt(resDetails.get("numOfChildrenTeen").toString());
        int numOfNights = Integer.parseInt(resDetails.get("numOfNights").toString());
        int guestCategoryId = Integer.parseInt(resDetails.get("guestCategoryId").toString());
        int accommodationTypeId = Integer.parseInt(resDetails.get("accommodationTypeId").toString());
        String guestCategory = resDetails.get("guestCategory").toString();
        String accommodationType = resDetails.get("accommodationType").toString();
        
        double adultRate = nrPanel.searchRate(guestCategoryId, accommodationTypeId)/numOfAdults;
        double adultRatePreTax = adultRate - (adultRate*0.16);
        double adultTotal = nrPanel.searchRate(guestCategoryId, accommodationTypeId)*numOfNights*numOfAdults;
        double teenRate = getTeenRate(guestCategory);
        double teenRatePreTax = teenRate - (teenRate*0.16);
        double teenTotal = getTeenRate(guestCategory)*numOfNights*numOfChildrenTeen;
        double childRate = getChildRate(guestCategory);
        double childRatePreTax = childRate - (childRate*0.16);
        double childTotal = getChildRate(guestCategory)*numOfNights*numOfChildrenU12;
        
        double subtotal = 0, discount = 0, agentCommission = 0, vat = 0, tax = 0, totalCharges = 0;
        subtotal = adultTotal + teenTotal + childTotal;
        
        try {
            //adult details
            createContent(cb, 58, y, String.valueOf(numOfAdults), PdfContentByte.ALIGN_RIGHT);  //number of adults
            createContent(cb, 64, y, guestCategory + ", " + accommodationType, PdfContentByte.ALIGN_LEFT);  //guest category, accommodation type
            createContent(cb, 365, y, String.valueOf(adultRatePreTax), PdfContentByte.ALIGN_RIGHT); //rate pre-tax
            createContent(cb, 455, y, String.valueOf(adultRate), PdfContentByte.ALIGN_RIGHT); //rate
            createContent(cb, 565, y, String.valueOf(adultTotal), PdfContentByte.ALIGN_RIGHT);  //total charges
            //teen details
            if (numOfChildrenTeen != 0) {
                y = y-15;
                createContent(cb, 58, y, String.valueOf(numOfChildrenTeen), PdfContentByte.ALIGN_RIGHT);  //number of Children Teen
                createContent(cb, 64, y, "Child 12+, " + accommodationType, PdfContentByte.ALIGN_LEFT);  //guest category, accommodation type
                createContent(cb, 365, y, String.valueOf(teenRatePreTax), PdfContentByte.ALIGN_RIGHT); //rate pre-tax
                createContent(cb, 455, y, String.valueOf(teenRate), PdfContentByte.ALIGN_RIGHT); //rate
                createContent(cb, 565, y, String.valueOf(teenTotal), PdfContentByte.ALIGN_RIGHT);  //total charges
            }
            //child details
            if (numOfChildrenU12 != 0) {
                y = y-15;
                createContent(cb, 58, y, String.valueOf(numOfChildrenU12), PdfContentByte.ALIGN_RIGHT);  //number of Children U12
                createContent(cb, 64, y, "Child U12, " + accommodationType, PdfContentByte.ALIGN_LEFT);  //guest category, accommodation type
                createContent(cb, 365, y, String.valueOf(childRatePreTax), PdfContentByte.ALIGN_RIGHT); //rate pre-tax
                createContent(cb, 455, y, String.valueOf(childRate), PdfContentByte.ALIGN_RIGHT); //rate
                createContent(cb, 565, y, String.valueOf(childTotal), PdfContentByte.ALIGN_RIGHT);  //total charges
            }
            y = y-15;
            for (int i = 0; i<additionalCosts.size(); i++) {
                createContent(cb, 58, y, String.valueOf(1), PdfContentByte.ALIGN_RIGHT);  //count
                createContent(cb, 64, y, String.valueOf(additionalCosts.get(i).get("category")), PdfContentByte.ALIGN_LEFT);  //additional cost category
                createContent(cb, 455, y, String.valueOf(additionalCosts.get(i).get("charges")), PdfContentByte.ALIGN_RIGHT); //rate
                createContent(cb, 565, y, String.valueOf(additionalCosts.get(i).get("charges")), PdfContentByte.ALIGN_RIGHT);  //total charges
                subtotal += Double.parseDouble(additionalCosts.get(i).get("charges").toString());
                y = y-15;
            }
            
            if (invoice != null) {
                discount = Double.parseDouble(invoice.get("discount").toString());
                agentCommission = subtotal * Double.parseDouble(invoice.get("agentCommission").toString());
                vat = 0.16;
                tax = (subtotal - discount - agentCommission) * vat;        
                totalCharges = (subtotal - discount - agentCommission) + tax;
            } else {
                JOptionPane.showMessageDialog(null, "No invoice for specified reservation. ");
            }
            
            y = 140;
            createContent(cb, 565, y, String.valueOf(subtotal), PdfContentByte.ALIGN_RIGHT);  //subtotal
            y = y-30;
            createContent(cb, 565, y, String.valueOf(discount), PdfContentByte.ALIGN_RIGHT);  //discount
//            y = y-20;
//            createContent(cb, 565, y, String.valueOf(agentCommission), PdfContentByte.ALIGN_RIGHT);  //agent commission
//            y = y-20;
//            createContent(cb, 565, y, String.valueOf(vat), PdfContentByte.ALIGN_RIGHT);  //vat
//            y = y-20;
//            createContent(cb, 565, y, String.valueOf(tax), PdfContentByte.ALIGN_RIGHT);  //tax
            y = y-30;
            createContent(cb, 565, y, String.valueOf(totalCharges), PdfContentByte.ALIGN_RIGHT);  //total charges
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void createHeadings(PdfContentByte cb, float x, float y, String text) {
        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.setTextMatrix(x, y);
        cb.showText(text.trim());
        cb.endText();
    }
    
    private void createSummary(PdfContentByte cb, float x, float y, String text) {
        cb.beginText();
        cb.setFontAndSize(bfBold, 10);
        cb.setTextMatrix(x, y);
        //cb.showText(text.trim());
        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, text, x, y, 0);
        cb.endText();
    }

    private void printPageNumber(PdfContentByte cb) {
        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Page No. " + (pageNumber + 1), 570, 25, 0);
        cb.endText();

        pageNumber++;
    }

    private void createContent(PdfContentByte cb, float x, float y, String text, int align) {
        cb.beginText();
        cb.setFontAndSize(bf, 8);
        cb.showTextAligned(align, text.trim(), x, y, 0);
        cb.endText();
    }

    private void initializeFonts() {
        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
