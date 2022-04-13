## 프로젝트 내용

---

## Introduction

Firebase cloud system을 이용한 Chat Application입니다.

Firebase Firestore, Firebase Autentication, FIrebase Storage, Firebase Realtime database 등 다양한 기능이 사용했습니다.

로그인, 회원가입, 프로필 설정, 채팅 등 기능을 구현했습니다.

---

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

## 앱 화면

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/36ae14f9-265a-4bae-8f78-c014ab56a906/Untitled.jpeg)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d9d8a58f-8368-4e24-abbc-303aac623777/Untitled.jpeg)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/a71639f1-6196-4fa7-9871-138f89ec753c/Untitled.jpeg)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/892acf4d-370d-4fcf-af34-f48031a98029/Untitled.jpeg)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/07948828-82d6-4cf2-b196-184f3c6329e6/Untitled.jpeg)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ad7ecad1-0f09-4d89-b5c0-dc29fd1a0da3/Untitled.jpeg)

