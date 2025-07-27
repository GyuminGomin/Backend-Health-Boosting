# OAuth2 Google

## 
```
User Attributes: [
    {
    sub=101799480458511539465,
    name=김규민,
    given_name=규민,
    family_name=김,
    picture=https://lh3.googleusercontent.com/a/ACg8ocJgx_Sw7-IHqV3aWbrxaMEazuTYzuf9RUXCmPwC3IBMoAiJ3g=s96-c, -- 프로필 이미지 URL
    email=a63514894@gmail.com, -- email
    email_verified=true -- email 인증 여부
    }
]
```


# 도커 관련
## 도커 파일 만들기
```
- docker redis 테스트
docker run -d --name redis-test -p 6379:6379 redis

- docker ps로 현재 프로세스 확인
docker ps
CONTAINER ID   IMAGE     COMMAND                   CREATED          STATUS          PORTS                    NAMES
949b0795e492   redis     "docker-entrypoint.s…"   41 minutes ago   Up 41 minutes   0.0.0.0:6379->6379/tcp   redis-test

-> 위에서 Container ID 또는 Names를 복사

- Redis CLI로 컨테이너 접속
docker exec -it redis-test redis-cli

- 명령어
# 키 전체 보기 (테스트 환경만)
keys *

# 특정 패턴 보기 (이메일 인증용만)
keys email:verify:*

# 특정 키 값 확인
get email:verify:test@example.com

# TTL 확인 (남은 유효 시간)
ttl email:verify:test@example.com
```
# Restful 방식
```
1. GET
2. POST - 처음 /api/users
3. PUT - 전체 수정 시 /api/users/1
4. PATCH - 일부 수정시 /api/users/1 에 email 같은거
5. DELETE

Restful 방식에 맞지 않아서 post만 쓰는 방식은 권장하지 않음
Swagger-ui에 정확하게 딱 들어 맞으려면 put과 patch 사용 필요
```