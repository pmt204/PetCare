package yoot.nhom11.petcare.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import yoot.nhom11.petcare.dto.request.PetRequest;
import yoot.nhom11.petcare.dto.response.PetResponse;
import yoot.nhom11.petcare.entity.Pet;
import yoot.nhom11.petcare.mapper.PetMapper;
import yoot.nhom11.petcare.repository.PetRepository;
import yoot.nhom11.petcare.service.impl.PetServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Spy
    private PetMapper petMapper;

    @InjectMocks
    private PetServiceImpl petService;

    private Pet pet1;
    private Pet pet2;

    @BeforeEach
    void setUp() {
        pet1 = Pet.builder()
                .pet_id(1)
                .pet_name("Fluffy")
                .pet_type("Cat")
                .pet_age(3)
                .pet_gender("Male")
                .pet_avatar("http://avatar.url/1")
                .create_at(LocalDateTime.now())
                .update_at(LocalDateTime.now())
                .build();

        pet2 = Pet.builder()
                .pet_id(2)
                .pet_name("Buddy")
                .pet_type("Dog")
                .pet_age(5)
                .pet_gender("Female")
                .pet_avatar("http://avatar.url/2")
                .create_at(LocalDateTime.now())
                .update_at(LocalDateTime.now())
                .build();
    }

    @Test
    void getAllPets_success() {
        when(petRepository.findAll()).thenReturn(Arrays.asList(pet1, pet2));

        List<PetResponse> responses = petService.getAllPets();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Fluffy", responses.get(0).getPetName());
        assertEquals("Buddy", responses.get(1).getPetName());
        verify(petRepository, times(1)).findAll();
    }

    @Test
    void getPetById_success() {
        when(petRepository.findById(1)).thenReturn(Optional.of(pet1));

        PetResponse response = petService.getPetById(1);

        assertNotNull(response);
        assertEquals(1, response.getPetId());
        assertEquals("Fluffy", response.getPetName());
        verify(petRepository, times(1)).findById(1);
    }

    @Test
    void getPetById_notFound() {
        when(petRepository.findById(99)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> petService.getPetById(99));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Pet not found", exception.getReason());
        verify(petRepository, times(1)).findById(99);
    }

    @Test
    void createPet_success() {
        PetRequest request = PetRequest.builder()
                .petName("Lola")
                .petType("Rabbit")
                .petAge(1)
                .petGender("Female")
                .petAvatar("http://avatar.url/3")
                .build();

        Pet savedPet = Pet.builder()
                .pet_id(3)
                .pet_name(request.getPetName())
                .pet_type(request.getPetType())
                .pet_age(request.getPetAge())
                .pet_gender(request.getPetGender())
                .pet_avatar(request.getPetAvatar())
                .create_at(LocalDateTime.now())
                .update_at(LocalDateTime.now())
                .build();

        when(petRepository.save(any(Pet.class))).thenReturn(savedPet);

        PetResponse response = petService.createPet(request);

        assertNotNull(response);
        assertEquals(3, response.getPetId());
        assertEquals("Lola", response.getPetName());
        assertNotNull(response.getCreateAt());
        assertNotNull(response.getUpdateAt());
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    void updatePet_success() {
        PetRequest request = PetRequest.builder()
                .petName("Fluffy Updated")
                .petType("Cat")
                .petAge(4)
                .petGender("Male")
                .petAvatar("http://avatar.url/updated")
                .build();

        when(petRepository.findById(1)).thenReturn(Optional.of(pet1));
        when(petRepository.save(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PetResponse response = petService.updatePet(1, request);

        assertNotNull(response);
        assertEquals("Fluffy Updated", response.getPetName());
        assertEquals(4, response.getPetAge());
        assertEquals("http://avatar.url/updated", response.getPetAvatar());
        verify(petRepository, times(1)).findById(1);
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    void updatePet_notFound() {
        PetRequest request = PetRequest.builder()
                .petName("Fluffy Updated")
                .petType("Cat")
                .build();

        when(petRepository.findById(99)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> petService.updatePet(99, request));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Pet not found", exception.getReason());
        verify(petRepository, times(1)).findById(99);
        verify(petRepository, never()).save(any(Pet.class));
    }
}
