package yoot.nhom11.petcare.dto.request;

import java.util.List;

public class DoctorRequest {
    private String name;
    private String specialty;
    private String experienceYears;
    private String image;
    private Double rating;
    private String description;
    private String fullDescription;
    private List<String> services;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public String getExperienceYears() { return experienceYears; }
    public void setExperienceYears(String experienceYears) { this.experienceYears = experienceYears; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFullDescription() { return fullDescription; }
    public void setFullDescription(String fullDescription) { this.fullDescription = fullDescription; }
    public List<String> getServices() { return services; }
    public void setServices(List<String> services) { this.services = services; }
}
