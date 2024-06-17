package homework4.mapper;

import homework4.domain.SysRole;
import homework4.domain.SysUser;
import homework4.domain.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.stream.Collectors;
/*
@Configuration
public class UserDtoMapperConfig {

    @Bean
    public ModelMapper userDtoMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(SysUser.class, UserDto.class)
                .addMapping(SysUser::getUserName, UserDto::setUserName)
                .addMappings(m -> m.map(src -> src.getSysRoles().stream()
                        .map(SysRole::getRoleName)
                        .collect(Collectors.joining(", ")), UserDto::setSysRoles));

        return mapper;
    }
}

 */

