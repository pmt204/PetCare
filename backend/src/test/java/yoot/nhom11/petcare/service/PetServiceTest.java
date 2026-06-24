package yoot.nhom11.petcare.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import yoot.nhom11.petcare.exception.BusinessException;
import yoot.nhom11.petcare.exception.ErrorCode;
import yoot.nhom11.petcare.dto.request.PetFilterRequest;
import yoot.nhom11.petcare.dto.request.PetRequest;
import yoot.nhom11.petcare.dto.response.PetResponse;
import yoot.nhom11.petcare.entity.Pet;
import yoot.nhom11.petcare.mapper.PetMapper;
import yoot.nhom11.petcare.mapper.PetMapperImpl;
import yoot.nhom11.petcare.repository.PetRepository;
import yoot.nhom11.petcare.service.impl.PetServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Spy
    private PetMapper petMapper = new PetMapperImpl();

    @InjectMocks
    private PetServiceImpl petService;

    private Pet pet1;
    private Pet pet2;

    @BeforeEach
    void setUp() {
        pet1 = Pet.builder()
                .petId(1)
                .petName("Fluffy")
                .petType("Cat")
                .petAge(3)
                .petGender("Male")
                .petAvatar("http://avatar.url/1")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        pet2 = Pet.builder()
                .petId(2)
                .petName("Buddy")
                .petType("Dog")
                .petAge(5)
                .petGender("Female")
                .petAvatar("http://avatar.url/2")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getAllPets_success() {
        PetFilterRequest filter = new PetFilterRequest();
        Pageable pageable = PageRequest.of(0, 10);
        when(petRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(Arrays.asList(pet1, pet2)));

        Page<PetResponse> responses = petService.getAllPets(filter, pageable);

        assertNotNull(responses);
        assertEquals(2, responses.getContent().size());
        assertEquals("Fluffy", responses.getContent().get(0).getPetName());
        assertEquals("Buddy", responses.getContent().get(1).getPetName());
        verify(petRepository, times(1)).findAll(any(Specification.class), eq(pageable));
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

        BusinessException exception = assertThrows(BusinessException.class, () -> petService.getPetById(99));
        assertEquals(ErrorCode.PET_NOT_FOUND, exception.getErrorCode());
        assertEquals("Pet not found", exception.getMessage());
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
                .petId(3)
                .petName(request.getPetName())
                .petType(request.getPetType())
                .petAge(request.getPetAge())
                .petGender(request.getPetGender())
                .petAvatar(request.getPetAvatar())
                .slug("lola")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        when(petRepository.findBySlug("lola")).thenReturn(Optional.empty());
        when(petRepository.save(any(Pet.class))).thenReturn(savedPet);

        PetResponse response = petService.createPet(request);

        assertNotNull(response);
        assertEquals(3, response.getPetId());
        assertEquals("Lola", response.getPetName());
        assertEquals("lola", response.getSlug());
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
        when(petRepository.findBySlug("fluffy-updated")).thenReturn(Optional.empty());
        when(petRepository.save(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PetResponse response = petService.updatePet(1, request);

        assertNotNull(response);
        assertEquals("Fluffy Updated", response.getPetName());
        assertEquals(4, response.getPetAge());
        assertEquals("http://avatar.url/updated", response.getPetAvatar());
        assertEquals("fluffy-updated", response.getSlug());
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

        BusinessException exception = assertThrows(BusinessException.class, () -> petService.updatePet(99, request));
        assertEquals(ErrorCode.PET_NOT_FOUND, exception.getErrorCode());
        assertEquals("Pet not found", exception.getMessage());
        verify(petRepository, times(1)).findById(99);
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void getPetBySlug_success() {
        pet1.setSlug("fluffy");
        when(petRepository.findBySlug("fluffy")).thenReturn(Optional.of(pet1));

        PetResponse response = petService.getPetBySlug("fluffy");

        assertNotNull(response);
        assertEquals(1, response.getPetId());
        assertEquals("Fluffy", response.getPetName());
        assertEquals("fluffy", response.getSlug());
        verify(petRepository, times(1)).findBySlug("fluffy");
    }

    @Test
    void getPetBySlug_notFound() {
        when(petRepository.findBySlug("unknown-slug")).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> petService.getPetBySlug("unknown-slug"));
        assertEquals(ErrorCode.PET_NOT_FOUND, exception.getErrorCode());
        assertEquals("Pet not found with slug: unknown-slug", exception.getMessage());
        verify(petRepository, times(1)).findBySlug("unknown-slug");
    }
}
