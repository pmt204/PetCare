import React, { useEffect, useState } from 'react';
import AdminLayout from '../../layouts/AdminLayout';
import { BarChart2, Users, Calendar, Star, Loader2 } from 'lucide-react';
import api from '../../services/api';
import { showToast } from '../../components/Toast';

interface DoctorWorkload {
  id: number;
  name: string;
  specialty: string;
  appointmentsCount: number;
  workingHours: string;
  status: 'ACTIVE' | 'ON_LEAVE';
}

export const Reports: React.FC = () => {
  const [workloads, setWorkloads] = useState<DoctorWorkload[]>([]);
  const [loading, setLoading] = useState(true);
  const [avgRating, setAvgRating] = useState<number>(5.0);
  const [selectedDate, setSelectedDate] = useState<string>(
    new Date().toISOString().split('T')[0]
  );

  const fetchWorkloads = async () => {
    setLoading(true);
    try {
      // GET /api/admin/reports/workloads?date=yyyy-MM-dd
      const res = await api.get('/admin/reports/workloads', {
        params: { date: selectedDate }
      });
      setWorkloads(res.data || []);

      // Fetch doctors from database to calculate average satisfaction rating dynamically
      const doctorsRes = await api.get('/doctors');
      const docs = doctorsRes.data || [];
      if (docs.length > 0) {
        const sum = docs.reduce((acc: number, d: any) => acc + (d.rating || 5.0), 0);
        setAvgRating(sum / docs.length);
      } else {
        setAvgRating(5.0);
      }
    } catch (err) {
      console.error('Error fetching workloads or doctors:', err);
      showToast('Tải báo cáo hiệu suất bác sĩ thất bại!', 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchWorkloads();
  }, [selectedDate]);

  const totalAppointments = workloads.reduce((sum, item) => sum + item.appointmentsCount, 0);
  const activeVetsCount = workloads.filter(w => w.status === 'ACTIVE').length;

  return (
    <AdminLayout>
      <div className="space-y-8 animate-fade-in font-sans">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div>
            <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Clinic Workload Reports</h1>
            <p className="text-slate-500 text-sm mt-1">Review active staff performance, veterinarian workloads, and clinic throughput</p>
          </div>
          {/* Date Picker */}
          <div className="flex items-center space-x-2 bg-white border border-slate-200 rounded-xl px-3 py-1.5 shadow-xs">
            <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Xem theo ngày:</span>
            <input
              type="date"
              value={selectedDate}
              onChange={(e) => setSelectedDate(e.target.value)}
              className="border-0 focus:ring-0 text-sm font-semibold text-slate-700 cursor-pointer p-0"
            />
          </div>
        </div>

        {loading ? (
          <div className="flex h-64 items-center justify-center">
            <Loader2 className="h-10 w-10 text-teal-600 animate-spin" />
            <span className="ml-2 text-slate-500 font-medium">Đang thống kê hiệu suất...</span>
          </div>
        ) : (
          <>
            {/* Highlight widgets */}
            <div className="grid grid-cols-1 sm:grid-cols-4 gap-6">
              <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center space-x-4">
                <div className="bg-indigo-50 p-3 rounded-xl text-indigo-650 border border-indigo-150">
                  <Calendar className="h-6 w-6" />
                </div>
                <div>
                  <span className="text-slate-400 text-xs block uppercase">Lượt khám trong ngày</span>
                  <span className="text-2xl font-bold text-slate-800">{totalAppointments}</span>
                </div>
              </div>
              <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center space-x-4">
                <div className="bg-green-50 p-3 rounded-xl text-green-600 border border-green-150">
                  <Users className="h-6 w-6" />
                </div>
                <div>
                  <span className="text-slate-400 text-xs block uppercase">Bác sĩ hoạt động</span>
                  <span className="text-2xl font-bold text-slate-800">{activeVetsCount}</span>
                </div>
              </div>
              <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center space-x-4">
                <div className="bg-amber-50 p-3 rounded-xl text-amber-600 border border-amber-150">
                  <Star className="h-6 w-6" />
                </div>
                <div>
                  <span className="text-slate-400 text-xs block uppercase">Đánh giá trung bình</span>
                  <span className="text-2xl font-bold text-slate-800">{avgRating.toFixed(1)} / 5</span>
                </div>
              </div>
              <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center space-x-4">
                <div className="bg-purple-50 p-3 rounded-xl text-purple-600 border border-purple-150">
                  <BarChart2 className="h-6 w-6" />
                </div>
                <div>
                  <span className="text-slate-400 text-xs block uppercase">Khung giờ bận nhất</span>
                  <span className="text-sm font-bold text-slate-800 mt-1 block">Sáng (09:00 - 11:00)</span>
                </div>
              </div>
            </div>

            {/* Workload breakdown table */}
            <div className="bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-sm">
              <div className="bg-slate-50 border-b border-slate-200 px-6 py-4 flex items-center justify-between">
                <h3 className="font-bold text-slate-800 text-base">Bảng phân bổ hiệu suất bác sĩ ({selectedDate})</h3>
                <span className="text-xs font-semibold bg-indigo-50 text-indigo-700 py-1 px-3 rounded-full uppercase">Live Update</span>
              </div>

              <div className="overflow-x-auto text-sm">
                <table className="min-w-full divide-y divide-slate-200 text-left text-slate-700">
                  <thead className="bg-slate-50 text-slate-400 uppercase text-xs font-bold tracking-wider">
                    <tr>
                      <th className="px-6 py-3.5">Họ tên bác sĩ</th>
                      <th className="px-6 py-3.5">Chuyên khoa</th>
                      <th className="px-6 py-3.5 text-center">Số ca khám trong ngày</th>
                      <th className="px-6 py-3.5">Ca làm việc</th>
                      <th className="px-6 py-3.5">Trạng thái</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-200 bg-white">
                    {workloads.length === 0 ? (
                      <tr>
                        <td colSpan={5} className="px-6 py-8 text-center text-slate-450">
                          Chưa có thông tin bác sĩ hoặc lịch làm việc nào được ghi nhận.
                        </td>
                      </tr>
                    ) : (
                      workloads.map((doc) => (
                        <tr key={doc.id} className="hover:bg-slate-50/50 transition">
                          <td className="px-6 py-4 font-bold text-slate-800">{doc.name}</td>
                          <td className="px-6 py-4">
                            <span className="bg-slate-100 text-slate-700 px-2.5 py-0.5 rounded-full text-xs font-semibold">
                              {doc.specialty}
                            </span>
                          </td>
                          <td className="px-6 py-4 text-center font-bold text-slate-700">
                            {doc.appointmentsCount} lượt khám
                          </td>
                          <td className="px-6 py-4 text-slate-500">{doc.workingHours}</td>
                          <td className="px-6 py-4">
                            <span className={`inline-flex items-center gap-1 text-xs font-bold ${doc.status === 'ACTIVE' ? 'text-green-600' : 'text-slate-450'}`}>
                              <span className={`h-2 w-2 rounded-full ${doc.status === 'ACTIVE' ? 'bg-green-500' : 'bg-slate-300'}`} />
                              {doc.status}
                            </span>
                          </td>
                        </tr>
                      ))
                    )}
                  </tbody>
                </table>
              </div>
            </div>
          </>
        )}
      </div>
    </AdminLayout>
  );
};

export default Reports;
