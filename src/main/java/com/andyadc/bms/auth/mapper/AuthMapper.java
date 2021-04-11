package com.andyadc.bms.auth.mapper;

import com.andyadc.bms.auth.entity.AuthMenu;
import com.andyadc.bms.auth.entity.AuthUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthMapper {

    List<AuthMenu> selectMenuByUserId(Long userId);

    int insertMenuSelective(AuthMenu menu);

    int updateMenuByPrimaryKeySelective(AuthMenu menu);

    int insertUserSelective(AuthUser user);

    int updateUserByPrimaryKeySelective(AuthUser user);
}
