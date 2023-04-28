package com.seeds.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.seeds.admin.dto.request.GameWinRankReq;
import com.seeds.admin.dto.response.GaWinRankExportResp;
import com.seeds.admin.dto.response.GameWinRankResp;
import com.seeds.admin.service.GameRankingService;
import com.seeds.common.dto.GenericDto;
import com.seeds.game.feign.RemoteGameRankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 游戏排行榜管理
 * @author hang.yu
 * @date 2022/04/26
 */
@Service
@Slf4j
public class GameRankingServiceImpl implements GameRankingService {

    @Autowired
    private RemoteGameRankService remoteGameRankService;

    @Override
    public List<GameWinRankResp.GameWinRank> queryList(GameWinRankReq query) {
        GenericDto<List<GameWinRankResp.GameWinRank>> result = remoteGameRankService.winInfo(query);
        return result.getData();
    }

    @Override
    public void export(List<GameWinRankResp.GameWinRank> records, HttpServletResponse response) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        List<GaWinRankExportResp> respList = new ArrayList<>();
        for (GameWinRankResp.GameWinRank rank: records) {
            GaWinRankExportResp resp = new GaWinRankExportResp();
            BeanUtils.copyProperties(rank, resp);
            resp.setAccId(rank.getAccID().toString());
            respList.add(resp);
        }
        String fileName = "Ranking-" + DateUtil.format(new Date(), "MM月dd日HH时mm分") + ".xlsx";
        try {
            //1、设定响应类型
            response.setContentType("application/vnd.ms-excel");
            //2、设定附件的打开方法为：下载，并指定文件名称为fileName.xlsx
            response.setHeader("content-disposition","attachment;filename=" + fileName);
            //3、创建工作簿
            ExcelWriterBuilder writeWork = EasyExcel.write(response.getOutputStream(), GaWinRankExportResp.class);;
            //4、创建表格
            ExcelWriterSheetBuilder sheet = writeWork.sheet();
            sheet.doWrite(respList);
        } catch (IOException e) {
            log.error("文件导出失败，fileName={}", fileName);
        }
    }
}
