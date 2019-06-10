package com.ysttench.application.hdy.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ysttench.application.hdy.rdto.SysMsg;

public interface SysmsgMapper {
@Select("select * from sys_msg")
    SysMsg findMsg();
@Update("update sys_msg set OLD_PATH=#{oldpath} ")
void updateold(String oldpath);
@Update("update sys_msg set NEW_PATH=#{newpath} ")
void updatenew(String newpath);

}
