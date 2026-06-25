import React from 'react';
import { Calendar, FileText, Activity, User, ChevronRight } from 'lucide-react';

export interface MedicalRecord {
  id: number;
  petId?: number;
  petName: string;
  vetName: string;
  visitDate: string;
  diagnosis: string;
  treatmentNote: string;
  status: string;
}

interface MedicalRecordTimelineProps {
  records: MedicalRecord[];
  onViewDetails?: (id: number) => void;
}

export const MedicalRecordTimeline: React.FC<MedicalRecordTimelineProps> = ({ records, onViewDetails }) => {
  // Sort records descending by visitDate
  const sortedRecords = [...records].sort((a, b) => new Date(b.visitDate).getTime() - new Date(a.visitDate).getTime());

  if (sortedRecords.length === 0) {
    return (
      <div className="text-center py-12 bg-white rounded-2xl border border-slate-200 p-8">
        <span className="text-4xl">📭</span>
        <h3 className="text-lg font-bold text-slate-700 mt-4">No Medical Records</h3>
        <p className="text-slate-400 text-sm mt-1">This pet has no medical history registered yet.</p>
      </div>
    );
  }

  return (
    <div className="relative border-l border-slate-200 ml-4 md:ml-6 space-y-8 py-2">
      {sortedRecords.map((item) => (
        <div key={item.id} className="relative pl-8 md:pl-10 group">
          {/* Timeline Dot */}
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
              <div className="flex items-center space-x-3 justify-between md:justify-end">
                <div className="flex gap-2">
                  <span className="text-xs font-semibold bg-indigo-50 text-indigo-600 py-1 px-3 rounded-full uppercase tracking-wider">{item.petName}</span>
                  <span className="text-xs font-semibold bg-slate-100 text-slate-700 py-1 px-3 rounded-full uppercase tracking-wider flex items-center gap-1">
                    <User className="h-3 w-3" /> {item.vetName}
                  </span>
                </div>
                {onViewDetails && (
                  <button 
                    onClick={() => onViewDetails(item.id)}
                    className="flex items-center space-x-1 text-xs text-indigo-600 font-semibold hover:text-indigo-800 transition"
                  >
                    <span>Details</span>
                    <ChevronRight className="h-4 w-4" />
                  </button>
                )}
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="space-y-1">
                <span className="text-xs text-slate-400 font-semibold uppercase flex items-center gap-1">
                  <Activity className="h-3.5 w-3.5" /> Diagnosis
                </span>
                <p className="text-sm text-slate-800 font-bold">{item.diagnosis}</p>
              </div>
              <div className="space-y-1">
                <span className="text-xs text-slate-400 font-semibold uppercase flex items-center gap-1">
                  <FileText className="h-3.5 w-3.5" /> Treatment Note
                </span>
                <p className="text-sm text-slate-600 font-medium">{item.treatmentNote || 'No treatment instructions provided.'}</p>
              </div>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};
export default MedicalRecordTimeline;
