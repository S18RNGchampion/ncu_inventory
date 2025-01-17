package com.lantu.domain.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2024-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ncu_library_bookinfo.bookinfo")
public class Bookinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
    
    private String newbarcode;


}
