package com.github.b1uec0in.josaformatter;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    static public void assertEqualsEx(Object expected, Object actual) {

        System.out.println(expected);
        assertEquals(expected, actual);
    }
    @Test
    public void sample1() throws Exception {
        System.out.println("\nsample1:");

        assertEqualsEx("OS10은 이미 사용중입니다.", KoreanUtils.format("%s은 이미 사용중입니다.", "OS10"));

        // HangulJongSungDetector
        assertEqualsEx("삼을", KoreanUtils.format("%s을", "삼"));
        assertEquals("삼을", KoreanUtils.format("%s를", "삼"));
        assertEqualsEx("사를", KoreanUtils.format("%s을", "사"));
        assertEquals("사를", KoreanUtils.format("%s를", "사"));

        // EnglishCapitalJongSungDetector
        assertEqualsEx("FBI는 이미 사용중입니다.", KoreanUtils.format("%s는 이미 사용중입니다.", "FBI"));
        assertEquals("FBI는 이미 사용중입니다.", KoreanUtils.format("%s은 이미 사용중입니다.", "FBI"));

        assertEqualsEx("IBM은 이미 사용중입니다.", KoreanUtils.format("%s는 이미 사용중입니다.", "IBM"));
        assertEquals("IBM은 이미 사용중입니다.", KoreanUtils.format("%s은 이미 사용중입니다.", "IBM"));

        // EnglishJongSungDetector
        assertEqualsEx("gradle은", KoreanUtils.format("%s는", "gradle"));
        assertEqualsEx("glide는", KoreanUtils.format("%s는", "glide"));
        assertEqualsEx("first는", KoreanUtils.format("%s는", "first"));
        assertEqualsEx("unit은", KoreanUtils.format("%s는", "unit"));
        assertEqualsEx("p는", KoreanUtils.format("%s는", "p"));
        assertEqualsEx("app은", KoreanUtils.format("%s는", "app"));
        assertEqualsEx("method는", KoreanUtils.format("%s는", "method"));


        // EnglishNumberKorStyleJongSungDetector
        assertEqualsEx("MP3는 이미 사용중입니다.", KoreanUtils.format("%s는 이미 사용중입니다.", "MP3"));
        assertEquals("MP3는 이미 사용중입니다.", KoreanUtils.format("%s은 이미 사용중입니다.", "MP3"));

        assertEqualsEx("OS10은 이미 사용중입니다.", KoreanUtils.format("%s은 이미 사용중입니다.", "OS10"));
        assertEqualsEx("Office2000은 이미 사용중입니다.", KoreanUtils.format("%s은 이미 사용중입니다.", "Office2000"));
        assertEqualsEx("Office2010은 이미 사용중입니다.", KoreanUtils.format("%s는 이미 사용중입니다.", "Office2010"));
        assertEqualsEx("WD-40은 이미 사용중입니다.", KoreanUtils.format("%s는 이미 사용중입니다.", "WD-40"));

        assertEqualsEx("iOS8.3은 이미 사용중입니다.", KoreanUtils.format("%s는 이미 사용중입니다.", "iOS8.3"));
        assertEqualsEx("GS25로 목적지를 설정하시겠습니까?", KoreanUtils.format("%s으로 목적지를 설정하시겠습니까?", "GS25"));

        // NumberJongSungDetector
        assertEqualsEx("3과 4를 비교", KoreanUtils.format("%s와 %s를 비교", 3, 4));
        assertEqualsEx("112와 4.0을 비교", KoreanUtils.format("%s와 %s를 비교", 112, 4.0));


        // HanjaJongSungDetector
        assertEqualsEx("6月은", KoreanUtils.format("%s는", "6月"));
        assertEqualsEx("大韓民國은", KoreanUtils.format("%s는", "大韓民國"));


        // getReadText, skipEndText
        assertEqualsEx("(폰)을", KoreanUtils.format("%s를", "(폰)"));
        assertEquals("(폰)을", KoreanUtils.format("(%s)를", "폰"));

        assertEqualsEx("갤럭시를 아이폰으로", KoreanUtils.format("%1$s을 %2$s으로", "갤럭시", "아이폰"));
        assertEqualsEx("iPhone을 Galaxy로", KoreanUtils.format("%2$s을 %1$s으로", "Galaxy", "iPhone"));
        assertEqualsEx("아이폰을 Galaxy로 변경할까요?", KoreanUtils.format("%s를 %s으로 변경할까요?", "아이폰", "Galaxy"));

        // 판단 불가
        assertEqualsEx("???을(를) 찾을 수 없습니다.", KoreanUtils.format("%s를 찾을 수 없습니다.", "???"));

        // 기타
        assertEqualsEx("SK에서는 幸福과 覇氣를 기억하세요.", KoreanUtils.format("%s에서는 %s와 %s를 기억하세요.", "SK", "幸福", "覇氣"));

        // 외국어 처리 : 외국어 뒤 숫자를 영어로 읽는 경우.
        assertEqualsEx("아이폰3를 갤럭시6로", KoreanUtils.format("%2$s을 %1$s으로", "갤럭시6", "아이폰3"));

        // 사용자 규칙 추가
        KoreanUtils.getDefaultJosaFormatter().addReadRule("베타", "beta");
        assertEqualsEx("베타3를", KoreanUtils.format("%s을", "베타3"));

    }

    @Test
    public void testEnglishNumberJongSungDetector() throws Exception {
        // 영문+숫자인 경우 항상 숫자를 영어로 읽도록 함.
        System.out.println("\nEnglishNumberJongSungDetector:");

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


        assertEqualsEx("MP3는 이미 사용중입니다.", josaFormatter.format("%s는 이미 사용중입니다.", "MP3"));
        assertEquals("MP3는 이미 사용중입니다.", josaFormatter.format("%s은 이미 사용중입니다.", "MP3"));

        assertEqualsEx("OS10은 이미 사용중입니다.", josaFormatter.format("%s은 이미 사용중입니다.", "OS10"));
        assertEqualsEx("Office2000는 이미 사용중입니다.", josaFormatter.format("%s은 이미 사용중입니다.", "Office2000"));
        assertEqualsEx("Office2010은 이미 사용중입니다.", josaFormatter.format("%s는 이미 사용중입니다.", "Office2010"));
        assertEqualsEx("WD-40는 이미 사용중입니다.", josaFormatter.format("%s는 이미 사용중입니다.", "WD-40"));

        assertEqualsEx("iOS8.3는 이미 사용중입니다.", josaFormatter.format("%s는 이미 사용중입니다.", "iOS8.3"));

    }

    @Test
    public void testEnglishJongSungDetector() throws Exception {

        // 받침 있는 경우
        String[] jongSungSample = {
                "apple",
                "god",
                "game",
                "gone",
        };

        // 받침 없는 경우
        String[] notJongSungSample = {
                "risk",
                "tank",
                "text",
                "wood",
        };


        for (String str: jongSungSample) {
            assertEquals(str + "은", KoreanUtils.format("%s는", str));
        }
        for (String str: notJongSungSample) {
            assertEquals(str + "는", KoreanUtils.format("%s은", str));
        }
    }
}