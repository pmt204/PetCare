import React from 'react';
import DashboardLayout from '../../layouts/DashboardLayout';
import { Plus, User, Award, Shield } from 'lucide-react';

interface Vet {
  id: number;
  name: string;
  specialty: string;
  experienceYears: string;
  rating: number;
  active: boolean;
}

export const Vets: React.FC = () => {
  const [vets] = React.useState<Vet[]>([
    { id: 1, name: 'Dr. John Doe', specialty: 'General Vet', experienceYears: '5 years', rating: 4.8, active: true },
    { id: 2, name: 'Dr. Sarah Conner', specialty: 'Surgeon', experienceYears: '8 years', rating: 4.9, active: true },
  ]);

  return (
    <DashboardLayout>
      <div className="space-y-8 animate-fade-in font-sans">
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Clinic Veterinarians</h1>
            <p className="text-slate-500 text-sm mt-1">Manage veterinary accounts, specializations and profiles</p>
          </div>
          <button className="flex items-center space-x-2 bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-2 px-4 rounded-xl shadow-sm transition">
            <Plus className="h-5 w-5" />
            <span>Add Vet</span>
          </button>
        </div>

        {/* Vet Card Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {vets.map((vet) => (
            <div key={vet.id} className="bg-white border border-slate-200 rounded-2xl p-6 hover:shadow-lg hover:border-indigo-200 transition-all duration-300">
              <div className="flex items-center space-x-4">
                <div className="h-14 w-14 rounded-full bg-slate-100 flex items-center justify-center text-slate-600 border border-slate-200">
                  <User className="h-6 w-6" />
                </div>
                <div>
                  <h3 className="text-lg font-bold text-slate-800">{vet.name}</h3>
                  <span className="text-xs font-semibold text-indigo-600 bg-indigo-50 py-0.5 px-2.5 rounded-full">{vet.specialty}</span>
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4 border-t border-slate-100 mt-6 pt-4 text-sm text-center">
                <div>
                  <span className="text-slate-400 text-xs block uppercase">Experience</span>
                  <span className="font-semibold text-slate-700 mt-0.5 block">{vet.experienceYears}</span>
                </div>
                <div className="border-l border-slate-100">
                  <span className="text-slate-400 text-xs block uppercase">Rating</span>
                  <span className="font-bold text-indigo-600 mt-0.5 block">⭐ {vet.rating}</span>
                </div>
              </div>

              <div className="flex justify-between items-center mt-6 pt-4 border-t border-slate-100 text-xs">
                <div className="flex items-center space-x-1.5 text-slate-500">
                  <Shield className="h-3.5 w-3.5 text-green-500" />
                  <span>Account Active</span>
                </div>
                <div className="flex items-center space-x-1 text-slate-500">
                  <Award className="h-3.5 w-3.5 text-amber-500" />
                  <span>Staff</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </DashboardLayout>
  );
};
