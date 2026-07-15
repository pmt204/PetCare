import React, { useEffect, useState } from 'react';
import AdminLayout from '../../layouts/AdminLayout';
import { Calendar, User, Heart, Stethoscope, Search, Clock, CheckCircle, XCircle, Loader2, RefreshCw, Phone, AlertCircle, ChevronLeft, ChevronRight } from 'lucide-react';
import api from '../../services/api';
import { showToast } from '../../components/Toast';

interface OwnerSummary {
  id: number;
  fullName: string;
  email: string;
  phone: string;
}

interface PetSummary {
  id: number;
  name: string;
  species: 'DOG' | 'CAT' | 'OTHER';
  breed: string;
}

interface VetSummary {
  id: number;
  fullName: string;
  email: string;
}

interface AppointmentItem {
  id: number;
  owner: OwnerSummary | null;
  pet: PetSummary | null;
  veterinarian: VetSummary | null;
  appointmentAt: string | null; // ISO string
  reasonForVisit: string | null;
  statusEnum: string | null;

  // Compatibility fields
  doctorId: number | null;
  doctorName: string | null;
  patientName: string | null;
  patientPhone: string | null;
  appointmentTime: string | null;
  reason: string | null;
  status: string | null;
  paymentMethod: string | null;
  paymentStatus: string | null;
}

