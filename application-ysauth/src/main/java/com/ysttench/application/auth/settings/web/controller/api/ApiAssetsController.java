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
import com.ysttench.application.auth.settings.kernel.entity.ApiStAssetsForMap;
import com.ysttench.application.auth.settings.kernel.entity.FaCardFormMap;
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
import com.ysttench.application.common.util.StringUtil;

/**
 * 固定资产
 * @author Howard
 */
@Controller
@RequestMapping("/assets/")
public class ApiAssetsController extends BaseController {
	@Inject
	private AuthConfigComponent authConfigComponent;
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
		return ForwardConstants.API + ForwardConstants.ASSETS + ForwardConstants.LIST;
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
		FaCardFormMap faCardFormMap = getFormMap(FaCardFormMap.class);
		faCardFormMap = toFormMap(faCardFormMap, pageNow, pageSize, faCardFormMap.getStr("orderby"));
		if (pageNow == null) {
			pageNow = "1";
		}
		int rowcount = 0;
		int pagecount = 0;
		int start = 10 * (Integer.parseInt(pageNow) - 1);
		List<FaCardFormMap> list = new ArrayList<FaCardFormMap>();
		if (faCardFormMap.get("searchValue") == null) {
			faCardFormMap.put("searchValue", "");
		}
		String resultJson = authConfigComponent.getAuthService().getgudinBypage(
				SessionUtil.getSessionAttr("organ").toString(), faCardFormMap.get("searchValue").toString(), column,
				sort);
		JSONObject jobj = JSONObject.parseObject(resultJson);
		if ("0".equals(jobj.getString("errcode"))) {
			JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
			for (int j = start; (j < (start + 10)) && (j < jsonArray.size()); j++) {
				JSONObject jobj1 = (JSONObject) jsonArray.get(j);
				if (jsonArray.get(j) != null) {
					FaCardFormMap map = new FaCardFormMap();
					map.put("unitname", jobj1.getString("unitname"));
					map.put("placename", jobj1.getString("placename"));
					map.put("fassetno", jobj1.getString("fassetno"));
					map.put("id", jobj1.getString("fpkid"));
					map.put("fname", jobj1.getString("fname"));
					map.put("fcreatedate", jobj1.getString("fcreatedate"));
					map.put("fnumber", jobj1.getString("fnumber"));
					map.put("fquantity", jobj1.getString("fquantity"));
					map.put("typename", jobj1.getString("typename"));
					if (StringUtil.isEmpty(jobj1.getString("changename"))) {
						map.put("username", jobj1.getString("username"));
					} else {
						map.put("username", jobj1.getString("changename"));
					}
					map.put("departname", jobj1.getString("departname"));
					map.put("orgname", jobj1.getString("orgname"));
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
		sysPrintForMap.put("type", "1");
		SysPrintForMap print = sysPrintMapper.findDetail(sysPrintForMap).get(0);
		String[] ids1 = SessionUtil.getParaValues("ids");
		for (int i = 0; i < ids1.length; i++) {
			String resultJson = authConfigComponent.getAuthService().getDprintMsgE(ids1[i]);
			JSONObject jobj = JSONObject.parseObject(resultJson);
			if ("0".equals(jobj.getString("errcode"))) {
				JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
				for (int j = 0; j < jsonArray.size(); j++) {
					String name = "";
					JSONObject jobj1 = (JSONObject) jsonArray.get(j);
					if (!StringUtil.isEmpty(jobj1.getString("changename"))) {
						name = jobj1.getString("changename");
					}else {
						name = jobj1.getString("username");
					}
					String s = "资产编码:$" + jobj1.getString("fassetno") + "$资产名称:$" + jobj1.getString("fname") + "$资产位置:$"
							+ jobj1.getString("placename") + "$所属部门:$" + jobj1.get("departname").toString() + "$保管人:$"
							+ name;
					String ewm = jobj1.getString("fassetno") + ":!:" + jobj1.getString("fname") + ":!:"
							+ jobj1.getString("placename") + ":!:" + jobj1.get("departname").toString() + ":!:"
							+ name;
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
	        String fileName = "固定资产";
	        FaCardFormMap faCardFormMap = findHasHMap(FaCardFormMap.class);
	        String exportData = faCardFormMap.getStr("exportData");// 列表头的json字符串
	        List<Map<String, Object>> listMap = JsonUtils.parseJSONList(exportData);
			List<FaCardFormMap> list = new ArrayList<FaCardFormMap>();
			String resultJson = authConfigComponent.getAuthService().getgudinBypage(
					SessionUtil.getSessionAttr("organ").toString(), null, column,
					sort);
			JSONObject jobj = JSONObject.parseObject(resultJson);
			if ("0".equals(jobj.getString("errcode"))) {
				JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
				for (int j = 0; j < jsonArray.size(); j++) {
					JSONObject jobj1 = (JSONObject) jsonArray.get(j);
					if (jsonArray.get(j) != null) {
						FaCardFormMap map = new FaCardFormMap();
						map.put("unitname", jobj1.getString("unitname"));
						map.put("placename", jobj1.getString("placename"));
						map.put("fassetno", jobj1.getString("fassetno"));
						map.put("id", jobj1.getString("fpkid"));
						map.put("fname", jobj1.getString("fname"));
						map.put("fcreatedate", jobj1.getString("fcreatedate"));
						map.put("fnumber", jobj1.getString("fnumber"));
						map.put("fquantity", jobj1.getString("fquantity"));
						map.put("typename", jobj1.getString("typename"));
						if (StringUtil.isEmpty(jobj1.getString("changename"))) {
							map.put("username", jobj1.getString("username"));
						} else {
							map.put("username", jobj1.getString("changename"));
						}
						map.put("departname", jobj1.getString("departname"));
						map.put("orgname", jobj1.getString("orgname"));
						list.add(map);
					}
				}
			}
	        ToExcel toExcel = new ToExcel();
	        toExcel.myexportToExcel(response, listMap, list, fileName);
	    }
}
