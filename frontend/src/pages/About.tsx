import React from 'react';
import DashboardLayout from '../layouts/DashboardLayout';
import { Heart, Shield, Award } from 'lucide-react';

export const About: React.FC = () => {
  const team = [
    { name: 'Dr. John Doe', role: 'Bác sĩ trưởng khoa & Sáng lập', initial: 'JD' },
    { name: 'Dr. Sarah Conner', role: 'Chuyên gia Phẫu thuật ngoại khoa', initial: 'SC' },
    { name: 'Dr. Helen Carter', role: 'Chuyên khoa Nội & Da liễu thú y', initial: 'HC' },
    { name: 'Dr. Adam Smith', role: 'Trưởng phòng Xét nghiệm y khoa', initial: 'AS' },
  ];

  return (
    <DashboardLayout>
      <div className="space-y-20 pb-20 animate-fade-in font-sans">
        
        {/* Header Banner with Blurred Overlay */}
        <section className="relative overflow-hidden bg-gradient-to-r from-teal-800 to-teal-600 rounded-3xl mx-4 sm:mx-6 lg:mx-8 mt-6 shadow-xl py-20 text-center text-white">
          <div className="absolute inset-0 bg-slate-900/10 backdrop-blur-sm z-10" />
          <div className="relative z-20 max-w-4xl mx-auto px-6 space-y-4">
            <h1 className="text-3xl sm:text-5xl font-extrabold tracking-tight text-white mb-2">
              Về PetCare - Sứ mệnh của chúng tôi
            </h1>
            <p className="text-slate-100 text-base sm:text-lg max-w-2xl mx-auto">
              Nơi đặt trọn niềm tin và sự chuyên nghiệp trong từng ca khám bệnh, chẩn đoán sức khỏe cho thú cưng.
            </p>
          </div>
        </section>

        {/* Story & Mission: Zig-zag layout */}
        <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-16">
          
          {/* Row 1 */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
            <div className="space-y-6">
              <div className="h-1 w-12 bg-teal-500 rounded-full" />
              <h2 className="text-3xl font-extrabold text-slate-900 leading-tight">Câu chuyện thành lập</h2>
              <p className="text-slate-500 leading-relaxed">
                Được thành lập từ năm 2020 bởi các bác sĩ thú y có lòng nhiệt huyết và tình yêu động vật sâu sắc, PetCare ra đời với mong muốn mang đến một giải pháp công nghệ hiện đại giúp tối ưu quy trình đặt lịch khám bệnh lâm sàng và tự động hóa hồ sơ sức khỏe trực tuyến cho vật nuôi tại Việt Nam.
              </p>
              <p className="text-slate-500 leading-relaxed">
                Chúng tôi tập trung 100% tài nguyên vào các dịch vụ khám chữa bệnh, xét nghiệm y khoa chuyên sâu và quản lý hồ sơ theo dõi sức khỏe trọn đời. Với PetCare, bé yêu của bạn luôn được chăm sóc bởi những trang thiết bị đạt chuẩn phòng khám tốt nhất.
              </p>
            </div>
            <div className="bg-slate-100 rounded-3xl overflow-hidden aspect-video shadow-md">
              <img 
                src="/vet_hero_banner.jpg" 
                alt="Clinic Lobby" 
                className="w-full h-full object-cover transform hover:scale-105 transition duration-500"
              />
            </div>
          </div>

          {/* Row 2: Alternated */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center lg:flex-row-reverse">
            <div className="lg:order-2 space-y-6">
              <div className="h-1 w-12 bg-orange-400 rounded-full" />
              <h2 className="text-3xl font-extrabold text-slate-900 leading-tight">Sứ mệnh y tế & Tầm nhìn</h2>
              <p className="text-slate-500 leading-relaxed">
                Sứ mệnh của chúng tôi là nâng cao sức khỏe vật nuôi và tạo sự thuận lợi tối đa cho chủ nuôi thông qua hệ thống sổ tiêm vaccine điện tử, bệnh án điện tử, giúp giảm thiểu thời gian chờ đợi tại phòng khám và số hóa việc kê đơn.
              </p>
              <p className="text-slate-500 leading-relaxed">
                Tầm nhìn trở thành hệ thống phòng khám lâm sàng hàng đầu Việt Nam đi đầu trong công nghệ y tế thú y thông minh và chăm sóc chuyên sâu.
              </p>
            </div>
            <div className="lg:order-1 bg-gradient-to-tr from-teal-50 to-teal-100/50 p-1.5 rounded-3xl shadow-md">
              <div className="bg-white p-8 rounded-[22px] space-y-6">
                <h3 className="text-lg font-bold text-slate-800">Giá trị cốt lõi</h3>
                <div className="space-y-4">
                  <div className="flex items-start space-x-3">
                    <Heart className="h-5 w-5 text-teal-600 flex-shrink-0 mt-0.5" />
                    <span className="text-slate-600 text-sm font-medium">Y đức và tình thương đặt lên hàng đầu.</span>
                  </div>
                  <div className="flex items-start space-x-3">
                    <Shield className="h-5 w-5 text-teal-600 flex-shrink-0 mt-0.5" />
                    <span className="text-slate-600 text-sm font-medium">Thông tin bệnh trạng và giá cả công khai minh bạch.</span>
                  </div>
                  <div className="flex items-start space-x-3">
                    <Award className="h-5 w-5 text-teal-600 flex-shrink-0 mt-0.5" />
                    <span className="text-slate-600 text-sm font-medium">Quy chuẩn y tế chuyên môn hóa cao.</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

        </section>

        {/* Team Section */}
        <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 bg-slate-50/50">
          <div className="text-center max-w-3xl mx-auto space-y-4">
            <h2 className="text-3xl font-extrabold text-slate-900 tracking-tight">Đội ngũ bác sĩ của chúng tôi</h2>
            <p className="text-slate-500">Các chuyên gia trực tiếp khám và chịu trách nhiệm y khoa tại PetCare.</p>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8 mt-12">
            {team.map((member, index) => (
              <div 
                key={index} 
                className="bg-white p-6 rounded-2xl border border-slate-100 hover:border-teal-100 shadow-md hover:shadow-lg transition duration-300 text-center flex flex-col items-center space-y-4"
              >
                <div className="h-20 w-20 rounded-full bg-gradient-to-tr from-teal-500 to-teal-400 text-white flex items-center justify-center text-xl font-bold shadow-inner">
                  {member.initial}
                </div>
                <div className="space-y-1">
                  <h4 className="font-extrabold text-slate-800 text-lg">{member.name}</h4>
                  <p className="text-teal-600 text-xs font-semibold">{member.role}</p>
                </div>
                <p className="text-xs text-slate-400 leading-relaxed">
                  Tận tâm và chu đáo, luôn hết lòng chăm sóc sức khỏe và cứu chữa thú cưng trong mọi tình huống.
                </p>
              </div>
            ))}
          </div>
        </section>

      </div>
    </DashboardLayout>
  );
};
