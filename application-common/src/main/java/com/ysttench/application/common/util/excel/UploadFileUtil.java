package com.ysttench.application.common.util.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ysttench.application.common.util.ListUtils;
import com.ysttench.application.common.util.StringUtil;


/**
 * 类说明：上传文件工具类
 * 
 * @author Howard
 * 
 */
public class UploadFileUtil {
    // logger日志对象
    private static Logger logger = LoggerFactory.getLogger(UploadFileUtil.class);

    /**
     * 获取excel文件表头信息
     * 
     * @param filePath
     *            文件路径
     * @param fileType
     *            文件类型
     * @return
     * @throws Exception
     */
    public static ParseExcelResult getFieldNameOfXLSFile(InputStream fileStream, String fileType) throws Exception {
        return getFieldNameOfXLSFile(fileStream, fileType, 0);
    }
    
    /**
     * 获取excel文件表头信息
     * 
     * @param filePath
     *            文件路径
     * @param fileType
     *            文件类型
     * @param headRowNum 头部行号
     * @return
     * @throws Exception
     */
    public static ParseExcelResult getFieldNameOfXLSFile(InputStream fileStream, String fileType, int headRowNum) throws Exception {
        ParseExcelResult response = new ParseExcelResult();
        Workbook wb = ExcelUtil.createExcelFile(fileStream, fileType);
        Sheet sheet = wb.getSheetAt(0);// 每次只读取第一个sheet页
        int firstRowNum = sheet.getFirstRowNum() > headRowNum ? sheet.getFirstRowNum() : headRowNum;
        Row row = sheet.getRow(firstRowNum);// 第一行为表头
        List<String> fieldList = getExcelFieldName(row, response);
        response.setRowNum(sheet.getLastRowNum());// 总行数
        response.setFieldName(ListUtils.list2strArry(fieldList));
        return response;
    }

