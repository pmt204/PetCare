package yoot.nhom11.petcare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import yoot.nhom11.petcare.dto.request.PetFilterRequest;
import yoot.nhom11.petcare.dto.request.PetRequest;
import yoot.nhom11.petcare.dto.response.PetResponse;
import yoot.nhom11.petcare.service.PetService;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PetController.class)
@AutoConfigureMockMvc(addFilters = false)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private PetService petService;

    private PetResponse petResponse1;
    private PetResponse petResponse2;

    @BeforeEach
    void setUp() {
        petResponse1 = PetResponse.builder()
                .petId(1)
                .petName("Fluffy")
                .petType("Cat")
                .petAge(3)
                .petGender("Male")
                .petAvatar("http://avatar.url/1")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        petResponse2 = PetResponse.builder()
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
    void getAllPets_success() throws Exception {
        when(petService.getAllPets(any(PetFilterRequest.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(petResponse1, petResponse2)));

        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content[0].petName").value("Fluffy"))
                .andExpect(jsonPath("$.content[1].petName").value("Buddy"));

        verify(petService, times(1)).getAllPets(any(PetFilterRequest.class), any(Pageable.class));
    }

    @Test
    void getPetById_success() throws Exception {
        when(petService.getPetById(1)).thenReturn(petResponse1);

        mockMvc.perform(get("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.petId").value(1))
                .andExpect(jsonPath("$.petName").value("Fluffy"));

        verify(petService, times(1)).getPetById(1);
    }

    @Test
    void getPetById_notFound() throws Exception {
        when(petService.getPetById(99)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));

        mockMvc.perform(get("/api/pets/99"))
                .andExpect(status().isNotFound());

        verify(petService, times(1)).getPetById(99);
    }

    @Test
    void createPet_success() throws Exception {
        PetRequest request = PetRequest.builder()
                .petName("Lola")
                .petType("Rabbit")
                .petAge(1)
                .petGender("Female")
                .petAvatar("http://avatar.url/3")
                .build();

        PetResponse createdResponse = PetResponse.builder()
                .petId(3)
                .petName("Lola")
                .petType("Rabbit")
                .petAge(1)
                .petGender("Female")
                .petAvatar("http://avatar.url/3")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        when(petService.createPet(any(PetRequest.class))).thenReturn(createdResponse);

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.petId").value(3))
                .andExpect(jsonPath("$.petName").value("Lola"));

        verify(petService, times(1)).createPet(any(PetRequest.class));
    }

    @Test
    void createPet_invalidPayload() throws Exception {
        PetRequest request = PetRequest.builder()
                .petName("") // Blank pet name
                .petType("Rabbit")
                .petAge(-1) // Negative pet age
                .build();

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(petService, never()).createPet(any(PetRequest.class));
    }

    @Test
    void updatePet_success() throws Exception {
        PetRequest request = PetRequest.builder()
                .petName("Fluffy Updated")
                .petType("Cat")
                .petAge(4)
                .petGender("Male")
                .petAvatar("http://avatar.url/updated")
                .build();

        PetResponse updatedResponse = PetResponse.builder()
                .petId(1)
                .petName("Fluffy Updated")
                .petType("Cat")
                .petAge(4)
                .petGender("Male")
                .petAvatar("http://avatar.url/updated")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        when(petService.updatePet(eq(1), any(PetRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.petName").value("Fluffy Updated"))
                .andExpect(jsonPath("$.petAge").value(4))
                .andExpect(jsonPath("$.petAvatar").value("http://avatar.url/updated"));

        verify(petService, times(1)).updatePet(eq(1), any(PetRequest.class));
    }

    @Test
    void getPetBySlug_success() throws Exception {
        petResponse1.setSlug("fluffy");
        when(petService.getPetBySlug("fluffy")).thenReturn(petResponse1);

        mockMvc.perform(get("/api/pets/slug/fluffy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.petId").value(1))
                .andExpect(jsonPath("$.slug").value("fluffy"))
                .andExpect(jsonPath("$.petName").value("Fluffy"));

        verify(petService, times(1)).getPetBySlug("fluffy");
    }

    @Test
    void getPetBySlug_notFound() throws Exception {
        when(petService.getPetBySlug("unknown-slug"))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found with slug: unknown-slug"));

        mockMvc.perform(get("/api/pets/slug/unknown-slug"))
                .andExpect(status().isNotFound());

        verify(petService, times(1)).getPetBySlug("unknown-slug");
    }
}
