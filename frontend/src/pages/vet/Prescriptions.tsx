import React, { useEffect, useState } from 'react';
import VetLayout from '../../layouts/VetLayout';
import { Plus, Edit2, Trash2, Loader2, FileText, Pill, Users, Info } from 'lucide-react';
import api from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import { showToast } from '../../components/Toast';

interface PrescriptionItem {
  id: number;
  doctorId: number;
  doctorName: string;
  patientName: string;
  createdDate: string;
  medicineList: string;
  instructions: string;
  status: string;
}

export const Prescriptions: React.FC = () => {
  const { user } = useAuth();
  const [prescriptions, setPrescriptions] = useState<PrescriptionItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingPrescription, setEditingPrescription] = useState<PrescriptionItem | null>(null);
  
  const [myDoctorId, setMyDoctorId] = useState<number | null>(null);
  const [filterMode, setFilterMode] = useState<'all' | 'mine'>('mine');

  const [form, setForm] = useState({
    patientName: '',
    medicineList: '',
    instructions: '',
    status: 'Active'
  });

  // Tìm doctorId của bác sĩ hiện tại dựa vào họ tên
  const fetchDoctorInfo = async () => {
    try {
      const res = await api.get('/doctors');
      const doc = (res.data || []).find((d: any) => d.name === user?.fullName);
      if (doc) {
        setMyDoctorId(doc.id);
      }
    } catch (err) {
      console.error('Error fetching doctor details:', err);
    }
  };

  const fetchPrescriptions = async () => {
    try {
      const response = await api.get('/prescriptions');
      setPrescriptions(response.data || []);
    } catch (err) {
      console.error('Error fetching prescriptions:', err);
      showToast('Tải danh sách đơn thuốc thất bại!', 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const loadAll = async () => {
      await fetchDoctorInfo();
      await fetchPrescriptions();
    };
    loadAll();
  }, [user]);

  // Lọc đơn thuốc
  const displayedPrescriptions = prescriptions.filter(p => {
    if (filterMode === 'mine') {
      return myDoctorId ? p.doctorId === myDoctorId : p.doctorName === user?.fullName;
    }
    return true;
  });

  const openAddModal = () => {
    setEditingPrescription(null);
    setForm({ patientName: '', medicineList: '', instructions: 'Uống sau bữa ăn ngày 2 lần', status: 'Active' });
    setIsModalOpen(true);
  };

  const openEditModal = (p: PrescriptionItem) => {
    setEditingPrescription(p);
    setForm({
      patientName: p.patientName,
      medicineList: p.medicineList,
      instructions: p.instructions,
      status: p.status || 'Active'
    });
    setIsModalOpen(true);
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa đơn thuốc này không?')) {
      try {
        await api.delete(`/prescriptions/${id}`);
        showToast('Xóa đơn thuốc thành công!', 'success');
        fetchPrescriptions();
      } catch (err) {
        console.error('Error deleting prescription:', err);
        showToast('Xóa đơn thuốc thất bại!', 'error');
      }
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.patientName.trim()) {
      showToast('Vui lòng nhập tên thú cưng/khách hàng!', 'error');
      return;
    }
    if (!form.medicineList.trim()) {
      showToast('Vui lòng nhập danh sách thuốc!', 'error');
      return;
    }

    try {
      const payload = {
        doctorId: myDoctorId || user?.id || 1,
        patientName: form.patientName.trim(),
        medicineList: form.medicineList.trim(),
        instructions: form.instructions.trim(),
        status: form.status
      };

      if (editingPrescription) {
        await api.put(`/prescriptions/${editingPrescription.id}`, payload);
        showToast('Cập nhật đơn thuốc thành công!', 'success');
      } else {
        await api.post('/prescriptions', payload);
        showToast('Kê đơn thuốc thành công!', 'success');
      }
      setIsModalOpen(false);
      fetchPrescriptions();
    } catch (err) {
      console.error('Error saving prescription:', err);
      showToast('Lưu đơn thuốc thất bại!', 'error');
    }
  };

  return (
    <VetLayout>
      <div className="space-y-8 animate-fade-in font-sans">
        
        {/* Header Title & Cụm lọc + Thêm mới */}
        <div className="flex flex-col md:flex-row md:items-center justify-between space-y-4 md:space-y-0">
          <div>
            <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Prescriptions Management</h1>
            <p className="text-slate-500 text-sm mt-1">Prescribe medication, view logs, edit directions and medical instructions</p>
          </div>
          
          <div className="flex items-center space-x-3 bg-white p-2 border border-slate-200 rounded-2xl shadow-xs self-start">
            <select
              value={filterMode}
              onChange={(e) => setFilterMode(e.target.value as 'all' | 'mine')}
              className="border border-slate-200 rounded-xl px-2.5 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 font-semibold text-slate-700 cursor-pointer"
            >
              <option value="mine">Chỉ đơn thuốc của tôi</option>
              <option value="all">Tất cả đơn thuốc phòng khám</option>
            </select>
            
            <button 
              onClick={openAddModal}
              className="flex items-center space-x-2 bg-teal-600 hover:bg-teal-700 text-white font-semibold py-2 px-4 rounded-xl shadow-xs transition"
            >
              <Plus className="h-4.5 w-4.5" />
              <span>Kê đơn thuốc</span>
            </button>
          </div>
        </div>

        {/* Bảng dữ liệu */}
        {loading ? (
          <div className="flex h-64 items-center justify-center">
            <Loader2 className="h-10 w-10 text-teal-600 animate-spin" />
            <span className="ml-2 text-slate-500 font-medium">Đang tải danh sách đơn thuốc...</span>
          </div>
        ) : displayedPrescriptions.length === 0 ? (
          <div className="border border-dashed border-slate-200 bg-white rounded-2xl p-12 text-center text-slate-400 text-sm">
            Không tìm thấy đơn thuốc nào phù hợp với bộ lọc. Click "Kê đơn thuốc" để tạo mới.
          </div>
        ) : (
          <div className="bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-sm">
            <div className="overflow-x-auto text-sm">
              <table className="min-w-full divide-y divide-slate-200 text-left text-slate-700">
                <thead className="bg-slate-50 text-slate-400 uppercase text-xs font-bold tracking-wider">
                  <tr>
                    <th className="px-6 py-3.5">Mã Đơn</th>
                    <th className="px-6 py-3.5">Bác sĩ kê</th>
                    <th className="px-6 py-3.5">Thú cưng / Khách</th>
                    <th className="px-6 py-3.5">Danh mục thuốc</th>
                    <th className="px-6 py-3.5">Hướng dẫn</th>
                    <th className="px-6 py-3.5">Ngày kê</th>
                    <th className="px-6 py-3.5">Trạng thái</th>
                    <th className="px-6 py-3.5 text-center">Thao tác</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-200 bg-white">
                  {displayedPrescriptions.map((p) => {
                    const localDate = p.createdDate 
                      ? new Date(p.createdDate).toLocaleDateString('vi-VN') 
                      : 'Chưa rõ';
                    return (
                      <tr key={p.id} className="hover:bg-slate-50/50 transition">
                        <td className="px-6 py-4 font-semibold text-slate-500">#{p.id}</td>
                        <td className="px-6 py-4 font-semibold text-slate-800">{p.doctorName || 'Bác sĩ'}</td>
                        <td className="px-6 py-4 font-bold text-teal-700">{p.patientName}</td>
                        <td className="px-6 py-4 font-medium text-slate-800 max-w-xs truncate" title={p.medicineList}>
                          {p.medicineList}
                        </td>
                        <td className="px-6 py-4 text-slate-500 text-xs max-w-xs truncate" title={p.instructions}>
                          {p.instructions}
                        </td>
                        <td className="px-6 py-4 text-slate-500">{localDate}</td>
                        <td className="px-6 py-4">
                          <span className={`px-2 py-0.5 rounded-full text-[10px] font-bold ${p.status?.toLowerCase() === 'active' || !p.status ? 'bg-green-50 text-green-700' : 'bg-slate-100 text-slate-500'}`}>
                            {p.status || 'Active'}
                          </span>
                        </td>
                        <td className="px-6 py-4 text-center">
                          <div className="flex justify-center items-center space-x-1.5">
                            <button 
                              onClick={() => openEditModal(p)}
                              className="p-1.5 text-indigo-650 hover:bg-indigo-50 rounded-lg transition"
                              title="Sửa đơn thuốc"
                            >
                              <Edit2 className="h-4 w-4" />
                            </button>
                            <button 
                              onClick={() => handleDelete(p.id)}
                              className="p-1.5 text-red-650 hover:bg-red-50 rounded-lg transition"
                              title="Xóa đơn thuốc"
                            >
                              <Trash2 className="h-4 w-4" />
                            </button>
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        )}
      </div>

      {/* Modal kê đơn thuốc */}
      {isModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center">
          {/* Backdrop */}
          <div className="absolute inset-0 bg-slate-900/60 backdrop-blur-xs" onClick={() => setIsModalOpen(false)} />
          
          {/* Content */}
          <div className="relative bg-white rounded-3xl shadow-xl w-full max-w-md p-8 border border-slate-100 z-10 animate-fade-in-down mx-4">
            <h3 className="text-xl font-bold text-slate-900 mb-6 flex items-center space-x-2">
              <Pill className="h-6 w-6 text-teal-600 animate-pulse" />
              <span>{editingPrescription ? 'Cập nhật Đơn thuốc' : 'Kê đơn thuốc Thú y'}</span>
            </h3>

            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-xs font-bold uppercase text-slate-400 mb-1.5">Tên Thú cưng / Khách hàng</label>
                <div className="relative">
                  <span className="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400">
                    <Users className="h-4 w-4" />
                  </span>
                  <input
                    type="text"
                    value={form.patientName}
                    onChange={(e) => setForm({ ...form, patientName: e.target.value })}
                    placeholder="Ví dụ: Cún LuLu"
                    className="pl-10 w-full rounded-xl border border-slate-200 py-2.5 px-3 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 font-medium text-slate-750"
                    required
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-bold uppercase text-slate-400 mb-1.5">Danh mục thuốc & Liều lượng</label>
                <div className="relative">
                  <span className="absolute top-3 left-0 pl-3.5 flex text-slate-400">
                    <Pill className="h-4 w-4" />
                  </span>
                  <textarea
                    value={form.medicineList}
                    onChange={(e) => setForm({ ...form, medicineList: e.target.value })}
                    placeholder="Ví dụ: Amoxicillin 250mg (10 viên), Paracetamol Vet (1 lọ)..."
                    rows={3}
                    className="pl-10 w-full rounded-xl border border-slate-200 py-2.5 px-3 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 font-medium text-slate-750"
                    required
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-bold uppercase text-slate-400 mb-1.5">Hướng dẫn sử dụng</label>
                <div className="relative">
                  <span className="absolute top-3 left-0 pl-3.5 flex text-slate-400">
                    <FileText className="h-4 w-4" />
                  </span>
                  <textarea
                    value={form.instructions}
                    onChange={(e) => setForm({ ...form, instructions: e.target.value })}
                    placeholder="Ví dụ: Uống sau ăn, sáng 1 viên, chiều 1 viên..."
                    rows={2}
                    className="pl-10 w-full rounded-xl border border-slate-200 py-2.5 px-3 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 font-medium text-slate-750"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-bold uppercase text-slate-400 mb-1.5">Trạng thái đơn thuốc</label>
                <div className="relative">
                  <span className="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400">
                    <Info className="h-4 w-4" />
                  </span>
                  <select
                    value={form.status}
                    onChange={(e) => setForm({ ...form, status: e.target.value })}
                    className="pl-10 w-full rounded-xl border border-slate-200 py-2.5 px-3 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 font-semibold text-slate-750 bg-white"
                  >
                    <option value="Active">Hoạt động (Active)</option>
                    <option value="Completed">Hoàn thành (Completed)</option>
                    <option value="Cancelled">Đã hủy (Cancelled)</option>
                  </select>
                </div>
              </div>

              <div className="flex space-x-3 pt-4">
                <button
                  type="button"
                  onClick={() => setIsModalOpen(false)}
                  className="flex-1 py-2.5 bg-slate-100 hover:bg-slate-250 text-slate-700 font-bold rounded-xl transition text-sm"
                >
                  Hủy bỏ
                </button>
                <button
                  type="submit"
                  className="flex-1 py-2.5 bg-teal-600 hover:bg-teal-700 text-white font-bold rounded-xl shadow-xs transition text-sm"
                >
                  {editingPrescription ? 'Lưu thay đổi' : 'Kê đơn'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </VetLayout>
  );
};

export default Prescriptions;
