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
        assertEqualsEx("6月은 ", KoreanUtils.format("%s은 ", "6月"));


        System.out.println("\nsample1:");

        assertEqualsEx("OS10은 이미 사용중입니다.", KoreanUtils.format("%s은 이미 사용중입니다.", "OS10"));

        // HangulJongSungDetector
        assertEqualsEx("삼을 ", KoreanUtils.format("%s을 ", "삼"));
        assertEquals("삼을 ", KoreanUtils.format("%s를 ", "삼"));
        assertEqualsEx("사를 ", KoreanUtils.format("%s을 ", "사"));
        assertEquals("사를 ", KoreanUtils.format("%s를 ", "사"));

        // EnglishCapitalJongSungDetector
        assertEqualsEx("FBI는 이미 사용중입니다.", KoreanUtils.format("%s는 이미 사용중입니다.", "FBI"));
        assertEquals("FBI는 이미 사용중입니다.", KoreanUtils.format("%s은 이미 사용중입니다.", "FBI"));

        assertEqualsEx("IBM은 이미 사용중입니다.", KoreanUtils.format("%s는 이미 사용중입니다.", "IBM"));
        assertEquals("IBM은 이미 사용중입니다.", KoreanUtils.format("%s은 이미 사용중입니다.", "IBM"));

        // EnglishJongSungDetector
        assertEqualsEx("gradle은 ", KoreanUtils.format("%s는 ", "gradle"));
        assertEqualsEx("glide는 ", KoreanUtils.format("%s는 ", "glide"));
        assertEqualsEx("first는 ", KoreanUtils.format("%s는 ", "first"));
        assertEqualsEx("unit은 ", KoreanUtils.format("%s는 ", "unit"));
        assertEqualsEx("p는 ", KoreanUtils.format("%s는 ", "p"));
        assertEqualsEx("app은 ", KoreanUtils.format("%s는 ", "app"));


        // EnglishNumberKorStyleJongSungDetector
        assertEqualsEx("MP3는 이미 사용중입니다.", KoreanUtils.format("%s는 이미 사용중입니다.", "MP3"));
        assertEquals("MP3는 이미 사용중입니다.", KoreanUtils.format("%s은 이미 사용중입니다.", "MP3"));

        assertEqualsEx("OS10은 이미 사용중입니다.", KoreanUtils.format("%s은 이미 사용중입니다.", "OS10"));
        assertEqualsEx("Office2000은 이미 사용중입니다.", KoreanUtils.format("%s은 이미 사용중입니다.", "Office2000"));
        assertEqualsEx("Office2010은 이미 사용중입니다.", KoreanUtils.format("%s는 이미 사용중입니다.", "Office2010"));
        assertEqualsEx("WD40은 이미 사용중입니다.", KoreanUtils.format("%s는 이미 사용중입니다.", "WD40"));

        assertEqualsEx("iOS8.3은 이미 사용중입니다.", KoreanUtils.format("%s는 이미 사용중입니다.", "iOS8.3"));

        // NumberJongSungDetector
        assertEqualsEx("3과 4를 비교", KoreanUtils.format("%s와 %s를 비교", 3, 4));
        assertEqualsEx("112와 4.0을 비교", KoreanUtils.format("%s와 %s를 비교", 112, 4.0));


        // HanjaJongSungDetector
        assertEqualsEx("6月은 ", KoreanUtils.format("%s는 ", "6月"));
        assertEqualsEx("大韓民國은 ", KoreanUtils.format("%s는 ", "大韓民國"));


        // getReadText, skipEndText
        assertEqualsEx("(폰)을 ", KoreanUtils.format("%s를 ", "(폰)"));
        assertEquals("(폰)을 ", KoreanUtils.format("(%s)를 ", "폰"));

        assertEquals(4, 2 + 2);
        String str;
        str = KoreanUtils.format("%2$s을 %1$s으로 변경하시겠습니까?", "아이폰", "갤럭시");
        System.out.println(str);
        str = KoreanUtils.format("%2$s을 %1$s으로 변경하시겠습니까?", "갤럭시7", "아이폰8");
        System.out.println(str);

        assertEqualsEx("SK에서는 幸福과 覇氣를 기억하세요.", KoreanUtils.format("%s에서는 %s와 %s를 기억하세요.", "SK", "幸福", "覇氣"));

    }

    @Test
    public void testEnglishNumberJongSungDetector() throws Exception {
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
        assertEqualsEx("WD40는 이미 사용중입니다.", josaFormatter.format("%s는 이미 사용중입니다.", "WD40"));

        assertEqualsEx("iOS8.3는 이미 사용중입니다.", josaFormatter.format("%s는 이미 사용중입니다.", "iOS8.3"));

    }
}