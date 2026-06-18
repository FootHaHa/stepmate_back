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

## 주요 API

| 영역 | Method | Endpoint | 설명 |
| --- | --- | --- | --- |
| 인증 | `POST` | `/api/auth/google` | Google 로그인 및 JWT 발급 |
| 사용자 | `GET` | `/api/user/me` | 내 정보 조회 |
| 사용자 | `PATCH` | `/api/user/body-info` | 키·몸무게 수정 |
| 사용자 | `GET` | `/api/user/streak` | 연속 운동 기록 조회 |
| 러닝 | `POST` | `/api/run` | 러닝 세션 시작 |
| 러닝 | `PATCH` | `/api/run/{id}/finish` | 러닝 세션 종료 |
| 러닝 | `DELETE` | `/api/run/{id}` | 러닝 세션 삭제 |
| 리포트 | `GET` | `/api/run/{id}/summary` | 세션 분석 결과 조회 |
| 리포트 | `GET` | `/api/run/today` | 오늘의 요약 조회 |
| 리포트 | `GET` | `/api/run/monthly-stats` | 최근 30일 통계 조회 |
| 리포트 | `GET` | `/api/run/monthly` | 월별 운동 날짜 조회 |
| 리포트 | `GET` | `/api/run/daily` | 일별 세션 조회 |
| 센서 | `POST` | `/api/sensor/batch` | 센서 데이터 일괄 저장 |
| 게임 | `POST` | `/api/game/scores` | 게임 점수 저장 |
| 게임 | `GET` | `/api/game/ranking` | 게임 랭킹 조회 |
| 게임 | `GET` | `/api/game/my-best` | 내 최고 점수 조회 |

요청 및 응답 예시는 `API.md`에서 확인할 수 있습니다.

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

## 빌드 및 테스트

Windows:

```powershell
.\gradlew.bat test
.\gradlew.bat build
```

macOS / Linux:

```bash
./gradlew test
./gradlew build
```

## 연관 저장소

- Frontend: [FootHaHa/stepmate_front](https://github.com/FootHaHa/stepmate_front)
- Board: [FootHaHa/stepmate_board](https://github.com/FootHaHa/stepmate_board)
