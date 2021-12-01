[![CI](http://15.165.96.28/buildStatus/icon?job=kodesalon-sonarqube)](http://15.165.96.28/job/kodesalon-sonarqube/)

# KodeSalon 게시판 서버

## ✅ 목표

- 기능구현에만 집중하지 않기
- 유연하고 확장성이 좋은 객체지향적인 코드 작성
- TDD 방식으로 개발하여 Test coverage 80% 이상 유지
- code convention 준수 : [캠퍼스 핵데이 Java 코딩 컨벤션](https://naver.github.io/hackday-conventions-java/)
  , [AngularJS Commit Message Conventions](https://gist.github.com/stephenparish/9941e89d80e2bc58a153)
- Git branch 전략 사용
- 프로젝트 문제사항 및 해결 방법 Github wiki 공유
- 프로젝트 진행사항 Github issue, project 관리

## ✅ 기술 스택

### Backend

- Java11
- Spring Boot
- JPA, Querydsl, Flyway
- Junit5, Mockito, DbUnit
- Gradle

### DevOps

- AWS - EC2, RDS, S3, CodeDeploy
- MySQL, Redis
- Jenkins
- Nginx
- Sonarqube
- Docker

### Collaboration & Tool

- Slack
- Intellij, Vim
- Git, Github

## ✅ 서버 구조도

<img width="1277" alt="kodesalon-structure" src="https://user-images.githubusercontent.com/44643805/144169788-a23af487-dbaf-4a33-9da7-29d17612693b.png">
### 서버 구조를 적용한 방법

#### AWS

- [AWS EC2](https://www.notion.so/seongbeenkim/EC2-d8bd1bca162642b5a935b121bc4a4880)
- [AWS RDS](https://www.notion.so/RDS-73782b76bd5f4e389401dc999be8b919)
- [AWS S3](https://www.notion.so/seongbeenkim/S3-1848373e3f9948d4ac9d37790ee1d4c1)
- [AWS CodeDeploy](https://www.notion.so/seongbeenkim/CodeDeploy-97ad4fdcef054d75a7e4aa1e9eec11d4)

#### Jenkins

- [Jenkins](https://www.notion.so/seongbeenkim/Jenkins-6040c90c24ba48efa248451fe3f4560f)

#### Sonarqube

- [Sonarqube](https://www.notion.so/seongbeenkim/Sonarqube-075c4a9921a94067b82a2f595fcf9b81)

#### Docker

- [Docker](https://www.notion.so/seongbeenkim/Docker-edb9dcd4c7504a54bfafe7bcff7d6ec0)

## ✅ 기술적 이슈

- 세션 관리
  - [분산 서버 Session 관리 방법 1 - Scale up, Scale out](https://www.notion.so/seongbeenkim/bfe0124212734b0f91fe2f190a51ac86?v=92efa7174bf44632ae00e8e831771cd4&p=eb19d98b96bc4a4fb8e84f1a6ec22d5a)
  - [분산 서버 Session 관리 방법 2 - Sticky Session, Session Clustering, Session Storage](https://www.notion.so/seongbeenkim/bfe0124212734b0f91fe2f190a51ac86?v=92efa7174bf44632ae00e8e831771cd4&p=d3048fe4fc5648fbbaadbbb0bcba93a7)
  - [분산 서버 Session 관리 방법 3 - Session Storage용 Database](https://www.notion.so/seongbeenkim/bfe0124212734b0f91fe2f190a51ac86?v=92efa7174bf44632ae00e8e831771cd4&p=3c9fbcfb3ce74a89b8ec68774d6d9582)
  - [JWT → Redis for session - 1. JWT를 사용하지 말아야 하는 이유](https://seongbeenkim.notion.site/JWT-Redis-for-session-1-JWT-1dc34ee724aa4dca82c1ef9b03462c80)
  - [JWT → Redis for session - 2. Redis 적용](https://www.notion.so/seongbeenkim/b463d484f43249c2b3ae22142d4c2015?v=fa1582d8d8de4913a694275c1915bfca&p=aedc7b46b00046e284a67337c96be690)

- 성능 향상
  - [JPA 기존 paging 조회 시 발생하는 N+1, 불필요한 Count 쿼리 발생, full scan 쿼리를 Query 튜닝을 활용한 성능 개선하기](https://www.notion.so/Paging-e804041f2a0a4274aaf982e93a0a4954)
  - [성능 향상을 위한 Cache 적용 - 1. Cache, Cashing Strategies](https://www.notion.so/seongbeenkim/b463d484f43249c2b3ae22142d4c2015?v=fa1582d8d8de4913a694275c1915bfca&p=6e85614d73a84bf98ae0b2fdb7dfaec2)
  - [성능 향상을 위한 Cache 적용 - 2. Redis 적용 & Eviction](https://www.notion.so/seongbeenkim/b463d484f43249c2b3ae22142d4c2015?v=fa1582d8d8de4913a694275c1915bfca&p=2d1dd944738d4f33b8501b96739d56e3)
  - [DB 분산 처리를 위한 Replication 적용하기](https://www.notion.so/seongbeenkim/Replication-31fb707a5d0849aea6ccc0e7b1bb28b5#e02eb99a1da1460db054fd34f00cc6bf)

- [JPA DTO 프로젝션을 간소회하기 위해 Querydsl 도입하기](https://www.notion.so/QueryDsl-f09fc5067e5b4a0a809b1509053dc238)
- [목표로 잡은 테스트 커버리지를 유지하기 위해 Jacoco 적용하기](https://www.notion.so/seongbeenkim/Jacoco-db6f0853e10749019b902c68efa5875b)
- [Production 깔끔하게 유지하며, 테스트 기반으로 API 문서를 자동화하는 Rest Docs 활용하기](https://www.notion.so/seongbeenkim/Spring-REST-Docs-12e598fd203c4a69bddbee6267d12cb7)
- [DB Unit 을 활용하여 DB 테스트 간소화 및 테스트 별 독립적인 테스트 환경 구축하기](https://www.notion.so/DB-Unit-b9c221b3d0fe4f94bdd46ada22ac31cf)
- [형상 관리를 통한 DB 마이그레이션을 하는 Flyway 적용하기](https://www.notion.so/seongbeenkim/Flyway-a1299bd54e94455e85facea386df6643)

