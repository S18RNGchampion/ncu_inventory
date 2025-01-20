package com.lantu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lantu.common.vo.Result;
import com.lantu.domain.po.*;
import com.lantu.domain.vo.*;
import com.lantu.enums.InventoryStatusEnum;
import com.lantu.mapper.BookinfoMapper;
import com.lantu.mapper.NewbarcodeMapper;
import com.lantu.service.INewbarcodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-15
 */
@Service
public class NewbarcodeServiceImpl extends ServiceImpl<NewbarcodeMapper, Newbarcode> implements INewbarcodeService {

    @Autowired
    private NewbarcodeMapper newbarcodeMapper;

    @Autowired
    private BookinfoMapper bookinfoMapper;

    /**
     * 属于 IO 密集型任务
     */
    private final ExecutorService executorService = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() << 1,
            Runtime.getRuntime().availableProcessors() << 2,
            60,
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    public void processFileContent(List<String> newbarcodes) {
        // 当前时间，用于设置条形码的创建时间
        Date currentTime = new Date();
        // 存储所有需要插入数据库的条形码信息
        List<Newbarcode> barcodeList = new ArrayList<>();

        // 定位信息相关变量
        String floor = null, shelf = null, rownum = null, colnum = null; // 楼层、书架编号、行号、列号
        Character startChar = null, endChar = null; // 范围的起点和终点字符（如 A 或 B）
        boolean inRange = false; // 是否正在处理一个范围的标志
        List<String> tempBarcodes = new ArrayList<>(); // 临时存储当前范围内的条形码
        int sequence=1;
        // 遍历输入文件的每一行内容
        for (String line : newbarcodes) {
            // 去除每行的前后空白字符
            line = line.trim();

            // 如果当前行是定位信息（格式如 14#A12#5#1#A）
            if (line.matches("^\\d+#\\w+#\\d+#\\d+#\\w$")) {
                // 分割定位信息字符串，提取具体部分
                String[] parts = line.split("#");
                floor = parts[0];                // 楼层
                shelf = parts[1];                // 书架编号
                rownum = parts[2];               // 行号
                colnum = parts[3];               // 列号
                char currentChar = parts[4].charAt(0); // 最后一部分的字符（A 或 B 等）

                if (!inRange) {
                    // 如果未处于范围内，当前行表示范围的开始
                    startChar = currentChar; // 设置范围的起始字符
                    inRange = true;          // 标记进入范围
                    tempBarcodes.clear();    // 清空临时条形码列表，为新的范围准备
                } else {
                    // 如果已处于范围内，当前行表示范围的结束
                    endChar = currentChar;   // 设置范围的结束字符
                    inRange = false;         // 标记退出范围

                    // 清空该书框内以前的所有条形码信息
                    LambdaQueryWrapper<Newbarcode> lambdaQueryWrapper = Wrappers.lambdaQuery(Newbarcode.class)
                            .eq(Newbarcode::getFloorname, Integer.parseInt(floor))
                            .eq(Newbarcode::getShelf, shelf)
                            .eq(Newbarcode::getRownum, rownum)
                            .eq(Newbarcode::getColnum, colnum);
                    newbarcodeMapper.delete(lambdaQueryWrapper);

                    // 根据范围的起止字符决定条形码插入顺序
                    if (startChar < endChar) {
                        // 如果范围是从 A 到 B，按原顺序插入条形码
                        for (String barcode : tempBarcodes) {
                            insertOrUpdateNewBarcode(barcodeList,barcode,floor,shelf,rownum,colnum,currentTime,sequence);
                            sequence++;
                        }
                    } else {
                        // 如果范围是从 B 到 A，按逆序插入条形码
                        for (int i = tempBarcodes.size() - 1; i >= 0; i--) {
                            insertOrUpdateNewBarcode(barcodeList,tempBarcodes.get(i),floor,shelf,rownum,colnum,currentTime,sequence);
                            sequence++;
                        }
                    }
                }
            } else {
                // 如果当前行是条形码数据
                if (inRange) {
                    // 将条形码添加到临时列表中
                    tempBarcodes.add(line);
                }
            }
        }

        // 处理文件结尾未关闭的范围（例如，最后一个范围没有结束标志）
        if (inRange) {
            if (startChar > endChar) {
                // 按顺序插入剩余条形码
                for (String barcode : tempBarcodes) {
                    insertOrUpdateNewBarcode(barcodeList,barcode,floor,shelf,rownum,colnum,currentTime,sequence);
                }
            } else {
                // 按逆序插入剩余条形码
                for (int i = tempBarcodes.size() - 1; i >= 0; i--) {
                    insertOrUpdateNewBarcode(barcodeList,tempBarcodes.get(i),floor,shelf,rownum,colnum,currentTime,sequence);
                }
            }
        }

        // 批量将所有条形码插入数据库
        newbarcodeMapper.batchInsertNewbarcodes(barcodeList);
    }

