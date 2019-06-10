package com.ysttench.application.auth.settings.web.controller.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ysttench.application.auth.core.rdto.Dprint;
import com.ysttench.application.auth.settings.kernel.entity.ExcelForMap;
import com.ysttench.application.auth.settings.kernel.entity.SysPrintForMap;
import com.ysttench.application.auth.settings.kernel.mapper.SysPrintMapper;
import com.ysttench.application.auth.settings.web.controller.common.AttrConstants;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;
import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * excel
 * @author Howard
 */
@Controller
@RequestMapping("/excel/")
public class ApiExcelController extends BaseController {
	@Inject
	private SysPrintMapper sysPrintMapper;

	/**
	 * 跳转列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	public String listUI() throws Exception {
		return ForwardConstants.API + ForwardConstants.EXCEL + ForwardConstants.LIST;
	}

	/**
	 * 跳转列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list2")
	public String listUI2() throws Exception {
		return ForwardConstants.API + ForwardConstants.EXCEL + ForwardConstants.LIST2;
	}

	/**
	 * 分页显示页面
	 * 
	 * @param pageNow
	 * @param pageSize
	 * @param column
	 * @param sort
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findByPage")
	public PageView findByPage(String pageNow, String pageSize, String column, String sort, HttpServletRequest request)
			throws Exception {
		ExcelForMap excelFormMap = getFormMap(ExcelForMap.class);
		excelFormMap = toFormMap(excelFormMap, pageNow, pageSize, excelFormMap.getStr("orderby"));
		if (pageNow == null) {
			pageNow = "1";
		}
		int rowcount = 0;
		int pagecount = 0;
		int start = 10 * (Integer.parseInt(pageNow) - 1);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		File file = new File(request.getSession().getServletContext().getRealPath("/") + "excel.xls");
		try {
			// 创建输入流，读取Excel
			InputStream is = new FileInputStream(file.getAbsolutePath());
			// jxl提供的Workbook类
			Workbook wb = Workbook.getWorkbook(is);
			// Excel的页签数量
			int sheet_size = wb.getNumberOfSheets();
			for (int index = 0; index < sheet_size; index++) {
				// 每个页签创建一个Sheet对象
				Sheet sheet = wb.getSheet(index);
				// sheet.getRows()返回该页的总行数
				for (int i = (start + 1); i < sheet.getRows(); i++) {
					// sheet.getColumns()返回该页的总列数
					Map<String, String> map = new HashMap<String, String>();
					for (int j = 0; j < sheet.getColumns(); j++) {
						String cellinfo = sheet.getCell(j, i).getContents();
						map.put(String.valueOf(j), cellinfo);
						if (j == (sheet.getColumns() - 1)) {
							list.add(map);
						}
					}
				}
				rowcount = sheet.getRows() - 1;
				if ((rowcount % 10) != 0) {
					pagecount = rowcount / 10 + 1;
				} else {
					pagecount = rowcount / 10;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pageView.setRowCount(rowcount);
		pageView.setPageCount(pagecount);
		pageView.setRecords(list);
		return pageView;
	}

	/**
	 * 分页显示页面
	 * 
	 * @param pageNow
	 * @param pageSize
	 * @param column
	 * @param sort
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findByPage1")
	public PageView findByPage1(String pageNow, String pageSize, String column, String sort, HttpServletRequest request)
			throws Exception {
		ExcelForMap excelFormMap = getFormMap(ExcelForMap.class);
		excelFormMap = toFormMap(excelFormMap, pageNow, pageSize, excelFormMap.getStr("orderby"));
		if (pageNow == null) {
			pageNow = "1";
		}
		int rowcount = 0;
		int pagecount = 0;
		int start = 10 * (Integer.parseInt(pageNow) - 1);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		File file = new File(request.getSession().getServletContext().getRealPath("/") + "excel1.xls");
		try {
			// 创建输入流，读取Excel
			InputStream is = new FileInputStream(file.getAbsolutePath());
			// jxl提供的Workbook类
			Workbook wb = Workbook.getWorkbook(is);
			// Excel的页签数量
			int sheet_size = wb.getNumberOfSheets();
			for (int index = 0; index < sheet_size; index++) {
				// 每个页签创建一个Sheet对象
				Sheet sheet = wb.getSheet(index);
				// sheet.getRows()返回该页的总行数
				for (int i = (start + 1); i < sheet.getRows(); i++) {
					// sheet.getColumns()返回该页的总列数
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", sheet.getCell(0, i).getContents());
					map.put("lh", sheet.getCell(1, i).getContents());
					map.put("pm", sheet.getCell(2, i).getContents());
					map.put("gg", sheet.getCell(3, i).getContents());
					map.put("sl", sheet.getCell(4, i).getContents());
					map.put("ph", sheet.getCell(5, i).getContents());
					map.put("xh", sheet.getCell(6, i).getContents());
					list.add(map);
				}
				rowcount = sheet.getRows() - 1;
				if ((rowcount % 10) != 0) {
					pagecount = rowcount / 10 + 1;
				} else {
					pagecount = rowcount / 10;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pageView.setRowCount(rowcount);
		pageView.setPageCount(pagecount);
		pageView.setRecords(list);
		return pageView;
	}

	/**
	 * 打印
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("print")
	@Transactional(readOnly = false)
	// 需要事务操作必须加入此注解
	@SystemLog(module = "excel管理", methods = "excel管理-打印")
	// 凡需要处理业务逻辑的.都需要记录操作日志
	public String print(HttpServletRequest request) throws Exception {
		Dprint dp = new Dprint();
		String organ = SessionUtil.getSessionAttr("organ").toString();
		SysPrintForMap sysPrintForMap = new SysPrintForMap();
		sysPrintForMap.put("organ", organ);
		sysPrintForMap.put("type", "3");
		SysPrintForMap print = sysPrintMapper.findDetail(sysPrintForMap).get(0);
		String[] ids = SessionUtil.getParaValues("ids");
		String[] ids1 = ids[0].split(",");
		File file = new File(request.getSession().getServletContext().getRealPath("/") + "excel.xls");
		// 创建输入流，读取Excel
		InputStream is = new FileInputStream(file.getAbsolutePath());
		// jxl提供的Workbook类
		Workbook wb = Workbook.getWorkbook(is);
		// Excel的页签数量
		Sheet sheet = wb.getSheet(0);
		String[] s1 = new String[sheet.getColumns()];
		for (int j = 0; j < sheet.getColumns(); j++) {
			s1[j] = sheet.getCell(j, 0).getContents();
		}
		for (String id : ids1) {
			String s = "";
			String ewn = "";
			String[] str = new String[sheet.getColumns()];
			for (int k = 1; k < sheet.getColumns(); k++) {
				str[k] = sheet.getCell(k, Integer.parseInt(id)).getContents();
				if (k == 1) {
					s += s1[k] + ":$" + str[k];
					ewn += str[k];
				} else {
					s += "$" + s1[k] + ":$" + str[k];
					ewn += ":!:" + str[k];
				}
			}
			String result = dp.getRemoteInfo(print, organ, s, ewn);
			if (!result.contains("成功")) {
				return result;
			}
		}
		return AttrConstants.SUCCESS;

	}

	/**
	 * 打印
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("print1")
	@Transactional(readOnly = false)
	// 需要事务操作必须加入此注解
	@SystemLog(module = "excel管理", methods = "excel管理-打印")
	// 凡需要处理业务逻辑的.都需要记录操作日志
	public String print1(HttpServletRequest request) throws Exception {
		Dprint dp = new Dprint();
		String organ = SessionUtil.getSessionAttr("organ").toString();
/*		SysPrintForMap print = new SysPrintForMap();
		print.put("wsdlUrl", "http://10.0.216.147:801/WebService.asmx");
		print.put("nameSpace", "http://tempuri.org/");
		print.put("xNum", "0");
		print.put("yNum", "1");
		print.put("module", "printDemo");*/
		SysPrintForMap sysPrintForMap = new SysPrintForMap();
		sysPrintForMap.put("organ", organ);
		sysPrintForMap.put("type", "4");
		SysPrintForMap print = sysPrintMapper.findDetail(sysPrintForMap).get(0);
		String[] ids = SessionUtil.getParaValues("ids");
		String[] ids1 = ids[0].split(",");
		File file = new File(request.getSession().getServletContext().getRealPath("/") + "excel1.xls");
		// 创建输入流，读取Excel
		InputStream is = new FileInputStream(file.getAbsolutePath());
		// jxl提供的Workbook类
		Workbook wb = Workbook.getWorkbook(is);
		// Excel的页签数量
		Sheet sheet = wb.getSheet(0);
		String[] s1 = new String[sheet.getColumns()];
		for (int j = 0; j < sheet.getColumns(); j++) {
			s1[j] = sheet.getCell(j, 0).getContents();
		}
		for (String id : ids1) {
			String s = "";
			String ewn = "";
			String[] str = new String[sheet.getColumns()];
			for (int k = 1; k < sheet.getColumns(); k++) {
				str[k] = sheet.getCell(k, Integer.parseInt(id)).getContents();
				if (k == 1) {
					s += s1[k] + ":$" + str[k];
					ewn += str[k];
				} else {
					s += "$" + s1[k] + ":$" + str[k];
					ewn += ":!:" + str[k];
				}
			}
			String result = dp.getRemoteInfo(print, organ, s, ewn);
			if (!result.contains("成功")) {
				return result;
			}
		}
		return AttrConstants.SUCCESS;

	}

	/**
	 * 新增excel
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		return ForwardConstants.API + ForwardConstants.EXCEL + ForwardConstants.ADD;
	}

	/**
	 * 新增excel
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("addUI1")
	public String addUI1(Model model) throws Exception {
		return ForwardConstants.API + ForwardConstants.EXCEL + ForwardConstants.ADD2;
	}

	/**
	 * 载入excel
	 * 
	 * @param request
	 * @param myfile
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("addEntity")
	@SystemLog(module = "excel管理", methods = "excel管理-载入excel") // 凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly = false) // 需要事务操作必须加入此注解
	public String addEntity(HttpServletRequest request,
			@RequestParam(value = "file", required = false) MultipartFile myfile) throws Exception {
		if (!myfile.isEmpty()) {
			try {

				// 文件保存路径
				String filePath = request.getSession().getServletContext().getRealPath("/") + "excel.xls";
				// 转存文件
				myfile.transferTo(new File(filePath));
			} catch (Exception e) {
				e.printStackTrace();
				return AttrConstants.FAIL;
			}
		}
		return AttrConstants.SUCCESS;

	}

	/**
	 * 载入excel
	 * 
	 * @param request
	 * @param myfile
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("addEntity1")
	@SystemLog(module = "excel管理", methods = "excel管理-载入excel") // 凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly = false) // 需要事务操作必须加入此注解
	public String addEntity1(HttpServletRequest request,
			@RequestParam(value = "file", required = false) MultipartFile myfile) throws Exception {
		if (!myfile.isEmpty()) {
			try {

				// 文件保存路径
				String filePath = request.getSession().getServletContext().getRealPath("/") + "excel1.xls";
				// 转存文件
				myfile.transferTo(new File(filePath));
			} catch (Exception e) {
				e.printStackTrace();
				return AttrConstants.FAIL;
			}
		}
		return AttrConstants.SUCCESS;

	}
}
