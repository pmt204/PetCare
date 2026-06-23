package yoot.nhom11.petcare.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lab_results")
public class LabResult extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "medical_record_id", nullable = false)
	private MedicalRecord medicalRecord;

	@Column(name = "title", nullable = false, length = 200)
	private String title;

	@Column(name = "file_name", length = 255)
	private String fileName;

	@Column(name = "file_url", length = 500)
	private String fileUrl;

	@Column(name = "mime_type", length = 100)
	private String mimeType;

	@Column(name = "note", length = 1000)
	private String note;
}
