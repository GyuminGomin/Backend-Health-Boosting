package com.test.healthboosting.common.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CustomOAuth2UserMapper {

    @Select("""
SELECT COUNT(*)
  FROM tb_member
 WHERE oauth_provider_id = #{oauthProvideId}
    """)
    public Integer isExistUser(String oauthProvideId);

    @Insert("""
INSERT INTO tb_member
(
 login_type,
 oauth_provider,
 oauth_provider_id,
 user_id,
 password,
 phone_number,
 gender,
 email,
 name,
 profile_image_url,
 role,
 dt_create,
 dt_update,
 birthday
)
VALUES
(
 #{loginType},
 #{oauthProvider},
 #{oauthProvideId},
 #{userId},
 #{password},
 #{phoneNumber},
 #{gender},
 #{email},
 #{name},
 #{profileImageUrl},
 #{role},
 NOW(),
 NOW(),
 #{birthday}
)
ON DUPLICATE KEY UPDATE
email = VALUES(email),
name = VALUES(name),
profile_image_url = VALUES(profile_image_url),
dt_update = NOW()

    """)
    public Integer insertUser();
}
