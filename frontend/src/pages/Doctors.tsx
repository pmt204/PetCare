import React from 'react';
import { useNavigate } from 'react-router-dom';
import DashboardLayout from '../layouts/DashboardLayout';
import { Calendar } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

interface Vet {
  id: number;
  name: string;
  specialty: string;
  experienceYears: string;
  rating: number;
  initial: string;
  description: string;
}

export const Doctors: React.FC = () => {
  const navigate = useNavigate();
  const { user } = useAuth();

  const vets: Vet[] = [
    { id: 1, name: 'Dr. John Doe', specialty: 'Khám bệnh đa khoa & Nội khoa', experienceYears: '5 năm', rating: 4.8, initial: 'JD', description: 'Chuyên gia chẩn đoán lâm sàng nội khoa, khám tổng quát và điều trị các bệnh truyền nhiễm thường gặp.' },
    { id: 2, name: 'Dr. Sarah Conner', specialty: 'Phẫu thuật ngoại khoa & Chấn thương', experienceYears: '8 năm', rating: 4.9, initial: 'SC', description: 'Hơn 8 năm kinh nghiệm thực hiện các ca phẫu thuật phức tạp, chấn thương chỉnh hình và cấp cứu thú cưng.' },
    { id: 3, name: 'Dr. Helen Carter', specialty: 'Da liễu & Điều trị nội trú', experienceYears: '6 năm', rating: 4.7, initial: 'HC', description: 'Chuyên điều trị các bệnh về da liễu, nấm, dị ứng và theo dõi sức khỏe vật nuôi nằm viện dài ngày.' },
    { id: 4, name: 'Dr. Adam Smith', specialty: 'Xét nghiệm lâm sàng & Chẩn đoán hình ảnh', experienceYears: '7 năm', rating: 4.8, initial: 'AS', description: 'Phụ trách siêu âm chẩn đoán hình ảnh, phân tích chỉ số máu phòng lab giúp đưa ra phác đồ điều trị chính xác.' },
  ];

  const handleBookingClick = (vetId: number) => {
    if (user) {
      if (user.role === 'OWNER') {
        navigate(`/owner/book?vetId=${vetId}`);
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
          <span className="text-teal-600 font-bold text-xs uppercase tracking-widest bg-teal-50 py-1.5 px-3 rounded-full">Đội ngũ y tế</span>
          <h1 className="text-3xl sm:text-4xl font-extrabold text-slate-900 tracking-tight">Danh sách Bác sĩ Thú y</h1>
          <p className="text-slate-500 text-sm sm:text-base max-w-lg mx-auto leading-relaxed">
            Gặp gỡ những bác sĩ chuyên môn cao tại PetCare, tận tâm chăm sóc và điều trị tốt nhất cho vật nuôi của bạn.
          </p>
        </div>

        {/* Vets Grid Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
          {vets.map((vet) => (
            <div 
              key={vet.id}
              className="bg-white border border-slate-200/80 rounded-3xl p-6 shadow-md hover:shadow-xl hover:border-teal-200 transition-all duration-300 flex flex-col items-center justify-between text-center space-y-5 group"
            >
              <div className="flex flex-col items-center space-y-4 w-full">
                {/* Rounded Avatar Initials */}
                <div className="h-20 w-20 rounded-full bg-gradient-to-tr from-teal-500 to-teal-400 text-white flex items-center justify-center text-xl font-bold shadow-inner group-hover:scale-105 transition duration-300">
                  {vet.initial}
                </div>
                
                <div className="space-y-1">
                  <h3 className="font-extrabold text-slate-800 text-lg group-hover:text-teal-600 transition">{vet.name}</h3>
                  <span className="inline-block text-[11px] font-bold text-teal-700 bg-teal-50 py-0.5 px-2.5 rounded-full">
                    {vet.specialty}
                  </span>
                </div>

                <p className="text-xs text-slate-400 leading-relaxed text-justify px-2 h-20 overflow-y-auto">
                  {vet.description}
                </p>
              </div>

              {/* Specs & CTA */}
              <div className="w-full space-y-4">
                <div className="grid grid-cols-2 gap-2 border-t border-slate-100 pt-4 text-xs font-semibold">
                  <div>
                    <span className="text-slate-400 block uppercase font-medium">Kinh nghiệm</span>
                    <span className="font-bold text-slate-700 block mt-0.5">{vet.experienceYears}</span>
                  </div>
                  <div className="border-l border-slate-100">
                    <span className="text-slate-400 block uppercase font-medium">Đánh giá</span>
                    <span className="font-bold text-teal-600 block mt-0.5">⭐ {vet.rating}</span>
                  </div>
                </div>

                <button
                  onClick={() => handleBookingClick(vet.id)}
                  className="w-full flex items-center justify-center space-x-2 py-3 bg-teal-600 hover:bg-teal-700 text-white text-xs font-bold rounded-xl shadow-md hover:shadow-teal-500/10 transition duration-200"
                >
                  <Calendar className="h-4 w-4" />
                  <span>Đặt lịch khám</span>
                </button>
              </div>
            </div>
          ))}
        </div>

      </div>
    </DashboardLayout>
  );
};
export default Doctors;
