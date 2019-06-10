package com.ysttench.application.auth.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ysttench.application.auth.core.rdto.BomInfo;
import com.ysttench.application.auth.core.rdto.CkInfo;
import com.ysttench.application.auth.core.rdto.CwInfo;
import com.ysttench.application.auth.core.rdto.Dprint;
import com.ysttench.application.auth.core.rdto.GdInfo;
import com.ysttench.application.auth.core.rdto.GdPdInfo;
import com.ysttench.application.auth.core.rdto.InvokeHelper;
import com.ysttench.application.auth.core.rdto.KcInfo;
import com.ysttench.application.auth.core.rdto.PdCkInfo;
import com.ysttench.application.auth.core.rdto.PdInfo;
import com.ysttench.application.auth.core.rdto.ResponseDto;
import com.ysttench.application.auth.core.rdto.SlInfo;
import com.ysttench.application.auth.core.rdto.StkInfo;
import com.ysttench.application.auth.core.service.AuthService;
import com.ysttench.application.auth.settings.kernel.entity.ApiStAssetsForMap;
import com.ysttench.application.auth.settings.kernel.entity.LgDForMap;
import com.ysttench.application.auth.settings.kernel.entity.LgForMap;
import com.ysttench.application.auth.settings.kernel.entity.LogUserLoginFormMap;
import com.ysttench.application.auth.settings.kernel.entity.SysPrintForMap;
import com.ysttench.application.auth.settings.kernel.entity.SysSystemForMap;
import com.ysttench.application.auth.settings.kernel.entity.SysUserFormMap;
import com.ysttench.application.auth.settings.kernel.mapper.ApiBdScanentryMapper;
import com.ysttench.application.auth.settings.kernel.mapper.ApiStAssetsMapper;
import com.ysttench.application.auth.settings.kernel.mapper.ApiStkInstockMapper;
import com.ysttench.application.auth.settings.kernel.mapper.LgMapper;
import com.ysttench.application.auth.settings.kernel.mapper.LogUserLoginMapper;
import com.ysttench.application.auth.settings.kernel.mapper.SysPrintMapper;
import com.ysttench.application.auth.settings.kernel.mapper.SysSystemMapper;
import com.ysttench.application.auth.settings.kernel.mapper.SysUserMapper;
import com.ysttench.application.auth.settings.web.controller.common.AuthConfigComponent;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.DatetimeUtil;
import com.ysttench.application.common.util.JsonUtils;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.common.util.encrypt.Encrypt;

/**
 * 接口实现
 * 
 * @author Howard
 * 
 */
@WebService(targetNamespace = "http://service.core.auth.application.ysttench.com/", endpointInterface = "com.ysttench.application.auth.core.service.AuthService")
public class AuthServiceImpl implements AuthService {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Inject
	private ApiStAssetsMapper apiStAssetsMapper;
	@Inject
	ApiBdScanentryMapper apiBdScanentryMapper;
	@Inject
	ApiStkInstockMapper apiStkInstockMapper;
	@Inject
	private LogUserLoginMapper logUserLoginMapper;
	@Inject
	private SysUserMapper sysUserMapper;
	@Inject
	private AuthConfigComponent authConfigComponent;
	@Inject
	private SysSystemMapper sysSystemMapper;
	@Inject
	private SysPrintMapper sysPrintMapper;
	@Inject
	private LgMapper lgMapper;
	// 密码加密
	private Encrypt encrypt = new Encrypt();

