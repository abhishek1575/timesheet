package timesheet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @Column(nullable = false, columnDefinition = "VARCHAR(255) CHECK (email REGEXP '^[\\\\w-\\\\.]+@cstech\\\\.ai$')")
    private String email;

    @NotNull
    private String password;

    @NotNull(message="role is required")
    @Enumerated(EnumType.STRING)
    private Role role;

}
