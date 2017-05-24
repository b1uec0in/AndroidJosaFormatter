package com.github.b1uec0in.josaformatter;

import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


/**
 * Created by yjbae@sk.com on 2017/05/24.
 */

public class JosaFormatter {
    // 조사들을 종성이 있을 때와 없을 때 순서로 나열.
    private static List<Pair<String, String>> josaPairs = Arrays.asList(
            new Pair<>("은 ", "는 "),
            new Pair<>("이 ", "가 "),
            new Pair<>("을 ", "를 "),
            new Pair<>("과 ", "와 "),
            new Pair<>("으로 ", "로 ")
    );

    private ArrayList<JongSungDetector> jongSungDetectors = new ArrayList<>(Arrays.asList(
            new HangulJongSungDetector(),
            new EnglishCapitalJongSungDetector(),
            new EnglishJongSungDetector(),
            //new EnglishNumberJongSungDetector(),
            new EnglishNumberKorStyleJongSungDetector(),
            new NumberJongSungDetector(),
            new HanjaJongSungDetector()
    ));

    public ArrayList<JongSungDetector> getJongSungDetectors() {
        return jongSungDetectors;
    }

    public void setJongSungDetectors(ArrayList<JongSungDetector> jongSungDetectors) {
        this.jongSungDetectors = jongSungDetectors;
    }

    public String format(String format, Object... args) {
        return format(Locale.getDefault(), format, args);
    }

    public String format(Locale l, String format, Object... args) {
        ArrayList<Formatter.FormattedString> formattedStrings = Formatter.format(l, format, args);

        int count = formattedStrings.size();
        StringBuilder sb = new StringBuilder(formattedStrings.get(0).toString());

        if (count == 1) {
            return sb.toString();
        }

        for (int i = 1; i < formattedStrings.size(); ++i) {
            Formatter.FormattedString formattedString = formattedStrings.get(i);

            String str;

            if (formattedString.isFixedString()) {
                str = getJosaModifiedString(formattedStrings.get(i - 1).toString(), formattedString.toString());
            } else {
                str = formattedString.toString();
            }

            sb.append(str);
        }

        return sb.toString();
    }

    public String getJosaModifiedString(String previous, String str) {

        if (previous == null || previous.length() == 0) {
            return str;
        }

        Pair<String, String> matchedJosaPair = null;
        int josaIndex = -1;

        String searchStr = null;
        for (Pair<String, String> josaPair : josaPairs) {
            int firstIndex = str.indexOf(josaPair.first);
            int secondIndex = str.indexOf(josaPair.second);

            if (firstIndex >= 0 && secondIndex >= 0) {
                if (firstIndex < secondIndex) {
                    josaIndex = firstIndex;
                    searchStr = josaPair.first;
                } else {
                    josaIndex = secondIndex;
                    searchStr = josaPair.second;
                }
            } else if (firstIndex >= 0) {
                josaIndex = firstIndex;
                searchStr = josaPair.first;
            } else if (secondIndex >= 0) {
                josaIndex = secondIndex;
                searchStr = josaPair.second;
            }

            if (josaIndex >= 0 && isEndSkipText(str, 0, josaIndex)) {
                matchedJosaPair = josaPair;
                break;
            }
        }

        if (matchedJosaPair != null) {

            String readText = getReadText(previous);

            ArrayList<JongSungDetector> jongSungDetectors = getJongSungDetectors();
            for (JongSungDetector jongSungDetector : jongSungDetectors) {
                if (jongSungDetector.canHandle(readText)) {
                    return replaceStringByJongSung(str, matchedJosaPair, jongSungDetector.hasJongSung(readText));
                }
            }

            // 없으면 괄호 표현식을 사용한다. ex) "???을(를) 찾을 수 없습니다."

            String replaceStr = matchedJosaPair.first.trim() + "(" + matchedJosaPair.second.trim() + ") ";
            return str.substring(0, josaIndex) + replaceStr + str.substring(josaIndex + searchStr.length());
        }

        return str;
    }

    public String replaceStringByJongSung(String str, Pair<String, String> josaPair, boolean hasJongSung) {
        if (josaPair != null) {
            // 잘못된 것을 찾아야 하므로 반대로 찾는다. 종성이 있으면 종성이 없을 때 사용하는 조사가 사용 되었는지 찾는다.
            String searchStr = hasJongSung ? josaPair.second : josaPair.first;
            String replaceStr = hasJongSung ? josaPair.first : josaPair.second;
            int josaIndex = str.indexOf(searchStr);

            if (josaIndex >= 0 && isEndSkipText(str, 0, josaIndex)) {
                return str.substring(0, josaIndex) + replaceStr + str.substring(josaIndex + searchStr.length());
            }
        }

        return str;
    }

