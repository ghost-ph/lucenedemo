package com.patterncat.lucene.probTagger;

import java.util.ArrayList;

/**
 * 未登录地名识别规则
 *
 * @author luogang
 * @2010-3-23
 */
public class UnknowGrammar {

    /**
     * An inner class of Ternary Search Trie that represents a node in the trie.
     */
    public final class TSTNode {

        /** The key to the node. */
        public ArrayList<DocSpan> data = null;

        /** The relative nodes. */
        protected TSTNode loKID;
        protected TSTNode eqKID;
        protected TSTNode hiKID;

        /** The char used in the split. */
        protected PartOfSpeech splitchar;

        /**
         * Constructor method.
         *
         *@param splitchar
         *            The char used in the split.
         */
        protected TSTNode(PartOfSpeech splitchar) {
            this.splitchar = splitchar;
        }

        public String toString() {
            return "splitchar:" + splitchar;
        }
    }

    /** The base node in the trie. */
    public TSTNode root;

    public void addProduct(ArrayList<PartOfSpeech> key,
                           ArrayList<DocSpan> lhs) {
        if (root == null) {
            root = new TSTNode(key.get(0));
        }
        TSTNode node = null;
        if (key.size() > 0 && root != null) {
            TSTNode currentNode = root;
            int charIndex = 0;
            while (true) {
                if (currentNode == null)
                    break;
                int charComp = key.get(charIndex).compareTo(
                        currentNode.splitchar);
                if (charComp == 0) {
                    charIndex++;
                    if (charIndex == key.size()) {
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
            ArrayList<DocSpan> occur2 = null;
            if (node != null) {
                occur2 = node.data;
            }
            if (occur2 != null) {
                // occur2.insert(pi);
                return;
            }
            currentNode = getOrCreateNode(key);
            currentNode.data = lhs;
        }
    }

    public MatchRet matchLong(ArrayList<DocToken> key, int offset,
                              MatchRet matchRet) {

        if (key == null || root == null || "".equals(key)
                || offset >= key.size()) {
            matchRet.end = offset;
            matchRet.lhs = null;
            return matchRet;
        }
        int ret = offset;
        ArrayList<DocSpan> retPOS = null;

        // System.out.println("enter");
        TSTNode currentNode = root;
        int charIndex = offset;
        while (true) {
            if (currentNode == null) {
                // System.out.println("ret "+ret);
                matchRet.end = ret;
                matchRet.lhs = retPOS;
                return matchRet;
            }
            int charComp = key.get(charIndex).type
                    .compareTo(currentNode.splitchar);

            if (charComp == 0) {
                // System.out.println("comp:"+key.get(charIndex).type);
                charIndex++;

                if (currentNode.data != null && charIndex > ret) {
                    ret = charIndex;
                    retPOS = currentNode.data;
                    // System.out.println("ret pos:"+retPOS);
                }
                if (charIndex == key.size()) {
                    matchRet.end = ret;
                    matchRet.lhs = retPOS;
                    return matchRet;
                }
                currentNode = currentNode.eqKID;
            } else if (charComp < 0) {
                currentNode = currentNode.loKID;
            } else {
                currentNode = currentNode.hiKID;
            }
        }
    }

    public static void replace(ArrayList<DocToken> key, int offset,
                               ArrayList<DocSpan> spans) {
        int j = 0;
        for (int i = offset; i < key.size(); ++i) {
            DocSpan span = spans.get(j);
            DocToken token = key.get(i);
            StringBuilder newText = new StringBuilder();
            int newStart = token.start;
            int newEnd = token.end;
            PartOfSpeech newType = span.type;

            for (int k = 0; k < span.length; ++k) {
                token = key.get(i + k);
                newText.append(token.termText);
                newEnd = token.end;
            }
            DocToken newToken = new DocToken(newStart, newEnd, newText
                    .toString(), newType);

            for (int k = 0; k < span.length; ++k) {
                key.remove(i);
            }
            key.add(i, newToken);
            j++;
            if (j >= spans.size()) {
                return;
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
    protected TSTNode getOrCreateNode(ArrayList<PartOfSpeech> key)
            throws NullPointerException, IllegalArgumentException {
        if (key == null) {
            throw new NullPointerException(
                    "attempt to get or create node with null key");
        }
        if ("".equals(key)) {
            throw new IllegalArgumentException(
                    "attempt to get or create node with key of zero length");
        }
        if (root == null) {
            root = new TSTNode(key.get(0));
        }
        TSTNode currentNode = root;
        int charIndex = 0;
        while (true) {
            int charComp = key.get(charIndex).compareTo(currentNode.splitchar);
            if (charComp == 0) {
                charIndex++;
                if (charIndex == key.size()) {
                    return currentNode;
                }
                if (currentNode.eqKID == null) {
                    currentNode.eqKID = new TSTNode(key.get(charIndex));
                }
                currentNode = currentNode.eqKID;
            } else if (charComp < 0) {
                if (currentNode.loKID == null) {
                    currentNode.loKID = new TSTNode(key.get(charIndex));
                }
                currentNode = currentNode.loKID;
            } else {
                if (currentNode.hiKID == null) {
                    currentNode.hiKID = new TSTNode(key.get(charIndex));
                }
                currentNode = currentNode.hiKID;
            }
        }
    }

    public static class MatchRet {
        public int end;
        public ArrayList<DocSpan> lhs;

        public MatchRet(int e, ArrayList<DocSpan> d) {
            end = e;
            lhs = d;
        }

        public String toString() {
            return end + ":" + lhs;
        }
    }

    private UnknowGrammar() {
        // 1
		/*ArrayList<DocSpan> lhs = new ArrayList<DocSpan>();
		ArrayList<PartOfSpeech> rhs = new ArrayList<PartOfSpeech>(); // right-hand

		// ==============================================以下是省的部分
		// 北京市
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Municipality);
		rhs.add(PartOfSpeech.SuffixCity);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(2, PartOfSpeech.Municipality));

		addProduct(rhs, lhs);

		// 北京市朝阳区高碑店乡高碑店
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Municipality);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.End);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Municipality));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(1, PartOfSpeech.End));

		addProduct(rhs, lhs);

		// 中国江苏南京市江苏省
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.SuffixProvince);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(2, PartOfSpeech.Province));

		addProduct(rhs, lhs);
		// 中国江苏江阴市江苏省
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.SuffixProvince);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(2, PartOfSpeech.Province));

		addProduct(rhs, lhs);
		// 中国江苏苏州市吴中区江苏省
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.SuffixProvince);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(2, PartOfSpeech.Province));

		addProduct(rhs, lhs);
		// 中国江苏苏州市吴中区江苏省
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixProvince);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(2, PartOfSpeech.Province));

		addProduct(rhs, lhs);
		// 河南郑州市河南省
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.SuffixIndicationFacility);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixProvince);
		lhs.add(new DocSpan(3, PartOfSpeech.Province));

		addProduct(rhs, lhs);

		// 河南郑州市河南省
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.SuffixProvince);
		lhs.add(new DocSpan(2, PartOfSpeech.Province));

		addProduct(rhs, lhs);

		// =============================以下是市部分

		// 中国江苏南京市
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixCity);
		lhs.add(new DocSpan(2, PartOfSpeech.City));


		addProduct(rhs, lhs);
		// 中国江苏南京市
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixCity);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.City));


		addProduct(rhs, lhs);

		// 中国江苏南京市南京市
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixCity);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(2, PartOfSpeech.City));

		addProduct(rhs, lhs);
		// 中国江苏南京市栖霞区南京市
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixCity);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(2, PartOfSpeech.City));

		addProduct(rhs, lhs);
		// 广东省东莞市市
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixCity);
		rhs.add(PartOfSpeech.SuffixCity);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.Other));

		addProduct(rhs, lhs);

		// 广东省东莞市市
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Other);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Other));
		lhs.add(new DocSpan(3, PartOfSpeech.Street));

		addProduct(rhs, lhs);

		// 河南省郑州市
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixCity);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.City));

		addProduct(rhs, lhs);

		// =================================以下是县区部分
		// 北京市朝阳区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.County));

		addProduct(rhs, lhs);

		// 北京市大兴区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.County));

		addProduct(rhs, lhs);
		//
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// 中国江苏常州市江苏省常州市武进区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.SuffixProvince);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(2, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// 中国江苏如东县
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);

		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// 中国 江苏 无锡市惠山区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// 广东省广州市白云区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// 东城区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// 近郊密云县
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Other);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Other));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);

		// 中国江苏海安县海安县
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// 中国江苏常州市新北区江苏常州市新北区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// 中国江苏无锡市惠山区无锡市惠山区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Country);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Country));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);

		// 中原区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(3, PartOfSpeech.County));
		addProduct(rhs, lhs);

		// 万江区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(3, PartOfSpeech.County));
		addProduct(rhs, lhs);

		// 道里区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.SuffixStreet);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// 河南新郑机场台商投资区建设路南侧
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixCounty);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.LandMark));
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// 彭水县
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);

		// 市区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.SuffixCity);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// 中国 江苏 南京市 雨花台区铁心桥星河工业园8号
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);
		// 江宁区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);

		// ========================以下是镇乡
		// 石排镇
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixBuildingNo);
		rhs.add(PartOfSpeech.SuffixTown);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(3, PartOfSpeech.Town));
		addProduct(rhs, lhs);

		// 昌平镇
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixTown);
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		addProduct(rhs, lhs);

		// =================================以下是街道号等

		// 惠山经济开发区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixDistrict);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.District));
		addProduct(rhs, lhs);
		// 洛社配套区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixDistrict);
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		addProduct(rhs, lhs);

		// 玄武大道
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		//
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// 2号楼
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.SuffixBuildingUnit);
		lhs.add(new DocSpan(2, PartOfSpeech.BuildingUnit));
		addProduct(rhs, lhs);
		// 东路
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(2, PartOfSpeech.SuffixStreet));
		addProduct(rhs, lhs);
		// 六路
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(2, PartOfSpeech.SuffixStreet));
		addProduct(rhs, lhs);
		// 学院路
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// 精神病医院
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixLandMark);
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// 台城大厦
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.SuffixLandMark);
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// // 文峰大厦
		// lhs = new ArrayList<AddressSpan>();
		// rhs = new ArrayList<AddressType>(); // right-hand .
		//
		// rhs.add(AddressType.County);
		// rhs.add(AddressType.SuffixLandMark);
		// lhs.add(new AddressSpan(2, AddressType.LandMark));
		// addProduct(rhs, lhs);
		// 五公里处
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.SuffixIndicationPosition);
		lhs.add(new DocSpan(2, PartOfSpeech.IndicationPosition));
		addProduct(rhs, lhs);
		// 四幢
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.SuffixBuildingNo);
		lhs.add(new DocSpan(2, PartOfSpeech.BuildingNo));
		addProduct(rhs, lhs);

		// distract
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.DetailDesc);
		rhs.add(PartOfSpeech.SuffixDistrict);
		lhs.add(new DocSpan(3, PartOfSpeech.District));
		addProduct(rhs, lhs);

		// 玉村镇
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Village);
		rhs.add(PartOfSpeech.SuffixTown);
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		addProduct(rhs, lhs);

		// 广东省东莞市长安镇107国道长安酒店斜对面
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixLandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);

		// 江苏省南京市新街口洪武北路青石街24号
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(3, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// 东莞市厚街镇新厚沙路新塘村路口直入出100米
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// 重庆市渝北区两路镇龙兴街84号号码一支路五星小区对面
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// 江苏省南京市高淳县开发区商贸区998号
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixDistrict);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixCounty);
		rhs.add(PartOfSpeech.No);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.SuffixDistrict));
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		lhs.add(new DocSpan(2, PartOfSpeech.No));
		addProduct(rhs, lhs);
		// 广东省东莞市厚街镇家具大道国际家具大道
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// 江苏省南京市江宁区淳化镇淳化居委会
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.End);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(3, PartOfSpeech.DetailDesc));
		lhs.add(new DocSpan(1, PartOfSpeech.End));
		addProduct(rhs, lhs);

		// 海淀区西三环新兴桥西北角(新兴宾馆门口)
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.StartSuffix);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixLandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.LandMark));
		lhs.add(new DocSpan(2, PartOfSpeech.RelatedPos));
		lhs.add(new DocSpan(1, PartOfSpeech.StartSuffix));
		lhs.add(new DocSpan(2, PartOfSpeech.DetailDesc));
		addProduct(rhs, lhs);

		// 朝阳区建国门外永安里新华保险大厦南侧(119中学西侧)
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.LandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.District));
		lhs.add(new DocSpan(1, PartOfSpeech.LandMark));
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);

		// 沙田西太隆工业区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.District);
		lhs.add(new DocSpan(3, PartOfSpeech.District));
		addProduct(rhs, lhs);

		// 东城区
		// lhs = new ArrayList<AddressSpan>();
		// rhs = new ArrayList<AddressType>(); // right-hand .
		//
		// rhs.add(AddressType.RelatedPos);
		// rhs.add(AddressType.District);
		// lhs.add(new AddressSpan(2, AddressType.County));
		// addProduct(rhs, lhs);

		// 东城区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// 大岭山工业区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.SuffixDistrict);
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		addProduct(rhs, lhs);

		// 锦厦新村
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.SuffixVillage);
		lhs.add(new DocSpan(2, PartOfSpeech.Village));
		addProduct(rhs, lhs);

		// 第二工业区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.SuffixDistrict);
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		addProduct(rhs, lhs);

		// 花园新村
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.SuffixVillage);
		lhs.add(new DocSpan(2, PartOfSpeech.Village));
		addProduct(rhs, lhs);

		// 北京市朝阳区霞光里66号远洋新干线A座908室
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.Symbol);
		lhs.add(new DocSpan(1, PartOfSpeech.No));
		lhs.add(new DocSpan(3, PartOfSpeech.LandMark));
		lhs.add(new DocSpan(1, PartOfSpeech.Symbol));
		addProduct(rhs, lhs);
		// 雨花台区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.County);
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);

		// 新寓二村
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.Village);
		lhs.add(new DocSpan(2, PartOfSpeech.Village));
		addProduct(rhs, lhs);

		// 港口路
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.SuffixDistrict);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// //新风中路
		// lhs = new ArrayList<AddressSpan>();
		// rhs = new ArrayList<AddressType>(); // right-hand .
		//
		// rhs.add(AddressType.Unknow);
		// rhs.add(AddressType.RelatedPos);
		// lhs.add(new AddressSpan(2, AddressType.Street));
		// addProduct(rhs, lhs);

		// 学前路
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		//
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// 哈尔滨市哈平路集中区黄海路39号
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.SuffixCounty);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// 广东省东莞市市区红山西路红街二巷9号
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(3, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// 东莞市横沥镇中山路576号
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// 东城区北锣鼓巷沙络胡同7号院(近安定门地铁A口)
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// 东城区北三环和平里东街小街桥北(美廉美东北角)
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.RelatedPos);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.LandMark));
		lhs.add(new DocSpan(1, PartOfSpeech.RelatedPos));
		addProduct(rhs, lhs);

		// 广东省广州市白云区广园中路景泰直街东2巷2号认真英语大厦903
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.RelatedPos));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// 广东省广州市从化市太平镇太平经济技术开发区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.District);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		addProduct(rhs, lhs);

		// 广东省广州市番禺区大石街冼村城岗大街3巷10号
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Village);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.Village));
		lhs.add(new DocSpan(3, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// 海淀区大钟寺四道口路1号(近学院南路)
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.StartSuffix);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.District));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.No));
		lhs.add(new DocSpan(1, PartOfSpeech.StartSuffix));
		lhs.add(new DocSpan(1, PartOfSpeech.RelatedPos));
		lhs.add(new DocSpan(1, PartOfSpeech.SuffixLandMark));
		lhs.add(new DocSpan(1, PartOfSpeech.IndicationPosition));
		addProduct(rhs, lhs);
		// 朝阳区来广营西路88号
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.SuffixCounty);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		addProduct(rhs, lhs);

		// 道镇闸口村东莞电化集团进宝工业区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.SuffixStreet);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.Village);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Village));
		addProduct(rhs, lhs);
		// 道镇闸口村东莞电化集团进宝工业区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.Village);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.District);
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Village));
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		addProduct(rhs, lhs);
		// 江苏省南京市高淳县淳溪镇镇兴路288号
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// 重庆市巫溪县城厢镇镇泉街
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .

		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.Town);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		addProduct(rhs, lhs);
		// 北京市密云县檀营乡二村
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixCounty);
		rhs.add(PartOfSpeech.Town);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		addProduct(rhs, lhs);
		// 重庆市永川市双竹镇石梯坎村
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCity);
		rhs.add(PartOfSpeech.Town);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		addProduct(rhs, lhs);

		// 重庆市合川区市合阳镇文明街97号
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixTown);
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		addProduct(rhs, lhs);

		// 江苏省南京市溧水县大东门街29号3楼
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixBuildingUnit);
		rhs.add(PartOfSpeech.SuffixStreet);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(3, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// 河南省郑州市惠济区桥南新区金桥路2号
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixDistrict);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(3, PartOfSpeech.District));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		// 渝北区龙湖花园美食街
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixLandMark);
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// 北京市房山区韩村河镇韩村河村
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Village);
		rhs.add(PartOfSpeech.SuffixIndicationFacility);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.Village);
		rhs.add(PartOfSpeech.Village);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(3, PartOfSpeech.Town));
		lhs.add(new DocSpan(2, PartOfSpeech.Village));
		addProduct(rhs, lhs);
		// 北京市房山区韩村河镇尤家坟村
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Village);
		rhs.add(PartOfSpeech.SuffixIndicationFacility);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.Village);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(3, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Village));
		addProduct(rhs, lhs);
		// 北京市海淀区罗庄南里3号楼
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.BuildingUnit);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(3, PartOfSpeech.LandMark));
		lhs.add(new DocSpan(1, PartOfSpeech.BuildingUnit));
		addProduct(rhs, lhs);
		// 道镇闸口村东莞电化集团进宝工业区
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.Village);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.District);
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Village));
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		addProduct(rhs, lhs);
		// 鼓楼区草场门大街阳光广场龙江体育馆内地图
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.LandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.LandMark));
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);

		// 广东省东莞市市区红山西路红街二巷9号
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.District));
		lhs.add(new DocSpan(3, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// 广东省广州市白云区机场路新市西街17号
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// 广东省广州市海珠区工业大道南金城一街29号
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.RelatedPos));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// 广东省广州市海珠区泰宁村南晒场2号13B
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixVillage);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Village));
		addProduct(rhs, lhs);
		// 广东省广州市天河区龙口中路3号帝景苑C栋14E房
		// 2010.5.24
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// 海淀区学院路明光北里8号
		// 2010.5.25
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixLandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(3, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);

		// 重庆市渝中区嘉陵江滨江路
		// 2010.5.25
		// lhs = new ArrayList<AddressSpan>();
		// rhs = new ArrayList<AddressType>(); // right-hand .
		// rhs.add(AddressType.Province);
		// rhs.add(AddressType.County);
		// rhs.add(AddressType.County);
		// rhs.add(AddressType.Street);
		// lhs.add(new AddressSpan(1, AddressType.Province));
		// lhs.add(new AddressSpan(1, AddressType.County));
		// lhs.add(new AddressSpan(2, AddressType.Street));
		// addProduct(rhs, lhs);
		// 重庆市沙坪坝区马家岩临江装饰城14-5号
		// 2010.5.25
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.LandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.District));
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// 中国 江苏 无锡市滨湖区 无锡前桥洋溪大桥南（振兴仓储有限公司）
		// 2010.5.25
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.StartSuffix);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.LandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.StartSuffix));
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// 中国 江苏 无锡市北塘区 新兴工业区
		// 2010.5.25
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixDistrict);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.District));
		addProduct(rhs, lhs);
		// 中国 江苏 苏州市吴中区 吴江市盛泽和服商区D幢16号
		// 2010.5.25
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.LandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// 东莞市东城大道方中大厦2楼
		// 2010.5.25
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixStreet);
		rhs.add(PartOfSpeech.LandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// 江苏省南京市玄武区南拘中山东路301号
		// 2010.5.25
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.District));
		lhs.add(new DocSpan(2, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// 河南郑州市河南省郑州市南关街民乐东里38号
		// 2010.5.25
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixLandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(3, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// 广东省东莞市大岭山镇连平下高田村
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.Village);
		rhs.add(PartOfSpeech.Unknow);
		rhs.add(PartOfSpeech.End);
		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.Town));
		lhs.add(new DocSpan(3, PartOfSpeech.Village));
		lhs.add(new DocSpan(1, PartOfSpeech.Unknow));
		lhs.add(new DocSpan(1, PartOfSpeech.End));
		addProduct(rhs, lhs);

		// 东莞市东城区花园新村市场路20号
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixCounty);
		rhs.add(PartOfSpeech.Village);
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Village));
		addProduct(rhs, lhs);
		// 北京市丰台区右安门外玉林里26号楼1单元301室
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.BuildingUnit);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.District));
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		lhs.add(new DocSpan(1, PartOfSpeech.BuildingUnit));
		addProduct(rhs, lhs);

		// 北京市密云县工业开发区
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixCounty);
		rhs.add(PartOfSpeech.SuffixDistrict);
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.SuffixDistrict));
		addProduct(rhs, lhs);
		// 北京市密云县密云镇白檀村
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.Village);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.Village));
		addProduct(rhs, lhs);
		// 朝阳区博大中路荣华桥东(近亦庄)
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.StartSuffix);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.Town);
		lhs.add(new DocSpan(1, PartOfSpeech.StartSuffix));
		lhs.add(new DocSpan(1, PartOfSpeech.RelatedPos));
		lhs.add(new DocSpan(1, PartOfSpeech.DetailDesc));
		addProduct(rhs, lhs);
		// 海淀区学院南路68号吉安大厦C座汇智楼111室
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixLandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(1, PartOfSpeech.No));
		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// 中国 江苏 江阴市 永康五金城大街49-51号
		// 2010.5.26
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCity);
		rhs.add(PartOfSpeech.Street);
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(2, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// 巩义市站街镇粮管所内
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Town);
		rhs.add(PartOfSpeech.SuffixTown);
		rhs.add(PartOfSpeech.LandMark);
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(2, PartOfSpeech.Town));
		lhs.add(new DocSpan(1, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		// 河南省郑州市管城区南五里堡村西堡103号
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Start);
		rhs.add(PartOfSpeech.Province);
		rhs.add(PartOfSpeech.City);
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.SuffixLandMark);
		rhs.add(PartOfSpeech.Village);

		lhs.add(new DocSpan(1, PartOfSpeech.Start));
		lhs.add(new DocSpan(1, PartOfSpeech.Province));
		lhs.add(new DocSpan(1, PartOfSpeech.City));
		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.RelatedPos));
		lhs.add(new DocSpan(3, PartOfSpeech.Village));
		addProduct(rhs, lhs);
		// 鼓楼东街
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.SuffixStreet);

		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// 从化市
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.SuffixCity);
		rhs.add(PartOfSpeech.Street);

		lhs.add(new DocSpan(2, PartOfSpeech.County));
		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		addProduct(rhs, lhs);
		// 北京西站
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixIndicationFacility);

		lhs.add(new DocSpan(2, PartOfSpeech.SuffixIndicationFacility));
		addProduct(rhs, lhs);

		// 西站
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixIndicationFacility);

		lhs.add(new DocSpan(2, PartOfSpeech.SuffixIndicationFacility));
		addProduct(rhs, lhs);

		// 北门
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixBuildingUnit);

		lhs.add(new DocSpan(2, PartOfSpeech.SuffixBuildingUnit));
		addProduct(rhs, lhs);


		//科技大学北门
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.SuffixBuildingUnit);

		lhs.add(new DocSpan(2, PartOfSpeech.BuildingUnit));
		addProduct(rhs, lhs);
		// 一里
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.SuffixLandMark);

		lhs.add(new DocSpan(2, PartOfSpeech.SuffixLandMark));
		addProduct(rhs, lhs);
		// 西桥
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.RelatedPos);
		rhs.add(PartOfSpeech.SuffixLandMark);

		lhs.add(new DocSpan(2, PartOfSpeech.SuffixLandMark));
		addProduct(rhs, lhs);

		//天华园 一里
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.District);
		rhs.add(PartOfSpeech.SuffixLandMark);

		lhs.add(new DocSpan(2, PartOfSpeech.LandMark));
		addProduct(rhs, lhs);
		//东方太阳城社区
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.LandMark);
		rhs.add(PartOfSpeech.SuffixDistrict);

		lhs.add(new DocSpan(2, PartOfSpeech.District));
		addProduct(rhs, lhs);
		//北京市东城区南河沿大街华龙街二段c座一层
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.County);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.Street);

		lhs.add(new DocSpan(1, PartOfSpeech.County));
		lhs.add(new DocSpan(3, PartOfSpeech.Street));
		addProduct(rhs, lhs);

		//11-A
		lhs = new ArrayList<DocSpan>();
		rhs = new ArrayList<PartOfSpeech>(); // right-hand .
		rhs.add(PartOfSpeech.Street);
		rhs.add(PartOfSpeech.No);
		rhs.add(PartOfSpeech.Symbol);

		lhs.add(new DocSpan(1, PartOfSpeech.Street));
		lhs.add(new DocSpan(2, PartOfSpeech.No));
		addProduct(rhs, lhs);*/

    }

    private static UnknowGrammar dicGrammar = new UnknowGrammar();

    /**
     *
     * @return the singleton of basic dictionary
     */
    public static UnknowGrammar getInstance() {
        return dicGrammar;
    }
}
