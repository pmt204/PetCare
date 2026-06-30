import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import DashboardLayout from '../layouts/DashboardLayout';
import { Stethoscope, Syringe, Heart, Activity, Calendar, ShieldCheck, Check } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';

interface ServiceItem {
  icon: React.ReactNode;
  name: string;
  price: string;
  description: string;
}

export const Services: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user } = useAuth();

  const [services, setServices] = React.useState<ServiceItem[]>([]);
  const [loading, setLoading] = React.useState(true);

  React.useEffect(() => {
    const fetchServices = async () => {
      try {
        const res = await api.get('/services');
        const iconMap = (name: string) => {
          if (name.includes('Khám')) return <Stethoscope className="h-8 w-8 text-teal-600" />;
          if (name.includes('Tiêm')) return <Syringe className="h-8 w-8 text-teal-600" />;
          if (name.includes('Xét')) return <Activity className="h-8 w-8 text-orange-500" />;
          if (name.includes('Phẫu')) return <Heart className="h-8 w-8 text-teal-600" />;
          if (name.includes('Nha')) return <ShieldCheck className="h-8 w-8 text-teal-600" />;
          return <Calendar className="h-8 w-8 text-teal-600" />;
        };
        const mapped = res.data.map((s: any) => ({
          icon: iconMap(s.name),
          name: s.name,
          price: `${s.price.toLocaleString('vi-VN')} ₫` + (s.name.includes('nội trú') ? ' / ngày' : ''),
          description: s.description
        }));
        setServices(mapped);
      } catch (err) {
        console.warn('Error loading services, using fallback:', err);
        setServices([
          {
            icon: <Stethoscope className="h-8 w-8 text-teal-600" />,
            name: 'Khám bệnh tổng quát',
            price: '150.000 ₫',
            description: 'Đánh giá lâm sàng tình trạng tim mạch, hệ hô hấp, tai mắt miệng và kiểm tra phản xạ vận động để phát hiện các triệu chứng bất thường.'
          },
          {
            icon: <Syringe className="h-8 w-8 text-teal-600" />,
            name: 'Tiêm phòng vaccine',
            price: '250.000 ₫',
            description: 'Tiêm vaccine ngừa các bệnh truyền nhiễm nguy hiểm ở chó và mèo (dại, parvo, giảm bạch cầu...) kèm lập sổ theo dõi tiêm phòng định kỳ.'
          },
          {
            icon: <Activity className="h-8 w-8 text-orange-500" />,
            name: 'Xét nghiệm & Siêu âm',
            price: '350.000 ₫',
            description: 'Phân tích tế bào máu, xét nghiệm sinh hóa gan thận và tiến hành siêu âm ổ bụng chuyên khoa giúp chẩn đoán bệnh chính xác nhất.'
          },
          {
            icon: <Heart className="h-8 w-8 text-teal-600" />,
            name: 'Phẫu thuật ngoại khoa',
            price: '1.500.000 ₫',
            description: 'Thực hiện các ca phẫu thuật triệt sản, mổ đẻ khẩn cấp, khâu vết thương sâu hoặc chấn thương xương khớp trong phòng mổ vô trùng đạt chuẩn.'
          },
          {
            icon: <ShieldCheck className="h-8 w-8 text-teal-600" />,
            name: 'Nha khoa thú y',
            price: '500.000 ₫',
            description: 'Lấy cao răng siêu âm an toàn, điều trị viêm nướu, nhổ răng sâu đau buốt giúp hơi thở thơm mát và cải thiện khả năng ăn nhai.'
          },
          {
            icon: <Calendar className="h-8 w-8 text-teal-600" />,
            name: 'Điều trị nội trú theo dõi',
            price: '400.000 ₫ / ngày',
            description: 'Chăm sóc đặc biệt 24/7 dành cho các trường hợp bệnh nặng, truyền dịch liên tục và theo dõi sát sao biểu hiện sinh tồn bởi bác sĩ trực.'
          }
        ]);
      } finally {
        setLoading(false);
      }
    };
    fetchServices();
  }, []);

  const handleBookingClick = () => {
    if (user) {
      if (user.role === 'OWNER') {
        navigate('/owner/book', { state: { from: location.pathname } });
      } else {
        alert('Tài khoản của bạn không phải là Chủ nuôi để đặt lịch.');
      }
    } else {
      navigate('/login');
    }
  };

  return (
    <DashboardLayout>
      <div className="space-y-12 pb-20 animate-fade-in font-sans">
        
        {/* Modern Split Header Section */}
        <section className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 pt-8">
          <div className="grid grid-cols-1 md:grid-cols-5 gap-8 items-center bg-gradient-to-br from-teal-50/60 via-white to-slate-50 border border-slate-200/80 p-8 rounded-3xl shadow-sm">
            
            {/* Left Content (3 columns) */}
            <div className="md:col-span-3 space-y-6">
              <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold bg-teal-100 text-teal-800 border border-teal-200">
                ✨ Dịch vụ chuẩn y khoa
              </span>
              <h1 className="text-3xl sm:text-4xl font-extrabold text-slate-900 tracking-tight leading-tight">
                Bảng Giá & Danh Mục <br />
                <span className="bg-gradient-to-r from-teal-600 to-teal-500 bg-clip-text text-transparent">Dịch Vụ Y Tế Thú Y</span>
              </h1>
              <p className="text-sm sm:text-base text-slate-500 leading-relaxed">
                Chúng tôi cung cấp quy trình y tế khép kín từ khám sàng lọc, chẩn đoán hình ảnh đến phẫu thuật ngoại khoa và chăm sóc nội trú. Bảng giá được niêm yết rõ ràng, minh bạch giúp bạn chủ động bảo vệ sức khỏe cho bé cưng.
              </p>
              
              <div className="flex flex-col sm:flex-row sm:items-center gap-4 pt-2">
                <button 
                  onClick={handleBookingClick}
                  className="bg-gradient-to-r from-teal-600 to-teal-500 hover:from-teal-700 hover:to-teal-600 text-white text-sm sm:text-base font-bold py-3.5 px-8 rounded-2xl shadow-md hover:shadow-teal-655/15 transform hover:-translate-y-0.5 transition duration-200 flex items-center justify-center space-x-2"
                >
                  <span>Đặt lịch khám ngay</span>
                  <span>➜</span>
                </button>
                <div className="text-xs text-slate-400 font-semibold sm:max-w-[150px] leading-relaxed text-center sm:text-left">
                  Hỗ trợ đặt lịch nhanh trực tuyến chỉ trong 30 giây
                </div>
              </div>
            </div>

            {/* Right Information Card (2 columns) */}
            <div className="md:col-span-2 bg-white border border-slate-200 p-6 rounded-2xl shadow-inner space-y-4">
              <h3 className="font-extrabold text-slate-800 text-sm border-b border-slate-100 pb-2 uppercase tracking-wider text-teal-700">Quy trình khám chữa bệnh</h3>
              <ul className="space-y-3.5 text-xs text-slate-600">
                <li className="flex items-center space-x-3">
                  <span className="h-5 w-5 rounded-full bg-teal-500 text-white flex items-center justify-center font-bold text-[10px]">1</span>
                  <span className="font-semibold text-slate-700">Đăng ký lịch hẹn trực tuyến</span>
                </li>
                <li className="flex items-center space-x-3">
                  <span className="h-5 w-5 rounded-full bg-teal-500 text-white flex items-center justify-center font-bold text-[10px]">2</span>
                  <span className="font-semibold text-slate-700">Khám sàng lọc & Chẩn đoán hình ảnh</span>
                </li>
                <li className="flex items-center space-x-3">
                  <span className="h-5 w-5 rounded-full bg-teal-500 text-white flex items-center justify-center font-bold text-[10px]">3</span>
                  <span className="font-semibold text-slate-700">Nhận phác đồ điều trị & Đơn thuốc</span>
                </li>
              </ul>
              
              <div className="bg-slate-50 p-3 rounded-xl border border-slate-150 text-[11px] text-slate-400 mt-2">
                📞 Hotline hỗ trợ 24/7: <strong className="text-slate-700">1900 8888</strong>
              </div>
            </div>

          </div>
        </section>

        {/* Horizontal Services List */}
        <section className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 space-y-6">
          <div className="border-b border-slate-200 pb-4">
            <h2 className="text-xl font-bold text-slate-800">Chi tiết bảng giá dịch vụ</h2>
          </div>

          {loading ? (
            <div className="text-center py-20 text-teal-600 font-bold animate-pulse">
              Đang tải danh mục dịch vụ...
            </div>
          ) : (
            <div className="space-y-4">
              {services.map((service, idx) => (
                <div 
                  key={idx}
                  className="bg-white border border-slate-200/80 rounded-3xl p-6 shadow-sm hover:shadow-md hover:border-teal-200 transition-all duration-300 flex flex-col md:flex-row md:items-center justify-between gap-6 group"
                >
                  {/* Left side: Num + Icon + Text */}
                  <div className="flex items-start space-x-4 flex-1">
                    {/* Number */}
                    <span className="text-2xl font-black text-slate-200 group-hover:text-teal-250 transition select-none pt-1">
                      {String(idx + 1).padStart(2, '0')}
                    </span>
                    
                    {/* Icon */}
                    <div className="p-3 bg-teal-50 rounded-xl text-teal-600 group-hover:bg-teal-100/80 transition duration-300 flex-shrink-0">
                      {service.icon}
                    </div>
                    
                    {/* Title & Description */}
                    <div className="space-y-1">
                      <h3 className="text-lg font-bold text-slate-800 group-hover:text-teal-600 transition">{service.name}</h3>
                      <p className="text-sm text-slate-500 leading-relaxed text-justify max-w-xl pr-2">
                        {service.description}
                      </p>
                    </div>
                  </div>

                  {/* Right side: Price Badge & Details */}
                  <div className="flex flex-col items-start md:items-end justify-between self-stretch flex-shrink-0 border-t md:border-t-0 md:border-l border-slate-100 pt-4 md:pt-0 md:pl-6 min-w-[170px]">
                    <span className="text-xl font-black text-orange-655 bg-orange-50/80 py-1.5 px-4 rounded-xl shadow-inner block">
                      {service.price}
                    </span>
                    <div className="flex items-center space-x-1.5 text-xs text-slate-400 font-medium mt-3 md:mt-0">
                      <Check className="h-4.5 w-4.5 text-teal-500" />
                      <span>Trọn gói, không phụ phí</span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </section>

      </div>
    </DashboardLayout>
  );
};
export default Services;
