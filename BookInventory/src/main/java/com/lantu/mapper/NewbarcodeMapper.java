package com.lantu.mapper;

import com.lantu.domain.po.Bookinfo;
import com.lantu.domain.po.FloorInventoryStatusCountPo;

import com.lantu.domain.po.FloorShelfStatusCountPo;
import com.lantu.domain.po.Newbarcode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lantu.domain.po.ShelfStatusCountDTO;
import com.lantu.domain.vo.FrameBooksVo;
import com.lantu.domain.vo.SheltInfoVo;
import com.lantu.domain.vo.SummaryInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-05-15
 */
@Mapper
public interface NewbarcodeMapper extends BaseMapper<Newbarcode> {

    void batchInsertNewbarcodes(List<Newbarcode> barcodeList);

    @Select("SELECT MAX(createdtime) FROM ncu_library_bookinfo.newbarcode")
    Date selectMaxCreatedTime();

    @Select("SELECT COUNT(*) FROM ncu_library_bookinfo.newbarcode WHERE createdtime = #{latestTime}")
    Integer selectTotalBooksByTime(Date latestTime);

    @Select("SELECT floorname AS floorId, COUNT(*) AS bookCount " +
            "FROM newbarcode " +
            "WHERE createdtime = #{latestTime} " +  // 排除 null 值
            "AND floorname IS NOT NULL " +
            "GROUP BY floorname")
    List<SummaryInfoVo.FloorBookInfo> selectBooksByFloor(Date latestTime);

    // 根据 floorname 和 createdtime 查询书架数据
    @Select("SELECT shelf AS name, COUNT(*) AS bookCount " +
            "FROM newbarcode " +
            "WHERE floorname = #{floorname} AND createdtime = #{latestTime} " +
            "AND shelf IS NOT NULL " +  // 排除 shelf 为 null 的记录
            "GROUP BY shelf")
    List<SheltInfoVo.Shelf> selectShelvesDataByFloornameAndTime(int floorname, Date latestTime);

    /**
     * 根据楼层名称和书架名称查询书框书籍数据
     *
     * @param floorName 楼层名称
     * @param shelfName 书架名称
     * @return 书框书籍数据列表
     */
    List<FrameBooksVo> selectFrameBooksData(@Param("floorName") int floorName, @Param("shelfName") String shelfName, @Param("latestTime") Date latestTime);


    @Select("SELECT newbarcode FROM ncu_library_bookinfo.newbarcode " +
            "WHERE floorname = #{floorName} " +
            "AND shelf = #{shelfName} " +
            "AND rownum = #{row} " +
            "AND colnum = #{col} " +
            "AND createdtime = #{latestTime} " +
            "ORDER BY id ASC")
    List<String> selectNewbarcodesByLocationAndTime(@Param("floorName") int floorName,
                                                    @Param("shelfName") String shelfName,
                                                    @Param("row") int row,
                                                    @Param("col") int col,
                                                    @Param("latestTime") Date latestTime);

    @Select("select shelf, status, count(*) count from newbarcode where floorname = #{floorNum} and shelf is not null and status is not null group by shelf, status")
    List<ShelfStatusCountDTO> inventoryByFloor(@Param("floorNum") Integer floorNum);

    List<FloorInventoryStatusCountPo> selectFloorInventoryStatus();

    @Select("select shelf from newbarcode where floorname=#{floorNum} and shelf is not null and status is not null group by shelf")
    List<String> getShelvesListByFloorNum(@Param("floorNum") Integer floorNum);

    @Select("select rownum,colnum,status, count(*) as 'count' from newbarcode where floorname=#{floorNum} and shelf=#{shelfName} and status is not null group by rownum,colnum,status")
    List<FloorShelfStatusCountPo> getFloorShelfStatusCount(Integer floorNum, String shelfName);

    @Select("select distinct floorname from newbarcode where floorname is not null order by floorname asc;")
    List<Integer> getFloors();

    @Select("select * from newbarcode where floorname = #{floorNum} and shelf = #{shelfNum} and rownum = #{rowNum} and colnum = #{colNum} order by id asc;")
    List<Newbarcode> inventoryByBookFrame(Integer floorNum, String shelfNum, Integer rowNum, Integer colNum);
}
