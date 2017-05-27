package me.smwa.courses.utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XLSUtils {
    public static List<List<String>> parse(byte[] xls) {
        List<List<String>> ret = new ArrayList<>();

        try {
            InputStream inputStream = new ByteArrayInputStream(xls);
            Workbook workbook = getRelevantWorkbook(inputStream);

            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();

            while (iterator.hasNext()) {
                List<String> newRow = new ArrayList<>();
                Row nextRow = iterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellTypeEnum()) {
                        case STRING:
                            newRow.add(cell.getStringCellValue());
                            continue;
                        case NUMERIC:
                            newRow.add(String.valueOf(
                                    ((int) cell.getNumericCellValue())
                            ));
                            continue;
                        case BOOLEAN:
                            newRow.add(String.valueOf(cell.getBooleanCellValue()));
                            continue;
                        default:
                            newRow.add("");
                            break;
                    }
                }

                //Make all rows as long as the header
                while (ret.size() > 0 && newRow.size() < ret.get(0).size()) {
                    newRow.add("");
                }

                ret.add(newRow);
            }

            workbook.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    private static Workbook getRelevantWorkbook(InputStream inputStream) throws IOException
    {
        try {
            return new XSSFWorkbook(inputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
