package com.dcc.api.skills.utiliy;

import com.dcc.api.skills.Skill;
import com.dcc.api.utiility.ReportAbstractService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@Service
public class SkillExportToPdfService extends ReportAbstractService {

    public void writeTableHeader(PdfPTable table, String[] headers) {
        for (String header : headers) {
            //font color
            Color fontColor = Color.decode("#151515"); // Light gray
            //header font
            Font font = FontFactory.getFont(FontFactory.COURIER_BOLD, 16, fontColor);
            //header background color
            Color headerBackgroundColor = Color.decode("#d3d3d3"); // Light gray
            PdfPCell headerCell = new PdfPCell(new Phrase(header, font)); // Set font size to 14px
            //padding
            headerCell.setPadding(10); // Set padding to 10px
            headerCell.setBackgroundColor(headerBackgroundColor); // Optional: Set background color to differentiate the header
            table.addCell(headerCell);
        }
    }


    public void writeTableData(PdfPTable table, List<Skill> list) {
        table.setWidthPercentage(100); // Set table width to 100%
        int number = 0;
        for (Skill item : list) {
            number += 1;

            PdfPCell numberCell = new PdfPCell(new Phrase(String.valueOf(number), getFontContent()));
            numberCell.setPadding(5); // Set padding to 10px for data cells
            table.addCell(numberCell);

            PdfPCell nameCell = new PdfPCell(new Phrase(item.getName(), getFontContent()));
            nameCell.setPadding(5); // Set padding to 10px for data cells
            table.addCell(nameCell);

            PdfPCell descriptionCell = new PdfPCell(new Phrase(item.getDescription(), getFontContent()));
            descriptionCell.setPadding(5); // Set padding to 10px for data cells
            table.addCell(descriptionCell);
        }
    }
    public void exportToPDF(HttpServletResponse response, List<Skill> data) throws IOException {

        // init response
        response = initResponseForExportPdf(response, "USER");

        // define paper size
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        // start document
        document.open();

        // title
        Paragraph title = new Paragraph("Skills", getFontTitle());
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        // subtitle
//        Paragraph subtitle = new Paragraph("Report Date : 09/12/2022", getFontSubtitle());
//        subtitle.setAlignment(Paragraph.ALIGN_LEFT);
//        document.add(subtitle);

        enterSpace(document);
        // create a table with 3 columns
        PdfPTable table = new PdfPTable(3);

        // table header
        String[] headers = new String[]{"No", "Name", "Description"};
        writeTableHeader(table, headers);

        // table content
        writeTableData(table, data);

        // add table to the document
        document.add(table);

        document.close();
    }
}
