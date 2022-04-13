## 프로젝트 내용

---
<br>

## Introduction

Firebase cloud system을 이용한 Chat Application입니다.

Firebase Firestore, Firebase Autentication, FIrebase Storage, Firebase Realtime database 등 다양한 기능이 사용했습니다.

로그인, 회원가입, 프로필 설정, 채팅 등 기능을 구현했습니다.

---
<br>

## 주요 기능

### 1. 로그인/회원가입

Firebase의 Authentication 기능을 이용한 Email & Password Login 기능 구현

Firebase의 Firestore database 기능을 이용해 그 외 개인정보들 저장

### 2. Profile Image 설정

User의 Permission을 받아 내부 앨범 이미지에 접근하여 Firestore Storage에 저장

이후 해당 Profile Image Url이 요청될 때 Firebase Storage로 가서 다운로드하여 Profile Image 표현

### 3. Chat with realtime

Firebase의 realtime database를 이용하여 실시간 채팅 구현

실제 내가 입력한 채팅 내용이 실시간으로 상대방에게 보여지고 갱신되어짐.

해당 채팅방의 database reference를 가져와 data 변동이 있으면 이를 바로 알리고 ListView Adapter를 통해 내용 갱신

---
<br>

## 앱 화면
<img src="https://user-images.githubusercontent.com/50730897/163184795-c58261c6-abd4-4eff-a856-6d95befd634d.jpg" width="300" height="500" align="left">
<img src="https://user-images.githubusercontent.com/50730897/163184801-38daad1f-9df7-4b45-a80b-2122d010f101.jpg" width="300" height="500" align="left">
<img src="https://user-images.githubusercontent.com/50730897/163184800-4053ee23-5510-464e-aa68-c11449639ce5.jpg" width="300" height="500" align="left">
<img src="https://user-images.githubusercontent.com/50730897/163184799-77755620-5f9c-4144-b54b-f06290ff6519.jpg" width="300" height="500" align="left">
<img src="https://user-images.githubusercontent.com/50730897/163184790-08034c99-88bf-40d9-9675-ee3160e5faf4.jpg" width="300" height="500" align="left">
<img src="https://user-images.githubusercontent.com/50730897/163184787-89156a54-8b76-435f-a0a7-f95e4533ed36.jpg" width="300" height="500" align="left">




