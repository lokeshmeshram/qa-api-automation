package com.automation.platform.shared.services.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.logging.Logger;

public class DataHelper {

    private final static Logger LOGGER = Logger.getLogger(DataHelper.class.getName());

    /**
     *
     * @param filepath       - test data input data file path
     * @param sheetName      - sheet-name of excel data input data file
     * @return - return read data from excel
     */
    public static Object [][] data(String filepath, String sheetName) {
        String[][] data = null;
        try
        {
            FileInputStream fis = new FileInputStream(filepath);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sh = wb.getSheet(sheetName);
            XSSFRow row = sh.getRow(1);
            int noOfRows = sh.getPhysicalNumberOfRows();
            int noOfCols = row.getLastCellNum();
            Cell cell;
            data = new String[noOfRows-1][noOfCols];

            for(int i =1; i<noOfRows;i++){
                for(int j=0;j<noOfCols;j++){
                    row = sh.getRow(i);
                    cell= row.getCell(j);
                    data[i-1][j] = cell.getStringCellValue();
                }
            }
        }
        catch (Exception e) {
            LOGGER.info("The exception is: " +e.getMessage());
        }
        return data;

    }
}
