# StrongLog

### 프로젝트 소개
#### 개요 
- 정서 웰빙 : 감정을 건강하게 조절하고, 긍정적인 사고를 유지하며, 스트레스를 효과적으로 다루는 능력
- 바쁜 일상에 치여 마음 건강을 잘 돌보지 못하는 현대인들의 정서 웰빙을 쉽게 높이는 방법이 없을까 ?

#### 목적
- 자신의 감정을 기록하고, 타인과 좋은 말들을 공유하며 스스로를 단단하게 만들 수 있는 정서 웰빙 블로그 제작

#### 기대효과
- 비공개 게시글로 프라이빗한 글쓰기 가능
- 공개 게시글로 타인과 감정, 경험, 좋은 말 공유
- 댓글과 좋아요 기능으로 소통과 공감대 형성
- 정서 웰빙 향상 효과 : 정서적 안정, 긍정적 사고, 스트레스 관리, 회복탄력성 향상

### 기술스택
- Language : JAVA
- Framework : SpringBoot3, Spring Web, Spring Data JPA, Spring Security, Spring Validation
- Template Engine : Thymeleaf, ChatGPT
- Database : MariaDB, JPA, JPQL
- Deployment / DevOps : AWS LightSail (Amazon Linux 2023), Ningx, HTTPS (Let’s Encrypt), Custom Domain 연결
- Security / Session : Spring Session, HTTPS (SSL 인증서)
- Code Test : Junit5
- Version Control : Git / GitHub
  
#### Project Architecture
<img width="1216" height="779" alt="image" src="https://github.com/user-attachments/assets/0a1d78f4-5929-43f1-a8a6-2ffc39fafbdc" />

### 주요기능
#### 로그인/로그아웃
- Spring Security
- 오류 메시지 표시
- 로그인 상태

#### 회원가입
- 아이디, 비밀번호, 닉네임 유효성 검사
- 아이디, 닉네임 중복 방지
- 오류 메시지 표시

#### -------------------------------------------
#### 메인 페이지
#### 게시글 목록 조회
- 인기 게시글 TOP 10 (좋아요 많은 순) 조회
- 카테고리별 게시글 TOP 10 (좋아요 많은 순) 조회
- Authorization⭕ : 공개 게시글만 조회 
- Authorization ❌ : 공개 게시글만 조회

#### 페이지 이동
- 게시글 클릭 -> 게시글 상세조회
- 글쓴이 클릭 -> 개인 블로그 페이지

#### -------------------------------------------
#### 개인 블로그 페이지
#### 게시글 READ
- 목록 조회
   - Paging : 한 페이지에 게시글 10개씩 조회
   - Authorization⭕ : 글쓴이에게만 공개/비공개 여부 노출
   - Authorization⭕ : 글쓴이만 비공개 게시글 포함 조회
   - Authorization ❌ : 공개 게시글만 조회
   - 카테고리별 게시글 조회
- 상세 조회
   - Authorization⭕ : 글쓴이만 수정/삭제 접근 가능
   - Authentication ⭕ : 댓글 등록 가능
   - Authentication ⭕ : 좋아요 가능
   - Authentication ❌ : 댓글 등록 X, 좋아요 X

#### 게시글 CREATE
- Authorization⭕ : 블로그 주인만 글쓰기 가능
- Private 기능 : 게시글 작성 시 공개/비공개 설정

#### 게시글 UPDATE/DELEETE
- Authorization⭕ : 글쓴이만 게시글 수정/삭제 가능

#### 댓글 CRUD
- 게시글에 대한 댓글 목록 조회
- Authentication ⭕ : 댓글 등록 가능
- Authorization⭕ : 댓글 작성자만 댓글 수정/삭제 가능

#### -------------------------------------------

#### 좋아요 기능
- Authentication ⭕
- 게시글에 좋아요 on/off







