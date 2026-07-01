import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import VetLayout from '../../layouts/VetLayout';
import { Clock, FileText, CheckCircle, Loader2, Calendar } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import api from '../../services/api';
import { showToast } from '../../components/Toast';

interface Appointment {
  id: number;
  patientName: string;
  petName: string;
  petSpecies: string;
  petId: number;
  time: string;
  reason: string;
  status: string;
}

export const Schedule: React.FC = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedDate, setSelectedDate] = useState<string>(
    new Date().toISOString().split('T')[0] // Mặc định là ngày hôm nay
  );
  const [myDoctorId, setMyDoctorId] = useState<number | null>(null);

  // Tìm doctorId của bác sĩ dựa trên họ tên AppUser
  const fetchDoctorId = async () => {
    try {
      const res = await api.get('/doctors');
      const doc = (res.data || []).find((d: any) => d.name === user?.fullName);
      if (doc) {
        setMyDoctorId(doc.id);
        return doc.id;
      }
      return null;
    } catch (err) {
      console.error('Error fetching doctors list:', err);
      return null;
    }
  };

  const fetchAppointments = async (docId: number) => {
    setLoading(true);
    try {
      // GET /api/doctors/{id}/appointments?date=yyyy-MM-dd
      const res = await api.get(`/doctors/${docId}/appointments`, {
        params: { date: selectedDate }
      });
      const mapped = (res.data || []).map((app: any) => {
        const appointmentTimeLocal = app.appointmentTime 
          ? new Date(app.appointmentTime).toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' }) 
          : 'Chưa đặt';
        return {
          id: app.id,
          patientName: app.ownerName || 'Khách hàng',
          petName: app.petName || 'Thú cưng',
          petSpecies: app.species || 'Chó/Mèo',
          petId: app.petId || 0,
          time: appointmentTimeLocal,
          reason: app.reason || 'Khám tổng quát',
          status: app.status || 'CONFIRMED'
        };
      });
      setAppointments(mapped);
    } catch (err) {
      console.error('Error fetching appointments:', err);
      showToast('Tải danh sách lịch hẹn thất bại!', 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const loadData = async () => {
      let docId = myDoctorId;
      if (!docId) {
        docId = await fetchDoctorId();
      }
      if (docId) {
        await fetchAppointments(docId);
      } else {
        setLoading(false);
      }
    };
    loadData();
  }, [selectedDate, user]);

  const handleDiagnose = (app: Appointment) => {
    // Chuyển sang trang tạo bệnh án và truyền thông tin thú cưng qua query params
    navigate(`/vet/records/create?petId=${app.petId}&patientName=${encodeURIComponent(app.patientName)}&appointmentId=${app.id}`);
  };

  return (
    <VetLayout>
      <div className="space-y-8 animate-fade-in font-sans">
        
        {/* Tiêu đề & Chọn ngày */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between space-y-4 sm:space-y-0">
          <div>
            <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Doctor Schedule</h1>
            <p className="text-slate-500 text-sm mt-1">Xem danh sách lịch hẹn khám bệnh trực tiếp từ cơ sở dữ liệu</p>
          </div>
          
          <div className="flex items-center space-x-2 bg-white px-3 py-2 border border-slate-200 rounded-2xl shadow-xs self-start">
            <span className="text-xs font-bold text-slate-500 uppercase flex items-center gap-1">
              <Calendar className="h-4 w-4 text-slate-400" /> Chọn ngày:
            </span>
            <input
              type="date"
              value={selectedDate}
              onChange={(e) => setSelectedDate(e.target.value)}
              className="border border-slate-250 rounded-xl px-2.5 py-1 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 font-semibold text-slate-700 cursor-pointer bg-white"
            />
          </div>
        </div>

        {/* Nội dung lịch khám */}
        {loading ? (
          <div className="flex h-64 items-center justify-center">
            <Loader2 className="h-10 w-10 text-teal-600 animate-spin" />
            <span className="ml-2 text-slate-500 font-medium">Đang tải lịch hẹn...</span>
          </div>
        ) : !myDoctorId ? (
          <div className="border border-dashed border-red-200 bg-red-50/50 rounded-2xl p-12 text-center text-red-600 text-sm font-medium">
            Tài khoản bác sĩ của bạn chưa được liên kết với hồ sơ bác sĩ phòng khám (Doctor Profile). Vui lòng liên hệ Admin để cấu hình tên họ trùng khớp.
          </div>
        ) : appointments.length === 0 ? (
          <div className="border border-dashed border-slate-200 bg-white rounded-2xl p-12 text-center text-slate-400 text-sm">
            Không có lịch hẹn khám nào trong ngày {selectedDate}.
          </div>
        ) : (
          <div className="bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-sm">
            <div className="bg-slate-50 border-b border-slate-200 px-6 py-4 flex items-center justify-between">
              <h3 className="font-bold text-slate-800 text-base">Danh sách ca khám bệnh ({selectedDate})</h3>
              <span className="text-xs font-bold bg-teal-50 text-teal-700 py-1 px-3 rounded-full uppercase">
                {appointments.length} ca khám
              </span>
            </div>
            
            <div className="divide-y divide-slate-100">
              {appointments.map((app) => (
                <div key={app.id} className="p-6 flex flex-col md:flex-row md:justify-between md:items-center gap-4 hover:bg-slate-50/50 transition">
                  <div className="space-y-3">
                    <div className="flex items-center space-x-3">
                      <span className="text-sm font-semibold text-slate-700 flex items-center gap-1">
                        <Clock className="h-4 w-4 text-slate-400" /> {app.time}
                      </span>
                      <span className={`text-xs font-bold uppercase tracking-wider py-0.5 px-2.5 rounded-full ${
                        app.status === 'COMPLETED' || app.status === 'Completed' 
                          ? 'bg-green-50 text-green-700' 
                          : app.status === 'REQUESTED' 
                          ? 'bg-blue-50 text-blue-700' 
                          : 'bg-amber-50 text-amber-700'
                      }`}>
                        {app.status}
                      </span>
                    </div>

                    <div className="text-sm">
                      <p className="text-slate-800 font-bold">Chủ nuôi: {app.patientName} • Thú cưng: {app.petName} ({app.petSpecies})</p>
                      <p className="text-slate-500 flex items-center gap-1.5 mt-1 text-xs">
                        <FileText className="h-3.5 w-3.5" /> Triệu chứng/Lý do: {app.reason}
                      </p>
                    </div>
                  </div>

                  {app.status !== 'COMPLETED' && app.status !== 'Completed' && (
                    <div>
                      <button 
                        onClick={() => handleDiagnose(app)}
                        className="flex items-center space-x-2 bg-indigo-650 hover:bg-indigo-750 text-white font-bold py-2 px-4 rounded-xl text-sm shadow-xs transition"
                      >
                        <CheckCircle className="h-4 w-4" />
                        <span>Khám bệnh & Kê đơn</span>
                      </button>
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </VetLayout>
  );
};

export default Schedule;
