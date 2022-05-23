package com.dongzz.quick.security.service.mapstruct;

import com.dongzz.quick.common.base.BaseMapper;
import com.dongzz.quick.security.domain.SysRole;
import com.dongzz.quick.security.service.dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends BaseMapper<RoleDto, SysRole> {
}
