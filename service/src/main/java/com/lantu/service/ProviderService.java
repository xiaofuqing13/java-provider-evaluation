package com.lantu.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lantu.common.Result;
import com.lantu.entity.EvaluationEntity;
import com.lantu.entity.ProviderEntity;
import com.lantu.mapper.EvaluationMapper;
import com.lantu.mapper.ProviderMapper;
import com.lantu.util.WeBASEUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Component
public class ProviderService {
 public static final String userAddress = "0x4706c91af4bc0df6cfc91700cfce07017ba231f9";
@Resource
 private ProviderMapper providerMapper;

public Result add(ProviderEntity providerEntity) {
     List<String> funcParam = new ArrayList<>();
     funcParam.add(providerEntity.getAddress());
     funcParam.add(providerEntity.getName());
     providerEntity.setServiceCount(providerEntity.getServiceCount());

     log.info(String.valueOf(funcParam));
     String result = WeBASEUtils.commonReq(userAddress,"addServiceProvider",funcParam);
     JSONObject jsonObject = JSONObject.parseObject(result);
     log.info(String.valueOf(jsonObject));
     String msg = jsonObject.getString("message");
    if (msg != null && msg.equals("someValue")) {
      providerMapper.insert(providerEntity);
      return Result.success("上链成功");
     }else {
      return Result.error("上链失败");
     }

}

 @Autowired
 private EvaluationMapper evaluationMapper;
 public Result addEvaluation(EvaluationEntity evaluationEntity) {
  List<String> funcParam = new ArrayList<>();

funcParam.add(evaluationEntity.getProviderName());
  funcParam.add(String.valueOf(evaluationEntity.getRating()));
  funcParam.add(evaluationEntity.getComment());

  log.info("Evaluation parameters: " + funcParam);
  String result = WeBASEUtils.commonReq(userAddress, "submitEvaluation", funcParam);
  JSONObject jsonObject = JSONObject.parseObject(result);
  log.info("Evaluation response: " + jsonObject);
  String msg = jsonObject.getString("message");

  if (msg.equals("Success")) {
   // 如果评价上链成功，则将评价信息插入到数据库中
   evaluationMapper.insert(evaluationEntity);
   return Result.success(evaluationEntity);
  } else {
   return Result.error("评价失败");
  }
 }

    public Result getserviceInfo(String name) {
        List<String> funcParam = new ArrayList<>();
        funcParam.add(name);
        log.info(String.valueOf(funcParam));
        String result = WeBASEUtils.commonReq(userAddress, "getServiceInformation", funcParam);
        JSONArray jsonObject = JSONObject.parseArray(result);
        log.info(String.valueOf(jsonObject));
        JSONArray jsonArray = JSONArray.parseArray(result);
        log.info(String.valueOf(jsonObject));
        // 获取姓名、是否信任、总评分和评价数量
        String address = jsonArray.getString(4);
        String trusted = jsonArray.getString(1);
        int totalRating = jsonArray.getIntValue(2);
        int totalRatingsCount = jsonArray.getIntValue(3);
            ProviderEntity providerEntity = new ProviderEntity();
       providerEntity.setAddress(address);
            providerEntity.setName(name);
            providerEntity.setTrusted("true");
            providerEntity.setTotalRating(totalRating);
            providerEntity.setTotalRatingsCount(totalRatingsCount);
        // 更新数据库中对应名字的行
        UpdateWrapper<ProviderEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("name", name);
        providerMapper.update(providerEntity, updateWrapper);
        return Result.success(providerEntity);

    }

    public EvaluationEntity getevaluation(String name){
        List<String> funcParam = new ArrayList<>();
        funcParam.add(name);
        log.info(String.valueOf(funcParam));
        String result = WeBASEUtils.commonReq(userAddress, "getEvaluationInformation", funcParam);
        JSONArray jsonObject = JSONObject.parseArray(result);
        log.info(String.valueOf(jsonObject));
        JSONArray jsonArray = JSONArray.parseArray(result);
        log.info(String.valueOf(jsonObject));
        QueryWrapper<EvaluationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("providerName",name);
        return evaluationMapper.selectOne(queryWrapper);

    }

    public Result setstatus(ProviderEntity providerEntity) {
        List<String> funcParam = new ArrayList<>();
//        funcParam.add(providerEntity.isTrusted());
        funcParam.add(providerEntity.getName());
        funcParam.add(String.valueOf(providerEntity.getTrusted()));


        log.info(String.valueOf(funcParam));
        String result = WeBASEUtils.commonReq(userAddress,"setServiceProviderTrustStatus",funcParam);
        JSONObject jsonObject = JSONObject.parseObject(result);
        log.info(String.valueOf(jsonObject));
        String msg = jsonObject.getString("message");
        if (msg.equals("Success")){
            providerMapper.update(providerEntity, new UpdateWrapper<ProviderEntity>().eq("name", providerEntity.getName()));//更新数据库字段
            return Result.success("上链成功");
        }else {
            return Result.error("上链失败");
        }

    }
    /**
     * 分页查询
     */
    public PageInfo<ProviderEntity> selectPage(ProviderEntity providerEntity, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProviderEntity> list = providerMapper.selectList(null);
        return PageInfo.of(list);
    }
    /**
     * 分页查询2
     */
    public PageInfo<EvaluationEntity> selectPage2(EvaluationEntity evaluationEntity, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EvaluationEntity> list = evaluationMapper.selectList(null);
        return PageInfo.of(list);
    }

    }




