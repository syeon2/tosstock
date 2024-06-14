## 📊 Tosstock Project

> 여러 증권사의 종목 탐색 및 최대 5년간의 개장가, 종가, 저가, 고가 등 세부 정보 제공과 투자자 간 인사이트 공유가 가능한 커뮤니케이션 프로그램입니다.

---

### 🌱 사용된 기술

- for Server : Java 17, Spring Boot 3.2.5
- for Database : MySQL, Redis, JPA, QueryDSL
- for Cloud : AWS EC2, AWS RDS, AWS ElastiCache

---

### 🌱 Architecture

<a href="https://ibb.co/jhddSg4"><img src="https://i.ibb.co/H700jng/Tosstock-2.jpg" alt="Tosstock-2" border="0"></a>

---

### 🌱 ERD

<a href="https://ibb.co/qxBRQgx"><img src="https://i.ibb.co/M9nR019/tosstock-erd-v1.png" alt="tosstock-erd-v1" border="0"></a>

---

### 🌱 핵심 기능

- 회원가입 및 비밀번호 암호화(Bcrypt)
- Access Token & Refresh Token 개념을 활용한 인증/인가
- 각 종목 별 게시글 & 댓글 작성, 유저간 팔로우 기능
- 팔로우한 회원들의 소식 (뉴스피드) 조회 기능
- 5가지 코스피 증권 데이터 조회 및 각 5년간 거래 차트 데이터 조회 기능

---

### 🌱 문제 및 트러블슈팅

- [인증/인가와 지속적인 로그인 유지를 위해 JWT 토큰 개념 활용](https://medium.com/@gsy4568/4-tosstock-project-jwt-%EC%9D%B8%EC%A6%9D-%EC%9D%B8%EA%B0%80-7f82943b16e1)
