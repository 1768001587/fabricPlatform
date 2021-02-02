package com.hust.keyRD.system.controller;

import com.hust.keyRD.commons.entities.CommonResult;
import com.hust.keyRD.commons.entities.DataSample;
import com.hust.keyRD.commons.entities.Record;
import com.hust.keyRD.commons.exception.fabric.FabricException;
import com.hust.keyRD.commons.vo.RecordVO;
import com.hust.keyRD.system.api.Constants.FabricConstant;
import com.hust.keyRD.system.api.service.FabricService;

import com.hust.keyRD.system.service.ChannelService;
import com.hust.keyRD.system.service.DataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
public class TraceController {
    @Resource
    private FabricService fabricService;
    @Resource
    private DataService dataService;
    @Resource
    private ChannelService channelService;

    //根据文件id获取该文件的最新操作
    @PostMapping(value = "/trace/traceBackward")
    public CommonResult traceBackward(@RequestBody Map<String, String> params){
        String dataId = params.get("dataId");
        Integer channelId = dataService.findDataById(Integer.valueOf(dataId)).getChannelId();
        String channelName = channelService.findChannelById(channelId).getChannelName();
        Record record = fabricService.traceBackward(dataId, channelName);
        RecordVO recordVO = new RecordVO();
        recordVO.setRecord(record);
        int i = record.getDataId().indexOf(FabricConstant.Separator);
        DataSample dataSample = dataService.findDataById(Integer.valueOf(record.getDataId().substring(0,i)));
        recordVO.setFileName(dataSample.getDataName());
        return new CommonResult<>(200,"溯源成功",recordVO);
    }

    //根据文件id和txId获取该文件的上一次操作
    @PostMapping(value = "/trace/traceBackwardAgain")
    public CommonResult traceBackwardAgain(@RequestBody Map<String, String> params){
        String dataId = params.get("dataId");
        Integer channelId = dataService.findDataById(Integer.valueOf(dataId)).getChannelId();
        String channelName = channelService.findChannelById(channelId).getChannelName();
        String txId = params.get("txId");
        if(txId.equals("0")) {
            return new CommonResult<>(404,"该记录已经是最早的记录，无更早的记录",null);
        }
        Record record = fabricService.traceBackward(dataId,channelName,txId);
        RecordVO recordVO = new RecordVO();
        recordVO.setRecord(record);
        int i = record.getDataId().indexOf(FabricConstant.Separator);
        DataSample dataSample = dataService.findDataById(Integer.valueOf(record.getDataId().substring(0,i)));
        recordVO.setFileName(dataSample.getDataName());
        return new CommonResult<>(200,"溯源成功",recordVO);
    }
    //一次性获取指定文件所有溯源操作记录
    @PostMapping(value = "/trace/traceBackwardForAll")
    public CommonResult traceBackwardForAll(@RequestBody Map<String, String> params){
        String dataId = params.get("dataId");
        List<Record> result = new LinkedList<>();
        Integer channelId = dataService.findDataById(Integer.valueOf(dataId)).getChannelId();
        String channelName = channelService.findChannelById(channelId).getChannelName();
        Record record = fabricService.traceBackward(dataId, channelName);//这是最新一次的操作记录
        result.add(record);
        Record tmp = record;
        while(!tmp.getLastTxId().equals("0")){
            String thisTxId = tmp.getThisTxId();
            tmp = fabricService.traceBackward(dataId,thisTxId);
            result.add(tmp);
        }
        return new CommonResult<>(200,"获取该文件的所有溯源记录成功",result);
    }

}
