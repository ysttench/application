package com.ysttench.application.common.util.excel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.format.CellDateFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excel的cell取值
 * @author Howard
 *
 */
public class ParseCellValueUtil {
    private final static Logger logger = LoggerFactory.getLogger(ParseCellValueUtil.class);

    /**
     * 此方法用于解析Excel表格中的单元格中的数据
     * 
     * @param cell
     *            对应于Excel表格中的单元格
     * @return String 单元格中的数据的字符串表示形式
     */
    public static Object parseCellValue(Cell cell) throws Exception {
        Object cellValue = null;
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_NUMERIC: // 数值型
            if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                short dataFormat = cell.getCellStyle().getDataFormat();
                logger.info("dataFormat "+dataFormat);
                String dateFmt = null;

                logger.info("dateFmt "+dateFmt);
                double val = cell.getNumericCellValue();
                Date date = DateUtil.getJavaDate(val);
                if (dataFormat == 14) { 
                    dateFmt = "yyyy/mm/dd"; 
                } else if(dataFormat == 165) { 
                    dateFmt = "m/d/yy";
                } else if(dataFormat == 166) {
                    dateFmt = "d-mmm-yy"; 
                } else if(dataFormat == 167) { 
                    dateFmt = "mmmm d yyyy ";
                } else if(dataFormat == 168) { 
                    dateFmt = "m/d/yyyy";
                } else if(dataFormat == 169) { 
                    dateFmt = "d-mmm-yyyy";
                }else if(dataFormat == 176) { 
                    dateFmt = "yyyy-MM-dd HH:mm:ss";
                }else if(dataFormat == 31) { 
                    dateFmt = "yyyy年m月d日";
                }else if(dataFormat == 57) {
                    dateFmt = "yyyy年m月";
                }else if(dataFormat == 58) {
                    dateFmt = "m月d日";
                }else if(dataFormat == 32) {
                    dateFmt = "h时mm分";
                }else if(dataFormat == 32) {
                    dateFmt = "h时mm分";
                }else if(dataFormat == 21) {
                    dateFmt = "h:mm:ss";
                }else if(dataFormat == 22) {
                    dateFmt = "yyyy/MM/dd HH:mm:ss";
                }  else {
                    dateFmt = cell.getCellStyle().getDataFormatString();
                }
                cellValue = new CellDateFormatter(dateFmt).format(date);

            } else {
                double value = cell.getNumericCellValue();
                CellStyle style = cell.getCellStyle();
                DecimalFormat format = null;
                String temp = style.getDataFormatString();
                temp=temp==null?"General":temp;
                // 单元格设置成常规
                 if (temp.equals("@")) {
                    format = new DecimalFormat("0");
                }else{
                    format = new DecimalFormat();
                    format.applyPattern("#");
                } 
                cellValue = format.format(value);
            }
            break;
        case Cell.CELL_TYPE_STRING: // 字符串型
            cellValue = cell.getStringCellValue();
            break;
        case Cell.CELL_TYPE_BOOLEAN: // 布尔型
            cellValue = cell.getBooleanCellValue();
            break;
        case Cell.CELL_TYPE_FORMULA: // 公式型
            cellValue = cell.getCellFormula();
            break;
        case Cell.CELL_TYPE_BLANK: // 空值
            break;
        case Cell.CELL_TYPE_ERROR: // 错误
            cellValue = cell.getErrorCellValue();
            break;
        default:
            cellValue = cell.toString();
        }
        return cellValue;
    }

    public static byte[] get(String urlString) throws Exception {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(new File(urlString));
            return toByteArrray(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] toByteArrray(InputStream in) throws Exception {
        if (in == null)
            return null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[10000];
        int n = 0;
        try {
            while (-1 != (n = in.read(buffer))) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}
