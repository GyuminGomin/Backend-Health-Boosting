# MVN 명령어

```access transformers
$ mvn clean install -U

$ mvn dependency:tree
- 현재 프로젝트의 의존성 라이브러리 목록 확인
```

# MySQL - Oracle

- vs
```sql
# oracle
MERGE INTO <Table>
USING (<condition>)
  SET (<condition>)
 WHEN MATCHED THEN <condition>
 WHEN NOT MATCHED THEN <condition>
  
# mysql
-- 위 방법 자체를 실무적 측면 속도적 측면 사용하지 않는다.
```
