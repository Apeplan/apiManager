package com.xiaomingming.api.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/12/29.
 */
public class ProjectUtil {

    private static String[] strs = new String[]{
            "false", "class", "finally", "is", "return", "none", "continue", "for", "lambda",
            "try", "true", "def", "from", "nonlocal", "while", "and", "del", "global", "not",
            "with", "as", "elif", "if", "or", "yield", "assert", "else", "import", "pass", "break",
            "except", "in", "raise"};

    public static String getRandemStr() {
        return strs[new Random().nextInt(strs.length)];
    }

    public static Integer getDBDate() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * md5(String input) 字符串md5
     */
    public static String md5(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(input.getBytes());
            byte[] resultByteArray = messageDigest.digest();
            char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            char[] resultCharArray = new char[resultByteArray.length * 2];
            int index = 0;
            for (byte b : resultByteArray) {
                resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
                resultCharArray[index++] = hexDigits[b & 0xf];
            }
            return new String(resultCharArray);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * md5L(String input) 字符串md5,返回小写密文
     */
    public static String md5L(String input) {
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(input.getBytes());
            byte[] md = mdInst.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aMd : md) {
                String shaHex = Integer.toHexString(aMd & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * XorEncode 异或加密
     */
    public static String XorEncode(String str, String privatekey) {
        int[] snNum = new int[str.length()];
        String result = "";
        String temp = "";
        for (int i = 0, j = 0; i < str.length(); i++, j++) {
            if (j == privatekey.length())
                j = 0;
            snNum[i] = str.charAt(i) ^ privatekey.charAt(j);
        }
        for (int k = 0; k < str.length(); k++) {
            if (snNum[k] < 10) {
                temp = "00" + snNum[k];
            } else {
                if (snNum[k] < 100) {
                    temp = "0" + snNum[k];
                }
            }
            result += temp;
        }
        return result;
    }

    /**
     * XorDecode 异或解密
     */
    public static String XorDecode(String str, String privateKey) {
        char[] snNum = new char[str.length() / 3];
        String result = "";

        for (int i = 0, j = 0; i < str.length() / 3; i++, j++) {
            if (j == privateKey.length())
                j = 0;
            int n = Integer.parseInt(str.substring(i * 3, i * 3 + 3));
            snNum[i] = (char) ((char) n ^ privateKey.charAt(j));
        }
        for (int k = 0; k < str.length() / 3; k++) {
            result += snNum[k];
        }
        return result;
    }

    /**
     * sha1 字符串sha1值
     */
    public static String sha1(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String shaHex = Integer.toHexString(aMessageDigest & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * sha1 文件hash校验
     */
    public static String sha1(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] b = new byte[1024 * 1024 * 10];//10M memory
            int len;
            while ((len = in.read(b)) > 0) {
                messageDigest.update(b, 0, len);
            }
            return byte2Hex(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static String byte2Hex(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte aB : b) {
            String s = Integer.toHexString(aB & 0xFF);
            if (s.length() == 1) {
                sb.append("0");
            }
            //sb.append(s.toUpperCase());
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        if ((str != null) && !"".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param str
     * @return
     */
    public static String formatLike(String str) {
        if (isNotEmpty(str)) {
            return "%" + str + "%";
        } else {
            return null;
        }
    }

    /**
     * 替换HTML脚本
     *
     * @param value
     * @return
     */
    public static String cleanXSS(String value) {
        //You'll need to remove the spaces from the html entities below
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        return value;
    }

    /**
     * 过滤XSS注入
     *
     * @param value
     * @return
     */
    public static String filterXSS(String value) {
        String cleanValue = null;
        if (value != null) {
            cleanValue = Normalizer.normalize(value, Normalizer.Form.NFD);
            // Avoid null characters
            cleanValue = cleanValue.replaceAll("\0", "");

            // Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid anything in a src='...' type of expression
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid eval(...) expressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid expression(...) expressions
            scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid javascript:... expressions
            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid vbscript:... expressions
            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");

            // Avoid onload= expressions
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            cleanValue = scriptPattern.matcher(cleanValue).replaceAll("");
        }
        return cleanValue;
    }

    private static final Pattern SLUG_REGEX = Pattern.compile("^[A-Za-z0-9_-]{5,100}$", Pattern.CASE_INSENSITIVE);

    /**
     * 判断文件是否是图片类型
     *
     * @param imageFile
     * @return
     */
    public static boolean isImage(InputStream imageFile) {
        try {
            Image img = ImageIO.read(imageFile);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 随机数
     *
     * @param size
     * @return
     */
    public static String getRandomNumber(int size) {
        String num = "";

        for (int i = 0; i < size; ++i) {
            double a = Math.random() * 9.0D;
            a = Math.ceil(a);
            int randomNum = (new Double(a)).intValue();
            num = num + randomNum;
        }

        return num;
    }

    /**
     * 获取保存文件的位置,jar所在目录的路径
     *
     * @return
     */
    public static String getUplodFilePath() {
        String path = ProjectUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.substring(1, path.length());
        try {
            path = java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int lastIndex = path.lastIndexOf("/") + 1;
        path = path.substring(0, lastIndex);
        File file = new File("");
        return file.getAbsolutePath() + "/";
    }

    static Random r = new Random();

    /**
     * 根据一个范围，生成一个随机的整数
     *
     * @param min 最小值（包括）
     * @param max 最大值（包括）
     * @return 随机数
     */
    public static int random(int min, int max) {
        return r.nextInt(max - min + 1) + min;
    }

    private static final char[] _UU64 = "-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] _UU32 = "0123456789abcdefghijklmnopqrstuv".toCharArray();

    /**
     * @return 64进制表示的紧凑格式的 UUID
     */
    public static String UU64() {
        return UU64(java.util.UUID.randomUUID());
    }

    /**
     * 返回一个 UUID ，并用 64 进制转换成紧凑形式的字符串，内容为 [\\-0-9a-zA-Z_]
     * <p>
     * 比如一个类似下面的 UUID:
     * <p>
     * <pre>
     * a6c5c51c-689c-4525-9bcd-c14c1e107c80
     * 一共 128 位，分做L64 和 R64，分为为两个 64位数（两个 long）
     *    > L = uu.getLeastSignificantBits();
     *    > UUID = uu.getMostSignificantBits();
     * 而一个 64 进制数，是 6 位，因此我们取值的顺序是
     * 1. 从L64位取10次，每次取6位
     * 2. 从L64位取最后的4位 ＋ R64位头2位拼上
     * 3. 从R64位取10次，每次取6位
     * 4. 剩下的两位最后取
     * 这样，就能用一个 22 长度的字符串表示一个 32 长度的UUID，压缩了 1/3
     * </pre>
     *
     * @param uu UUID 对象
     * @return 64进制表示的紧凑格式的 UUID
     */
    public static String UU64(java.util.UUID uu) {
        int index = 0;
        char[] cs = new char[22];
        long L = uu.getMostSignificantBits();
        long R = uu.getLeastSignificantBits();
        long mask = 63;
        // 从L64位取10次，每次取6位
        for (int off = 58; off >= 4; off -= 6) {
            long hex = (L & (mask << off)) >>> off;
            cs[index++] = _UU64[(int) hex];
        }
        // 从L64位取最后的4位 ＋ R64位头2位拼上
        int l = (int) (((L & 0xF) << 2) | ((R & (3 << 62)) >>> 62));
        cs[index++] = _UU64[l];
        // 从R64位取10次，每次取6位
        for (int off = 56; off >= 2; off -= 6) {
            long hex = (R & (mask << off)) >>> off;
            cs[index++] = _UU64[(int) hex];
        }
        // 剩下的两位最后取
        cs[index++] = _UU64[(int) (R & 3)];
        // 返回字符串
        return new String(cs);
    }

    /**
     * 从一个 UU64 恢复回一个 UUID 对象
     *
     * @param uu64 64进制表示的 UUID, 内容为 [\\-0-9a-zA-Z_]
     * @return UUID 对象
     */
    public static java.util.UUID fromUU64(String uu64) {
        String uu16 = UU16FromUU64(uu64);
        return java.util.UUID.fromString(UU(uu16));
    }

    public static String UU32(java.util.UUID uu) {
        StringBuilder sb = new StringBuilder();
        long m = uu.getMostSignificantBits();
        long l = uu.getLeastSignificantBits();
        for (int i = 0; i < 13; i++) {
            sb.append(_UU32[(int) (m >> ((13 - i - 1) * 5)) & 31]);
        }
        for (int i = 0; i < 13; i++) {
            sb.append(_UU32[(int) (l >> ((13 - i - 1)) * 5) & 31]);
        }
        return sb.toString();
    }

    public static String UU32() {
        return UU32(java.util.UUID.randomUUID());
    }

    public static java.util.UUID fromUU32(String u32) {
        return new java.util.UUID(parseUnsignedLong(u32.substring(0, 13), 32),
                parseUnsignedLong(u32.substring(13), 32));
    }

    public static long parseUnsignedLong(String s, int radix) {
        int len = s.length();
        long first = Long.parseLong(s.substring(0, len - 1), radix);
        int second = Character.digit(s.charAt(len - 1), radix);
        return first * radix + second;
    }

    /**
     * 将紧凑格式的 UU16 字符串变成标准 UUID 格式的字符串
     *
     * @param uu16
     * @return 标准 UUID 字符串
     */
    public static String UU(String uu16) {
        StringBuilder sb = new StringBuilder();
        sb.append(uu16.substring(0, 8));
        sb.append('-');
        sb.append(uu16.substring(8, 12));
        sb.append('-');
        sb.append(uu16.substring(12, 16));
        sb.append('-');
        sb.append(uu16.substring(16, 20));
        sb.append('-');
        sb.append(uu16.substring(20));
        return sb.toString();
    }

    private static final char[] _UU16 = "0123456789abcdef".toCharArray();

    /**
     * 将一个 UU64 表示的紧凑字符串，变成 UU16 表示的字符串
     * <p>
     * <pre>
     * 每次取2个字符，恢复成3个byte，重复10次， 最后一次，是用最后2个字符，恢复回2个byte </prev>
     *
     * @param uu64
     *            uu64 64进制表示的 UUID, 内容为 [\\-0-9a-zA-Z_]
     * @return 16进制表示的紧凑格式的 UUID
     */
    public static String UU16FromUU64(String uu64) {
        byte[] bytes = new byte[32];
        char[] cs = uu64.toCharArray();
        int index = 0;
        // 每次取2个字符，恢复成3个byte，重复10次，
        for (int i = 0; i < 10; i++) {
            int off = i * 2;
            char cl = cs[off];
            char cr = cs[off + 1];
            int l = Arrays.binarySearch(_UU64, cl);
            int r = Arrays.binarySearch(_UU64, cr);
            int n = (l << 6) | r;
            bytes[index++] = (byte) ((n & 0xF00) >>> 8);
            bytes[index++] = (byte) ((n & 0xF0) >>> 4);
            bytes[index++] = (byte) (n & 0xF);
        }
        // 最后一次，是用最后2个字符，恢复回2个byte
        char cl = cs[20];
        char cr = cs[21];
        int l = Arrays.binarySearch(_UU64, cl);
        int r = Arrays.binarySearch(_UU64, cr);
        int n = (l << 2) | r;
        bytes[index++] = (byte) ((n & 0xF0) >>> 4);
        bytes[index++] = (byte) (n & 0xF);

        // 返回 UUID 对象
        char[] names = new char[32];
        for (int i = 0; i < bytes.length; i++)
            names[i] = _UU16[bytes[i]];
        return new String(names);
    }

    /**
     * 返回指定长度由随机数字+小写字母组成的字符串
     *
     * @param length 指定长度
     * @return 随机字符串
     */
    public static String captchaChar(int length) {
        return captchaChar(length, false);
    }

    /**
     * 返回指定长度随机数字+字母(大小写敏感)组成的字符串
     *
     * @param length          指定长度
     * @param caseSensitivity 是否区分大小写
     * @return 随机字符串
     */
    public static String captchaChar(int length, boolean caseSensitivity) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();// 随机用以下三个随机生成器
        Random randdata = new Random();
        int data = 0;
        for (int i = 0; i < length; i++) {
            int index = rand.nextInt(caseSensitivity ? 3 : 2);
            // 目的是随机选择生成数字，大小写字母
            switch (index) {
                case 0:
                    data = randdata.nextInt(10);// 仅仅会生成0~9, 0~9的ASCII为48~57
                    sb.append(data);
                    break;
                case 1:
                    data = randdata.nextInt(26) + 97;// 保证只会产生ASCII为97~122(a-z)之间的整数,
                    sb.append((char) data);
                    break;
                case 2: // caseSensitivity为true的时候, 才会有大写字母
                    data = randdata.nextInt(26) + 65;// 保证只会产生ASCII为65~90(A~Z)之间的整数
                    sb.append((char) data);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 返回指定长度随机数字组成的字符串
     *
     * @param length 指定长度
     * @return 随机字符串
     */
    public static String captchaNumber(int length) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * POST请求获取数据
     */
    public static String getResultByPOST(String url, Map<String, String> map) {
        HttpURLConnection conn = null;
        ByteArrayOutputStream out = null;
        InputStream in = null;
        StringBuilder params = new StringBuilder();
        try {
            if (map != null && map.size() > 0) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    Object key = entry.getKey();
                    Object val = entry.getValue();
                    params.append(key).append("=").append(val).append("&");
                }
                params.deleteCharAt(params.length() - 1);
            }
            URL ul = new URL(url);
            conn = (HttpURLConnection) ul.openConnection();
            conn.setConnectTimeout(8888);
            conn.setReadTimeout(8888);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);// 是否输入参数
            byte[] bypes = params.toString().getBytes();
            conn.getOutputStream().write(bypes);// 输入参数
            in = conn.getInputStream();
            out = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[4096 * 2];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            byte[] buffert = out.toByteArray();
            return new String(buffert, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {

                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {

                }
            }
        }
    }
}
