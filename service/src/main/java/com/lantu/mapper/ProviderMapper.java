package com.lantu.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lantu.entity.ProviderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProviderMapper extends BaseMapper<ProviderEntity> {

    @Select("SELECT * FROM serviceproviders WHERE name = #{name}")
    ProviderEntity findByName();

}
