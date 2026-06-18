# StepMate Back

StepMate 서비스의 Spring Boot 기반 REST API 서버입니다. Google 로그인과 JWT 인증을 처리하고, 사용자 정보, 러닝 세션, 스마트 인솔 센서 데이터, 보행 분석 결과 및 게임 랭킹을 관리합니다.

## 주요 기능

- Google ID Token 검증 및 사용자 등록·로그인
- JWT 발급 및 API 인증
- 사용자 신체 정보와 연속 운동 기록 관리
- 러닝 세션 시작, 종료 및 삭제
- 압력·보행 센서 데이터 일괄 저장
- 일별·월별 기록과 세션 분석 결과 제공
- 게임 점수 저장 및 랭킹 조회

## 기술 스택

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Security
- Spring Data JPA
- PostgreSQL
- Gradle
- JWT
- Google API Client

## 사전 준비

- JDK 21
- PostgreSQL
- Google OAuth 클라이언트 ID

Gradle은 Wrapper가 포함되어 있어 별도로 설치하지 않아도 됩니다.

## 데이터베이스 준비

PostgreSQL에 StepMate가 사용할 데이터베이스를 생성합니다.

```sql
CREATE DATABASE stepmate_db;
```

`src/main/resources/application.properties`에 데이터베이스 연결 정보, JWT 비밀키, Google OAuth 클라이언트 ID를 설정합니다.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/stepmate_db
spring.datasource.username=<DB_USERNAME>
spring.datasource.password=<DB_PASSWORD>

spring.jwt.secret=<32_BYTES_OR_LONGER_SECRET>
google.oauth.client-ids=<ANDROID_CLIENT_ID>,<WEB_CLIENT_ID>
```

실제 비밀번호, JWT 비밀키 및 OAuth 설정값은 공개 저장소에 커밋하지 마세요. 배포 환경에서는 환경 변수나 별도의 비밀 관리 도구를 사용하는 것을 권장합니다.

## 프로젝트 구조

```text
src/main/java/foothaha/stepmate_back/
├─ auth/       # Google 로그인
├─ config/     # Spring Security, JWT
├─ game/       # 게임 점수와 랭킹
├─ response/   # 공통 응답 및 예외 처리
├─ run/        # 러닝 세션과 분석 결과
├─ sensor/     # 스마트 인솔 센서 데이터
└─ user/       # 사용자 정보
```

## 실행하기

Windows:

```powershell
.\gradlew.bat bootRun
```

macOS / Linux:

```bash
./gradlew bootRun
```

기본 포트는 `8080`입니다.

## 인증 방식

로그인을 제외한 보호된 API는 다음 헤더에 백엔드가 발급한 JWT를 전달해야 합니다.

```http
Authorization: Bearer <JWT>
```

Google 로그인 요청:

```http
POST /api/auth/google
Content-Type: application/json
```

```json
{
  "idToken": "<GOOGLE_ID_TOKEN>"
}
```

<br>

## 👥 Contributors

<table>
  <tr>
    <td align="center"><a href="https://github.com/mainsprout"><img src="https://avatars.githubusercontent.com/u/143585656?s=400&u=c4fc8317d32cc54a7091f164a2667cbbc14fa482&v=4" width="100px;" alt=""/><br /><sub><b>mainsprout</b></sub></a><br />🌱</td>
    <td align="center"><a href="https://github.com/hym7196"><img src="https://avatars.githubusercontent.com/u/64295988?v=4" width="100px;" alt=""/><br /><sub><b>hym7196</b></sub></a><br />🎨</td>
    <td align="center"><a href="https://github.com/dusal1111"><img src="https://avatars.githubusercontent.com/u/147612119?v=4" width="100px;" alt=""/><br /><sub><b>dusal1111</b></sub></a><br />🎨</td>
  </tr>
</table>

## 연관 저장소

- Frontend: [FootHaHa/stepmate_front](https://github.com/FootHaHa/stepmate_front)
- Board: [FootHaHa/stepmate_board](https://github.com/FootHaHa/stepmate_board)
