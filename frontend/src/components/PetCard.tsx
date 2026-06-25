import React from 'react';
import { Calendar, Heart, Award, Edit2, Trash2 } from 'lucide-react';

export interface Pet {
  id: number;
  name: string;
  species: string;
  breed: string;
  avatarUrl: string | null;
  age: string;
  gender: string;
  weight?: string; // Added weight field support
  lastVisitDate?: string;
  status?: string;
}

interface PetCardProps {
  pet: Pet;
  onEdit?: (pet: Pet) => void;
  onDelete?: (id: number) => void;
}

export const PetCard: React.FC<PetCardProps> = ({ pet, onEdit, onDelete }) => {
  return (
    <div className="bg-white border border-slate-200 rounded-3xl overflow-hidden hover:border-teal-200 hover:shadow-xl transition-all duration-300 group relative">
      {/* Header Info */}
      <div className="bg-gradient-to-r from-teal-50 to-orange-50/50 p-6 flex justify-center relative overflow-hidden">
        <div className="absolute top-4 right-4 bg-white/80 backdrop-blur-sm p-1.5 rounded-full border border-slate-100 group-hover:scale-110 transition">
          <Heart className="h-4 w-4 text-orange-500 fill-orange-500" />
        </div>
        
        {/* Action Buttons */}
        <div className="absolute top-4 left-4 flex gap-1.5 opacity-0 group-hover:opacity-100 transition-opacity duration-300">
          {onEdit && (
            <button 
              onClick={(e) => { e.stopPropagation(); onEdit(pet); }}
              className="bg-white/95 hover:bg-teal-50 p-1.5 rounded-lg border border-slate-200 text-slate-600 hover:text-teal-600 shadow-sm transition"
              title="Sửa"
            >
              <Edit2 className="h-3.5 w-3.5" />
            </button>
          )}
          {onDelete && (
            <button 
              onClick={(e) => { e.stopPropagation(); onDelete(pet.id); }}
              className="bg-white/95 hover:bg-red-50 p-1.5 rounded-lg border border-slate-200 text-slate-600 hover:text-red-600 shadow-sm transition"
              title="Xóa"
            >
              <Trash2 className="h-3.5 w-3.5" />
            </button>
          )}
        </div>

        <div className="h-24 w-24 rounded-full bg-teal-100 border-4 border-white flex items-center justify-center text-4xl shadow-inner group-hover:scale-105 transition duration-300">
          {pet.avatarUrl ? (
            <img src={pet.avatarUrl} alt={pet.name} className="h-full w-full rounded-full object-cover" />
          ) : (
            pet.species.toLowerCase() === 'dog' ? '🐶' : pet.species.toLowerCase() === 'cat' ? '🐱' : '🐰'
          )}
        </div>
      </div>

      <div className="p-6 space-y-4">
        <div className="text-center">
          <h3 className="text-xl font-bold text-slate-800 group-hover:text-teal-600 transition">{pet.name}</h3>
          <p className="text-slate-400 text-xs font-semibold uppercase tracking-wider mt-1">
            Giống: {pet.breed || 'Chưa rõ'} • {pet.species === 'Dog' ? 'Chó' : pet.species === 'Cat' ? 'Mèo' : pet.species}
          </p>
        </div>
        
        <div className="grid grid-cols-3 gap-2 border-t border-b border-slate-100 py-3 text-center text-sm">
          <div>
            <span className="text-slate-400 text-xs block uppercase font-medium">Tuổi</span>
            <span className="font-bold text-slate-700 mt-0.5 block">{pet.age || 'N/A'}</span>
          </div>
          <div className="border-l border-slate-100">
            <span className="text-slate-400 text-xs block uppercase font-medium">Giới tính</span>
            <span className="font-bold text-slate-700 mt-0.5 block">{pet.gender === 'Male' ? 'Đực' : pet.gender === 'Female' ? 'Cái' : 'Chưa rõ'}</span>
          </div>
          <div className="border-l border-slate-100">
            <span className="text-slate-400 text-xs block uppercase font-medium">Cân nặng</span>
            <span className="font-bold text-slate-700 mt-0.5 block">{pet.weight || 'N/A'}</span>
          </div>
        </div>

        <div className="flex justify-between items-center pt-2">
          <div className="flex items-center space-x-1.5 text-[11px] text-teal-700 font-bold bg-teal-50 py-1 px-2.5 rounded-full">
            <Calendar className="h-3.5 w-3.5 text-teal-600" />
            <span>Khám lần cuối: {pet.lastVisitDate || 'Chưa khám'}</span>
          </div>
          <div className="flex items-center space-x-1 text-xs text-slate-500 font-semibold">
            <Award className="h-3.5 w-3.5 text-orange-500" />
            <span>{pet.status || 'Khỏe mạnh'}</span>
          </div>
        </div>
      </div>
    </div>
  );
};
export default PetCard;
