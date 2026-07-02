import React, { useEffect, useState } from 'react';
import AdminLayout from '../../layouts/AdminLayout';
import { Plus, User, Award, Shield, Loader2, Edit2, Trash2, X } from 'lucide-react';
import api from '../../services/api';
import { showToast } from '../../components/Toast';

interface Vet {
  id: number;
  name: string;
  specialty: string;
  experienceYears: string;
  rating: number;
  description: string;
  fullDescription: string;
  active: boolean;
}

export const Vets: React.FC = () => {
  const [vets, setVets] = useState<Vet[]>([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingVet, setEditingVet] = useState<Vet | null>(null);

  const [form, setForm] = useState({
    name: '',
    specialty: '',
    experienceYears: '',
    rating: '5.0',
    description: '',
    fullDescription: '',
    image: ''
  });

  const fetchVets = async () => {
    try {
      const response = await api.get('/doctors');
      const mappedVets = response.data.map((doc: any) => ({
        id: doc.id,
        name: doc.name,
        specialty: doc.specialty || 'General Vet',
        experienceYears: doc.experienceYears || 'Chưa cập nhật',
        rating: doc.rating || 5.0,
        description: doc.description || '',
        fullDescription: doc.fullDescription || '',
        active: true
      }));
      setVets(mappedVets);
    } catch (err) {
      console.error('Error fetching doctors:', err);
      showToast('Tải danh sách bác sĩ thất bại!', 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchVets();
  }, []);

  const openAddModal = () => {
    setEditingVet(null);
    setForm({
      name: '',
      specialty: 'General Vet',
      experienceYears: '3 years',
      rating: '5.0',
      description: 'Chuyên gia y tế thú y chăm sóc sức khỏe tổng quát',
      fullDescription: 'Bác sĩ thú y giàu kinh nghiệm chăm sóc, tư vấn chẩn đoán và điều trị bệnh nội khoa ngoại khoa cho chó mèo.',
      image: 'https://images.unsplash.com/photo-1622253692010-333f2da6031d?auto=format&fit=crop&q=80&w=250&h=250'
    });
    setIsModalOpen(true);
  };

  const openEditModal = (vet: Vet) => {
    setEditingVet(vet);
    setForm({
      name: vet.name,
      specialty: vet.specialty,
      experienceYears: vet.experienceYears,
      rating: vet.rating.toString(),
      description: vet.description,
      fullDescription: vet.fullDescription,
      image: 'https://images.unsplash.com/photo-1622253692010-333f2da6031d?auto=format&fit=crop&q=80&w=250&h=250'
    });
    setIsModalOpen(true);
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa bác sĩ này không?')) {
      try {
        await api.delete(`/doctors/${id}`);
        showToast('Xóa bác sĩ thành công!', 'success');
        await fetchVets();
      } catch (err) {
        console.error('Error deleting doctor:', err);
        showToast('Xóa bác sĩ thất bại!', 'error');
      }
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.name || !form.specialty || !form.experienceYears) {
      showToast('Vui lòng điền đầy đủ Họ tên, Chuyên khoa và Kinh nghiệm!', 'warning');
      return;
    }

    try {
      const payload = {
        name: form.name,
        specialty: form.specialty,
        experienceYears: form.experienceYears,
        rating: parseFloat(form.rating) || 5.0,
        description: form.description,
        fullDescription: form.fullDescription,
        image: form.image,
        services: ['General Consultation', 'Vaccination', 'Checkup']
      };

      if (editingVet) {
        await api.put(`/doctors/${editingVet.id}`, payload);
        showToast('Cập nhật hồ sơ bác sĩ thành công!', 'success');
      } else {
        await api.post('/doctors', payload);
        showToast('Thêm bác sĩ mới thành công!', 'success');
      }
      setIsModalOpen(false);
      await fetchVets();
    } catch (err) {
      console.error('Error saving doctor:', err);
      showToast('Lưu thông tin bác sĩ thất bại!', 'error');
    }
  };

  return (
    <AdminLayout>
      <div className="space-y-8 animate-fade-in font-sans">
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Clinic Veterinarians</h1>
            <p className="text-slate-500 text-sm mt-1">Manage veterinary accounts, specializations and profiles</p>
          </div>
          <button 
            onClick={openAddModal}
            className="flex items-center space-x-2 bg-teal-600 hover:bg-teal-700 text-white font-semibold py-2.5 px-5 rounded-xl shadow-sm transition"
          >
            <Plus className="h-5 w-5" />
            <span>Thêm Bác sĩ</span>
          </button>
        </div>

        {loading ? (
          <div className="flex h-64 items-center justify-center">
            <Loader2 className="h-10 w-10 text-teal-600 animate-spin" />
            <span className="ml-2 text-slate-500 font-medium">Đang tải danh sách bác sĩ...</span>
          </div>
        ) : vets.length === 0 ? (
          <div className="border border-dashed border-slate-200 bg-white rounded-2xl p-12 text-center text-slate-400 text-sm">
            Chưa có hồ sơ bác sĩ nào trong cơ sở dữ liệu.
          </div>
        ) : (
          /* Vet Card Grid */
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {vets.map((vet) => (
              <div key={vet.id} className="bg-white border border-slate-200 rounded-2xl p-6 hover:shadow-lg hover:border-indigo-200 transition-all duration-300 flex flex-col justify-between">
                <div>
                  <div className="flex justify-between items-start">
                    <div className="flex items-center space-x-4">
                      <div className="h-14 w-14 rounded-full bg-slate-105 flex items-center justify-center text-slate-600 border border-slate-200">
                        <User className="h-6 w-6" />
                      </div>
                      <div>
                        <h3 className="text-lg font-bold text-slate-800">{vet.name}</h3>
                        <span className="text-xs font-semibold text-teal-600 bg-teal-50 py-0.5 px-2.5 rounded-full">{vet.specialty}</span>
                      </div>
                    </div>
                    {/* Action buttons */}
                    <div className="flex space-x-1">
                      <button 
                        onClick={() => openEditModal(vet)}
                        className="p-1.5 hover:bg-slate-100 rounded-lg text-slate-400 hover:text-indigo-600 transition"
                        title="Chỉnh sửa"
                      >
                        <Edit2 className="h-4 w-4" />
                      </button>
                      <button 
                        onClick={() => handleDelete(vet.id)}
                        className="p-1.5 hover:bg-slate-100 rounded-lg text-slate-400 hover:text-rose-600 transition"
                        title="Xóa"
                      >
                        <Trash2 className="h-4 w-4" />
                      </button>
                    </div>
                  </div>

                  <p className="text-xs text-slate-500 mt-4 line-clamp-2 italic">"{vet.description || 'Chưa cập nhật giới thiệu ngắn.'}"</p>

                  <div className="grid grid-cols-2 gap-4 border-t border-slate-100 mt-6 pt-4 text-sm text-center">
                    <div>
                      <span className="text-slate-400 text-xs block uppercase">Experience</span>
                      <span className="font-semibold text-slate-700 mt-0.5 block">{vet.experienceYears}</span>
                    </div>
                    <div className="border-l border-slate-100">
                      <span className="text-slate-400 text-xs block uppercase">Rating</span>
                      <span className="font-bold text-teal-600 mt-0.5 block">⭐ {vet.rating.toFixed(1)}</span>
                    </div>
                  </div>
                </div>

                <div className="flex justify-between items-center mt-6 pt-4 border-t border-slate-100 text-xs">
                  <div className="flex items-center space-x-1.5 text-slate-500">
                    <Shield className="h-3.5 w-3.5 text-green-500" />
                    <span>Account Active</span>
                  </div>
                  <div className="flex items-center space-x-1 text-slate-500">
                    <Award className="h-3.5 w-3.5 text-amber-500" />
                    <span>Staff</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Add/Edit Modal */}
        {isModalOpen && (
          <div className="fixed inset-0 bg-slate-900/60 z-50 flex items-center justify-center p-4 backdrop-blur-sm">
            <div className="bg-white rounded-3xl p-6 sm:p-8 max-w-lg w-full shadow-2xl border border-slate-100 animate-scale-up relative">
              <button 
                onClick={() => setIsModalOpen(false)}
                className="absolute top-4 right-4 p-2 hover:bg-slate-100 rounded-full text-slate-400 hover:text-slate-600 transition"
              >
                <X className="h-5 w-5" />
              </button>

              <h2 className="text-xl font-bold text-slate-900 mb-6">
                {editingVet ? 'Chỉnh sửa Hồ sơ Bác sĩ' : 'Thêm Bác sĩ thú y mới'}
              </h2>

              <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                  <label className="block text-xs font-bold text-slate-400 uppercase mb-1">Họ tên Bác sĩ</label>
                  <input
                    type="text"
                    required
                    value={form.name}
                    onChange={(e) => setForm(prev => ({ ...prev, name: e.target.value }))}
                    className="w-full border border-slate-200 rounded-xl px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500"
                    placeholder="VD: Dr. Helen Carter"
                  />
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-xs font-bold text-slate-400 uppercase mb-1">Chuyên khoa</label>
                    <input
                      type="text"
                      required
                      value={form.specialty}
                      onChange={(e) => setForm(prev => ({ ...prev, specialty: e.target.value }))}
                      className="w-full border border-slate-200 rounded-xl px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500"
                      placeholder="VD: Surgeon / General Vet"
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-bold text-slate-400 uppercase mb-1">Kinh nghiệm</label>
                    <input
                      type="text"
                      required
                      value={form.experienceYears}
                      onChange={(e) => setForm(prev => ({ ...prev, experienceYears: e.target.value }))}
                      className="w-full border border-slate-200 rounded-xl px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500"
                      placeholder="VD: 5 years"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-xs font-bold text-slate-400 uppercase mb-1">Đánh giá sao (Rating)</label>
                    <input
                      type="number"
                      step="0.1"
                      min="1"
                      max="5"
                      required
                      value={form.rating}
                      onChange={(e) => setForm(prev => ({ ...prev, rating: e.target.value }))}
                      className="w-full border border-slate-200 rounded-xl px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500"
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-bold text-slate-400 uppercase mb-1">Ảnh đại diện (Unsplash URL)</label>
                    <input
                      type="text"
                      value={form.image}
                      onChange={(e) => setForm(prev => ({ ...prev, image: e.target.value }))}
                      className="w-full border border-slate-200 rounded-xl px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500"
                      placeholder="https://..."
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-400 uppercase mb-1">Giới thiệu ngắn</label>
                  <input
                    type="text"
                    value={form.description}
                    onChange={(e) => setForm(prev => ({ ...prev, description: e.target.value }))}
                    className="w-full border border-slate-200 rounded-xl px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500"
                    placeholder="VD: Chuyên gia y tế chăm sóc sức khỏe thú cưng tổng quát..."
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-400 uppercase mb-1">Mô tả đầy đủ chuyên môn</label>
                  <textarea
                    rows={3}
                    value={form.fullDescription}
                    onChange={(e) => setForm(prev => ({ ...prev, fullDescription: e.target.value }))}
                    className="w-full border border-slate-200 rounded-xl px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500"
                    placeholder="VD: Bác sĩ thú y Helen Carter có hơn 5 năm kinh nghiệm trong điều trị các bệnh về da..."
                  />
                </div>

                <div className="flex justify-end space-x-3 pt-4 border-t border-slate-100">
                  <button
                    type="button"
                    onClick={() => setIsModalOpen(false)}
                    className="px-5 py-2.5 border border-slate-200 rounded-xl text-slate-500 hover:bg-slate-50 text-sm font-semibold transition"
                  >
                    Hủy bỏ
                  </button>
                  <button
                    type="submit"
                    className="px-5 py-2.5 bg-teal-600 hover:bg-teal-700 text-white rounded-xl text-sm font-semibold transition shadow-xs"
                  >
                    {editingVet ? 'Lưu thay đổi' : 'Thêm mới'}
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </AdminLayout>
  );
};

export default Vets;
