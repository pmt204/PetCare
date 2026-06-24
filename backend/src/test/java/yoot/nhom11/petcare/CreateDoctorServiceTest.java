package yoot.nhom11.petcare;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import yoot.nhom11.petcare.dto.request.DoctorRequest;
import yoot.nhom11.petcare.dto.response.DoctorResponse;
import yoot.nhom11.petcare.entity.Doctor;
import yoot.nhom11.petcare.repository.DoctorRepository;
import yoot.nhom11.petcare.service.impl.DoctorServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CreateDoctorServiceTest {

    @Mock
    private DoctorRepository repository;

    @InjectMocks
    private DoctorServiceImpl service;

    @Test
    void createDoctor_savesAndReturnsResponse() {
        DoctorRequest req = new DoctorRequest();
        req.setName("Dr A");
        req.setSpecialty("Surgery");
        req.setExperienceYears("5");
        req.setDescription("Short");
        req.setServices(Collections.emptyList());

        ArgumentCaptor<Doctor> captor = ArgumentCaptor.forClass(Doctor.class);
        Doctor saved = new Doctor();
        saved.setId(1L);
        saved.setName("Dr A");
        saved.setSpecialty("Surgery");
        saved.setExperienceYears("5");
        saved.setDescription("Short");

        when(repository.save(any())).thenReturn(saved);

        DoctorResponse res = service.create(req);

        verify(repository, times(1)).save(captor.capture());
        Doctor toSave = captor.getValue();
        assertEquals("Dr A", toSave.getName());
        assertEquals(1L, res.getId());
        assertEquals("Dr A", res.getName());
    }
}
