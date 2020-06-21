package com.github.wycm.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class PhraseTranslateUtils {

    private static final String WORD_POS_PREFIX = "[\\[|〔|〈|<]";
    private static final String WORD_POS_SUFFIX = "[\\]|〕|〉|>]";
    private static final String WORD_POS_PATTERN = WORD_POS_PREFIX + "[^x00-xff]+?" + WORD_POS_SUFFIX;

    private static final String BLANK_PATTERN = "\\s+|\t|\r|\n";
    private static final String TXT_BLANK_PATTERN = "\\s+|\t";
    private static Pattern posPattern = compile(WORD_POS_PATTERN);
    private static Pattern blankPattern = compile(BLANK_PATTERN);
    private static Pattern txtBlankPattern = compile(TXT_BLANK_PATTERN);

    /**
     * 将待翻译文本拆分为语句
     *
     * @param transText
     * @return
     */
    public static List<String> splitPhrase(String transText) {
        return Arrays.asList(StringUtils.split(transText, "."));
    }

    /**
     * 将一个语句按照顺序拆分为不超过maxLength的短语词组
     *
     * @param transText
     * @param maxLength
     * @return
     */
    public static List<String> splitTransWords(String transText, int maxLength) {
        List<String> transList = new ArrayList<>();
        String[] tempWords = StringUtils.split(transText, " ");
        String combinationWord = "";
        for (int i = 1; i <= maxLength; i++) {
            for (int j = 0; j + i <= tempWords.length; j++) {
                for (int k = j; k < j + i; k++) {
                    combinationWord += tempWords[k] + " ";
                }
                transList.add(new String(combinationWord).trim());
                combinationWord = "";
            }
        }
        return transList;
    }

    public static String parseTransResult(String translation, String regx) {
        return translation;
    }

    public static String parseWordPos(String dictTrans) {
        Matcher matcher = posPattern.matcher(dictTrans.trim());
        if (matcher.find()) {
            return matcher.group().replaceAll(WORD_POS_PREFIX, "").replaceAll(WORD_POS_SUFFIX, "");
        }
        return "";
    }

    private static final String WORD_TRANS_PREFIX = "([\\]|〕|,|〉|>])";
    private static final String WORD_TRANS_SUFFIX = "([,|，|:|：|。|！|!])";
    private static final String DONT_MATCH = "??";
    private static final String NO_ADVERB_WORD_TRANS_PATTERN = WORD_TRANS_PREFIX + DONT_MATCH + ".*" + WORD_TRANS_SUFFIX;
    private static final String NO_INTERPRETATION_WORD_TRANS_PATTERN = WORD_TRANS_PREFIX + ".*";
    private static final String NO_ADVERB_INTERPRETATION_WORD_TRANS_PATTERN = WORD_TRANS_PREFIX + DONT_MATCH + ".*" + WORD_TRANS_SUFFIX + DONT_MATCH;
    private static final String WORD_TRANS_PATTERN = WORD_TRANS_PREFIX + ".*" + WORD_TRANS_SUFFIX;
    private static Pattern transPattern = compile(WORD_TRANS_PATTERN);
    private static Pattern noAdverbTransPattern = compile(NO_ADVERB_WORD_TRANS_PATTERN);
    private static Pattern noInterpretationTransPattern = compile(NO_INTERPRETATION_WORD_TRANS_PATTERN);
    private static Pattern noAdverbInterpretationTransPattern = compile(NO_ADVERB_INTERPRETATION_WORD_TRANS_PATTERN);

    public static String parseDictTranslation(String dictTrans) {
        dictTrans = dictTrans.replaceAll("①|②", "");
        if (dictTrans.contains("（") || dictTrans.contains("(") || dictTrans.contains(")") || dictTrans.contains("）")) {
            dictTrans = dictTrans.replaceAll("[（|\\(].*[）|\\)]", "");
        }
        if (dictTrans.contains("：")) {
            dictTrans = dictTrans.substring(0, dictTrans.indexOf("：") + 1);
        }
        Matcher matcher = transPattern.matcher(dictTrans.trim());
        if (matcher.find()) {
            String tempStr = matcher.group().replaceAll("]|〕", "");
            Matcher tmpMatcher = transPattern.matcher(tempStr);
            if (tmpMatcher.find()) {
                return tmpMatcher.group().replaceAll(WORD_TRANS_PREFIX, "").replaceAll(WORD_TRANS_SUFFIX, "").trim();
            }
            return matcher.group().replaceAll(WORD_TRANS_PREFIX, "").replaceAll(WORD_TRANS_SUFFIX, "").trim();
        } else {
            matcher = noInterpretationTransPattern.matcher(dictTrans.trim());
            if (matcher.find()) {
                String tempStr = matcher.group().replaceAll("]|〕", "");
                Matcher tmpMatcher = noInterpretationTransPattern.matcher(tempStr);
                if (tmpMatcher.find()) {
                    return tmpMatcher.group().replaceAll(WORD_TRANS_PREFIX, "").replaceAll(WORD_TRANS_SUFFIX, "").trim();
                }
                return matcher.group().replaceAll(WORD_TRANS_PREFIX, "").replaceAll(WORD_TRANS_SUFFIX, "").trim();
            } else {
                matcher = noAdverbTransPattern.matcher(dictTrans.trim());
                if (matcher.find()) {
                    return matcher.group().replaceAll(WORD_TRANS_SUFFIX, "").trim();
                } else {
                    matcher = noAdverbInterpretationTransPattern.matcher(dictTrans.trim());
                    if (matcher.find()) {
                        return matcher.group().trim();
                    }
                }
            }
        }
        return "";
    }

    public static String formatBlankInfo(String transWord) {
        if (StringUtils.isBlank(transWord)) {
            return "";
        }
        Matcher m = blankPattern.matcher(transWord);
        return m.replaceAll(" ");
    }

    public static String formatTxtBlankInfo(String transWord) {
        if (StringUtils.isBlank(transWord)) {
            return "";
        }
        Matcher m = txtBlankPattern.matcher(transWord);
        return m.replaceAll(" ");
    }

    public static void main(String[] args) {
        String str = "acanjime isanjire tulergi gurun ubaliyambure kuren";
        System.out.println(formatBlankInfo("test"));
        System.out.println(splitTransWords(str, 5));
        /**
         * [数]① 一， 一个： tere sargan jui be monGo de bufi, emu aniya i onGolo yala goidahak@ becehe. 《1?实》其女聘与蒙古，未及一年果亡。 fa i ninGude emu fempi bithe be sabufi, neifi tuwaci sargan jui beyei muribuha baita be giyan giyan i tucibume araha. 《11?聊》看到窗户上有一封信，拆开一看，是女子一五一十地备述自己的冤案。〔见窗上一函，开视，则女备述其冤状。〕②一样的，相同的： emu doro uhe mujin 道同志合。 emu oho de banjiha emcu halai ah@n 《38?庸》同母异父的哥哥。
         * 这么那么地，推三推四地： erken terken i inenGi anatahai atanGi dube da. 《28?百》推三推四地一再拖延，何时才有个终局？
         * [不及]同olhombi②。
         * [不及]同oibombi：aikabade ulhire unde seci，inu aifini oibume ohoni．《18?诗》借曰未知，亦聿既耄。
         * 〈禽〉■（介+鸟）（似
         * [及]袭捕禽兽，捕捉： gurun geolembi 捕捉野兽。 si mini gurun geolehe be sabuhao. 《38?庸》你见过我捕捉野兽吗？
         * 〔名〕羊皮金，系清初旧话，见《大清全书》。
         * 〔名〕①谙达，宾友，友人。②伙计，友伴。
         * [副]何等，多么，果真： anda saikan 果真好。 asihan de bi anda sektu bihe, te ainaha. 《30?成》 年轻时我睡得多么轻，如今是怎么了?
         */

        List<String> testList = new ArrayList<>();
//        testList.add("[数]① 一， 一个： tere sargan jui be monGo de bufi, emu aniya i onGolo yala goidahak@ becehe. 《1?实》其女聘与蒙古，未及一年果亡。");
//        testList.add("这么那么地，推三推四地： erken terken i inenGi anatahai atanGi dube da. 《28?百》推三推四地一再拖延，何时才有个终局？");
//        testList.add("[不及]同olhombi②。");
//        testList.add("[不及]同oibombi：aikabade ulhire unde seci，inu aifini oibume ohoni．《18?诗》借曰未知，亦聿既耄。");
//        testList.add("〈禽〉■（介+鸟）（似");
//        testList.add("[及]袭捕禽兽，捕捉： gurun geolembi 捕捉野兽。");
//        testList.add("〔名〕羊皮金，系清初旧话，见《大清全书》。");
//        testList.add("[副]何等，多么，果真： anda saikan 果真好。 asihan de bi anda sektu bihe, te ainaha. 《30?成》 年轻时我睡得多么轻，如今是怎么了?");
//        testList.add("①管，干预，干涉： yasa morofi meifen ergide baibi jili bi, ede jai we dambi. 《30?成》圆睁着两个眼，只是一脖子气，谁还管他。 meni jecereme tehe jiyanggiy@n ambasa de gemu ere baita be icihiyara saligan toose ak@ gelhun ak@ darak@. 《48?档》我等驻边将军大臣，皆无处理此事之权，故不敢干预之。 aikabade eture jetere be darak@, beyere yuyure be fonjirak@, jug@n yabure niyalma i adali obume (wei guwanta seme) sakdasa be akara gingkarade isibuci, ak@ oho manggi ai hacin i gosiholome songgoho seme ai baita. 《28?百》如果不管吃穿，不问冻饿，像过路人一样（说谁管他），把老人弄得愁闷气愤了，等（他们）去世以后无论怎样伤心痛哭，又有什么用呢？②援，援助，救援，增援： hadai gurun de dame cooha tebume unggihe. 《1?实》救援哈达国，派兵驻扎。 sungSan ci dame jihe moringga cooha be yafahalame bireme dosifi, terei geren be gidaha. 《5?氏》有骑兵自松山来援，徒步冲入，败其众。 ere aniya duin biyade, hehe han i cooha dame isijifi haha han i cooha be gidafi, geren gemu ba bade ukame samsihabi. 《48?档》至今年四月，女汗之兵马前来增援，男汗之兵即被打败。其众四散逃命。julgei cooha baitalara mangga seh urse, bata i cooha be julergi amargi ishunde daci ojorak@ be julergi amargi ishunde daci ojorak@ omi metembi. 《50?其》古之所谓善用兵者，能使敌人首尾不相及。③烧着，点着： duin ergici");
//        testList.add("同");
//        testList.add("[名]同");
//        testList.add("[方]①这边： birai ebergi 河这边。");
//        testList.add("<地>安徽，简称皖。");
//        testList.add("〈衙〉安徽清吏司。");
//        testList.add("[名]〈地〉安徽。");
//        testList.add("祈年门。");
//        testList.add("〈官〉侯人（古代迎送路客的官员）。");
//        testList.add("〈官〉令尹(古官名)。");
//        testList.add("〔名〕〈植〉榠楂，异果名。");
//        testList.add("〈公〉行粮（支放运军的钱粮): ere tucibuhe coohai urse gurun booi jalin coohai bede h@sun bume faSSame yabure be, bi ambula gosime g@nime ofi, bigan i jetere ciyanliyang bahabume, ceni booi anggala be ujibumbi. 《3?上》 此所派之兵丁等，既为国家效力行间，朕心更为轸念，已于行粮恩赏之外，给与坐粮以养赡其家口。");
//        testList.add("[名]<植>探子(异果名。似梨，冬熟，味稍酸)。");
//        testList.add("[名]<动>麒麟。");
//        testList.add("[名]〈咒〉生蛆的！");
//        testList.add("[名]〈动〉解（tonto“獬豸”的别名）。");
//        testList.add("〈动〉野鸡膀子菜（野菜名）。");
        testList.add("[名]〈动〉威夷（异兽名）");
        for (String testStr : testList) {
            System.out.println(parseWordPos(testStr));
            System.out.println(parseDictTranslation(testStr));
        }

    }

}
