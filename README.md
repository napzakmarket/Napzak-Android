<img width="100" src="https://github.com/user-attachments/assets/ee232d34-10e4-407e-89f3-d3d6306a6aaf" />
<a href='https://play.google.com/store/apps/details?id=com.napzak.market'><img width="250" alt="play" src="https://github.com/user-attachments/assets/f2b9e3e6-8879-4b9c-8c11-b80e51235580"/></a>

## 👾 납작마켓 (NAPZAK) - 덕후를 위한 중고거래 앱 👾

<img src="https://github.com/user-attachments/assets/e4d3bb65-414f-4e6f-9345-7a10465e0f8d" />

> **납작마켓**은 2D 캐릭터부터 가상(Virtual) 장르의 아이템까지, **납작한 것들만**을 다루는 서브컬처 장르 전용 **중고거래 서비스**입니다.


<br>

## 👾 Contributors

| <img width="200" alt="이석준" src="https://github.com/user-attachments/assets/d2cd6176-b0ac-499c-ad59-434893dbb13d" /> | <img width="200" alt="김채린" src="https://github.com/user-attachments/assets/2e123f58-f407-46fb-9cba-8ce9978c688a" /> | <img width="200" alt="김종명" src="https://github.com/user-attachments/assets/d475398d-3056-447f-bcec-fd043572a70d" /> | <img width="200" alt="이연진" src="https://github.com/user-attachments/assets/bc0b2b8c-e4ac-428e-b786-aa57b028a7be" /> | <img width="200" alt="장재원" src="https://github.com/user-attachments/assets/5d7035ac-cf7d-43b5-a906-f2e1c4eeae35" /> |
|:-------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------:|
|                      [이석준](https://github.com/boiledeggg)                      |                        [김채린](https://github.com/chrin05)                        |                       [김종명](https://github.com/jm991014)                        |                        [이연진](https://github.com/yeonjeen)                        |                       [장재원](https://github.com/jangsjw)                        |
| `홈`, `상세보기`, `탈퇴` <br> `프로필 편집`, `신고` | `탐색`, `장르상세` <br> `내마켓` | `등록` <br> `물품 정보 수정` | `로그인` <br> `온보딩` | `마이페이지` <br> `설정` |


<br>

## 🌟주요 기능

### 1️⃣ 덕후 취향 반영 온보딩
- 관심 장르를 직접 설정하고 취향에 딱 맞는 아이템들을 한눈에 확인해요.
- 개인 맞춤 상품 추천으로 취향 저격 아이템을 발견해보세요.

### 2️⃣ 500여개 장르로 세분화된 상세 탐색
- 불편한 검색은 이제 그만! 애니메이션, 게임 등 세분화된 장르로 더욱 편리하고 정확하게 상품을 찾아봐요.
- 검색, 필터 기능을 통해 원하는 장르 및 아이템을 쉽고 정확하게 탐색해보세요.

### 3️⃣ 원하는 상품은 '구해요'에서 쉽게 찾고, 팔고 싶은 굿즈는 '팔아요'에서 빠르게 거래해요
- ‘팔아요’와 ‘구해요’ 카테고리를 통해 거래 목적에 맞게, 보다 편리하고 확실하게 거래해보세요.

### 4️⃣ **덕후들의 거래 방식에 딱 맞는 등록 시스템!**
- 원하는 아이템을 구하기 위해 **매일 검색하거나 찾아다니지 않아도 돼요!**
- **장르 설정**부터 **상품 상태 설정**까지, 빠르고 간편한 거래가 가능해요.
- **가격 제시 버튼**과 **원하는 가격대 설정 기능**으로 위시템을 구할 수 있어요.

### 5️⃣ 나만의 덕질 마켓
- 자신만의 독특한 스타일로 마켓의 개성을 드러낼 수 있어요.
- 관심 장르, 소개글, 프로필 이미지로 직접 커스텀하여 나만의 마켓을 꾸며보세요.

<br>

## 🛠️ Tech Stack

![Kotlin](https://img.shields.io/badge/Kotlin-%2B%20JVM-7F52FF?style=for-the-badge&logo=kotlin)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-0F7F78?style=for-the-badge&logo=android)
![Hilt](https://img.shields.io/badge/Hilt-2A4D6E?style=for-the-badge&logo=android)
![Coroutine](https://img.shields.io/badge/Coroutines-3D8AD3?style=for-the-badge&logo=kotlin)

| 항목                    | 내용                                                         |
|-------------------------|--------------------------------------------------------------|
| **Architecture**         | **Clean Architecture**: 각 계층을 분리하여 유지보수성과 확장성을 확보. |
| **Pattern**              | **MVVM**: UI와 비즈니스 로직 분리를 통해 효율적인 상태 관리 및 코드 유지보수 용이. |
| **UI Framework**         | **Jetpack Compose**: 직관적이고 선언적인 UI 작성 가능, 코드 재사용성 증가. |
| **Dependency Injection** | **Hilt**: 의존성 관리 간소화, 코드의 모듈화와 테스트 용이성 증대. |
| **Network**              | **Retrofit2, OkHttp**: 네트워크 요청 처리, 타입 안정성 보장. |
| **Local Storage**        | **Room**: SQLite에 기반한 원활한 데이터 관리 지원 | 
| **Navigation**           | **Jetpack Navigation**: 화면 간 이동 및 데이터 전달을 안전하고 효율적으로 처리. |
| **Image Processing**     | **Coil, Lottie**: 이미지 로딩 및 애니메이션 처리 효율적. |
| **Asynchronous**         | **Coroutine, Flow**: 비동기 처리 및 동시성 프로그래밍 지원. |
| **Test**                 | **JUnit, Mockito, Roboelectric**: 단위 테스트로 로직 안정성 확보 |
| **OAuth**                | **Kakao Login SDK**: 카카오 계정을 활용한 로그인 지원|
| **Firebase**             | **FCM, Remote-Config**: 앱 알림과 최소 버전 관리 |


<br>

## :dependabot: Dependency Graph

<img width="5472" height="890" alt="project dot" src="https://github.com/user-attachments/assets/12667d2d-a227-48fb-979a-1afc83cbd3f5" />

<br><br>


## 📄 Conventions

**💻 [Github Convention](https://www.notion.so/Github-Convention-43d85e549715430383c9cafc6211edc8?pvs=4)**

**🧔 [Naming Convention](https://www.notion.so/Naming-Convention-8fa2e29e82434fd38dc3ee218d72818c?pvs=4)**

**📦 [Package Convention](https://www.notion.so/Packaging-Convention-6a4dea5a32e94fa9b14c307b5bcb7c2c?pvs=4)**

**📁 [Project Setting](https://www.notion.so/Project-Setting-56ed8782613046508f680ab89873cabd?pvs=4)**

<br>
