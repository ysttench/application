package com.ysttench.application.htbw.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htbw.settings.kernel.entity.WarnMsgFormMap;

public interface WarnMsgMapper extends BaseMapper {

    List<WarnMsgFormMap> findMsg(WarnMsgFormMap warnMsgFormMap);

    List<WarnMsgFormMap> findWarnMsgByPage(WarnMsgFormMap formMap);}