import React from 'react';
import { Calendar as CalendarIcon, User, Clock, FileText, Check, Plus, AlertCircle } from 'lucide-react';
import type { Pet } from './PetCard';
import api from '../services/api';

export interface Vet {
  id: number;
  name: string;
  specialty: string;
  experienceYears?: string;
  rating?: number;
  image?: string;
}

export interface ServiceItem {
  name: string;
  price: number;
}

interface AppointmentBookingFormProps {
  pets: Pet[];
  vets: Vet[];
  services?: ServiceItem[];
  onSubmit: (data: {
    petId: string;
    service: string;
    vetId: string;
    date: string;
    time: string;
    reason: string;
    paymentMethod: string;
  }) => void;
  isLoading?: boolean;
  initialVetId?: string;
  onCancel?: () => void;
}

export const AppointmentBookingForm: React.FC<AppointmentBookingFormProps> = ({ 
  pets, 
  vets, 
  services: servicesProp,
  onSubmit, 
  isLoading = false,
  initialVetId,
  onCancel
}) => {
  const [step, setStep] = React.useState(1);
  const [formData, setFormData] = React.useState({
    petId: '',
    service: 'Khám bệnh tổng quát',
    vetId: initialVetId || '',
    date: '',
    time: '',
    reason: '',
    paymentMethod: 'DIRECT',
  });

  React.useEffect(() => {
    if (initialVetId) {
      setFormData(prev => ({ ...prev, vetId: initialVetId }));
    }
  }, [initialVetId]);

  const [showAddPetModal, setShowAddPetModal] = React.useState(false);
  const [newPetName, setNewPetName] = React.useState('');
  const [newPetSpecies, setNewPetSpecies] = React.useState('Dog');
  const [localPets, setLocalPets] = React.useState<Pet[]>(pets);

  React.useEffect(() => {
    setLocalPets(pets);
  }, [pets]);

  // Clinical medical services list only
  const services = servicesProp && servicesProp.length > 0 ? servicesProp : [
    { name: 'Khám bệnh tổng quát', price: 100000 },
    { name: 'Tiêm phòng vaccine', price: 150000 },
    { name: 'Phẫu thuật ngoại khoa', price: 1000000 },
    { name: 'Xét nghiệm & Siêu âm', price: 300000 },
    { name: 'Nha khoa thú y', price: 250000 },
    { name: 'Điều trị nội trú theo dõi', price: 500000 }
  ];

  const timeSlots = [
    '08:00', '08:30', '09:00', '09:30', '10:00', '10:30', '11:00', '11:30',
    '13:30', '14:00', '14:30', '15:00', '15:30', '16:00', '16:30', '17:00'
  ];

  const [busySlots, setBusySlots] = React.useState<string[]>([]);

  React.useEffect(() => {
    if (!formData.date) {
      setBusySlots([]);
      return;
    }
    const fetchBusySlots = async () => {
      try {
        const params: any = { date: formData.date };
        if (formData.vetId) {
          params.vetId = formData.vetId;
        }
        const res = await api.get('/appointments/busy-slots', { params });
        setBusySlots(res.data || []);
      } catch (err) {
        console.error('Error fetching busy slots:', err);
        setBusySlots([]);
      }
    };
    fetchBusySlots();
  }, [formData.date, formData.vetId]);

  const isSlotDisabled = (slot: string) => {
    if (!formData.date) return false;
    
    // 1. Check if the slot is in the past (for today)
    const todayStr = new Date().toLocaleDateString('en-CA');
    if (formData.date === todayStr) {
      const now = new Date();
      const [slotHour, slotMin] = slot.split(':').map(Number);
      const slotTime = new Date();
      slotTime.setHours(slotHour, slotMin, 0, 0);
      if (slotTime.getTime() < now.getTime()) {
        return true;
      }
    }
    
    // 2. Check if the slot is already busy/booked
    return busySlots.includes(slot);
  };

  React.useEffect(() => {
    if (formData.date && formData.time) {
      const isDisabled = isSlotDisabled(formData.time);
      if (isDisabled) {
        setFormData(prev => ({ ...prev, time: '' }));
      }
    }
  }, [formData.date, formData.time, busySlots]);

  const nextStep = () => setStep((prev) => Math.min(prev + 1, 4));
  const prevStep = () => setStep((prev) => Math.max(prev - 1, 1));

  const handleFormSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (step === 4) {
      onSubmit(formData);
    }
  };

  const handleAddPetSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newPetName.trim()) return;

    const newPet: Pet = {
      id: Date.now(),
      name: newPetName,
      species: newPetSpecies,
      breed: 'Lai',
      avatarUrl: null,
      age: 'Chưa cập nhật',
      gender: 'Male',
      weight: 'Chưa đo',
      lastVisitDate: 'Mới đăng ký',
      status: 'Healthy'
    };

    setLocalPets(prev => [...prev, newPet]);
    setFormData(prev => ({ ...prev, petId: newPet.id.toString() }));
    setNewPetName('');
    setShowAddPetModal(false);
  };

  const selectedPet = localPets.find(p => p.id.toString() === formData.petId);
  const selectedVet = vets.find(v => v.id.toString() === formData.vetId);
  const selectedServiceObj = services.find(s => s.name === formData.service);
  const selectedServicePrice = selectedServiceObj ? selectedServiceObj.price : 100000;

  return (
    <div className="space-y-8 max-w-xl mx-auto">
      {/* Stepper Progress Bar */}
      <div className="flex items-center justify-between relative py-2 mx-auto">
        <div className="absolute left-0 right-0 top-1/2 h-0.5 bg-slate-200 -z-10" />
        <div className="absolute left-0 top-1/2 h-0.5 bg-teal-500 transition-all duration-500 -z-10" style={{ width: `${(step - 1) * 33.33}%` }} />
        
        <div className="flex flex-col items-center">
          <button 
            type="button"
            onClick={() => step > 1 && setStep(1)}
            className={`h-10 w-10 rounded-full flex items-center justify-center font-bold text-sm border-2 transition duration-200 ${
              step >= 1 
                ? 'bg-teal-600 border-teal-600 text-white shadow-lg shadow-teal-500/20' 
                : 'bg-white border-slate-300 text-slate-500'
            }`}
          >
            {step > 1 ? <Check className="h-4 w-4" /> : '1'}
          </button>
          <span className="text-xs font-bold mt-2 text-slate-600 text-center">Dịch vụ</span>
        </div>
        
        <div className="flex flex-col items-center">
          <button 
            type="button"
            onClick={() => step > 2 && setStep(2)}
            disabled={step < 2}
            className={`h-10 w-10 rounded-full flex items-center justify-center font-bold text-sm border-2 transition duration-200 ${
              step >= 2 
                ? 'bg-teal-600 border-teal-600 text-white shadow-lg shadow-teal-500/20' 
                : 'bg-white border-slate-300 text-slate-500'
            }`}
          >
            {step > 2 ? <Check className="h-4 w-4" /> : '2'}
          </button>
          <span className="text-xs font-bold mt-2 text-slate-600 text-center">Thời gian</span>
        </div>
        
        <div className="flex flex-col items-center">
          <button 
            type="button"
            onClick={() => step > 3 && setStep(3)}
            disabled={step < 3}
            className={`h-10 w-10 rounded-full flex items-center justify-center font-bold text-sm border-2 transition duration-200 ${
              step >= 3 
                ? 'bg-teal-600 border-teal-600 text-white shadow-lg shadow-teal-500/20' 
                : 'bg-white border-slate-300 text-slate-500'
            }`}
          >
            {step > 3 ? <Check className="h-4 w-4" /> : '3'}
          </button>
          <span className="text-xs font-bold mt-2 text-slate-600 text-center">Xác nhận</span>
        </div>

        <div className="flex flex-col items-center">
          <div className={`h-10 w-10 rounded-full flex items-center justify-center font-bold text-sm border-2 transition duration-200 ${
            step >= 4 
              ? 'bg-teal-600 border-teal-600 text-white shadow-lg shadow-teal-500/20' 
              : 'bg-white border-slate-300 text-slate-500'
          }`}>
            4
          </div>
          <span className="text-xs font-bold mt-2 text-slate-600 text-center">Thanh toán</span>
        </div>
      </div>

      {/* Form Container */}
      <div className="bg-white border border-slate-200/80 rounded-3xl p-6 sm:p-8 shadow-xl shadow-slate-100">
        <form onSubmit={handleFormSubmit} className="space-y-6">
          
          {/* Step 1: Select Pet, Service & Vet */}
          {step === 1 && (
            <div className="space-y-6 animate-fade-in">
              
              {/* Pet selection */}
              <div>
                <div className="flex justify-between items-center mb-2">
                  <label className="block text-sm font-bold text-slate-700 flex items-center gap-2">
                    🐾 Chọn thú cưng
                  </label>
                  <button
                    type="button"
                    onClick={() => setShowAddPetModal(true)}
                    className="text-xs font-semibold text-teal-600 hover:text-teal-700 flex items-center gap-1 transition"
                  >
                    <Plus className="h-3.5 w-3.5" /> Thêm thú cưng mới
                  </button>
                </div>
                <select 
                  value={formData.petId}
                  onChange={(e) => setFormData({ ...formData, petId: e.target.value })}
                  className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl text-sm outline-none focus:bg-white focus:border-teal-500 focus:ring-2 focus:ring-teal-500/20 transition duration-200"
                  required
                >
                  <option value="">-- Vui lòng chọn thú cưng --</option>
                  {localPets.map(pet => (
                    <option key={pet.id} value={pet.id}>{pet.name} ({pet.species === 'Dog' ? 'Chó' : pet.species === 'Cat' ? 'Mèo' : pet.species} - {pet.breed})</option>
                  ))}
                </select>
                {localPets.length === 0 && (
                  <p className="text-xs text-orange-500 mt-1.5 flex items-center gap-1">
                    <AlertCircle className="h-3 w-3" /> Bạn chưa đăng ký thú cưng nào. Hãy nhấn nút thêm nhanh phía trên.
                  </p>
                )}
              </div>

              {/* Service Selection */}
              <div>
                <label className="block text-sm font-bold text-slate-700 mb-2 flex items-center gap-2">
                  🩺 Chọn loại dịch vụ y tế
                </label>
                <select 
                  value={formData.service}
                  onChange={(e) => setFormData({ ...formData, service: e.target.value })}
                  className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl text-sm outline-none focus:bg-white focus:border-teal-500 focus:ring-2 focus:ring-teal-500/20 transition duration-200"
                  required
                >
                  {services.map((service, idx) => (
                    <option key={idx} value={service.name}>{service.name}</option>
                  ))}
                </select>
              </div>

              {/* Vet Selection Cards Grid */}
              <div>
                <label className="block text-sm font-bold text-slate-700 mb-3 flex items-center gap-2">
                  <User className="h-4 w-4 text-slate-400" /> Chọn bác sĩ phụ trách
                </label>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                  {/* Random Option Card */}
                  <div
                    onClick={() => setFormData({ ...formData, vetId: '' })}
                    className={`flex items-center space-x-4 p-4 border rounded-2xl cursor-pointer transition duration-200 ${
                      formData.vetId === ''
                        ? 'border-teal-500 bg-teal-50/30 ring-2 ring-teal-500/10'
                        : 'border-slate-200 bg-white hover:border-slate-300'
                    }`}
                  >
                    <div className="h-12 w-12 rounded-full bg-slate-100 flex items-center justify-center text-slate-500 text-lg font-bold">
                      🎲
                    </div>
                    <div>
                      <h4 className="font-bold text-slate-800 text-sm">Bác sĩ bất kỳ</h4>
                      <p className="text-slate-400 text-xs">Hệ thống chọn ngẫu nhiên</p>
                    </div>
                  </div>

                  {vets.map((vet) => {
                    const isSelected = String(formData.vetId) === String(vet.id);
                    const docImg = vet.image;
                    const initials = vet.name.split(' ').map((n: string) => n[0]).join('').substring(0, 2).toUpperCase();

                    return (
                      <div
                        key={vet.id}
                        onClick={() => setFormData({ ...formData, vetId: String(vet.id) })}
                        className={`flex items-center space-x-4 p-4 border rounded-2xl cursor-pointer transition duration-200 ${
                          isSelected
                            ? 'border-teal-500 bg-teal-50/30 ring-2 ring-teal-500/10'
                            : 'border-slate-200 bg-white hover:border-slate-300'
                        }`}
                      >
                        {docImg ? (
                          <img
                            src={docImg}
                            alt={vet.name}
                            className="h-12 w-12 rounded-full object-cover border border-slate-100"
                          />
                        ) : (
                          <div className="h-12 w-12 rounded-full bg-gradient-to-tr from-teal-500 to-teal-400 text-white flex items-center justify-center text-sm font-bold shadow-inner">
                            {initials}
                          </div>
                        )}
                        <div className="flex-1 min-w-0">
                          <h4 className="font-bold text-slate-800 text-sm truncate">{vet.name}</h4>
                          <p className="text-slate-400 text-[11px] truncate">{vet.specialty || 'Bác sĩ Thú y'}</p>
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>

              <div className="flex justify-between pt-4">
                {onCancel ? (
                  <button 
                    type="button" 
                    onClick={onCancel}
                    className="border border-slate-200 hover:bg-slate-50 text-slate-500 font-bold py-3 px-6 rounded-xl text-sm transition"
                  >
                    Hủy
                  </button>
                ) : <div />}
                <button 
                  type="button" 
                  onClick={nextStep}
                  disabled={!formData.petId}
                  className="bg-teal-600 hover:bg-teal-700 text-white font-bold py-3 px-8 rounded-xl text-sm shadow-md hover:shadow-lg hover:shadow-teal-500/10 transition duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Tiếp theo
                </button>
              </div>
            </div>
          )}

          {/* Step 2: Choose Date & Time slots (Grid layout) */}
          {step === 2 && (
            <div className="space-y-6 animate-fade-in">
              <div>
                <label className="block text-sm font-bold text-slate-700 mb-2 flex items-center gap-2">
                  <CalendarIcon className="h-4 w-4 text-slate-400" /> Chọn ngày đặt lịch
                </label>
                <input 
                  type="date"
                  value={formData.date}
                  onChange={(e) => setFormData({ ...formData, date: e.target.value })}
                  className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl text-sm outline-none focus:bg-white focus:border-teal-500 focus:ring-2 focus:ring-teal-500/20 transition duration-200"
                  min={new Date().toLocaleDateString('en-CA')}
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-bold text-slate-700 mb-3 flex items-center gap-2">
                  <Clock className="h-4 w-4 text-slate-400" /> Chọn khung giờ khám trống
                </label>
                
                {/* Time slot buttons grid */}
                <div className="grid grid-cols-4 gap-2.5">
                  {timeSlots.map((slot) => {
                    const isSelected = formData.time === slot;
                    const isDisabled = isSlotDisabled(slot);
                    return (
                      <button
                        key={slot}
                        type="button"
                        disabled={isDisabled}
                        onClick={() => setFormData({ ...formData, time: slot })}
                        className={`py-2.5 px-2 rounded-xl text-xs font-bold border transition duration-200 ${
                          isDisabled
                            ? 'bg-slate-100 border-slate-100 text-slate-300 cursor-not-allowed opacity-50'
                            : isSelected 
                            ? 'bg-teal-600 border-teal-600 text-white shadow-md shadow-teal-500/15' 
                            : 'bg-slate-50 border-slate-200 text-slate-600 hover:bg-slate-100 hover:border-slate-300'
                        }`}
                      >
                        {slot}
                      </button>
                    );
                  })}
                </div>
              </div>

              <div className="flex justify-between pt-4 border-t border-slate-100">
                <div className="flex gap-2">
                  {onCancel && (
                    <button 
                      type="button" 
                      onClick={onCancel}
                      className="border border-slate-200 hover:bg-slate-50 text-slate-500 font-bold py-3 px-6 rounded-xl text-sm transition"
                    >
                      Hủy
                    </button>
                  )}
                  <button 
                    type="button" 
                    onClick={prevStep}
                    className="border border-slate-200 hover:bg-slate-50 text-slate-600 font-bold py-3 px-6 rounded-xl text-sm transition"
                  >
                    Quay lại
                  </button>
                </div>
                <button 
                  type="button" 
                  onClick={nextStep}
                  disabled={!formData.date || !formData.time}
                  className="bg-teal-600 hover:bg-teal-700 text-white font-bold py-3 px-8 rounded-xl text-sm shadow-md hover:shadow-lg hover:shadow-teal-500/10 transition duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Tiếp theo
                </button>
              </div>
            </div>
          )}

          {/* Step 3: Confirmation & Symptoms text */}
          {step === 3 && (
            <div className="space-y-6 animate-fade-in">
              <div className="bg-slate-50 border border-slate-200/80 p-5 rounded-2xl space-y-4">
                <h3 className="font-bold text-slate-800 border-b border-slate-200 pb-2 text-sm uppercase tracking-wider text-teal-700">Tóm tắt lịch hẹn</h3>
                <div className="grid grid-cols-2 gap-y-3 gap-x-4 text-xs sm:text-sm">
                  <div>
                    <span className="text-slate-400 block font-medium">Thú cưng</span>
                    <span className="font-bold text-slate-700">{selectedPet ? selectedPet.name : 'Chưa chọn'}</span>
                  </div>
                  <div>
                    <span className="text-slate-400 block font-medium">Dịch vụ</span>
                    <span className="font-bold text-slate-700">{formData.service}</span>
                  </div>
                  <div>
                    <span className="text-slate-400 block font-medium">Bác sĩ</span>
                    <span className="font-bold text-slate-700">{selectedVet ? selectedVet.name : 'Bác sĩ ngẫu nhiên'}</span>
                  </div>
                  <div>
                    <span className="text-slate-400 block font-medium">Thời gian</span>
                    <span className="font-bold text-slate-700 text-orange-600">{formData.date} lúc {formData.time}</span>
                  </div>
                </div>
              </div>

              <div>
                <label className="block text-sm font-bold text-slate-700 mb-2 flex items-center gap-2">
                  <FileText className="h-4 w-4 text-slate-400" /> Tình trạng sức khỏe & Ghi chú
                </label>
                <textarea 
                  value={formData.reason}
                  onChange={(e) => setFormData({ ...formData, reason: e.target.value })}
                  rows={4}
                  className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl text-sm outline-none focus:bg-white focus:border-teal-500 focus:ring-2 focus:ring-teal-500/20 transition duration-200"
                  placeholder="Vui lòng mô tả chi tiết biểu hiện bệnh của bé hoặc ghi chú thêm cho bác sĩ..."
                />
              </div>

              <div className="flex justify-between pt-4 border-t border-slate-100">
                <div className="flex gap-2">
                  {onCancel && (
                    <button 
                      type="button" 
                      onClick={onCancel}
                      className="border border-slate-200 hover:bg-slate-50 text-slate-500 font-bold py-3 px-6 rounded-xl text-sm transition"
                      disabled={isLoading}
                    >
                      Hủy
                    </button>
                  )}
                  <button 
                    type="button" 
                    onClick={prevStep}
                    className="border border-slate-200 hover:bg-slate-50 text-slate-600 font-bold py-3 px-6 rounded-xl text-sm transition"
                    disabled={isLoading}
                  >
                    Quay lại
                  </button>
                </div>
                <button 
                  type="button"
                  onClick={nextStep}
                  className="bg-teal-600 hover:bg-teal-700 text-white font-bold py-3 px-8 rounded-xl text-sm shadow-md hover:shadow-lg hover:shadow-teal-500/10 transition duration-200 flex items-center gap-2"
                >
                  Tiếp theo
                </button>
              </div>
            </div>
          )}

          {/* Step 4: Payment method selection */}
          {step === 4 && (
            <div className="space-y-6 animate-fade-in">
              <div className="bg-slate-50 border border-slate-200/80 p-5 rounded-2xl space-y-4">
                <h3 className="font-bold text-slate-800 border-b border-slate-200 pb-2 text-sm uppercase tracking-wider text-teal-700">Thông tin thanh toán</h3>
                <div className="flex justify-between items-center text-sm">
                  <span className="text-slate-600 font-medium">Dịch vụ:</span>
                  <span className="font-bold text-slate-800">{formData.service}</span>
                </div>
                <div className="flex justify-between items-center text-sm">
                  <span className="text-slate-600 font-medium">Lệ phí tạm tính:</span>
                  <span className="font-extrabold text-orange-600">{selectedServicePrice.toLocaleString('vi-VN')}đ</span>
                </div>
              </div>

              <div className="space-y-3">
                <label className="block text-sm font-bold text-slate-700 mb-2">
                  💳 Chọn phương thức thanh toán
                </label>
                
                {/* DIRECT Payment Option Card */}
                <div
                  onClick={() => setFormData({ ...formData, paymentMethod: 'DIRECT' })}
                  className={`flex items-start space-x-4 p-4 border rounded-2xl cursor-pointer transition duration-200 ${
                    formData.paymentMethod === 'DIRECT'
                      ? 'border-teal-500 bg-teal-50/30 ring-2 ring-teal-500/10'
                      : 'border-slate-200 bg-white hover:border-slate-300'
                  }`}
                >
                  <div className="h-10 w-10 rounded-full bg-slate-100 flex items-center justify-center text-slate-600 text-lg">
                    💵
                  </div>
                  <div className="flex-1 min-w-0">
                    <h4 className="font-bold text-slate-800 text-sm">Thanh toán trực tiếp tại quầy</h4>
                    <p className="text-slate-500 text-xs mt-0.5">Thanh toán bằng tiền mặt/quẹt thẻ khi đến phòng khám.</p>
                  </div>
                  {formData.paymentMethod === 'DIRECT' && (
                    <div className="h-5 w-5 rounded-full bg-teal-600 flex items-center justify-center text-white text-xs">
                      ✓
                    </div>
                  )}
                </div>

                {/* VNPAY Payment Option Card */}
                <div
                  onClick={() => setFormData({ ...formData, paymentMethod: 'VNPAY' })}
                  className={`flex items-start space-x-4 p-4 border rounded-2xl cursor-pointer transition duration-200 ${
                    formData.paymentMethod === 'VNPAY'
                      ? 'border-teal-500 bg-teal-50/30 ring-2 ring-teal-500/10'
                      : 'border-slate-200 bg-white hover:border-slate-300'
                  }`}
                >
                  <div className="h-10 w-10 rounded-full bg-slate-100 flex items-center justify-center text-slate-600 text-lg">
                    💳
                  </div>
                  <div className="flex-1 min-w-0">
                    <h4 className="font-bold text-slate-800 text-sm flex items-center gap-2">
                      Thanh toán online qua VNPay
                      <span className="text-[10px] font-bold bg-orange-100 text-orange-600 py-0.5 px-1.5 rounded-full">Sandbox</span>
                    </h4>
                    <p className="text-slate-500 text-xs mt-0.5">Thanh toán an toàn qua cổng VNPay (hỗ trợ ATM, Thẻ quốc tế, QR).</p>
                  </div>
                  {formData.paymentMethod === 'VNPAY' && (
                    <div className="h-5 w-5 rounded-full bg-teal-600 flex items-center justify-center text-white text-xs">
                      ✓
                    </div>
                  )}
                </div>
              </div>

              <div className="flex justify-between pt-4 border-t border-slate-100">
                <div className="flex gap-2">
                  {onCancel && (
                    <button 
                      type="button" 
                      onClick={onCancel}
                      className="border border-slate-200 hover:bg-slate-50 text-slate-500 font-bold py-3 px-6 rounded-xl text-sm transition"
                      disabled={isLoading}
                    >
                      Hủy
                    </button>
                  )}
                  <button 
                    type="button" 
                    onClick={prevStep}
                    className="border border-slate-200 hover:bg-slate-50 text-slate-600 font-bold py-3 px-6 rounded-xl text-sm transition"
                    disabled={isLoading}
                  >
                    Quay lại
                  </button>
                </div>
                <button 
                  type="submit"
                  disabled={isLoading}
                  className="bg-gradient-to-r from-orange-500 to-amber-500 hover:from-orange-600 hover:to-amber-600 text-white font-bold py-3 px-8 rounded-xl text-sm shadow-md hover:shadow-lg hover:shadow-orange-500/10 transition duration-200 disabled:opacity-75 flex items-center gap-2"
                >
                  {isLoading ? 'Đang gửi...' : 'Xác nhận đặt lịch'}
                </button>
              </div>
            </div>
          )}

        </form>
      </div>

      {/* Add Pet Modal */}
      {showAddPetModal && (
        <div className="fixed inset-0 bg-slate-900/60 z-50 flex items-center justify-center p-4 backdrop-blur-sm">
          <div className="bg-white rounded-3xl p-6 sm:p-8 max-w-sm w-full shadow-2xl border border-slate-100 animate-scale-up">
            <h3 className="text-lg font-bold text-slate-900 mb-4 flex items-center gap-2">🐾 Đăng ký nhanh thú cưng</h3>
            <form onSubmit={handleAddPetSubmit} className="space-y-4">
              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-1.5">Tên thú cưng</label>
                <input 
                  type="text"
                  value={newPetName}
                  onChange={(e) => setNewPetName(e.target.value)}
                  className="w-full px-4 py-2.5 border border-slate-200 rounded-xl text-sm outline-none focus:border-teal-500 focus:ring-2 focus:ring-teal-500/10 transition"
                  placeholder="Ví dụ: Milo, Lu"
                  required
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-1.5">Loài vật nuôi</label>
                <select 
                  value={newPetSpecies}
                  onChange={(e) => setNewPetSpecies(e.target.value)}
                  className="w-full px-4 py-2.5 border border-slate-200 rounded-xl text-sm outline-none focus:border-teal-500 focus:ring-2 focus:ring-teal-500/10 transition"
                >
                  <option value="Dog">Chó (Dog)</option>
                  <option value="Cat">Mèo (Cat)</option>
                  <option value="Other">Khác (Other)</option>
                </select>
              </div>

              <div className="flex justify-end space-x-2 pt-4 border-t border-slate-100">
                <button
                  type="button"
                  onClick={() => setShowAddPetModal(false)}
                  className="px-4 py-2 text-sm text-slate-500 font-semibold hover:bg-slate-100 rounded-lg transition"
                >
                  Hủy
                </button>
                <button
                  type="submit"
                  className="px-5 py-2 text-sm bg-teal-600 hover:bg-teal-700 text-white font-bold rounded-lg transition shadow-md hover:shadow-teal-500/10"
                >
                  Thêm
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

    </div>
  );
};
export default AppointmentBookingForm;
