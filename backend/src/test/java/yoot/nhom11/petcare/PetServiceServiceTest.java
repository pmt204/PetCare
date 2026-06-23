package yoot.nhom11.petcare;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yoot.nhom11.petcare.dto.request.PetServiceRequest;
import yoot.nhom11.petcare.dto.response.PetServiceResponse;
import yoot.nhom11.petcare.entity.PetService;
import yoot.nhom11.petcare.repository.PetServiceRepository;
import yoot.nhom11.petcare.service.impl.PetServiceServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceServiceTest {

    @Mock
    private PetServiceRepository petServiceRepository;

    @InjectMocks
    private PetServiceServiceImpl petServiceService;

    @Test
    void createPetService_shouldSaveAndReturnResponse() {
        PetServiceRequest request = new PetServiceRequest();
        request.setName("Check-up");
        request.setDescription("Annual check-up");
        request.setPrice(50.0);

        PetService savedService = new PetService(1L, "Check-up", "Annual check-up", 50.0);

        when(petServiceRepository.save(any(PetService.class))).thenReturn(savedService);

        PetServiceResponse response = petServiceService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Check-up", response.getName());
        verify(petServiceRepository, times(1)).save(any(PetService.class));
    }

    @Test
    void getById_shouldReturnResponse_whenFound() {
        PetService service = new PetService(1L, "Check-up", "Annual check-up", 50.0);
        when(petServiceRepository.findById(1L)).thenReturn(Optional.of(service));

        PetServiceResponse response = petServiceService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(petServiceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(java.util.NoSuchElementException.class, () -> {
            petServiceService.getById(1L);
        });
    }

    @Test
    void listAll_shouldReturnListOfResponses() {
        PetService service = new PetService(1L, "Check-up", "Annual check-up", 50.0);
        when(petServiceRepository.findAll()).thenReturn(Collections.singletonList(service));

        List<PetServiceResponse> responses = petServiceService.listAll();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Check-up", responses.get(0).getName());
    }

    @Test
    void update_shouldUpdateAndReturnResponse() {
        PetServiceRequest request = new PetServiceRequest();
        request.setName("Updated Check-up");
        request.setPrice(60.0);

        PetService existingService = new PetService(1L, "Check-up", "Annual check-up", 50.0);
        when(petServiceRepository.findById(1L)).thenReturn(Optional.of(existingService));

        PetService updatedService = new PetService(1L, "Updated Check-up", "Annual check-up", 60.0);
        when(petServiceRepository.save(any(PetService.class))).thenReturn(updatedService);

        PetServiceResponse response = petServiceService.update(1L, request);

        assertNotNull(response);
        assertEquals("Updated Check-up", response.getName());
        assertEquals(60.0, response.getPrice());
        verify(petServiceRepository, times(1)).save(any(PetService.class));
    }

    @Test
    void delete_shouldCallRepositoryDelete() {
        when(petServiceRepository.existsById(1L)).thenReturn(true);
        doNothing().when(petServiceRepository).deleteById(1L);

        petServiceService.delete(1L);

        verify(petServiceRepository, times(1)).deleteById(1L);
    }
}
