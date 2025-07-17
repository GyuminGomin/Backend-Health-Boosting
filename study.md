# OAuth2 Google

## 
```access transformers
Securing GET /login/oauth2/code/google?state=3Bq_QnxQGJkzRqHmLRww5R-3KikyPv9SxTTeEavK_-U%3D&code=4%2F0AVMBsJhTbVosf7Oi4r1DhnwW84Ez4CjWGSX-COa8f_udb57MgD_EetqEQidcaJovfPsIvw&scope=email+profile+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+openid&authuser=0&prompt=consent

Changed session id from 372E3EFA6209CACD2D804F3E8C6C5420

Set SecurityContextHolder to OAuth2AuthenticationToken 
[Principal=
    Name: [101799480458511539465],
    Granted Authorities: [
        [OAUTH2_USER,
         SCOPE_https://www.googleapis.com/auth/userinfo.email,
         SCOPE_https://www.googleapis.com/auth/userinfo.profile,
         SCOPE_openid]
    ],
    User Attributes: [
        {sub=101799480458511539465, -- 고유 식별자
         name=김규민,
         given_name=규민,
         family_name=김,
         picture=https://lh3.googleusercontent.com/a/ACg8ocJgx_Sw7-IHqV3aWbrxaMEazuTYzuf9RUXCmPwC3IBMoAiJ3g=s96-c, -- 프로필 이미지 URL
         email=a63514894@gmail.com, -- email
         email_verified=true -- email 인증 여부
        }
    ],
    Credentials=[PROTECTED],
    Authenticated=true,
    Details=WebAuthenticationDetails
    [RemoteIpAddress=0:0:0:0:0:0:0:1,
     SessionId=372E3EFA6209CACD2D804F3E8C6C5420],
     Granted Authorities=[
        OAUTH2_USER,
        SCOPE_https://www.googleapis.com/auth/userinfo.email,
        SCOPE_https://www.googleapis.com/auth/userinfo.profile,
        SCOPE_openid
      ]
    ]
```