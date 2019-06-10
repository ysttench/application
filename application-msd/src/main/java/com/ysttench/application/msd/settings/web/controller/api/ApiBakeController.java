package com.ysttench.application.msd.settings.web.controller.api;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.msd.settings.kernel.entity.ApiBakeFormMap;
import com.ysttench.application.msd.settings.kernel.entity.ApiMsdCVFormMap;
import com.ysttench.application.msd.settings.kernel.mapper.ApiBakeMapper;
import com.ysttench.application.msd.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.msd.settings.web.controller.index.BaseController;

/**
 * 烘烤参数设定
 * 
 * @author Howard
 *
 */
@Controller
@RequestMapping("/bake/")
public class ApiBakeController extends BaseController {
	@Inject
	ApiBakeMapper apiBakeMapper;

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		return ForwardConstants.API + ForwardConstants.BAKE + ForwardConstants.LIST;
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public PageView findByPage(String pageNow, String pageSize, String column, String sort) throws Exception {
		ApiBakeFormMap apiBakeFormMap = getFormMap(ApiBakeFormMap.class);
		apiBakeFormMap = toFormMap(apiBakeFormMap, pageNow, pageSize, apiBakeFormMap.getStr("orderby"));
		apiBakeFormMap.put("column", StringUtil.prop2tablefield(column));
		apiBakeFormMap.put("sort", sort);
		List<ApiBakeFormMap> alist = new ArrayList<ApiBakeFormMap>();
		List<ApiBakeFormMap> list = apiBakeMapper.findbakePage(apiBakeFormMap);
		for (ApiBakeFormMap map : list) {
			if (!"0".equals(map.get("minThick").toString())) {
				if (map.get("minThick").toString().equals(map.get("maxThick").toString())) {
					map.put("thick", "厚度 :" + map.get("maxThick").toString());
				} else {
					map.put("thick", map.get("minThick").toString() + "< 厚度 <=" + map.get("maxThick").toString());
				}
			} else {
				map.put("thick", "厚度 <=" + map.get("maxThick").toString());
			}
			if (!"0".equals(map.get("minThick").toString())) {
				if (map.get("minPlant").toString().equals(map.get("maxPlant").toString())) {
					map.put("plant", "超出车间寿命 :" + map.get("maxPlant").toString());
				} else {
					map.put("plant", map.get("minPlant").toString() + "< 超出车间寿命 <=" + map.get("maxPlant").toString());

				}
			} else {
				map.put("plant", "超出车间寿命 <=" + map.get("maxPlant").toString());
			}
			if (!"0".equals(map.get("minThick").toString())) {
				if (map.get("minRh").toString().equals(map.get("maxRh").toString())) {
					map.put("rh", " 湿度 :" + map.get("maxRh").toString());
				} else {
					map.put("rh", map.get("minRh").toString() + "< 湿度 <=" + map.get("maxRh").toString());
				}
			} else {
				map.put("rh", "湿度 <=" + map.get("maxRh").toString());
			}
			if (!"0".equals(map.get("minTemple").toString())) {
				if (map.get("minTemple").toString().equals(map.get("maxTemple").toString())) {
					map.put("temple", "温度:" + map.get("minTemple").toString());
				} else {
					map.put("temple", map.get("minRh").toString() + "< 温度 <=" + map.get("maxTemple").toString());
				}
			} else {
				map.put("temple", " 温度 <=" + map.get("maxTemple").toString());
			}
			alist.add(map);
		}
		pageView.setRecords(apiBakeMapper.findbakePage(apiBakeFormMap));// 不调用默认分页,调用自已的mapper中findUserPage
		return pageView;
	}
}
