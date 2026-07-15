import React from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import DashboardLayout from '../layouts/DashboardLayout';
import { ArrowLeft, Star, Calendar, ShieldCheck, Heart, User } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';

interface Vet {
  id: number;
  name: string;
  specialty: string;
  experienceYears: string;
  rating: number;
  initial: string;
  description: string;
  biography: string;
  schedule: string;
  services: string[];
  image?: string;
}

export const DoctorDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const location = useLocation();
  const { user } = useAuth();

  const [vet, setVet] = React.useState<Vet | null>(null);
  const [loading, setLoading] = React.useState(true);

  React.useEffect(() => {
    const fetchDoctor = async () => {
      try {
        const res = await api.get(`/doctors/${id}`);
        const doc = res.data;
        setVet({
          id: doc.id,
          name: doc.name,
          specialty: doc.specialty,
          experienceYears: doc.experienceYears,
          rating: doc.rating || 5.0,
          description: doc.description,
          biography: doc.fullDescription || doc.description,
          schedule: doc.name.includes('Sarah') ? 'Thứ 3 - Thứ 7 (09:00 - 18:00)' : (doc.name.includes('Helen') ? 'Thứ 2 - Thứ 6 (08:00 - 12:00)' : 'Thứ 2 - Thứ 6 (08:00 - 17:00)'),
          services: doc.services && doc.services.length > 0 ? doc.services : ['Khám lâm sàng tổng quát', 'Khám sàng lọc sức khỏe trước tiêm'],
          image: doc.image,
          initial: doc.name.split(' ').map((n: string) => n[0]).join('').substring(0, 2).toUpperCase()
        });
      } catch (err) {
        console.warn('Error fetching doctor details, using fallback mock:', err);
        const fallbackVets = [
          { 
            id: 1, 
            name: 'Dr. John Doe', 
            specialty: 'Khám bệnh đa khoa & Nội khoa', 
            experienceYears: '5 năm', 
            rating: 4.8, 
            initial: 'JD', 
            description: 'Chuyên gia chẩn đoán lâm sàng nội khoa, khám tổng quát và điều trị các bệnh truyền nhiễm thường gặp.',
            biography: 'Dr. John Doe tốt nghiệp chuyên ngành Y học Thú y từ Đại học Nông nghiệp danh tiếng. Với hơn 5 năm kinh nghiệm lâm sàng chuyên sâu, ông đặc biệt nhạy bén trong việc chẩn đoán các bệnh lý nội khoa phức tạp.',
            schedule: 'Thứ 2 - Thứ 6 (08:00 - 17:00)',
            services: ['Khám bệnh lâm sàng tổng quát', 'Khám sàng lọc sức khỏe trước tiêm', 'Tư vấn phác đồ điều trị bệnh truyền nhiễm'],
            image: '/images/doctor_john.jpg'
          },
          { 
            id: 2, 
            name: 'Dr. Sarah Conner', 
            specialty: 'Phẫu thuật ngoại khoa & Chấn thương', 
            experienceYears: '8 năm', 
            rating: 4.9, 
            initial: 'SC', 
            description: 'Hơn 8 năm kinh nghiệm thực hiện các ca phẫu thuật phức tạp, chấn thương chỉnh hình và cấp cứu thú cưng.',
            biography: 'Dr. Sarah Conner là một trong những bác sĩ ngoại khoa hàng đầu tại hệ thống PetCare. Bà đã hoàn thành khóa đào tạo chuyên sâu về chấn thương chỉnh hình thú cưng tại nước ngoài.',
            schedule: 'Thứ 3 - Thứ 7 (09:00 - 18:00)',
            services: ['Phẫu thuật triệt sản chó & mèo', 'Phẫu thuật kết hợp xương chấn thương', 'Cấp cứu ngoại khoa khẩn cấp'],
            image: '/images/doctor_sarah.jpg'
          },
          { 
            id: 3, 
            name: 'Dr. Helen Carter', 
            specialty: 'Da liễu & Điều trị nội trú', 
            experienceYears: '6 năm', 
            rating: 4.7, 
            initial: 'HC', 
            description: 'Chuyên điều trị các bệnh về da liễu, nấm, dị ứng và theo dõi sức khỏe vật nuôi nằm viện dài ngày.',
            biography: 'Dr. Helen Carter tốt nghiệp Thạc sĩ Y khoa thú y và cống hiến 6 năm qua trong lĩnh vực da liễu động vật.',
            schedule: 'Thứ 2 - Thứ 6 (08:00 - 12:00)',
            services: ['Khám soi da liễu tìm ký sinh trùng', 'Điều trị viêm tai & nấm da mãn tính', 'Theo dõi chăm sóc nội trú đặc biệt'],
            image: '/images/doctor_helen.jpg'
          }
        ];
        const found = fallbackVets.find(v => v.id === Number(id));
        if (found) setVet(found);
      } finally {
        setLoading(false);
      }
    };
    fetchDoctor();
  }, [id]);

  if (loading) {
    return (
      <DashboardLayout>
        <div className="text-center py-20 text-teal-600 font-bold animate-pulse font-sans">
          Đang tải chi tiết bác sĩ...
        </div>
      </DashboardLayout>
    );
  }

  if (!vet) {
    return (
      <DashboardLayout>
        <div className="text-center py-12 font-sans">
          <p className="text-slate-500 font-bold">Không tìm thấy thông tin bác sĩ.</p>
          <button onClick={() => navigate('/vets')} className="mt-4 text-teal-600 font-bold hover:underline">Quay lại danh sách</button>
        </div>
      </DashboardLayout>
    );
  }

  const handleBookingClick = () => {
    if (user) {
      if (user.role === 'OWNER') {
        navigate(`/owner/book?vetId=${vet.id}`, { state: { from: location.pathname } });
      } else {
        alert('Tài khoản của bạn không phải là Chủ nuôi để đặt lịch.');
      }
    } else {
      navigate('/login');
    }
  };

  return (
    <DashboardLayout>
      <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-8 sm:py-12 animate-fade-in font-sans">
        
        {/* Header navigation bar */}
        <div className="flex items-center space-x-4 mb-8">
          <button 
            onClick={() => navigate('/vets')} 
            className="p-2 border border-slate-200 rounded-xl bg-white hover:bg-slate-50 text-slate-600 transition"
            title="Quay lại"
          >
            <ArrowLeft className="h-5 w-5" />
          </button>
          <div>
            <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Chi tiết Bác sĩ</h1>
            <p className="text-slate-500 text-sm mt-1">Đội ngũ y tế chất lượng cao PetCare</p>
          </div>
        </div>

        {/* Layout Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 items-start">
          
          {/* Left Area (Main Profile Details) - 2 Cols */}
          <div className="lg:col-span-2 space-y-6">
            
            {/* Main Info Card */}
            <div className="bg-white border border-slate-200 rounded-3xl p-6 sm:p-8 shadow-sm space-y-6">
              <div className="flex flex-col sm:flex-row items-center sm:items-start gap-6">
                {/* Avatar Image or Initials */}
                {vet.image ? (
                  <img 
                    src={vet.image} 
                    alt={vet.name} 
                    className="h-24 w-24 rounded-full object-cover border border-slate-100 shadow-inner flex-shrink-0"
                  />
                ) : (
                  <div className="h-24 w-24 rounded-full bg-gradient-to-tr from-teal-500 to-teal-400 text-white flex items-center justify-center text-2xl font-black shadow-inner flex-shrink-0">
                    {vet.initial}
                  </div>
                )}
                
                {/* Name & Specialty */}
                <div className="text-center sm:text-left space-y-2.5">
                  <div className="flex flex-col sm:flex-row sm:items-center gap-2">
                    <h2 className="text-2xl font-extrabold text-slate-900">{vet.name}</h2>
                    <span className="self-center flex items-center gap-1 text-xs font-bold text-teal-700 bg-teal-50 px-2.5 py-1 rounded-full border border-teal-100">
                      <Star className="h-3 w-3 fill-teal-650 text-teal-600" /> {vet.rating} / 5
                    </span>
                  </div>
                  <p className="text-sm font-bold text-teal-700 bg-teal-50 py-1 px-3.5 rounded-xl border border-teal-100 inline-block">
                    {vet.specialty}
                  </p>
                  <p className="text-slate-500 text-sm">{vet.description}</p>
                </div>
              </div>

              {/* Biography Section */}
              <div className="border-t border-slate-100 pt-6 space-y-3">
                <h3 className="font-extrabold text-slate-800 text-base flex items-center gap-2">
                  <User className="h-4 w-4 text-teal-600" /> Tiểu sử & Chuyên môn
                </h3>
                <p className="text-sm text-slate-600 leading-relaxed text-justify">
                  {vet.biography}
                </p>
              </div>
            </div>

            {/* Services Performed */}
            <div className="bg-white border border-slate-200 rounded-3xl p-6 sm:p-8 shadow-sm space-y-4">
              <h3 className="font-extrabold text-slate-800 text-base flex items-center gap-2">
                <Heart className="h-4 w-4 text-teal-600" /> Các dịch vụ y khoa phụ trách
              </h3>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 text-sm text-slate-600">
                {vet.services.map((service, idx) => (
                  <div key={idx} className="flex items-center space-x-2 bg-slate-50 p-3 rounded-xl border border-slate-150">
                    <ShieldCheck className="h-4 w-4 text-teal-500 flex-shrink-0" />
                    <span className="font-semibold text-slate-700">{service}</span>
                  </div>
                ))}
              </div>
            </div>

          </div>

          {/* Right Area (Schedule & Booking CTA Card) - 1 Col */}
          <div className="bg-white border border-slate-200 rounded-3xl p-6 shadow-sm space-y-6 sticky top-24">
            <h3 className="font-extrabold text-slate-800 text-sm border-b border-slate-100 pb-2.5 uppercase tracking-wider text-teal-700">Lịch làm việc của Bác sĩ</h3>
            
            <div className="space-y-4">
              <div className="flex items-start space-x-3 text-sm">
                <Calendar className="h-5 w-5 text-slate-400 mt-0.5" />
                <div>
                  <span className="text-slate-400 block text-xs font-semibold uppercase">Giờ túc trực lâm sàng</span>
                  <span className="font-bold text-slate-700 block mt-1">{vet.schedule}</span>
                </div>
              </div>

              <div className="border-t border-slate-100 my-2"></div>
              
              <div className="text-xs text-slate-400 leading-relaxed">
                * Quý khách vui lòng đăng ký đặt lịch khám trước để hạn chế thời gian chờ đợi tại phòng khám PetCare.
              </div>
            </div>

            <button 
              onClick={handleBookingClick}
              className="w-full bg-gradient-to-r from-teal-600 to-teal-500 hover:from-teal-700 hover:to-teal-600 text-white font-bold py-3.5 px-6 rounded-2xl shadow-md hover:shadow-teal-500/10 transition duration-200"
            >
              Đặt lịch khám ngay
            </button>
          </div>

        </div>

      </div>
    </DashboardLayout>
  );
};

export default DoctorDetails;
