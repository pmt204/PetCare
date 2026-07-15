package yoot.nhom11.petcare;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import yoot.nhom11.petcare.repository.DoctorRepository;
import yoot.nhom11.petcare.service.impl.DoctorServiceImpl;

@ExtendWith(MockitoExtension.class)
public class DeleteDoctorServiceTest {

    @Mock private DoctorRepository repository;
    @InjectMocks private DoctorServiceImpl service;

    @Test
    void delete_existingId_callsDelete() {
        when(repository.existsById(4L)).thenReturn(true);
        service.delete(4L);
        verify(repository, times(1)).deleteById(4L);
    }
}
