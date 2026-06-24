import React from 'react';
import DashboardLayout from '../../layouts/DashboardLayout';
import { Plus, Heart, Calendar, Award } from 'lucide-react';

interface Pet {
  id: number;
  name: string;
  species: string;
  breed: string;
  avatarUrl: string | null;
  age: string;
  gender: string;
}

export const Pets: React.FC = () => {
  const [pets] = React.useState<Pet[]>([
    { id: 1, name: 'Milo', species: 'Dog', breed: 'Poodle', avatarUrl: null, age: '2 years', gender: 'Male' },
    { id: 2, name: 'Bella', species: 'Cat', breed: 'Siamese', avatarUrl: null, age: '1 year', gender: 'Female' },
  ]);

  return (
    <DashboardLayout>
      <div className="space-y-8 animate-fade-in">
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight m-0">My Pets</h1>
            <p className="text-slate-500 text-sm mt-1">Manage and view profiles of your beloved companions</p>
          </div>
          <button className="flex items-center space-x-2 bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-2 px-4 rounded-xl shadow-sm hover:shadow transition transform hover:-translate-y-0.5 active:translate-y-0">
            <Plus className="h-5 w-5" />
            <span>Add Pet</span>
          </button>
        </div>

        {/* Pet Cards Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {pets.map((pet) => (
            <div 
              key={pet.id} 
              className="bg-white border border-slate-200 rounded-2xl overflow-hidden hover:border-indigo-200 hover:shadow-lg transition-all duration-300 group cursor-pointer"
            >
              <div className="bg-gradient-to-r from-indigo-50 to-purple-50 p-6 flex justify-center relative overflow-hidden">
                <div className="absolute top-4 right-4 bg-white/80 backdrop-blur-sm p-1.5 rounded-full border border-slate-100 group-hover:scale-110 transition">
                  <Heart className="h-4 w-4 text-red-500 fill-red-500" />
                </div>
                <div className="h-24 w-24 rounded-full bg-indigo-100 border-4 border-white flex items-center justify-center text-4xl shadow-inner group-hover:scale-105 transition duration-300">
                  {pet.species === 'Dog' ? '🐶' : '🐱'}
                </div>
              </div>
              <div className="p-6 space-y-4">
                <div className="text-center">
                  <h3 className="text-xl font-bold text-slate-800 group-hover:text-indigo-600 transition">{pet.name}</h3>
                  <p className="text-slate-400 text-xs font-semibold uppercase tracking-wider mt-1">{pet.breed} • {pet.species}</p>
                </div>
                
                <div className="grid grid-cols-2 gap-4 border-t border-b border-slate-100 py-3 text-center text-sm">
                  <div>
                    <span className="text-slate-400 text-xs block uppercase">Age</span>
                    <span className="font-semibold text-slate-700 mt-0.5 block">{pet.age}</span>
                  </div>
                  <div className="border-l border-slate-100">
                    <span className="text-slate-400 text-xs block uppercase">Gender</span>
                    <span className="font-semibold text-slate-700 mt-0.5 block">{pet.gender}</span>
                  </div>
                </div>

                <div className="flex justify-between items-center pt-2">
                  <div className="flex items-center space-x-1.5 text-xs text-indigo-600 font-semibold bg-indigo-50 py-1 px-2.5 rounded-full">
                    <Calendar className="h-3.5 w-3.5" />
                    <span>Last visit: 2 days ago</span>
                  </div>
                  <div className="flex items-center space-x-1 text-xs text-slate-500">
                    <Award className="h-3.5 w-3.5 text-amber-500" />
                    <span>Healthy</span>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </DashboardLayout>
  );
};
