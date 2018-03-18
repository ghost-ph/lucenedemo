package com.patterncat.lucene.probTagger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * basic dictionary
 */
public class DicDoc {
    /**
     * An inner class of Ternary Search Trie that represents a node in the trie.
     */
    public final class TSTNode {
        /** The key to the node. */
        public DocTypes data = null;

        /** The relative nodes. */
        protected TSTNode loKID;
        protected TSTNode eqKID;
        protected TSTNode hiKID;

        /** The char used in the split. */
        protected char splitchar;

        /**
         * Constructor method.
         *
         *@param splitchar
         *            The char used in the split.
         */
        protected TSTNode(char splitchar) {
            this.splitchar = splitchar;
        }

        public String toString() {
            return "splitchar:" + splitchar;
        }
    }

    /** The base node in the trie. */
    public TSTNode root;

    public static String getDir() {
        String dir = System.getProperty("dic.dir");
        if (dir == null)
            dir = "/dic/add/";
        else if (!dir.endsWith("/"))
            dir += "/";
        return dir;
    }

    /**
     * Constructs a Ternary Search Trie and loads data from a <code>File</code>
     * into the Trie. The file is a normal text document, where each line is of
     * the form word : integer.
     *
     *@param file
     *            The <code>File</code> with the data to load into the Trie.
     *@exception IOException
     *                A problem occured while reading the data.
     */
    public void load(String dic, PartOfSpeech type, int weight) {
        String line = null;
        long code = 0;

        try {
            InputStream file = null;
            if (System.getProperty("dic.dir") == null) {
                file = getClass().getResourceAsStream(getDir() + dic);
            } else
                file = new FileInputStream(new File(getDir() + dic));

            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(file, "GBK"));
            while ((line = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ":");
                String key = "";
                if (st.hasMoreTokens()) {
                    key = st.nextToken();
                }
                if (st.hasMoreTokens()) {
                    try {
                        code = Long.parseLong(st.nextToken());
                        // System.out.println(code);
                    } catch (NumberFormatException e) {
                        code = 0;
                    }
                }
                addWord(key, code, type, weight);
            }
            in.close();
        } catch (IOException e) {
            System.out.println("not find dictionary file:" + dic);
        } catch (java.util.NoSuchElementException e) {
            System.out.println("format error:" + line);
            // System.exit(-1);
        }
    }

    // 添加内容
    private void addWord(String key, long code, PartOfSpeech type, int w) {
        if ("".equals(key)) {
            return;
        }
        if (root == null) {
            root = new TSTNode(key.charAt(0));
        }

        DocTypes.DocTypeInf pi = new DocTypes.DocTypeInf(type, w, code);

        TSTNode node = null;
        if (key.length() > 0 && root != null) {
            TSTNode currentNode = root;
            int charIndex = 0;
            while (true) {
                if (currentNode == null)
                    break;
                int charComp = (key.charAt(charIndex) - currentNode.splitchar);
                if (charComp == 0) {
                    charIndex++;
                    if (charIndex == key.length()) {
                        node = currentNode;
                        break;
                    }
                    currentNode = currentNode.eqKID;
                } else if (charComp < 0) {
                    currentNode = currentNode.loKID;
                } else {
                    currentNode = currentNode.hiKID;
                }
            }
            DocTypes occur2 = null;
            if (node != null) {
                occur2 = node.data;
            }
            if (occur2 != null) {
                occur2.insert(pi);
                return;
            }
            currentNode = getOrCreateNode(key);
            DocTypes occur3 = currentNode.data;
            if (occur3 != null) {
                occur3.insert(pi);
            } else {
                DocTypes occur = new DocTypes();
                occur.put(pi);
                currentNode.data = occur;
            }
        }
    }

    public static int matchEnglish(int start, String sentence) {
        int i = start;
        int count = sentence.length();
        String sd = "ＱＷＥＲＴＹＵＩＯＰＡＳＤＦＧＨＪＫＬＺＸＣＶＢＮＭ号";
        while (i < count) {
            char c = sentence.charAt(i);
            if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
                ++i;
            } else if (sd.indexOf(c) > -1) {
                ++i;
            } else {
                break;
            }
        }

        return i;
    }

    public static int matchNum(int start, String sentence) {
        int i = start;
        int count = sentence.length();
        while (i < count) {
            char c = sentence.charAt(i);
            if ((c >= '0' && c <= '9') || (c >= '０' && c <= '９') || c == '-'
                    || c == '－') {
                ++i;
            } else {
                break;
            }
        }

        if (i > start && i < count) {
            char end = sentence.charAt(i);
            if ('号' == end || '#' == end || '＃' == end) {
                i++;
            }
        }
        return i;
    }

    public ArrayList<MatchRet> matchAll(String key, int offset) {
        ArrayList<MatchRet> matchRets = new ArrayList<MatchRet>();
        if (key == null || root == null || "".equals(key)
                || offset >= key.length()) {
            return matchRets;
        }
        // System.out.println(key);
        int ret = offset;
        PartOfSpeech retPOS = null;
        int retWeight = 0;

        ret = matchNum(offset, key);
        if (ret > offset) {
            retPOS = PartOfSpeech.m;
            retWeight = 100;

            DocTypes.DocTypeInf posInf = new DocTypes.DocTypeInf(retPOS,
                    retWeight, 0);
            DocTypes addressData = new DocTypes();
            addressData.put(posInf);
            MatchRet matchRet = new MatchRet(ret, addressData);
            matchRets.add(matchRet);
        }

        int retEnglishNum = matchEnglish(offset, key);
        if (retEnglishNum > ret) {
            // matchRet.end = ret;
            // matchRet.pos = AddressType.No;
            // return matchRet;
            ret = retEnglishNum;
            retPOS = PartOfSpeech.nx;
            retWeight = 100;
            DocTypes.DocTypeInf posInf = new DocTypes.DocTypeInf(retPOS,
                    retWeight, 0);
            DocTypes addressData = new DocTypes();
            addressData.put(posInf);
            MatchRet matchRet = new MatchRet(ret, addressData);
            matchRets.add(matchRet);
        }

        TSTNode currentNode = root;
        int charIndex = offset;
        while (true) {
            if (currentNode == null) {
                // System.out.println("ret "+ret);
                // matchRet.end = ret;
                // matchRet.code = code;
                // matchRet.pos = retPOS;
                // matchRet.weight = retWeight;

                return matchRets;
            }
            int charComp = key.charAt(charIndex) - currentNode.splitchar;

            if (charComp == 0) {
                charIndex++;

                if (currentNode.data != null) {
                    ret = charIndex;
                    // code = currentNode.code;
                    // retPOS = currentNode.type;
                    // retWeight = currentNode.weight;
                    MatchRet matchRet = new MatchRet(ret, currentNode.data);
                    matchRets.add(matchRet);
                    // System.out.println("ret pos:"+retPOS);
                }
                if (charIndex == key.length()) {
                    return matchRets;
                }
                currentNode = currentNode.eqKID;
            } else if (charComp < 0) {
                currentNode = currentNode.loKID;
            } else {
                currentNode = currentNode.hiKID;
            }
        }
    }

    /**
     * Returns the node indexed by key, creating that node if it doesn't exist,
     * and creating any required intermediate nodes if they don't exist.
     *
     *@param key
     *            A <code>String</code> that indexes the node that is returned.
     *@return The node object indexed by key. This object is an instance of an
     *         inner class named <code>TernarySearchTrie.TSTNode</code>.
     *@exception NullPointerException
     *                If the key is <code>null</code>.
     *@exception IllegalArgumentException
     *                If the key is an empty <code>String</code>.
     */
    protected TSTNode getOrCreateNode(String key) throws NullPointerException,
            IllegalArgumentException {
        if (key == null) {
            throw new NullPointerException(
                    "attempt to get or create node with null key");
        }
        if ("".equals(key)) {
            throw new IllegalArgumentException(
                    "attempt to get or create node with key of zero length");
        }
        if (root == null) {
            root = new TSTNode(key.charAt(0));
        }
        TSTNode currentNode = root;
        int charIndex = 0;
        while (true) {
            int charComp = (key.charAt(charIndex) - currentNode.splitchar);
            if (charComp == 0) {
                charIndex++;
                if (charIndex == key.length()) {
                    return currentNode;
                }
                if (currentNode.eqKID == null) {
                    currentNode.eqKID = new TSTNode(key.charAt(charIndex));
                }
                currentNode = currentNode.eqKID;
            } else if (charComp < 0) {
                if (currentNode.loKID == null) {
                    currentNode.loKID = new TSTNode(key.charAt(charIndex));
                }
                currentNode = currentNode.loKID;
            } else {
                if (currentNode.hiKID == null) {
                    currentNode.hiKID = new TSTNode(key.charAt(charIndex));
                }
                currentNode = currentNode.hiKID;
            }
        }
    }

    public static class MatchRet {
        public int end;
        public DocTypes posInf;

        public MatchRet(int e, DocTypes d) {
            end = e;
            posInf = d;
        }

        public String toString() {
            return "endPosition:" + end + " posInf:" + posInf;
        }
    }

    private static DicDoc dicCore = new DicDoc();

    /**
     *
     * @return the singleton of basic dictionary
     */
    public static DicDoc getInstance() {
        return dicCore;
    }

    public static void reLoad() {
        dicCore = null;
        dicCore = new DicDoc();
    }

    private DicDoc() {
		/*load("country.txt", PartOfSpeech.Country, 10000);
		load("province.txt", PartOfSpeech.Province, 10000);
		load("city.txt", PartOfSpeech.City, 10000);
		load("county.txt", PartOfSpeech.County, 1000);
		load("street.txt", PartOfSpeech.Street, 10000000);
		load("relatedPos.txt", PartOfSpeech.RelatedPos, 100);
		load("landmark.txt", PartOfSpeech.LandMark, 100000);
		load("town.txt", PartOfSpeech.Town, 1000);
		load("district.txt", PartOfSpeech.District, 1000000);
		load("village.txt", PartOfSpeech.Village, 10000);
		load("SuffixLandMark.txt", PartOfSpeech.SuffixLandMark, 100000);
		load("SuffixDistrict.txt", PartOfSpeech.SuffixDistrict, 100000);
		load("SuffixBuildingUnit.txt", PartOfSpeech.SuffixBuildingUnit, 1000);

		addWord("其他国家或地区", 100000000000l, PartOfSpeech.Country, 100);
		addWord("北京市", 110000000000l, PartOfSpeech.Municipality, 200000);
		addWord("北京", 110000000000l, PartOfSpeech.Municipality, 200000);
		addWord("上海市", 310000000000l, PartOfSpeech.Municipality, 200000);
		addWord("上海", 310000000000l, PartOfSpeech.Municipality, 200000);
		addWord("天津市", 120000000000l, PartOfSpeech.Municipality, 200000);
		addWord("天津", 120000000000l, PartOfSpeech.Municipality, 200000);
		addWord("重庆市", 500000000000l, PartOfSpeech.Municipality, 200000);
		addWord("重庆", 500000000000l, PartOfSpeech.Municipality, 200000);
		addWord("郑州市", 410100000000l, PartOfSpeech.City, 10000000);
		addWord("南京市", 320100000000l, PartOfSpeech.City, 10000000);
		addWord("九龙坡", 500107000000l, PartOfSpeech.County, 10000000);
		addWord("九龙坡区", 500107000000l, PartOfSpeech.County, 10000000);
		addWord("中山区", 210202000000l, PartOfSpeech.County, 10000000);

		addWord("交叉口", 0, PartOfSpeech.Crossing, 100);
		addWord("交界处", 0, PartOfSpeech.Crossing, 100);
		addWord("十字路口", 0, PartOfSpeech.Crossing, 100);

		addWord("近郊", 0, PartOfSpeech.Other, 100000);

		addWord("（", 0, PartOfSpeech.StartSuffix, 1300000);
		addWord("(", 0, PartOfSpeech.StartSuffix, 1300000);
		addWord("大街", 0, PartOfSpeech.SuffixStreet, 1300000);
		addWord("弄", 0, PartOfSpeech.SuffixStreet, 1300000);
		addWord("环", 0, PartOfSpeech.SuffixStreet, 1300000);
		addWord("段", 0, PartOfSpeech.SuffixStreet, 1300000);
		addWord("胡同", 0, PartOfSpeech.SuffixStreet, 1300000);
		addWord("公路", 0, PartOfSpeech.SuffixStreet, 1200000);
		addWord("路", 0, PartOfSpeech.SuffixStreet, 1200000);
		addWord("大道", 0, PartOfSpeech.SuffixStreet, 200);
		addWord("道", 0, PartOfSpeech.SuffixStreet, 200);
		addWord("街", 0, PartOfSpeech.SuffixStreet, 200);
		addWord("巷", 0, PartOfSpeech.SuffixStreet, 200);
		addWord("国道", 0, PartOfSpeech.SuffixStreet, 200);
		addWord("路口", 0, PartOfSpeech.SuffixStreet, 200);
		addWord("条", 0, PartOfSpeech.SuffixStreet, 200);

		addWord("省", 0, PartOfSpeech.SuffixProvince, 10);
		addWord("自治区", 0, PartOfSpeech.SuffixProvince, 10);
		addWord("特别行政区", 0, PartOfSpeech.SuffixMunicipality, 10);
		addWord("市", 0, PartOfSpeech.SuffixCity, 10);
		addWord("依族苗族自治州", 0, PartOfSpeech.SuffixCity, 10);
		addWord("自治州", 0, PartOfSpeech.SuffixCity, 100);
		addWord("藏族自治州", 0, PartOfSpeech.SuffixCity, 100);
		addWord("土家族苗族自治州", 0, PartOfSpeech.SuffixCity, 100);
		addWord("朝鲜族自治州", 0, PartOfSpeech.SuffixCity, 100);
		addWord("盟", 0, PartOfSpeech.SuffixCity, 100);

		addWord("回族区", 0, PartOfSpeech.SuffixCounty, 100);
		addWord("区", 0, PartOfSpeech.SuffixCounty, 10);
		addWord("區", 0, PartOfSpeech.SuffixCounty, 100);
		addWord("县", 0, PartOfSpeech.SuffixCounty, 100);
		addWord("自治旗", 0, PartOfSpeech.SuffixCounty, 100);
		addWord("自治县", 0, PartOfSpeech.SuffixCounty, 100);
		addWord("回族自治县", 0, PartOfSpeech.SuffixCounty, 100);
		addWord("土家族自治县", 0, PartOfSpeech.SuffixCounty, 100);

		addWord("镇", 0, PartOfSpeech.SuffixTown, 10000);
		addWord("乡", 0, PartOfSpeech.SuffixTown, 10000);
		// ("营",0,AddressType.SuffixTown,200);

		// addWord("庄",0,AddressType.SuffixVillage,100);
		addWord("新村", 0, PartOfSpeech.SuffixVillage, 1000000000);
		addWord("村", 0, PartOfSpeech.SuffixVillage, 1000);
		// addWord("塘",0,AddressType.SuffixVillage,100);

		addWord("栋", 0, PartOfSpeech.SuffixBuildingNo, 200);
		addWord("亭", 0, PartOfSpeech.SuffixBuildingNo, 200);
		addWord("组", 0, PartOfSpeech.SuffixBuildingNo, 200);
		addWord("档", 0, PartOfSpeech.SuffixBuildingNo, 200);
		addWord("卡", 0, PartOfSpeech.SuffixBuildingNo, 200);

		addWord("號", 0, PartOfSpeech.SuffixBuildingNo, 200);
		addWord("號樓", 0, PartOfSpeech.SuffixBuildingNo, 200);
		addWord("幢", 0, PartOfSpeech.SuffixBuildingNo, 200);
		addWord("排", 0, PartOfSpeech.SuffixBuildingNo, 200);
		addWord("栋", 0, PartOfSpeech.SuffixBuildingNo, 200);
		// addWord("信箱",0,AddressType.SuffixBuildingNo,200);
		addWord("座", 0, PartOfSpeech.SuffixBuildingNo, 1000);
		// addWord("坐",0,AddressType.SuffixBuildingNo,200);
		addWord("号", 0, PartOfSpeech.SuffixBuildingNo, 200);
		// addWord("房",0,AddressType.SuffixBuildingNo,200);
		// addWord("期",0,AddressType.SuffixBuildingNo,200);
		addWord("临时", 0, PartOfSpeech.SuffixBuildingNo, 200);

		addWord("院", 0, PartOfSpeech.SuffixIndicationFacility, 200);
		addWord("大院", 0, PartOfSpeech.SuffixIndicationFacility, 200);
		addWord("路口", 0, PartOfSpeech.SuffixIndicationFacility, 100);
		addWord("站", 0, PartOfSpeech.SuffixIndicationFacility, 100);
		addWord("库", 0, PartOfSpeech.SuffixIndicationFacility, 100);
		addWord("地铁", 0, PartOfSpeech.SuffixIndicationFacility, 100);
		addWord("大桥", 0, PartOfSpeech.SuffixIndicationFacility, 100);
		addWord("桥", 0, PartOfSpeech.SuffixIndicationFacility, 100);
		addWord("河", 0, PartOfSpeech.SuffixIndicationFacility, 100);
		addWord("公里", 0, PartOfSpeech.SuffixIndicationPosition, 100);
		addWord("米", 0, PartOfSpeech.SuffixIndicationPosition, 100);
		addWord("市辖区", 0, PartOfSpeech.Other, 900000000);*/
    }
}
