package com.ysttench.application.web.core.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ysttench.application.auth.core.rdto.ResponseDto;
import com.ysttench.application.auth.settings.kernel.entity.BdDepartmentFormMap;
import com.ysttench.application.auth.settings.kernel.entity.BdStaffFormMap;
import com.ysttench.application.auth.settings.kernel.entity.BdStockFormMap;
import com.ysttench.application.auth.settings.kernel.entity.CountInputFormMap;
import com.ysttench.application.auth.settings.kernel.entity.FaCardFormMap;
import com.ysttench.application.auth.settings.kernel.entity.PpBomFormMap;
import com.ysttench.application.auth.settings.kernel.entity.PrdPickmtrlFormMap;
import com.ysttench.application.auth.settings.kernel.entity.StkInstockFormMap;
import com.ysttench.application.auth.settings.kernel.entity.StkInventoryFormMap;
import com.ysttench.application.auth.settings.kernel.mapper.BdDepartmentMapper;
import com.ysttench.application.auth.settings.kernel.mapper.BdStaffMapper;
import com.ysttench.application.auth.settings.kernel.mapper.BdStockMapper;
import com.ysttench.application.auth.settings.kernel.mapper.CountInputMapper;
import com.ysttench.application.auth.settings.kernel.mapper.FaCardMapper;
import com.ysttench.application.auth.settings.kernel.mapper.PpBomMapper;
import com.ysttench.application.auth.settings.kernel.mapper.PrdPickmtrlMapper;
import com.ysttench.application.auth.settings.kernel.mapper.StkInstockMapper;
import com.ysttench.application.auth.settings.kernel.mapper.StkInventoryMapper;
import com.ysttench.application.common.util.JsonUtils;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.web.core.service.AuthService;

/**
 * 接口实现
 * 
 * @author Howard
 * 
 */
@WebService(targetNamespace = "http://service.core.web.application.ysttench.com/", endpointInterface = "com.ysttench.application.web.core.service.AuthService")
public class AuthServiceImpl implements AuthService  {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Inject
	BdStockMapper bdStockMapper;
	@Inject
	StkInstockMapper stkInstockMapper;
	@Inject
	StkInventoryMapper stkInventoryMapper;
	@Inject
	PrdPickmtrlMapper prdPickmtrlMapper;
	@Inject
	FaCardMapper faCardMapper;
	@Inject
	PpBomMapper ppBomMapper;
	@Inject
	CountInputMapper countInputMapper;
	@Inject
	BdDepartmentMapper bdDepartmentMapper;
	@Inject
	BdStaffMapper bdStaffMapper;