	/**
	 * 打印接口
	 * 
	 * @param x
	 * @param y
	 * @param ID
	 * @return
	 */
	@Override
	public String print(String userName, String password, String rk, String dg) {
		ResponseDto responseDto = new ResponseDto();
		try {
			String[] rks = rk.split(",");
			String[] dgs = dg.split(",");
			String organ = check2(userName, password);
			if (!StringUtil.isEmpty(organ)) {
				Dprint dp = new Dprint();
				SysPrintForMap sysPrintForMap = new SysPrintForMap();
				sysPrintForMap.put("organ", organ);
				sysPrintForMap.put("type", "0");
				SysPrintForMap print = sysPrintMapper.findDetail(sysPrintForMap).get(0);
				for (int i = 0; i < rks.length; i++) {
					String resultJson = authConfigComponent.getAuthService().getprintMsg(rks[i], dgs[i]);
					JSONObject jobj = JSONObject.parseObject(resultJson);
					if ("0".equals(jobj.getString("errcode"))) {
						JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
						if (jsonArray.size() > Integer.parseInt(print.get("num").toString())) {
							responseDto.setErrcode("1");
							responseDto.setErrmsg("打印条码数量过多 ,请到后台进行打印");
						} else {
							for (int j = 0; j < jsonArray.size(); j++) {
								JSONObject jobj1 = (JSONObject) jsonArray.get(j);
								String s = "料号:$" + jobj1.getString("fnumber") + "$品名:$" + jobj1.getString("fname")
										+ "$规格:$" + jobj1.getString("fspecification") + "$数量:$"
										+ Double.parseDouble(jobj1.get("frealqty").toString()) + "$批号:$"
										+ jobj1.getString("flotText") + "$S/N:$" + jobj1.getString("fserialno");
								String ewm = jobj1.getString("fnumber") + " :!: " + jobj1.getString("fname") + " :!: "
										+ jobj1.getString("fspecification") + " :!: " + jobj1.getString("flotText")
										+ " :!: " + jobj1.getString("fserialno") + " :!: " + jobj1.getString("frealqty")
										+ " :!: ";
								String result = dp.getRemoteInfo(print, organ, s, ewm);
								if (result == null) {
									responseDto.setErrcode("1");
									responseDto.setErrmsg("打印提交失败");
									System.out.println("第" + i + "条入库单的第" + j + "个物料打印失败");
								} else {
									responseDto.setErrcode("0");
									responseDto.setErrmsg(result);
								}
							}
						}
					} else {
						responseDto.setErrcode("1");
						responseDto.setErrmsg(jobj.getString("errmsg"));
					}
				}
			}
			/*
			 * }
			 */ } catch (Exception e) {
			e.printStackTrace();
			logger.error("打印异常");
			responseDto.setErrcode("1");
			responseDto.setErrmsg("打印异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 入库单显示
	 * 
	 * @param userName
	 * @param password
	 * @param date
	 * @return
	 */
	@Override
	public String showPrint(String userName, String password, String date) {
		ResponseDto responseDto = new ResponseDto();
		try {
			SysUserFormMap userFormMap = new SysUserFormMap();
			userFormMap.put("userName", userName);
			userFormMap.put("menuName", "采购入库单管理");
			List<SysUserFormMap> user = sysUserMapper.perm(userFormMap);
			int count = Integer.parseInt(user.get(0).get("count").toString());
			if (count == 0) {
				responseDto.setErrcode("2");
				responseDto.setErrmsg("该用户无权限查看");
			} else {
				List<StkInfo> list = new ArrayList<StkInfo>();
				String organ = check(userName, password);
				String resultJson = authConfigComponent.getAuthService().getGRN(organ, date);
				JSONObject jobj = JSONObject.parseObject(resultJson);
				if ("0".equals(jobj.getString("errcode"))) {
					JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jobj1 = (JSONObject) jsonArray.get(i);
						StkInfo info = new StkInfo();
						info.setFDATE(jobj1.getString("fdate"));
						info.setFBILLNO(jobj1.getString("fbillno"));
						info.setFREALQTY(jobj1.getString("count"));
						info.setFPOORDERNO(jobj1.getString("fpoorderno"));
						info.setFName(jobj1.getString("fname"));
						info.setFNumber(jobj1.getString("fnumber"));
						list.add(info);
					}
					responseDto.setErrcode("0");
					responseDto.setErrmsg("获取采购入库单信息成功");
					responseDto.setResponseObject(list);
				} else {
					responseDto.setErrcode("1");
					responseDto.setErrmsg(jobj.getString("errmsg"));
				}
			}
			/* } */
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询异常");
			responseDto.setErrcode("1");
			responseDto.setErrmsg("查询异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 登陆验证接口
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	@Override
	public String login(String userName, String password) {
		ResponseDto responseDto = new ResponseDto();
		try {
			SysUserFormMap sysUserFormMap = new SysUserFormMap();
			sysUserFormMap.put("userName", userName);
			List<SysUserFormMap> mps = sysUserMapper.findUserPage(sysUserFormMap);
			if (mps != null && mps.size() > 0) {
				List<SysSystemForMap> systemlist = sysSystemMapper.findSystem(new SysSystemForMap());
				InvokeHelper.POST_K3CloudURL = systemlist.get(0).get("url").toString();
				String dbId = systemlist.get(0).get("dbid").toString();
				int lang = 2052;
				SysUserFormMap tempUserFormMap = mps.get(0);
				if ("1".equals(tempUserFormMap.get("userType").toString())) {
					Map<String, String> map = InvokeHelper.Login(dbId, userName, password, lang);
					if (!(map.get("errcode") == "0")) {
						responseDto.setErrcode("1");
						responseDto.setErrmsg(map.get("Msg").toString());
					} else {
						sysUserFormMap.put("menuName", "仓储盘点明盘");
						List<SysUserFormMap> user1 = sysUserMapper.perm(sysUserFormMap);
						String count1 = user1.get(0).get("count").toString();
						sysUserFormMap.put("menuName", "固定资产盘点明盘");
						List<SysUserFormMap> user2 = sysUserMapper.perm(sysUserFormMap);
						String count2 = user2.get(0).get("count").toString();
						sysUserFormMap.put("menuName", "列管资产盘点明盘");
						List<SysUserFormMap> user3 = sysUserMapper.perm(sysUserFormMap);
						String count3 = user3.get(0).get("count").toString();
						List<String> list2 = new ArrayList<String>();
						sysUserFormMap = tempUserFormMap;
						LogUserLoginFormMap logUserLoginFormMap = new LogUserLoginFormMap();
						logUserLoginFormMap.put("userId", sysUserFormMap.get("userId").toString());
						logUserLoginFormMap.put("userName", userName);
						logUserLoginFormMap.put("loginIp", SessionUtil.getIpAddress());
						logUserLoginMapper.addlog(logUserLoginFormMap);
						list2.add(dbId);
						list2.add(map.get("organid").toString());
						list2.add(map.get("organ").toString());
						list2.add(count1);
						list2.add(count2);
						list2.add(count3);
						responseDto.setErrcode("0");
						responseDto.setErrmsg("登陆成功");
						responseDto.setResponseObject(list2);
					}
				} else {
					if (tempUserFormMap.containsKey("password") && tempUserFormMap.getStr("password") != null) {
						String dbPassword = encrypt.decoder(tempUserFormMap.getStr("password"));
						if (dbPassword.equals(password)) {
							sysUserFormMap.put("menuName", "仓储盘点明盘");
							List<SysUserFormMap> user1 = sysUserMapper.perm(sysUserFormMap);
							String count1 = user1.get(0).get("count").toString();
							sysUserFormMap.put("menuName", "固定资产盘点明盘");
							List<SysUserFormMap> user2 = sysUserMapper.perm(sysUserFormMap);
							String count2 = user2.get(0).get("count").toString();
							sysUserFormMap.put("menuName", "列管资产盘点明盘");
							List<SysUserFormMap> user3 = sysUserMapper.perm(sysUserFormMap);
							String count3 = user3.get(0).get("count").toString();
							List<String> list2 = new ArrayList<String>();
							list2.add(dbId);
							list2.add(tempUserFormMap.get("organId").toString());
							list2.add(tempUserFormMap.get("organ").toString());
							list2.add(count1);
							list2.add(count2);
							list2.add(count3);
							responseDto.setErrcode("0");
							responseDto.setErrmsg("登陆成功");
							responseDto.setResponseObject(list2);
						} else {
							responseDto.setErrcode("1");
							responseDto.setErrmsg("用户名或密码错误 登陆失败");

						}
					}
				}
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("无权限登陆");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("登陆异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 库存查询
	 * 
	 * @param userName
	 * @param password
	 * @param ck
	 * @return
	 */
	@Override
	public String checkKC(String userName, String password, String ck) {
		ResponseDto responseDto = new ResponseDto();
		try {
			SysUserFormMap userFormMap = new SysUserFormMap();
			userFormMap.put("userName", userName);
			userFormMap.put("menuName", "即时库存查询");
			List<SysUserFormMap> user = sysUserMapper.perm(userFormMap);
			int count = Integer.parseInt(user.get(0).get("count").toString());
			if (count == 0) {
				responseDto.setErrcode("2");
				responseDto.setErrmsg("该用户无权限查看");
			} else {
				List<KcInfo> list = new ArrayList<KcInfo>();
				String organ = check(userName, password);
				String resultJson = authConfigComponent.getAuthService().getkucun(organ, ck);
				JSONObject jobj = JSONObject.parseObject(resultJson);
				if ("0".equals(jobj.getString("errcode"))) {
					JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jobj1 = (JSONObject) jsonArray.get(i);
						KcInfo info = new KcInfo();
						info.setFMaterialnumber(jobj1.getString("fnumber"));
						info.setFMaterialNAME(jobj1.getString("fname"));
						info.setFStockIdname(jobj1.getString("mname"));
						info.setFBaseQty(jobj1.getString("count"));
						info.setFMaterialSpecification(jobj1.getString("fspecification"));
						list.add(info);
					}
					responseDto.setErrcode("0");
					responseDto.setErrmsg("获取库存信息成功");
					responseDto.setResponseObject(list);
				} else {
					responseDto.setErrcode("1");
					responseDto.setErrmsg(jobj.getString("errmsg"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询异常");
			responseDto.setErrcode("1");
			responseDto.setErrmsg("查询异常");
		}

		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 出库单查询
	 * 
	 * @param userName
	 * @param password
	 * @param date
	 * @return
	 */
	@Override
	public String checkCK(String userName, String password, String date, String searchValue) {
		ResponseDto responseDto = new ResponseDto();
		try {
			SysUserFormMap userFormMap = new SysUserFormMap();
			userFormMap.put("userName", userName);
			userFormMap.put("menuName", "生产领料单管理");
			List<SysUserFormMap> user = sysUserMapper.perm(userFormMap);
			int count = Integer.parseInt(user.get(0).get("count").toString());
			if (count == 0) {
				responseDto.setErrcode("2");
				responseDto.setErrmsg("该用户无权限查看");
			} else {
				List<SlInfo> list = new ArrayList<SlInfo>();
				String organ = check(userName, password);
				String resultJson = authConfigComponent.getAuthService().getCRN(organ, date, searchValue);
				JSONObject jobj = JSONObject.parseObject(resultJson);
				if ("0".equals(jobj.getString("errcode"))) {
					JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jobj1 = (JSONObject) jsonArray.get(i);
						SlInfo slInfo = new SlInfo();
						slInfo.setFID(jobj1.getString("fid"));
						slInfo.setFBILLNO(jobj1.getString("fbillno"));
						slInfo.setFDATE(jobj1.getString("fdate"));
						slInfo.setFDOCUMENTSTATUS(jobj1.getString("fdocumentstatus"));
						slInfo.setFCREATORname(jobj1.getString("fname"));
						list.add(slInfo);
					}
					responseDto.setErrcode("0");
					responseDto.setErrmsg("获取生产领料单信息成功");
					responseDto.setResponseObject(list);
				} else {
					responseDto.setErrcode("1");
					responseDto.setErrmsg(jobj.getString("errmsg"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询异常");
			responseDto.setErrcode("1");
			responseDto.setErrmsg("查询异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 仓库查询
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	@Override
	public String getwarehouse(String userName, String password) {
		ResponseDto responseDto = new ResponseDto();
		try {
			List<CkInfo> list = new ArrayList<CkInfo>();
			String organ = check(userName, password);
			String resultJson = authConfigComponent.getAuthService().getcangku(organ);
			JSONObject jobj = JSONObject.parseObject(resultJson);
			if ("0".equals(jobj.getString("errcode"))) {
				JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject jobj1 = (JSONObject) jsonArray.get(i);
					CkInfo info = new CkInfo();
					info.setFID(jobj1.getString("fstockid"));
					info.setFNAME(jobj1.getString("fname"));
					list.add(info);

				}
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取仓库信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg(jobj.getString("errmsg"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询异常");
			responseDto.setErrcode("1");
			responseDto.setErrmsg("查询异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 出库单详情查询
	 * 
	 * @param fid
	 * @return
	 */
	@Override
	public String checkDetailCk(String fid) {
		ResponseDto responseDto = new ResponseDto();
		List<SlInfo> list = new ArrayList<SlInfo>();
		try {
			String resultJson = authConfigComponent.getAuthService().getchuku(fid);
			JSONObject jobj = JSONObject.parseObject(resultJson);
			if ("0".equals(jobj.getString("errcode"))) {
				JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject jobj1 = (JSONObject) jsonArray.get(i);
					SlInfo slInfo = new SlInfo();
					slInfo.setFMATERIALIDNumber(jobj1.getString("fnumber"));
					slInfo.setFMATERIALIDname(jobj1.getString("fname"));
					slInfo.setFSTOCKAPPQTY(jobj1.getString("fappqty"));
					slInfo.setFSTOCKACTUALQTY(jobj1.getString("fstockactualqty"));
					slInfo.setXlh(jobj1.getString("fserialno"));
					slInfo.setFENTRYID(jobj1.getString("fentryid"));
					slInfo.setSTOCKID(jobj1.getString("fstockid"));
					list.add(slInfo);
				}
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取生产领料单详情信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg(jobj.getString("errmsg"));
			}
		} catch (Exception e) {
			responseDto.setErrcode("0");
			responseDto.setErrmsg("获取生产领料单详情信息异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 确认出库
	 * 
	 * @param userName
	 * @param password
	 * @param fid
	 * @param accnumber
	 * @param fentryid
	 * @param stocknumber
	 * @return
	 */
	@Override
	public String checkConfirm(String userName, String password, String fid, String accnumber, String fentryid,
			String stocknumber) {
		ResponseDto responseDto = new ResponseDto();
		String s = "";
		try {
			List<SysSystemForMap> systemlist = sysSystemMapper.findSystem(new SysSystemForMap());
			InvokeHelper.POST_K3CloudURL = systemlist.get(0).get("url").toString();
			String dbId = systemlist.get(0).get("dbid").toString();
			int lang = 2052;
			Map<String, String> map = InvokeHelper.Login(dbId, userName, password, lang);
			if ((map.get("errcode") == "0")) {
				String[] number = accnumber.split(",");
				String[] stock = stocknumber.split(",");
				String json1 = "";
				String json2 = "";
				String flot = "";
				String resultJson = authConfigComponent.getAuthService().getDetailMateril(fid, "");
				JSONObject jobj1 = JSONObject.parseObject(resultJson);
				JSONArray jsonArray = JSONArray.parseArray(jobj1.getString("responseObject").toString());
				if ("0".equals(jobj1.getString("errcode"))) {
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jobj = (JSONObject) jsonArray.get(i);
						if ("1".equals(jobj.getString("fisbatchmange"))) {
							flot = jobj.getString("flot");
						}
						if (i == 0) {
							json1 = "{\"NumberSearch\": \"True\",\"ValidateFlag\": \"True\",\"IsDeleteEntry\":\"True\",\"IsEntryBatchFill\": \"True\",\"NeedUpDateFields\": [],\"NeedReturnFields\": [],\"SubSystemId\": \"\",\"Model\": [{\"FID\":\""
									+ jobj.getString("fid") + "\",\"FBillType\":{\"FNumber\":\""
									+ jobj.getString("billnumber") + "\"},\"FDate\":\"" + DatetimeUtil.getDateyyyyMMdd()
									+ "\",\"FStockOrgId\":{\"FNumber\":\"" + jobj.getString("fnumber")
									+ "\"},\"FStockId0\":{\"FNumber\":\"\"},\"FPrdOrgId\":{\"FNumber\":\""
									+ jobj.getString("prdorgnumber") + "\"},\"FWorkShopId\":{\"FNumber\":\""
									+ jobj.getString("") + "\"},\"FOwnerTypeId0\":\"" + jobj.getString("fownertypeid")
									+ "\",\"FOwnerId0\":{\"FNumber\":\"" + jobj.getString("fownerid")
									+ "\"},\"FPickerId\":{},\"FSTOCKERID\":{\"FName\":\"\"},\"FDescription\":\"\",\"FCurrId\":{\"FNumber\":\"\"},\"FIsCrossTrade\":\"false\",\"FVmiBusiness\":\"false\",\"FScanBox\":\"\",\"F_PAEZ_Text\":\"\",\"FSourceType\":\"\",\"FEntity\":[";
							json2 = "{\"FEntryID\":\"" + jobj.getString("fentryid")
									+ "\",\"FMaterialId\":{\"FNumber\":\"" + jobj.getString("materilnumber")
									+ "\"},\"FUnitID\":{\"FNumber\":\"" + jobj.getString("fname1")
									+ "\"},\"FAppQty\":\"" + jobj.getString("fappqty")
									+ "\",\"FEntryVmiBusiness\":\"false\",\"FStockId\":{\"FNumber\":\""
									+ jobj.getString("stocknumber")
									+ "\"},\"FStockLocId\":{\"FSTOCKLOCID__FF100003\":{\"FNumber\":\"" + stock[i]
									+ "\"}},\"FLot\":{\"FNumber\":\"" + flot + "\"},\"FProduceDate\":\""
									+ jobj.getString("fproducedate") + "\",\"FMoBillNo\":\""
									+ jobj.getString("fmobillno") + "\",\"FMoEntryId\":\""
									+ jobj.getString("fmoentryid") + "\",\"FOwnerTypeId\":\""
									+ jobj.getString("fownertypeid") + "\",\"FStockUnitId\":{\"FNumber\":\""
									+ jobj.getString("fname2") + "\"},\"FStockActualQty\":\"" + number[i]
									+ "\",\"FMoId\":\"" + jobj.getString("fmoid") + "\",\"FMoEntrySeq\":\""
									+ jobj.getString("fmoentryseq") + "\",\"FBaseUnitId\":{\"FNumber\":\""
									+ jobj.getString("fname3") + "\"},\"FKeeperTypeId\":\""
									+ jobj.getString("fkeepertypeid") + "\",\"FKeeperId\":{\"FNumber\":\""
									+ jobj.getString("fkeeperid") + "\"},\"FOwnerId\":{\"FNumber\":\""
									+ jobj.getString("fownerid") + "\"},\"FExpiryDate\":\""
									+ jobj.getString("fexpirydate") + "\"}";

						} else {
							json2 += ",{\"FEntryID\":\"" + jobj.getString("fentryid")
									+ "\",\"FMaterialId\":{\"FNumber\":\"" + jobj.getString("materilnumber")
									+ "\"},\"FUnitID\":{\"FNumber\":\"" + jobj.getString("fname1")
									+ "\"},\"FAppQty\":\"" + jobj.getString("fappqty")
									+ "\",\"FEntryVmiBusiness\":\"false\",\"FStockId\":{\"FNumber\":\""
									+ jobj.getString("stocknumber")
									+ "\"},\"FStockLocId\":{\"FSTOCKLOCID__FF100003\":{\"FNumber\":\"" + stock[i]
									+ "\"}},\"FLot\":{\"FNumber\":\"" + flot + "\"},\"FProduceDate\":\""
									+ jobj.getString("fproducedate") + "\",\"FMoBillNo\":\""
									+ jobj.getString("fmobillno") + "\",\"FMoEntryId\":\""
									+ jobj.getString("fmoentryid") + "\",\"FOwnerTypeId\":\""
									+ jobj.getString("fownertypeid") + "\",\"FStockUnitId\":{\"FNumber\":\""
									+ jobj.getString("fname2") + "\"},\"FStockActualQty\":\"" + number[i]
									+ "\",\"FMoId\":\"" + jobj.getString("fmoid") + "\",\"FMoEntrySeq\":\""
									+ jobj.getString("fmoentryseq") + "\",\"FBaseUnitId\":{\"FNumber\":\""
									+ jobj.getString("fname3") + "\"},\"FKeeperTypeId\":\""
									+ jobj.getString("fkeepertypeid") + "\",\"FKeeperId\":{\"FNumber\":\""
									+ jobj.getString("fkeeperid") + "\"},\"FOwnerId\":{\"FNumber\":\""
									+ jobj.getString("fownerid") + "\"},\"FExpiryDate\":\""
									+ jobj.getString("fexpirydate") + "\"}";
						}
					}
				} else {
					responseDto.setErrcode("1");
					responseDto.setErrmsg("提交生产领料单详情信息失败");
				}
				s = json1 + json2 + "]}],\"BatchCount\": \"0\"}";
				System.out.println(s);
				String sFormId = "PRD_PickMtrl";
				String sResult = InvokeHelper.batchSave(sFormId, s);
				JSONObject j1 = JSONObject.parseObject(sResult);
				JSONObject j2 = JSONObject.parseObject(j1.get("Result").toString());
				JSONObject j3 = JSONObject.parseObject(j2.get("ResponseStatus").toString());
				if ("true".equals(j3.get("IsSuccess").toString())) {
					responseDto.setErrcode("0");
					responseDto.setErrmsg("提交生产领料单详情信息成功");
					responseDto.setResponseObject(j3.get("SuccessEntitys").toString());
				} else {
					responseDto.setErrcode("1");
					responseDto.setErrmsg(j3.get("Errors").toString());
				}
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("提交生产领料单详情信息异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 变更打印
	 * 
	 * @param organ
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @param f
	 * @return
	 */
	@Override
	public String changePrint(String userName, String password, String a, String b, String c, String d, String e,
			String f) {
		ResponseDto responseDto = new ResponseDto();
		try {
			String organ = check2(userName, password);
			Dprint dp = new Dprint();
			SysPrintForMap sysPrintForMap = new SysPrintForMap();
			sysPrintForMap.put("organ", organ);
			SysPrintForMap print = sysPrintMapper.findDetail(sysPrintForMap).get(0);
			String s = "品名:$" + a + "$料号:$" + b + "$数量:$" + c + "$规格:$" + d + "$批号:$" + e + "$S/N:$" + f;
			String ewm = a + ":!:" + b + ":!:" + c + ":!:" + d + ":!:" + e + ":!:" + f;
			String result = dp.getRemoteInfo(print, organ, s, ewm);
			if (result == null) {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("打印提交失败");
			} else {
				responseDto.setErrcode("0");
				responseDto.setErrmsg(result);
			}
		} catch (Exception e2) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("打印异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	@Override
	public String checkGD(String userName, String password) {
		ResponseDto responseDto = new ResponseDto();
		try {
			SysUserFormMap userFormMap = new SysUserFormMap();
			userFormMap.put("userName", userName);
			userFormMap.put("menuName", "固定资产管理");
			List<SysUserFormMap> user = sysUserMapper.perm(userFormMap);
			int count = Integer.parseInt(user.get(0).get("count").toString());
			if (count == 0) {
				responseDto.setErrcode("2");
				responseDto.setErrmsg("该用户无权限查看");
			} else {
				List<GdInfo> list = new ArrayList<GdInfo>();
				String organ = check(userName, password);
				String resultJson = authConfigComponent.getAuthService().getGDZC(organ);
				JSONObject jobj = JSONObject.parseObject(resultJson);
				if ("0".equals(jobj.getString("errcode"))) {
					JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jobj1 = (JSONObject) jsonArray.get(i);
						GdInfo gdInfo = new GdInfo();
						gdInfo.setFNUMBER(jobj1.getString("fnumber"));
						gdInfo.setFQUANTITY(jobj1.getString("fquantity"));
						gdInfo.setUNITNAME(jobj1.getString("unitname"));
						gdInfo.setFCREATEDATE(jobj1.getString("fcreatedate"));
						gdInfo.setTYPENAME(jobj1.getString("typename"));
						gdInfo.setFNAME(jobj1.getString("fname"));
						gdInfo.setFASSETNO(jobj1.getString("fassetno"));
						gdInfo.setFPKID(jobj1.getString("fpkid"));
						gdInfo.setPLACENAME(jobj1.getString("placename"));
						if (StringUtil.isEmpty(jobj1.getString("changename"))) {
							gdInfo.setUSERNAME(jobj1.getString("username"));
						} else {
							gdInfo.setUSERNAME(jobj1.getString("changename"));
						}
						gdInfo.setDEPARTNAME(jobj1.getString("departname"));
						list.add(gdInfo);
					}
					responseDto.setErrcode("0");
					responseDto.setErrmsg("获取固定资产信息成功");
					responseDto.setResponseObject(list);
				} else {
					responseDto.setErrcode("1");
					responseDto.setErrmsg(jobj.getString("errmsg"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询异常");
			responseDto.setErrcode("1");
			responseDto.setErrmsg("查询异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 固定资产打印
	 * 
	 * @param userName
	 * @param password
	 * @param id
	 * @return
	 */
	@Override
	public String print2(String userName, String password, String id) {
		ResponseDto responseDto = new ResponseDto();
		try {
			String[] ids = id.split(",");
			String organ = check2(userName, password);
			String organ1 = check(userName, password);
			Dprint dp = new Dprint();
			SysPrintForMap sysPrintForMap = new SysPrintForMap();
			sysPrintForMap.put("organ", organ1);
			sysPrintForMap.put("type", "1");
			SysPrintForMap print = sysPrintMapper.findDetail(sysPrintForMap).get(0);
			for (int i = 0; i < ids.length; i++) {
				String resultJson = authConfigComponent.getAuthService().getprint2Msg(ids[i]);
				JSONObject jobj = JSONObject.parseObject(resultJson);
				if ("0".equals(jobj.getString("errcode"))) {
					JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
					for (int j = 0; j < jsonArray.size(); j++) {
						JSONObject jobj1 = (JSONObject) jsonArray.get(j);
						String s = "资产编码:$" + jobj1.getString("fassetno") + "$资产名称:$" + jobj1.getString("fname")
								+ "$资产位置:$" + jobj1.getString("placename") + "$所属部门:$"
								+ jobj1.get("departname").toString() + "$保管人:$" + jobj1.getString("username");
						String ewm = jobj1.getString("fnumber") + ":!:" + jobj1.getString("fassetno") + ":!:"
								+ jobj1.getString("fname") + ":!:" + jobj1.getString("placename") + ":!:"
								+ jobj1.get("departname").toString() + ":!:" + jobj1.getString("username");
						String result = dp.getRemoteInfo(print, organ, s, ewm);
						if (result == null) {
							responseDto.setErrcode("1");
							responseDto.setErrmsg("打印提交失败");
						} else {
							responseDto.setErrcode("0");
							responseDto.setErrmsg(result);
						}
					}
				} else {
					responseDto.setErrcode("1");
					responseDto.setErrmsg(jobj.getString("errmsg"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("打印异常");
			responseDto.setErrcode("1");
			responseDto.setErrmsg("打印异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 获取组织
	 * 
	 * @param name
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public String getorgan(String name, String password) throws Exception {
		String organ = "";
		SysUserFormMap sysUserFormMap = new SysUserFormMap();
		sysUserFormMap.put("userName", name);
		List<SysUserFormMap> mps = sysUserMapper.findUserPage(sysUserFormMap);
		if (mps != null && mps.size() > 0) {
			if (StringUtil.isEmpty(mps.get(0).get("organ").toString())) {
				List<SysSystemForMap> list = sysSystemMapper.findSystem(new SysSystemForMap());
				InvokeHelper.POST_K3CloudURL = list.get(0).get("url").toString();
				String dbId = list.get(0).get("dbid").toString();
				int lang = 2052;
				Map<String, String> map = InvokeHelper.Login(dbId, name, password, lang);
				if ((map.get("errcode") == "0")) {
					organ = map.get("organ").toString();
				}
			} else {
				organ = mps.get(0).get("organ").toString();
			}
		}
		return organ;
	}

	/**
	 * 获取订单信息
	 * 
	 * @param name
	 * @param password
	 * @param flot
	 * @param fmobillno
	 * @return
	 */
	@Override
	public String getBomli(String userName, String password, String flot, String fmobillno) {
		ResponseDto responseDto = new ResponseDto();
		List<BomInfo> list = new ArrayList<BomInfo>();
		try {
			SysUserFormMap userFormMap = new SysUserFormMap();
			userFormMap.put("userName", userName);
			userFormMap.put("menuName", "生产领料单管理");
			List<SysUserFormMap> user = sysUserMapper.perm(userFormMap);
			int count = Integer.parseInt(user.get(0).get("count").toString());
			if (count == 0) {
				responseDto.setErrcode("2");
				responseDto.setErrmsg("该用户无权限查看");
			} else {
				String organ = check(userName, password);
				String resultJson = authConfigComponent.getAuthService().getBom(flot, fmobillno, organ);
				JSONObject jobj = JSONObject.parseObject(resultJson);
				if ("0".equals(jobj.getString("errcode"))) {
					JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
					for (int j = 0; j < jsonArray.size(); j++) {
						JSONObject jobj1 = (JSONObject) jsonArray.get(j);
						BomInfo info = new BomInfo();
						info.setFBILLNO(jobj1.getString("fbillno"));
						info.setFMOBILLNO(jobj1.getString("fmobillno"));
						info.setFNAME(jobj1.getString("fname"));
						info.setFNEEDQTY(jobj1.getString("fneedqty"));
						info.setFNUMBER(jobj1.getString("fnumber"));
						info.setFENTRYID(jobj1.getString("fentryid"));
						info.setSTOCKQTY(jobj1.getString("stockqty"));
						info.setSTOCKID(jobj1.getString("stockid"));
						// info.setFSENO(jobj1.getString(""));
						list.add(info);
					}
					responseDto.setErrcode("0");
					responseDto.setErrmsg("获取生产用料清单信息成功");
					responseDto.setResponseObject(list);
				} else {
					responseDto.setErrcode("1");
					responseDto.setErrmsg(jobj.getString("errmsg"));
				}
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("查询异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 建生产领料暂存单
	 * 
	 * @param name
	 * @param password
	 * @param fentryid
	 * @param accnumber
	 * @param stocknumber
	 * @return
	 */
	@Override
	public String CreateCL(String userName, String password, String fentryid, String accnumber, String stocknumber) {
		ResponseDto responseDto = new ResponseDto();
		String s = "";
		String[] number = accnumber.split(",");
		String[] stock = stocknumber.split(",");
		try {
			String organ = check(userName, password);
			String json1 = "";
			String json2 = "";
			String flot = "";
			String resultJson = authConfigComponent.getAuthService().getorg(organ);
			JSONObject j = JSONObject.parseObject(resultJson);
			JSONArray jrray = JSONArray.parseArray(j.getString("responseObject").toString());
			JSONObject job = (JSONObject) jrray.get(0);
			String resultJson1 = authConfigComponent.getAuthService().getDetailBom(fentryid, organ);
			JSONObject jobj1 = JSONObject.parseObject(resultJson1);
			JSONArray jsonArray = JSONArray.parseArray(jobj1.getString("responseObject").toString());
			if ("0".equals(jobj1.getString("errcode"))) {
				for (int i = 0; i < 1; i++) {
					JSONObject jobj = (JSONObject) jsonArray.get(i);
					if ("1".equals(jobj.getString("fisbatchmange"))) {
						flot = jobj.getString("flot");
					}
					if (i == 0) {
						json1 = "{\"Creator\": \"\",\"NeedUpDateFields\": [],\"NeedReturnFields\": [],\"IsDeleteEntry\": \"True\",\"SubSystemId\": \"\",\"IsVerifyBaseDataField\": \"false\",\"IsEntryBatchFill\": \"True\",\"Model\": {\"FID\": \"0\",\"FBillType\": {\"FNumber\": \"SCLLD01_SYS\"},\"FDate\": \""
								+ DatetimeUtil.getDateyyyyMMdd() + "\",\"FStockOrgId\": {\"FNumber\": \""
								+ job.getString("fnumber")
								+ "\"},\"FStockId0\": {\"FNumber\": \"\"},\"FPrdOrgId\": {\"FNumber\": \""
								+ job.getString("fnumber") + "\"},\"FWorkShopId\": {\"FNumber\": \""
								+ jobj.getString("departnumber")
								+ "\"},\"FOwnerTypeId0\": \"BD_OwnerOrg\",\"FOwnerId0\": { \"FNumber\": \""
								+ job.getString("fnumber")
								+ "\"},\"FPickerId\": {},\"FSTOCKERID\": {\"FName\": \"\"},\"FDescription\": \"\",\"FCurrId\": {\"FNumber\": \"\"},\"FIsCrossTrade\": \"false\",\"FVmiBusiness\": \"false\",\"FScanBox\": \"\",\"F_PAEZ_Text\": \"\",\"FSourceType\": \"\",\"FEntity\": [";
						json2 = "{\"FEntryID\":\"0\",\"FMaterialId\":{\"FNumber\":\"" + jobj.getString("materilnumber")
								+ "\"},\"FUnitID\":{\"FNumber\":\"" + jobj.getString("fname1") + "\"},\"FAppQty\":\""
								+ jobj.getString("fappqty") + "\",\"FActualQty\":\"" + number[i]
								+ "\",\"FEntryVmiBusiness\":\"false\",\"FStockId\":{\"FNumber\":\""
								+ jobj.getString("stocknumber")
								+ "\"},\"FStockLocId\":{\"FSTOCKLOCID__FF100003\":{\"FNumber\":\"" + stock[i]
								+ "\"}},\"FLot\":{\"FNumber\":\"" + flot + "\"},\"FMoBillNo\":\""
								+ jobj.getString("fmobillno") + "\",\"FMoEntryId\":\"" + jobj.getString("fmoentryid")
								+ "\",\"FOwnerTypeId\":\"BD_OwnerOrg\",\"FStockUnitId\":{\"FNumber\":\""
								+ jobj.getString("fname3") + "\"},\"FMoId\":\"" + jobj.getString("fmoid")
								+ "\",\"FMoEntrySeq\":\"" + jobj.getString("fmoentryseq")
								+ "\",\"FBaseUnitId\":{\"FNumber\":\"" + jobj.getString("fname3")
								+ "\"},\"FKeeperTypeId\":\"BD_KeeperOrg\",\"FKeeperId\":{\"FNumber\":\""
								+ job.getString("fnumber") + "\"},\"FOwnerId\":{\"FNumber\":\""
								+ job.getString("fnumber") + "\"},\"FExpiryDate\":\"" + jobj.getString("fexpirydate")
								+ "\"}";
					} else {
						json2 += ",{\"FEntryID\":\"0\",\"FMaterialId\":{\"FNumber\":\""
								+ jobj.getString("materilnumber") + "\"},\"FUnitID\":{\"FNumber\":\""
								+ jobj.getString("fname1") + "\"},\"FAppQty\":\"" + jobj.getString("fappqty")
								+ "\",\"FActualQty\":\"" + number[i]
								+ "\",\"FEntryVmiBusiness\":\"false\",\"FStockId\":{\"FNumber\":\""
								+ jobj.getString("stocknumber")
								+ "\"},\"FStockLocId\":{\"FSTOCKLOCID__FF100003\":{\"FNumber\":\"" + stock[i]
								+ "\"}},\"FLot\":{\"FNumber\":\"" + flot + "\"},\"FMoBillNo\":\""
								+ jobj.getString("fmobillno") + "\",\"FMoEntryId\":\"" + jobj.getString("fmoentryid")
								+ "\",\"FOwnerTypeId\":\"BD_OwnerOrg\",\"FStockUnitId\":{\"FNumber\":\""
								+ jobj.getString("fname3") + "\"},\"FMoId\":\"" + jobj.getString("fmoid")
								+ "\",\"FMoEntrySeq\":\"" + jobj.getString("fmoentryseq")
								+ "\",\"FBaseUnitId\":{\"FNumber\":\"" + jobj.getString("fname3")
								+ "\"},\"FKeeperTypeId\":\"BD_KeeperOrg\",\"FKeeperId\":{\"FNumber\":\""
								+ job.getString("fnumber") + "\"},\"FOwnerId\":{\"FNumber\":\""
								+ job.getString("fnumber") + "\"},\"FExpiryDate\":\"" + jobj.getString("fexpirydate")
								+ "\"}";
					}
				}
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("提交生产领料单详情信息失败");
			}
			s = json1 + json2 + "]}}";
			System.out.println(s);
			String sFormId = "PRD_PickMtrl";
			String sResult = InvokeHelper.Draft(sFormId, s);
			JSONObject j1 = JSONObject.parseObject(sResult);
			JSONObject j2 = JSONObject.parseObject(j1.get("Result").toString());
			JSONObject j3 = JSONObject.parseObject(j2.get("ResponseStatus").toString());
			if ("true".equals(j3.get("IsSuccess").toString())) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("提交生产领料单详情信息成功");
				responseDto.setResponseObject(j3.get("SuccessEntitys").toString());
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg(j3.get("Errors").toString());
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("提交生产领料单详情信息异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 建生产领料保存单
	 * 
	 * @param name
	 * @param password
	 * @param fentryid
	 * @param accnumber
	 * @param stocknumber
	 * @return
	 */
	@Override
	public String SaveCL(String userName, String password, String fentryid, String accnumber, String stocknumber) {
		ResponseDto responseDto = new ResponseDto();
		String result = CreateCL(userName, password, fentryid, accnumber, stocknumber);
		JSONObject jobj1 = JSONObject.parseObject(result);
		JSONArray jsonArray = JSONArray.parseArray(jobj1.getString("responseObject").toString());
		JSONObject jobj = (JSONObject) jsonArray.get(0);
		String result2 = checkConfirm(userName, password, jobj.getString("Id"), accnumber, "", stocknumber);
		JSONObject jobj2 = JSONObject.parseObject(result2);
		if ("1".equals(jobj1.getString("errcode")) || "1".equals(jobj2.getString("errcode"))) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("提交生产领料单信息失败");
		} else {
			responseDto.setErrcode("0");
			responseDto.setErrmsg("提交生产领料单详情信息成功");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 获取列管资产信息
	 * 
	 * @param userName
	 * @param number
	 * @return
	 */
	@Override
	public String getLG(String userName, String number) {
		ResponseDto responseDto = new ResponseDto();
		try {
			SysUserFormMap userFormMap = new SysUserFormMap();
			userFormMap.put("userName", userName);
			userFormMap.put("menuName", "列管资产已审核管理");
			List<SysUserFormMap> user = sysUserMapper.perm(userFormMap);
			int count = Integer.parseInt(user.get(0).get("count").toString());
			if (count == 0) {
				responseDto.setErrcode("2");
				responseDto.setErrmsg("该用户无权限查看");
			} else {
				ApiStAssetsForMap apiStAssetsForMap = new ApiStAssetsForMap();
				if (!StringUtil.isEmpty(number)) {
					apiStAssetsForMap.put("searchValue", number);
				}
				apiStAssetsForMap.put("status", "1");
				List<ApiStAssetsForMap> list = apiStAssetsMapper.findAllPage(apiStAssetsForMap);
				if (list.size() != 0) {
					responseDto.setErrcode("0");
					responseDto.setErrmsg("获取成功");
					responseDto.setResponseObject(list);
				} else {
					responseDto.setErrcode("1");
					responseDto.setErrmsg("暂无列管资产信息");
				}
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("查询异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 获取仓位
	 * 
	 * @param stockid
	 * @return
	 */
	@Override
	public String getCW(String stockid) {
		ResponseDto responseDto = new ResponseDto();
		try {
			List<CwInfo> list = new ArrayList<CwInfo>();
			String resultJson = authConfigComponent.getAuthService().getcw(stockid);
			JSONObject jobj1 = JSONObject.parseObject(resultJson);
			if ("1".equals(jobj1.getString("errcode"))) {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("获取仓位信息失败");
			} else {
				JSONArray jsonArray = JSONArray.parseArray(jobj1.getString("responseObject").toString());
				for (int j = 0; j < jsonArray.size(); j++) {
					JSONObject jobj = (JSONObject) jsonArray.get(j);
					CwInfo info = new CwInfo();
					info.setBINNO(jobj.getString("binno"));
					info.setBINNAME(jobj.getString("binname"));
					list.add(info);
				}
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取仓位信息成功");
				responseDto.setResponseObject(list);
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("获取仓位信息异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 仓储盘点单查询
	 * 
	 * @param userName
	 * @param password
	 * @param date
	 * @return
	 */
	@Override
	public String checkPD(String userName, String password, String date) {
		ResponseDto responseDto = new ResponseDto();
		try {
			SysUserFormMap userFormMap = new SysUserFormMap();
			userFormMap.put("userName", userName);
			userFormMap.put("menuName", "即时库存查询");
			List<SysUserFormMap> user = sysUserMapper.perm(userFormMap);
			int count = Integer.parseInt(user.get(0).get("count").toString());
			if (count == 0) {
				responseDto.setErrcode("2");
				responseDto.setErrmsg("该用户无权限查看");
			} else {
				List<PdInfo> list = new ArrayList<PdInfo>();
				String organ = check(userName, password);
				String resultJson = authConfigComponent.getAuthService().getPd(organ, date);
				JSONObject jobj = JSONObject.parseObject(resultJson);
				if ("0".equals(jobj.getString("errcode"))) {
					JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jobj1 = (JSONObject) jsonArray.get(i);
						PdInfo info = new PdInfo();
						info.setFID(jobj1.getString("fid"));
						info.setFBILLNO(jobj1.getString("fbillno"));
						info.setFDATE(jobj1.getString("fdate"));
						info.setFDOCUMENTSTATUS(jobj1.getString("fdocumentstatus"));
						list.add(info);
					}
					responseDto.setErrcode("0");
					responseDto.setErrmsg("获取仓储盘点单信息成功");
					responseDto.setResponseObject(list);
				} else {
					responseDto.setErrcode("1");
					responseDto.setErrmsg(jobj.getString("errmsg"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询异常");
			responseDto.setErrcode("1");
			responseDto.setErrmsg("查询仓储盘点异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 仓储盘点单详情
	 * 
	 * @param fid
	 * @return
	 */
	@Override
	public String checkDetailPd(String fid, String stock, String userName) {
		ResponseDto responseDto = new ResponseDto();
		List<PdInfo> list = new ArrayList<PdInfo>();
		try {
			SysUserFormMap userFormMap = new SysUserFormMap();
			userFormMap.put("userName", userName);
			userFormMap.put("menuName", "仓储盘点明盘");
			List<SysUserFormMap> user = sysUserMapper.perm(userFormMap);
			String count = user.get(0).get("count").toString();
			String resultJson = authConfigComponent.getAuthService().getdePd(fid, stock);
			JSONObject jobj = JSONObject.parseObject(resultJson);
			if ("0".equals(jobj.getString("errcode"))) {
				JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject jobj1 = (JSONObject) jsonArray.get(i);
					PdInfo info = new PdInfo();
					info.setFENTRYID(jobj1.getString("fentryid"));
					info.setFNUMBER(jobj1.getString("materilnumber"));
					info.setFNAME(jobj1.getString("fname"));
					info.setFSERIALNO(jobj1.getString("fserialno"));
					// count 1 明盘 0 暗盘
					info.setPDTYPE(count);
					info.setFBaseAcctQty(jobj1.getString("fbaseacctqty"));
					info.setSTOCKNUMBER(jobj1.getString("stocknumber"));
					if ("0E-10".equals(jobj1.getString("fbasecountqty"))) {
						info.setFBaseCountQty("0");
					} else {
						info.setFBaseCountQty(jobj1.getString("fbasecountqty"));
					}
					list.add(info);
				}
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取盘点单详情信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg(jobj.getString("errmsg"));
			}
		} catch (Exception e) {
			responseDto.setErrcode("0");
			responseDto.setErrmsg("获取盘点单详情信息异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	@Override
	public String checkCkdetailPd(String fid) {
		ResponseDto responseDto = new ResponseDto();
		List<PdCkInfo> list = new ArrayList<PdCkInfo>();
		try {
			String resultJson = authConfigComponent.getAuthService().getdePdCk(fid);
			JSONObject jobj = JSONObject.parseObject(resultJson);
			if ("0".equals(jobj.getString("errcode"))) {
				JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject jobj1 = (JSONObject) jsonArray.get(i);
					PdCkInfo info = new PdCkInfo();
					info.setFNUMBER(jobj1.getString("fnumber"));
					info.setFNAME(jobj1.getString("fname"));
					list.add(info);
				}
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取盘点单详情信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg(jobj.getString("errmsg"));
			}
		} catch (Exception e) {
			responseDto.setErrcode("0");
			responseDto.setErrmsg("获取盘点单详情信息异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 仓储管理确认盘点
	 * 
	 * @param fid
	 * @param accnumber
	 * @param fentryid
	 * @return
	 */
	@Override
	public String checkPdfirm(String fid, String accnumber, String fentryid) {
		ResponseDto responseDto = new ResponseDto();
		try {
			String resultJson = authConfigComponent.getAuthService().checkPd(fid, accnumber, fentryid);
			JSONObject jobj = JSONObject.parseObject(resultJson);
			if ("0".equals(jobj.getString("errcode"))) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("提交盘点结果信息成功");
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("提交盘点结果信息失败");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("提交盘点结果信息异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 固定资产盘点单查询
	 * 
	 * @param userName
	 * @param password
	 * @param date
	 * @return
	 */
	@Override
	public String checkGdPD(String userName, String password, String date) {
		ResponseDto responseDto = new ResponseDto();
		try {
			SysUserFormMap userFormMap = new SysUserFormMap();
			userFormMap.put("userName", userName);
			userFormMap.put("menuName", "固定资产管理");
			List<SysUserFormMap> user = sysUserMapper.perm(userFormMap);
			int count = Integer.parseInt(user.get(0).get("count").toString());
			if (count == 0) {
				responseDto.setErrcode("2");
				responseDto.setErrmsg("该用户无权限查看");
			} else {
				List<GdPdInfo> list = new ArrayList<GdPdInfo>();
				String organ = check(userName, password);
				String resultJson = authConfigComponent.getAuthService().getGdPd(organ, date);
				JSONObject jobj = JSONObject.parseObject(resultJson);
				if ("0".equals(jobj.getString("errcode"))) {
					JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jobj1 = (JSONObject) jsonArray.get(i);
						GdPdInfo info = new GdPdInfo();
						info.setFID(jobj1.getString("fid"));
						info.setFBILLNO(jobj1.getString("fbillno"));
						info.setFDATE(jobj1.getString("fdate"));
						info.setFDOCUMENTSTATUS(jobj1.getString("fdocumentstatus"));
						list.add(info);
					}
					responseDto.setErrcode("0");
					responseDto.setErrmsg("获取生产领料单信息成功");
					responseDto.setResponseObject(list);
				} else {
					responseDto.setErrcode("1");
					responseDto.setErrmsg(jobj.getString("errmsg"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询异常");
			responseDto.setErrcode("1");
			responseDto.setErrmsg("查询异常");
		}
		return JsonUtils.bean2json(responseDto);

	}

	/**
	 * 固定资产盘点单详情
	 * 
	 * @param fid
	 * @return
	 */
	@Override
	public String checkDetailGdPd(String fid, String userName) {
		ResponseDto responseDto = new ResponseDto();
		List<GdPdInfo> list = new ArrayList<GdPdInfo>();
		try {
			SysUserFormMap userFormMap = new SysUserFormMap();
			userFormMap.put("userName", userName);
			userFormMap.put("menuName", "固定资产盘点明盘");
			List<SysUserFormMap> user = sysUserMapper.perm(userFormMap);
			String count = user.get(0).get("count").toString();
			String resultJson = authConfigComponent.getAuthService().getdeGdPd(fid);
			JSONObject jobj = JSONObject.parseObject(resultJson);
			if ("0".equals(jobj.getString("errcode"))) {
				JSONArray jsonArray = JSONArray.parseArray(jobj.getString("responseObject").toString());
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject jobj1 = (JSONObject) jsonArray.get(i);
					GdPdInfo info = new GdPdInfo();
					info.setFENTRYID(jobj1.getString("fentryid"));
					info.setFNUMBER(jobj1.getString("fcardnumber"));
					info.setFNAME(jobj1.getString("fname"));
					// count 1 明盘 0 暗盘
					info.setPDTYPE(count);
					info.setFBaseAcctQty(jobj1.getString("fbookqty"));
					info.setFASSETSNUMBER(jobj1.getString("fassetnumber"));
					if ("0E-10".equals(jobj1.getString("finitqty"))) {
						info.setFBaseCountQty("0");
					} else {
						info.setFBaseCountQty(jobj1.getString("finitqty"));
					}
					list.add(info);
				}
				responseDto.setErrcode("0");
				responseDto.setErrmsg("获取盘点单详情信息成功");
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg(jobj.getString("errmsg"));
			}
		} catch (Exception e) {
			responseDto.setErrcode("0");
			responseDto.setErrmsg("获取盘点单详情信息异常");
		}
		return JsonUtils.bean2json(responseDto);

	}

	/**
	 * 固定资产确认盘点
	 * 
	 * @param fid
	 * @param accnumber
	 * @param fentryid
	 * @return
	 */
	@Override
	public String checkGdPdfirm(String fid, String accnumber, String fentryid) {
		ResponseDto responseDto = new ResponseDto();
		try {
			String resultJson = authConfigComponent.getAuthService().checkGdPd(fid, accnumber, fentryid);
			JSONObject jobj = JSONObject.parseObject(resultJson);
			if ("0".equals(jobj.getString("errcode"))) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg("提交盘点结果信息成功");
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("提交盘点结果信息失败");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("提交盘点结果信息异常");
		}
		return JsonUtils.bean2json(responseDto);

	}

	/**
	 * 列管资产盘点单查询
	 * 
	 * @param userName
	 * @param password
	 * @param date
	 * @return
	 */
	@Override
	public String checkLgCK(String userName, String password, String date) {
		ResponseDto responseDto = new ResponseDto();
		try {
			SysUserFormMap userFormMap = new SysUserFormMap();
			userFormMap.put("userName", userName);
			userFormMap.put("menuName", "列管资产盘点管理");
			List<SysUserFormMap> user = sysUserMapper.perm(userFormMap);
			int count = Integer.parseInt(user.get(0).get("count").toString());
			if (count == 0) {
				responseDto.setErrcode("2");
				responseDto.setErrmsg("该用户无权限查看");
			} else {
				String organ = check2(userName, password);
				LgForMap lgForMap = new LgForMap();
				lgForMap.put("organ", organ);
				List<LgForMap> list = lgMapper.findAllPage(lgForMap);
				if (list.size() > 0) {
					responseDto.setErrcode("0");
					responseDto.setErrmsg("查询盘点单信息成功");
					responseDto.setResponseObject(list);
				} else {
					responseDto.setErrcode("1");
					responseDto.setErrmsg("暂无盘点单信息");
				}
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("查询盘点单信息异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 列管资产盘点详情
	 * 
	 * @param fid
	 * @return
	 */
	@Override
	public String checkDetailLgPd(String fid, String userName) {
		ResponseDto responseDto = new ResponseDto();
		try {
			SysUserFormMap userFormMap = new SysUserFormMap();
			userFormMap.put("userName", userName);
			userFormMap.put("menuName", "列管资产盘点明盘");
			List<SysUserFormMap> user = sysUserMapper.perm(userFormMap);
			String count = user.get(0).get("count").toString();
			LgForMap lgForMap = new LgForMap();
			lgForMap.put("fid", fid);
			List<LgForMap> list = new ArrayList<LgForMap>();
			list = lgMapper.findAllMsg(lgForMap);
			if (list.size() > 0) {
				responseDto.setErrcode("0");
				responseDto.setErrmsg(count);
				responseDto.setResponseObject(list);
			} else {
				responseDto.setErrcode("1");
				responseDto.setErrmsg("查询盘点单信息失败");
			}
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("查询盘点详情信息异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	/**
	 * 列管盘点确认
	 * 
	 * @param type
	 * @param userName
	 * @param accnumber
	 * @param fentryid
	 * @return
	 */
	@Override
	public String checkLgPdfirm(String type, String userName, String accnumber, String fentryid) {
		ResponseDto responseDto = new ResponseDto();
		try {
			String[] number = accnumber.split(",");
			String[] id = fentryid.split(",");
			if ("cp".equals(type)) {
				for (int i = 0; i < number.length; i++) {
					LgDForMap jgDForMap = new LgDForMap();
					jgDForMap.put("id", id[i]);
					List<LgDForMap> list = lgMapper.finMsg(jgDForMap);
					int zc = Integer.parseInt(list.get(0).get("zcNum").toString());
					int ch = (int) Double.parseDouble(number[i]) - zc;
					jgDForMap.put("cpQty", ch);
					jgDForMap.put("cpName", userName);
					jgDForMap.put("cpNum", (int) Double.parseDouble(number[i]));
					lgMapper.edit(jgDForMap);
				}
			} else {
				for (int i = 0; i < number.length; i++) {
					LgDForMap jgDForMap = new LgDForMap();
					jgDForMap.put("id", id[i]);
					List<LgDForMap> list = lgMapper.finMsg(jgDForMap);
					int zc = Integer.parseInt(list.get(0).get("zcNum").toString());
					int ch = (int) Double.parseDouble(number[i]) - zc;
					jgDForMap.put("fpQty", ch);
					jgDForMap.put("fpName", userName);
					jgDForMap.put("fpNum", (int) Double.parseDouble(number[i]));
					lgMapper.edit(jgDForMap);
				}
			}
			responseDto.setErrcode("1");
			responseDto.setErrmsg("提交盘点结果信息成功");
		} catch (Exception e) {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("提交盘点结果信息异常");
		}
		return JsonUtils.bean2json(responseDto);
	}

	public String check(String userName, String password) throws Exception {
		String organ = "";
		SysUserFormMap sysUserFormMap = new SysUserFormMap();
		sysUserFormMap.put("userName", userName);
		List<SysUserFormMap> mps = sysUserMapper.findUserPage(sysUserFormMap);
		if (mps != null && mps.size() > 0) {
			SysUserFormMap tempUserFormMap = mps.get(0);
			if ("1".equals(tempUserFormMap.get("userType").toString())) {
				List<SysSystemForMap> systemlist = sysSystemMapper.findSystem(new SysSystemForMap());
				InvokeHelper.POST_K3CloudURL = systemlist.get(0).get("url").toString();
				String dbId = systemlist.get(0).get("dbid").toString();
				int lang = 2052;
				Map<String, String> map = InvokeHelper.Login(dbId, userName, password, lang);
				if ((map.get("errcode") == "0")) {
					organ = map.get("organid").toString();
				}
			} else {
				organ = tempUserFormMap.get("organId").toString();
			}
		}
		return organ;
	}

	public String check2(String userName, String password) throws Exception {
		String organ = "";
		SysUserFormMap sysUserFormMap = new SysUserFormMap();
		sysUserFormMap.put("userName", userName);
		List<SysUserFormMap> mps = sysUserMapper.findUserPage(sysUserFormMap);
		if (mps != null && mps.size() > 0) {
			SysUserFormMap tempUserFormMap = mps.get(0);
			if ("1".equals(tempUserFormMap.get("userType").toString())) {
				List<SysSystemForMap> systemlist = sysSystemMapper.findSystem(new SysSystemForMap());
				InvokeHelper.POST_K3CloudURL = systemlist.get(0).get("url").toString();
				String dbId = systemlist.get(0).get("dbid").toString();
				int lang = 2052;
				Map<String, String> map = InvokeHelper.Login(dbId, userName, password, lang);
				if ((map.get("errcode") == "0")) {
					organ = map.get("organ").toString();
				}
			} else {
				organ = tempUserFormMap.get("organ").toString();
			}
		}
		return organ;
	}
}