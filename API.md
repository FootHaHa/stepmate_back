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
    "provider": "GOOGLE"
  }
}
```

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
      "accelX": 0.12,
      "accelY": -0.34,
      "accelZ": 9.81,
      "gyroX": 0.01,
      "gyroY": 0.02,
      "gyroZ": -0.01
    },
    {
      "stepNumber": 2,
      "footSide": "RIGHT",
      "measuredAt": "2026-06-01T10:00:00.100",
      "pressure1": 115,
      "pressure2": 130,
      "pressure3": 100,
      "pressure4": 108,
      "accelX": -0.05,
      "accelY": 0.20,
      "accelZ": 9.79,
      "gyroX": 0.00,
      "gyroY": -0.01,
      "gyroZ": 0.03
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
    "calories": 128.0,

    "avgHeelLeft": 312.4,
    "avgMidLeft": 198.7,
    "avgToeLeft": 245.1,
    "avgOuterLeft": 87.3,

    "avgHeelRight": 298.6,
    "avgMidRight": 210.2,
    "avgToeRight": 231.9,
    "avgOuterRight": 91.5,

    "avgLeftPressure": 210.875,
    "avgRightPressure": 208.05,
    "balanceScore": 99.33
  }
}
```

---

## 4-2. 월별 레포트 조회

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

## 4-3. 일별 레포트 조회

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
```
