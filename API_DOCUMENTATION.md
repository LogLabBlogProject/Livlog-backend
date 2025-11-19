# 📚 Livlog API 문서

## 목차
- [개요](#개요)
- [인증 API](#-인증-api)
- [사용자 API](#-사용자-api)
- [블로그 정보 API](#-블로그-정보-api)
- [포스트 API](#-포스트-api)
- [카테고리 API](#-카테고리-api)
- [태그 API](#-태그-api)
- [댓글 API](#-댓글-api)
- [좋아요 API](#-좋아요-api)
- [미디어 API](#-미디어-api)
- [공통 응답 형식](#공통-응답-형식)
- [에러 코드](#에러-코드)

---

## 개요

### Base URL
```
http://localhost:8080
```

### 공통 헤더
```http
Content-Type: application/json
Authorization: Bearer {access_token}  # 인증이 필요한 API
```

### 공통 응답 형식
모든 API는 다음 형식으로 응답합니다:

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

---

## 🔐 인증 API

### 회원가입
로컬 계정으로 회원가입합니다.

```http
POST /api/v1/auth/signup
```

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123!",
  "nickname": "홍길동",
  "profile": "https://example.com/profile.jpg"  // optional
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "email": "user@example.com",
    "nickname": "홍길동",
    "profile": "https://example.com/profile.jpg",
    "role": "USER",
    "provider": "LOCAL",
    "emailVerified": true,
    "createdAt": "2025-11-02T10:00:00",
    "updatedAt": "2025-11-02T10:00:00"
  }
}
```

### 로그인
이메일과 비밀번호로 로그인합니다.

```http
POST /api/v1/auth/login
```

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123!"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "user": {
      "id": 1,
      "email": "user@example.com",
      "nickname": "홍길동"
    }
  }
}
```

---

## 👤 사용자 API

### 사용자 조회
특정 사용자의 정보를 조회합니다.

```http
GET /api/v1/users/{userId}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "email": "user@example.com",
    "nickname": "홍길동",
    "profile": "https://example.com/profile.jpg",
    "role": "USER",
    "provider": "LOCAL",
    "emailVerified": true,
    "createdAt": "2025-11-02T10:00:00",
    "updatedAt": "2025-11-02T10:00:00"
  }
}
```

### 이메일로 사용자 조회
이메일 주소로 사용자를 조회합니다.

```http
GET /api/v1/users/email/{email}
```

**Example:**
```http
GET /api/v1/users/email/user@example.com
```

### 프로필 수정
사용자 프로필 정보를 수정합니다.

```http
PUT /api/v1/users/{userId}/profile
```

**Request Body:**
```json
{
  "nickname": "새로운닉네임",
  "profile": "https://example.com/new-profile.jpg"
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "email": "user@example.com",
    "nickname": "새로운닉네임",
    "profile": "https://example.com/new-profile.jpg",
    ...
  }
}
```

### 비밀번호 변경
사용자의 비밀번호를 변경합니다.

```http
PUT /api/v1/users/{userId}/password
```

**Request Body:**
```json
{
  "oldPassword": "old_password123!",
  "newPassword": "new_password456!"
}
```

**Response:**
```json
{
  "success": true,
  "data": "비밀번호가 변경되었습니다"
}
```

### 회원 탈퇴
사용자를 삭제합니다.

```http
DELETE /api/v1/users/{userId}
```

**Response:**
```json
{
  "success": true,
  "data": "사용자가 삭제되었습니다"
}
```

---

## 📰 블로그 정보 API

### 블로그 정보 조회
특정 사용자의 블로그 정보를 조회합니다.

```http
GET /api/v1/bloginfo/user/{userId}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "홍길동의 기술 블로그",
    "description": "개발 일지와 기술 이야기를 공유합니다",
    "userId": 1
  }
}
```

### 블로그 정보 생성
새로운 블로그 정보를 생성합니다.

```http
POST /api/v1/bloginfo
```

**Request Body:**
```json
{
  "title": "홍길동의 기술 블로그",
  "description": "개발 일지와 기술 이야기를 공유합니다",
  "userId": 1
}
```

### 블로그 정보 수정
블로그 정보를 수정합니다.

```http
PUT /api/v1/bloginfo/user/{userId}
```

**Request Body:**
```json
{
  "title": "새로운 블로그 제목",
  "description": "새로운 블로그 설명"
}
```

### 블로그 정보 삭제
블로그 정보를 삭제합니다.

```http
DELETE /api/v1/bloginfo/user/{userId}
```

---

## 📝 포스트 API

### 전체 포스트 목록 조회
모든 포스트를 조회합니다.

```http
GET /api/v1/post/list
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "Spring Boot 시작하기",
      "content": "Spring Boot에 대해 알아봅시다...",
      "userId": 1,
      "categoryId": 1,
      "tags": ["Spring", "Java"],
      "createdAt": "2025-11-02T10:00:00",
      "updatedAt": "2025-11-02T10:00:00"
    }
  ]
}
```

### 포스트 상세 조회
특정 포스트의 상세 정보를 조회합니다.

```http
GET /api/v1/post/detail/{id}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "Spring Boot 시작하기",
    "content": "Spring Boot에 대해 알아봅시다. 전체 내용...",
    "userId": 1,
    "categoryId": 1,
    "tags": [
      { "id": 1, "name": "Spring" },
      { "id": 2, "name": "Java" }
    ],
    "createdAt": "2025-11-02T10:00:00",
    "updatedAt": "2025-11-02T10:00:00"
  }
}
```

### 최신 포스트 조회
최신 포스트를 생성일 기준 내림차순으로 조회합니다.

```http
GET /api/v1/post/latest
```

### 사용자별 포스트 조회
특정 사용자의 모든 포스트를 조회합니다.

```http
GET /api/v1/post/user/{userId}
```

### 카테고리별 포스트 조회
특정 카테고리의 모든 포스트를 조회합니다.

```http
GET /api/v1/post/category/{categoryId}
```

### 태그별 포스트 조회
특정 태그가 포함된 모든 포스트를 조회합니다.

```http
GET /api/v1/post/tag/{tagId}
```

### 포스트 검색
조건에 맞는 포스트를 검색합니다.

```http
GET /api/v1/post/search
```

**Request Body:**
```json
{
  "title": "Spring",
  "content": "Boot",
  "sCreatedDate": "2025-01-01T00:00:00",
  "eCreatedDate": "2025-12-31T23:59:59"
}
```

### 포스트 작성
새로운 포스트를 작성합니다.

```http
POST /api/v1/post/create
```

**Request Body:**
```json
{
  "title": "새로운 포스트 제목",
  "content": "포스트 내용입니다...",
  "userId": 1,
  "categoryId": 1,
  "tagIds": [1, 2, 3]
}
```

### 포스트 수정
포스트를 수정합니다.

```http
PUT /api/v1/post/update
```

**Request Body:**
```json
{
  "id": 1,
  "title": "수정된 제목",
  "content": "수정된 내용..."
}
```

### 포스트 삭제
포스트를 삭제합니다.

```http
DELETE /api/v1/post/delete/{id}
```

---

## 🏷️ 카테고리 API

### 사용자 카테고리 목록 조회
특정 사용자의 모든 카테고리를 조회합니다.

```http
GET /api/v1/category/user/{userId}
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "개발",
      "activated": true,
      "userId": 1,
      "createdAt": "2025-11-02T10:00:00"
    },
    {
      "id": 2,
      "name": "일상",
      "activated": true,
      "userId": 1,
      "createdAt": "2025-11-02T10:00:00"
    }
  ]
}
```

### 카테고리 생성
새로운 카테고리를 생성합니다.

```http
POST /api/v1/category/create
```

**Request Body:**
```json
{
  "name": "새로운 카테고리",
  "userId": 1
}
```

### 카테고리 수정
카테고리 이름을 수정합니다.

```http
PUT /api/v1/category/update/{id}
```

**Request Body:**
```json
{
  "name": "수정된 카테고리명"
}
```

### 카테고리 삭제 (소프트 삭제)
카테고리를 비활성화합니다.

```http
DELETE /api/v1/category/delete/{id}
```

---

## 🔖 태그 API

### 전체 태그 목록 조회
모든 태그를 조회합니다.

```http
GET /api/v1/tags
```

**Response:**
```json
{
  "success": true,
  "data": [
    { "id": 1, "name": "Spring" },
    { "id": 2, "name": "Java" },
    { "id": 3, "name": "React" }
  ]
}
```

### 태그 조회
특정 태그를 조회합니다.

```http
GET /api/v1/tags/{id}
```

### 태그 생성
새로운 태그를 생성합니다.

```http
POST /api/v1/tags
```

**Request Body:**
```json
{
  "name": "새로운태그"
}
```

### 태그 수정
태그 이름을 수정합니다.

```http
PUT /api/v1/tags/{id}
```

**Request Body:**
```json
{
  "name": "수정된태그명"
}
```

### 태그 삭제
태그를 삭제합니다.

```http
DELETE /api/v1/tags/{id}
```

---

## 💬 댓글 API

### 포스트 댓글 목록 조회
특정 포스트의 모든 댓글을 조회합니다.

```http
GET /api/v1/comments/post/{postId}
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "postId": 1,
      "userId": 2,
      "parentCommentId": null,
      "content": "좋은 글 감사합니다!"
    },
    {
      "id": 2,
      "postId": 1,
      "userId": 3,
      "parentCommentId": 1,
      "content": "저도 공감합니다."
    }
  ]
}
```

### 댓글 조회
특정 댓글을 조회합니다.

```http
GET /api/v1/comments/{id}
```

### 댓글 작성
새로운 댓글을 작성합니다.

```http
POST /api/v1/comments
```

**Request Body:**
```json
{
  "postId": 1,
  "userId": 2,
  "content": "댓글 내용입니다.",
  "parentCommentId": null  // 대댓글인 경우 부모 댓글 ID
}
```

### 댓글 수정
댓글 내용을 수정합니다.

```http
PUT /api/v1/comments/{id}
```

**Request Body:**
```json
{
  "content": "수정된 댓글 내용"
}
```

### 댓글 삭제
댓글을 삭제합니다.

```http
DELETE /api/v1/comments/{id}
```

---

## ❤️ 좋아요 API

### 좋아요 토글
좋아요를 추가하거나 취소합니다.

```http
POST /api/v1/likes/toggle
```

**Request Body:**
```json
{
  "postId": 1,
  "userId": 2
}
```

**Response (좋아요 추가):**
```json
{
  "success": true,
  "data": {
    "message": "좋아요 추가",
    "data": {
      "postId": 1,
      "userId": 2
    }
  }
}
```

**Response (좋아요 취소):**
```json
{
  "success": true,
  "data": {
    "message": "좋아요 취소",
    "data": null
  }
}
```

### 좋아요 수 조회
특정 포스트의 총 좋아요 개수를 조회합니다.

```http
GET /api/v1/likes/count/{postId}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "count": 42
  }
}
```

### 포스트의 좋아요 목록 조회
특정 포스트에 좋아요를 누른 사용자 목록을 조회합니다.

```http
GET /api/v1/likes/post/{postId}
```

**Response:**
```json
{
  "success": true,
  "data": [
    { "postId": 1, "userId": 2 },
    { "postId": 1, "userId": 3 },
    { "postId": 1, "userId": 5 }
  ]
}
```

### 사용자가 좋아요한 포스트 목록
특정 사용자가 좋아요를 누른 모든 포스트를 조회합니다.

```http
GET /api/v1/likes/user/{userId}
```

**Response:**
```json
{
  "success": true,
  "data": [
    { "postId": 1, "userId": 2 },
    { "postId": 5, "userId": 2 },
    { "postId": 10, "userId": 2 }
  ]
}
```

### 좋아요 정보 조회
포스트의 좋아요 수와 특정 사용자의 좋아요 여부를 조회합니다.

```http
GET /api/v1/likes/info?postId={postId}&userId={userId}
```

**Example:**
```http
GET /api/v1/likes/info?postId=1&userId=2
```

**Response:**
```json
{
  "success": true,
  "data": {
    "likeCount": 42,
    "isLiked": true
  }
}
```

---

## 📎 미디어 API

### 미디어 파일 업로드
이미지 또는 파일을 업로드합니다.

```http
POST /api/v1/media/upload
Content-Type: multipart/form-data
```

**Request (Form Data):**
- `file`: 업로드할 파일
- `postId`: 포스트 ID (선택사항)

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "fileName": "image.jpg",
    "fileType": "image/jpeg",
    "url": "/uploads/media/abc123-uuid.jpg",
    "postId": 1
  }
}
```

### 포스트의 미디어 목록 조회
특정 포스트에 연결된 모든 미디어를 조회합니다.

```http
GET /api/v1/media/post/{postId}
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "fileName": "image1.jpg",
      "fileType": "image/jpeg",
      "url": "/uploads/media/abc123-uuid.jpg",
      "postId": 1
    },
    {
      "id": 2,
      "fileName": "image2.png",
      "fileType": "image/png",
      "url": "/uploads/media/def456-uuid.png",
      "postId": 1
    }
  ]
}
```

### 미디어 조회
특정 미디어 정보를 조회합니다.

```http
GET /api/v1/media/{id}
```

### 미디어 삭제
미디어 파일을 삭제합니다.

```http
DELETE /api/v1/media/{id}
```

**Response:**
```json
{
  "success": true,
  "data": "deleted"
}
```

---

## 공통 응답 형식

### 성공 응답
```json
{
  "success": true,
  "data": { ... },
  "error": null
}
```

### 에러 응답
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

---

## 에러 코드

| 코드 | 상태 코드 | 설명 |
|-----|---------|------|
| `USER_NOT_FOUND` | 404 | 사용자를 찾을 수 없음 |
| `POST_NOT_FOUND` | 404 | 포스트를 찾을 수 없음 |
| `CATEGORY_NOT_FOUND` | 404 | 카테고리를 찾을 수 없음 |
| `TAG_NOT_FOUND` | 404 | 태그를 찾을 수 없음 |
| `COMMENT_NOT_FOUND` | 404 | 댓글을 찾을 수 없음 |
| `MEDIA_NOT_FOUND` | 404 | 미디어를 찾을 수 없음 |
| `DUPLICATE_EMAIL` | 400 | 이미 사용 중인 이메일 |
| `DUPLICATE_NICKNAME` | 400 | 이미 사용 중인 닉네임 |
| `INVALID_PASSWORD` | 400 | 잘못된 비밀번호 |
| `INVALID_TOKEN` | 401 | 유효하지 않은 토큰 |
| `EXPIRED_TOKEN` | 401 | 만료된 토큰 |
| `UNAUTHORIZED` | 401 | 인증 필요 |
| `FORBIDDEN` | 403 | 권한 없음 |
| `INTERNAL_SERVER_ERROR` | 500 | 서버 내부 오류 |

---

## 참고사항

### 페이지네이션
현재 페이지네이션은 구현되어 있지 않습니다. 향후 추가될 예정입니다.

**예정된 형식:**
```http
GET /api/v1/post/list?page=0&size=10&sort=createdAt,desc
```

### 파일 업로드 제한
- 최대 파일 크기: 10MB (추후 설정 가능)
- 지원 파일 형식: 이미지 (jpg, png, gif), 문서 (pdf, txt)

### Rate Limiting
현재 Rate Limiting은 적용되어 있지 않습니다. 프로덕션 환경에서는 추가 필요합니다.

---

**문서 버전: 1.0**
**최종 수정일: 2025-11-02**
