package com.patterncat.lucene.newsExtract;

/**
 * 转移概率
 */
public class ContextStatDoc {

    private static int[][] transProbs;

    private static ContextStatDoc cs = new ContextStatDoc();

    public static ContextStatDoc getInstance() {
        return cs;
    }

    private ContextStatDoc() {
        int matrixLength = DocType.values().length;
        transProbs = new int[matrixLength][];

        for (int i = 0; i < DocType.values().length; ++i) {
            transProbs[i] = new int[matrixLength];
        }
		/*addTrob(DocType.Start, DocType.Street, 10000000);
		addTrob(DocType.Start, DocType.LandMark, 1000000);
		addTrob(DocType.Start, DocType.District, 100000000);
		addTrob(DocType.Start, DocType.Village, 1000000);
		addTrob(DocType.Start, DocType.Town, 100000);
		addTrob(DocType.Start, DocType.City, 1000000);
		addTrob(DocType.Start, DocType.County, 1000000);
		addTrob(DocType.Start, DocType.Country, 1000000);
		addTrob(DocType.Start, DocType.Province, 1000000);
		addTrob(DocType.Start, DocType.Municipality, 1000000);

		addTrob(DocType.Province, DocType.City, 100000000);
		addTrob(DocType.Province, DocType.County, 1000000);
		addTrob(DocType.Province, DocType.Town, 10);
		addTrob(DocType.Province, DocType.End, 10000000);
		addTrob(DocType.Province, DocType.SuffixMunicipality, -10000000);
		addTrob(DocType.Municipality, DocType.District, 1000000);
		addTrob(DocType.Municipality, DocType.County, 10000000);
		addTrob(DocType.Municipality, DocType.Street, 10000);


		addTrob(DocType.Municipality, DocType.End, 10000);
		addTrob(DocType.City, DocType.County, 10000000);
		addTrob(DocType.City, DocType.Town, 10000000);
		addTrob(DocType.City, DocType.City, 1000000);
		addTrob(DocType.City, DocType.Street, 100000000);
		addTrob(DocType.City, DocType.District, 10000000);
		addTrob(DocType.City, DocType.LandMark, 1000000);
		addTrob(DocType.City, DocType.Country, 1000);
		addTrob(DocType.City, DocType.End, 1000);
		addTrob(DocType.City, DocType.SuffixCity, -1000000);
		addTrob(DocType.City, DocType.SuffixLandMark, -100000);
		addTrob(DocType.City, DocType.SuffixStreet, -100000);
		addTrob(DocType.City, DocType.SuffixCounty, -100000);
		addTrob(DocType.County, DocType.Village, 10000);
		addTrob(DocType.County, DocType.Town, 100000);
		addTrob(DocType.County, DocType.Province, 100);
		addTrob(DocType.County, DocType.Street, 100000000);
		addTrob(DocType.County, DocType.District, 10000000);
		addTrob(DocType.County, DocType.County, -900000000);
		addTrob(DocType.County, DocType.Other, -1000);
		addTrob(DocType.County, DocType.Unknow, -10000000);
		addTrob(DocType.County, DocType.End, 10000);
		addTrob(DocType.County, DocType.LandMark, 1000000000);
		addTrob(DocType.County, DocType.SuffixDistrict, -1000000000);
		addTrob(DocType.County, DocType.City, -1000000000);
		addTrob(DocType.County, DocType.SuffixCity, -1000000000);

		addTrob(DocType.RelatedPos, DocType.County, -1000);
		addTrob(DocType.RelatedPos, DocType.LandMark, 100000);
		addTrob(DocType.RelatedPos, DocType.End, 10);
		addTrob(DocType.RelatedPos, DocType.Municipality, 100);
		addTrob(DocType.Street, DocType.SuffixLandMark, 100);

		addTrob(DocType.Town, DocType.Village, 10000000);
		addTrob(DocType.Town, DocType.LandMark, 10000000);
		addTrob(DocType.Town, DocType.District, 10000000);
		addTrob(DocType.Town, DocType.Street, 100000000);
		addTrob(DocType.Town, DocType.SuffixLandMark, 10000000);
		addTrob(DocType.Town, DocType.End, 100);
		addTrob(DocType.Town, DocType.Town, 1);
		addTrob(DocType.Town, DocType.RelatedPos, 10000000);
		addTrob(DocType.Town, DocType.Village, 100000000);
		addTrob(DocType.Town, DocType.SuffixCounty, -100000000);
		addTrob(DocType.StreetNo, DocType.BuildingUnit, 10);
		addTrob(DocType.StreetNo, DocType.LandMark, 10000000);
		addTrob(DocType.StreetNo, DocType.BuildingNo, 100);
		addTrob(DocType.StreetNo, DocType.City, 10000);
		addTrob(DocType.No, DocType.District, 10000000);
		addTrob(DocType.No, DocType.RelatedPos, 10000);
		addTrob(DocType.No, DocType.LandMark, 10000000);
		addTrob(DocType.No, DocType.Street, 10000000);
		addTrob(DocType.No, DocType.City, -10000000);
		addTrob(DocType.No, DocType.SuffixBuildingUnit, 10000000);

		addTrob(DocType.Street, DocType.City, -1000000000);
		addTrob(DocType.Street, DocType.County, -10000000);
		addTrob(DocType.Street, DocType.Town, -1000000);
		addTrob(DocType.Street, DocType.Street, 1000000000);
		addTrob(DocType.Street, DocType.SuffixStreet, 10000);
		addTrob(DocType.Street, DocType.LandMark, 10000000);
		addTrob(DocType.Street, DocType.No, 10000);
		addTrob(DocType.Street, DocType.End, 1000000);
		addTrob(DocType.Street, DocType.City, -1000000);
		addTrob(DocType.Village, DocType.District, 1000);
		addTrob(DocType.Village, DocType.End, 100);
		addTrob(DocType.Village, DocType.Street, 100);
		addTrob(DocType.Village, DocType.LandMark, 100);
		addTrob(DocType.LandMark, DocType.No, 10000);
		addTrob(DocType.LandMark, DocType.End, 10000);
		addTrob(DocType.LandMark, DocType.Street, 10000000);
		addTrob(DocType.LandMark, DocType.StartSuffix, 10000000);
		addTrob(DocType.LandMark, DocType.RelatedPos, 100000);
		addTrob(DocType.LandMark, DocType.City, -900000000);
		addTrob(DocType.LandMark, DocType.County, -900000000);

		addTrob(DocType.BuildingUnit, DocType.District, 100000);
		addTrob(DocType.BuildingUnit, DocType.Province, 10);
		addTrob(DocType.BuildingUnit, DocType.Street, 10);
		addTrob(DocType.District, DocType.LandMark, 1000000);
		addTrob(DocType.District, DocType.City, -10000);
		addTrob(DocType.District, DocType.Village, 1000000);
		addTrob(DocType.District, DocType.Street, 1000000);
		addTrob(DocType.District, DocType.County, -1000000);
		addTrob(DocType.District, DocType.District, 1000000);
		addTrob(DocType.District, DocType.End, 1000000);
		addTrob(DocType.SuffixBuildingNo, DocType.County, 10000);
		addTrob(DocType.IndicationFacility, DocType.IndicationPosition,
				10000);
		addTrob(DocType.IndicationFacility, DocType.RelatedPos,
				100000000);
		addTrob(DocType.IndicationPosition, DocType.LandMark, 100000000);
		addTrob(DocType.Unknow, DocType.SuffixLandMark, 100000);
		addTrob(DocType.Unknow, DocType.Street, 100000);
		addTrob(DocType.Unknow, DocType.SuffixStreet, 100000);
		addTrob(DocType.Unknow, DocType.City, -1000000000);

		addTrob(DocType.SuffixLandMark, DocType.No, 100000);
		addTrob(DocType.SuffixLandMark, DocType.RelatedPos, 100000);
		addTrob(DocType.SuffixLandMark, DocType.County, -10000);

		addTrob(DocType.County, DocType.SuffixLandMark, -10000);
		addTrob(DocType.County, DocType.County, -10000);
		addTrob(DocType.County, DocType.SuffixCounty, -100000);
		addTrob(DocType.Municipality, DocType.SuffixCity, -100000);
		addTrob(DocType.Municipality, DocType.SuffixStreet, -100000);
		addTrob(DocType.Municipality, DocType.City, -1000000000);
		addTrob(DocType.County, DocType.SuffixStreet, -100000);
		addTrob(DocType.Unknow, DocType.Town, -100000);
		addTrob(DocType.Unknow, DocType.County, -100000);
		addTrob(DocType.Other, DocType.LandMark, 100000000);

		addTrob(DocType.SuffixCity, DocType.SuffixLandMark, -100000);
		addTrob(DocType.City, DocType.SuffixCity, -100000);
		addTrob(DocType.No, DocType.SuffixLandMark, 100000);

		addTrob(DocType.SuffixDistrict, DocType.County, -1000);
		addTrob(DocType.Street, DocType.County, -100000000);
		addTrob(DocType.SuffixCity, DocType.Street, 1000000000);
		addTrob(DocType.SuffixCounty, DocType.Town, 1000000);
		addTrob(DocType.SuffixCounty, DocType.Street, 1000000000);
		addTrob(DocType.SuffixCounty, DocType.County, -100000000);
		addTrob(DocType.SuffixCounty, DocType.City, -10000000);
		addTrob(DocType.SuffixStreet, DocType.City, -100000);
		addTrob(DocType.SuffixCounty, DocType.Village, 100000000);
		addTrob(DocType.SuffixCounty, DocType.Street, 10000000);
		addTrob(DocType.SuffixCounty, DocType.District, 100000000);
		addTrob(DocType.SuffixCounty, DocType.LandMark, 1000000000);
		addTrob(DocType.SuffixTown, DocType.Town, -100000000);
		addTrob(DocType.SuffixTown, DocType.County, -100000000);
		addTrob(DocType.SuffixTown, DocType.Village, 100000000);
		addTrob(DocType.SuffixTown, DocType.RelatedPos, -10000000);
		addTrob(DocType.SuffixTown, DocType.City, -100000);

		addTrob(DocType.LandMark, DocType.SuffixCounty, -900000000);
		addTrob(DocType.LandMark, DocType.SuffixCity, -900000000);
		addTrob(DocType.SuffixLandMark, DocType.County, -100000);
		addTrob(DocType.SuffixLandMark, DocType.Street, 10000000);
		addTrob(DocType.StartSuffix, DocType.LandMark, 20000);
		addTrob(DocType.StartSuffix, DocType.Town, -10000000);*/
    }

    public void addTrob(DocType prev, DocType cur, int val) {
        transProbs[prev.ordinal()][cur.ordinal()] = val;
    }

    /**
     * get context possibility
     *
     * @param nPrev
     *            the previous POS
     * @param nCur
     *            the current POS
     * @return the context possibility between two POSs
     */
    public int getContextPossibility(DocType prev, DocType cur) {
        return transProbs[prev.ordinal()][cur.ordinal()];
    }

    public static int[][] getTransProbs() {
        return transProbs;
    }
}
