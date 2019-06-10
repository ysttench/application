package com.ysttench.application.web.core.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * 接口
 * 
 * @author Howard
 *
 */
@WebService(targetNamespace = "http://service.core.web.application.ysttench.com/")
public interface AuthService {
	/**
	 * 获取仓库信息
	 * 
	 * @param organ
	 * @return
	 */
	@WebMethod
	public String getcangku(String organ);

	/**
	 * 获取入库单信息
	 * 
	 * @param organ
	 * @param date
	 * @return
	 */
	@WebMethod
	public String getGRN(String organ, String date);

	/**
	 * 获取入库单分页信息
	 * 
	 * @param organ
	 * @param fbillno
	 * @return
	 */
	@WebMethod
	public String getGRNBypage(String organ, String searchValue, String column, String sort);

	/**
	 * 获取出库单信息
	 * 
	 * @param organ
	 * @param date
	 * @return
	 */
	@WebMethod
	public String getCRN(String organ, String date, String searchValue);

	/**
	 * 获取分页出库单信息
	 * 
	 * @param organ
	 * @param fbillno
	 * @return
	 */
	@WebMethod
	public String getCRNBypage(String organ, String searchValue, String column, String sort);

	/**
	 * 获取打印条码信息
	 * 
	 * @param fbillno
	 * @param dgillno
	 * @return
	 */
	@WebMethod
	public String getprintMsg(String fbillno, String dgillno);

	/**
	 * 获取库存信息
	 * 
	 * @param organ
	 * @param ck
	 * @return
	 */
	@WebMethod
	public String getkucun(String organ, String ck);

	/**
	 * 获取分页库存信息
	 * 
	 * @param organ
	 * @param fbillno
	 * @return
	 */
	@WebMethod
	public String getkucunBypage(String organ, String searchValue, String column, String sort);

	/**
	 * 获取出库单详情信息
	 * 
	 * @param fid
	 * @return
	 */
	@WebMethod
	public String getchuku(String fid);

	/**
	 * 获取固定资产信息
	 * 
	 * @param organ
	 * @return
	 */
	@WebMethod
	public String getgudin(String organ);

	/**
	 * 获取固定资产分页信息
	 * 
	 * @param organ
	 * @param fbillno
	 * @return
	 */
	@WebMethod
	public String getgudinBypage(String organ, String searchValue, String column, String sort);

	/**
	 * 获取出库保存信息
	 * 
	 * @param fid
	 * @param fmaterialidNumber
	 * @return
	 */
	public String getDetailMateril(String fid, String id);

	/**
	 * 获取单个打印固定资产信息
	 * 
	 * @param fid
	 * @return
	 */
	public String getDprintMsgE(String id);

	/**
	 * 固定资产
	 * 
	 * @param organ
	 * @return
	 */
	public String getGDZC(String organ);

	/**
	 * 固定资产打印信息
	 * 
	 * @param id
	 * @return
	 */
	public String getprint2Msg(String id);

	/**
	 * 获取单个打印信息
	 * 
	 * @param id
	 * @return
	 */
	public String getDprintMsg(String id);

	/**
	 * 获取生产用料清单信息
	 * 
	 * @param fbillno
	 * @param fmobillno
	 * @param organ
	 * @return
	 */
	public String getBom(String flot, String fmobillno, String organ);

	/**
	 * 获取订单详细信息
	 * 
	 * @param fentryid
	 * @param organ
	 * @return
	 */
	public String getDetailBom(String fentryid, String organ);

	/**
	 * 获取组织编码
	 * 
	 * @param organ
	 * @return
	 */
	public String getorg(String organ);

	/**
	 * 获取仓位
	 * 
	 * @param stockid
	 * @return
	 */
	public String getcw(String stockid);

	/**
	 * 获取仓储盘点单信息
	 * 
	 * @param organ
	 * @param date
	 * @return
	 */
	public String getPd(String organ, String date);

	/**
	 * 获取仓储盘点单详情信息
	 * 
	 * @param fid
	 * @param stock
	 * @return
	 */
	public String getdePd(String fid, String stock);

	/**
	 * 仓储盘点确认
	 * 
	 * @param fid
	 * @param accnumber
	 * @param fentryid
	 * @return
	 */
	public String checkPd(String fid, String accnumber, String fentryid);

	/**
	 * 固定资产盘点单信息
	 * 
	 * @param organ
	 * @param date
	 * @return
	 */
	public String getGdPd(String organ, String date);

	/**
	 * 固定资产盘点单确认
	 * 
	 * @param fid
	 * @param accnumber
	 * @param fentryid
	 * @return
	 */
	public String checkGdPd(String fid, String accnumber, String fentryid);

	/**
	 * 固定资产盘点单详情
	 * 
	 * @param fid
	 * @return
	 */
	public String getdeGdPd(String fid);

	/**
	 * 出库单详情
	 * 
	 * @param fid
	 * @return
	 */
	public String getdePdCk(String fid);

	/**
	 * 获取组织下的部门信息
	 * 
	 * @param organ
	 * @return
	 */
	public String gedepartment(String organ);

	/**
	 * 获取特定组织和部门下的人员信息
	 * 
	 * @param organ
	 * @param department
	 * @return
	 */
	public String gekeeper(String organ, String department);

}