    public boolean isEndSkipText(String str, int begin, int end) {
        for (int i = begin; i < end; ++i) {
            if (!isEndSkipText(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // 조사 앞에 붙는 문자중 무시할 문자들. ex) "(%s)으로"
    public boolean isEndSkipText(char ch) {
        String skipChars = "\"')]}>";
        return skipChars.indexOf(ch) >= 0;
    }

    public String getReadText(String str) {
        int skipCount = 0;

        int i;
        for (i = str.length() - 1; i >= 0; --i) {
            char ch = str.charAt(i);

            if (!isEndSkipText(ch)) {
                break;
            }
        }

        return str.substring(0, i + 1);
    }

    interface JongSungDetector {
        boolean canHandle(String str);

        boolean hasJongSung(String str);
    }


    public static class HangulJongSungDetector implements JongSungDetector {

        @Override
        public boolean canHandle(String str) {
            return CharUtils.isHangulFullChar(CharUtils.lastChar(str));
        }

        @Override
        public boolean hasJongSung(String str) {
            return CharUtils.hasHangulJongSung(CharUtils.lastChar(str));
        }
    }

    public static class EnglishCapitalJongSungDetector implements JongSungDetector {

        @Override
        public boolean canHandle(String str) {
            char ch = CharUtils.lastChar(str);
            if (CharUtils.isAlphaUpperCase(ch)) {
                return true;
            }

            return false;
        }

        @Override
        public boolean hasJongSung(String str) {
            String jongSungChars = "LMNR";
            char lastChar = CharUtils.lastChar(str);
            return jongSungChars.indexOf(lastChar) >= 0;
        }
    }


    public static class EnglishJongSungDetector implements JongSungDetector {

        @Override
        public boolean canHandle(String str) {
            return CharUtils.isAlpha(CharUtils.lastChar(str));
        }

        @Override
        public boolean hasJongSung(String str) {
            char lastChar1 = CharUtils.lastChar(str);

            // 3자 이상인 경우만 마지막 2자만 postfix로 간주.
            String postfix = null;
            if (str.length() >= 3) {
                char lastChar2 = str.charAt(str.length() - 2);
                char lastChar3 = str.charAt(str.length() - 3);

                if (CharUtils.isAlpha(lastChar2) && CharUtils.isAlpha(lastChar3)) {
                    postfix = (String.valueOf(lastChar2) + String.valueOf(lastChar1)).toLowerCase();
                }
            }

            if (postfix != null) {
                String jongSungChars = "lmnp";
                if (jongSungChars.indexOf(lastChar1) >= 0) {
                    return true;
                }

                // 종성이 있는 postfix
                switch (postfix) {
                    case "le":
                    case "ne":
                    case "me":
                    case "ng":
                        return true;
                }

                // 종성이 없는 postfix
                switch (postfix) {
                    case "ed":
                    case "nd":
                    case "ld":
                    case "rd":
                    case "lt":
                    case "nt":
                    case "pt":
                    case "rt":
                    case "st":
                    case "xt":
                        return false;
                }

                String jongSungExtraChars = "bcdkpqt";
                if (jongSungExtraChars.indexOf(lastChar1) >= 0) {
                    return true;
                }
            } else {
                // 1자, 2자는 약자로 간주하고 알파벳 그대로 읽음. (엘엠엔알)만 종성이 있음.
                String jongSungChars = "lmnr";
                if (jongSungChars.indexOf(lastChar1) >= 0) {
                    return true;
                }
            }
            return false;
        }
    }

    // 영문+숫자를 미국식으로 읽기 ex) MP3, iPhone4, iOS8.3 (iOS eight point three), Office2003 (Office two thousand three)
    // 일반적으로 영문+숫자라도 11 이상은 그냥 한글로 읽는 경우가 많아서 적합하지 않을 수 있음.
    public static class EnglishNumberJongSungDetector implements JongSungDetector {
        public static ParseResult parse(String str) {
            ParseResult parseResult = new ParseResult();
            int i;
            boolean isSpaceFound = false;
            int numberPartBeiginIndex = 0;
            boolean isNumberCompleted = false;
            // 뒤에서부터 숫자, 영어 순서로 찾는다.
            for (i = str.length() - 1; i >= 0; --i) {
                char ch = str.charAt(i);

                if (!isNumberCompleted && !isSpaceFound && CharUtils.isNumber(ch)) {
                    parseResult.isNumberFound = true;
                    numberPartBeiginIndex = i;
                    continue;
                }

                if (ch == ',') {
                    continue;
                }

                if (!isNumberCompleted && parseResult.isNumberFound && !parseResult.isFloat && ch == '.') {
                    parseResult.isFloat = true;
                    continue;
                }

                // 공백은 숫자가 찾아진 이후 한번만 허용
                if (!isNumberCompleted && parseResult.isNumberFound && !isSpaceFound && ch == ' ') {
                    isSpaceFound = true;
                    isNumberCompleted = true;
                    continue;
                }

                // - 는 음수나 dash 용도로 사용될 수 있음.
                if (!isNumberCompleted && parseResult.isNumberFound && !isSpaceFound && ch == '-') {
                    isNumberCompleted = true;
                    continue;
                }

                // 영어는 숫자가 찾아진 이후에만 허용
                if (parseResult.isNumberFound && CharUtils.isAlpha(ch)) {
                    parseResult.isEnglishFound = true;
                    isNumberCompleted = true;
                    break;
                }

                break;
            }

            if (parseResult.isNumberFound) {
                parseResult.numberPart = str.substring(numberPartBeiginIndex);
                parseResult.prefixPart = str.substring(0, numberPartBeiginIndex);

                try {
                    parseResult.number = Double.parseDouble(parseResult.numberPart);
                } catch (Exception ignore) {
                }
            }

            return parseResult;
        }

        @Override
        public boolean canHandle(String str) {
            EnglishNumberJongSungDetector.ParseResult parseResult = EnglishNumberJongSungDetector.parse(str);

            return parseResult.isNumberFound && parseResult.isEnglishFound;

        }

        @Override
        public boolean hasJongSung(String str) {
            EnglishNumberJongSungDetector.ParseResult parseResult = EnglishNumberJongSungDetector.parse(str);

            if (!parseResult.isFloat) {
                long number = (long) (parseResult.number);

                if (number == 0) {
                    return false;
                }

                // 두자리 예외 처리
                int twoDigit = (int) (number % 100);
                switch (twoDigit) {
                    case 10:
                    case 13:
                    case 14:
                    case 16:
                        return true;
                }

                // million 이상 예외 처리
                // 100 : hundred (x)
                // 1000 : thousand (x)
                // 1000000... : million, billion, trillion (o)
                if (number % 100000 == 0) {
                    return true;
                }
            }

            // 마지막 한자리 (소수 포함)
            int oneDigit = CharUtils.lastChar(parseResult.numberPart) - '0';

            switch (oneDigit) {
                case 1:
                case 7:
                case 8:
                case 9:
                    return true;
            }

            return false;
        }

        public static class ParseResult {
            public boolean isNumberFound;

            // valid only if isNumberFound
            public boolean isEnglishFound;
            public double number;
            public boolean isFloat;
            public String prefixPart;
            public String numberPart;
        }
    }

    // 영문+숫자 10이하만 영어로 읽기 ex) MP3, iPhone4
    // 다른 경우에는 숫자를 한글로 읽기 위해서는 EnglishNumberJongSungDetector 와 같이 사용하면 안된다.
    public static class EnglishNumberKorStyleJongSungDetector implements JongSungDetector {

        @Override
        public boolean canHandle(String str) {
            EnglishNumberJongSungDetector.ParseResult parseResult = EnglishNumberJongSungDetector.parse(str);

            return parseResult.isNumberFound && parseResult.isEnglishFound && !parseResult.isFloat && parseResult.number <= 10;

        }

        @Override
        public boolean hasJongSung(String str) {
            EnglishNumberJongSungDetector.ParseResult parseResult = EnglishNumberJongSungDetector.parse(str);
            int number = (int) (parseResult.number);
            switch (number) {
                case 1:
                case 7:
                case 8:
                case 9:
                case 10:
                    return true;
            }

            return false;
        }
    }

    // 숫자를 한국식으로 읽기
    public static class NumberJongSungDetector implements JongSungDetector {
        @Override
        public boolean canHandle(String str) {
            EnglishNumberJongSungDetector.ParseResult parseResult = EnglishNumberJongSungDetector.parse(str);

            return parseResult.isNumberFound;

        }

        @Override
        public boolean hasJongSung(String str) {
            EnglishNumberJongSungDetector.ParseResult parseResult = EnglishNumberJongSungDetector.parse(str);

            if (!parseResult.isFloat) {
                long number = (long) (parseResult.number);
                // 조 예외 처리 : 조(받침 없음), 십, 백, 천, 만, 억, 경, 현
                if (number % 1000000000000L == 0) {
                    return false;
                }
            }

            // 마지막 한자리 (소수 포함)
            int oneDigit = CharUtils.lastChar(parseResult.numberPart) - '0';
            switch (oneDigit) {
                case 0:
                case 1:
                case 3:
                case 6:
                case 7:
                case 8:
                    return true;
            }

            return false;
        }
    }


    // 한자는 한글 코드로 변경해서 판단
    public static class HanjaJongSungDetector implements JongSungDetector {

        @Override
        public boolean canHandle(String str) {
            return HanjaMap.canHandle(CharUtils.lastChar(str));
        }

        @Override
        public boolean hasJongSung(String str) {
            char hangulChar = HanjaMap.toHangul(CharUtils.lastChar(str));
            return CharUtils.hasHangulJongSung(hangulChar);
        }
    }
}
