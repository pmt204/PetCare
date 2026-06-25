import React from 'react';
import AdminLayout from '../../layouts/AdminLayout';
import { BarChart2, Users, Calendar, Star } from 'lucide-react';

interface DoctorWorkload {
  id: number;
  name: string;
  specialty: string;
  appointmentsCount: number;
  workingHours: string;
  status: 'ACTIVE' | 'ON_LEAVE';
}

export const Reports: React.FC = () => {
  const [workloads] = React.useState<DoctorWorkload[]>([
    { id: 1, name: 'Dr. John Doe', specialty: 'General Vet', appointmentsCount: 8, workingHours: '08:00 - 17:00', status: 'ACTIVE' },
    { id: 2, name: 'Dr. Sarah Conner', specialty: 'Surgeon', appointmentsCount: 4, workingHours: '09:00 - 18:00', status: 'ACTIVE' },
    { id: 3, name: 'Dr. Helen Carter', specialty: 'Dermatologist', appointmentsCount: 0, workingHours: '08:00 - 12:00', status: 'ON_LEAVE' },
  ]);

  const totalAppointments = workloads.reduce((sum, item) => sum + item.appointmentsCount, 0);
  const activeVetsCount = workloads.filter(w => w.status === 'ACTIVE').length;

  return (
    <AdminLayout>
      <div className="space-y-8 animate-fade-in font-sans">
        <div>
          <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Clinic Workload Reports</h1>
          <p className="text-slate-500 text-sm mt-1">Review active staff performance, veterinarian workloads, and clinic throughput</p>
        </div>

        {/* Highlight widgets */}
        <div className="grid grid-cols-1 sm:grid-cols-4 gap-6">
          <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center space-x-4">
            <div className="bg-indigo-50 p-3 rounded-xl text-indigo-650 border border-indigo-150">
              <Calendar className="h-6 w-6" />
            </div>
            <div>
              <span className="text-slate-400 text-xs block uppercase">Today's Visits</span>
              <span className="text-2xl font-bold text-slate-800">{totalAppointments}</span>
            </div>
          </div>
          <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center space-x-4">
            <div className="bg-green-50 p-3 rounded-xl text-green-600 border border-green-150">
              <Users className="h-6 w-6" />
            </div>
            <div>
              <span className="text-slate-400 text-xs block uppercase">Active Doctors</span>
              <span className="text-2xl font-bold text-slate-800">{activeVetsCount}</span>
            </div>
          </div>
          <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center space-x-4">
            <div className="bg-amber-50 p-3 rounded-xl text-amber-600 border border-amber-150">
              <Star className="h-6 w-6" />
            </div>
            <div>
              <span className="text-slate-400 text-xs block uppercase">Average Satisfaction</span>
              <span className="text-2xl font-bold text-slate-800">4.9 / 5</span>
            </div>
          </div>
          <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center space-x-4">
            <div className="bg-purple-50 p-3 rounded-xl text-purple-600 border border-purple-150">
              <BarChart2 className="h-6 w-6" />
            </div>
            <div>
              <span className="text-slate-400 text-xs block uppercase">Busiest Shift</span>
              <span className="text-sm font-bold text-slate-800 mt-1 block">Morning (09:00-11:00)</span>
            </div>
          </div>
        </div>

        {/* Workload breakdown table */}
        <div className="bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-sm">
          <div className="bg-slate-50 border-b border-slate-200 px-6 py-4 flex items-center justify-between">
            <h3 className="font-bold text-slate-800 text-base">Staff Workload Breakdown</h3>
            <span className="text-xs font-semibold bg-indigo-50 text-indigo-700 py-1 px-3 rounded-full uppercase">Live Update</span>
          </div>

          <div className="overflow-x-auto text-sm">
            <table className="min-w-full divide-y divide-slate-200 text-left text-slate-700">
              <thead className="bg-slate-50 text-slate-400 uppercase text-xs font-bold tracking-wider">
                <tr>
                  <th className="px-6 py-3.5">Doctor Name</th>
                  <th className="px-6 py-3.5">Specialty</th>
                  <th className="px-6 py-3.5 text-center">Today's Appointments</th>
                  <th className="px-6 py-3.5">Shift Hours</th>
                  <th className="px-6 py-3.5">Status</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-200 bg-white">
                {workloads.map((doc) => (
                  <tr key={doc.id} className="hover:bg-slate-50/50 transition">
                    <td className="px-6 py-4 font-bold text-slate-800">{doc.name}</td>
                    <td className="px-6 py-4">
                      <span className="bg-slate-100 text-slate-700 px-2.5 py-0.5 rounded-full text-xs font-semibold">
                        {doc.specialty}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-center font-bold text-slate-700">
                      {doc.appointmentsCount} visits
                    </td>
                    <td className="px-6 py-4 text-slate-500">{doc.workingHours}</td>
                    <td className="px-6 py-4">
                      <span className={`inline-flex items-center gap-1 text-xs font-bold ${doc.status === 'ACTIVE' ? 'text-green-600' : 'text-slate-400'}`}>
                        <span className={`h-2 w-2 rounded-full ${doc.status === 'ACTIVE' ? 'bg-green-500' : 'bg-slate-350'}`} />
                        {doc.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </AdminLayout>
  );
};
export default Reports;
