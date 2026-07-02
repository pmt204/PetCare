import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { CheckCircle2, XCircle, Loader2, Calendar, User, ArrowRight } from 'lucide-react';
import api from '../../services/api';
import { showToast } from '../../components/Toast';

interface AppointmentData {
  id: number;
  appointmentAt: string;
  reasonForVisit: string;
  paymentMethod: string;
  paymentStatus: string;
  pet?: {
    name: string;
  };
  veterinarian?: {
    fullName: string;
  };
}

export const PaymentCallback: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [status, setStatus] = useState<'loading' | 'success' | 'failed'>('loading');
  const [appointment, setAppointment] = useState<AppointmentData | null>(null);
  const [amount, setAmount] = useState<string>('');
  const [txnNo, setTxnNo] = useState<string>('');

  useEffect(() => {
    const verifyPayment = async () => {
      const searchParams = new URLSearchParams(location.search);
      const paramsObj: Record<string, string> = {};
      searchParams.forEach((value, key) => {
        paramsObj[key] = value;
      });

      const responseCode = searchParams.get('vnp_ResponseCode');
      const rawAmount = searchParams.get('vnp_Amount');
      const transactionNo = searchParams.get('vnp_TransactionNo') || '';

      if (rawAmount) {
        // VNPay amount is multiplied by 100, format it back
        const amt = parseInt(rawAmount) / 100;
        setAmount(amt.toLocaleString('vi-VN') + ' VND');
      }
      setTxnNo(transactionNo);

      if (responseCode !== '00') {
        setStatus('failed');
        showToast('Thanh toán VNPay không thành công hoặc đã bị hủy.', 'error');
        return;
      }

      try {
        // Call backend callback to verify signature and update appointment payment status
        const response = await api.get('/appointments/payment-callback', {
          params: paramsObj,
        });

        setAppointment(response.data);
        setStatus('success');
        showToast('Thanh toán lịch hẹn thành công!', 'success');
      } catch (error) {
        console.error('Error verifying payment:', error);
        setStatus('failed');
        showToast('Không thể xác thực giao dịch thanh toán.', 'error');
      }
    };

    verifyPayment();
  }, [location]);

  return (
    <div className="min-h-screen bg-slate-50 flex items-center justify-center p-4">
      <div className="bg-white rounded-3xl p-6 sm:p-8 max-w-md w-full shadow-2xl border border-slate-100/80 text-center space-y-6 animate-scale-up">
        {status === 'loading' && (
          <div className="py-8 space-y-4 flex flex-col items-center">
            <Loader2 className="h-12 w-12 text-teal-600 animate-spin" />
            <h3 className="text-lg font-bold text-slate-800">Đang xác thực giao dịch</h3>
            <p className="text-slate-400 text-xs max-w-xs leading-relaxed">
              Vui lòng không đóng trình duyệt hoặc tải lại trang trong khi chúng tôi xử lý thông tin thanh toán của bạn.
            </p>
          </div>
        )}

        {status === 'success' && (
          <div className="space-y-6">
            <div className="flex justify-center">
              <div className="h-16 w-16 bg-teal-50 rounded-full flex items-center justify-center text-teal-600 shadow-inner">
                <CheckCircle2 className="h-10 w-10" />
              </div>
            </div>

            <div className="space-y-2">
              <span className="text-teal-600 font-bold text-[11px] uppercase tracking-widest bg-teal-50 py-1 px-3 rounded-full inline-block">Giao dịch thành công</span>
              <h2 className="text-2xl font-extrabold text-slate-900 tracking-tight">Thanh toán hoàn tất!</h2>
              <p className="text-slate-500 text-sm">Cảm ơn bạn đã lựa chọn dịch vụ của PetCare.</p>
            </div>

            {/* Transaction summary details card */}
            <div className="bg-slate-50 border border-slate-200/80 p-5 rounded-2xl text-left space-y-3 text-sm">
              <div className="flex justify-between items-center pb-2 border-b border-slate-200">
                <span className="text-slate-500">Mã giao dịch VNPay:</span>
                <span className="font-semibold text-slate-700 font-mono text-xs">{txnNo}</span>
              </div>
              <div className="flex justify-between items-center pb-2 border-b border-slate-200">
                <span className="text-slate-500">Số tiền thanh toán:</span>
                <span className="font-bold text-teal-700">{amount}</span>
              </div>
              {appointment && (
                <>
                  <div className="flex items-center gap-3 pt-1">
                    <Calendar className="h-4 w-4 text-slate-400 shrink-0" />
                    <div>
                      <span className="text-xs text-slate-400 block leading-none">Thời gian hẹn</span>
                      <span className="font-bold text-slate-700 text-xs">
                        {new Date(appointment.appointmentAt).toLocaleDateString('vi-VN')} lúc{' '}
                        {new Date(appointment.appointmentAt).toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })}
                      </span>
                    </div>
                  </div>
                  <div className="flex items-center gap-3">
                    <User className="h-4 w-4 text-slate-400 shrink-0" />
                    <div>
                      <span className="text-xs text-slate-400 block leading-none">Bác sĩ phụ trách</span>
                      <span className="font-bold text-slate-700 text-xs">
                        {appointment.veterinarian?.fullName || 'Bác sĩ ngẫu nhiên'}
                      </span>
                    </div>
                  </div>
                  {appointment.pet && (
                    <div className="flex items-center gap-3">
                      <span className="text-xs text-slate-400 shrink-0 w-4 text-center">🐾</span>
                      <div>
                        <span className="text-xs text-slate-400 block leading-none">Thú cưng</span>
                        <span className="font-bold text-slate-700 text-xs">
                          {appointment.pet.name}
                        </span>
                      </div>
                    </div>
                  )}
                </>
              )}
            </div>

            <button
              onClick={() => navigate('/owner/appointments')}
              className="w-full bg-gradient-to-r from-teal-600 to-teal-500 hover:from-teal-700 hover:to-teal-600 text-white font-bold py-3.5 px-6 rounded-2xl text-sm shadow-md hover:shadow-lg hover:shadow-teal-500/10 transition duration-200 flex items-center justify-center gap-2 group"
            >
              Xem lịch hẹn của tôi
              <ArrowRight className="h-4 w-4 group-hover:translate-x-0.5 transition" />
            </button>
          </div>
        )}

        {status === 'failed' && (
          <div className="space-y-6">
            <div className="flex justify-center">
              <div className="h-16 w-16 bg-red-50 rounded-full flex items-center justify-center text-red-500 shadow-inner">
                <XCircle className="h-10 w-10" />
              </div>
            </div>

            <div className="space-y-2">
              <span className="text-red-500 font-bold text-[11px] uppercase tracking-widest bg-red-50 py-1 px-3 rounded-full inline-block">Giao dịch thất bại</span>
              <h2 className="text-2xl font-extrabold text-slate-900 tracking-tight">Thanh toán thất bại!</h2>
              <p className="text-slate-500 text-sm">Giao dịch không thành công hoặc đã bị hủy bởi người dùng.</p>
            </div>

            <div className="flex flex-col gap-2">
              <button
                onClick={() => navigate('/owner/pets')}
                className="w-full bg-slate-800 hover:bg-slate-950 text-white font-bold py-3.5 px-6 rounded-2xl text-sm shadow-md transition duration-200"
              >
                Quay lại Trang chủ
              </button>
              <button
                onClick={() => navigate('/owner/pets?book=true')}
                className="w-full border border-slate-200 hover:bg-slate-50 text-slate-600 font-bold py-3 px-6 rounded-2xl text-sm transition"
              >
                Thử đặt lịch lại
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default PaymentCallback;
