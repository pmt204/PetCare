package yoot.nhom11.petcare;

import java.util.List;

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
public class ListDoctorsServiceTest {

    @Mock private DoctorRepository repository;
    @InjectMocks private DoctorServiceImpl service;

    @Test
    void listAll_returnsResponses() {
        Doctor d1 = new Doctor(); d1.setId(10L); d1.setName("A");
        Doctor d2 = new Doctor(); d2.setId(11L); d2.setName("B");
        when(repository.findAll()).thenReturn(List.of(d1, d2));

        var res = service.listAll();
        assertEquals(2, res.size());
        assertEquals("A", res.get(0).getName());
    }
}
