package com.lantu.domain.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lantu.domain.po.Bookinfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookInfoResp {

    private Integer id;

    private String newbarcode;

    private Integer status;

    private Date createdtime;

    private int floorname;

    private String shelf;

    private int rownum;

    private int colnum;

    private String address;

    private String barcode;

    private String propertyNumber;

    private String callNumber;

    private String name;

    private String author;

    private String isbn;

    private String publishingHouse;

    private Date publishYear;

    private BigDecimal fixedPrice;

    private BigDecimal discountedPrice;

    private String shelfNumber;

    private String bookStatus;


    @DateTimeFormat(pattern = "yyyy")
    private LocalDateTime collectionTime;

    private String carrierType;

    private String borrowingAttributes;

    private String annex;

    private String illustrate;

    public void setBookInfo(Bookinfo selectOne) {
        if (selectOne.getAddress() != null) {
            this.setAddress(selectOne.getAddress());
        }
        if (selectOne.getAnnex() != null) {
            this.setAnnex(selectOne.getAnnex());
        }
        if (selectOne.getAuthor() != null) {
            this.setAuthor(selectOne.getAuthor());
        }
        if (selectOne.getBarcode() != null) {
            this.setBarcode(selectOne.getBarcode());
        }
        if (selectOne.getBookStatus() != null) {
            this.setBookStatus(selectOne.getBookStatus());
        }
        if (selectOne.getBorrowingAttributes() != null) {
            this.setBorrowingAttributes(selectOne.getBorrowingAttributes());
        }
        if (selectOne.getCallNumber() != null) {
            this.setCallNumber(selectOne.getCallNumber());
        }
        if (selectOne.getCarrierType() != null) {
            this.setCarrierType(selectOne.getCarrierType());
        }
        if (selectOne.getCollectionTime() != null) {
            this.setCollectionTime(selectOne.getCollectionTime());
        }
        if (selectOne.getDiscountedPrice() != null) {
            this.setDiscountedPrice(selectOne.getDiscountedPrice());
        }
        if (selectOne.getIllustrate() != null) {
            this.setIllustrate(selectOne.getIllustrate());
        }
        if (selectOne.getIsbn() != null) {
            this.setIsbn(selectOne.getIsbn());
        }
        if (selectOne.getName() != null) {
            this.setName(selectOne.getName());
        }
        if (selectOne.getPropertyNumber() != null) {
            this.setPropertyNumber(selectOne.getPropertyNumber());
        }
        if (selectOne.getPublishYear() != null) {
            this.setPublishYear(selectOne.getPublishYear());
        }
        if (selectOne.getShelfNumber() != null) {
            this.setShelfNumber(selectOne.getShelfNumber());
        }
    }

}
