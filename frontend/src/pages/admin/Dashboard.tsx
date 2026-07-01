import React, { useEffect, useState } from 'react';
import AdminLayout from '../../layouts/AdminLayout';
import { DollarSign, FileText, TrendingUp, Download, Loader2, Clock, CheckCircle } from 'lucide-react';
import api from '../../services/api';
import { showToast } from '../../components/Toast';

interface InvoiceItem {
  id: number;
  petName: string;
  ownerName: string;
  totalAmount: number;
  paymentStatus: 'PAID' | 'PENDING';
  createdAtRaw: string; // ISO string
  createdAtFormatted: string; // vi-VN formatted
  servicesString: string;
}

export const Dashboard: React.FC = () => {
  const [invoices, setInvoices] = useState<InvoiceItem[]>([]);
  const [loading, setLoading] = useState(true);
  
  // Trạng thái bộ lọc xuất Excel
  const [exportMode, setExportMode] = useState<'all' | 'day' | 'month' | 'year'>('all');
  const [filterDay, setFilterDay] = useState<string>(new Date().toISOString().split('T')[0]);
  const [filterMonth, setFilterMonth] = useState<string>(new Date().toISOString().slice(0, 7)); // YYYY-MM
  const [filterYear, setFilterYear] = useState<string>(new Date().getFullYear().toString());

  const fetchDashboardData = async () => {
    try {
      const response = await api.get('/admin/invoices');
      const mapped = (response.data || []).map((inv: any) => {
        const appointment = inv.appointment || {};
        const pet = appointment.pet || {};
        const owner = appointment.owner || {};
        const services = inv.services || [];
        const servicesStr = services.map((s: any) => s.name).join(', ') || 'Khám bệnh';
        
        return {
          id: inv.id,
          petName: pet.name || appointment.patientName || 'Thú cưng',
          ownerName: owner.fullName || appointment.patientName || 'Khách hàng',
          totalAmount: inv.totalAmount || 0,
          paymentStatus: inv.paymentStatus || 'PENDING',
          createdAtRaw: inv.createdAt || '',
          createdAtFormatted: inv.createdAt 
            ? new Date(inv.createdAt).toLocaleString('vi-VN') 
            : 'Chưa rõ ngày',
          servicesString: servicesStr
        };
      });
      setInvoices(mapped);
    } catch (err) {
      console.error('Error fetching invoices for dashboard:', err);
      showToast('Tải số liệu doanh thu thất bại!', 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDashboardData();
  }, []);

  // Tính toán số liệu tổng quan
  const totalSalesPaid = invoices
    .filter(inv => inv.paymentStatus === 'PAID')
    .reduce((sum, inv) => sum + inv.totalAmount, 0);

  const totalSalesPending = invoices
    .filter(inv => inv.paymentStatus === 'PENDING')
    .reduce((sum, inv) => sum + inv.totalAmount, 0);

  const totalInvoicesCount = invoices.length;
  const paidInvoicesCount = invoices.filter(inv => inv.paymentStatus === 'PAID').length;

  // Lọc dữ liệu theo cấu hình xuất Excel hiện tại
  const getFilteredInvoices = () => {
    return invoices.filter(inv => {
      if (!inv.createdAtRaw) return false;
      const date = new Date(inv.createdAtRaw);
      
      // Chuyển múi giờ local của ngày tạo hóa đơn
      const localYear = date.getFullYear().toString();
      const localMonth = (date.getMonth() + 1).toString().padStart(2, '0');
      const localDay = date.getDate().toString().padStart(2, '0');
      
      const ymd = `${localYear}-${localMonth}-${localDay}`; // YYYY-MM-DD
      const ym = `${localYear}-${localMonth}`; // YYYY-MM

      if (exportMode === 'day') {
        return ymd === filterDay;
      }
      if (exportMode === 'month') {
        return ym === filterMonth;
      }
      if (exportMode === 'year') {
        return localYear === filterYear;
      }
      return true; // Mode 'all'
    });
  };

  const filteredData = getFilteredInvoices();
  const totalFilteredAmount = filteredData.reduce((sum, inv) => sum + inv.totalAmount, 0);

  // Hàm xuất file Excel (CSV dạng UTF-8 BOM)
  const handleExportExcel = () => {
    if (filteredData.length === 0) {
      showToast('Không có dữ liệu hóa đơn nào trùng khớp với bộ lọc!', 'info');
      return;
    }

    const csvHeaders = '\ufeffMã hóa đơn,Ngày tạo,Tên khách hàng,Tên thú cưng,Dịch vụ sử dụng,Tổng tiền (VNĐ),Trạng thái\n';
    const csvRows = filteredData.map(inv => 
      `"${inv.id}","${inv.createdAtFormatted}","${inv.ownerName}","${inv.petName}","${inv.servicesString}","${inv.totalAmount}","${inv.paymentStatus}"`
    ).join('\n');

    const blob = new Blob([csvHeaders + csvRows], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.setAttribute('href', url);

    let filename = 'bao_cao_doanh_thu_tat_ca.csv';
    if (exportMode === 'day') {
      filename = `bao_cao_doanh_thu_ngay_${filterDay}.csv`;
    } else if (exportMode === 'month') {
      filename = `bao_cao_doanh_thu_thang_${filterMonth}.csv`;
    } else if (exportMode === 'year') {
      filename = `bao_cao_doanh_thu_nam_${filterYear}.csv`;
    }

    link.setAttribute('download', filename);
    link.style.visibility = 'hidden';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    showToast('Xuất báo cáo Excel thành công!', 'success');
  };

  return (
    <AdminLayout>
      <div className="space-y-8 animate-fade-in font-sans">
        <div>
          <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Clinic Admin Dashboard</h1>
          <p className="text-slate-500 text-sm mt-1">Real-time revenue metrics, financial reports, and transaction details</p>
        </div>

        {loading ? (
          <div className="flex h-64 items-center justify-center">
            <Loader2 className="h-10 w-10 text-teal-600 animate-spin" />
            <span className="ml-2 text-slate-500 font-medium">Đang tổng hợp số liệu...</span>
          </div>
        ) : (
          <>
            {/* Top Cards Grid */}
            <div className="grid grid-cols-1 sm:grid-cols-4 gap-6">
              
              {/* Card 1: Doanh thu thực tế */}
              <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center space-x-4">
                <div className="bg-emerald-50 p-3 rounded-xl text-emerald-600 border border-emerald-150">
                  <CheckCircle className="h-6 w-6" />
                </div>
                <div>
                  <span className="text-slate-400 text-xs block uppercase font-bold">Doanh thu thực tế</span>
                  <span className="text-xl font-bold text-slate-800">{totalSalesPaid.toLocaleString('vi-VN')} ₫</span>
                  <span className="text-[10px] text-slate-400 block mt-0.5">{paidInvoicesCount} hóa đơn đã thu</span>
                </div>
              </div>

              {/* Card 2: Doanh thu chờ thu */}
              <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center space-x-4">
                <div className="bg-amber-50 p-3 rounded-xl text-amber-600 border border-amber-150">
                  <Clock className="h-6 w-6" />
                </div>
                <div>
                  <span className="text-slate-400 text-xs block uppercase font-bold">Doanh thu chờ thu</span>
                  <span className="text-xl font-bold text-slate-800">{totalSalesPending.toLocaleString('vi-VN')} ₫</span>
                  <span className="text-[10px] text-slate-400 block mt-0.5">{totalInvoicesCount - paidInvoicesCount} hóa đơn chờ</span>
                </div>
              </div>

              {/* Card 3: Tổng doanh thu dự kiến */}
              <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center space-x-4">
                <div className="bg-indigo-50 p-3 rounded-xl text-indigo-600 border border-indigo-150">
                  <DollarSign className="h-6 w-6" />
                </div>
                <div>
                  <span className="text-slate-400 text-xs block uppercase font-bold">Tổng doanh thu</span>
                  <span className="text-xl font-bold text-slate-800">{(totalSalesPaid + totalSalesPending).toLocaleString('vi-VN')} ₫</span>
                  <span className="text-[10px] text-slate-400 block mt-0.5">Tổng số tiền trên sổ sách</span>
                </div>
              </div>

              {/* Card 4: Tổng hóa đơn */}
              <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center space-x-4">
                <div className="bg-purple-50 p-3 rounded-xl text-purple-600 border border-purple-150">
                  <FileText className="h-6 w-6" />
                </div>
                <div>
                  <span className="text-slate-400 text-xs block uppercase font-bold">Tổng số hóa đơn</span>
                  <span className="text-xl font-bold text-slate-800">{totalInvoicesCount}</span>
                  <span className="text-[10px] text-slate-400 block mt-0.5">Tất cả giao dịch phát sinh</span>
                </div>
              </div>

            </div>

            {/* Excel Exporter panel & Data preview */}
            <div className="bg-white border border-slate-200 rounded-2xl shadow-sm p-6 space-y-6">
              
              <div className="flex flex-col md:flex-row md:items-center justify-between border-b border-slate-100 pb-5 space-y-4 md:space-y-0">
                <div className="space-y-1">
                  <h3 className="font-bold text-slate-800 text-lg flex items-center gap-1.5">
                    <TrendingUp className="h-5 w-5 text-teal-600" />
                    <span>Xuất báo cáo doanh thu Excel</span>
                  </h3>
                  <p className="text-slate-400 text-xs">Lọc hóa đơn phát sinh theo ngày, tháng, năm hoặc xuất toàn bộ dữ liệu lịch sử</p>
                </div>
                
                {/* Nút Xuất Excel */}
                <button
                  onClick={handleExportExcel}
                  className="bg-emerald-600 hover:bg-emerald-700 text-white font-bold py-2.5 px-5 rounded-xl shadow-xs text-sm transition flex items-center justify-center space-x-2 self-start"
                >
                  <Download className="h-4 w-4" />
                  <span>Xuất file Excel</span>
                </button>
              </div>

              {/* Bộ lọc báo cáo */}
              <div className="grid grid-cols-1 md:grid-cols-3 gap-6 bg-slate-50 p-4 rounded-xl border border-slate-200/60">
                
                {/* Cột 1: Chọn Chế độ */}
                <div className="flex flex-col space-y-1.5">
                  <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Chế độ xuất báo cáo</span>
                  <select
                    value={exportMode}
                    onChange={(e) => setExportMode(e.target.value as 'all' | 'day' | 'month' | 'year')}
                    className="border border-slate-200 rounded-xl px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 font-semibold text-slate-700 cursor-pointer bg-white"
                  >
                    <option value="all">Tất cả thời gian (Từ trước đến nay)</option>
                    <option value="day">Xuất theo ngày cố định</option>
                    <option value="month">Xuất theo tháng cố định</option>
                    <option value="year">Xuất theo năm cố định</option>
                  </select>
                </div>

                {/* Cột 2: Chọn Thời gian tương ứng */}
                <div className="flex flex-col space-y-1.5 justify-center">
                  <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Chọn thời gian cụ thể</span>
                  
                  {exportMode === 'all' && (
                    <div className="text-slate-500 text-sm font-semibold py-2">
                      📁 Xuất tất cả các hóa đơn trong lịch sử phòng khám
                    </div>
                  )}

                  {exportMode === 'day' && (
                    <input
                      type="date"
                      value={filterDay}
                      onChange={(e) => setFilterDay(e.target.value)}
                      className="border border-slate-200 rounded-xl px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 font-semibold text-slate-700 cursor-pointer bg-white"
                    />
                  )}

                  {exportMode === 'month' && (
                    <input
                      type="month"
                      value={filterMonth}
                      onChange={(e) => setFilterMonth(e.target.value)}
                      className="border border-slate-200 rounded-xl px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 font-semibold text-slate-700 cursor-pointer bg-white"
                    />
                  )}

                  {exportMode === 'year' && (
                    <select
                      value={filterYear}
                      onChange={(e) => setFilterYear(e.target.value)}
                      className="border border-slate-200 rounded-xl px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-teal-500 font-semibold text-slate-700 cursor-pointer bg-white"
                    >
                      <option value="2026">Năm 2026</option>
                      <option value="2025">Năm 2025</option>
                      <option value="2024">Năm 2024</option>
                    </select>
                  )}
                </div>

                {/* Cột 3: Tóm tắt kết quả khớp */}
                <div className="flex flex-col space-y-1.5 justify-center bg-white border border-slate-200 p-3.5 rounded-xl text-center">
                  <span className="text-xs font-bold text-slate-400 uppercase">Khớp bộ lọc</span>
                  <span className="text-base font-extrabold text-teal-600 block mt-0.5">{totalFilteredAmount.toLocaleString('vi-VN')} ₫</span>
                  <span className="text-[10px] text-slate-400 block">{filteredData.length} hóa đơn phát sinh</span>
                </div>

              </div>

              {/* Bảng xem trước dữ liệu lọc (Data preview) */}
              <div className="border border-slate-200 rounded-xl overflow-hidden shadow-xs">
                <div className="bg-slate-50 border-b border-slate-200 px-5 py-3">
                  <h4 className="font-bold text-slate-800 text-sm">Xem trước dữ liệu sẽ xuất ({filteredData.length} hóa đơn)</h4>
                </div>
                <div className="overflow-x-auto text-xs max-h-[300px] overflow-y-auto">
                  <table className="min-w-full divide-y divide-slate-200 text-left text-slate-650">
                    <thead className="bg-slate-50 font-bold text-slate-400 uppercase tracking-wider">
                      <tr>
                        <th className="px-5 py-2.5">Hóa đơn</th>
                        <th className="px-5 py-2.5">Thời gian</th>
                        <th className="px-5 py-2.5">Chủ nuôi</th>
                        <th className="px-5 py-2.5">Thú cưng</th>
                        <th className="px-5 py-2.5">Tổng tiền</th>
                        <th className="px-5 py-2.5">Trạng thái</th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-100 bg-white">
                      {filteredData.length === 0 ? (
                        <tr>
                          <td colSpan={6} className="px-5 py-8 text-center text-slate-400">
                            Không có hóa đơn nào khớp với khoảng thời gian đã chọn.
                          </td>
                        </tr>
                      ) : (
                        filteredData.map((inv) => (
                          <tr key={inv.id} className="hover:bg-slate-50/40">
                            <td className="px-5 py-3 font-semibold text-slate-700">#{inv.id}</td>
                            <td className="px-5 py-3 text-slate-500">{inv.createdAtFormatted}</td>
                            <td className="px-5 py-3 font-medium text-slate-700">{inv.ownerName}</td>
                            <td className="px-5 py-3 text-slate-600">{inv.petName}</td>
                            <td className="px-5 py-3 font-bold text-slate-800">{inv.totalAmount.toLocaleString('vi-VN')} ₫</td>
                            <td className="px-5 py-3">
                              <span className={`px-2 py-0.5 rounded-full text-[9px] font-bold ${inv.paymentStatus === 'PAID' ? 'bg-green-50 text-green-700' : 'bg-amber-50 text-amber-700'}`}>
                                {inv.paymentStatus}
                              </span>
                            </td>
                          </tr>
                        ))
                      )}
                    </tbody>
                  </table>
                </div>
              </div>

            </div>
          </>
        )}
      </div>
    </AdminLayout>
  );
};

export default Dashboard;
