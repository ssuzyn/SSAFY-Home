# ZIP - 부동산 정보 플랫폼

## 📌 프로젝트 소개

ZIP은 부동산 초보자도 쉽게 접근할 수 있는 부동산 정보 플랫폼입니다. 실거래가 정보, 관심 매물 관리, 부동산 뉴스, 그리고 GPT 기반 부동산 용어 설명 기능을 제공합니다.

- **개발 기간**: 2023.11.19 - 2023.11.26
- **팀원**: 2인 개발 (페어 프로그래밍)

![image](https://github.com/user-attachments/assets/cba6e127-42ff-4c8a-9599-990e1083ece3)

## 💡 주요 기능

### 1. 지도 기반 부동산 정보 제공
- 실거래가 정보 시각화
- 클러스터링을 통한 대용량 데이터 처리 (약 640만건)
- 2초 이내 마커 렌더링 최적화

### 2. 사용자 맞춤 서비스
- 관심 매물 관리
- 검색 필터링
- JWT 기반 사용자 인증

### 3. 부동산 정보 제공
- 실시간 부동산 뉴스 크롤링
- GPT API 기반 부동산 개념 도우미
- 게시판 기능

## 🏗 시스템 아키텍처

![image](https://github.com/user-attachments/assets/eb96ae2c-a783-4543-88d1-cc8c5a789f1c)

## 📊 ERD

![image](https://github.com/user-attachments/assets/3bd65433-0b8d-4758-aae3-aad47561eefe)

## 🔍 성능 최적화

### 데이터베이스 최적화
- 최신 거래 데이터 분리 저장
- 지도 마커용 별도 테이블 구축
- 조회 성능 향상을 위한 인덱싱

### 프론트엔드 최적화
- 지도 마커 클러스터링 적용
- 사이드바와 모달 활용으로 UX 개선
- 투명 검색창으로 지도 가시성 향상

## 📱 화면 구성

### 로그인/회원가입

![image](https://github.com/user-attachments/assets/4f01d6df-2555-4f99-b1fa-dc7715531777)

![image](https://github.com/user-attachments/assets/fd976c28-895b-450d-91ba-8e024051e0d6)


### 메인 화면
- 실거래가 정보와 관심 매물 통합 조회
- 직관적인 UI/UX 제공
- 사용자 관심매물 조회, 삭제 가능

![image](https://github.com/user-attachments/assets/1802403f-6ad7-49ee-9422-27e508618234)
![image](https://github.com/user-attachments/assets/a6c80f75-a232-448f-b45b-34bbd94a06fc)
![image](https://github.com/user-attachments/assets/d013b123-6063-4fa1-baad-d760d4556a5b)


### 부동산 개념 도우미
- GPT API 활용 부동산 용어 설명
- 초보자를 위한 쉬운 설명 제공
![image](https://github.com/user-attachments/assets/e4a1adf3-262f-446c-9f82-c2132f9621cf)

### 부동산 뉴스

![image](https://github.com/user-attachments/assets/378fd24f-ed4d-451e-bf84-278f650d269a)


### 부동산 Q&A 게시판

![image](https://github.com/user-attachments/assets/f86dda24-3e66-479e-b908-b12fddb78112)
