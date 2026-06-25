import React, { useEffect, useState } from 'react';
import DashboardLayout from '../../layouts/DashboardLayout';
import AppointmentBookingForm from '../../components/AppointmentBookingForm';
import type { Vet } from '../../components/AppointmentBookingForm';
import type { Pet } from '../../components/PetCard';
import { useAuth } from '../../context/AuthContext';
import api from '../../services/api';

export const BookAppointment: React.FC = () => {
  const { user } = useAuth();
  const [pets, setPets] = useState<Pet[]>([]);
  const [vets, setVets] = useState<Vet[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const loadFormData = async () => {
      if (!user?.id) return;
      try {
        const response = await api.get('/appointments/form-data', {
          params: { ownerId: user.id }
        });
        
        // Map pets from PetOption
        const mappedPets = response.data.pets.map((p: any) => ({
          id: p.id,
          name: p.name,
          species: 'Other',
          breed: '',
          avatarUrl: null,
          age: '',
          gender: ''
        }));

        // Map vets from VeterinarianOption
        const mappedVets = response.data.veterinarians.map((v: any) => ({
          id: v.id,
          name: v.fullName,
          specialty: 'Bác sĩ Thú y PetCare'
        }));

        setPets(mappedPets);
        setVets(mappedVets);
      } catch (err) {
        console.error('Error fetching appointment form options:', err);
      }
    };
    loadFormData();
  }, [user]);

  const handleBookingSubmit = async (data: {
    petId: string;
    service: string;
    vetId: string;
    date: string;
    time: string;
    reason: string;
  }) => {
    setIsLoading(true);
    try {
      const appointmentDateTime = `${data.date}T${data.time}:00`;
      await api.post('/appointments', {
        ownerId: user?.id,
        petId: parseInt(data.petId),
        veterinarianId: parseInt(data.vetId),
        appointmentAt: appointmentDateTime,
        reasonForVisit: data.reason || data.service
      });

      setIsLoading(false);
      alert(
        `🎉 Đặt lịch khám thành công tại PetCare!\n\n` +
        `• Thú cưng: ${pets.find(p => p.id.toString() === data.petId)?.name || 'Mới đăng ký'}\n` +
        `• Dịch vụ: ${data.service}\n` +
        `• Bác sĩ: ${vets.find(v => v.id.toString() === data.vetId)?.name || 'Bác sĩ ngẫu nhiên'}\n` +
        `• Thời gian: ${data.date} lúc ${data.time}\n` +
        `• Lý do khám: ${data.reason}`
      );
    } catch (err: any) {
      setIsLoading(false);
      console.error('Error booking appointment:', err);
      alert('❌ Đặt lịch khám bệnh thất bại! Vui lòng kiểm tra lại thông tin.');
    }
  };

  return (
    <DashboardLayout>
      <div className="max-w-3xl mx-auto px-4 py-8 sm:py-12 animate-fade-in font-sans">
        <div className="text-center space-y-3 mb-8">
          <span className="text-teal-600 font-bold text-xs uppercase tracking-widest bg-teal-50 py-1.5 px-3 rounded-full">Đăng ký khám</span>
          <h1 className="text-3xl sm:text-4xl font-extrabold text-slate-900 tracking-tight">Đăng ký lịch khám bệnh</h1>
          <p className="text-slate-500 text-sm sm:text-base max-w-lg mx-auto leading-relaxed">
            Đặt lịch khám nhanh chóng cho thú cưng của bạn chỉ với 3 bước đơn giản. Đội ngũ bác sĩ y khoa PetCare luôn sẵn sàng hỗ trợ.
          </p>
        </div>

        <AppointmentBookingForm 
          pets={pets} 
          vets={vets} 
          onSubmit={handleBookingSubmit} 
          isLoading={isLoading} 
        />
      </div>
    </DashboardLayout>
  );
};

export default BookAppointment;