export const Appointments: React.FC = () => {
  const [appointments, setAppointments] = useState<AppointmentItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('ALL');
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 5;
  
  // Trạng thái modal xác nhận hủy
  const [cancelModalOpen, setCancelModalOpen] = useState(false);
  const [selectedApp, setSelectedApp] = useState<AppointmentItem | null>(null);
  const [submittingCancel, setSubmittingCancel] = useState(false);

  const fetchAppointments = async () => {
    setLoading(true);
    try {
      const response = await api.get('/admin/appointments');
      setAppointments(response.data || []);
    } catch (err) {
      console.error('Error fetching appointments:', err);
      showToast('Tải danh sách lịch hẹn thất bại!', 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAppointments();
  }, []);

  useEffect(() => {
    setCurrentPage(1);
  }, [searchTerm, statusFilter]);

  const openCancelModal = (app: AppointmentItem) => {
    setSelectedApp(app);
    setCancelModalOpen(true);
  };

  const closeCancelModal = () => {
    setSelectedApp(null);
    setCancelModalOpen(false);
  };

  const handleCancelAppointment = async () => {
    if (!selectedApp) return;
    setSubmittingCancel(true);
    try {
      await api.put(`/admin/appointments/${selectedApp.id}/cancel`);
      showToast(`Đã hủy thành công lịch hẹn #${selectedApp.id}!`, 'success');
      await fetchAppointments();
      closeCancelModal();
    } catch (err) {
      console.error('Error cancelling appointment:', err);
      showToast('Hủy lịch hẹn thất bại. Vui lòng thử lại!', 'error');
    } finally {
      setSubmittingCancel(false);
    }
  };

  // Các hàm helper để lấy dữ liệu đồng nhất từ cả 2 dạng biểu diễn DTO
  const getPetName = (app: AppointmentItem) => app.pet?.name || app.patientName || 'Chưa rõ';
  const getOwnerName = (app: AppointmentItem) => app.owner?.fullName || app.patientName || 'Vãng lai';
  const getOwnerPhone = (app: AppointmentItem) => app.owner?.phone || app.patientPhone || 'Không có';
  const getVetName = (app: AppointmentItem) => app.veterinarian?.fullName || app.doctorName || 'Chưa phân công';
  const getReason = (app: AppointmentItem) => app.reasonForVisit || app.reason || 'Khám bệnh định kỳ';
  
  const getStatusTextAndStyle = (app: AppointmentItem) => {
    const rawStatus = (app.status || app.statusEnum || 'REQUESTED').toUpperCase();
    switch (rawStatus) {
      case 'CANCELLED':
        return { text: 'Đã hủy', badge: 'bg-rose-50 text-rose-700 border-rose-150' };
      case 'COMPLETED':
        return { text: 'Đã khám', badge: 'bg-emerald-50 text-emerald-700 border-emerald-150' };
      case 'CONFIRMED':
        return { text: 'Đã xác nhận', badge: 'bg-teal-50 text-teal-700 border-teal-150' };
      case 'REQUESTED':
      default:
        return { text: 'Chờ duyệt', badge: 'bg-amber-50 text-amber-700 border-amber-150' };
    }
  };

  const formatDateTime = (dateStr: string | null) => {
    if (!dateStr) return 'Chưa rõ thời gian';
    try {
      return new Date(dateStr).toLocaleString('vi-VN', {
        hour: '2-digit',
        minute: '2-digit',
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
      });
    } catch (e) {
      return dateStr;
    }
  };

  // Lọc danh sách lịch hẹn
  const filteredAppointments = appointments.filter(app => {
    // Lọc theo trạng thái
    const rawStatus = (app.status || app.statusEnum || 'REQUESTED').toUpperCase();
    if (statusFilter !== 'ALL' && rawStatus !== statusFilter) {
      return false;
    }

    // Lọc theo từ khóa tìm kiếm
    const keyword = searchTerm.trim().toLowerCase();
    if (!keyword) return true;

    const petName = getPetName(app).toLowerCase();
    const ownerName = getOwnerName(app).toLowerCase();
    const vetName = getVetName(app).toLowerCase();
    const idStr = app.id.toString();

    return petName.includes(keyword) || 
           ownerName.includes(keyword) || 
           vetName.includes(keyword) || 
           idStr.includes(keyword);
  });

  const totalPages = Math.ceil(filteredAppointments.length / itemsPerPage);
  const paginatedAppointments = filteredAppointments.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  return (
    <AdminLayout>
      <div className="space-y-8 animate-fade-in font-sans">
        
        {/* Header Section */}
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div>
            <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Quản lý Lịch hẹn</h1>
            <p className="text-slate-500 text-sm mt-1">
              Xem danh sách lịch hẹn khám của phòng khám và quản lý trạng thái hủy lịch.
            </p>
          </div>
          <div>
            <button
              onClick={fetchAppointments}
              className="flex items-center gap-2 bg-white border border-slate-200 hover:bg-slate-50 text-slate-700 font-bold px-4 py-2.5 rounded-xl shadow-sm transition"
            >
              <RefreshCw className="h-4 w-4" />
              Tải lại
            </button>
          </div>
        </div>

        {/* Search & Filters */}
        <div className="bg-white border border-slate-200 p-5 rounded-2xl shadow-sm flex flex-col md:flex-row gap-4 items-center justify-between">
          <div className="relative w-full md:w-96">
            <span className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-450">
              <Search className="h-5 w-5 text-slate-400" />
            </span>
            <input
              type="text"
              placeholder="Tìm kiếm mã lịch hẹn, tên chủ nuôi, thú cưng..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-11 pr-4 py-2.5 w-full bg-slate-50 border border-slate-200 rounded-xl text-slate-800 text-sm focus:bg-white focus:outline-none focus:ring-2 focus:ring-teal-500 focus:border-teal-500 transition"
            />
          </div>

          <div className="flex gap-2 items-center w-full md:w-auto">
            <label className="text-slate-500 text-sm font-medium whitespace-nowrap">Trạng thái:</label>
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="px-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-slate-700 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 transition w-full md:w-48"
            >
              <option value="ALL">Tất cả trạng thái</option>
              <option value="REQUESTED">Chờ duyệt (Requested)</option>
              <option value="CONFIRMED">Đã xác nhận (Confirmed)</option>
              <option value="COMPLETED">Đã khám (Completed)</option>
              <option value="CANCELLED">Đã hủy (Cancelled)</option>
            </select>
          </div>
        </div>

        {/* Content Section */}
        {loading ? (
          <div className="flex h-64 items-center justify-center">
            <Loader2 className="h-10 w-10 text-teal-600 animate-spin" />
            <span className="ml-2 text-slate-500 font-medium">Đang tải danh sách lịch hẹn...</span>
          </div>
        ) : (
          <div className="bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-sm">
            <div className="overflow-x-auto">
              <table className="w-full text-left border-collapse text-sm">
                <thead>
                  <tr className="bg-slate-50 border-b border-slate-200 text-slate-650 font-bold text-xs uppercase tracking-wider">
                    <th className="px-6 py-4">Mã lịch hẹn</th>
                    <th className="px-6 py-4">Thời gian khám</th>
                    <th className="px-6 py-4">Chủ nuôi & Số điện thoại</th>
                    <th className="px-6 py-4">Thú cưng</th>
                    <th className="px-6 py-4">Bác sĩ phụ trách</th>
                    <th className="px-6 py-4">Lý do khám</th>
                    <th className="px-6 py-4">Trạng thái</th>
                    <th className="px-6 py-4 text-center">Thao tác</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100 text-slate-700">
                  {paginatedAppointments.length === 0 ? (
                    <tr>
                      <td colSpan={8} className="p-12 text-center text-slate-400 font-medium">
                        Không có lịch hẹn nào trùng khớp với bộ lọc tìm kiếm.
                      </td>
                    </tr>
                  ) : (
                    paginatedAppointments.map((app) => {
                      const statusStyle = getStatusTextAndStyle(app);
                      const rawStatus = (app.status || app.statusEnum || 'REQUESTED').toUpperCase();
                      const isCancelable = rawStatus !== 'CANCELLED' && rawStatus !== 'COMPLETED';
                      
                      return (
                        <tr key={app.id} className="hover:bg-slate-50/50 transition">
                          <td className="px-6 py-4 font-bold text-slate-900">#{app.id}</td>
                          <td className="px-6 py-4">
                            <span className="flex items-center gap-1.5 font-medium text-slate-800">
                              <Calendar className="h-4 w-4 text-teal-600 flex-shrink-0" />
                              {formatDateTime(app.appointmentAt || app.appointmentTime)}
                            </span>
                          </td>
                          <td className="px-6 py-4">
                            <div className="space-y-0.5">
                              <span className="font-semibold text-slate-850 flex items-center gap-1">
                                <User className="h-3.5 w-3.5 text-slate-400" />
                                {getOwnerName(app)}
                              </span>
                              <span className="text-xs text-slate-500 flex items-center gap-1">
                                <Phone className="h-3 w-3 text-slate-400" />
                                {getOwnerPhone(app)}
                              </span>
                            </div>
                          </td>
                          <td className="px-6 py-4">
                            <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-bold bg-teal-50 text-teal-700 border border-teal-100">
                              <Heart className="h-3 w-3" />
                              {getPetName(app)}
                            </span>
                          </td>
                          <td className="px-6 py-4">
                            <span className="font-medium text-slate-800 flex items-center gap-1">
                              <Stethoscope className="h-3.5 w-3.5 text-slate-400" />
                              {getVetName(app)}
                            </span>
                          </td>
                          <td className="px-6 py-4 max-w-xs truncate" title={getReason(app) || ''}>
                            {getReason(app)}
                          </td>
                          <td className="px-6 py-4">
                            <span className={`inline-flex items-center px-2.5 py-1.5 rounded-full text-xs font-bold border ${statusStyle.badge}`}>
                              {statusStyle.text}
                            </span>
                          </td>
                          <td className="px-6 py-4 text-center">
                            {isCancelable ? (
                              <button
                                onClick={() => openCancelModal(app)}
                                className="text-xs font-bold text-rose-600 bg-rose-50 border border-rose-100 hover:bg-rose-100/70 rounded-lg px-3 py-1.5 transition-all shadow-sm"
                              >
                                Hủy lịch hẹn
                              </button>
                            ) : (
                              <span className="text-xs text-slate-400 italic">Không thể hủy</span>
                            )}
                          </td>
                        </tr>
                      );
                    })
                  )}
                </tbody>
              </table>
            </div>
            
            {/* Phân trang */}
            {totalPages > 1 && (
              <div className="flex items-center justify-between border-t border-slate-100 bg-white px-6 py-4 rounded-b-2xl">
                <div className="flex flex-1 justify-between sm:hidden">
                  <button
                    onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                    disabled={currentPage === 1}
                    className="relative inline-flex items-center rounded-xl border border-slate-200 bg-white px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50 disabled:opacity-50 transition"
                  >
                    Trước
                  </button>
                  <button
                    onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
                    disabled={currentPage === totalPages}
                    className="relative ml-3 inline-flex items-center rounded-xl border border-slate-200 bg-white px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50 disabled:opacity-50 transition"
                  >
                    Sau
                  </button>
                </div>
                <div className="hidden sm:flex sm:flex-1 sm:items-center sm:justify-between">
                  <div>
                    <p className="text-sm text-slate-500 font-medium">
                      Hiển thị từ <span className="font-semibold text-slate-900">{Math.min((currentPage - 1) * itemsPerPage + 1, filteredAppointments.length)}</span> đến{' '}
                      <span className="font-semibold text-slate-900">{Math.min(currentPage * itemsPerPage, filteredAppointments.length)}</span> trong tổng số{' '}
                      <span className="font-semibold text-slate-900">{filteredAppointments.length}</span> lịch hẹn
                    </p>
                  </div>
                  <div>
                    <nav className="isolate inline-flex -space-x-px rounded-xl gap-1" aria-label="Pagination">
                      <button
                        onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                        disabled={currentPage === 1}
                        className="relative inline-flex items-center rounded-lg border border-slate-200 bg-white p-2 text-slate-500 hover:bg-slate-50 disabled:opacity-50 transition"
                      >
                        <ChevronLeft className="h-4 w-4" />
                      </button>
                      
                      {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
                        <button
                          key={page}
                          onClick={() => setCurrentPage(page)}
                          className={`relative inline-flex items-center rounded-lg px-3.5 py-1.5 text-sm font-bold transition ${
                            page === currentPage
                              ? 'z-10 bg-teal-600 text-white shadow-md shadow-teal-500/10'
                              : 'text-slate-600 border border-slate-200 bg-white hover:bg-slate-50'
                          }`}
                        >
                          {page}
                        </button>
                      ))}
                      
                      <button
                        onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
                        disabled={currentPage === totalPages}
                        className="relative inline-flex items-center rounded-lg border border-slate-200 bg-white p-2 text-slate-500 hover:bg-slate-50 disabled:opacity-50 transition"
                      >
                        <ChevronRight className="h-4 w-4" />
                      </button>
                    </nav>
                  </div>
                </div>
              </div>
            )}
          </div>
        )}

        {/* Modal xác nhận hủy */}
        {cancelModalOpen && selectedApp && (
          <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-sm z-50 flex items-center justify-center p-4">
            <div className="bg-white rounded-3xl max-w-md w-full p-6 shadow-2xl border border-slate-100 space-y-6 animate-scale-up">
              <div className="flex items-start space-x-4">
                <div className="p-3 bg-rose-50 text-rose-600 rounded-2xl border border-rose-100 flex-shrink-0">
                  <AlertCircle className="h-6 w-6" />
                </div>
                <div>
                  <h3 className="text-lg font-bold text-slate-900">Xác nhận Hủy Lịch Hẹn</h3>
                  <p className="text-sm text-slate-500 mt-1">
                    Bạn có chắc chắn muốn hủy lịch hẹn khám bệnh này không?
                  </p>
                </div>
              </div>

              {/* Thông tin tóm tắt lịch hẹn */}
              <div className="bg-slate-50 p-4 rounded-2xl border border-slate-100 text-sm space-y-2.5">
                <div className="flex justify-between">
                  <span className="text-slate-400 font-medium">Mã lịch hẹn:</span>
                  <span className="text-slate-800 font-bold">#{selectedApp.id}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-slate-400 font-medium">Khách hàng:</span>
                  <span className="text-slate-800 font-semibold">{getOwnerName(selectedApp)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-slate-400 font-medium">Thú cưng:</span>
                  <span className="text-slate-800 font-semibold">{getPetName(selectedApp)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-slate-400 font-medium">Giờ hẹn:</span>
                  <span className="text-slate-800 font-medium">
                    {formatDateTime(selectedApp.appointmentAt || selectedApp.appointmentTime)}
                  </span>
                </div>
              </div>

              <div className="text-xs text-slate-400 leading-relaxed">
                ⚠️ <strong>Lưu ý:</strong> Hành động này sẽ chuyển trạng thái của lịch hẹn thành <strong>ĐÃ HỦY</strong>. Khung giờ khám của bác sĩ sẽ được giải phóng cho khách hàng khác. Hành động này không thể hoàn tác.
              </div>

              <div className="flex space-x-3">
                <button
                  type="button"
                  disabled={submittingCancel}
                  onClick={closeCancelModal}
                  className="flex-1 py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold rounded-xl transition"
                >
                  Đóng
                </button>
                <button
                  type="button"
                  disabled={submittingCancel}
                  onClick={handleCancelAppointment}
                  className="flex-1 py-2.5 bg-rose-600 hover:bg-rose-700 text-white font-bold rounded-xl transition flex items-center justify-center gap-1.5 shadow-md shadow-rose-500/10"
                >
                  {submittingCancel ? (
                    <>
                      <Loader2 className="h-4 w-4 animate-spin" />
                      Đang hủy...
                    </>
                  ) : (
                    'Đồng ý hủy'
                  )}
                </button>
              </div>
            </div>
          </div>
        )}

      </div>
    </AdminLayout>
  );
};

export default Appointments;
