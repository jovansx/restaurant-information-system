package akatsuki.restaurantsysteminformation.unregistereduser;

import akatsuki.restaurantsysteminformation.unregistereduser.dto.UnregisteredCreateUserDTO;
import akatsuki.restaurantsysteminformation.unregistereduser.exception.UserExistsException;
import akatsuki.restaurantsysteminformation.unregistereduser.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnregisteredUserServiceImpl implements UnregisteredUserService {
    private UnregisteredUserRepository unregisteredUserRepository;

    @Autowired
    public void setUnregisteredUserRepository(UnregisteredUserRepository unregisteredUserRepository) {
        this.unregisteredUserRepository = unregisteredUserRepository;
    }

    @Override
    public UnregisteredUser getOne(long id) {
        return unregisteredUserRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException("User with the id " + id + " is not found in the database.")
        );
    }

    @Override
    public void create(UnregisteredUser unregisteredUser) {
        Optional<UnregisteredUser> user = unregisteredUserRepository.findByPinCode(unregisteredUser.getPinCode());
        if(user.isEmpty()) {
            unregisteredUserRepository.save(unregisteredUser);
        } else {
            throw new UserExistsException("User with the pin code " + unregisteredUser.getPinCode() + " already exists in the database.");
        }
    }

    @Override
    public List<UnregisteredUser> getAll() {
        return unregisteredUserRepository.findAll();
    }

}
