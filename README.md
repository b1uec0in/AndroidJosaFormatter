# Josa Formatter Android Sample
받침에 따라 조사(은,는,이,가,을,를 등)를 교정할 수 있는 String.format과 유사한 함수를 제공합니다.

이 프로젝트는 안드로이드 샘플 프로젝트입니다.

실제 라이브러리 프로젝트:
https://github.com/b1uec0in/JosaFormatter


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
+ compile 'com.github.b1uec0in:JosaFormatter:1.4'
}
```

