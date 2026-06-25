import React from 'react';
import { useNavigate } from 'react-router-dom';
import DashboardLayout from '../layouts/DashboardLayout';
import { Stethoscope, Syringe, Heart, Activity, Calendar, ShieldCheck } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

interface ServiceItem {
  icon: React.ReactNode;
  name: string;
  price: string;
  description: string;
}

export const Services: React.FC = () => {
  const navigate = useNavigate();
  const { user } = useAuth();

  const services: ServiceItem[] = [
    {
      icon: <Stethoscope className="h-8 w-8 text-teal-600" />,
      name: 'Khám bệnh tổng quát',
      price: '$15.00',
      description: 'Đánh giá lâm sàng tình trạng tim mạch, hệ hô hấp, tai mắt miệng và kiểm tra phản xạ vận động để phát hiện các triệu chứng bất thường.'
    },
    {
      icon: <Syringe className="h-8 w-8 text-teal-600" />,
      name: 'Tiêm phòng vaccine',
      price: '$22.00',
      description: 'Tiêm vaccine ngừa các bệnh truyền nhiễm nguy hiểm ở chó và mèo (dại, parvo, giảm bạch cầu...) kèm lập sổ theo dõi tiêm phòng định kỳ.'
    },
    {
      icon: <Activity className="h-8 w-8 text-orange-500" />,
      name: 'Xét nghiệm & Siêu âm',
      price: '$35.00',
      description: 'Phân tích tế bào máu, xét nghiệm sinh hóa gan thận và tiến hành siêu âm ổ bụng chuyên khoa giúp chẩn đoán bệnh chính xác nhất.'
    },
    {
      icon: <Heart className="h-8 w-8 text-teal-600" />,
      name: 'Phẫu thuật ngoại khoa',
      price: '$150.00',
      description: 'Thực hiện các ca phẫu thuật triệt sản, mổ đẻ khẩn cấp, khâu vết thương sâu hoặc chấn thương xương khớp trong phòng mổ vô trùng đạt chuẩn.'
    },
    {
      icon: <ShieldCheck className="h-8 w-8 text-teal-600" />,
      name: 'Nha khoa thú y',
      price: '$50.00',
      description: 'Lấy cao răng siêu âm an toàn, điều trị viêm nướu, nhổ răng sâu đau buốt giúp hơi thở thơm mát và cải thiện khả năng ăn nhai.'
    },
    {
      icon: <Calendar className="h-8 w-8 text-teal-600" />,
      name: 'Điều trị nội trú theo dõi',
      price: '$40.00 / ngày',
      description: 'Chăm sóc đặc biệt 24/7 dành cho các trường hợp bệnh nặng, truyền dịch liên tục và theo dõi sát sao biểu hiện sinh tồn bởi bác sĩ trực.'
    }
  ];

  const handleBookingClick = () => {
    if (user) {
      if (user.role === 'OWNER') {
        navigate('/owner/book');
      } else {
        alert('Tài khoản của bạn không phải là Chủ nuôi để đặt lịch.');
      }
    } else {
      navigate('/login');
    }
  };

  return (
    <DashboardLayout>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 sm:py-12 animate-fade-in font-sans">
        
        {/* Header Title */}
        <div className="text-center space-y-3 mb-12">
          <span className="text-teal-600 font-bold text-xs uppercase tracking-widest bg-teal-50 py-1.5 px-3 rounded-full">Bảng giá dịch vụ</span>
          <h1 className="text-3xl sm:text-4xl font-extrabold text-slate-900 tracking-tight">Dịch Vụ Lâm Sàng Thú Y</h1>
          <p className="text-slate-500 text-sm sm:text-base max-w-lg mx-auto leading-relaxed">
            Chúng tôi niêm yết công khai bảng giá các dịch vụ y khoa chuyên môn để chủ nuôi luôn chủ động và an tâm điều trị.
          </p>
        </div>

        {/* Services Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {services.map((service, idx) => (
            <div 
              key={idx}
              className="bg-white border border-slate-200/80 rounded-3xl p-8 shadow-md hover:shadow-xl hover:border-teal-200 transition-all duration-300 flex flex-col justify-between items-start space-y-6 group"
            >
              <div className="space-y-4 w-full">
                <div className="flex justify-between items-start">
                  <div className="p-3.5 bg-teal-50/70 rounded-2xl group-hover:bg-teal-50 transition duration-300">
                    {service.icon}
                  </div>
                  <span className="text-xl font-black text-orange-600 bg-orange-50 py-1.5 px-3.5 rounded-2xl shadow-sm">
                    {service.price}
                  </span>
                </div>
                
                <h3 className="text-xl font-bold text-slate-800 group-hover:text-teal-600 transition">{service.name}</h3>
                <p className="text-sm text-slate-500 leading-relaxed text-justify h-20 overflow-y-auto pr-1">
                  {service.description}
                </p>
              </div>

              <button
                onClick={handleBookingClick}
                className="w-full py-3.5 bg-teal-600 hover:bg-teal-700 text-white font-bold rounded-xl text-sm shadow-md hover:shadow-teal-500/10 transition duration-200"
              >
                Đặt lịch khám ngay
              </button>
            </div>
          ))}
        </div>

      </div>
    </DashboardLayout>
  );
};
export default Services;
