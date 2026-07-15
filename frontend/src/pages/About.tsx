import React from 'react';
import DashboardLayout from '../layouts/DashboardLayout';
import { Heart, Shield, Award } from 'lucide-react';
import api from '../services/api';

export const About: React.FC = () => {
  const [team, setTeam] = React.useState<any[]>([]);
  const [loading, setLoading] = React.useState(true);

  React.useEffect(() => {
    const fetchDoctors = async () => {
      try {
        const res = await api.get('/doctors');
        const mappedTeam = res.data.map((doc: any) => ({
          name: doc.name,
          role: doc.name.includes('John') ? 'Bác sĩ trưởng khoa & Sáng lập' : (doc.name.includes('Sarah') ? 'Chuyên gia Phẫu thuật ngoại khoa' : 'Chuyên khoa Nội & Da liễu thú y'),
          image: doc.image,
          initial: doc.name.split(' ').map((n: string) => n[0]).join('').substring(0, 2).toUpperCase()
        }));
        setTeam(mappedTeam);
      } catch (err) {
        console.warn('Error fetching doctors in About page, using fallback:', err);
        setTeam([
          { name: 'Dr. John Doe', role: 'Bác sĩ trưởng khoa & Sáng lập', initial: 'JD', image: '/images/doctor_john.jpg' },
          { name: 'Dr. Sarah Conner', role: 'Chuyên gia Phẫu thuật ngoại khoa', initial: 'SC', image: '/images/doctor_sarah.jpg' },
          { name: 'Dr. Helen Carter', role: 'Chuyên khoa Nội & Da liễu thú y', initial: 'HC', image: '/images/doctor_helen.jpg' },
        ]);
      } finally {
        setLoading(false);
      }
    };
    fetchDoctors();
  }, []);

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

        {/* Story Section */}
        <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
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
                src="/images/clinic_lobby.jpg" 
                alt="Clinic Lobby" 
                className="w-full h-full object-cover transform hover:scale-105 transition duration-500"
              />
            </div>
          </div>

          {/* Row 2: Alternated */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center lg:flex-row-reverse mt-16">
            <div className="bg-white border border-slate-200/60 rounded-3xl p-8 sm:p-10 shadow-sm space-y-6">
              <div className="space-y-4">
                <span className="text-[11px] font-bold text-teal-700 bg-teal-50 py-1 px-3 rounded-full uppercase tracking-wider">Mục tiêu phát triển</span>
                <h3 className="text-2xl font-extrabold text-slate-900 leading-snug">Giá trị cốt lõi & Cam kết y tế</h3>
              </div>
              
              <div className="space-y-4 text-sm text-slate-650">
                <div className="flex items-start space-x-3">
                  <Heart className="h-5 w-5 text-teal-650 mt-0.5 flex-shrink-0" />
                  <p><strong>Yêu thương vô điều kiện:</strong> Đối xử với thú cưng của khách hàng như chính thành viên trong gia đình mình.</p>
                </div>
                <div className="flex items-start space-x-3">
                  <Shield className="h-5 w-5 text-teal-650 mt-0.5 flex-shrink-0" />
                  <p><strong>An toàn & Chuẩn xác:</strong> Mọi quy trình chẩn đoán lâm sàng, cận lâm sàng đều được thực hiện theo tiêu chuẩn kiểm duyệt khắt khe.</p>
                </div>
                <div className="flex items-start space-x-3">
                  <Award className="h-5 w-5 text-teal-650 mt-0.5 flex-shrink-0" />
                  <p><strong>Chuyên môn liên tục cải tiến:</strong> Đội ngũ y tế không ngừng đào tạo và cập nhật các phác đồ y khoa hiện đại nhất thế giới.</p>
                </div>
              </div>
            </div>
            
            <div className="space-y-6">
              <h3 className="text-2xl font-extrabold text-slate-900 leading-snug">Hành trình 5 năm đồng hành</h3>
              <p className="text-slate-500 leading-relaxed">
                Trong suốt thời gian hoạt động, PetCare tự hào đã đồng hành và chăm sóc cho hàng ngàn bé thú cưng tại Việt Nam. Không chỉ cung cấp dịch vụ y tế chuẩn mực, chúng tôi còn xây dựng cộng đồng nâng cao kiến thức chăm sóc vật nuôi khoa học cho các chủ nuôi.
              </p>
            </div>
          </div>
        </section>

        {/* Doctors Section */}
        <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 bg-slate-50/50">
          <div className="text-center max-w-3xl mx-auto space-y-4">
            <h2 className="text-3xl font-extrabold text-slate-900 tracking-tight">Đội ngũ bác sĩ của chúng tôi</h2>
            <p className="text-slate-500">Các chuyên gia trực tiếp khám và chịu trách nhiệm y khoa tại PetCare.</p>
          </div>

          {loading ? (
            <div className="text-center py-20 text-teal-600 font-bold animate-pulse">
              Đang tải danh sách bác sĩ...
            </div>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8 mt-12">
              {team.map((member, index) => (
                <div 
                  key={index} 
                  className="bg-white p-6 rounded-2xl border border-slate-100 hover:border-teal-100 shadow-md hover:shadow-lg transition duration-300 text-center flex flex-col items-center space-y-4"
                >
                  {/* Avatar image or initials */}
                  {member.image ? (
                    <img 
                      src={member.image} 
                      alt={member.name} 
                      className="h-20 w-20 rounded-full object-cover border border-slate-100 shadow-inner"
                    />
                  ) : (
                    <div className="h-20 w-20 rounded-full bg-gradient-to-tr from-teal-500 to-teal-400 text-white flex items-center justify-center text-xl font-bold shadow-inner">
                      {member.initial}
                    </div>
                  )}
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
          )}
        </section>

      </div>
    </DashboardLayout>
  );
};
export default About;
