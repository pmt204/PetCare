import React, { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import VetLayout from '../../layouts/VetLayout';
import { Save, Plus, Trash2, Activity, FileText } from 'lucide-react';
import LabResultUploader from '../../components/LabResultUploader';
import type { PrescriptionItem } from '../../components/PrescriptionViewer';
import { useAuth } from '../../context/AuthContext';
import api from '../../services/api';
import { showToast } from '../../components/Toast';

interface LocalPet {
  id: number;
  name: string;
  species: string;
  breed: string;
}

export const CreateRecord: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { user } = useAuth();
  
  const queryPetId = searchParams.get('petId');
  
  const [pets, setPets] = useState<LocalPet[]>([]);
  const [formData, setFormData] = useState({
    petId: '',
    diagnosis: '',
    symptoms: '',
    notes: '',
    followUpInstruction: '',
    nextVisitDate: '',
  });

  const [prescriptions, setPrescriptions] = useState<Omit<PrescriptionItem, 'id'>[]>([]);
  const [newMed, setNewMed] = useState({
    medicationName: '',
    dosage: '',
    frequency: '',
    durationDays: 5,
    instructions: '',
  });

  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [loading, setLoading] = useState(false);

  // Tải danh sách thú cưng
  useEffect(() => {
    const loadPets = async () => {
      try {
        const res = await api.get('/pets', { params: { size: 1000 } });
        const petList = (res.data.content || []).map((p: any) => ({
          id: p.petId,
          name: p.petName,
          species: p.petType,
          breed: p.petBreed || ''
        }));
        setPets(petList);
      } catch (err) {
        console.error('Error fetching pets list:', err);
      }
    };
    loadPets();
  }, []);

  // Tự động chọn thú cưng dựa trên tham số query truyền từ lịch khám
  useEffect(() => {
    if (queryPetId && pets.length > 0) {
      const match = pets.find(p => p.id.toString() === queryPetId);
      if (match) {
        setFormData(prev => ({ ...prev, petId: queryPetId }));
      }
    }
  }, [queryPetId, pets]);

  const handleAddMedication = () => {
    if (!newMed.medicationName || !newMed.dosage || !newMed.frequency) {
      showToast('Vui lòng nhập đầy đủ Tên thuốc, Liều lượng và Tần suất!', 'warning');
      return;
    }
    setPrescriptions([...prescriptions, { ...newMed }]);
    setNewMed({ medicationName: '', dosage: '', frequency: '', durationDays: 5, instructions: '' });
  };

  const handleRemoveMedication = (index: number) => {
    setPrescriptions(prescriptions.filter((_, idx) => idx !== index));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.petId) {
      showToast('Vui lòng chọn thú cưng!', 'error');
      return;
    }
    if (!formData.diagnosis.trim()) {
      showToast('Vui lòng điền chẩn đoán lâm sàng!', 'error');
      return;
    }

    const queryAppointmentId = searchParams.get('appointmentId');
    setLoading(true);
    try {
      const selectedPet = pets.find(p => p.id.toString() === formData.petId);
      const patientName = selectedPet ? selectedPet.name : 'Unknown';

      // 1. Lưu hồ sơ bệnh án
      await api.post('/medical-records', {
        doctorId: user?.id,
        veterinarianId: user?.id,
        petId: Number(formData.petId),
        appointmentId: queryAppointmentId ? Number(queryAppointmentId) : null,
        patientName: patientName,
        diagnosis: formData.diagnosis.trim(),
        symptoms: formData.symptoms.trim(),
        notes: formData.notes.trim(),
        treatmentNote: formData.notes.trim(),
        followUpInstruction: formData.followUpInstruction.trim(),
        nextVisitDate: formData.nextVisitDate || null
      });

      // 2. Kê đơn thuốc (nếu có)
      if (prescriptions.length > 0) {
        const medicineList = prescriptions.map(p => `${p.medicationName} (${p.dosage} ${p.frequency})`).join(', ');
        const instructions = prescriptions.map(p => p.instructions).filter(Boolean).join('; ');
        
        await api.post('/prescriptions', {
          doctorId: user?.id,
          patientName: patientName,
          medicineList: medicineList,
          instructions: instructions || 'Theo chỉ dẫn của bác sĩ'
        });
      }

      // 3. Tải lên tệp báo cáo xét nghiệm (nếu có)
      if (selectedFile) {
        const fileData = new FormData();
        fileData.append('file', selectedFile);
        
        const fileRes = await api.post('/files/upload', fileData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });
        
        const uploadedUrl = fileRes.data.url;
        await api.post('/test-results', {
          doctorId: user?.id,
          patientName: patientName,
          testType: 'Lab PDF Report',
          filePath: uploadedUrl,
          result: 'Succeed'
        });
      }

      showToast('Lưu hồ sơ bệnh án & đơn thuốc thành công!', 'success');
      navigate('/vet/schedule');
    } catch (err) {
      console.error('Error submitting medical record:', err);
      showToast('Lưu bệnh án thất bại! Vui lòng thử lại.', 'error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <VetLayout>
      <div className="max-w-4xl mx-auto space-y-8 animate-fade-in font-sans">
        <div>
          <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Tạo Hồ Sơ Bệnh Án</h1>
          <p className="text-slate-500 text-sm mt-1">Đăng ký thông tin chẩn đoán lâm sàng, hướng dẫn điều trị và kê đơn thuốc cho thú cưng</p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-8">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            
            {/* Core Diagnosis Form (Colspan 2) */}
            <div className="md:col-span-2 space-y-6">
              <div className="bg-white border border-slate-200 rounded-2xl p-6 sm:p-8 shadow-sm space-y-6">
                <h3 className="font-bold text-slate-800 text-base border-b border-slate-100 pb-3 flex items-center gap-1.5">
                  <Activity className="h-4.5 w-4.5 text-indigo-650" /> Đánh giá Lâm sàng
                </h3>
                
                <div className="grid grid-cols-1 gap-6">
                  <div>
                    <label className="block text-sm font-semibold text-slate-700 mb-2">Chọn Thú cưng *</label>
                    <select
                      value={formData.petId}
                      onChange={(e) => setFormData({ ...formData, petId: e.target.value })}
                      className="w-full px-3 py-2.5 border border-slate-355 rounded-lg text-sm focus:ring-2 focus:ring-teal-500 outline-none transition cursor-pointer bg-white"
                      required
                    >
                      <option value="">-- Chọn thú cưng khám --</option>
                      {pets.map(p => (
                        <option key={p.id} value={p.id}>{p.name} ({p.species === 'CAT' ? 'Mèo' : p.species === 'DOG' ? 'Chó' : p.species} - {p.breed})</option>
                      ))}
                    </select>
                  </div>

                  <div>
                    <label className="block text-sm font-semibold text-slate-700 mb-2">Chẩn đoán bệnh *</label>
                    <textarea
                      value={formData.diagnosis}
                      onChange={(e) => setFormData({ ...formData, diagnosis: e.target.value })}
                      rows={3}
                      className="w-full px-3 py-2.5 border border-slate-355 rounded-lg text-sm focus:ring-2 focus:ring-teal-500 outline-none transition"
                      placeholder="Nhập triệu chứng, kết quả khám lâm sàng và chẩn đoán cuối cùng..."
                      required
                    />
                  </div>

                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
                    <div>
                      <label className="block text-sm font-semibold text-slate-700 mb-2">Phương pháp điều trị / Dặn dò</label>
                      <textarea
                        value={formData.notes}
                        onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
                        rows={3}
                        className="w-full px-3 py-2.5 border border-slate-355 rounded-lg text-sm focus:ring-2 focus:ring-teal-500 outline-none transition"
                        placeholder="Hướng dẫn chăm sóc tại nhà hoặc điều trị tại chỗ..."
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-semibold text-slate-700 mb-2">Hướng dẫn tái khám (nếu có)</label>
                      <textarea
                        value={formData.followUpInstruction}
                        onChange={(e) => setFormData({ ...formData, followUpInstruction: e.target.value })}
                        rows={3}
                        className="w-full px-3 py-2.5 border border-slate-355 rounded-lg text-sm focus:ring-2 focus:ring-teal-500 outline-none transition"
                        placeholder="Các điều kiện cần lưu ý để tái khám hoặc chăm sóc sau khám..."
                      />
                    </div>
                  </div>

                  <div>
                    <label className="block text-sm font-semibold text-slate-700 mb-2">Ngày hẹn tái khám (không bắt buộc)</label>
                    <input
                      type="date"
                      value={formData.nextVisitDate}
                      onChange={(e) => setFormData({ ...formData, nextVisitDate: e.target.value })}
                      className="w-full px-3 py-2.5 border border-slate-355 rounded-lg text-sm focus:ring-2 focus:ring-teal-500 outline-none transition cursor-pointer bg-white"
                      min={new Date().toISOString().split('T')[0]}
                    />
                  </div>
                </div>
              </div>

              {/* Prescription Builder */}
              <div className="bg-white border border-slate-200 rounded-2xl p-6 sm:p-8 shadow-sm space-y-6">
                <h3 className="font-bold text-slate-800 text-base border-b border-slate-100 pb-3 flex items-center gap-1.5">
                  💊 Kê đơn thuốc thú y
                </h3>

                {/* Medication Inputs */}
                <div className="grid grid-cols-2 sm:grid-cols-3 gap-4 bg-slate-50 p-4 rounded-xl border border-slate-150">
                  <div className="col-span-2 sm:col-span-1">
                    <label className="block text-xs font-semibold text-slate-500 mb-1">Tên thuốc</label>
                    <input
                      type="text"
                      value={newMed.medicationName}
                      onChange={(e) => setNewMed({ ...newMed, medicationName: e.target.value })}
                      className="w-full px-2.5 py-1.5 border border-slate-300 rounded-lg text-xs outline-none bg-white"
                      placeholder="VD: Amoxicillin"
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-semibold text-slate-500 mb-1">Liều lượng</label>
                    <input
                      type="text"
                      value={newMed.dosage}
                      onChange={(e) => setNewMed({ ...newMed, dosage: e.target.value })}
                      className="w-full px-2.5 py-1.5 border border-slate-300 rounded-lg text-xs outline-none bg-white"
                      placeholder="VD: 250mg hoặc 5ml"
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-semibold text-slate-500 mb-1">Tần suất</label>
                    <input
                      type="text"
                      value={newMed.frequency}
                      onChange={(e) => setNewMed({ ...newMed, frequency: e.target.value })}
                      className="w-full px-2.5 py-1.5 border border-slate-300 rounded-lg text-xs outline-none bg-white"
                      placeholder="VD: Ngày 2 lần"
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-semibold text-slate-500 mb-1">Số ngày uống</label>
                    <input
                      type="number"
                      value={newMed.durationDays}
                      onChange={(e) => setNewMed({ ...newMed, durationDays: Number(e.target.value) })}
                      className="w-full px-2.5 py-1.5 border border-slate-300 rounded-lg text-xs outline-none bg-white"
                      min={1}
                    />
                  </div>
                  <div className="col-span-2">
                    <label className="block text-xs font-semibold text-slate-500 mb-1">Hướng dẫn sử dụng đặc biệt</label>
                    <input
                      type="text"
                      value={newMed.instructions}
                      onChange={(e) => setNewMed({ ...newMed, instructions: e.target.value })}
                      className="w-full px-2.5 py-1.5 border border-slate-300 rounded-lg text-xs outline-none bg-white"
                      placeholder="VD: Uống sau khi ăn"
                    />
                  </div>
                  <div className="col-span-2 sm:col-span-3 flex justify-end pt-2">
                    <button
                      type="button"
                      onClick={handleAddMedication}
                      className="flex items-center space-x-1.5 bg-indigo-50 hover:bg-indigo-100 text-indigo-700 font-bold py-1.5 px-4 rounded-lg text-xs transition border border-indigo-150"
                    >
                      <Plus className="h-3.5 w-3.5" />
                      <span>Thêm vào đơn</span>
                    </button>
                  </div>
                </div>

                {/* Prescription List Table */}
                {prescriptions.length > 0 ? (
                  <div className="border border-slate-200 rounded-xl overflow-hidden text-sm">
                    <table className="min-w-full divide-y divide-slate-200 text-left text-slate-700">
                      <thead className="bg-slate-50 text-slate-505 uppercase text-xs font-bold">
                        <tr>
                          <th className="px-4 py-2">Tên thuốc</th>
                          <th className="px-4 py-2">Liều lượng</th>
                          <th className="px-4 py-2">Tần suất</th>
                          <th className="px-4 py-2">Số ngày</th>
                          <th className="px-4 py-2 text-right">Thao tác</th>
                        </tr>
                      </thead>
                      <tbody className="divide-y divide-slate-150 bg-white">
                        {prescriptions.map((med, idx) => (
                          <tr key={idx} className="hover:bg-slate-50/50 transition">
                            <td className="px-4 py-3 font-semibold text-slate-800">{med.medicationName}</td>
                            <td className="px-4 py-3">{med.dosage}</td>
                            <td className="px-4 py-3">{med.frequency}</td>
                            <td className="px-4 py-3">{med.durationDays} ngày</td>
                            <td className="px-4 py-3 text-right">
                              <button
                                type="button"
                                onClick={() => handleRemoveMedication(idx)}
                                className="text-red-500 hover:text-red-700 transition"
                              >
                                <Trash2 className="h-4 w-4 inline" />
                              </button>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                ) : (
                  <p className="text-slate-400 text-xs italic">Chưa có thuốc nào được kê. Sử dụng các ô nhập bên trên để thêm thuốc vào đơn.</p>
                )}
              </div>
            </div>

            {/* Sidebar Documents Upload (Colspan 1) */}
            <div className="space-y-6">
              <div className="bg-white border border-slate-200 rounded-2xl p-6 shadow-sm space-y-4">
                <h3 className="font-bold text-slate-800 text-sm border-b border-slate-100 pb-2 flex items-center gap-1.5">
                  <FileText className="h-4 w-4 text-slate-450" /> Báo cáo Xét nghiệm
                </h3>
                <LabResultUploader onFileChange={setSelectedFile} selectedFile={selectedFile} />
              </div>

              {/* Submit Buttons */}
              <div className="space-y-3">
                <button
                  type="submit"
                  disabled={loading}
                  className="w-full flex items-center justify-center space-x-2 bg-teal-600 hover:bg-teal-700 text-white font-bold py-2.5 px-4 rounded-xl shadow transition disabled:opacity-75"
                >
                  <Save className="h-4.5 w-4.5" />
                  <span>{loading ? 'Đang lưu bệnh án...' : 'Lưu hồ sơ bệnh án'}</span>
                </button>
                <button
                  type="button"
                  onClick={() => navigate(-1)}
                  className="w-full text-center border border-slate-300 bg-white hover:bg-slate-50 text-slate-700 font-semibold py-2.5 px-4 rounded-xl text-sm transition"
                >
                  Hủy bỏ
                </button>
              </div>
            </div>

          </div>
        </form>
      </div>
    </VetLayout>
  );
};

export default CreateRecord;
