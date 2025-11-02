# 🚀 Livlog - 블로그 플랫폼 백엔드

사용자들이 자신만의 블로그를 운영할 수 있는 플랫폼입니다. 포스트 작성, 카테고리 분류, 태그 관리, 댓글, 좋아요 등 다양한 기능을 제공합니다.

## 📋 목차

- [기술 스택](#-기술-스택)
- [주요 기능](#-주요-기능)
- [프로젝트 구조](#-프로젝트-구조)
- [시작하기](#-시작하기)
- [API 문서](#-api-문서)
- [데이터베이스 구조](#-데이터베이스-구조)
- [인증 및 보안](#-인증-및-보안)

## 🛠 기술 스택

### Backend
- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Security** - 인증/인가
- **Spring Data JPA** - ORM
- **H2 Database** - 개발/테스트용 인메모리 DB

### 인증
- **JWT (JSON Web Token)** - 토큰 기반 인증
- **OAuth2** - 소셜 로그인 (Google, GitHub, Kakao, Naver 등)

### 문서화
- **Springdoc OpenAPI (Swagger)** - API 문서 자동 생성

### 빌드 도구
- **Gradle 8.5**

## ✨ 주요 기능

### 👤 사용자 관리
- 로컬 회원가입/로그인
- OAuth2 소셜 로그인 (Google, GitHub, Kakao, Naver, Facebook, Twitter, LinkedIn)
- 프로필 관리 (닉네임, 프로필 이미지)
- 비밀번호 변경

### 📝 블로그 관리
- 블로그 정보 설정 (제목, 설명)
- 사용자별 블로그 커스터마이징

### 📄 포스트 기능
- 포스트 CRUD
- 포스트 검색 (제목, 내용, 날짜)
- 카테고리별 포스트 조회
- 태그별 포스트 조회
- 사용자별 포스트 조회
- 최신 포스트 목록

### 🏷️ 카테고리 & 태그
- 카테고리 CRUD
- 태그 CRUD
- 사용자별 카테고리 관리

### 💬 댓글 시스템
- 댓글 CRUD
- 대댓글 지원 (계층형 구조)
- 포스트별 댓글 조회

### ❤️ 좋아요 기능
- 포스트 좋아요/취소
- 좋아요 수 조회
- 사용자가 좋아요한 포스트 목록

### 📎 미디어 관리
- 이미지/파일 업로드
- 포스트별 미디어 관리
- 미디어 삭제

## 📁 프로젝트 구조

```
src/main/java/com/loglab/livlog/
├── auth/                       # 인증 관련
│   ├── jwt/                    # JWT 토큰 처리
│   │   ├── controller/
│   │   ├── entity/
│   │   ├── dto/
│   │   ├── repository/
│   │   └── JwtTokenProvider.java
│   └── oauth/                  # OAuth2 소셜 로그인
│       ├── handler/
│       ├── service/
│       └── dto/
├── user/                       # 사용자
│   ├── controller/
│   ├── entity/
│   ├── dto/
│   ├── repository/
│   └── service/
├── bloginfo/                   # 블로그 정보
│   ├── controller/
│   ├── entity/
│   ├── dto/
│   ├── repository/
│   └── service/
├── post/                       # 포스트
│   ├── controller/
│   ├── entity/
│   ├── dto/
│   ├── repository/
│   └── service/
├── category/                   # 카테고리
│   ├── controller/
│   ├── entity/
│   ├── dto/
│   ├── repository/
│   └── service/
├── tag/                        # 태그
│   ├── controller/
│   ├── entity/
│   ├── dto/
│   ├── repository/
│   └── service/
├── comment/                    # 댓글
│   ├── controller/
│   ├── entity/
│   ├── dto/
│   ├── repository/
│   └── service/
├── postlike/                   # 좋아요
│   ├── controller/
│   ├── entity/
│   ├── dto/
│   ├── repository/
│   └── service/
├── media/                      # 미디어
│   ├── controller/
│   ├── entity/
│   ├── dto/
│   ├── repository/
│   └── service/
└── global/                     # 공통 설정
    ├── config/                 # 설정 파일
    │   ├── SecurityConfig.java
    │   └── SwaggerConfig.java
    ├── dto/
    │   └── CommonResponse.java
    └── exception/
```

## 🚀 시작하기

### 사전 요구사항

- Java 17 이상
- Gradle 8.5 이상

### 설치 및 실행

1. **프로젝트 클론**
```bash
git clone <repository-url>
cd Livlog-backend
```

2. **application.yml 설정**

`src/main/resources/application.yml` 파일을 확인하고 필요한 설정을 수정합니다:

```yaml
spring:
  application:
    name: livlog

  # H2 Database
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:

  # H2 Console
  h2:
    console:
      enabled: true
      path: /h2-console

  # JPA
  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        format_sql: true
        show_sql: true

jwt:
  issuer: livlog-backend
  secret: your-secret-key-must-be-at-least-32-characters-long-for-hs512-algorithm
```

3. **빌드**
```bash
./gradlew clean build
```

4. **실행**
```bash
./gradlew bootRun
```

또는 JAR 파일로 실행:
```bash
java -jar build/libs/livlog-0.0.1-SNAPSHOT.jar
```

5. **접속 확인**
- 애플리케이션: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- H2 Console: http://localhost:8080/h2-console

## 📖 API 문서

### Swagger UI
서버 실행 후 다음 URL에서 모든 API를 확인하고 테스트할 수 있습니다:

**http://localhost:8080/swagger-ui/index.html**

### 주요 API 엔드포인트

#### 인증
- `POST /api/v1/auth/signup` - 회원가입
- `POST /api/v1/auth/login` - 로그인

#### 사용자
- `GET /api/v1/users/{userId}` - 사용자 조회
- `PUT /api/v1/users/{userId}/profile` - 프로필 수정
- `PUT /api/v1/users/{userId}/password` - 비밀번호 변경
- `DELETE /api/v1/users/{userId}` - 회원 탈퇴

#### 블로그 정보
- `GET /api/v1/bloginfo/user/{userId}` - 블로그 정보 조회
- `POST /api/v1/bloginfo` - 블로그 정보 생성
- `PUT /api/v1/bloginfo/user/{userId}` - 블로그 정보 수정

#### 포스트
- `GET /api/v1/post/list` - 전체 포스트 목록
- `GET /api/v1/post/detail/{id}` - 포스트 상세 조회
- `GET /api/v1/post/latest` - 최신 포스트
- `GET /api/v1/post/user/{userId}` - 사용자별 포스트
- `GET /api/v1/post/category/{categoryId}` - 카테고리별 포스트
- `GET /api/v1/post/tag/{tagId}` - 태그별 포스트
- `POST /api/v1/post/create` - 포스트 작성
- `PUT /api/v1/post/update` - 포스트 수정
- `DELETE /api/v1/post/delete/{id}` - 포스트 삭제

#### 카테고리
- `GET /api/v1/category/user/{userId}` - 사용자 카테고리 목록
- `POST /api/v1/category/create` - 카테고리 생성
- `PUT /api/v1/category/update/{id}` - 카테고리 수정
- `DELETE /api/v1/category/delete/{id}` - 카테고리 삭제

#### 태그
- `GET /api/v1/tags` - 전체 태그 목록
- `GET /api/v1/tags/{id}` - 태그 조회
- `POST /api/v1/tags` - 태그 생성
- `PUT /api/v1/tags/{id}` - 태그 수정
- `DELETE /api/v1/tags/{id}` - 태그 삭제

#### 댓글
- `GET /api/v1/comments/post/{postId}` - 포스트 댓글 목록
- `GET /api/v1/comments/{id}` - 댓글 조회
- `POST /api/v1/comments` - 댓글 작성
- `PUT /api/v1/comments/{id}` - 댓글 수정
- `DELETE /api/v1/comments/{id}` - 댓글 삭제

#### 좋아요
- `POST /api/v1/likes/toggle` - 좋아요 토글
- `GET /api/v1/likes/count/{postId}` - 좋아요 수
- `GET /api/v1/likes/post/{postId}` - 포스트의 좋아요 목록
- `GET /api/v1/likes/user/{userId}` - 사용자가 좋아요한 포스트
- `GET /api/v1/likes/info?postId=&userId=` - 좋아요 정보

#### 미디어
- `POST /api/v1/media/upload` - 파일 업로드
- `GET /api/v1/media/post/{postId}` - 포스트 미디어 목록
- `GET /api/v1/media/{id}` - 미디어 조회
- `DELETE /api/v1/media/{id}` - 미디어 삭제

자세한 API 명세는 [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)를 참조하세요.

## 🗄️ 데이터베이스 구조

### 주요 엔티티

#### User (사용자)
- id, email, password, nickname, username
- profile (프로필 이미지)
- role (USER, ADMIN)
- provider (LOCAL, GOOGLE, GITHUB, etc.)
- emailVerified

#### BlogInfo (블로그 정보)
- id, title, description
- user (OneToOne)

#### Post (포스트)
- id, title, content
- user (ManyToOne)
- category (ManyToOne)
- tags (ManyToMany)

#### Category (카테고리)
- id, name, activated
- user (ManyToOne)

#### Tag (태그)
- id, name
- posts (ManyToMany)

#### Comment (댓글)
- id, content
- post (ManyToOne)
- user (ManyToOne)
- parentComment (ManyToOne) - 대댓글용
- replies (OneToMany) - 자식 댓글들

#### PostLike (좋아요)
- postId, userId (복합키)
- post (ManyToOne)
- user (ManyToOne)

#### Media (미디어)
- id, fileName, fileType, url, size
- post (ManyToOne)

### ERD 관계
```
User 1:1 BlogInfo
User 1:N Post
User 1:N Category
User 1:N Comment
User N:M Post (through PostLike)

Post N:1 User
Post N:1 Category
Post N:M Tag
Post 1:N Comment
Post 1:N Media

Comment N:1 Post
Comment N:1 User
Comment 1:N Comment (self-referencing, 대댓글)
```

## 🔐 인증 및 보안

### JWT 토큰
- Access Token: 1시간 유효
- Refresh Token: 24시간 유효
- HS512 알고리즘 사용

### OAuth2 지원 Provider
- Google
- GitHub
- Kakao
- Naver
- Facebook
- Twitter
- LinkedIn

### 보안 설정
- CSRF 비활성화 (JWT 사용)
- Stateless 세션 관리
- 비밀번호 BCrypt 암호화

### 공개 엔드포인트
다음 엔드포인트는 인증 없이 접근 가능합니다:
- `/` - 홈
- `/swagger-ui/**` - Swagger UI
- `/v3/api-docs/**` - API 문서
- `/h2-console/**` - H2 콘솔
- `/api/v1/**` - 모든 API (임시, 개발 단계)
- `/login`, `/oauth2/**` - 로그인/OAuth2

## ⚙️ 환경 변수

프로덕션 환경에서는 다음 환경 변수를 설정해야 합니다:

```bash
# JWT
JWT_SECRET=your-very-long-and-secure-secret-key-at-least-32-characters
JWT_ISSUER=livlog-backend

# Database (프로덕션 사용 시)
DB_URL=jdbc:postgresql://localhost:5432/livlog
DB_USERNAME=your-db-username
DB_PASSWORD=your-db-password

# OAuth2 (선택사항)
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret

GITHUB_CLIENT_ID=your-github-client-id
GITHUB_CLIENT_SECRET=your-github-client-secret

# ... 기타 OAuth2 Provider 설정
```

## 🧪 테스트

```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 실행
./gradlew test --tests "com.loglab.livlog.user.*"
```

## 📝 개발 가이드

### 코드 컨벤션
- 패키지 구조: 기능별 모듈화 (user, post, comment 등)
- DTO 사용: 엔티티 직접 노출 방지
- Service 계층: 비즈니스 로직 처리
- Repository 계층: 데이터 접근 로직

### 응답 형식
모든 API는 공통 응답 형식을 사용합니다:

**성공 응답:**
```json
{
  "success": true,
  "data": { ... },
  "error": null
}
```

**에러 응답:**
```json
{
  "success": false,
  "data": null,
  "error": {
    "message": "에러 메시지",
    "code": "ERROR_CODE"
  }
}
```

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 라이센스

This project is licensed under the MIT License.

## 📞 문의

프로젝트 관련 문의사항이 있으시면 이슈를 등록해주세요.

---

**Made with ❤️ by Livlog Team**
