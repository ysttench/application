package com.ysttench.application.auth.settings.web.controller.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ysttench.application.auth.core.rdto.Dprint;
import com.ysttench.application.auth.core.rdto.ToExcel;
import com.ysttench.application.auth.settings.kernel.entity.StkInstockFormMap;
import com.ysttench.application.auth.settings.kernel.entity.SysPrintForMap;
import com.ysttench.application.auth.settings.kernel.mapper.SysPrintMapper;
import com.ysttench.application.auth.settings.web.controller.common.AttrConstants;
import com.ysttench.application.auth.settings.web.controller.common.AuthConfigComponent;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;
import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.JsonUtils;

/**
 * 入库单
 * @author Howard
 */
@Controller
@RequestMapping("/apireceipt/")
public class ApiReceiptController extends BaseController {
	@Inject
	private AuthConfigComponent authConfigComponent;
	@Inject
	private SysPrintMapper sysPrintMapper;
/**
 * 列表页面
 * @return
 * @throws Exception
 */
	@RequestMapping("list")
	public String listUI() throws Exception {
		return ForwardConstants.API + ForwardConstants.RECEPIT + ForwardConstants.LIST;
	}
/**
 * 分页显示
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
		StkInstockFormMap stkInstockFormMap = getFormMap(StkInstockFormMap.class);
		stkInstockFormMap = toFormMap(stkInstockFormMap, pageNow, pageSize, stkInstockFormMap.getStr("orderby"));
		if (pageNow == null) {
			pageNow = "1";
		}
		int rowcount = 0;
		int pagecount = 0;
		int start = 10 * (Integer.parseInt(pageNow) - 1);
		List<StkInstockFormMap> list = new ArrayList<StkInstockFormMap>();
		if (stkInstockFormMap.get("searchValue") == null) {
			stkInstockFormMap.put("searchValue", "");
		}
		String resultJson = authConfigComponent.getAuthService().getGRNBypage(
				SessionUtil.getSessionAttr("organ").toString(), stkInstockFormMap.get("searchValue").toString(),column,sort);
		JSONObject jobj = JSONObject.parseObject(resultJson);
		if ("0".equals(jobj.getString("errcode"))) {
			JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
			for (int j = start; (j < (start + 10)) && (j < jsonArray.size()); j++) {
				JSONObject jobj1 = (JSONObject) jsonArray.get(j);
				if (jsonArray.get(j) != null) {
					StkInstockFormMap map = new StkInstockFormMap();
					map.put("fid", jobj1.getString("fentryid"));
					map.put("FBILLNO", jobj1.getString("fbillno"));
					map.put("FDATE", jobj1.getString("fdate"));
					map.put("FDOCUMENTSTATUS", jobj1.getString("fdocumentstatus"));
					map.put("FNUMBER", jobj1.getString("fnumber"));
					map.put("FNAME", jobj1.getString("fname"));
					map.put("MANE", jobj1.getString("mane"));
					map.put("FLOT", jobj1.getString("flot"));
					map.put("UNITNAME", jobj1.getString("unitname"));
					map.put("FREALQTY", jobj1.getString("frealqty"));
					map.put("ORGNAME", jobj1.getString("orgname"));
					map.put("ORG1NAME", jobj1.getString("org1name"));
					list.add(map);
				}
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
	@SystemLog(module = "入库单管理", methods = "入库单管理-打印条码")
	// 凡需要处理业务逻辑的.都需要记录操作日志
	public String print() throws Exception {
		Dprint dp = new Dprint();
		String organ = SessionUtil.getSessionAttr("organ").toString();
		SysPrintForMap sysPrintForMap = new SysPrintForMap();
		sysPrintForMap.put("organ", organ);
		sysPrintForMap.put("type", "0");
		SysPrintForMap print = sysPrintMapper.findDetail(sysPrintForMap).get(0);
		String[] ids1 = SessionUtil.getParaValues("ids");
		for(int i=0;i<ids1.length;i++) {
			String resultJson = authConfigComponent.getAuthService().getDprintMsg(ids1[i]);
			JSONObject jobj = JSONObject.parseObject(resultJson);
			if ("0".equals(jobj.getString("errcode"))) {
				JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
				for (int j = 0; j < jsonArray.size(); j++) {
					JSONObject jobj1 = (JSONObject) jsonArray.get(j);
					String s = "品名:$" + jobj1.getString("fname") + "$料号:$" + jobj1.getString("fnumber") + "$规格:$"
							+ jobj1.getString("fspecification") + "$数量:$"
							+ Double.parseDouble(jobj1.get("frealqty").toString()) + "$批号:$"
							+ jobj1.getString("flotText") + "$S/N:$" + jobj1.getString("fserialno");
					String ewm = jobj1.getString("fnumber") + ":!:" + jobj1.getString("fname") + ":!:"
							+ jobj1.getString("fspecification") + ":!:" + jobj1.getString("flotText") + ":!:"
							+ jobj1.getString("fserialno") + ":!:" + jobj1.getString("frealqty");
					String result = dp.getRemoteInfo(print, organ, s, ewm);
					if (!result.contains("成功")) {
						return result;
					}
				}
			} else {
				return AttrConstants.FAIL;
			}
		}
		return AttrConstants.SUCCESS;
	}
	@RequestMapping("/export")
    public void download(HttpServletRequest request, HttpServletResponse response, String column,String sort) throws IOException {
		StkInstockFormMap stkInstockFormMap = findHasHMap(StkInstockFormMap.class);
		List<StkInstockFormMap> list = new ArrayList<StkInstockFormMap>();
		String resultJson = authConfigComponent.getAuthService().getGRNBypage(
				SessionUtil.getSessionAttr("organ").toString(), null ,column,sort);
		JSONObject jobj = JSONObject.parseObject(resultJson);
		if ("0".equals(jobj.getString("errcode"))) {
			JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
			for (int j = 0;j < jsonArray.size(); j++) {
				JSONObject jobj1 = (JSONObject) jsonArray.get(j);
				if (jsonArray.get(j) != null) {
					StkInstockFormMap map = new StkInstockFormMap();
					map.put("fid", jobj1.getString("fentryid"));
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
					map.put("MANE", jobj1.getString("mane"));
					map.put("FLOT", jobj1.getString("flot"));
					map.put("UNITNAME", jobj1.getString("unitname"));
					map.put("FREALQTY", jobj1.getString("frealqty"));
					map.put("ORGNAME", jobj1.getString("orgname"));
					map.put("ORG1NAME", jobj1.getString("org1name"));
					list.add(map);
				}
			}
		}
        String fileName = "采购入库单";
        String exportData = stkInstockFormMap.getStr("exportData");// 列表头的json字符串
        List<Map<String, Object>> listMap = JsonUtils.parseJSONList(exportData);
        ToExcel toExcel = new ToExcel();
        toExcel.myexportToExcel(response, listMap, list, fileName);
    }
}
