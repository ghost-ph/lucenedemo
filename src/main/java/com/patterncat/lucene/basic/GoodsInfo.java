package com.patterncat.lucene.basic;

import java.util.Date;

/**
 * 搜索信息的JavaBean
 *
 * @author wolf
 *
 */
public class GoodsInfo {
    private static final long serialVersionUID = 8888L;
    private String goodsPrice; // 商品价格
    private String goodsName; // 商品名字
    private String goodsNameURL;// 商品名字 点击进入的Url
    private String goodsSaller;// 商品卖家
    private String image;// 商品图片
    private String goodsType;// 商品类型
    private String MfrName;// 工业制作名字
    private String MfrNumber;// 工业制作型号
    private String goodsDescription;// 商品描述
    private long priceInteger;
    private String moneyUnit;// $

    private Date date;// 日期

    public String getMoneyUnit() {
        return moneyUnit;
    }

    public void setMoneyUnit(String MfrName) {
        this.moneyUnit = MfrName;
    }

    public long getPriceInteger() {
        return priceInteger;
    }

    public void setPriceInteger(long PriceInteger) {
        this.priceInteger = PriceInteger;
    }

    public String getMfrName() {
        return MfrName;
    }

    public void setMfrName(String MfrName) {
        this.MfrName = MfrName;
    }

    public String getMfrNumber() {
        return MfrNumber;
    }

    public void setMfrNumber(String MfrNumber) {
        this.MfrNumber = MfrNumber;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    public String getGoodsNameURL() {
        return goodsNameURL;
    }

    public void setGoodsNameURL(String goodsNameURL) {
        this.goodsNameURL = goodsNameURL;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setGoodsSaller(String goodsSaller) {
        this.goodsSaller = goodsSaller;
    }

    public String getGoodsSaller() {
        return goodsSaller;
    }

    /**
     * 覆盖toString()方法
     */
    @Override
    public String toString() {
        String contract = "";
        if (goodsType != null)
            contract += " goodsType:" + goodsType.trim();
        if (goodsName != null && goodsName.trim().length() > 0)
            contract += " goodsName:" + goodsName.trim();
        if (goodsNameURL != null && goodsNameURL.trim().length() > 0)
            contract += " goodsNameURL:" + goodsNameURL.trim();
        if (goodsDescription != null && goodsDescription.trim().length() > 0)
            contract += " goodsDescription:" + goodsDescription.trim();
        if (goodsPrice != null && goodsPrice.trim().length() > 0)
            contract += "goodsPrice:" + goodsPrice.trim();
        if (MfrNumber != null && MfrNumber.trim().length() > 0)
            contract += " MfrNumber:" + MfrNumber.trim();
        if (MfrName != null && MfrName.trim().length() > 0)
            contract += " MfrName:" + MfrName.trim();
        if (goodsSaller != null && goodsSaller.trim().length() > 0)
            contract += "goodsSaller：" + goodsSaller.trim();
        if (image != null && image.trim().length() > 0)
            contract += "image：" + image.trim();
        if (date != null && date.toString().trim().length() > 0)
            contract += "date：" + date.toString().trim();
        contract += "priceInteger：" + String.valueOf(priceInteger);
        if (moneyUnit != null && moneyUnit.trim().length() > 0)
            contract += "moneyUnit：" + moneyUnit.trim();
        return contract;
    }

}
