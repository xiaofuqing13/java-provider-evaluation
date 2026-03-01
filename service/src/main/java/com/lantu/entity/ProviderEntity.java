package com.lantu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("serviceproviders")
public class ProviderEntity {
    @TableId(value = "id",type = IdType.AUTO)
    private int id;
    private String address;
    private String name;
    private String trusted;
    @TableField("totalRating")
    private int totalRating;
    @TableField("totalRatingsCount")
    private int totalRatingsCount;
    @TableField("serviceCount")

    private String serviceCount;

}
