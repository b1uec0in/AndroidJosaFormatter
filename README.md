# Android Josa Formatter 1.2
String.format을 확장해 받침에 따라 조사(은,는,이,가,을,를 등)를 교정합니다.

***아직 읽는 방법에 대한 규칙을 찾지 못한 부분이 많습니다. 오동작이 발견되거나 좀 더 나은 규칙이 있다면 꼭!! 알려주세요.***


### Sample

```java
KoreanUtils.format("%s를 %s으로 변경할까요?", "아이폰", "Galaxy");

아이폰을 Galaxy로 변경할까요?
```


### Setup
app/build.gradle
```diff
apply plugin: 'com.android.application'

+ repositories {
+    maven { url "https://jitpack.io" }
+ }

android {
...
}

dependencies {
...
+ compile 'com.github.b1uec0in:androidjosaformatter:1.1'
}
```

### Features
* 앞 글자의 종성(받침) 여부에 따라 조사(은,는,이,가,을,를 등)를 교정합니다.
* 한글 뿐만 아니라 영어, 숫자, 한자 등도 처리가 가능합니다.
* 조사 앞에 인용 부호나 괄호가 있어도 동작합니다.
```java
KoreanUtils.format("'%s'는 사용중인 닉네임 입니다.", nickName);
```
* Detector를 직접 등록하거나 우선 순위 등을 조정할 수 있습니다. (JongSungDetector 클래스 순서 참고)

### JongSungDetector 기본 우선 순위
* 한글 (HangulJongSungDetector)<br/>
: '홍길동'는 -> '홍길동'은
* 영문 대문자 약어 (EnglishCapitalJongSungDetector)<br/>
: 'IBM'가 -> 'IBM'이
* 일반 영문 (EnglishJongSungDetector)<br/>
: 'Google'를 -> 'Google'을
* 영문+숫자 (EnglishNumberJongSungDetector) - 기본 비활성<br/>
: 'BaskinRobbins31'는 -> 'BaskinRobbins31'은 
* 영문+10이하 숫자 (EnglishNumberKorStyleJongSungDetector)<br/>
: 'MP3'은 -> 'MP3'는
* 숫자 (NumberJongSungDetector)<br/>
: '1'와 '2'은  -> '1'과 '2'는
* 한자 (HanjaJongSungDetector)<br/>
: '6月'는 -> '6月'은

### 예외 처리
* '영문+숫자'인 경우 기본적으로 10 이하만 영어로 읽습니다. 'MP3'는 '엠피쓰리'로 읽지만, 'Window 2000'은 '윈도우 이천'으로 읽습니다.<br/>
항상 영어로 읽도록 하기 위해서는 직접 EnglishNumberJongSungDetector 를 등록해야 합니다.
```java
JosaFormatter josaFormatter = new JosaFormatter();
ArrayList<JosaFormatter.JongSungDetector> jongSungDetectors = josaFormatter.getJongSungDetectors();

// replace EnglishNumberKorStyleJongSungDetector with EnglishNumberJongSungDetector
for (int i = 0; i < jongSungDetectors.size(); ++i) {
    JosaFormatter.JongSungDetector jongSungDetector = jongSungDetectors.get(i);
    if (jongSungDetector instanceof JosaFormatter.EnglishNumberKorStyleJongSungDetector) {
        jongSungDetectors.set(i, new JosaFormatter.EnglishNumberJongSungDetector());
        break;
    }
}

String text = josaFormatter.format("%s을 구매하시겠습니까?", "Office 2000"));
// Office 2000를 구매하시겠습니까?

```

* '한글+숫자'인 경우 숫자를 영어로 읽기 위해서는 규칙을 직접 추가해야 합니다.
```java
KoreanUtils.getDefaultJosaFormatter().addReadRule("베타", "beta");
String text = KoreanUtils.format("%s을 구매하시겠습니까?", "베가 베타 3"));
// 베가 베타 3를 구매하시겠습니까?
```

### Reference
* 한글 받침에 따라 '을/를' 구분 <br/>
http://gun0912.tistory.com/65

* 한글, 영어 받침 처리 (iOS) <br/>
https://github.com/trilliwon/JNaturalKorean

* 한자를 한글로 변환 <br/>
http://devhome.tistory.com/18




