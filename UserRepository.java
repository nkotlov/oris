package dz3;

import java.util.List;

public interface UserRepository extends CrudRepository<User> {
    List<User> findAllByAge(Integer age);
    List<User> findAllByEmail(String email);
    List<User> findAllByPhoneNumber(String phoneNumber);
    List<User> findAllByYearsExperience(Integer yearsExperience);
}
