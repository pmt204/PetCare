import React from 'react';
import DashboardLayout from '../../layouts/DashboardLayout';
import { Calendar, FileText, Activity } from 'lucide-react';

interface HistoryItem {
  id: number;
  petName: string;
  vetName: string;
  visitDate: string;
  diagnosis: string;
  treatment: string;
  status: string;
}

export const MedicalHistory: React.FC = () => {
  const [history] = React.useState<HistoryItem[]>([
    { id: 1, petName: 'Milo', vetName: 'Dr. John Doe', visitDate: '2026-06-22', diagnosis: 'Mild cough', treatment: 'Give medicine for 5 days', status: 'Completed' },
    { id: 2, petName: 'Bella', vetName: 'Dr. Sarah Conner', visitDate: '2026-05-14', diagnosis: 'Routine checkup', treatment: 'N/A', status: 'Completed' },
  ]);

  return (
    <DashboardLayout>
      <div className="space-y-8 animate-fade-in font-sans">
        <div>
          <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Medical History</h1>
          <p className="text-slate-500 text-sm mt-1">Timeline of past diagnoses, prescriptions and laboratory results</p>
        </div>

        {/* Timeline */}
        <div className="relative border-l border-slate-200 ml-4 md:ml-6 space-y-8 py-2">
          {history.map((item) => (
            <div key={item.id} className="relative pl-8 md:pl-10 group">
              {/* Dot */}
              <div className="absolute -left-[17px] top-1 bg-white border-4 border-indigo-600 h-8 w-8 rounded-full flex items-center justify-center text-xs shadow transition group-hover:scale-110 group-hover:border-purple-600 duration-300">
                🐾
              </div>

              {/* Card */}
              <div className="bg-white border border-slate-200 hover:border-indigo-200 p-6 rounded-2xl shadow-sm hover:shadow transition duration-300">
                <div className="flex flex-col md:flex-row md:justify-between md:items-center gap-4 border-b border-slate-100 pb-4 mb-4">
                  <div>
                    <span className="text-xs text-slate-400 font-semibold block uppercase">Visit Date</span>
                    <span className="text-sm font-bold text-slate-700 flex items-center gap-1.5 mt-0.5">
                      <Calendar className="h-4 w-4 text-slate-400" /> {item.visitDate}
                    </span>
                  </div>
                  <div className="flex items-center space-x-3">
                    <span className="text-xs font-semibold bg-indigo-50 text-indigo-600 py-1 px-3 rounded-full uppercase tracking-wider">{item.petName}</span>
                    <span className="text-xs font-semibold bg-slate-100 text-slate-700 py-1 px-3 rounded-full uppercase tracking-wider">{item.vetName}</span>
                  </div>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div className="space-y-1">
                    <span className="text-xs text-slate-400 font-semibold uppercase flex items-center gap-1"><Activity className="h-3.5 w-3.5" /> Diagnosis</span>
                    <p className="text-sm text-slate-800 font-medium">{item.diagnosis}</p>
                  </div>
                  <div className="space-y-1">
                    <span className="text-xs text-slate-400 font-semibold uppercase flex items-center gap-1"><FileText className="h-3.5 w-3.5" /> Treatment Note</span>
                    <p className="text-sm text-slate-600 font-medium">{item.treatment}</p>
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
