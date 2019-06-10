package com.ysttench.application.hdy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.ysttench.application.hdy.rdto.FileMsg;

public interface FileMapper {
@Select("select * from file_msg where FILE_NAME=#{othermsg}")
List<FileMsg> findmsg(String othermsg);
@Insert({ "insert into file_msg(FILE_NAME) values(#{othermsg})" })
void add(String othermsg);
@Select("select * from file_msg")
List<FileMsg> getmsg();

}
