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
    void buildRolesNormalList() {
        List<ERoleName> roleNameList = new ArrayList<>();
        roleNameList.add(ERoleName.CUSTOMER);
        roleNameList.add(ERoleName.ADMIN);
        roleNameList.add(ERoleName.SUPPLIER);

        Role roleMockedFirst = Mockito.mock(Role.class);
        Mockito.doReturn(ERoleName.CUSTOMER).when(roleMockedFirst).getRoleName();
        Role roleMockedSecond = Mockito.mock(Role.class);
        Mockito.doReturn(ERoleName.SUPPLIER).when(roleMockedSecond).getRoleName();

        Mockito.when(roleRepositoryMocked.getRoleByRoleName(Mockito.any(ERoleName.class)))
                .thenReturn(roleMockedFirst)
                .thenReturn(null)
                .thenReturn(roleMockedSecond);

        List<Role> result = roleService.buildRolesList(roleNameList);
        assertEquals(ERoleName.CUSTOMER, result.get(0).getRoleName());
        assertEquals(ERoleName.SUPPLIER, result.get(1).getRoleName());
        Mockito.verify(roleRepositoryMocked, Mockito.times(3)).getRoleByRoleName(Mockito.any());
    }

    @Test
    void buildRolesEmptyList() {
        List<ERoleName> roleNameList = new ArrayList<>();

        Role roleMockedFirst = Mockito.mock(Role.class);
        Mockito.doReturn(ERoleName.CUSTOMER).when(roleMockedFirst).getRoleName();

        Mockito.when(roleRepositoryMocked.getRoleByRoleName(Mockito.any(ERoleName.class)))
                .thenReturn(roleMockedFirst);

        List<Role> result = roleService.buildRolesList(roleNameList);
        assertEquals(ERoleName.CUSTOMER, result.get(0).getRoleName());
        Mockito.verify(roleRepositoryMocked, Mockito.times(1)).getRoleByRoleName(Mockito.any());
    }

    @Test
    void buildRolesDoubledList() {
        List<ERoleName> roleNameList = new ArrayList<>();
        roleNameList.add(ERoleName.CUSTOMER);
        roleNameList.add(ERoleName.CUSTOMER);

        Role roleMockedFirst = Mockito.mock(Role.class);
        Mockito.doReturn(ERoleName.CUSTOMER).when(roleMockedFirst).getRoleName();
        Role roleMockedSecond = Mockito.mock(Role.class);
        Mockito.doReturn(ERoleName.CUSTOMER).when(roleMockedSecond).getRoleName();

        Mockito.when(roleRepositoryMocked.getRoleByRoleName(Mockito.any(ERoleName.class)))
                .thenReturn(roleMockedFirst)
                .thenReturn(roleMockedSecond);

        List<Role> result = roleService.buildRolesList(roleNameList);
        assertEquals(ERoleName.CUSTOMER, result.get(0).getRoleName());
        assertEquals(1, result.size());
        Mockito.verify(roleRepositoryMocked, Mockito.times(2)).getRoleByRoleName(Mockito.any());
    }
}
