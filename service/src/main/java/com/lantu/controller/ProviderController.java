package com.lantu.controller;


import com.github.pagehelper.PageInfo;
import com.lantu.common.Result;
import com.lantu.entity.EvaluationEntity;
import com.lantu.entity.ProviderEntity;
import com.lantu.service.ProviderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@RequestMapping("/provider")
public class ProviderController {
    @Resource
    private ProviderService providerService;

    @PostMapping("/addservice")
    public Result add(@RequestBody ProviderEntity providerEntity){
        return providerService.add(providerEntity);
    }

    @PostMapping("/submit")
    public Result addEvaluation(@RequestBody EvaluationEntity evaluationEntity) {
        return providerService.addEvaluation(evaluationEntity);
    }

    @GetMapping("/getserviceInfo/{name}")
    public Result getInfo(@PathVariable String name){
        return providerService.getserviceInfo(name);
    }

    @GetMapping("/getevaluation/{name}")
    public Result getevaluation(@PathVariable String name){
        EvaluationEntity evaluationEntity = providerService.getevaluation(name);
        if (evaluationEntity != null) {
            return Result.success("查询成功");
        } else {
            return Result.error("查询失败，未找到该服务商信息");
        }
    }

    @PostMapping("/setStatus")
    public Result setStatus(@RequestBody ProviderEntity providerEntity){
        return providerService.setstatus(providerEntity);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(ProviderEntity providerEntity,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ProviderEntity> page = providerService.selectPage(providerEntity, pageNum, pageSize);
        return Result.success(page);
    }
    /**
     * 分页查询2
     */
    @GetMapping("/selectPage2")
    public Result selectPage2(EvaluationEntity evaluationEntity,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<EvaluationEntity> page = providerService.selectPage2(evaluationEntity, pageNum, pageSize);
        return Result.success(page);
    }

}
