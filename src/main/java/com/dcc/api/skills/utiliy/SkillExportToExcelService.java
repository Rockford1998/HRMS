package com.dcc.api.skills.utiliy;

import com.dcc.api.skills.Skill;
import com.dcc.api.utiility.ReportAbstractService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SkillExportToExcelService extends ReportAbstractService {
    public void writeTableData(List<Skill> data) {
        CellStyle style = getFontContentExcel();
        int startRow = 2;
        for (Skill skill : data) {
            Row row = sheet.createRow(startRow++);
            int columnCount = 0;
            createCell(row, columnCount++, skill.getId(), style);
            createCell(row, columnCount++, skill.getName(), style);
            createCell(row, columnCount, skill.getDescription(), style);

        }
    }

    public void exportToExcel(HttpServletResponse response, List<Skill> data) throws IOException {
        newReportExcel();

        // response  writer to excel
        response = initResponseForExportExcel(response, "skills");
        ServletOutputStream outputStream = response.getOutputStream();


        // write sheet, title & header
        String[] headers = new String[]{"Id", "Name", "Description"};
        writeTableHeaderExcel("Sheet Skill", "Report Skill", headers);

        // write content row
        writeTableData(data);

        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
