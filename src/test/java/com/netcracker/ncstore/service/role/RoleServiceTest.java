package com.netcracker.ncstore.service.role;

import com.netcracker.ncstore.model.Role;
import com.netcracker.ncstore.model.enumerations.ERoleName;
import com.netcracker.ncstore.repository.RoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
class RoleServiceTest {
    private IRoleService roleService;

    @Mock
    private AutoCloseable closeable;

    @Mock
    private RoleRepository roleRepositoryMocked;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        roleService = new RoleService(roleRepositoryMocked);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void parseRoleNamesListToRolesList() {
        List<String> roleNamesList = new ArrayList<>();
        roleNamesList.add("CUSTOMER");
        roleNamesList.add("SUPPLIER");

        Role roleMockedFirst = Mockito.mock(Role.class);
        Mockito.doReturn(ERoleName.CUSTOMER).when(roleMockedFirst).getRoleName();
        Role roleMockedSecond = Mockito.mock(Role.class);
        Mockito.doReturn(ERoleName.SUPPLIER).when(roleMockedSecond).getRoleName();

        Mockito.when(roleRepositoryMocked.findRoleByRoleName(Mockito.any(ERoleName.class)))
                .thenReturn(roleMockedFirst)
                .thenReturn(roleMockedSecond);

        List<Role> result = roleService.parseRoleNamesListToRolesList(roleNamesList);
        assertEquals(ERoleName.CUSTOMER, result.get(0).getRoleName());
        assertEquals(ERoleName.SUPPLIER, result.get(1).getRoleName());
        Mockito.verify(roleRepositoryMocked, Mockito.times(2)).findRoleByRoleName(Mockito.any());
    }

    @Test
    void parseRoleNamesEmptyListToRolesList() {
        List<String> roleNamesList = new ArrayList<>();

        Role roleMockedFirst = Mockito.mock(Role.class);
        Mockito.doReturn(ERoleName.CUSTOMER).when(roleMockedFirst).getRoleName();

        Mockito.when(roleRepositoryMocked.findRoleByRoleName(Mockito.any(ERoleName.class)))
                .thenReturn(roleMockedFirst);

        List<Role> result = roleService.parseRoleNamesListToRolesList(roleNamesList);
        assertEquals(ERoleName.CUSTOMER, result.get(0).getRoleName());
        Mockito.verify(roleRepositoryMocked, Mockito.times(1)).findRoleByRoleName(Mockito.any());
    }

    @Test
    void parseRoleNamesDoubledListToRolesList() {
        List<String> roleNamesList = new ArrayList<>();
        roleNamesList.add("CUSTOMER");
        roleNamesList.add("CUSTOMER");

        Role roleMockedFirst = Mockito.mock(Role.class);
        Mockito.doReturn(ERoleName.CUSTOMER).when(roleMockedFirst).getRoleName();
        Role roleMockedSecond = Mockito.mock(Role.class);
        Mockito.doReturn(ERoleName.CUSTOMER).when(roleMockedSecond).getRoleName();

        Mockito.when(roleRepositoryMocked.findRoleByRoleName(Mockito.any(ERoleName.class)))
                .thenReturn(roleMockedFirst)
                .thenReturn(roleMockedSecond);

        List<Role> result = roleService.parseRoleNamesListToRolesList(roleNamesList);
        assertEquals(ERoleName.CUSTOMER, result.get(0).getRoleName());
        assertEquals(1, result.size());
        Mockito.verify(roleRepositoryMocked, Mockito.times(2)).findRoleByRoleName(Mockito.any());
    }

    @Test
    void parseRoleNamesCorruptedListToRolesList() {
        List<String> roleNamesList = new ArrayList<>();
        roleNamesList.add("CUSTOMER");
        roleNamesList.add("HACKER");
        roleNamesList.add("SUPPLIER");
        roleNamesList.add("SUPERADMIN");

        Role roleMockedFirst = Mockito.mock(Role.class);
        Mockito.doReturn(ERoleName.CUSTOMER).when(roleMockedFirst).getRoleName();
        Role roleMockedSecond = Mockito.mock(Role.class);
        Mockito.doReturn(ERoleName.SUPPLIER).when(roleMockedSecond).getRoleName();

        Mockito.when(roleRepositoryMocked.findRoleByRoleName(Mockito.any(ERoleName.class)))
                .thenReturn(roleMockedFirst)
                .thenReturn(roleMockedSecond);

        List<Role> result = roleService.parseRoleNamesListToRolesList(roleNamesList);
        assertEquals(ERoleName.CUSTOMER, result.get(0).getRoleName());
        assertEquals(ERoleName.SUPPLIER, result.get(1).getRoleName());
        Mockito.verify(roleRepositoryMocked, Mockito.times(2)).findRoleByRoleName(Mockito.any());
    }

    @Test
    void rolesListToRoleNamesList() {
        Role mockedRole = Mockito.mock(Role.class);
        List<Role> rolesList = new ArrayList<>();
        rolesList.add(mockedRole);

        Mockito.doReturn(ERoleName.CUSTOMER).when(mockedRole).getRoleName();

        assertEquals(ERoleName.CUSTOMER, roleService.rolesListToRoleNamesList(rolesList).get(0));
    }

    @Test
    void rolesEmptyListToRoleNamesList() {
        List<Role> rolesList = new ArrayList<>();

        assertEquals(0, roleService.rolesListToRoleNamesList(rolesList).size());
    }
}
