package com.ysttench.application.auth.core.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * 接口
 * 
 * @author Howard
 *
 */
@WebService(targetNamespace = "http://service.core.auth.application.ysttench.com/")
public interface AuthService {
	/**
	 * 打印接口
	 * 
	 * @param userName
	 * @param password
	 * @param rk
	 * @param dg
	 * @return
	 */
	@WebMethod
	public String print(String userName, String password, String rk, String dg);

	/**
	 * 入库单显示
	 * 
	 * @param userName
	 * @param password
	 * @param date
	 * @return
	 */
	@WebMethod
	public String showPrint(String userName, String password, String date);

	/**
	 * 库存查询
	 * 
	 * @param userName
	 * @param password
	 * @param ck
	 * @return
	 */
	@WebMethod
	public String checkKC(String userName, String password, String ck);

	/**
	 * 登陆验证接口
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public String login(String userName, String password);

	/**
	 * 出库单查询
	 * 
	 * @param userName
	 * @param password
	 * @param date
	 * @return
	 */
	@WebMethod
	public String checkCK(String userName, String password, String date, String searchValue);

	/**
	 * 列管资产盘点单查询
	 * 
	 * @param userName
	 * @param password
	 * @param date
	 * @return
	 */
	@WebMethod
	public String checkLgCK(String userName, String password, String date);

	/**
	 * 仓储盘点单查询
	 * 
	 * @param userName
	 * @param password
	 * @param date
	 * @return
	 */
	@WebMethod
	public String checkPD(String userName, String password, String date);

	/**
	 * 固定资产盘点单查询
	 * 
	 * @param userName
	 * @param password
	 * @param date
	 * @return
	 */
	@WebMethod
	public String checkGdPD(String userName, String password, String date);

	/**
	 * 出库单详情查询
	 * 
	 * @param fid
	 * @return
	 */
	@WebMethod
	public String checkDetailCk(String fid);

	/**
	 * 仓储盘点单详情
	 * 
	 * @param fid
	 * @return
	 */
	@WebMethod
	public String checkDetailPd(String fid, String stock, String userName);

	/**
	 * 获取仓储盘点单仓库信息
	 * 
	 * @param fid
	 * @return
	 */
	@WebMethod
	public String checkCkdetailPd(String fid);

	/**
	 * 列管资产盘点详情
	 * 
	 * @param fid
	 * @return
	 */
	@WebMethod
	public String checkDetailLgPd(String fid, String userName);

	/**
	 * 固定资产盘点单详情
	 * 
	 * @param fid
	 * @param userName
	 * @return
	 */
	@WebMethod
	public String checkDetailGdPd(String fid, String userName);

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
	@WebMethod
	public String checkConfirm(String userName, String password, String fid, String accnumber, String fentryid,
			String stocknumber);

	/**
	 * 仓储管理确认盘点
	 * 
	 * @param fid
	 * @param accnumber
	 * @param fentryid
	 * @return
	 */
	@WebMethod
	public String checkPdfirm(String fid, String accnumber, String fentryid);

	/**
	 * 列管盘点确认
	 * 
	 * @param type
	 * @param userName
	 * @param accnumber
	 * @param fentryid
	 * @return
	 */
	@WebMethod
	public String checkLgPdfirm(String type, String userName, String accnumber, String fentryid);

	/**
	 * 固定资产确认盘点
	 * 
	 * @param fid
	 * @param accnumber
	 * @param fentryid
	 * @return
	 */
	@WebMethod
	public String checkGdPdfirm(String fid, String accnumber, String fentryid);

	/**
	 * 仓库查询
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	@WebMethod
	public String getwarehouse(String userName, String password);

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
	@WebMethod
	public String changePrint(String userName, String password, String a, String b, String c, String d, String e,
			String f);

	/**
	 * 固定资产盘点
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	@WebMethod
	public String checkGD(String userName, String password);

	/**
	 * 固定资产打印
	 * 
	 * @param userName
	 * @param password
	 * @param id
	 * @return
	 */
	@WebMethod
	public String print2(String userName, String password, String id);

	/**
	 * 获取订单信息
	 * 
	 * @param name
	 * @param password
	 * @param flot
	 * @param fmobillno
	 * @return
	 */
	@WebMethod
	public String getBomli(String name, String password, String flot, String fmobillno);

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
	@WebMethod
	public String CreateCL(String name, String password, String fentryid, String accnumber, String stocknumber);

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
	@WebMethod
	public String SaveCL(String name, String password, String fentryid, String accnumber, String stocknumber);

	/**
	 * 获取列管资产信息
	 * 
	 * @param userName
	 * @param number
	 * @return
	 */
	@WebMethod
	public String getLG(String userName, String number);

	/**
	 * 获取仓位
	 * 
	 * @param stockid
	 * @return
	 */
	@WebMethod
	public String getCW(String stockid);
}
