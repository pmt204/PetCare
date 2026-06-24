package yoot.nhom11.petcare.mapper;

import java.util.Optional;

import yoot.nhom11.petcare.dto.request.DoctorRequest;
import yoot.nhom11.petcare.dto.response.DoctorResponse;
import yoot.nhom11.petcare.entity.Doctor;

public class DoctorMapper {

    public static Doctor toEntity(DoctorRequest r) {
        Doctor d = new Doctor();
        d.setName(r.getName());
        d.setSpecialty(r.getSpecialty());
        d.setExperienceYears(r.getExperienceYears());
        d.setImage(r.getImage());
        d.setRating(r.getRating());
        d.setDescription(Optional.ofNullable(r.getDescription()).orElse(""));
        d.setFullDescription(r.getFullDescription());
        d.setServices(r.getServices());
        return d;
    }

    public static DoctorResponse toResponse(Doctor d) {
        DoctorResponse r = new DoctorResponse();
        r.setId(d.getId());
        r.setName(d.getName());
        r.setSpecialty(d.getSpecialty());
        r.setExperienceYears(d.getExperienceYears());
        r.setImage(d.getImage());
        r.setRating(d.getRating());
        r.setDescription(d.getDescription());
        r.setFullDescription(d.getFullDescription());
        r.setServices(d.getServices());
        return r;
    }

    public static void updateEntityFromRequest(DoctorRequest req, Doctor d) {
        if (req.getName() != null) d.setName(req.getName());
        if (req.getSpecialty() != null) d.setSpecialty(req.getSpecialty());
        if (req.getExperienceYears() != null) d.setExperienceYears(req.getExperienceYears());
        if (req.getImage() != null) d.setImage(req.getImage());
        if (req.getRating() != null) d.setRating(req.getRating());
        if (req.getDescription() != null) d.setDescription(req.getDescription());
        if (req.getFullDescription() != null) d.setFullDescription(req.getFullDescription());
        if (req.getServices() != null) d.setServices(req.getServices());
    }
}
