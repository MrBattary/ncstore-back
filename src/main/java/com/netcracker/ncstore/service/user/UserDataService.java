package com.netcracker.ncstore.service.user;

import com.netcracker.ncstore.exception.UserServiceNotFoundException;
import com.netcracker.ncstore.model.Company;
import com.netcracker.ncstore.model.Person;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.repository.CompanyRepository;
import com.netcracker.ncstore.repository.PersonRepository;
import com.netcracker.ncstore.repository.UserRepository;
import com.netcracker.ncstore.service.user.interfaces.IUserDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserDataService implements IUserDataService {
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final CompanyRepository companyRepository;

    public UserDataService(final UserRepository userRepository,
                           final PersonRepository personRepository,
                           final CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.companyRepository = companyRepository;
    }


    @Override
    public String getSupplierNameByUserId(UUID userId) throws UserServiceNotFoundException {
        String supplierName;
        try {
            supplierName = getCompany(userId).getCompanyName();
        } catch (UserServiceNotFoundException e) {
            try {
                Person personData = getPerson(userId);
                supplierName = personData.getFirstName() + " " + personData.getLastName();
            }catch (UserServiceNotFoundException exception){
                throw new UserServiceNotFoundException("User with UUID " + userId + " not found. ");
            }
        }
        return supplierName;
    }

    @Override
    public String getPublicNameForUser(UUID userId) throws UserServiceNotFoundException {
        String publicName;
        try {
            publicName = getCompany(userId).getCompanyName();
        } catch (UserServiceNotFoundException e) {
            try {
                publicName = getPerson(userId).getNickName();
            }catch (UserServiceNotFoundException exception){
                throw new UserServiceNotFoundException("User with UUID " + userId + " not found. ");
            }
        }
        return publicName;
    }

    @Override
    public Company getCompany(UUID userId) throws UserServiceNotFoundException {
        return companyRepository.
                findById(userId).
                orElseThrow(() -> new UserServiceNotFoundException("Company with UUID " + userId + " not found. "));
    }

    @Override
    public Person getPerson(UUID userId) throws UserServiceNotFoundException {
        return personRepository.
                findById(userId).
                orElseThrow(() -> new UserServiceNotFoundException("Person with UUID " + userId + " not found. "));
    }

    @Override
    public User getUserByEmail(String email) throws UserServiceNotFoundException {
        return userRepository.
                findByEmail(email).
                orElseThrow(() -> new UserServiceNotFoundException("User with email " + email + " not found. "));
    }

    @Override
    public User getUserById(UUID userId) throws UserServiceNotFoundException {
        return userRepository.
                findById(userId).
                orElseThrow(() -> new UserServiceNotFoundException("User with UUID " + userId + " not found. "));
    }

    @Override
    public boolean isCompany(UUID userId) {
        return companyRepository.existsById(userId);
    }

    @Override
    public boolean isPerson(UUID userId) {
        return personRepository.existsById(userId);
    }
}
