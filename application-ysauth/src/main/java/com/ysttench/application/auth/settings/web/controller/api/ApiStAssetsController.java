package com.ysttench.application.auth.settings.web.controller.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ysttench.application.auth.core.rdto.Dprint;
import com.ysttench.application.auth.core.rdto.ToExcel;
import com.ysttench.application.auth.settings.kernel.entity.ApiOfficeFormMap;
import com.ysttench.application.auth.settings.kernel.entity.ApiStAssetsForMap;
import com.ysttench.application.auth.settings.kernel.entity.BdDepartmentFormMap;
import com.ysttench.application.auth.settings.kernel.entity.BdStaffFormMap;
import com.ysttench.application.auth.settings.kernel.entity.LgDForMap;
import com.ysttench.application.auth.settings.kernel.entity.LgForMap;
import com.ysttench.application.auth.settings.kernel.entity.SysPrintForMap;
import com.ysttench.application.auth.settings.kernel.entity.SysUserFormMap;
import com.ysttench.application.auth.settings.kernel.mapper.ApiOfficeMapper;
import com.ysttench.application.auth.settings.kernel.mapper.ApiStAssetsMapper;
import com.ysttench.application.auth.settings.kernel.mapper.ApiStAssinfoMapper;
import com.ysttench.application.auth.settings.kernel.mapper.LgDMapper;
import com.ysttench.application.auth.settings.kernel.mapper.LgMapper;
import com.ysttench.application.auth.settings.kernel.mapper.SysPrintMapper;
import com.ysttench.application.auth.settings.web.controller.common.AttrConstants;
import com.ysttench.application.auth.settings.web.controller.common.AuthConfigComponent;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;
import com.ysttench.application.common.exception.SystemException;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.JsonUtils;
import com.ysttench.application.common.util.StringUtil;

import jxl.Sheet;
import jxl.Workbook;

/**
 * 列管资产
 * 
 * @author Howard
 */
@Controller
@RequestMapping("/stassets/")
public class ApiStAssetsController extends BaseController {
	@Inject
	private ApiStAssetsMapper apiStAssetsMapper;
	@Inject
	ApiStAssinfoMapper apiStAssinfoMapper;
	@Inject
	SysPrintMapper sysPrintMapper;
	@Inject
	LgMapper lgMapper;
	@Inject
	LgDMapper lgDMapper;
	@Inject
	private AuthConfigComponent authConfigComponent;
	@Inject
	ApiOfficeMapper apiOfficeMapper;

