package timesheet.dto;

import lombok.Data;
import timesheet.entity.Role;

@Data
public class UserDto {


    private Long id;

    private String name;

    private String email;

    private Role role;


}


