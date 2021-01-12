package com.hust.keyRD.system.controller;

import com.hust.keyRD.commons.entities.CommonResult;
import com.hust.keyRD.commons.entities.DataSample;
import com.hust.keyRD.commons.entities.Record;
import com.hust.keyRD.commons.exception.fabric.FabricException;
import com.hust.keyRD.commons.vo.RecordVO;
import com.hust.keyRD.system.api.service.FabricService;

import com.hust.keyRD.system.service.DataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class TraceController {
    @Resource
    FabricService fabricService;
    @Resource
    private DataService dataService;

    //根据文件id获取该文件的最新操作
    @PostMapping(value = "/trace/traceBackward")
    public CommonResult traceBackward(@RequestBody Map<String, String> params){
        String dataId = params.get("dataId");
        Record record = fabricService.traceBackward(dataId);
        RecordVO recordVO = new RecordVO();
        recordVO.setRecord(record);
        DataSample dataSample = dataService.findDataById(Integer.valueOf(record.getDataId()));
        recordVO.setFileName(dataSample.getDataName());
        return new CommonResult<>(200,"溯源成功",recordVO);
    }

    //根据文件id和txId获取该文件的上一次操作
    @PostMapping(value = "/trace/traceBackwardAgain")
    public CommonResult traceBackwardAgain(@RequestBody Map<String, String> params){
        String dataId = params.get("dataId");
        String txId = params.get("txId");
        if(txId.equals("0")) {
            return new CommonResult<>(404,"该记录已经是最早的记录，无更早的记录",null);
        }
        Record record = fabricService.traceBackward(dataId,txId);
        RecordVO recordVO = new RecordVO();
        recordVO.setRecord(record);
        DataSample dataSample = dataService.findDataById(Integer.valueOf(record.getDataId()));
        recordVO.setFileName(dataSample.getDataName());
        return new CommonResult<>(200,"溯源成功",recordVO);
    }
}
