package com.dongzz.quick.security.service.mapstruct;

import com.dongzz.quick.common.base.BaseMapper;
import com.dongzz.quick.security.domain.SysPermission;
import com.dongzz.quick.security.service.dto.PermissionDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper extends BaseMapper<PermissionDto, SysPermission> {

}
