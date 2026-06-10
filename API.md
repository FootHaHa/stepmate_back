# StepMate API 명세

---

# 1. 사용자(User)

## 1-1. 사용자 정보 조회

### Request

**GET** `/api/user/me`

* Bearer Token 필요

### Response

```json
{
  "code": "S200",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "email": "user@example.com",
    "name": "홍길동",
    "profileImageUrl": "https://example.com/profile.jpg",
    "weightKg": 60.0,
    "heightCm": 170.0,
    "provider": "GOOGLE"
  }
}
```

---

## 1-2. 신체 정보 수정

### Request

**PATCH** `/api/user/body-info`

* Bearer Token 필요

```json
{
  "weightKg": 72.5,
  "heightCm": 175.0
}
```

### Response

```json
{
  "code": "S200",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "email": "user@example.com",
    "name": "홍길동",
    "profileImageUrl": "https://example.com/profile.jpg",
    "weightKg": 72.5,
    "heightCm": 175.0,
    "provider": "GOOGLE"
  }
}
```

---

## 1-3. 연속 달리기 기록 조회

### Request

**GET** `/api/user/streak`

* Bearer Token 필요

### Response

```json
{
  "code": "S200",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "currentStreak": 5,
    "maxStreak": 12,
    "ranToday": true
  }
}
```

> - `currentStreak` : 오늘 기준 현재 연속 달리기 일수 (오늘 또는 어제 달리지 않았으면 0)
> - `maxStreak` : 역대 최대 연속 달리기 일수
> - `ranToday` : 오늘 달리기 세션 완료 여부

---

# 2. 달리기(Run)

## 2-1. 달리기 세션 시작

### Request

**POST** `/api/run`

* Bearer Token 필요

### Response

```json
{
  "code": "S201",
  "message": "리소스가 성공적으로 생성되었습니다.",
  "data": {
    "runSessionId": 1,
    "startedAt": "2026-06-01T20:30:00",
    "status": "IN_PROGRESS"
  }
}
```

---

## 2-2. 달리기 세션 종료

### Request

**PATCH** `/api/run/{runSessionId}/finish`

* Bearer Token 필요

```json
{
  "runSessionId": 1,
  "totalSteps": 3200,
  "startedAt": "2026-06-01T20:30:00",
  "endedAt": "2026-06-01T21:00:00",
  "durationSeconds": 1800
}
```

### Response

* 반환 값 없음 (204 No Content)

---

## 2-3. 달리기 세션 삭제

### Request

**DELETE** `/api/run/{runSessionId}`

* Bearer Token 필요
* Path Variable

    * `runSessionId` : 삭제할 세션 ID

### Response

```json
{
  "code": "S200",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": null
}
```

---

# 3. 센서 데이터(Sensor)

## 3-1. 원시 데이터 일괄 전송

### 압력 센서 매핑

| 필드 | 위치 |
| ---- | ---- |
| `pressure1` | 엄지발가락 (T1) |
| `pressure2` | 5번째 중족골 (M5) |
| `pressure3` | 1번째 중족골 (M1) |
| `pressure4` | 발 뒤꿈치 (heel) |
| `pressure5` | 중족부 (MF) |

### FootType 값 설명

| 값 | 설명 |
| --- | --- |
| `NORMAL` | 일반 |
| `DUCK_FOOT` | 팔자 |
| `PIGEON_TOE` | 안짱 |

### Request

**POST** `/api/sensor/batch`

* Bearer Token 필요

```json
{
  "runSessionId": 1,
  "data": [
    {
      "stepNumber": 1,
      "footSide": "LEFT",
      "measuredAt": "2026-06-01T10:00:00",
      "pressure1": 120,
      "pressure2": 135,
      "pressure3": 98,
      "pressure4": 110,
      "pressure5": 95,
      "footType": "NORMAL",
      "groundAngle": 2.5
    },
    {
      "stepNumber": 2,
      "footSide": "RIGHT",
      "measuredAt": "2026-06-01T10:00:00.100",
      "pressure1": 115,
      "pressure2": 130,
      "pressure3": 100,
      "pressure4": 108,
      "pressure5": 90,
      "footType": "DUCK_FOOT",
      "groundAngle": -1.3
    }
  ]
}
```

### Response

```json
{
  "code": "S201",
  "message": "리소스가 성공적으로 생성되었습니다.",
  "data": null
}
```

---

# 4. 레포트(Report)

## 4-1. 상세 레포트 조회

### Request

**GET** `/api/run/{runSessionId}/summary`

* Bearer Token 필요

### LandingType 값 설명

| 값 | 설명 |
| --- | --- |
| `CLASS1` | 엄지발가락 신전형 |
| `CLASS2` | 중족부-외측 전족부 추진형 |
| `CLASS3` | 일반형 |

### Response

