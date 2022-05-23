package com.dongzz.quick.security.service.mapstruct;

import com.dongzz.quick.common.base.BaseMapper;
import com.dongzz.quick.security.domain.SysDept;
import com.dongzz.quick.security.service.dto.DeptDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeptMapper extends BaseMapper<DeptDto, SysDept> {

}
