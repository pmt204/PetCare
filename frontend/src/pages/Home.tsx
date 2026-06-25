import React from 'react';
import { useNavigate } from 'react-router-dom';
import DashboardLayout from '../layouts/DashboardLayout';
import { Stethoscope, Activity, Syringe, Check } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export const Home: React.FC = () => {
  const navigate = useNavigate();
  const { user } = useAuth();

  const handleCtaClick = () => {
    if (user) {
      if (user.role === 'OWNER') {
        navigate('/owner/book');
      } else if (user.role === 'VET') {
        navigate('/vet/schedule');
      } else {
        navigate('/admin/vets');
      }
    } else {
      navigate('/login');
    }
  };

  return (
    <DashboardLayout>
      <div className="space-y-20 pb-20 animate-fade-in">
        
        {/* Hero Section */}
        <section className="relative overflow-hidden bg-slate-900 text-white rounded-3xl mx-4 sm:mx-6 lg:mx-8 mt-6 shadow-2xl">
          <div className="absolute inset-0 bg-gradient-to-r from-teal-900/90 to-slate-900/60 z-10" />
          <img 
            src="/vet_hero_banner.jpg" 
            alt="Veterinary Doctors" 
            className="absolute inset-0 w-full h-full object-cover transform scale-105 hover:scale-100 transition duration-1000"
          />
          
          <div className="relative max-w-5xl mx-auto px-6 py-24 sm:py-32 lg:px-8 lg:py-40 z-20 flex flex-col items-start space-y-6">
            <span className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-semibold bg-teal-500/20 text-teal-300 border border-teal-500/30 backdrop-blur-sm">
              🏥 Đạt chuẩn y tế lâm sàng quốc tế
            </span>
            <h1 className="text-4xl sm:text-6xl font-extrabold tracking-tight text-white max-w-2xl leading-tight">
              Chăm sóc tận tâm, <span className="bg-gradient-to-r from-orange-400 to-amber-300 bg-clip-text text-transparent">thú cưng khỏe mạnh</span>
            </h1>
            <p className="text-lg sm:text-xl text-slate-300 max-w-xl leading-relaxed">
              PetCare tự hào là điểm tựa sức khỏe vững chắc cho người bạn bốn chân của bạn. Đặt lịch khám trực tiếp với bác sĩ chuyên môn và quản lý hồ sơ bệnh án trực tuyến.
            </p>
            <div className="pt-4">
              <button 
                onClick={handleCtaClick}
                className="bg-gradient-to-r from-orange-500 to-amber-500 hover:from-orange-600 hover:to-amber-600 text-white text-base font-bold py-4 px-8 rounded-full shadow-lg hover:shadow-orange-500/20 transform hover:-translate-y-1 transition duration-300 flex items-center space-x-2"
              >
                <span>Đặt lịch khám ngay</span>
                <span>➜</span>
              </button>
            </div>
          </div>
        </section>

        {/* Services Section */}
        <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-3xl mx-auto space-y-4">
            <h2 className="text-3xl sm:text-4xl font-extrabold text-slate-900 tracking-tight">
              Dịch vụ khám và điều trị chuyên sâu
            </h2>
            <p className="text-slate-500 text-base sm:text-lg">
              Chúng tôi chỉ tập trung cung cấp các giải pháp y tế thú y chất lượng cao nhằm bảo vệ sức khỏe bé yêu một cách tốt nhất.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mt-12">
            
            {/* Card 1 */}
            <div className="bg-white p-8 rounded-2xl border border-slate-100 hover:border-teal-100 shadow-md hover:shadow-xl hover:-translate-y-2 transition duration-300 flex flex-col items-start space-y-4">
              <div className="p-4 rounded-xl bg-teal-50 text-teal-600">
                <Stethoscope className="h-8 w-8" />
              </div>
              <h3 className="text-xl font-bold text-slate-800">Khám bệnh tổng quát</h3>
              <p className="text-slate-500 text-sm leading-relaxed">
                Đánh giá toàn diện các dấu hiệu lâm sàng, khám tai mũi họng, tim mạch nhằm phát hiện sớm mọi dấu hiệu bất thường và chẩn đoán kịp thời.
              </p>
            </div>

            {/* Card 2 */}
            <div className="bg-white p-8 rounded-2xl border border-slate-100 hover:border-teal-100 shadow-md hover:shadow-xl hover:-translate-y-2 transition duration-300 flex flex-col items-start space-y-4">
              <div className="p-4 rounded-xl bg-orange-50 text-orange-500">
                <Activity className="h-8 w-8" />
              </div>
              <h3 className="text-xl font-bold text-slate-800">Xét nghiệm & Siêu âm</h3>
              <p className="text-slate-500 text-sm leading-relaxed">
                Thực hiện xét nghiệm máu tổng phân tích tế bào, siêu âm ổ bụng định kỳ và chụp X-quang kỹ thuật số giúp chẩn đoán bệnh chính xác nhất.
              </p>
            </div>

            {/* Card 3 */}
            <div className="bg-white p-8 rounded-2xl border border-slate-100 hover:border-teal-100 shadow-md hover:shadow-xl hover:-translate-y-2 transition duration-300 flex flex-col items-start space-y-4">
              <div className="p-4 rounded-xl bg-teal-50 text-teal-600">
                <Syringe className="h-8 w-8" />
              </div>
              <h3 className="text-xl font-bold text-slate-800">Tiêm phòng vaccine</h3>
              <p className="text-slate-500 text-sm leading-relaxed">
                Cung cấp đầy đủ vaccine phòng ngừa các bệnh nguy hiểm (dại, parvo, care...) cùng quy trình khám sàng lọc y tế trước tiêm chuyên nghiệp.
              </p>
            </div>

          </div>
        </section>

        {/* Why Choose Us Section */}
        <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 bg-teal-900/5 rounded-3xl py-16">
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
            
            {/* Column 1: Image Mockup */}
            <div className="relative">
              <div className="absolute inset-0 bg-gradient-to-tr from-teal-600 to-orange-400 rounded-2xl filter blur-2xl opacity-10 transform scale-95" />
              <div className="relative bg-white p-4 rounded-2xl shadow-lg border border-slate-100">
                <img 
                  src="/vet_hero_banner.jpg" 
                  alt="Why Choose Us illustration" 
                  className="rounded-xl w-full h-80 object-cover"
                />
                <div className="absolute -bottom-6 -right-6 bg-orange-500 text-white p-4 rounded-2xl shadow-lg hidden sm:block animate-bounce-slow">
                  <span className="text-3xl font-extrabold block">10+</span>
                  <span className="text-xs uppercase tracking-wider text-orange-100">Năm kinh nghiệm</span>
                </div>
              </div>
            </div>

            {/* Column 2: Bullet Points */}
            <div className="space-y-6">
              <span className="text-xs font-bold text-teal-600 uppercase tracking-widest block">Lý do chọn PetCare</span>
              <h2 className="text-3xl font-extrabold text-slate-900 leading-tight">
                Hệ thống đặt lịch y tế & hồ sơ thông minh
              </h2>
              <p className="text-slate-500">
                Chúng tôi mang lại sự an tâm tuyệt đối cho chủ nuôi bằng cách kết hợp khoa học kỹ thuật thú y hiện đại và hồ sơ bệnh án minh bạch, được số hóa hoàn toàn.
              </p>
              
              <ul className="space-y-4">
                
                <li className="flex items-start space-x-3">
                  <div className="mt-1 flex-shrink-0 p-1 bg-teal-100 text-teal-600 rounded-full">
                    <Check className="h-4 w-4 stroke-[3px]" />
                  </div>
                  <div>
                    <h4 className="font-bold text-slate-800">Đội ngũ bác sĩ chuyên khoa</h4>
                    <p className="text-slate-500 text-sm">Bác sĩ giàu chuyên môn lâm sàng trực tiếp tiếp nhận lịch hẹn khám bệnh.</p>
                  </div>
                </li>

                <li className="flex items-start space-x-3">
                  <div className="mt-1 flex-shrink-0 p-1 bg-teal-100 text-teal-600 rounded-full">
                    <Check className="h-4 w-4 stroke-[3px]" />
                  </div>
                  <div>
                    <h4 className="font-bold text-slate-800">Trang thiết bị hiện đại</h4>
                    <p className="text-slate-500 text-sm">Hệ thống chẩn đoán hình ảnh cao cấp hỗ trợ chẩn đoán chính xác tức thời.</p>
                  </div>
                </li>

                <li className="flex items-start space-x-3">
                  <div className="mt-1 flex-shrink-0 p-1 bg-teal-100 text-teal-600 rounded-full">
                    <Check className="h-4 w-4 stroke-[3px]" />
                  </div>
                  <div>
                    <h4 className="font-bold text-slate-800">Hồ sơ bệnh án & sổ tiêm điện tử</h4>
                    <p className="text-slate-500 text-sm">Dễ dàng theo dõi lịch sử điều trị, các mũi tiêm đã thực hiện trực tuyến mọi lúc.</p>
                  </div>
                </li>

              </ul>
            </div>

          </div>
        </section>

      </div>
    </DashboardLayout>
  );
};