	/**
	 * 获取仓库信息
	 * 
	 * @param name
	 * @return
	 */
	@Override
	public String getcangku(String organ) {
		ResponseDto responseDto = new ResponseDto();
		try {
			BdStockFormMap bdStockFormMap = new BdStockFormMap();
			bdStockFormMap.put("organ", organ);
			List<BdStockFormMap> list = bdStockMapper.findBdStockMsg(bdStockFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取仓库信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无仓库信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取仓库信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	/**
	 * 获取入库单信息
	 * @param pageNow
	 * @param search
	 * @return
	 */
	@Override
	public String getGRN(String organ,String date) {
		ResponseDto responseDto = new ResponseDto();
		try {
			StkInstockFormMap stkInstockFormMap = new StkInstockFormMap();
			stkInstockFormMap.put("organ", organ);
			if (!StringUtil.isEmpty(date)) {
				stkInstockFormMap.put(date, date);
			}
			List<StkInstockFormMap> list = stkInstockMapper.findStkinsStockMsg(stkInstockFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取入库单信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无入库单信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取入库单信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	/**
	 * 获取出库单信息
	 * @param pageNow
	 * @param search
	 * @return
	 */
	@Override
	public String getCRN(String organ,String date,String searchValue) {
		ResponseDto responseDto = new ResponseDto();
		try {
			PrdPickmtrlFormMap prdPickmtrlFormMap = new PrdPickmtrlFormMap();
			prdPickmtrlFormMap.put("organ", organ);
			prdPickmtrlFormMap.put("searchValue", searchValue);
			prdPickmtrlFormMap.put(date, date);
			List<PrdPickmtrlFormMap> list = prdPickmtrlMapper.findprdMsg(prdPickmtrlFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取生产领料单信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无生产领料单信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取生产领料单信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	/**
	 * 获取打印条码信息
	 * @param fbillno
	 * @return
	 */
	@Override
	public String getprintMsg(String fbillno,String dgillno) {
		ResponseDto responseDto = new ResponseDto();
		try {
			StkInstockFormMap stkInstockFormMap = new StkInstockFormMap();
			stkInstockFormMap.put("fbillno", fbillno);
			stkInstockFormMap.put("dgillno", dgillno);
			List<StkInstockFormMap> list = stkInstockMapper.findDetailBdStockMsg(stkInstockFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取入库单物料信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无入库单物料信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取入库单物料信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	/**
	 * 获取库存信息
	 * @param organ
	 * @param pageNow
	 * @param search
	 * @return
	 */
	@Override
	public String getkucun(String organ,String ck) {
		ResponseDto responseDto = new ResponseDto();
		try {
			StkInventoryFormMap stkInventoryFormMap = new StkInventoryFormMap();
			stkInventoryFormMap.put("organ", organ);
			stkInventoryFormMap.put("ck", ck);
			List<StkInventoryFormMap> list = stkInventoryMapper.findventoryMsg(stkInventoryFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取库存信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无库存信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取库存信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	/**
	 * 获取出库单详情信息
	 * @param organ
	 * @param pageNow
	 * @param search
	 * @return
	 */
	@Override
	public String getchuku(String fid) {
		ResponseDto responseDto = new ResponseDto();
		try {
			PrdPickmtrlFormMap pickmtrlFormMap = new PrdPickmtrlFormMap();
			pickmtrlFormMap.put("fid", fid);
			List<PrdPickmtrlFormMap> list = prdPickmtrlMapper.findDetailpicMsg(pickmtrlFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取出库单物料信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无出库单物料信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取出库单物料信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String getGRNBypage(String organ, String searchValue, String column, String sort) {
		ResponseDto responseDto = new ResponseDto();
		try {
			StkInstockFormMap stkInstockFormMap = new StkInstockFormMap();
			stkInstockFormMap.put("organ", organ);
			stkInstockFormMap.put("searchValue", searchValue);
			stkInstockFormMap.put("column", column);
			stkInstockFormMap.put("sort", sort);
			List<StkInstockFormMap> list = stkInstockMapper.findStkinsStockMsgByPage(stkInstockFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取分页入库单信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无分页入库单信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取分页入库单信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String getCRNBypage(String organ, String searchValue, String column, String sort) {
		ResponseDto responseDto = new ResponseDto();
		try {
			PrdPickmtrlFormMap prdPickmtrlFormMap = new PrdPickmtrlFormMap();
			prdPickmtrlFormMap.put("organ", organ);
			prdPickmtrlFormMap.put("searchValue", searchValue);
			prdPickmtrlFormMap.put("column", column);
			prdPickmtrlFormMap.put("sort", sort);
			List<PrdPickmtrlFormMap> list = prdPickmtrlMapper.findprdMsgByPage(prdPickmtrlFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取生产领料单信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无生产领料单信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取生产领料单信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String getkucunBypage(String organ, String searchValue, String column, String sort) {
		ResponseDto responseDto = new ResponseDto();
		try {
			StkInventoryFormMap stkInventoryFormMap = new StkInventoryFormMap();
			stkInventoryFormMap.put("organ", organ);
			stkInventoryFormMap.put("searchValue", searchValue);
			stkInventoryFormMap.put("column", column);
			stkInventoryFormMap.put("sort", sort);
			List<StkInventoryFormMap> list = stkInventoryMapper.findventoryMsgByPage(stkInventoryFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取库存信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无库存信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取库存信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String getgudin(String organ) {
		ResponseDto responseDto = new ResponseDto();
		try {
			FaCardFormMap faCardFormMap = new FaCardFormMap();
			faCardFormMap.put("organ", organ);
			List<FaCardFormMap> list = faCardMapper.findGDMsg(faCardFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取固定资产信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无固定资产信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取固定资产信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String getgudinBypage(String organ,String searchValue,String column, String sort) {
		ResponseDto responseDto = new ResponseDto();
		try {
			FaCardFormMap faCardFormMap = new FaCardFormMap();
			faCardFormMap.put("organ", organ);
			faCardFormMap.put("column", column);
			faCardFormMap.put("sort", sort);
				faCardFormMap.put("searchValue", searchValue);
			List<FaCardFormMap> list = faCardMapper.findGDMsgByPage(faCardFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取固定资产信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无固定资产信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取固定资产信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String getDetailMateril(String fid, String id) {
		ResponseDto responseDto = new ResponseDto();
		try {
			PrdPickmtrlFormMap prdPickmtrlFormMap = new PrdPickmtrlFormMap();
			prdPickmtrlFormMap.put("fid", fid);
			if(!StringUtil.isEmpty(id)) {
			prdPickmtrlFormMap.put("fentryid", id);
			}
			List<PrdPickmtrlFormMap> list = prdPickmtrlMapper.findDetailprdMsg(prdPickmtrlFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取保存信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无保存信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取保存信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String getDprintMsgE(String id) {
		ResponseDto responseDto = new ResponseDto();
		try {
			FaCardFormMap faCardFormMap = new FaCardFormMap();
			faCardFormMap.put("id", id);
			List<FaCardFormMap> list = faCardMapper.findDetailCardMsg(faCardFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取固定资产信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无固定资产信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取固定资产信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String getGDZC(String organ) {
		ResponseDto responseDto = new ResponseDto();
		try {
			FaCardFormMap faCardFormMap = new FaCardFormMap();
			faCardFormMap.put("organ", organ);
			List<FaCardFormMap> list = faCardMapper.findGDMsg(faCardFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取固定资产信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无固定资产信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取固定资产信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String getprint2Msg(String id) {
		ResponseDto responseDto = new ResponseDto();
		try {
			FaCardFormMap faCardFormMap = new FaCardFormMap();
			faCardFormMap.put("id", id);
			List<FaCardFormMap> list = faCardMapper.findDetailCardMsg(faCardFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取固定资产信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无固定资产信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取固定资产信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String getDprintMsg(String fid) {
		ResponseDto responseDto = new ResponseDto();
		try {
			StkInstockFormMap stkInstockFormMap = new StkInstockFormMap();
			stkInstockFormMap.put("fid", fid);
			List<StkInstockFormMap> list = stkInstockMapper.findDetailBdStockMsg(stkInstockFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取入库单物料信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无入库单物料信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取入库单物料信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String getBom(String flot, String fmobillno,String organ) {
		ResponseDto responseDto = new ResponseDto();
		try {
			PpBomFormMap ppBomFormMap = new PpBomFormMap();
			ppBomFormMap.put("organ", organ);
			ppBomFormMap.put("flot", flot);
			ppBomFormMap.put("fmobillno", fmobillno);
			List<PpBomFormMap> list = ppBomMapper.selectBom(ppBomFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取生产用料清单信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无生产用料清单信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取生产用料清单信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String getDetailBom(String fentryid,String organ) {
		ResponseDto responseDto = new ResponseDto();
		try {
			PpBomFormMap ppBomFormMap = new PpBomFormMap();
			ppBomFormMap.put("fentryid", fentryid);
			ppBomFormMap.put("organ", organ);
			List<PpBomFormMap> list = ppBomMapper.findDetailBom(ppBomFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取生产用料清单信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无生产用料清单信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取生产用料清单信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String getorg(String organ) {
		ResponseDto responseDto = new ResponseDto();
		try {
			PpBomFormMap ppBomFormMap = new PpBomFormMap();
			ppBomFormMap.put("organ", organ);
			List<PpBomFormMap> list = ppBomMapper.getorg(ppBomFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取组织编码成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无组织编码信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取组织编码信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String getcw(String stockid) {
		ResponseDto responseDto = new ResponseDto();
		try {
			BdStockFormMap bdStockFormMap = new BdStockFormMap();
			bdStockFormMap.put("stockid", stockid);
			List<BdStockFormMap> list = bdStockMapper.getcw(bdStockFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取仓位信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无仓位信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取仓位信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String getPd(String organ, String date) {
		ResponseDto responseDto = new ResponseDto();
		try {
			CountInputFormMap countInputFormMap = new CountInputFormMap();
			countInputFormMap.put("organ", organ);
			List<CountInputFormMap> list = countInputMapper.findMsg(countInputFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取盘点单信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无盘点单信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取盘点单信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String getdePd(String fid,String stock) {
		ResponseDto responseDto = new ResponseDto();
		try {
			CountInputFormMap countInputFormMap = new CountInputFormMap();
			countInputFormMap.put("fid", fid);
			countInputFormMap.put("stock", stock);
			List<CountInputFormMap> list = countInputMapper.findDetaiMsg(countInputFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取盘点单信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无盘点单信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取盘点单信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}

	@Override
	public String checkPd(String fid,String accnumber, String fentryids) {
		ResponseDto responseDto = new ResponseDto();
		try {
			CountInputFormMap formap = new CountInputFormMap();
			formap.put("fid", fid);
			List<CountInputFormMap> lis = countInputMapper.findpd(formap);
			if (("B".equals(lis.get(0).get("fdocumentstatus").toString())||"C".equals(lis.get(0).get("fdocumentstatus").toString()))) {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("该盘点单现状态无法进行修改");
			}else {
				String[] number = accnumber.split(",");
				String[] fentryid = fentryids.split(",");
				for (int i = 0; i < number.length; i++) {
					CountInputFormMap countInputFormMap = new CountInputFormMap();
					countInputFormMap.put("fentryid", fentryid[i]);
					List<CountInputFormMap> list = countInputMapper.findpd(countInputFormMap);
					Double Zcnumber = Double.parseDouble(list.get(0).get("facctqty").toString());
					int ch =(int) (Double.parseDouble(number[i]) - Zcnumber);
					Double wc =  (Double) (ch/Zcnumber)*100;
					if (ch > 0) {
						countInputFormMap.put("fgainqty", ch);
						countInputFormMap.put("fbasegainqty", ch);
						countInputFormMap.put("fsecgainqty", ch);
						countInputFormMap.put("fextsecgainqty", ch);
						countInputFormMap.put("flossqty", "0");
						countInputFormMap.put("fbaselossqty", "0");
						countInputFormMap.put("fextseclossqty", "0");
						countInputFormMap.put("fseclossqty", "0");
					}else {
						int ch1 = 0 - ch;						
						countInputFormMap.put("flossqty", ch1);
						countInputFormMap.put("fbaselossqty", ch1);
						countInputFormMap.put("fextseclossqty", ch1);
						countInputFormMap.put("fseclossqty", ch1);
						countInputFormMap.put("fgainqty", "0");
						countInputFormMap.put("fbasegainqty", "0");
						countInputFormMap.put("fsecgainqty", "0");
						countInputFormMap.put("fextsecgainqty", "0");
					}
					countInputFormMap.put("fcounterror", wc);
					countInputFormMap.put("fcountqty", number[i]);
					countInputFormMap.put("fbasecountqty", number[i]);
					countInputFormMap.put("fseccountqty", number[i]);
					countInputMapper.edit(countInputFormMap);
				}
				responseDto.setErrcode("0");
				responseDto.setErrmsg("提交盘点信息成功");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("提交盘点信息异常");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String getGdPd(String organ, String date) {
		ResponseDto responseDto = new ResponseDto();
		try {
			FaCardFormMap faCardFormMap = new FaCardFormMap();
			faCardFormMap.put("organ", organ);
			List<FaCardFormMap> list = faCardMapper.findGdPdMsg(faCardFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取盘点单信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无盘点单信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取盘点单信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	
	}
	@Override
	public String checkGdPd(String fid, String accnumber, String fentryids) {

		ResponseDto responseDto = new ResponseDto();
		try {
			FaCardFormMap formap = new FaCardFormMap();
			formap.put("fid", fid);
			List<FaCardFormMap> lis = faCardMapper.findGdpd(formap);
			if (!("A".equals(lis.get(0).get("fdocumentstatus").toString())||"Z".equals(lis.get(0).get("fdocumentstatus").toString()))) {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("现状态盘点单无法进行修改");
			}else {
				String[] number = accnumber.split(",");
				String[] fentryid = fentryids.split(",");
				for (int i = 0; i < number.length; i++) {
					FaCardFormMap faformap = new FaCardFormMap();
					faformap.put("fentryid", fentryid[i]);
					List<FaCardFormMap> list = faCardMapper.findGdpd(faformap);
					Double Zcnumber = Double.parseDouble(list.get(0).get("fbookqty").toString());
					int ch =(int) (Double.parseDouble(number[i])-Zcnumber);
					faformap.put("fvariance", ch);
					faformap.put("finitqty", number[i]);
					faCardMapper.edit(faformap);
				}
				responseDto.setErrcode("0");
				responseDto.setErrmsg("提交盘点信息成功");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("提交盘点信息异常");
		}
		return JsonUtils.bean2json(responseDto);
	
	}
	@Override
	public String getdeGdPd(String fid) {
		ResponseDto responseDto = new ResponseDto();
		try {
			FaCardFormMap faCardFormMap = new FaCardFormMap();
			faCardFormMap.put("fid", fid);
			List<FaCardFormMap> list = faCardMapper.findDetaiGdPdMsg(faCardFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取盘点单信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无盘点单信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取盘点单信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	
	}
	@Override
	public String getdePdCk(String fid) {
		ResponseDto responseDto = new ResponseDto();
		try {
			CountInputFormMap countInputFormMap = new CountInputFormMap();
			countInputFormMap.put("fid", fid);
			List<CountInputFormMap> list = countInputMapper.findCkMsg(countInputFormMap);
			if (list.size() != 0 && list != null) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取盘点单仓库信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("暂无盘点单仓库信息!");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取盘点单仓库信息异常!");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String gedepartment(String organ) {
		ResponseDto responseDto = new ResponseDto();
		try {
			BdDepartmentFormMap formMap = new BdDepartmentFormMap();
			formMap.put("organ", organ);
			List<BdDepartmentFormMap> list = bdDepartmentMapper.findMsg(formMap);
		if (list.size() != 0 && list != null) {
			responseDto.setErrcode("0");
			responseDto.setErrmsg("获取部门信息成功");
			responseDto.setResponseObject(list);
		} else {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("暂无部门信息!");
		}
	} catch (Exception e) {
		responseDto.setErrcode("1");
		responseDto.setErrmsg("获取部门信息异常!");
	}
	return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String gekeeper(String organ,String department){
		ResponseDto responseDto = new ResponseDto();
		try {
			BdStaffFormMap formMap = new BdStaffFormMap();
			formMap.put("organ", organ);
			formMap.put("department", department);
			List<BdStaffFormMap> list = bdStaffMapper.findMsg(formMap);
		if (list.size() != 0 && list != null) {
			responseDto.setErrcode("0");
			responseDto.setErrmsg("获取盘点单仓库信息成功");
			responseDto.setResponseObject(list);
		} else {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("暂无盘点单仓库信息!");
		}
	} catch (Exception e) {
		responseDto.setErrcode("1");
		responseDto.setErrmsg("获取盘点单仓库信息异常!");
	}
	return JsonUtils.bean2json(responseDto);
	}
}