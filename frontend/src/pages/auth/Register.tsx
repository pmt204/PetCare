import React from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import api from '../../services/api';
import { showToast } from '../../components/Toast';

export const Register: React.FC = () => {
  const navigate = useNavigate();
  const { register, handleSubmit, watch, formState: { errors } } = useForm();
  const [errorMsg, setErrorMsg] = React.useState('');
  const [loading, setLoading] = React.useState(false);

  const password = watch('password');

  const onSubmit = async (data: any) => {
    setErrorMsg('');
    setLoading(true);
    try {
      // Call backend signup API
      await api.post('/auth/signup', {
        fullName: data.fullName,
        email: data.email,
        username: data.username,
        password: data.password,
        role: 'OWNER' // Mặc định khách hàng tự đăng ký là vai trò OWNER (Chủ nuôi)
      });

      showToast('Đăng ký tài khoản thành công! Đang chuyển hướng...', 'success');
      
      // Chuyển hướng sang trang đăng nhập sau 1.5 giây
      setTimeout(() => {
        navigate('/login');
      }, 1500);
    } catch (err: any) {
      console.error('Registration error:', err);
      const serverMessage = err.response?.data?.message || err.message;
      
      // Hỗ trợ dịch một số lỗi phổ biến từ backend sang Tiếng Việt
      if (serverMessage?.includes('Username is already taken')) {
        setErrorMsg('Tên tài khoản này đã được sử dụng!');
      } else if (serverMessage?.includes('Email is already in use')) {
        setErrorMsg('Email này đã được sử dụng bởi tài khoản khác!');
      } else {
        setErrorMsg(serverMessage || 'Đăng ký thất bại. Vui lòng thử lại!');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-100 py-12 px-4 sm:px-6 lg:px-8 font-sans">
      <div className="max-w-md w-full space-y-8 bg-white p-8 border border-slate-200 rounded-2xl shadow-sm">
        <div>
          <div className="text-center text-4xl mb-4">🐾</div>
          <h2 className="text-center text-3xl font-extrabold text-slate-900">Tạo tài khoản PetCare</h2>
          <p className="mt-2 text-center text-sm text-slate-500">
            Đăng ký để quản lý hồ sơ thú cưng và đặt lịch khám dễ dàng
          </p>
        </div>

        <form className="mt-8 space-y-5" onSubmit={handleSubmit(onSubmit)}>
          {errorMsg && (
            <div className="bg-red-50 text-red-600 p-3 rounded-lg text-sm border border-red-200 font-medium">
              ⚠️ {errorMsg}
            </div>
          )}

          <div className="space-y-4">
            {/* Họ và tên */}
            <div>
              <label className="block text-sm font-semibold text-slate-700 mb-1">Họ và tên</label>
              <input
                {...register('fullName', { required: 'Vui lòng nhập họ và tên' })}
                type="text"
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none transition"
                placeholder="Nhập đầy đủ họ và tên"
              />
              {errors.fullName && <span className="text-xs text-red-500 mt-1 block">{errors.fullName.message as string}</span>}
            </div>

            {/* Email */}
            <div>
              <label className="block text-sm font-semibold text-slate-700 mb-1">Địa chỉ Email</label>
              <input
                {...register('email', { 
                  required: 'Vui lòng nhập email',
                  pattern: {
                    value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                    message: 'Địa chỉ email không hợp lệ'
                  }
                })}
                type="email"
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none transition"
                placeholder="email@example.com"
              />
              {errors.email && <span className="text-xs text-red-500 mt-1 block">{errors.email.message as string}</span>}
            </div>

            {/* Tên đăng nhập */}
            <div>
              <label className="block text-sm font-semibold text-slate-700 mb-1">Tên đăng nhập</label>
              <input
                {...register('username', { 
                  required: 'Vui lòng nhập tên đăng nhập',
                  minLength: { value: 3, message: 'Tên đăng nhập tối thiểu 3 ký tự' }
                })}
                type="text"
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none transition"
                placeholder="Tên viết liền không dấu"
              />
              {errors.username && <span className="text-xs text-red-500 mt-1 block">{errors.username.message as string}</span>}
            </div>

            {/* Mật khẩu */}
            <div>
              <label className="block text-sm font-semibold text-slate-700 mb-1">Mật khẩu</label>
              <input
                {...register('password', { 
                  required: 'Vui lòng nhập mật khẩu',
                  minLength: { value: 6, message: 'Mật khẩu phải từ 6 ký tự trở lên' }
                })}
                type="password"
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-550 outline-none transition"
                placeholder="Tối thiểu 6 ký tự"
              />
              {errors.password && <span className="text-xs text-red-500 mt-1 block">{errors.password.message as string}</span>}
            </div>

            {/* Xác nhận mật khẩu */}
            <div>
              <label className="block text-sm font-semibold text-slate-700 mb-1">Xác nhận mật khẩu</label>
              <input
                {...register('confirmPassword', { 
                  required: 'Vui lòng xác nhận mật khẩu',
                  validate: value => value === password || 'Mật khẩu xác nhận không khớp'
                })}
                type="password"
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-550 outline-none transition"
                placeholder="Nhập lại mật khẩu"
              />
              {errors.confirmPassword && <span className="text-xs text-red-500 mt-1 block">{errors.confirmPassword.message as string}</span>}
            </div>
          </div>

          <div className="pt-2">
            <button
              type="submit"
              disabled={loading}
              className="w-full flex justify-center py-2.5 px-4 border border-transparent rounded-lg shadow-sm text-sm font-semibold text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition disabled:opacity-50"
            >
              {loading ? 'Đang đăng ký...' : 'Đăng ký tài khoản'}
            </button>
          </div>
        </form>

        <div className="text-center pt-4 border-t border-slate-100 text-sm">
          <span className="text-slate-500">Đã có tài khoản? </span>
          <Link to="/login" className="font-bold text-indigo-600 hover:text-indigo-700 transition">
            Đăng nhập ngay
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Register;