    /**
     * 处理每一个barcode
     * 如果该barcode已经存在，则更新状态和书架位置信息
     * 如果不存在则加入插入列表，后续批量插入
     * @param barcodeList
     * @param barcode
     * @param floor
     * @param shelf
     * @param rownum
     * @param colnum
     * @param currentTime
     */
    private void insertOrUpdateNewBarcode(List<Newbarcode> barcodeList,
                                          String barcode,
                                          String floor,
                                          String shelf,
                                          String rownum,
                                          String colnum,
                                          Date currentTime,
                                          int sequence
    ) {
        // 确定状态
        Integer status;
        if (barcode.contains("error")) {
            // 识别错误状态直接插入
            status = InventoryStatusEnum.errorStatus.getStatus();
            Newbarcode newbarcode = Newbarcode.builder()
                    .newbarcode(barcode)
                    .status(status)
                    .floorname(Integer.parseInt(floor))
                    .shelf(shelf)
                    .rownum(Integer.parseInt(rownum))
                    .colnum(Integer.parseInt(colnum))
                    .createdtime(currentTime)
                    .sequence(sequence)
                    .build();
            newbarcodeMapper.insert(newbarcode);
            return;
        } else {
            LambdaQueryWrapper<Bookinfo> queryWrapper = Wrappers.lambdaQuery(Bookinfo.class)
                    .eq(Bookinfo::getNewbarcode, barcode);
            Bookinfo selectOne = bookinfoMapper.selectOne(queryWrapper);
            if (selectOne != null) {
                status = InventoryStatusEnum.matchStatus.getStatus();
            } else {
                status = InventoryStatusEnum.notMatchStatus.getStatus();
            }
        }
        LambdaQueryWrapper<Newbarcode> newbarcodeLambdaQueryWrapper = Wrappers.lambdaQuery(Newbarcode.class)
                .eq(Newbarcode::getNewbarcode, barcode);
        Newbarcode one = newbarcodeMapper.selectOne(newbarcodeLambdaQueryWrapper);
        if (one != null){
            // 如果条形码重复
            LambdaUpdateWrapper<Newbarcode> newbarcodeLambdaUpdateWrapper = Wrappers.lambdaUpdate(Newbarcode.class)
                    .eq(Newbarcode::getNewbarcode, barcode)
                    .set(Newbarcode::getStatus, status)
                    .set(Newbarcode::getFloorname, Integer.parseInt(floor))
                    .set(Newbarcode::getShelf, shelf)
                    .set(Newbarcode::getRownum, Integer.parseInt(rownum))
                    .set(Newbarcode::getColnum, Integer.parseInt(colnum))
                    .set(Newbarcode::getCreatedtime, currentTime)
                    .set(Newbarcode::getSequence, sequence);
            newbarcodeMapper.update(null, newbarcodeLambdaUpdateWrapper);
        }else {
            Newbarcode newbarcode = Newbarcode.builder()
                    .newbarcode(barcode)
                    .status(status)
                    .floorname(Integer.parseInt(floor))
                    .shelf(shelf)
                    .rownum(Integer.parseInt(rownum))
                    .colnum(Integer.parseInt(colnum))
                    .createdtime(currentTime)
                    .sequence(sequence)
                    .build();
            barcodeList.add(newbarcode);
        }
    }

