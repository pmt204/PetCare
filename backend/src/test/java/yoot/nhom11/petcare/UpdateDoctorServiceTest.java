package yoot.nhom11.petcare;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import yoot.nhom11.petcare.dto.request.DoctorRequest;
import yoot.nhom11.petcare.entity.Doctor;
import yoot.nhom11.petcare.repository.DoctorRepository;
import yoot.nhom11.petcare.service.impl.DoctorServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UpdateDoctorServiceTest {

    @Mock private DoctorRepository repository;
    @InjectMocks private DoctorServiceImpl service;

    @Test
    void update_existingDoctor_updatesFields() {
        Doctor exist = new Doctor();
        exist.setId(3L);
        exist.setName("Old");
        exist.setSpecialty("OldSpec");

        when(repository.findById(3L)).thenReturn(Optional.of(exist));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        DoctorRequest req = new DoctorRequest();
        req.setName("New Name");

        var res = service.update(3L, req);
        assertEquals(3L, res.getId());
        assertEquals("New Name", res.getName());
        verify(repository, times(1)).save(exist);
    }
}
