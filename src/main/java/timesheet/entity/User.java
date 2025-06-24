package timesheet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    private String name;

    @Column(name = "email")
    @Pattern(regexp = "^[\\w-\\.]+@cstech\\.ai$", message = "Email must be @cstech.ai")
    private String email;


    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

}