    /**
     * 辅助方法：将条形码及其相关信息添加到条形码列表
     *
     * @param barcodeList 最终插入数据库的条形码列表
     * @param barcode     当前条形码
     * @param floor       楼层信息
     * @param shelf       书架编号
     * @param rownum      行号
     * @param colnum      列号
     * @param currentTime 当前时间戳
     */
    private void addToBarcodeList(List<Newbarcode> barcodeList, String barcode,
                                  String floor, String shelf, String rownum, String colnum, Date currentTime) {
        // 创建一个 Newbarcode 对象并设置其字段
        Newbarcode newbarcode = new Newbarcode();
        newbarcode.setNewbarcode(barcode);               // 设置条形码内容
        newbarcode.setFloorname(Integer.parseInt(floor)); // 楼层号（字符串转为整数）
        newbarcode.setShelf(shelf);                     // 设置书架编号
        newbarcode.setRownum(Integer.parseInt(rownum)); // 行号（字符串转为整数）
        newbarcode.setColnum(Integer.parseInt(colnum)); // 列号（字符串转为整数）
        newbarcode.setCreatedtime(currentTime);         // 设置创建时间
        // 将新条形码对象添加到列表中
        barcodeList.add(newbarcode);
    }


//    @Override
//    public void processFileContent(List<String> newbarcodes) {
//
//        // 获取当前时间
//        Date currentTime = new Date();
//
//        // 创建 Newbarcode 对象列表
//        List<Newbarcode> barcodeList = new ArrayList<>();
//        for (String barcode : newbarcodes) {
//            Newbarcode newbarcode = new Newbarcode();
//            newbarcode.setNewbarcode(barcode); // 设置条形码
//            newbarcode.setCreatedtime(currentTime);  // 设置当前时间戳
//            barcodeList.add(newbarcode);
//        }
//
//        //批量插入
//        newbarcodeMapper.batchInsertNewbarcodes(barcodeList);
//
//    }

    @Override
    public SummaryInfoVo getSummaryBookData() {
        // 获取总书籍数（假设这里用一个固定的值，实际上你可以从数据库获取）
        int totalBooks = getTotalBooks();

        // 获取各楼层的书籍信息
        List<SummaryInfoVo.FloorBookInfo> floors = getBooksByFloor();

        // 创建并返回 SummaryInfoVo 对象
        return new SummaryInfoVo(totalBooks, floors);
    }

    @Override
    public SheltInfoVo getShelvesData(int floorname) {
        try {
            // 1. 查询数据库中 createdtime 的最大值（即最后一次的时间）
            Date latestTime = newbarcodeMapper.selectMaxCreatedTime();

            // 2. 如果获取到了最新时间，则根据 floorname 和最新的 createdtime 查询数据
            if (latestTime != null) {
                // 查询书架数据
                List<SheltInfoVo.Shelf> shelves = newbarcodeMapper.selectShelvesDataByFloornameAndTime(floorname, latestTime);

                // 创建 SheltInfoVo 对象并设置数据
                SheltInfoVo sheltInfoVo = new SheltInfoVo();
                sheltInfoVo.setFloorname(floorname);
                sheltInfoVo.setShelves(shelves);

                return sheltInfoVo;
            } else {
                // 如果没有找到最大时间，返回一个空的 SheltInfoVo 或者默认值
                SheltInfoVo emptyResult = new SheltInfoVo();
                emptyResult.setFloorname(floorname);
                emptyResult.setShelves(Collections.emptyList());  // 返回空书架列表
                return emptyResult;
            }
        } catch (Exception e) {
            // 捕获任何异常，打印日志并返回 null 或自定义的错误信息
            e.printStackTrace();  // 记录异常信息
            return null;
        }
    }

    @Override
    public List<FrameBooksVo> getFrameBooksData(int floorName, String shelfName) {
        try {
            Date latestTime = newbarcodeMapper.selectMaxCreatedTime();
            // 调用 Mapper 方法获取书框书籍数据
            List<FrameBooksVo> frameBooksData = newbarcodeMapper.selectFrameBooksData(floorName, shelfName, latestTime);
            System.out.println("Fetched frameBooksData: " + frameBooksData); // 输出数据
            return frameBooksData;
        } catch (Exception e) {
            e.printStackTrace();
            // 如果查询出现异常，返回空列表或可以考虑返回错误信息
            return Collections.emptyList();
        }
    }

