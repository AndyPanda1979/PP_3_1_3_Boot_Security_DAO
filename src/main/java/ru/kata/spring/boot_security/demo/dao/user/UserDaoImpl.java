package ru.kata.spring.boot_security.demo.dao.user;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.dao.role.RoleDao;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao{

    @PersistenceContext
    private EntityManager entityManager;

    final RoleDao roleDao;

    public UserDaoImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public User findUserById(Long id) {
        User user = entityManager.find(User.class, id);
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    @Override
    public void saveUser(User user) {
        List<Role> result = new ArrayList<>();
        for (Role role: user.getRoles()) {
            result.add(roleDao.findRoleByName(role.getAuthority()));
        }
        user.setRoles(result);
        entityManager.persist(user);
    }

    @Override
    public void updateUser(User updatedUser) {
        System.out.println("Хочу посмотреть, что в Update user DAO");
        System.out.println(updatedUser.getId());
        System.out.println(updatedUser.getFirstName());
        System.out.println(updatedUser.getLastName());
        System.out.println(updatedUser.getPassword());
        System.out.println(updatedUser.getRoles());
        System.out.println("***************");
        List<Role> result = new ArrayList<>();
        for (Role role: updatedUser.getRoles()) {
           result.add(roleDao.findRoleByName(role.getAuthority()));
        }
        updatedUser.setRoles(result);
        entityManager.merge(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User proxyUser = entityManager.find(User.class, id);
        entityManager.remove(proxyUser);
    }

    @Override
    public Optional<User> tryGetUserByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u where u.firstName = :username", User.class);
        query.setParameter("username", username);
        try {
            Optional <User> rev = Optional.ofNullable(query.getSingleResult());
            return rev;
        } catch (Exception e){
            return Optional.empty();
        }
    }
}
