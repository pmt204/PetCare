package yoot.nhom11.petcare;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import yoot.nhom11.petcare.entity.Doctor;
import yoot.nhom11.petcare.repository.DoctorRepository;
import yoot.nhom11.petcare.service.impl.DoctorServiceImpl;

@ExtendWith(MockitoExtension.class)
public class GetDoctorServiceTest {

    @Mock private DoctorRepository repository;
    @InjectMocks private DoctorServiceImpl service;

    @Test
    void getById_returnsDoctor() {
        Doctor d = new Doctor();
        d.setId(2L);
        d.setName("Dr B");
        d.setSpecialty("Dermatology");

        when(repository.findById(2L)).thenReturn(Optional.of(d));

        var res = service.getById(2L);
        assertEquals(2L, res.getId());
        assertEquals("Dr B", res.getName());
    }
}