    /**
     *  获取excel文件头
     * @param headRowNum
     * @param response
     * @param fieldList
     * @param sheet
     * @throws Exception
     */
    private static List<String> getExcelFieldName(Row row, ParseExcelResult response) throws Exception {
        List<String> fieldList = new ArrayList<String>();
        if (row != null) {
            short firstCellNum = row.getFirstCellNum();
            short lastCellNum = row.getLastCellNum();
            for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                Cell cell = row.getCell(cellNum);
                Object value = ParseCellValueUtil.parseCellValue(cell);
                if (value == null) {
                    response.setCode(2);
                    response.setMsg("该文件表头不能为空！");
                }else{
                    if (fieldList.contains(value.toString())) {
                        response.setCode(2);
                        response.setMsg("该文件表头不能重复！");
                    }
                    fieldList.add(value.toString());
                }
            }
        } else {
            response.setCode(2);
            response.setMsg("该文件表头不能为空！");
        }
        return fieldList;
    }

    /**
     *  获取excel文件指定行值
     * @param sheet
     * @throws Exception
     */
    private static List<String> getExcelValue(Row row, int headRowNum) throws Exception {
        List<String> valueList = new ArrayList<String>();
        if (row != null) {
            short firstCellNum = row.getSheet().getRow(headRowNum).getFirstCellNum();
            short lastCellNum = row.getSheet().getRow(headRowNum).getLastCellNum();
            for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                Cell cell = row.getCell(cellNum);
                Object value = ParseCellValueUtil.parseCellValue(cell);
                    if (null != value && StringUtil.isNotEmpty(value.toString())) {
                        valueList.add(value.toString());
                    }else{
                        valueList.add(null);
                    }
            }
        }
        return valueList;
    }

    /**
     * 获取csv的文件头部
     * @param response
     * @param fieldList
     * @param record
     */
    private static List<String> getCSVFieldName(CSVRecord record, ParseExcelResult response) {
        List<String> fieldList = new ArrayList<String>();
        for (int j = 0; j < record.size(); j++) {
            String value = record.get(j);
            if (value == null || value.length() == 0) {
                response.setCode(2);
                response.setMsg("该文件表头不能为空！");
            }
            if (fieldList.contains(value)) {
                response.setCode(2);
                response.setMsg("该文件表头不能重复！");
            }
            fieldList.add(value);
        }
        return fieldList;
    }

    /**
     * 获取csv的文件指定行值
     * @param response
     * @param fieldList
     * @param record
     */
    private static List<String> getCSVValue(CSVRecord record) {
        List<String> valueList = new ArrayList<String>();
        for (int j = 0; j < record.size(); j++) {
            String value = record.get(j);
            if (value != null && !StringUtil.isEmpty(value)) {
                valueList.add(value);
            } else {
                valueList.add(null);
            }
        }
        return valueList;
    }

    /**
     * 解析csv文件表头信息
     * 
     * @param filePath
     * @param fileType
     * @return
     * @throws Exception
     */
    public static ParseExcelResult getFieldNameOfCSVFile(InputStream fileStream, String fileType,String ...separator) throws Exception {
        return getFieldNameOfCSVFile(fileStream, fileType, 0,separator);
    }
    /**
     * 解析csv文件表头信息
     * 
     * @param filePath
     * @param fileType
     * @param headRowNum 头部行号
     * @return
     * @throws Exception
     */
    public static ParseExcelResult getFieldNameOfCSVFile(InputStream fileStream, String fileType, int headRowNum,String ...separator) throws Exception {
        ParseExcelResult response = new ParseExcelResult();
        CSVFormat csvFormat = null;
//        InputStream inputStream = null;
        Reader fileReader = null;
        CSVParser csvParser = null;
        try {
            if (ExcelConstants.CSV.equals(fileType)) {
                csvFormat = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
            } else if (ExcelConstants.TXT.equals(fileType)) {
                if(separator.equals(",")){
                    csvFormat = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
                }else{
                    csvFormat = CSVFormat.newFormat('\t').withRecordSeparator('\t');
                }
            }
//            inputStream = new FileInputStream(new File(filePath));

            fileReader = new InputStreamReader(fileStream, "GB2312");
            // fileReader = new FileReader(filePath);
            csvParser = new CSVParser(fileReader, csvFormat);
            List<CSVRecord> csvRecords = csvParser.getRecords();
            if (csvRecords == null || csvRecords.size() == 0) {
                response.setCode(2);
                response.setMsg("该文件为空文件！");
                return response;
            }
            CSVRecord record = csvRecords.get(headRowNum);
            List<String> fieldList = getCSVFieldName(record, response);
//          parseFileResponse.setFileId(filePath);
            response.setFieldName(ListUtils.list2strArry(fieldList));
            response.setRowNum(csvRecords.size()-1);
        } catch (Exception e) {
            response.setCode(2);
            response.setMsg("解析该文件错误！");
            logger.error("invaild the upload csv", e.getMessage());
            e.printStackTrace();
            return response;
        } finally {
            try {
                fileStream.close();
                fileReader.close();
                csvParser.close();
            } catch (Exception e) {
                logger.error("parse csv object close failed");
                e.printStackTrace();
                throw new Exception("invaild the upload csv");
            }
        }

        return response;
    }

    /**
     * 解析excel文件数据(获取前10条数据)
     * 
     * @param filePath
     * @param fileType
     * @return
     * @throws Exception
     */
    public static ParseExcelResult getFieldNameAndValueOfXLSFile(InputStream fileStream, String fileType) throws Exception {
        return getFieldNameAndValueOfXLSFile(fileStream, fileType, 0, 10);
    }
    
    /**
     * 解析excel文件数据(获取前10条数据)
     * 
     * @param filePath
     * @param fileType
     * @param headRowNum 头部行号
     * @param displayRowSize 读取几行数据
     * @return
     * @throws Exception
     */
    public static ParseExcelResult getFieldNameAndValueOfXLSFile(InputStream fileStream, String fileType, int headRowNum, int displayRowSize)
            throws Exception {
        ParseExcelResult response = new ParseExcelResult();
        Workbook wb = ExcelUtil.createExcelFile(fileStream, fileType);
        Sheet sheet = wb.getSheetAt(0);
        int firstRowNum = sheet.getFirstRowNum() > headRowNum ? sheet.getFirstRowNum() : headRowNum;
        int lastRowNum = sheet.getLastRowNum() > displayRowSize ? displayRowSize : sheet.getLastRowNum();
        
        String[][] valueArray = new String[lastRowNum][];
        List<String> fieldList = null;
        int dataRowNum = 0;
        for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null)
                continue;
            if (rowNum == firstRowNum) {
                fieldList = getExcelFieldName(row, response);
            } else {
                List<String> valueList = getExcelValue(row, firstRowNum);
                if (valueList != null && !valueList.isEmpty()) {
                    valueArray[dataRowNum++] = ListUtils.list2strArry(valueList);
                }
            }
        }
        response.setRowNum(lastRowNum);
        response.setFieldName(ListUtils.list2strArry(fieldList));
        response.setValueArray(valueArray);
        return response;
    }

    /**
     * 解析csv文件数据
     * 
     * @param filePath
     * @param fileType
     * @return
     * @throws Exception
     */
    public static ParseExcelResult getFieldNameAndValueOfCSVFile(InputStream fileStream, String fileType,String ...separator) throws Exception {
        return getFieldNameAndValueOfCSVFile(fileStream, fileType, 0, 10,separator);
    }
    
    /**
     * 解析csv文件数据
     * 
     * @param filePath
     * @param fileType
     * @param headRowNum 头部行号
     * @param displayRowSize 读取几行数据
     * @return
     * @throws Exception
     */
    public static ParseExcelResult getFieldNameAndValueOfCSVFile(InputStream fileStream, String fileType, int headRowNum, int displayRowSize,String ...separator) throws Exception {
        ParseExcelResult response = new ParseExcelResult();
        CSVFormat csvFormat = null;
//        InputStream inputStream = null;
        Reader fileReader = null;
        CSVParser csvParser = null;
        List<String> fieldList = new ArrayList<String>();
        String[][] valueArray = null;
        try {
            if (ExcelConstants.CSV.equals(fileType)) {
                csvFormat = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
            } else if (ExcelConstants.TXT.equals(fileType)) {
                if(separator.equals(",")){
                    csvFormat = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
                }else{
                    csvFormat = CSVFormat.newFormat('\t').withRecordSeparator('\t');
                }
            }
//            inputStream = new FileInputStream(new File(filePath));

            fileReader = new InputStreamReader(fileStream, "GB2312");
            // fileReader = new FileReader(filePath);
            csvParser = new CSVParser(fileReader, csvFormat);
            List<CSVRecord> csvRecords = csvParser.getRecords();
            if (csvRecords == null || csvRecords.size() == 0) {
                response.setCode(2);
                response.setMsg("该文件为空文件！");
                return response;
            }
            int firstRowNum = headRowNum;
            int lastRowNum = csvRecords.size() > displayRowSize ? displayRowSize : csvRecords.size();

            valueArray = new String[lastRowNum - 1][];
            int dataRowNum = 0;
            for (int i = firstRowNum; i < lastRowNum; i++) {
                CSVRecord record = csvRecords.get(i);
                if (i == firstRowNum) {
                    fieldList = getCSVFieldName(record, response);
                } else {
                    List<String> valueList = getCSVValue(record);
                    if (valueList != null && !valueList.isEmpty()) {
                        valueArray[dataRowNum++] = ListUtils.list2strArry(valueList);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("invaild the upload csv", e.getMessage());
            response.setCode(2);
            response.setMsg("解析文件失败！");
            e.printStackTrace();
            return response;
        } finally {
            try {
                fileStream.close();
                fileReader.close();
                csvParser.close();
            } catch (Exception e) {
                logger.error("parse csv object close failed");
                e.printStackTrace();
                throw new Exception("invaild the upload csv");
            }
        }
        response.setFieldName(ListUtils.list2strArry(fieldList));
        response.setValueArray(valueArray);

        return response;
    }

    /**
     * 解析文件数据
     * 
     * @param bytes
     * @param fileType
     * @param id
     * @return
     * @throws Exception
     */
    public static String[][] getFileData(InputStream fileStream, String fileType,String ...separator) throws Exception {
        return getFileData(fileStream, fileType, 0,separator);
    }
    /**
     * 解析文件数据
     * 
     * @param bytes
     * @param fileType
     * @param headRowNum 头部行号
     * @param id
     * @return
     * @throws Exception
     */
    public static String[][] getFileData(InputStream fileStream, String fileType, int headRowNum,String ...separator) throws Exception {
        String[][] sqlArr = null;
        if (ExcelConstants.XLS.equals(fileType) || ExcelConstants.XLSX.equals(fileType)) {
            sqlArr = getValueOfXLSFile(fileStream, fileType, headRowNum);
        } else if (ExcelConstants.CSV.equals(fileType) || ExcelConstants.TXT.equals(fileType)) {
            sqlArr = getValueOfCSVFile(fileStream, fileType, headRowNum,separator);
        }
        return sqlArr;
    }

    /**
     * 获取csv文件里的数据
     * @param fileStream
     * @param sqlArr
     * @param headRowNum 头部行号
     * @return
     * @throws Exception
     */
    public static String[][] getValueOfCSVFile(InputStream fileStream, String fileType,String ...separator) throws Exception {
        return getValueOfCSVFile(fileStream, fileType, 0,separator);
    }
    /**
     * 获取csv文件里的数据
     * @param fileStream
     * @param sqlArr
     * @param headRowNum 头部行号
     * @return
     * @throws Exception
     */
    public static String[][] getValueOfCSVFile(InputStream fileStream, String fileType, int headRowNum,String ...separator) throws Exception {
        String[][] sqlArr = null;
        CSVFormat csvFormat = null;
//        InputStream inputStream = null;
        Reader fileReader = null;
        CSVParser csvParser = null;
        try {
            if (ExcelConstants.CSV.equals(fileType)) {
                csvFormat = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
            } else if (ExcelConstants.TXT.equals(fileType)) {
                if(separator.equals(",")){
                    csvFormat = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
                }else{
                    csvFormat = CSVFormat.newFormat('\t').withRecordSeparator('\t');
                }
            }
//                inputStream = new ByteArrayInputStream(bytes);
            fileReader = new InputStreamReader(fileStream, "GB2312");
            csvParser = new CSVParser(fileReader, csvFormat);
            List<CSVRecord> csvRecords = csvParser.getRecords();
            if (csvRecords == null || csvRecords.size() == 0) {
                logger.error("该文件为空文件！");
                throw new Exception("该文件为空文件！");
            }
            sqlArr = new String[csvRecords.size() - headRowNum - 1][];//
            int rowNm = 0;
            for (int i = headRowNum + 1; i < csvRecords.size(); i++) {
                CSVRecord record = csvRecords.get(i);
                List<String> valueList = getCSVValue(record);
                if (valueList != null && !valueList.isEmpty()) {
                    sqlArr[rowNm++] = ListUtils.list2strArry(valueList);
                }
            }
        } catch (Exception e) {
            logger.error("invaild the upload csv", e.getMessage());
            e.printStackTrace();
            throw new Exception("invaild the upload csv");
        } finally {
            try {
                fileStream.close();
                fileReader.close();
                csvParser.close();
            } catch (Exception e) {
                logger.error("parse csv object close failed");
                e.printStackTrace();
                throw new Exception("invaild the upload csv");
            }
        }
        return sqlArr;
    }

    /**
     * 获取excel数据
     * @param fileStream
     * @param fileType
     * @param id
     * @return
     * @throws Exception
     */
    public static String[][] getValueOfXLSFile(InputStream fileStream, String fileType) throws Exception {
        return getValueOfXLSFile(fileStream, fileType, 0);
    }
    
    /**
     * 获取excel数据
     * @param fileStream
     * @param fileType
     * @param headRowNum 头部行号
     * @param id
     * @return
     * @throws Exception
     */
    public static String[][] getValueOfXLSFile(InputStream fileStream, String fileType, int headRowNum) throws Exception {
        String[][] sqlArr;
        Workbook wb = getWorkbook(fileStream, fileType);
        Sheet sheet = wb.getSheetAt(0);//
        int firstRowNum = sheet.getFirstRowNum() > headRowNum ? sheet.getFirstRowNum() : headRowNum;// 第一行
        int lastRowNum = sheet.getLastRowNum();// 最后一行
        sqlArr = new String[lastRowNum - firstRowNum][]; //数据数组
        int rowNm = 0;
        for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
            Row row = sheet.getRow(rowNum);// 获取该行
            if (row == null) {
                continue;
            }
            List<String> valueList = getExcelValue(row, firstRowNum);
            if (valueList != null && !valueList.isEmpty()) {
                sqlArr[rowNm++] = ListUtils.list2strArry(valueList);
            }
        }
        return sqlArr;
    }

    /**
     * 通过字节流打开excel
     * @param bytes
     * @param fileType
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbook(InputStream fileStream, String fileType) throws Exception {
        Workbook wb = null;
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        if (ExcelConstants.XLS.equals(fileType)) {
            wb = getXlsWorkbook(fileStream);
        } else if (ExcelConstants.XLSX.equals(fileType)) {
            wb = getXlsxWorkbook(fileStream);
        }
        return wb;
    }

    /**
     * 打开xlsx文件
     * @param fileStream
     * @param wb
     * @return
     * @throws Exception
     */
    public static Workbook getXlsxWorkbook(InputStream fileStream) throws Exception {
        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(fileStream);
        } catch (Exception e) {
            logger.error("failed parse io for poi", e);
            e.printStackTrace();
            throw new Exception("failed parse io for poi", e);
        }
        return wb;
    }

    /**
     * 打开xls文件
     * @param fileStream
     * @param wb
     * @return
     * @throws Exception
     */
    public static Workbook getXlsWorkbook(InputStream fileStream) throws Exception {
        Workbook wb = null;
        try {
            wb = new HSSFWorkbook(fileStream);
        } catch (IOException e) {
            logger.error("failed parse io for poi", e);
            e.printStackTrace();
            throw new Exception("failed parse io for poi", e);
        }
        return wb;
    }

}
