import React from 'react';
import { useNavigate } from 'react-router-dom';
import DashboardLayout from '../../layouts/DashboardLayout';
import { Clock, FileText, CheckCircle, PlusCircle } from 'lucide-react';

interface Appointment {
  id: number;
  patientName: string;
  petName: string;
  petSpecies: string;
  time: string;
  reason: string;
  status: string;
}

export const Schedule: React.FC = () => {
  const navigate = useNavigate();
  const [appointments, setAppointments] = React.useState<Appointment[]>([
    { id: 1, patientName: 'Alice', petName: 'Milo', petSpecies: 'Dog', time: '09:00 AM', reason: 'Mild cough', status: 'Pending' },
    { id: 2, patientName: 'Bob', petName: 'Bella', petSpecies: 'Cat', time: '10:30 AM', reason: 'Routine vaccination', status: 'Confirmed' },
  ]);

  const handleComplete = (id: number) => {
    setAppointments((prev) => 
      prev.map((app) => app.id === id ? { ...app, status: 'Completed' } : app)
    );
    navigate(`/vet/records?appointmentId=${id}`);
  };

  return (
    <DashboardLayout>
      <div className="space-y-8 animate-fade-in font-sans">
        <div className="flex justify-between items-center flex-wrap gap-4">
          <div>
            <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Doctor Schedule</h1>
            <p className="text-slate-500 text-sm mt-1">View and manage today's appointments and patients</p>
          </div>
          <button 
            onClick={() => navigate('/vet/records')}
            className="flex items-center space-x-2 bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-2 px-4 rounded-xl shadow transition"
          >
            <PlusCircle className="h-5 w-5" />
            <span>Create Record</span>
          </button>
        </div>

        <div className="bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-sm">
          <div className="bg-slate-50 border-b border-slate-200 px-6 py-4">
            <h3 className="font-bold text-slate-800 text-base">Appointments for Today</h3>
          </div>
          
          <div className="divide-y divide-slate-100">
            {appointments.map((app) => (
              <div key={app.id} className="p-6 flex flex-col md:flex-row md:justify-between md:items-center gap-4 hover:bg-slate-50/50 transition">
                <div className="space-y-3">
                  <div className="flex items-center space-x-3">
                    <span className="text-sm font-semibold text-slate-700 flex items-center gap-1">
                      <Clock className="h-4 w-4 text-slate-400" /> {app.time}
                    </span>
                    <span className={`text-xs font-bold uppercase tracking-wider py-0.5 px-2.5 rounded-full ${app.status === 'Completed' ? 'bg-green-50 text-green-700' : 'bg-amber-50 text-amber-700'}`}>
                      {app.status}
                    </span>
                  </div>

                  <div className="text-sm">
                    <p className="text-slate-800 font-bold">Owner: {app.patientName} • Pet: {app.petName} ({app.petSpecies})</p>
                    <p className="text-slate-500 flex items-center gap-1.5 mt-1 text-xs">
                      <FileText className="h-3.5 w-3.5" /> Reason: {app.reason}
                    </p>
                  </div>
                </div>

                {app.status !== 'Completed' && (
                  <div>
                    <button 
                      onClick={() => handleComplete(app.id)}
                      className="flex items-center space-x-2 bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-1.5 px-4 rounded-lg text-sm shadow-sm transition"
                    >
                      <CheckCircle className="h-4 w-4" />
                      <span>Complete & Diagnosing</span>
                    </button>
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};
export default Schedule;
