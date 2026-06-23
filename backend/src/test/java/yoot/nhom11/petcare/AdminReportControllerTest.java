package yoot.nhom11.petcare;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import yoot.nhom11.petcare.config.SecurityConfig;
import yoot.nhom11.petcare.controller.AdminReportController;
import yoot.nhom11.petcare.repository.AppointmentRepository;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminReportController.class)
@Import(SecurityConfig.class)
public class AdminReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenNotAuthenticated_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/admin/reports/appointments/count?start=2024-01-01T00:00:00&end=2024-01-31T23:59:59"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void getAppointmentCount_shouldReturnCount() throws Exception {
        when(appointmentRepository.countByAppointmentTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(10L);

        mockMvc.perform(get("/api/admin/reports/appointments/count?start=2024-01-01T00:00:00&end=2024-01-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(10));
    }
}
