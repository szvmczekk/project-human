package pl.szvmczek.projecthuman.domain.user;

import pl.szvmczek.projecthuman.domain.user.dto.UserCredentialsDto;

import java.util.stream.Collectors;

public class UserCredentialsDtoMapper {
    public static UserCredentialsDto map(User user){
        return new UserCredentialsDto(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(UserRole::getName).collect(Collectors.toSet())
        );
    }
}
