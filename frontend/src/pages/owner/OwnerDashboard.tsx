import React, { useEffect, useRef, useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import DashboardLayout from '../../layouts/DashboardLayout';
import { User as UserIcon, Heart, Calendar, Plus, Camera, Mail, Phone, MapPin, Eye, FileText, Download, X } from 'lucide-react';
import PetCard from '../../components/PetCard';
import type { Pet } from '../../components/PetCard';
import PrescriptionViewer from '../../components/PrescriptionViewer';
import type { PrescriptionItem } from '../../components/PrescriptionViewer';
import InvoiceSummary from '../../components/InvoiceSummary';
import type { InvoiceServiceItem, InvoicePrescriptionItem } from '../../components/InvoiceSummary';
import { useAuth } from '../../context/AuthContext';
import api from '../../services/api';

interface DetailedMedicalRecord {
  id: number;
  petName: string;
  vetName: string;
  visitDate: string;
  diagnosis: string;
  treatmentNote: string;
  followUpInstruction?: string;
  status: 'Completed' | 'Upcoming' | 'Cancelled';
  service: string;
  prescriptions: PrescriptionItem[];
  services: InvoiceServiceItem[];
  medications: InvoicePrescriptionItem[];
  labResultFile?: { name: string; url: string } | null;
  paymentStatus: string;
  totalAmount: number;
}

export const OwnerDashboard: React.FC<{ defaultTab: 'pets' | 'history' }> = ({ defaultTab }) => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { user, updateUser } = useAuth();
  
  // Tab logic: read tab from query params if available, else use defaultTab prop
  const queryTab = searchParams.get('tab');
  const activeTab = queryTab === 'profile' ? 'profile' : (defaultTab === 'history' ? 'history' : 'pets');

  const handleTabChange = (tabName: string) => {
    if (tabName === 'profile') {
      navigate('/owner/pets?tab=profile');
    } else if (tabName === 'pets') {
      navigate('/owner/pets');
    } else {
      navigate('/owner/history');
    }
  };

  // 1. User Profile API Integration
  const [profile, setProfile] = useState({
    fullName: user?.fullName || '',
    email: user?.email || '',
    phone: '',
    address: '',
    avatar: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&q=80&w=150&h=150'
  });

  const profileFileInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await api.get('/users/profile');
        setProfile({
          fullName: response.data.fullName,
          email: response.data.email,
          phone: response.data.phone || '',
          address: response.data.address || '',
          avatar: response.data.avatarUrl || 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&q=80&w=150&h=150'
        });
        updateUser({
          fullName: response.data.fullName,
          email: response.data.email,
          phone: response.data.phone,
          address: response.data.address,
          avatarUrl: response.data.avatarUrl
        });
      } catch (err) {
        console.error('Error fetching profile:', err);
      }
    };
    if (user) {
      fetchProfile();
    }
  }, []);

  const handleProfileSave = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await api.put('/users/profile', {
        fullName: profile.fullName,
        email: profile.email,
        phone: profile.phone,
        address: profile.address,
        avatarUrl: profile.avatar
      });
      alert('🎉 Đã lưu thay đổi hồ sơ cá nhân thành công!');
      updateUser({
        fullName: response.data.fullName,
        email: response.data.email,
        phone: response.data.phone,
        address: response.data.address,
        avatarUrl: response.data.avatarUrl
      });
    } catch (err) {
      console.error('Error saving profile:', err);
      alert('❌ Lưu thay đổi hồ sơ thất bại!');
    }
  };

  const handleAvatarChange = () => {
    profileFileInputRef.current?.click();
  };

  const handleProfileFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      const file = e.target.files[0];
      const formData = new FormData();
      formData.append('file', file);
      try {
        const response = await api.post('/files/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });
        const uploadedUrl = response.data.url;
        setProfile(prev => ({ ...prev, avatar: uploadedUrl }));
        await api.put('/users/profile', {
          fullName: profile.fullName,
          email: profile.email,
          phone: profile.phone,
          address: profile.address,
          avatarUrl: uploadedUrl
        });
        updateUser({
          avatarUrl: uploadedUrl
        });
        alert('🎉 Đã cập nhật ảnh đại diện thành công!');
      } catch (err) {
        console.error('Upload failed:', err);
        alert('❌ Tải ảnh đại diện lên thất bại!');
      }
    }
  };

  // 2. Pet Profiles State & API
  const [pets, setPets] = useState<Pet[]>([]);
  const [petModalOpen, setPetModalOpen] = useState(false);
  const [editingPet, setEditingPet] = useState<Pet | null>(null);
  const [petForm, setPetForm] = useState({
    name: '',
    species: 'Dog',
    breed: '',
    age: '',
    gender: 'Male',
    weight: '',
    status: 'Khỏe mạnh',
    avatarUrl: null as string | null
  });

  const petAvatarFileInputRef = useRef<HTMLInputElement>(null);

  const fetchPets = async () => {
    try {
      const response = await api.get('/pets', { params: { ownerId: user?.id, size: 100 } });
      const mappedPets = response.data.content.map((p: any) => ({
        id: p.petId,
        name: p.petName,
        species: p.petType,
        breed: p.petBreed || '',
        age: p.petAge ? `${p.petAge} tuổi` : 'Chưa rõ',
        gender: p.petGender,
        avatarUrl: p.petAvatar,
        weight: 'N/A',
        lastVisitDate: 'Chưa khám',
        status: 'Khỏe mạnh'
      }));
      setPets(mappedPets);
    } catch (err) {
      console.error('Error fetching pets:', err);
    }
  };

  useEffect(() => {
    if (user?.id) {
      fetchPets();
    }
  }, [user]);

  const openAddPetModal = () => {
    setEditingPet(null);
    setPetForm({
      name: '',
      species: 'Dog',
      breed: '',
      age: '',
      gender: 'Male',
      weight: '',
      status: 'Khỏe mạnh',
      avatarUrl: null
    });
    setPetModalOpen(true);
  };

  const openEditPetModal = (pet: Pet) => {
    setEditingPet(pet);
    setPetForm({
      name: pet.name,
      species: pet.species,
      breed: pet.breed,
      age: pet.age.replace(' tuổi', ''),
      gender: pet.gender,
      weight: pet.weight || '',
      status: pet.status || 'Khỏe mạnh',
      avatarUrl: pet.avatarUrl
    });
    setPetModalOpen(true);
  };

  const handlePetDelete = async (id: number) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa hồ sơ thú cưng này không?')) {
      try {
        await api.delete(`/pets/${id}`);
        setPets(prev => prev.filter(p => p.id !== id));
        alert('🎉 Đã xóa hồ sơ thú cưng thành công!');
      } catch (err) {
        console.error('Error deleting pet:', err);
        alert('❌ Xóa hồ sơ thất bại!');
      }
    }
  };

  const handlePetSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const payload = {
        petName: petForm.name,
        petType: petForm.species,
        petBreed: petForm.breed,
        petAge: parseInt(petForm.age) || 0,
        petGender: petForm.gender,
        petAvatar: petForm.avatarUrl,
        ownerId: user?.id
      };

      if (editingPet) {
        await api.put(`/pets/${editingPet.id}`, payload);
        alert('🎉 Đã cập nhật hồ sơ thú cưng thành công!');
      } else {
        await api.post('/pets', payload);
        alert('🎉 Đã thêm mới thú cưng thành công!');
      }
      setPetModalOpen(false);
      fetchPets();
    } catch (err) {
      console.error('Error submitting pet:', err);
      alert('❌ Lưu hồ sơ thú cưng thất bại!');
    }
  };

  const handlePetAvatarChange = () => {
    petAvatarFileInputRef.current?.click();
  };

  const handlePetAvatarFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      const file = e.target.files[0];
      const formData = new FormData();
      formData.append('file', file);
      try {
        const response = await api.post('/files/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });
        setPetForm(prev => ({ ...prev, avatarUrl: response.data.url }));
        alert('🎉 Tải ảnh đại diện thú cưng lên thành công!');
      } catch (err) {
        console.error('Upload failed:', err);
        alert('❌ Tải ảnh đại diện thú cưng thất bại!');
      }
    }
  };

  // 3. Medical History API Integration
  const [history, setHistory] = useState<DetailedMedicalRecord[]>([]);

  useEffect(() => {
    const fetchHistory = async () => {
      if (pets.length === 0) return;
      try {
        const allRecords: DetailedMedicalRecord[] = [];
        for (const pet of pets) {
          const res = await api.get(`/pets/${pet.id}/medical-records`, { params: { size: 100 } });
          const items = res.data.content || [];
          for (const item of items) {
            allRecords.push({
              id: item.id,
              petName: pet.name,
              vetName: item.veterinarianName || 'Bác sĩ PetCare',
              visitDate: new Date(item.visitAt).toLocaleDateString('vi-VN'),
              diagnosis: item.diagnosis,
              treatmentNote: item.reasonForVisit || 'Khám lâm sàng',
              status: item.status === 'COMPLETED' ? 'Completed' : (item.status === 'UPCOMING' ? 'Upcoming' : 'Cancelled'),
              service: 'Khám điều trị',
              prescriptions: [],
              services: [],
              medications: [],
              paymentStatus: 'PAID',
              totalAmount: 0
            });
          }
        }
        allRecords.sort((a, b) => new Date(b.visitDate).getTime() - new Date(a.visitDate).getTime());
        setHistory(allRecords);
      } catch (err) {
        console.error('Error fetching medical history:', err);
      }
    };
    fetchHistory();
  }, [pets]);

  const [selectedRecord, setSelectedRecord] = useState<DetailedMedicalRecord | null>(null);

  const handleViewRecordDetails = async (recordId: number, petName: string) => {
    try {
      const response = await api.get(`/medical-records/${recordId}`);
      const data = response.data;
      
      const detailedRecord: DetailedMedicalRecord = {
        id: data.id,
        petName: petName,
        vetName: data.veterinarian?.fullName || 'Bác sĩ PetCare',
        visitDate: new Date(data.visitAt).toLocaleDateString('vi-VN'),
        diagnosis: data.diagnosis,
        treatmentNote: data.treatmentNote || 'N/A',
        followUpInstruction: data.followUpInstruction,
        status: data.status === 'COMPLETED' ? 'Completed' : (data.status === 'UPCOMING' ? 'Upcoming' : 'Cancelled'),
        service: data.reasonForVisit || 'Khám tổng quát',
        prescriptions: data.prescriptionItems || [],
        services: (data.services || []).map((s: any) => ({ name: s.name, price: s.price })),
        medications: (data.medications || []).map((m: any) => ({ name: m.name, price: m.price })),
        paymentStatus: data.bill?.status || 'PAID',
        totalAmount: data.bill?.totalPrice || 0,
        labResultFile: data.labResults && data.labResults.length > 0 ? {
          name: data.labResults[0].fileName || data.labResults[0].title,
          url: data.labResults[0].fileUrl
        } : null
      };
      setSelectedRecord(detailedRecord);
    } catch (err) {
      console.error('Error fetching medical record details:', err);
      alert('Không thể tải chi tiết ca khám bệnh!');
    }
  };

  return (
    <DashboardLayout>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        
        {/* Dashboard Grid Layout (Sidebar left, content right) */}
        <div className="grid grid-cols-1 lg:grid-cols-4 gap-8 items-start">
          
          {/* Left Sidebar Navigation */}
          <aside className="bg-white border border-slate-200/80 rounded-3xl p-6 shadow-sm space-y-6">
            <div className="flex flex-col items-center text-center space-y-3 pb-6 border-b border-slate-100">
              <div className="relative group">
                <img 
                  src={profile.avatar} 
                  alt={profile.fullName} 
                  className="h-20 w-20 rounded-full object-cover border-2 border-teal-500 shadow-inner group-hover:opacity-90 transition"
                />
                <button 
                  onClick={handleAvatarChange}
                  className="absolute bottom-0 right-0 p-1.5 bg-teal-600 text-white rounded-full hover:bg-teal-700 shadow-md transition duration-200"
                  title="Thay đổi ảnh"
                >
                  <Camera className="h-3 w-3" />
                </button>
                <input 
                  type="file" 
                  ref={profileFileInputRef} 
                  onChange={handleProfileFileChange} 
                  className="hidden" 
                  accept="image/*" 
                />
              </div>
              <div>
                <h3 className="font-bold text-slate-800 text-base">Xin chào, {profile.fullName.split(' ').pop()}!</h3>
                <span className="inline-block text-[11px] font-semibold bg-teal-50 text-teal-700 px-2.5 py-0.5 rounded-full mt-1">
                  Khách hàng Thành viên
                </span>
              </div>
            </div>

            <nav className="flex flex-col space-y-1">
              <button
                onClick={() => handleTabChange('profile')}
                className={`flex items-center space-x-3 px-4 py-3 text-sm font-bold rounded-2xl transition duration-200 ${
                  activeTab === 'profile'
                    ? 'bg-teal-600 text-white shadow-md shadow-teal-500/10'
                    : 'text-slate-600 hover:bg-slate-50 hover:text-teal-600'
                }`}
              >
                <UserIcon className="h-4.5 w-4.5" />
                <span>Hồ sơ cá nhân</span>
              </button>

              <button
                onClick={() => handleTabChange('pets')}
                className={`flex items-center space-x-3 px-4 py-3 text-sm font-bold rounded-2xl transition duration-200 ${
                  activeTab === 'pets'
                    ? 'bg-teal-600 text-white shadow-md shadow-teal-500/10'
                    : 'text-slate-600 hover:bg-slate-50 hover:text-teal-600'
                }`}
              >
                <Heart className="h-4.5 w-4.5" />
                <span>Hồ sơ thú cưng</span>
              </button>

              <button
                onClick={() => handleTabChange('history')}
                className={`flex items-center space-x-3 px-4 py-3 text-sm font-bold rounded-2xl transition duration-200 ${
                  activeTab === 'history'
                    ? 'bg-teal-600 text-white shadow-md shadow-teal-500/10'
                    : 'text-slate-600 hover:bg-slate-50 hover:text-teal-600'
                }`}
              >
                <Calendar className="h-4.5 w-4.5" />
                <span>Lịch sử khám bệnh</span>
              </button>
            </nav>
          </aside>

          {/* Right Main Content Area */}
          <div className="lg:col-span-3">
            
            {/* Tab 1: User Profile Page */}
            {activeTab === 'profile' && (
              <div className="bg-white border border-slate-200/80 rounded-3xl p-6 sm:p-8 shadow-sm space-y-6 animate-fade-in">
                <div>
                  <h2 className="text-2xl font-extrabold text-slate-900 tracking-tight">Hồ sơ người dùng</h2>
                  <p className="text-slate-500 text-sm mt-1">Cập nhật thông tin liên hệ và địa chỉ của bạn để phòng khám liên lạc</p>
                </div>
                
                <form onSubmit={handleProfileSave} className="space-y-6">
                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Họ và tên</label>
                      <div className="relative">
                        <input 
                          type="text" 
                          value={profile.fullName} 
                          onChange={(e) => setProfile({ ...profile, fullName: e.target.value })}
                          className="w-full pl-10 pr-4 py-3 bg-slate-50 border border-slate-200 rounded-xl text-sm outline-none focus:bg-white focus:border-teal-500 focus:ring-2 focus:ring-teal-500/15 transition"
                          required
                        />
                        <UserIcon className="h-4 w-4 text-slate-400 absolute left-3.5 top-3.5" />
                      </div>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Địa chỉ Email</label>
                      <div className="relative">
                        <input 
                          type="email" 
                          value={profile.email} 
                          onChange={(e) => setProfile({ ...profile, email: e.target.value })}
                          className="w-full pl-10 pr-4 py-3 bg-slate-50 border border-slate-200 rounded-xl text-sm outline-none focus:bg-white focus:border-teal-500 focus:ring-2 focus:ring-teal-500/15 transition"
                          required
                        />
                        <Mail className="h-4 w-4 text-slate-400 absolute left-3.5 top-3.5" />
                      </div>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Số điện thoại</label>
                      <div className="relative">
                        <input 
                          type="text" 
                          value={profile.phone} 
                          onChange={(e) => setProfile({ ...profile, phone: e.target.value })}
                          className="w-full pl-10 pr-4 py-3 bg-slate-50 border border-slate-200 rounded-xl text-sm outline-none focus:bg-white focus:border-teal-500 focus:ring-2 focus:ring-teal-500/15 transition"
                          required
                        />
                        <Phone className="h-4 w-4 text-slate-400 absolute left-3.5 top-3.5" />
                      </div>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Địa chỉ thường trú</label>
                      <div className="relative">
                        <input 
                          type="text" 
                          value={profile.address} 
                          onChange={(e) => setProfile({ ...profile, address: e.target.value })}
                          className="w-full pl-10 pr-4 py-3 bg-slate-50 border border-slate-200 rounded-xl text-sm outline-none focus:bg-white focus:border-teal-500 focus:ring-2 focus:ring-teal-500/15 transition"
                          required
                        />
                        <MapPin className="h-4 w-4 text-slate-400 absolute left-3.5 top-3.5" />
                      </div>
                    </div>
                  </div>

                  <div className="flex justify-end pt-4 border-t border-slate-100">
                    <button 
                      type="submit"
                      className="bg-teal-600 hover:bg-teal-700 text-white font-bold py-3 px-6 rounded-xl text-sm shadow-md hover:shadow-teal-500/10 transition duration-200"
                    >
                      Lưu thay đổi
                    </button>
                  </div>
                </form>
              </div>
            )}

            {/* Tab 2: Pet Profiles Page */}
            {activeTab === 'pets' && (
              <div className="space-y-6 animate-fade-in">
                <div>
                  <h2 className="text-2xl font-extrabold text-slate-900 tracking-tight">Hồ sơ thú cưng của tôi</h2>
                  <p className="text-slate-500 text-sm mt-1">Quản lý danh sách và kiểm tra tình trạng sức khỏe của các bé cưng nhà bạn</p>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
                  {pets.map(pet => (
                    <PetCard 
                      key={pet.id} 
                      pet={pet} 
                      onEdit={openEditPetModal} 
                      onDelete={handlePetDelete} 
                    />
                  ))}
                  
                  {/* Empty dashed border card for adding pet */}
                  <button 
                    onClick={openAddPetModal}
                    className="border-2 border-dashed border-slate-300 hover:border-teal-500 bg-white hover:bg-teal-50/10 p-6 rounded-3xl flex flex-col items-center justify-center space-y-3 min-h-[300px] transition-all duration-300 group cursor-pointer focus:outline-none"
                  >
                    <div className="h-12 w-12 rounded-full border border-slate-300 group-hover:border-teal-300 group-hover:bg-white text-slate-400 group-hover:text-teal-600 flex items-center justify-center transition">
                      <Plus className="h-6 w-6" />
                    </div>
                    <span className="text-sm font-bold text-slate-600 group-hover:text-teal-700 transition">Thêm thú cưng mới</span>
                  </button>
                </div>
              </div>
            )}

            {/* Tab 3: Medical History Page */}
            {activeTab === 'history' && (
              <div className="bg-white border border-slate-200/80 rounded-3xl p-6 sm:p-8 shadow-sm space-y-6 animate-fade-in">
                <div>
                  <h2 className="text-2xl font-extrabold text-slate-900 tracking-tight">Lịch sử khám bệnh</h2>
                  <p className="text-slate-500 text-sm mt-1">Danh sách bệnh án, lịch tái khám định kỳ và đơn thuốc điều trị của các bé</p>
                </div>

                {/* Table for Desktop, Cards/Stacked list for Mobile */}
                <div className="overflow-hidden border border-slate-100 rounded-2xl shadow-inner">
                  {/* Desktop Table View */}
                  <div className="hidden sm:block overflow-x-auto">
                    <table className="w-full text-left border-collapse">
                      <thead>
                        <tr className="bg-slate-50 text-[10px] text-slate-500 uppercase tracking-wider font-bold border-b border-slate-100">
                          <th className="py-4 px-5">Mã ca / Ngày</th>
                          <th className="py-4 px-5">Thú cưng</th>
                          <th className="py-4 px-5">Dịch vụ & Bác sĩ</th>
                          <th className="py-4 px-5 text-center">Trạng thái</th>
                          <th className="py-4 px-5 text-right">Chi tiết</th>
                        </tr>
                      </thead>
                      <tbody className="divide-y divide-slate-100 text-sm font-medium">
                        {history.map(record => (
                          <tr key={record.id} className="hover:bg-slate-50/50 transition">
                            <td className="py-4 px-5">
                              <span className="text-slate-400 block text-xs">#{record.id}</span>
                              <span className="text-slate-800 font-bold">{record.visitDate}</span>
                            </td>
                            <td className="py-4 px-5 text-slate-700 font-bold">{record.petName}</td>
                            <td className="py-4 px-5">
                              <span className="text-slate-800 block text-xs font-semibold">{record.service}</span>
                              <span className="text-slate-400 text-xs">{record.vetName}</span>
                            </td>
                            <td className="py-4 px-5 text-center">
                              <span className={`inline-block text-[10px] font-bold px-2.5 py-1 rounded-full uppercase tracking-wider ${
                                record.status === 'Completed'
                                  ? 'bg-emerald-50 text-emerald-700 border border-emerald-100'
                                  : record.status === 'Upcoming'
                                  ? 'bg-amber-50 text-amber-700 border border-amber-100'
                                  : 'bg-red-50 text-red-700 border border-red-100'
                              }`}>
                                {record.status === 'Completed' ? 'Hoàn thành' : record.status === 'Upcoming' ? 'Sắp tới' : 'Đã hủy'}
                              </span>
                            </td>
                            <td className="py-4 px-5 text-right">
                              <button 
                                onClick={() => handleViewRecordDetails(record.id, record.petName)}
                                className="inline-flex items-center space-x-1 text-teal-600 hover:text-teal-700 hover:underline transition font-bold"
                              >
                                <Eye className="h-4.5 w-4.5" />
                                <span>Xem chi tiết</span>
                              </button>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>

                  {/* Mobile Stacked List View */}
                  <div className="block sm:hidden divide-y divide-slate-100 bg-white">
                    {history.map(record => (
                      <div key={record.id} className="p-4 space-y-3">
                        <div className="flex justify-between items-center">
                          <span className="text-xs text-slate-400 font-bold">#{record.id} • {record.visitDate}</span>
                          <span className={`text-[9px] font-extrabold px-2 py-0.5 rounded-full uppercase tracking-wider ${
                            record.status === 'Completed'
                              ? 'bg-emerald-50 text-emerald-700 border border-emerald-100'
                              : record.status === 'Upcoming'
                              ? 'bg-amber-50 text-amber-700 border border-amber-100'
                              : 'bg-red-50 text-red-700 border border-red-100'
                          }`}>
                            {record.status === 'Completed' ? 'Hoàn thành' : record.status === 'Upcoming' ? 'Sắp tới' : 'Đã hủy'}
                          </span>
                        </div>
                        <div className="flex justify-between items-end">
                          <div>
                            <h4 className="font-extrabold text-slate-800 text-sm">{record.petName}</h4>
                            <p className="text-xs text-slate-500 mt-0.5">{record.service} • {record.vetName}</p>
                          </div>
                          <button 
                            onClick={() => handleViewRecordDetails(record.id, record.petName)}
                            className="flex items-center space-x-1 text-xs text-teal-600 hover:text-teal-700 font-bold transition"
                          >
                            <Eye className="h-4.5 w-4.5" />
                            <span>Xem chi tiết</span>
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>

                </div>
              </div>
            )}

          </div>
        </div>
      </div>

      {/* Add / Edit Pet Modal */}
      {petModalOpen && (
        <div className="fixed inset-0 bg-slate-900/60 z-50 flex items-center justify-center p-4 backdrop-blur-sm">
          <div className="bg-white rounded-3xl p-6 sm:p-8 max-w-md w-full shadow-2xl border border-slate-100 animate-scale-up">
            <div className="flex justify-between items-center mb-6">
              <h3 className="text-xl font-extrabold text-slate-900">
                {editingPet ? '✏️ Chỉnh sửa hồ sơ thú cưng' : '🐾 Thêm thú cưng mới'}
              </h3>
              <button 
                onClick={() => setPetModalOpen(false)}
                className="text-slate-400 hover:text-slate-600 p-1 rounded-full hover:bg-slate-50 transition"
              >
                <X className="h-5 w-5" />
              </button>
            </div>
            
            <form onSubmit={handlePetSubmit} className="space-y-4">
              {/* Pet Avatar Upload */}
              <div className="flex flex-col items-center mb-4">
                <div className="relative group">
                  <div className="h-20 w-20 rounded-full overflow-hidden bg-teal-100 border-2 border-slate-200 flex items-center justify-center text-3xl shadow-inner">
                    {petForm.avatarUrl ? (
                      <img src={petForm.avatarUrl} alt="Pet Avatar" className="h-full w-full object-cover" />
                    ) : (
                      petForm.species.toLowerCase() === 'dog' ? '🐶' : petForm.species.toLowerCase() === 'cat' ? '🐱' : '🐰'
                    )}
                  </div>
                  <button
                    type="button"
                    onClick={handlePetAvatarChange}
                    className="absolute bottom-0 right-0 p-1.5 bg-teal-600 hover:bg-teal-700 text-white rounded-full shadow transition"
                    title="Tải ảnh thú cưng"
                  >
                    <Camera className="h-3.5 w-3.5" />
                  </button>
                  <input
                    type="file"
                    ref={petAvatarFileInputRef}
                    onChange={handlePetAvatarFileChange}
                    className="hidden"
                    accept="image/*"
                  />
                </div>
                <span className="text-[11px] text-slate-400 mt-1.5">Ảnh đại diện thú cưng</span>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div className="col-span-2">
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-1.5">Tên thú cưng</label>
                  <input 
                    type="text"
                    value={petForm.name}
                    onChange={(e) => setPetForm({ ...petForm, name: e.target.value })}
                    className="w-full px-4 py-2.5 border border-slate-200 rounded-xl text-sm outline-none focus:border-teal-500 focus:ring-2 focus:ring-teal-500/10 transition"
                    placeholder="Milo, Bella..."
                    required
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-1.5">Loài vật nuôi</label>
                  <select 
                    value={petForm.species}
                    onChange={(e) => setPetForm({ ...petForm, species: e.target.value })}
                    className="w-full px-4 py-2.5 border border-slate-200 rounded-xl text-sm outline-none focus:border-teal-500 focus:ring-2 focus:ring-teal-500/10 transition"
                  >
                    <option value="Dog">Chó (Dog)</option>
                    <option value="Cat">Mèo (Cat)</option>
                    <option value="Other">Khác (Other)</option>
                  </select>
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-1.5">Giống loài</label>
                  <input 
                    type="text"
                    value={petForm.breed}
                    onChange={(e) => setPetForm({ ...petForm, breed: e.target.value })}
                    className="w-full px-4 py-2.5 border border-slate-200 rounded-xl text-sm outline-none focus:border-teal-500 focus:ring-2 focus:ring-teal-500/10 transition"
                    placeholder="Poodle, Husky..."
                    required
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-1.5">Tuổi</label>
                  <input 
                    type="text"
                    value={petForm.age}
                    onChange={(e) => setPetForm({ ...petForm, age: e.target.value })}
                    className="w-full px-4 py-2.5 border border-slate-200 rounded-xl text-sm outline-none focus:border-teal-500 focus:ring-2 focus:ring-teal-500/10 transition"
                    placeholder="2 tuổi, 6 tháng..."
                    required
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-1.5">Cân nặng (kg)</label>
                  <input 
                    type="text"
                    value={petForm.weight}
                    onChange={(e) => setPetForm({ ...petForm, weight: e.target.value })}
                    className="w-full px-4 py-2.5 border border-slate-200 rounded-xl text-sm outline-none focus:border-teal-500 focus:ring-2 focus:ring-teal-500/10 transition"
                    placeholder="5.5 kg..."
                    required
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-1.5">Giới tính</label>
                  <select 
                    value={petForm.gender}
                    onChange={(e) => setPetForm({ ...petForm, gender: e.target.value })}
                    className="w-full px-4 py-2.5 border border-slate-200 rounded-xl text-sm outline-none focus:border-teal-500 focus:ring-2 focus:ring-teal-500/10 transition"
                  >
                    <option value="Male">Đực (Male)</option>
                    <option value="Female">Cái (Female)</option>
                  </select>
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-1.5">Trạng thái sức khỏe</label>
                  <input 
                    type="text"
                    value={petForm.status}
                    onChange={(e) => setPetForm({ ...petForm, status: e.target.value })}
                    className="w-full px-4 py-2.5 border border-slate-200 rounded-xl text-sm outline-none focus:border-teal-500 focus:ring-2 focus:ring-teal-500/10 transition"
                    placeholder="Khỏe mạnh, mệt mỏi..."
                    required
                  />
                </div>
              </div>

              <div className="flex justify-end space-x-2 pt-4 border-t border-slate-100 mt-6">
                <button
                  type="button"
                  onClick={() => setPetModalOpen(false)}
                  className="px-5 py-2.5 text-sm text-slate-500 font-semibold hover:bg-slate-100 rounded-xl transition"
                >
                  Hủy
                </button>
                <button
                  type="submit"
                  className="px-6 py-2.5 text-sm bg-teal-600 hover:bg-teal-700 text-white font-bold rounded-xl transition shadow-md"
                >
                  Lưu hồ sơ
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Detailed Medical Record Modal (Viewer) */}
      {selectedRecord && (
        <div className="fixed inset-0 bg-slate-900/60 z-50 flex items-center justify-center p-4 backdrop-blur-sm overflow-y-auto">
          <div className="bg-white rounded-3xl max-w-3xl w-full shadow-2xl border border-slate-100 animate-scale-up my-8">
            <div className="flex justify-between items-center px-6 py-5 border-b border-slate-100 bg-slate-50 rounded-t-3xl">
              <div>
                <h3 className="text-lg font-extrabold text-slate-800">Chi tiết ca khám bệnh #{selectedRecord.id}</h3>
                <p className="text-slate-400 text-xs mt-0.5">Thời gian khám: {selectedRecord.visitDate}</p>
              </div>
              <button 
                onClick={() => setSelectedRecord(null)}
                className="text-slate-400 hover:text-slate-600 p-1.5 rounded-full hover:bg-white shadow-sm transition"
              >
                <X className="h-5 w-5" />
              </button>
            </div>

            <div className="p-6 space-y-6 max-h-[70vh] overflow-y-auto">
              
              {/* Pet & Doctor info bar */}
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 bg-teal-900/5 p-4 rounded-2xl border border-teal-500/10 text-sm">
                <div>
                  <span className="text-slate-400 block text-xs">Thú cưng</span>
                  <span className="font-bold text-slate-700">{selectedRecord.petName}</span>
                </div>
                <div>
                  <span className="text-slate-400 block text-xs">Bác sĩ phụ trách</span>
                  <span className="font-bold text-slate-700">{selectedRecord.vetName}</span>
                </div>
              </div>

              {/* Diagnosis & Notes */}
              <div className="space-y-4">
                <div>
                  <h4 className="text-sm font-bold text-slate-800 flex items-center gap-2">
                    <FileText className="h-4 w-4 text-teal-600" /> Kết quả chẩn đoán
                  </h4>
                  <p className="text-sm text-slate-600 mt-2 bg-slate-50 p-3 rounded-xl border border-slate-100 font-medium">
                    {selectedRecord.diagnosis}
                  </p>
                </div>

                <div>
                  <h4 className="text-sm font-bold text-slate-800">Ghi chú của bác sĩ</h4>
                  <p className="text-sm text-slate-500 mt-2 bg-slate-50 p-3 rounded-xl border border-slate-100">
                    {selectedRecord.treatmentNote}
                  </p>
                </div>

                {selectedRecord.followUpInstruction && (
                  <div>
                    <h4 className="text-sm font-bold text-slate-800">Hướng dẫn tái khám</h4>
                    <p className="text-sm text-orange-600 mt-2 bg-orange-50/30 p-3 rounded-xl border border-orange-200/40">
                      ⚠️ {selectedRecord.followUpInstruction}
                    </p>
                  </div>
                )}
              </div>

              {/* Prescription Viewer Table */}
              {selectedRecord.prescriptions.length > 0 && (
                <div className="space-y-2 border-t border-slate-100 pt-6">
                  <h4 className="text-sm font-bold text-slate-800 mb-3">💊 Chi tiết đơn thuốc</h4>
                  <PrescriptionViewer prescriptions={selectedRecord.prescriptions} />
                </div>
              )}

              {/* Lab Result File */}
              {selectedRecord.labResultFile && (
                <div className="space-y-3 border-t border-slate-100 pt-6">
                  <h4 className="text-sm font-bold text-slate-800">🔬 Kết quả xét nghiệm phòng Lab</h4>
                  <div className="flex items-center justify-between p-3.5 bg-slate-50 border border-slate-200 rounded-xl">
                    <div className="flex items-center space-x-3">
                      <div className="bg-red-50 text-red-500 p-2.5 rounded-lg border border-red-100 text-xs font-bold uppercase">
                        PDF
                      </div>
                      <div className="text-sm">
                        <p className="font-bold text-slate-700">{selectedRecord.labResultFile.name}</p>
                        <p className="text-xs text-slate-400">Định dạng file PDF kết quả</p>
                      </div>
                    </div>
                    <a 
                      href="#" 
                      onClick={(e) => { e.preventDefault(); alert('Đang tải xuống tài liệu...'); }}
                      className="bg-white hover:bg-slate-100 border border-slate-200 p-2 rounded-xl text-slate-600 hover:text-teal-600 transition shadow-sm"
                      title="Tải về"
                    >
                      <Download className="h-4.5 w-4.5" />
                    </a>
                  </div>
                </div>
              )}

              {/* Invoice Summary */}
              <div className="space-y-2 border-t border-slate-100 pt-6">
                <h4 className="text-sm font-bold text-slate-800 mb-3">💵 Chi tiết hóa đơn thanh toán</h4>
                <InvoiceSummary 
                  services={selectedRecord.services}
                  medications={selectedRecord.medications}
                  paymentStatus={selectedRecord.paymentStatus}
                />
              </div>

            </div>

            <div className="flex justify-end p-6 border-t border-slate-100 bg-slate-50/50 rounded-b-3xl">
              <button 
                onClick={() => setSelectedRecord(null)}
                className="bg-teal-600 hover:bg-teal-700 text-white font-bold py-2.5 px-6 rounded-xl text-sm transition shadow-md"
              >
                Đóng lại
              </button>
            </div>
          </div>
        </div>
      )}

    </DashboardLayout>
  );
};
export default OwnerDashboard;
