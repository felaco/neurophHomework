package org.FelipeProjects.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import java.io.*;
import java.util.List;

/**
 * Created by felipe on 08-08-2016.
 */
public class ReadExcel
{
    public static DataSet readExcel(String filename, List<Integer> features)
    {
        int featureCount = features.size();
        DataSet dataSet = new DataSet(featureCount,1);

        try (FileInputStream fis = new FileInputStream(filename))
        {
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            int resultColumn = sheet.getRow(0).getLastCellNum() - 1;

            for (Row row : sheet)
            {
                double [] entry = new double[featureCount];
                boolean isNumberArray = true;
                int entryColumn = 0;

                for(int column : features)
                {
                    Cell cell = row.getCell(column);
                    if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
                        entry[entryColumn] = cell.getNumericCellValue();
                    else
                    {
                        isNumberArray = false;
                        break;
                    }
                    entryColumn++;
                }

                if (isNumberArray == true)
                {
                    Cell resultCell = row.getCell(resultColumn);
                    double desiredOutput = resultCell.getNumericCellValue();
                    double [] output = new double[1];
                    output[0] = desiredOutput;

                    dataSet.addRow(new DataSetRow(entry, output));
                }
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return dataSet;
    }

    public static void writeExcel (List<Integer> desired, List <Integer> predicted)
    {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        for (int i = 0; i < predicted.size(); i++)
        {
            int desiredValue = desired.get(i);
            int predictedValue = predicted.get(i);
            Row row = sheet.createRow(i);
            Cell desiredCell = row.createCell(0);
            Cell predictedCell = row.createCell(1);

            desiredCell.setCellValue(desiredValue);
            predictedCell.setCellValue(predictedValue);

        }

        try(FileOutputStream fileOut = new FileOutputStream("resultados.xlsx"))
        {
            workbook.write(fileOut);
            workbook.close();
            fileOut.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
