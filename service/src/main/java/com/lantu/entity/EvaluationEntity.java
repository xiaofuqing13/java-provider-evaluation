package com.lantu.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("evaluations")
public class EvaluationEntity {
    private int id;
  

    private int rating;
    private String comment;
    @TableField("providerName")
    private String providerName; // 存储服务商名称
}
