package com.lantu.mapper;

import com.lantu.domain.po.Bookinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lantu.domain.vo.BookInfoResp;
import com.lantu.domain.vo.BookInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-05-15
 */
@Mapper
public interface BookinfoMapper extends BaseMapper<Bookinfo> {

    List<BookInfoVo> getTxtBookInfo(@Param("newbarcodeList") List<String> newbarcodeList ,@Param("startIndex") Long startIndex, @Param("pageSize") Long pageSize);

//    List<String> selectBookNamesByNewbarcodes(List<String> newbarcodes);
    List<Map<String, String>> selectBookNamesByNewbarcodes(List<String> newbarcodes);

}
