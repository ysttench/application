package com.ysttench.application.auth.settings.web.controller.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ysttench.application.auth.core.rdto.ToExcel;
import com.ysttench.application.auth.settings.kernel.entity.FaCardFormMap;
import com.ysttench.application.auth.settings.kernel.entity.StkInventoryFormMap;
import com.ysttench.application.auth.settings.web.controller.common.AuthConfigComponent;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.JsonUtils;
import com.ysttench.application.common.util.StringUtil;

/**
 * 即时库存
 * @author Howard
 */
@Controller
@RequestMapping("/apinventory/")
public class ApiNventoryController extends BaseController {
	@Inject
	private AuthConfigComponent authConfigComponent;

	/**
	 * 跳转列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	public String listUI() throws Exception {
		return ForwardConstants.API + ForwardConstants.NEVENRY + ForwardConstants.LIST;
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

		StkInventoryFormMap stkInventoryFormMap = getFormMap(StkInventoryFormMap.class);
		stkInventoryFormMap = toFormMap(stkInventoryFormMap, pageNow, pageSize, stkInventoryFormMap.getStr("orderby"));
		if (pageNow == null) {
			pageNow = "1";
		}
		int rowcount = 0;
		int pagecount = 0;
		int start = 10 * (Integer.parseInt(pageNow) - 1);
		List<StkInventoryFormMap> list = new ArrayList<StkInventoryFormMap>();
		if (stkInventoryFormMap.get("searchValue") == null) {
			stkInventoryFormMap.put("searchValue", "");
		}
		String resultJson = authConfigComponent.getAuthService().getkucunBypage(
				SessionUtil.getSessionAttr("organ").toString(), stkInventoryFormMap.get("searchValue").toString(),
				column, sort);
		JSONObject jobj = JSONObject.parseObject(resultJson);
		if ("0".equals(jobj.getString("errcode"))) {
			JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
			for (int j = start; (j < (start + 10)) && (j < jsonArray.size()); j++) {
				JSONObject jobj1 = (JSONObject) jsonArray.get(j);
				StkInventoryFormMap map = new StkInventoryFormMap();
				map.put("FNUMBER", jobj1.getString("fnumber"));
				map.put("FNAME", jobj1.getString("fname"));
				map.put("MNAME", jobj1.getString("mname"));
				map.put("FLOT", jobj1.getString("flot"));
				map.put("UNITNAME", jobj1.getString("unitname"));
				map.put("ORGNAME", jobj1.getString("orgname"));
				map.put("FBASEQTY", jobj1.getString("fbaseqty"));
				list.add(map);
			}
			rowcount = jsonArray.size();
			if ((rowcount % 10) != 0) {
				pagecount = rowcount / 10 + 1;
			} else {
				pagecount = rowcount / 10;
			}
		}
		pageView.setRowCount(rowcount);
		pageView.setPageCount(pagecount);
		pageView.setRecords(list);
		return pageView;
	}
	 @RequestMapping("/export")
	    public void download(HttpServletRequest request, HttpServletResponse response, String column,String sort) throws IOException {

			StkInventoryFormMap stkInventoryFormMap = findHasHMap(StkInventoryFormMap.class);
			List<StkInventoryFormMap> list = new ArrayList<StkInventoryFormMap>();
			String resultJson = authConfigComponent.getAuthService().getkucunBypage(
					SessionUtil.getSessionAttr("organ").toString(), null,
					column, sort);
			JSONObject jobj = JSONObject.parseObject(resultJson);
			if ("0".equals(jobj.getString("errcode"))) {
				JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
				for (int j = 0; j < jsonArray.size(); j++) {
					JSONObject jobj1 = (JSONObject) jsonArray.get(j);
					StkInventoryFormMap map = new StkInventoryFormMap();
					map.put("FNUMBER", jobj1.getString("fnumber"));
					map.put("FNAME", jobj1.getString("fname"));
					map.put("MNAME", jobj1.getString("mname"));
					map.put("FLOT", jobj1.getString("flot"));
					map.put("UNITNAME", jobj1.getString("unitname"));
					map.put("ORGNAME", jobj1.getString("orgname"));
					map.put("FBASEQTY", jobj1.getString("fbaseqty"));
					list.add(map);
				}
			}   
		    String fileName = "即时库存";
	        String exportData = stkInventoryFormMap.getStr("exportData");// 列表头的json字符串
	        List<Map<String, Object>> listMap = JsonUtils.parseJSONList(exportData);
	        ToExcel toExcel = new ToExcel();
	        toExcel.myexportToExcel(response, listMap, list, fileName);
	    }
}