	/**
	 * 跳转列表页面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		return ForwardConstants.API + ForwardConstants.STASSETS + ForwardConstants.LIST;
	}

	/**
	 * 跳转列表页面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("alist")
	public String listUI1(Model model) throws Exception {
		return ForwardConstants.API + ForwardConstants.STASSETS + ForwardConstants.LIST3;
	}

	/**
	 * 跳转列表页面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list2")
	public String listUI2(Model model) throws Exception {
		return ForwardConstants.API + ForwardConstants.STASSETS + ForwardConstants.LIST2;
	}

	/**
	 * 跳转列表页面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("lglist")
	public String lglistUI2(Model model) throws Exception {
		return ForwardConstants.API + ForwardConstants.STASSETS + ForwardConstants.LGLIST;
	}

	/**
	 * 分页显示
	 * 
	 * @param pageNow
	 * @param pageSize
	 * @param column
	 * @param sort
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findByPage")
	public PageView findByPage(String pageNow, String pageSize, String column, String sort) throws Exception {
		String status = SessionUtil.getPara("status");
		ApiStAssetsForMap apiStAssetsForMap = getFormMap(ApiStAssetsForMap.class);
		apiStAssetsForMap = toFormMap(apiStAssetsForMap, pageNow, pageSize, apiStAssetsForMap.getStr("orderby"));
		apiStAssetsForMap.put("column", StringUtil.prop2tablefield(column));
		apiStAssetsForMap.put("sort", sort);
		apiStAssetsForMap.put("organ", SessionUtil.getSessionAttr("organ").toString());
		apiStAssetsForMap.put("status", status);
		pageView.setRecords(apiStAssetsMapper.findAllPage(apiStAssetsForMap));
		return pageView;
	}

	/**
	 * 分页显示
	 * 
	 * @param pageNow
	 * @param pageSize
	 * @param column
	 * @param sort
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findByPage2")
	public PageView findByPage2(String pageNow, String pageSize, String column, String sort) throws Exception {
		ApiStAssetsForMap apiStAssetsForMap = getFormMap(ApiStAssetsForMap.class);
		apiStAssetsForMap = toFormMap(apiStAssetsForMap, pageNow, pageSize, apiStAssetsForMap.getStr("orderby"));
		apiStAssetsForMap.put("column", StringUtil.prop2tablefield(column));
		apiStAssetsForMap.put("sort", sort);
		apiStAssetsForMap.put("organ", SessionUtil.getSessionAttr("organ").toString());
		pageView.setRecords(apiStAssetsMapper.findAllPage(apiStAssetsForMap));
		return pageView;
	}

	/**
	 * 分页显示
	 * 
	 * @param pageNow
	 * @param pageSize
	 * @param column
	 * @param sort
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findByPage3")
	public PageView findByPage3(String pageNow, String pageSize, String column, String sort) throws Exception {
		LgDForMap lgDForMap = getFormMap(LgDForMap.class);
		lgDForMap.put("column", StringUtil.prop2tablefield(column));
		lgDForMap.put("sort", sort);
		lgDForMap.put("organ", SessionUtil.getSessionAttr("organ").toString());
		lgDForMap = toFormMap(lgDForMap, pageNow, pageSize, lgDForMap.getStr("orderby"));
		pageView.setRecords(lgDMapper.findAllPage(lgDForMap));
		return pageView;
	}

	/**
	 * 新增列管资产
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		return ForwardConstants.API + ForwardConstants.STASSETS + ForwardConstants.ADD;
	}

	/**
	 * 添加列管资产
	 * 
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping("addEntity")
	public String addEntity() {
		try {
			ApiStAssetsForMap apiStAssetsForMap = getFormMap(ApiStAssetsForMap.class);
			apiStAssetsForMap.put("sqer", SessionUtil.getSessionAttr("userName"));
			apiStAssetsForMap.put("organ", SessionUtil.getSessionAttr("organ"));
			apiStAssetsMapper.addEntity(apiStAssetsForMap);
		} catch (Exception e) {
			throw new SystemException("添加资产异常");
		}
		return AttrConstants.SUCCESS;
	}

	/**
	 * 删除
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("deleteEntity")
	public String deleteEntity() throws Exception {
		String[] ids = SessionUtil.getParaValues("ids");
		String[] ids1 = ids[0].split(",");
		for (String id : ids1) {
			ApiStAssetsForMap apiStAssetsForMap = new ApiStAssetsForMap();
			apiStAssetsForMap.put("id", id);
			List<ApiStAssetsForMap> list = apiStAssetsMapper.findAllPage(apiStAssetsForMap);
			if (!"1".equals(list.get(0).get("status").toString())) {
				apiStAssetsMapper.delete(apiStAssetsForMap);
			} else {
				return AttrConstants.FAIL1;
			}
		}
		return AttrConstants.SUCCESS;
	}

	/**
	 * 审核
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("shEntity")
	public String shEntity() throws Exception {
		String[] ids = SessionUtil.getParaValues("ids");
		String[] ids1 = ids[0].split(",");
		for (String id : ids1) {
			ApiStAssetsForMap apiStAssetsForMap = new ApiStAssetsForMap();
			apiStAssetsForMap.put("id", id);
			List<ApiStAssetsForMap> list = apiStAssetsMapper.findAllPage(apiStAssetsForMap);
			if ("0".equals(list.get(0).get("status").toString())) {
				apiStAssetsForMap.put("sper", SessionUtil.getSessionAttr("userName"));
				apiStAssetsForMap.put("status", "1");
				apiStAssetsMapper.edit(apiStAssetsForMap);
			} else {
				return AttrConstants.FAIL1;
			}
		}
		return AttrConstants.SUCCESS;
	}

	/**
	 * 返审核
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("fshEntity")
	public String fshEntity() throws Exception {
		String[] ids = SessionUtil.getParaValues("ids");
		String[] ids1 = ids[0].split(",");
		for (String id : ids1) {
			ApiStAssetsForMap apiStAssetsForMap = new ApiStAssetsForMap();
			apiStAssetsForMap.put("id", id);
			List<ApiStAssetsForMap> list = apiStAssetsMapper.findAllPage(apiStAssetsForMap);
			if ("1".equals(list.get(0).get("status").toString())) {
				apiStAssetsForMap.put("sper", SessionUtil.getSessionAttr("userName"));
				apiStAssetsForMap.put("status", "2");
				apiStAssetsMapper.edit(apiStAssetsForMap);
			} else {
				return AttrConstants.FAIL1;
			}
		}
		return AttrConstants.SUCCESS;
	}

	/**
	 * 进入用户编辑画面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = SessionUtil.getPara("id");
		if (StringUtil.isNotEmpty(id)) {
			ApiStAssetsForMap apiStAssetsForMap = new ApiStAssetsForMap();
			apiStAssetsForMap.put("id", id);
			List<ApiStAssetsForMap> list = apiStAssetsMapper.findDetailMsg(apiStAssetsForMap);
			apiStAssetsForMap = list.get(0);
			model.addAttribute("assets", apiStAssetsForMap);
		}
		return ForwardConstants.API + ForwardConstants.STASSETS + ForwardConstants.EDIT;
	}

	/**
	 * 创建盘点单
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("createPdUI")
	public String createPdUI(Model model) throws Exception {
		String id = SessionUtil.getPara("id");
		model.addAttribute("id", id);
		return ForwardConstants.API + ForwardConstants.STASSETS + ForwardConstants.EDIT1;
	}

	/**
	 * 跳转到添加到盘点单
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("addPdUI")
	public String addPdUI(Model model) throws Exception {
		String id = SessionUtil.getPara("id");
		model.addAttribute("id", id);
		return ForwardConstants.API + ForwardConstants.STASSETS + ForwardConstants.EDIT2;
	}

	/**
	 * 添加到盘点单
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("addPdEntity")
	@Transactional(readOnly = false)
	public String addPdEntity(Model model) throws Exception {
		try {
			LgDForMap lgDForMap = getFormMap(LgDForMap.class);
			String[] ids = lgDForMap.get("id").toString().split(",");
			lgDForMap.remove("id");
			String pdId = lgDForMap.get("pdName").toString();
			for (String id : ids) {
				lgDForMap.put("lgzcId", id);
				int count = Integer.parseInt(lgMapper.getcount(lgDForMap).get(0).get("count").toString());
				if (count != 0) {
					lgDForMap.put("pdId", pdId);
					if (isExit(lgDForMap)) {
						lgMapper.addEntity(lgDForMap);
					}
				} else {
					return AttrConstants.FAIL1;
				}
			}
		} catch (Exception e) {
			throw new SystemException("添加异常");
		}
		return AttrConstants.SUCCESS;
	}

	/**
	 * 判断唯一
	 * 
	 * @param lgDForMap
	 * @return
	 */
	private boolean isExit(LgDForMap lgDForMap) {
		List<LgDForMap> lg = lgDMapper.count(lgDForMap);
		int count = Integer.parseInt(lg.get(0).get("count").toString());
		if (count > 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 盘点单兴建保存
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("createEntity")
	@Transactional(readOnly = false)
	public String createEntity(Model model) throws Exception {
		try {
			LgDForMap lgDForMap = getFormMap(LgDForMap.class);
			String[] ids = lgDForMap.get("id").toString().split(",");
			getMsg(lgDForMap);
			String pdId = lgDForMap.get("id").toString();
			for (String id : ids) {
				lgDForMap.put("lgzcId", id);
				int count = Integer.parseInt(lgMapper.getcount(lgDForMap).get(0).get("count").toString());
				if (count != 0) {
					lgDForMap.put("pdId", pdId);
					lgMapper.addEntity(lgDForMap);
				} else {
					return AttrConstants.FAIL1;
				}
			}
		} catch (Exception e) {
			throw new SystemException("添加异常");
		}
		return AttrConstants.SUCCESS;
	}

	/**
	 * 获取信息
	 * 
	 * @param lgDForMap
	 */
	private void getMsg(LgDForMap lgDForMap) {
		try {
			lgDForMap.remove("id");
			lgDForMap.put("organ", SessionUtil.getSessionAttr("organ").toString());
			lgMapper.add(lgDForMap);
		} catch (Exception e) {
			throw new SystemException("添加异常");
		}
	}

	/**
	 * 编辑保存
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("editEntity")
	public String editEntity() throws Exception {
		try {
			ApiStAssetsForMap apiStAssetsForMap = getFormMap(ApiStAssetsForMap.class);
			List<ApiStAssetsForMap> list = apiStAssetsMapper.findAllPage1(apiStAssetsForMap);
			if ("1".equals(list.get(0).get("status").toString())) {
				return AttrConstants.FAIL1;
			} else {
				apiStAssetsForMap.put("status", "0");
				apiStAssetsMapper.edit(apiStAssetsForMap);
			}
		} catch (Exception e) {
			throw new SystemException("修改资产异常");
		}
		return AttrConstants.SUCCESS;
	}

	/**
	 * 验证账号是否存在
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping("isExist")
	@ResponseBody
	public boolean isExist(String assetsNum, String id) {
		ApiStAssetsForMap apiStAssetsForMap = new ApiStAssetsForMap();
		apiStAssetsForMap.put("assetsNum", assetsNum);
		apiStAssetsForMap.put("id", id);
		List<ApiStAssetsForMap> user = apiStAssetsMapper.count(apiStAssetsForMap);
		int count = Integer.parseInt(user.get(0).get("count").toString());
		if (count > 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 验证账号是否存在
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping("isExist2")
	@ResponseBody
	public boolean isExist2(String pdName) {
		LgDForMap lgDForMap = new LgDForMap();
		lgDForMap.put("pdName", pdName);
		List<LgDForMap> lg = lgDMapper.count2(lgDForMap);
		int count = Integer.parseInt(lg.get(0).get("count").toString());
		if (count > 0) {
			return false;
		} else {
			return true;
		}
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
	public String print() throws Exception {
		String[] ids = SessionUtil.getParaValues("ids");
		String[] ids1 = ids[0].split(",");
		Dprint dp = new Dprint();
		String organ = SessionUtil.getSessionAttr("organ").toString();
		SysPrintForMap sysPrintForMap = new SysPrintForMap();
		sysPrintForMap.put("organ", organ);
		sysPrintForMap.put("type", "2");
		SysPrintForMap print = sysPrintMapper.findDetail(sysPrintForMap).get(0);
		for (String id : ids1) {
			ApiStAssetsForMap apiStAssetsForMap = new ApiStAssetsForMap();
			apiStAssetsForMap.put("id", id);
			List<ApiStAssetsForMap> list = apiStAssetsMapper.findAllPrint(apiStAssetsForMap);
			apiStAssetsForMap = list.get(0);
			int start = apiStAssetsForMap.getStr("assetsType").indexOf("-");
			int endIndex = apiStAssetsForMap.getStr("assetsType").length();
			String s = "资产编号:$" + apiStAssetsForMap.getStr("assetsNum") + "$资产名称:$"
					+ apiStAssetsForMap.getStr("assetsName") + "$资产分类:$"
					+ apiStAssetsForMap.getStr("assetsType").substring(start + 1, endIndex) + "$保管人:$"
					+ apiStAssetsForMap.getStr("keeper") + "$部门:$" + apiStAssetsForMap.getStr("keepdepart") + "$组织:$"
					+ apiStAssetsForMap.getStr("organ").substring(0, apiStAssetsForMap.getStr("organ").length() - 4);
			String ewm = apiStAssetsForMap.getStr("assetsNum") + ":!:" + apiStAssetsForMap.getStr("assetsName") + ":!:"
					+ apiStAssetsForMap.getStr("assetsType") + ":!:" + apiStAssetsForMap.getStr("xh") + ":!:"
					+ apiStAssetsForMap.getStr("keeper") + ":!:" + apiStAssetsForMap.getStr("organ");
			String result = dp.getRemoteInfo(print, organ, s, ewm);
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
	@RequestMapping("excelUI")
	public String excelUI(Model model) throws Exception {
		return ForwardConstants.API + ForwardConstants.STASSETS + ForwardConstants.EXCEL;
	}

	/**
	 * 添加excel
	 * 
	 * @param txtGroupsSelect
	 * @return
	 */
	@ResponseBody
	@RequestMapping("excel")
	public String addEntity(HttpServletRequest request,
			@RequestParam(value = "file", required = false) MultipartFile myfile) throws Exception {
		String returnmsg = "";
		if (!myfile.isEmpty()) {
			try {
				// 文件保存路径
				String filePath = request.getSession().getServletContext().getRealPath("/") + "lgzc.xls";
				// 转存文件
				myfile.transferTo(new File(filePath));
				File file = new File(request.getSession().getServletContext().getRealPath("/") + "lgzc.xls");
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
					for (int i = 1; i < sheet.getRows(); i++) {
						ApiStAssetsForMap apiStAssetsForMap = new ApiStAssetsForMap();
						apiStAssetsForMap.put("assetsNum", sheet.getCell(1, i).getContents());
						apiStAssetsForMap.put("assetsName", sheet.getCell(2, i).getContents());
						apiStAssetsForMap.put("assetsType", sheet.getCell(3, i).getContents());
						apiStAssetsForMap.put("deleteStatus", "0");
						String place = sheet.getCell(4, i).getContents();
						if (!StringUtil.isEmpty(place)) {
							ApiOfficeFormMap apiOfficeFormMap = new ApiOfficeFormMap();
							apiOfficeFormMap.put("locationName", place);
							List<ApiOfficeFormMap> office = apiOfficeMapper.findsuperid(apiOfficeFormMap);
							if (office.size() != 0) {
								apiStAssetsForMap.put("place", office.get(0).get("id").toString());
							}
						}
						apiStAssetsForMap.put("keeper", sheet.getCell(5, i).getContents());
						apiStAssetsForMap.put("xh", sheet.getCell(6, i).getContents());
						apiStAssetsForMap.put("keepdepart", sheet.getCell(7, i).getContents());
						apiStAssetsForMap.put("organ", sheet.getCell(8, i).getContents());
						apiStAssetsForMap.put("xlh", sheet.getCell(9, i).getContents());
						if ("闲置".equals(sheet.getCell(10, i).getContents())) {
							apiStAssetsForMap.put("assetstatus", "0");
						} else if ("已领用".equals(sheet.getCell(10, i).getContents())) {
							apiStAssetsForMap.put("assetstatus", "1");
						} else if ("报废".equals(sheet.getCell(10, i).getContents())) {
							apiStAssetsForMap.put("assetstatus", "-1");
						}
						apiStAssetsForMap.put("gmdate", sheet.getCell(11, i).getContents());
						apiStAssetsForMap.put("sqer", SessionUtil.getSessionAttr("userName"));
						if (isExist(sheet.getCell(1, i).getContents(), "")) {
							apiStAssetsMapper.addEntity(apiStAssetsForMap);
						} else {
							returnmsg += "第" + i + "条记录(资产编码：" + sheet.getCell(1, i).getContents() + ";资产名称："
									+ sheet.getCell(2, i).getContents() + ")资产编码重复,";
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return AttrConstants.FAIL;
			}
		}
		if (StringUtil.isEmpty(returnmsg)) {
			return AttrConstants.SUCCESS;
		} else {
			return returnmsg;
		}

	}

	/**
	 * 盘盈信息
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("pyMsg")
	public String pyMsg(Model model) throws Exception {
		String id = SessionUtil.getPara("id");
		model.addAttribute("id", id);
		model.addAttribute("type", "py");
		return ForwardConstants.API + ForwardConstants.STASSETS + ForwardConstants.PD;
	}
	 @RequestMapping("/export")
	    public void download(HttpServletRequest request, HttpServletResponse response, String column,String sort) throws IOException {
	        String fileName = "列管资产待审核";
	        ApiStAssetsForMap apiStAssetsForMap = findHasHMap(ApiStAssetsForMap.class);
	        apiStAssetsForMap.put("column", StringUtil.prop2tablefield(column));
	        apiStAssetsForMap.put("sort", sort);
	        String exportData = apiStAssetsForMap.getStr("exportData");// 列表头的json字符串

	        List<Map<String, Object>> listMap = JsonUtils.parseJSONList(exportData);
	        apiStAssetsForMap.put("organ", SessionUtil.getSessionAttr("organ").toString());
			apiStAssetsForMap.put("status", "0");
	        List<ApiStAssetsForMap> lis = apiStAssetsMapper.findAllPage(apiStAssetsForMap);
	        for (ApiStAssetsForMap stassets : lis) {
	        	if (!StringUtil.isEmpty(stassets.getStr("assetstatus"))) {
	            if(stassets.getStr("assetstatus").equals("-1")){
	            	apiStAssetsForMap.put("assetstatus", "报废");
	            }else if (stassets.getStr("assetstatus").equals("0")) {
	            	apiStAssetsForMap.put("assetstatus", "闲置");
				}else if (stassets.getStr("assetstatus").equals("1")){ 
	            	apiStAssetsForMap.put("assetstatus", "已领用");
	            }else {
	            	apiStAssetsForMap.put("assetstatus", " ");
				}
	        	}
	        }
	        ToExcel toExcel = new ToExcel();
	        toExcel.myexportToExcel(response, listMap, lis, fileName);
	    }
	 @RequestMapping("/export1")
	    public void download1(HttpServletRequest request, HttpServletResponse response, String column,String sort) throws IOException {
	        String fileName = "列管资产已审核";
	        ApiStAssetsForMap apiStAssetsForMap = findHasHMap(ApiStAssetsForMap.class);
	        apiStAssetsForMap.put("column", StringUtil.prop2tablefield(column));
	        apiStAssetsForMap.put("sort", sort);
	        String exportData = apiStAssetsForMap.getStr("exportData");// 列表头的json字符串

	        List<Map<String, Object>> listMap = JsonUtils.parseJSONList(exportData);
	        apiStAssetsForMap.put("organ", SessionUtil.getSessionAttr("organ").toString());
			apiStAssetsForMap.put("status", "1");
	        List<ApiStAssetsForMap> lis = apiStAssetsMapper.findAllPage(apiStAssetsForMap);
	        for (ApiStAssetsForMap stassets : lis) {
	        	if (!StringUtil.isEmpty(stassets.getStr("assetstatus"))) {
	            if(stassets.getStr("assetstatus").equals("-1")){
	            	apiStAssetsForMap.put("assetstatus", "报废");
	            }else if (stassets.getStr("assetstatus").equals("0")) {
	            	apiStAssetsForMap.put("assetstatus", "闲置");
				}else if (stassets.getStr("assetstatus").equals("1")){ 
	            	apiStAssetsForMap.put("assetstatus", "已领用");
	            }else {
	            	apiStAssetsForMap.put("assetstatus", " ");
				}
	        	}
	        }
	        ToExcel toExcel = new ToExcel();
	        toExcel.myexportToExcel(response, listMap, lis, fileName);
	    }
	/**
	 * 总盘信息
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("zpMsg")
	public String zpMsg(Model model) throws Exception {
		String id = SessionUtil.getPara("id");
		model.addAttribute("id", id);
		model.addAttribute("type", "zp");
		return ForwardConstants.API + ForwardConstants.STASSETS + ForwardConstants.PD;
	}

	/**
	 * 盘亏信息
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("pkMsg")
	public String pkMsg(Model model) throws Exception {
		String id = SessionUtil.getPara("id");
		model.addAttribute("id", id);
		model.addAttribute("type", "pk");
		return ForwardConstants.API + ForwardConstants.STASSETS + ForwardConstants.PD;
	}

	@RequestMapping("fd")
	@ResponseBody
	public List<LgForMap> fd() throws Exception {
		LgForMap lgForMap = new LgForMap();
		String type = SessionUtil.getPara("type");
		String id = SessionUtil.getPara("id");
		List<LgForMap> list = new ArrayList<LgForMap>();
		lgForMap.put("fid", id);
		lgForMap.put(type, type);
		if ("py".equals(type)) {
			list = lgMapper.findAllpyMsg(lgForMap);
		} else if ("pk".equals(type)) {
			list = lgMapper.findAllpkMsg(lgForMap);
		} else {
			list = lgMapper.findAllMsg(lgForMap);
		}
		return list;

	}

	/**
	 * 保管部门信息
	 * 
	 * @return
	 */
	@RequestMapping("getkeepdepart")
	@ResponseBody
	public List<BdDepartmentFormMap> gedepartment() {
		List<BdDepartmentFormMap> list = new ArrayList<BdDepartmentFormMap>();
		String resultJson = authConfigComponent.getAuthService()
				.gedepartment(SessionUtil.getSessionAttr("organ").toString());
		JSONObject jobj = JSONObject.parseObject(resultJson);
		if ("0".equals(jobj.getString("errcode"))) {
			JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jobj1 = jsonArray.getJSONObject(i);
				BdDepartmentFormMap formMap = new BdDepartmentFormMap();
				formMap.put("fnumber", jobj1.getString("fnumber"));
				formMap.put("fname", jobj1.getString("fname"));
				list.add(formMap);
			}
		}
		return list;
	}

	/**
	 * 保管人信息
	 * 
	 * @param department
	 * @return
	 */
	@RequestMapping("getkeeper")
	@ResponseBody
	public List<BdStaffFormMap> getkeeper(String department) {
		List<BdStaffFormMap> list = new ArrayList<BdStaffFormMap>();
		String resultJson = authConfigComponent.getAuthService()
				.gekeeper(SessionUtil.getSessionAttr("organ").toString(), department);
		JSONObject jobj = JSONObject.parseObject(resultJson);
		if ("0".equals(jobj.getString("errcode"))) {
			JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jobj1 = jsonArray.getJSONObject(i);
				BdStaffFormMap formMap = new BdStaffFormMap();
				formMap.put("fname", jobj1.getString("fname"));
				list.add(formMap);
			}
		}
		return list;
	}

	/**
	 * 获取盘点单名称
	 * 
	 * @return
	 */
	@RequestMapping("getPdname")
	@ResponseBody
	public List<LgDForMap> getPdname() {
		LgDForMap lgDForMap = getFormMap(LgDForMap.class);
		lgDForMap.put("organ", SessionUtil.getSessionAttr("organ").toString());
		return lgDMapper.findAllPage(lgDForMap);
	}
}