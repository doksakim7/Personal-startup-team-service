# 🚀 Startup Team Service
S3를 활용한 프로필 이미지 관리 및 팀원 정보를 관리하는 백엔드 서비스입니다.   
AWS EC2 환경에 최적화되어 있으며, Spring Boot Actuator를 통해 서버 상태를 모니터링할 수 있습니다.

## 🔗 Project Info
- EC2 Public IP: http://3.35.20.88:8080
- Actuator Info: http://3.35.20.88:8080/actuator/info


# 1. 프로젝트 소개
Spring Boot 애플리케이션을 AWS 환경(EC2, S3, RDS)에 배포하고, Stateless 아키텍처를 실습하기 위한 프로젝트입니다.  
코드의 가독성과 유지보수성을 높이기 위한 계층형 아키텍처와 전역 예외 처리를 적용했습니다.

# 2. 주요 기능
- 팀원 관리: 팀원 정보 등록 및 상세 조회 (JPA 활용)
- 프로필 이미지 관리: AWS S3를 연동한 이미지 업로드 및 Presigned URL 기반의 보안 다운로드/조회 기능
- 인프라 설정: AWS Parameter Store를 통한 환경 변수 관리 및 외부 노출 방지
- 모니터링: Spring Boot Actuator를 활용한 서버 상태 및 앱 정보 노출

# 3. 기술 스택
- Framework: Spring Boot 4.x
- Language: Java 17
- Database: H2 (Local), MySQL/RDS (Prod)
- Cloud: AWS EC2, S3, RDS, Parameter Store
- Build Tool: Gradle

# 4. 프로젝트 구조
src/main/java/kr/spartaclub/startupteamservice/  
├── controller/         # REST API 컨트롤러 (리소스 중심 설계)  
├── service/            # 비즈니스 로직 및 외부 서비스(S3) 연동  
├── repository/         # Spring Data JPA 레포지토리  
├── entity/             # JPA 엔티티 및 도메인 모델  
├── dto/                # Request/Response 객체 (계층 간 데이터 전송)  
└── global/             # 전역 예외 처리(GlobalExceptionHandler) 및 공통 설정  

# 5. API 문서
## Base URL
http://3.35.20.88:8080

## Member API
### POST /api/members
팀원 정보를 등록합니다.
- **Status**: `201 Created`

- **Request**
```json
{
  "name": "현빈",
  "age": 43,
  "mbti": "ENTJ"
}
```

- **Response**
```json
{
  "memberId": 1,
  "name": "현빈"
}
```

### GET /api/members/{memberId}
특정 팀원의 정보를 조회합니다.
- **Status**: `200 OK` / `400 Bad Request` (멤버 ID가 존재하지 않는 경우)

- **Response**
```json
{
  "name": "현빈",
  "age": 43,
  "mbti": "ENTJ"
}
```

## Profile Image API
### POST /api/members/{memberId}/profile-image
프로필 이미지를 S3에 업로드하고, 멤버 정보에 이미지 경로(`profileImageKey`)를 DB에 저장합니다.
- **Status**: `201 Created`

- **Request (multipart/form-data)**
```text
profileImage: hyunbin.png
```

- **Response**
```json
{
  "key": "uploads/5134dc48-2c20-4854-80e1-9ab07dd3e22b_hyunbin.png"
}
```

### GET /api/members/{memberId}/profile-image
DB에 저장된 멤버의 `profileImageKey`를 기반으로, S3 접근이 가능한 Presigned URL을 생성하여 반환합니다.
- **Status**: 
  - `200 OK`
  - `400 Bad Request` (멤버 ID가 존재하지 않는 경우)
  - `500 Internal Server Error`: S3 연결 실패 등 서버 내부적인 업로드 오류 발생 시 (`FileUploadException`)

- **Request (Query Parameter)**  
key: profileImageKey

- **Response**
```json
{
  "url": "https://bucket-name.s3.ap-northeast-2.amazonaws.com/uploads/...presigned-url"
}
```

## Actuator API
### GET /actuator/health
현재 애플리케이션의 구동 상태를 확인합니다.
- **Status**: `200 OK`

- **Response**
```json
{
  "groups": [
    "liveness",
    "readiness"
  ],
  "status": "UP"
}
```

### GET /actuator/info
애플리케이션의 커스텀 정보를 확인합니다.
- **Status**: `200 OK`

- **Response**
```json
{
  "app": {
    "message": "Hello AWS!",
    "team-name": "4조 화이팅!"
  }
}
```


----

## 🧩 Architecture

이 프로젝트는 Stateless 아키텍처를 기반으로 구성되었습니다.

- 애플리케이션 서버: EC2
- 파일 저장: S3
- 데이터 저장: RDS

애플리케이션 서버는 상태를 저장하지 않으며,
데이터와 파일은 외부 서비스에 분리하여 서버 교체 및 확장이 가능하도록 설계했습니다.

### 아키텍처 다이어그램
           Client
              │
              ▼
       EC2 (Spring Boot)
        │            │
        ▼            ▼
     RDS (MySQL)   S3 (Images)


## 🔐 Presigned URL

S3 파일 접근 보안을 위해 Presigned URL 방식을 사용했습니다.

이를 통해 서버에서 직접 파일을 전달하지 않고,
일정 시간 동안만 유효한 URL을 생성하여 클라이언트가 S3에 직접 접근하도록 구성했습니다.

----

#  🛠️ Troubleshooting
- Port 8080 Conflict: 이전에 종료되지 않은 프로세스가 8080 포트를 점유하여 서버 실행이 안 되었으나, lsof와 kill 명령어를 통해 프로세스를 정리하고 정상 실행함.
- Actuator 404 Error: 기본 보안 설정으로 인해 /actuator/info가 닫혀 있었으나, application.yml 설정을 통해 노출 범위를 조정하여 해결함.
- Parameter Store 권한 문제: EC2에서 AWS 리소스 접근 시 IAM Role 권한이 누락되어 서버 구동이 멈추는 현상을 발견하고 권한 부여를 통해 해결함.

----

## 과제 제출
### LV 0 - AWS Budgets 화면
![AWS Lv0.png](docs/AWS%20Lv0.png)

### LV 1 - 설정 완료된 EC2의 퍼블릭 IP
3.35.20.88

### LV 2 - RDS & Parameter Store
1. Actuator Info 엔드포인트 URL  
   http://3.35.20.88:8080/actuator/info

2. RDS 보안 그룹 스크린샷  
   ![AWS Lv2.png](docs/AWS%20Lv2.png)

### LV 3 - Presigned URL
멤버(현빈) 프로필 업로드/다운로드 성공
![AWS Lv3 (1).png](docs/AWS%20Lv3%20%281%29.png)
![AWS Lv3 (2).png](docs/AWS%20Lv3%20%282%29.png)

----

# ⚖️ License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
