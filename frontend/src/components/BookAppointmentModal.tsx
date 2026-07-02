import React, { useEffect, useState } from 'react';
import { X } from 'lucide-react';
import AppointmentBookingForm from './AppointmentBookingForm';
import type { Vet } from './AppointmentBookingForm';
import type { Pet } from './PetCard';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';
import { showToast } from './Toast';

interface BookAppointmentModalProps {
  isOpen: boolean;
  onClose: () => void;
  initialVetId?: string;
}

export const BookAppointmentModal: React.FC<BookAppointmentModalProps> = ({ 
  isOpen, 
  onClose,
  initialVetId 
}) => {
  const { user } = useAuth();
  const [pets, setPets] = useState<Pet[]>([]);
  const [vets, setVets] = useState<Vet[]>([]);
  const [services, setServices] = useState<{ name: string; price: number }[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    if (!isOpen) return;

    const loadFormData = async () => {
      if (!user?.id) return;
      try {
        let doctorProfiles: any[] = [];
        try {
          const docRes = await api.get('/doctors');
          doctorProfiles = docRes.data;
        } catch (docErr) {
          console.warn('Error fetching doctor profiles:', docErr);
        }

        let serviceList: { name: string; price: number }[] = [];
        try {
          const serviceRes = await api.get('/services');
          serviceList = serviceRes.data.map((s: any) => ({
            name: s.name,
            price: s.price
          }));
        } catch (servErr) {
          console.warn('Error fetching services:', servErr);
          serviceList = [
            { name: 'Khám bệnh tổng quát', price: 100000 },
            { name: 'Tiêm phòng vaccine', price: 150000 },
            { name: 'Phẫu thuật ngoại khoa', price: 1000000 },
            { name: 'Xét nghiệm & Siêu âm', price: 300000 },
            { name: 'Nha khoa thú y', price: 250000 },
            { name: 'Điều trị nội trú theo dõi', price: 500000 }
          ];
        }

        const response = await api.get('/appointments/form-data', {
          params: { ownerId: user.id }
        });
        
        const mappedPets = response.data.pets.map((p: any) => ({
          id: p.id,
          name: p.name,
          species: p.species === 'DOG' ? 'Dog' : (p.species === 'CAT' ? 'Cat' : p.species),
          breed: p.breed || '',
          avatarUrl: null,
          age: '',
          gender: ''
        }));

        const mappedVets = response.data.veterinarians.map((v: any) => {
          const docProfile = doctorProfiles.find((d: any) => d.name.toLowerCase() === v.fullName.toLowerCase());
          return {
            id: v.id,
            name: v.fullName,
            specialty: docProfile?.specialty || 'Bác sĩ Thú y PetCare',
            image: docProfile?.image || null
          };
        });

        setPets(mappedPets);
        setVets(mappedVets);
        setServices(serviceList);
      } catch (err) {
        console.error('Error fetching appointment form options:', err);
      }
    };
    loadFormData();
  }, [user, isOpen]);

  const handleBookingSubmit = async (data: {
    petId: string;
    service: string;
    vetId: string;
    date: string;
    time: string;
    reason: string;
    paymentMethod: string;
  }) => {
    setIsLoading(true);
    try {
      const appointmentDateTime = `${data.date}T${data.time}:00`;
      const vetId = data.vetId && data.vetId.trim() !== '' ? parseInt(data.vetId) : null;
      const response = await api.post('/appointments', {
        ownerId: user?.id,
        petId: parseInt(data.petId),
        veterinarianId: vetId,
        appointmentAt: appointmentDateTime,
        reasonForVisit: data.reason || data.service,
        serviceName: data.service,
        paymentMethod: data.paymentMethod
      });

      setIsLoading(false);

      if (response.data.paymentUrl) {
        showToast('Đang chuyển hướng đến cổng thanh toán VNPay...', 'success');
        // Small delay to let user see toast
        setTimeout(() => {
          window.location.href = response.data.paymentUrl;
        }, 1000);
        return;
      }

      showToast(
        `Đặt lịch khám thành công tại PetCare!\n` +
        `• Thú cưng: ${pets.find(p => p.id.toString() === data.petId)?.name || 'Mới đăng ký'}\n` +
        `• Dịch vụ: ${data.service}\n` +
        `• Bác sĩ: ${vets.find(v => v.id.toString() === data.vetId)?.name || 'Bác sĩ ngẫu nhiên'}\n` +
        `• Thời gian: ${data.date} lúc ${data.time}`,
        'success'
      );
      onClose();
    } catch (err: any) {
      setIsLoading(false);
      console.error('Error booking appointment:', err);
      showToast('Đặt lịch khám bệnh thất bại! Vui lòng kiểm tra lại thông tin.', 'error');
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-slate-900/60 z-50 flex items-center justify-center p-4 backdrop-blur-sm overflow-y-auto">
      <div className="bg-white rounded-3xl p-6 sm:p-8 max-w-2xl w-full shadow-2xl border border-slate-100 animate-scale-up my-8 relative animate-fade-in">
        <button
          onClick={onClose}
          className="absolute top-4 right-4 p-2 text-slate-400 hover:text-slate-600 hover:bg-slate-100 rounded-full transition"
        >
          <X className="h-6 w-6" />
        </button>

        <div className="text-center space-y-3 mb-6">
          <span className="text-teal-600 font-bold text-xs uppercase tracking-widest bg-teal-50 py-1.5 px-3 rounded-full inline-block">Đăng ký khám</span>
          <h2 className="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">Đăng ký lịch khám bệnh</h2>
          <p className="text-slate-500 text-sm max-w-md mx-auto leading-relaxed">
            Đặt lịch khám nhanh chóng cho thú cưng của bạn chỉ với 3 bước đơn giản.
          </p>
        </div>

        <AppointmentBookingForm 
          pets={pets} 
          vets={vets} 
          services={services}
          onSubmit={handleBookingSubmit} 
          isLoading={isLoading} 
          initialVetId={initialVetId}
          onCancel={onClose}
        />
      </div>
    </div>
  );
};

export default BookAppointmentModal;
