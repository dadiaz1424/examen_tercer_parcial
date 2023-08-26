package com.banquito.core.branches.service;

import com.banquito.core.branches.exception.CRUDException;
import com.banquito.core.branches.model.Branch;
import com.banquito.core.branches.repository.BranchRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BranchServiceTest {

    private static final Logger log = LoggerFactory.getLogger(BranchServiceTest.class);

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchService branchService;

    @Test
    public void testLookByCode() {
        String code = "BRANCH1";
        Branch expectedBranch = new Branch("1", "BRANCH1", "Sucursal 1");
        when(branchRepository.findByCode(code)).thenReturn(expectedBranch);

        Branch actualBranch = branchService.lookByCode(code);

        assertNotNull(actualBranch);
        assertEquals(expectedBranch.getName(), actualBranch.getName());
    }

    @Test
    public void testGetAll() {
        List<Branch> expectedList = new ArrayList<>();
        expectedList.add(new Branch("1", "BRANCH1", "Sucursal 1"));
        expectedList.add(new Branch("2", "BRANCH2", "Sucursal 2"));

        when(branchRepository.findAll()).thenReturn(expectedList);

        List<Branch> actualList = branchService.getAll();

        assertEquals(expectedList.size(), actualList.size());
        assertEquals(expectedList.get(0).getName(), actualList.get(0).getName());
        assertEquals(expectedList.get(1).getName(), actualList.get(1).getName());
    }

    @Test
    public void testCreate() throws CRUDException {
        Branch branchToCreate = new Branch("1", "BRANCH1", "Sucursal 1");

        when(branchRepository.save(branchToCreate)).thenReturn(branchToCreate);

        assertDoesNotThrow(() -> branchService.create(branchToCreate));
    }

    @Test
    public void testUpdate() throws CRUDException {
        String code = "BRANCH1";
        Branch branchToUpdate = new Branch("1", "BRANCH1", "Sucursal Actualizada");

        when(branchRepository.findByCode(code)).thenReturn(branchToUpdate);
        when(branchRepository.save(branchToUpdate)).thenReturn(branchToUpdate);

        assertDoesNotThrow(() -> branchService.update(code, branchToUpdate));
    }

    @Test
    public void testUpdateNonexistentBranch() {
        String code = "NONEXISTENT";
        Branch branchToUpdate = new Branch("1", "BRANCH1", "Sucursal Actualizada");

        when(branchRepository.findByCode(code)).thenReturn(null);

        assertThrows(CRUDException.class, () -> branchService.update(code, branchToUpdate));
    }
    @Test
    public void testLookById() {
        String existingId = "1";
        String nonexistentId = "no";

        Branch expectedBranch = new Branch(existingId, "BRANCH1", "Sucursal 1");
        when(branchRepository.findById(existingId)).thenReturn(Optional.of(expectedBranch));
        when(branchRepository.findById(nonexistentId)).thenReturn(Optional.empty());
        try {
            Branch actualExistingBranch = branchService.lookById(existingId);
            assertNotNull(actualExistingBranch);
            assertEquals(expectedBranch.getName(), actualExistingBranch.getName());
        } catch (CRUDException e) {
            fail("No se encuentra la branch");
        }

        assertThrows(CRUDException.class, () -> branchService.lookById(nonexistentId));
    }
}