```json
{
  "code": "S200",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "summaryId": 1,
    "runSessionId": 42,
    "startedAt": "2026-06-02T09:00:00",
    "endedAt": "2026-06-02T09:30:00",
    "durationSeconds": 1800,
    "totalSteps": 3200,
    "totalDistanceKm": 2.34,
    "averagePace": 769.23,
    "calories": 128.0,

    "avgT1Left": 245.1,
    "avgM5Left": 198.7,
    "avgM1Left": 87.3,
    "avgHeelLeft": 312.4,
    "avgMFLeft": 102.5,

    "avgT1Right": 231.9,
    "avgM5Right": 210.2,
    "avgM1Right": 91.5,
    "avgHeelRight": 298.6,
    "avgMFRight": 98.3,

    "avgLeftPressure": 189.2,
    "avgRightPressure": 186.1,
    "balanceScore": 99.17,

    "leftLandingType": "CLASS3",
    "rightLandingType": "CLASS1",

    "uphillSeconds": 420,
    "downhillSeconds": 180,
    "flatSeconds": 1200,

    "normalRatio": 65.0,
    "duckFootRatio": 25.0,
    "pigeonToeRatio": 10.0
  }
}
```

### 경사 구간 시간 설명

| 필드 | 설명 | 분류 기준 (groundAngle 기준) |
| --- | --- | --- |
| `uphillSeconds` | 오르막 구간 소요 시간 (초) | `groundAngle >= 5.0°` |
| `downhillSeconds` | 내리막 구간 소요 시간 (초) | `groundAngle <= -5.0°` |
| `flatSeconds` | 평지 구간 소요 시간 (초) | `-5.0° < groundAngle < 5.0°` |

* `groundAngle`은 프론트엔드에서 EMA 평활(α=0.03) 처리 후 전송된 값
* 각 스텝의 실제 타임스탬프 간격으로 시간을 산출하므로 걷기/달리기 속도 차이가 반영됨

---

## 4-2. 오늘 하루 통합 요약 조회

### Request

**GET** `/api/run/today`

* Bearer Token 필요

### Response

```json
{
  "code": "S200",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "totalDistanceKm": 5.12,
    "averagePace": 352.34,
    "averageBalanceScore": 97.5
  }
}
```

> - `totalDistanceKm` : 오늘 완료된 세션의 거리 합산 (km)
> - `averagePace` : 오늘 전체 운동 시간 / 전체 거리 (초/km)
> - `averageBalanceScore` : 오늘 세션들의 보행 균형 점수 평균

---

## 4-3. 최근 30일 통계 조회

### Request

**GET** `/api/run/monthly-stats`

* Bearer Token 필요

### Response

```json
{
  "code": "S200",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "avgLeftLandingType": "CLASS3",
    "avgRightLandingType": "CLASS1",
    "totalDurationSeconds": 7200,
    "totalDistanceKm": 15.4,
    "averagePace": 467.53,
    "totalCalories": 520.0
  }
}
```

> - `avgLeftLandingType` : 최근 30일 왼발 착지 유형 최빈값 (`CLASS1` / `CLASS2` / `CLASS3`)
> - `avgRightLandingType` : 최근 30일 오른발 착지 유형 최빈값
> - `totalDurationSeconds` : 최근 30일 총 달리기 시간 (초)
> - `totalDistanceKm` : 최근 30일 총 달리기 거리 (km)
> - `averagePace` : 최근 30일 평균 페이스 (초/km)
> - `totalCalories` : 최근 30일 총 소모 칼로리 (kcal)

---

## 4-4. 월별 레포트 조회

### Request

**GET** `/api/run/monthly?year=2026&month=6`

* Bearer Token 필요
* Query Parameter

    * `year`
    * `month`

### Response

```json
{
  "code": "S200",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "dates": [
      "2026-06-02",
      "2026-06-05",
      "2026-06-10"
    ]
  }
}
```

---

## 4-5. 일별 레포트 조회

### Request

**GET** `/api/run/daily?date=2026-06-02`

* Bearer Token 필요
* Query Parameter

    * `date` (yyyy-MM-dd)

### Response

```json
{
  "code": "S200",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": [
    {
      "runSessionId": 42,
      "startedAt": "2026-06-02T09:00:00",
      "durationSeconds": 1800,
      "totalSteps": 3200
    }
  ]
}
```

---

# 공통 응답 형식

모든 API는 아래 형태의 공통 응답을 사용합니다.

```json
{
  "code": "S200",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {}
}
```

## Success Code

| Code | Message             |
| ---- | ------------------- |
| S200 | 요청이 성공적으로 처리되었습니다.  |
| S201 | 리소스가 성공적으로 생성되었습니다. |

## Error Code

| Code | Message            |
| ---- | ------------------ |
| E400 | 잘못된 요청입니다.         |
| E401 | 권한이 없습니다.          |
| E403 | 승인이 거부되었습니다.       |
| E404 | 요청 리소스를 찾을 수 없습니다. |
| E500 | 서버 내부 오류가 발생했습니다.  |

```
