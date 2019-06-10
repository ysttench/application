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
import com.ysttench.application.auth.settings.kernel.entity.PrdPickmtrlFormMap;
import com.ysttench.application.auth.settings.web.controller.common.AuthConfigComponent;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.JsonUtils;

/**
 * 出库单
 * @author Howard
 */
@Controller
@RequestMapping("/product/")
public class ApiProductController extends BaseController {
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
		return ForwardConstants.API + ForwardConstants.PRODUCT + ForwardConstants.LIST;
	}

	/**
	 * 分页显示页面
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
		PrdPickmtrlFormMap prdPickmtrlFormMap = getFormMap(PrdPickmtrlFormMap.class);
		prdPickmtrlFormMap = toFormMap(prdPickmtrlFormMap, pageNow, pageSize, prdPickmtrlFormMap.getStr("orderby"));
		if (pageNow == null) {
			pageNow = "1";
		}
		int rowcount = 0;
		int pagecount = 0;
		int start = 10 * (Integer.parseInt(pageNow) - 1);
		if (prdPickmtrlFormMap.get("searchValue") == null) {
			prdPickmtrlFormMap.put("searchValue", "");
		}
		List<PrdPickmtrlFormMap> list = new ArrayList<PrdPickmtrlFormMap>();
		String resultJson = authConfigComponent.getAuthService().getCRNBypage(
				SessionUtil.getSessionAttr("organ").toString(), prdPickmtrlFormMap.get("searchValue").toString(),
				column, sort);
		JSONObject jobj = JSONObject.parseObject(resultJson);
		if ("0".equals(jobj.getString("errcode"))) {
			JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
			for (int j = start; (j < (start + 10)) && (j < jsonArray.size()); j++) {
				JSONObject jobj1 = (JSONObject) jsonArray.get(j);
				PrdPickmtrlFormMap map = new PrdPickmtrlFormMap();
				map.put("FBILLNO", jobj1.getString("fbillno"));
				map.put("FDATE", jobj1.getString("fdate"));
				map.put("FDOCUMENTSTATUS", jobj1.getString("fdocumentstatus"));
				map.put("FNUMBER", jobj1.getString("fnumber"));
				map.put("FNAME", jobj1.getString("fname"));
				map.put("FLOT", jobj1.getString("flot"));
				map.put("UNITNAME", jobj1.getString("unitname"));
				if ("0E-10".equals(jobj1.getString("fstockactualqty"))) {
					map.put("FSTOCKACTUALQTY", "0");
				} else {
					map.put("FSTOCKACTUALQTY", jobj1.getString("fstockactualqty"));
				}
				map.put("ORGNAME", jobj1.getString("orgname"));
				map.put("MANE", jobj1.getString("mane"));
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
		PrdPickmtrlFormMap prdPickmtrlFormMap = findHasHMap(PrdPickmtrlFormMap.class);
		List<PrdPickmtrlFormMap> list = new ArrayList<PrdPickmtrlFormMap>();
		String resultJson = authConfigComponent.getAuthService().getCRNBypage(
				SessionUtil.getSessionAttr("organ").toString(), null,
				column, sort);
		JSONObject jobj = JSONObject.parseObject(resultJson);
		if ("0".equals(jobj.getString("errcode"))) {
			JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
			for (int j = 0; j < jsonArray.size(); j++) {
				JSONObject jobj1 = (JSONObject) jsonArray.get(j);
				PrdPickmtrlFormMap map = new PrdPickmtrlFormMap();
				map.put("FBILLNO", jobj1.getString("fbillno"));
				map.put("FDATE", jobj1.getString("fdate"));
				if (jobj1.getString("fdocumentstatus").equals("A")) {
					map.put("FDOCUMENTSTATUS", "新建");
				}else if (jobj1.getString("fdocumentstatus").equals("B")) {
					map.put("FDOCUMENTSTATUS", "审核中");
				}else if (jobj1.getString("fdocumentstatus").equals("C")) {
					map.put("FDOCUMENTSTATUS", "已审核");
				}else if (jobj1.getString("fdocumentstatus").equals("D")) {
					map.put("FDOCUMENTSTATUS", "重新审核");
				}else if (jobj1.getString("fdocumentstatus").equals("Z")) {
					map.put("FDOCUMENTSTATUS", "暂存");
				}
				map.put("FNUMBER", jobj1.getString("fnumber"));
				map.put("FNAME", jobj1.getString("fname"));
				map.put("FLOT", jobj1.getString("flot"));
				map.put("UNITNAME", jobj1.getString("unitname"));
				if ("0E-10".equals(jobj1.getString("fstockactualqty"))) {
					map.put("FSTOCKACTUALQTY", "0");
				} else {
					map.put("FSTOCKACTUALQTY", jobj1.getString("fstockactualqty"));
				}
				map.put("ORGNAME", jobj1.getString("orgname"));
				map.put("MANE", jobj1.getString("mane"));
				list.add(map);
			}
			}
		String fileName = "生产领料";
        String exportData = prdPickmtrlFormMap.getStr("exportData");// 列表头的json字符串
        List<Map<String, Object>> listMap = JsonUtils.parseJSONList(exportData);
        ToExcel toExcel = new ToExcel();
        toExcel.myexportToExcel(response, listMap, list, fileName);
    }
}