    //    @Override
//    public List<String> getBooksDetailsData(int floorName, String shelfName, int row, int col) {
//        try {
//            // 1. 查询数据库中 createdtime 的最大值（即最后一次的时间）
//            Date latestTime = newbarcodeMapper.selectMaxCreatedTime();
//            System.out.println("Latest created time: " + latestTime);
//
//            // 2. 查询最大时间下，符合条件的条形码（newbarcode）
//            List<String> newbarcodes = newbarcodeMapper.selectNewbarcodesByLocationAndTime(floorName, shelfName, row, col, latestTime);
//            if (newbarcodes.isEmpty()) {
//                return Collections.emptyList();  // 如果没有找到条形码，返回空列表
//            }
//            // 3. 使用这些 `newbarcode` 查询 `bookinfo` 表中的书名
//            List<String> bookNames = bookinfoMapper.selectBookNamesByNewbarcodes(newbarcodes);
//
//            return bookNames;
//        } catch (Exception e) {
//            e.printStackTrace();
//            // 如果查询出现异常，返回空列表
//            return Collections.emptyList();
//        }
//    }
    public List<String> getBooksDetailsData(int floorName, String shelfName, int row, int col) {
        try {
            // 1. 查询数据库中 createdtime 的最大值（即最后一次的时间）
            Date latestTime = newbarcodeMapper.selectMaxCreatedTime();
            System.out.println("Latest created time: " + latestTime);

            // 2. 查询最大时间下，符合条件的条形码（newbarcode）
            List<String> newbarcodes = newbarcodeMapper.selectNewbarcodesByLocationAndTime(floorName, shelfName, row, col, latestTime);
            if (newbarcodes.isEmpty()) {
                return Collections.emptyList();  // 如果没有找到条形码，返回空列表
            }

            // 3. 使用这些 newbarcode 查询 bookinfo 表中的书名
            List<Map<String, String>> bookInfos = bookinfoMapper.selectBookNamesByNewbarcodes(newbarcodes);

            // 4. 创建返回结果列表
            List<String> result = new ArrayList<>(newbarcodes.size());

            // 5. 创建一个 Map 用于存储查询到的书籍信息，key 为条形码，value 为书名
            Map<String, String> barcodeToName = new HashMap<>();

            // 6. 将查询到的书籍信息存入 barcodeToName 中
            for (Map<String, String> bookInfo : bookInfos) {
                String barcode = bookInfo.get("newbarcode");
                String name = bookInfo.get("name");
                if (name != null) {
                    barcodeToName.put(barcode, name);  // 存储条形码和书籍名
                }
            }

            // 7. 遍历 newbarcodes 按顺序添加结果
            for (String barcode : newbarcodes) {
                String name = barcodeToName.get(barcode);  // 获取书籍名
                if (name != null) {
                    result.add(name);  // 如果有书籍信息，加入书籍名
                } else {
                    result.add(barcode + " 没有对应书籍信息");  // 否则加入条形码和提示信息
                }
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            // 如果查询出现异常，返回空列表
            return Collections.emptyList();
        }
    }


    @Override
    public List<InventoryFloorVo> inventoryByFloor(Integer floorNum) {
        List<ShelfStatusCountDTO> shelfStatusCountDTOList = newbarcodeMapper.inventoryByFloor(floorNum);
        List<InventoryFloorVo> floorVoList = new ArrayList<>();
        Map<String, List<ShelfStatusCountDTO>> shelfMap = shelfStatusCountDTOList.stream()
                .collect(Collectors.groupingBy(ShelfStatusCountDTO::getShelf));
        Set<Map.Entry<String, List<ShelfStatusCountDTO>>> entrySet = shelfMap.entrySet();
        entrySet.forEach(entry -> {
            StatusNum statusNum = new StatusNum();
            for (ShelfStatusCountDTO shelfStatusCountDTO : entry.getValue()) {
                // 4个 if 只可能进入一个
                if (shelfStatusCountDTO.getStatus() == InventoryStatusEnum.errorStatus.getStatus()) {
                    statusNum.setErrorStatusNum(shelfStatusCountDTO.getCount());
                }
                if (shelfStatusCountDTO.getStatus() == InventoryStatusEnum.matchStatus.getStatus()) {
                    statusNum.setMatchStatusNum(shelfStatusCountDTO.getCount());
                }
                if (shelfStatusCountDTO.getStatus() == InventoryStatusEnum.notMatchStatus.getStatus()) {
                    statusNum.setNotMatchStatusNum(shelfStatusCountDTO.getCount());
                }
                if (shelfStatusCountDTO.getStatus() == InventoryStatusEnum.fixedMatchStatus.getStatus()) {
                    statusNum.setFixedMatchStatusNum(shelfStatusCountDTO.getCount());
                }
            }
            InventoryFloorVo inventoryFloorVo = InventoryFloorVo.builder()
                    .shelfNum(entry.getKey())
                    .statusNum(statusNum)
                    .build();
            floorVoList.add(inventoryFloorVo);
        });
        return floorVoList;

    }


    private List<SummaryInfoVo.FloorBookInfo> getBooksByFloor() {
        // 1. 查询出数据库中 createdtime 的最大值（即最后一次的时间）
        Date latestTime = newbarcodeMapper.selectMaxCreatedTime();
        // 调用 Mapper 方法查询各楼层的书籍数量
        return newbarcodeMapper.selectBooksByFloor(latestTime);
    }

    private int getTotalBooks() {
        // 1. 查询出数据库中 createdtime 的最大值（即最后一次的时间）
        Date latestTime = newbarcodeMapper.selectMaxCreatedTime();
        // 2. 根据最大时间查询总数（可以根据 latestTime 查询符合条件的数据的数量）
        if (latestTime != null) {
            // 使用最大时间查询符合条件的书籍总数
            return newbarcodeMapper.selectTotalBooksByTime(latestTime);
        } else {
            // 如果没有找到最大时间，可能意味着没有记录，返回 0 或其他合适的值
            return 0;
        }
    }

    @Override
    public StatusNum getTotalStatusNum() {
        LambdaQueryWrapper<Newbarcode> matchWrapper = Wrappers.lambdaQuery();
        LambdaQueryWrapper<Newbarcode> notMatchWrapper = Wrappers.lambdaQuery();
        LambdaQueryWrapper<Newbarcode> fixedMatchWrapper = Wrappers.lambdaQuery();
        LambdaQueryWrapper<Newbarcode> errorWrapper = Wrappers.lambdaQuery();
        errorWrapper.eq(Newbarcode::getStatus, InventoryStatusEnum.errorStatus.getStatus());
        matchWrapper.eq(Newbarcode::getStatus, InventoryStatusEnum.matchStatus.getStatus());
        notMatchWrapper.eq(Newbarcode::getStatus, InventoryStatusEnum.notMatchStatus.getStatus());
        fixedMatchWrapper.eq(Newbarcode::getStatus, InventoryStatusEnum.fixedMatchStatus.getStatus());
        Long errorCount = newbarcodeMapper.selectCount(errorWrapper);
        Long notMatchCount = newbarcodeMapper.selectCount(notMatchWrapper);
        Long fixedMatchCount = newbarcodeMapper.selectCount(fixedMatchWrapper);
        Long matchCount = newbarcodeMapper.selectCount(matchWrapper);
        StatusNum statusNum = new StatusNum();
        statusNum.setErrorStatusNum(errorCount);
        statusNum.setNotMatchStatusNum(notMatchCount);
        statusNum.setFixedMatchStatusNum(fixedMatchCount);
        statusNum.setMatchStatusNum(matchCount);
        return statusNum;
    }


    public Map<Integer, StatusNum> statisticFloorStatus() {
        List<FloorInventoryStatusCountPo> floorStatusCountPos = newbarcodeMapper.selectFloorInventoryStatus();
        Map<Integer, StatusNum> statusNumMap = new HashMap<>();

        for (FloorInventoryStatusCountPo floorStatusCountPo : floorStatusCountPos) {
            Integer floorname = floorStatusCountPo.getFloorname();
            StatusNum statusNum = statusNumMap.get(floorname);
            if (statusNum == null) {
                statusNum = new StatusNum();
                statusNumMap.put(floorname, statusNum);
            }

            switch (floorStatusCountPo.getStatus()) {
                case 1:
                    statusNum.setMatchStatusNum((long) floorStatusCountPo.getStatus_count());
                    break;
                case 2:
                    statusNum.setNotMatchStatusNum((long) floorStatusCountPo.getStatus_count());
                    break;
                case 3:
                    statusNum.setFixedMatchStatusNum((long) floorStatusCountPo.getStatus_count());
                    break;
                case 0:
                    statusNum.setErrorStatusNum((long) floorStatusCountPo.getStatus_count());
                    break;
            }

        }
        return statusNumMap;
    }

    @Override
    public Result<List<String>> getShelvesList(Integer floorNum) {
        List<String> shelfByFloorNum = newbarcodeMapper.getShelvesListByFloorNum(floorNum);
        return Result.success(shelfByFloorNum);
    }

    public Result<List<FloorShelfStatusVo>> getFloorShelfInventoryStatus(Integer floorNum, String floorName) {
        // 从数据库获取原始统计数据
        List<FloorShelfStatusCountPo> floorShelfStatusCountPoList = newbarcodeMapper.getFloorShelfStatusCount(floorNum, floorName);

        // 使用 Map 进行动态分组
        Map<String, FloorShelfStatusVo> groupedData = new HashMap<>();

        for (FloorShelfStatusCountPo po : floorShelfStatusCountPoList) {
            // 组合行列号作为唯一键
            String key = po.getRownum() + "-" + po.getColnum();

            // 如果 Map 中不存在该货架，初始化并添加
            groupedData.putIfAbsent(key, new FloorShelfStatusVo());
            FloorShelfStatusVo floorShelfStatusVo = groupedData.get(key);

            // 设置行号和列号（只需设置一次）
            floorShelfStatusVo.setRownum(po.getRownum());
            floorShelfStatusVo.setColnum(po.getColnum());

            // 获取状态统计对象
            StatusNum statusNum = floorShelfStatusVo.getStatusNum();
            if (statusNum == null) {
                statusNum = new StatusNum();
                floorShelfStatusVo.setStatusNum(statusNum);
            }

            // 根据状态累加统计
            switch (po.getStatus()) {
                case 1:
                    statusNum.setMatchStatusNum(statusNum.getMatchStatusNum() + po.getCount());
                    break;
                case 2:
                    statusNum.setNotMatchStatusNum(statusNum.getNotMatchStatusNum() + po.getCount());
                    break;
                case 3:
                    statusNum.setFixedMatchStatusNum(statusNum.getFixedMatchStatusNum() + po.getCount());
                    break;
                case 0:
                    statusNum.setErrorStatusNum(statusNum.getErrorStatusNum() + po.getCount());
                    break;
            }
        }

        // 将分组结果转为列表返回
        return Result.success(new ArrayList<>(groupedData.values()));
    }

    @Override
    public List<Integer> getFloors() {
        List<Integer> floors =  newbarcodeMapper.getFloors();
        return floors;
    }

    @Override
    public List<BooksVo> inventoryByBookFrame(Integer floorNum, String shelfNum, Integer rowNum, Integer colNum) {
        List<Newbarcode> bookInfoList = newbarcodeMapper.inventoryByBookFrame(floorNum, shelfNum, rowNum, colNum);
        List<BooksVo> bookInfoRespList = bookInfoList.stream()
                .map(each -> BeanUtil.toBean(each, BooksVo.class))
                .collect(Collectors.toList());

        List<Future<Void>> futures = new ArrayList<>();
        bookInfoRespList.forEach(e -> {
            Future<Void> future = executorService.submit(() -> {
                String bookName = bookinfoMapper.getNameBynewBarcode(e.getNewbarcode());
                e.setName(bookName);
                return null;
            });
            futures.add(future);
        });
        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("获取图书名异常", e);
            }
        }
        return bookInfoRespList;
    }

    @Override
    public BookInfoResp getBookDetailByNewBarcode(Integer newBarcodeId) {
        LambdaQueryWrapper<Newbarcode> newbarcodeLambdaQueryWrapper = Wrappers.lambdaQuery(Newbarcode.class)
                .eq(Newbarcode::getId, newBarcodeId);
        Newbarcode newbarcode = newbarcodeMapper.selectOne(newbarcodeLambdaQueryWrapper);
        BookInfoResp bookInfoResp = BeanUtil.toBean(newbarcode, BookInfoResp.class);
        LambdaQueryWrapper<Bookinfo> queryWrapper = Wrappers.lambdaQuery(Bookinfo.class)
                .eq(Bookinfo::getNewbarcode, newbarcode.getNewbarcode());
        Bookinfo bookinfo = bookinfoMapper.selectOne(queryWrapper);
        bookInfoResp.setBookInfo(bookinfo);
        return bookInfoResp;
    }
}
