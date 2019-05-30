package alancerpro.util;

public class StringFormatter {
    public static String capitalizeWords (String string) {
        char[] chars = string.toCharArray();
        boolean toUpperNext = true;
        for(int i=0; i<chars.length; ++i) {
            if(Character.isWhitespace(chars[i])) {
                toUpperNext = true;
                continue;
            }
            if(toUpperNext) {
                chars[i] = Character.toUpperCase(chars[i]);
                toUpperNext = false;
            } else {
                chars[i] = Character.toLowerCase(chars[i]);
            }
        }
        return new String(chars);
    }

    public static String capitalizeWord(String string) {
        char[] chars = string.toCharArray();
        for(int i = 0; i<chars.length; ++i) {
            if(i==0) {
                chars[i] = Character.toUpperCase(chars[i]);
            } else {
                chars[i] = Character.toLowerCase(chars[i]);
            }
        }
        return new String(chars);
    }

    public static String capitalizedThreeLetters(String string) {
        char[] chars = string.toCharArray();
        char[] charsResult = new char[3];
        for(int i = 0; i<3; ++i) {
            if(i==0) {
                charsResult[i] = Character.toUpperCase(chars[i]);
            } else {
                charsResult[i] = Character.toLowerCase(chars[i]);
            }
        }
        return new String(charsResult);
    }

    public static String convertVariableToString(String str) {
        String[] str1 = str.split("(?=\\p{Upper})");
        StringBuilder sb = new StringBuilder();
        for (String s: str1) {
            sb.append(StringFormatter.capitalizeWords(s));
            sb.append(" ");
        }
        sb.deleteCharAt(sb.lastIndexOf(" "));
        return sb.toString();
    }
}

