import React, { useEffect, useState } from 'react';
import AdminLayout from '../../layouts/AdminLayout';
import { Plus, Edit2, Trash2, Loader2, DollarSign, FileText, Settings } from 'lucide-react';
import api from '../../services/api';
import { showToast } from '../../components/Toast';

interface ClinicService {
  id: number;
  name: string;
  description: string;
  price: number;
}

export const Services: React.FC = () => {
  const [services, setServices] = useState<ClinicService[]>([]);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingService, setEditingService] = useState<ClinicService | null>(null);
  
  const [form, setForm] = useState({
    name: '',
    description: '',
    price: ''
  });

  const fetchServices = async () => {
    try {
      const response = await api.get('/admin/services');
      setServices(response.data || []);
    } catch (err) {
      console.error('Error fetching services:', err);
      showToast('Tải danh sách dịch vụ thất bại!', 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchServices();
  }, []);

  const openAddModal = () => {
    setEditingService(null);
    setForm({ name: '', description: '', price: '' });
    setIsModalOpen(true);
  };

  const openEditModal = (service: ClinicService) => {
    setEditingService(service);
    setForm({
      name: service.name,
      description: service.description || '',
      price: service.price.toString()
    });
    setIsModalOpen(true);
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa dịch vụ này không?')) {
      try {
        await api.delete(`/admin/services/${id}`);
        showToast('Xóa dịch vụ thành công!', 'success');
        fetchServices();
      } catch (err) {
        console.error('Error deleting service:', err);
        showToast('Xóa dịch vụ thất bại! Dịch vụ này có thể đang được sử dụng trong hóa đơn.', 'error');
      }
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const priceNum = parseFloat(form.price);
    if (!form.name.trim()) {
      showToast('Vui lòng nhập tên dịch vụ!', 'error');
      return;
    }
    if (isNaN(priceNum) || priceNum < 0) {
      showToast('Giá dịch vụ không hợp lệ!', 'error');
      return;
    }

    try {
      const payload = {
        name: form.name.trim(),
        description: form.description.trim(),
        price: priceNum
      };

      if (editingService) {
        await api.put(`/admin/services/${editingService.id}`, payload);
        showToast('Cập nhật dịch vụ thành công!', 'success');
      } else {
        await api.post('/admin/services', payload);
        showToast('Thêm mới dịch vụ thành công!', 'success');
      }
      setIsModalOpen(false);
      fetchServices();
    } catch (err) {
      console.error('Error saving service:', err);
      showToast('Lưu dịch vụ thất bại!', 'error');
    }
  };

  return (
    <AdminLayout>
      <div className="space-y-8 animate-fade-in font-sans">
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Clinic Services</h1>
            <p className="text-slate-500 text-sm mt-1">Manage clinical treatment types, consultation packages, and prices</p>
          </div>
          <button 
            onClick={openAddModal}
            className="flex items-center space-x-2 bg-teal-600 hover:bg-teal-700 text-white font-semibold py-2 px-4 rounded-xl shadow-sm transition"
          >
            <Plus className="h-5 w-5" />
            <span>Thêm Dịch vụ</span>
          </button>
        </div>

        {loading ? (
          <div className="flex h-64 items-center justify-center">
            <Loader2 className="h-10 w-10 text-teal-600 animate-spin" />
            <span className="ml-2 text-slate-500 font-medium">Đang tải danh sách dịch vụ...</span>
          </div>
        ) : services.length === 0 ? (
          <div className="border border-dashed border-slate-200 bg-white rounded-2xl p-12 text-center text-slate-400 text-sm">
            Chưa có dịch vụ nào trong cơ sở dữ liệu. Click "Thêm Dịch vụ" để tạo mới.
          </div>
        ) : (
          <div className="bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-sm">
            <div className="overflow-x-auto text-sm">
              <table className="min-w-full divide-y divide-slate-200 text-left text-slate-700">
                <thead className="bg-slate-50 text-slate-400 uppercase text-xs font-bold tracking-wider">
                  <tr>
                    <th className="px-6 py-3.5">Mã DV</th>
                    <th className="px-6 py-3.5">Tên Dịch vụ</th>
                    <th className="px-6 py-3.5">Mô tả</th>
                    <th className="px-6 py-3.5">Đơn giá</th>
                    <th className="px-6 py-3.5 text-center">Thao tác</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-200 bg-white">
                  {services.map((service) => (
                    <tr key={service.id} className="hover:bg-slate-50/50 transition">
                      <td className="px-6 py-4 font-semibold text-slate-500">#{service.id}</td>
                      <td className="px-6 py-4 font-bold text-slate-800">{service.name}</td>
                      <td className="px-6 py-4 text-slate-500 max-w-xs truncate">{service.description || 'Không có mô tả'}</td>
                      <td className="px-6 py-4 font-extrabold text-teal-600">{service.price.toLocaleString('vi-VN')} ₫</td>
                      <td className="px-6 py-4 text-center">
                        <div className="flex justify-center items-center space-x-2">
                          <button 
                            onClick={() => openEditModal(service)}
                            className="p-2 text-indigo-650 hover:bg-indigo-50 rounded-lg transition"
                            title="Sửa"
                          >
                            <Edit2 className="h-4 w-4" />
                          </button>
                          <button 
                            onClick={() => handleDelete(service.id)}
                            className="p-2 text-red-600 hover:bg-red-50 rounded-lg transition"
                            title="Xóa"
                          >
                            <Trash2 className="h-4 w-4" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}
      </div>

      {/* Add/Edit Modal */}
      {isModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center">
          {/* Backdrop */}
          <div className="absolute inset-0 bg-slate-900/60 backdrop-blur-xs" onClick={() => setIsModalOpen(false)} />
          
          {/* Modal Content */}
          <div className="relative bg-white rounded-3xl shadow-xl w-full max-w-md p-8 border border-slate-100 z-10 animate-fade-in-down mx-4">
            <h3 className="text-xl font-bold text-slate-900 mb-6 flex items-center space-x-2">
              <Settings className="h-6 w-6 text-teal-600" />
              <span>{editingService ? 'Cập nhật Dịch vụ' : 'Thêm Dịch vụ Mới'}</span>
            </h3>

            <form onSubmit={handleSubmit} className="space-y-5">
              <div>
                <label className="block text-xs font-bold uppercase text-slate-400 tracking-wider mb-2">Tên Dịch vụ</label>
                <div className="relative">
                  <span className="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400">
                    <Settings className="h-4 w-4" />
                  </span>
                  <input
                    type="text"
                    value={form.name}
                    onChange={(e) => setForm({ ...form, name: e.target.value })}
                    placeholder="Ví dụ: Siêu âm ổ bụng"
                    className="pl-10 w-full rounded-xl border border-slate-200 py-2.5 px-3 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 font-medium text-slate-750"
                    required
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-bold uppercase text-slate-400 tracking-wider mb-2">Mô tả dịch vụ</label>
                <div className="relative">
                  <span className="absolute top-3 left-0 pl-3.5 flex text-slate-400">
                    <FileText className="h-4 w-4" />
                  </span>
                  <textarea
                    value={form.description}
                    onChange={(e) => setForm({ ...form, description: e.target.value })}
                    placeholder="Mô tả tóm tắt về dịch vụ..."
                    rows={3}
                    className="pl-10 w-full rounded-xl border border-slate-200 py-2.5 px-3 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 font-medium text-slate-750"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-bold uppercase text-slate-400 tracking-wider mb-2">Giá dịch vụ (VNĐ)</label>
                <div className="relative">
                  <span className="absolute inset-y-0 left-0 pl-3.5 flex items-center text-slate-400 font-bold">
                    <DollarSign className="h-4 w-4" />
                  </span>
                  <input
                    type="number"
                    value={form.price}
                    onChange={(e) => setForm({ ...form, price: e.target.value })}
                    placeholder="Ví dụ: 300000"
                    className="pl-10 w-full rounded-xl border border-slate-200 py-2.5 px-3 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 font-medium text-slate-750"
                    required
                  />
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
                  className="flex-1 py-2.5 bg-teal-600 hover:bg-teal-700 text-white font-bold rounded-xl shadow-sm transition text-sm"
                >
                  {editingService ? 'Lưu thay đổi' : 'Thêm mới'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </AdminLayout>
  );
};

export default Services;
