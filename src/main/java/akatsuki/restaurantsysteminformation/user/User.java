package akatsuki.restaurantsysteminformation.user;

import akatsuki.restaurantsysteminformation.enums.UserType;
import akatsuki.restaurantsysteminformation.salary.Salary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "my_user")
@Where(clause = "is_deleted = false")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email_address", unique = true, nullable = false)
    private String emailAddress;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    // TODO aca ne zeli plate
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Salary> salary;

    @Column(name = "type", nullable = false)
    private UserType type;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public User(String firstName, String lastName, String emailAddress, String phoneNumber, List<Salary> salary, UserType type, boolean isDeleted) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.salary = salary;
        this.type = type;
        this.isDeleted = isDeleted;
    }

    public String getName() {
        return firstName + " " + lastName;
    }
}
