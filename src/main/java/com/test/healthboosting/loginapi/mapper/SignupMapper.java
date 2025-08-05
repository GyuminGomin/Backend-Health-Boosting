package com.test.healthboosting.loginapi.mapper;

import com.test.healthboosting.loginapi.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SignupMapper {

    @Select("""
SELECT count(*) CNT
  FROM tb_member
 WHERE email = #{email} OR id = #{userId}
    
""")
    public Integer selectUserExists(MemberDTO dto) throws Exception;
}
