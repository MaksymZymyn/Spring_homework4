package homework4.mapper;

import homework4.domain.SysUser;
import homework4.domain.SysRole;
import homework4.domain.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserDtoMapper extends DtoMapperFacade<SysUser, UserDto> {

    public UserDtoMapper() {
        super(SysUser.class, UserDto.class);
    }

    @Override
    protected void decorateDto(UserDto dto, SysUser user) {
        dto.setUserName(user.getUserName());
        dto.setSysRoles(user.getSysRoles().stream().map(SysRole::getRoleName).collect(Collectors.joining(", ")));
    }
}
