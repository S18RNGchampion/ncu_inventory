package com.lantu.service;

import com.lantu.common.vo.Result;
import com.lantu.domain.dto.BookInfoDTO;
import com.lantu.domain.po.Bookinfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lantu.domain.vo.BookInfoVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-05-15
 */
public interface IBookinfoService extends IService<Bookinfo> {
    
    Result getBookInfoByNameBarcode(BookInfoDTO bookInfoDTO);
    
    
    Result loadTxtAndGetBookinfo(MultipartFile file , Long pageNo , Long pageSize);
}
