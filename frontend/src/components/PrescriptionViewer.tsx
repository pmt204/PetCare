import React from 'react';
import { Pill } from 'lucide-react';

export interface PrescriptionItem {
  id?: number;
  medicationName: string;
  dosage: string;
  frequency: string;
  durationDays: number;
  instructions?: string;
}

interface PrescriptionViewerProps {
  prescriptions: PrescriptionItem[];
}

export const PrescriptionViewer: React.FC<PrescriptionViewerProps> = ({ prescriptions }) => {
  if (prescriptions.length === 0) {
    return (
      <div className="text-center py-6 bg-slate-50 rounded-xl border border-dashed border-slate-200">
        <p className="text-slate-400 text-sm">No medications prescribed for this visit.</p>
      </div>
    );
  }

  return (
    <div className="border border-slate-200 rounded-xl overflow-hidden bg-white shadow-sm">
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-slate-200 text-left text-sm text-slate-700">
          <thead className="bg-slate-50 text-slate-500 uppercase text-xs font-bold tracking-wider">
            <tr>
              <th scope="col" className="px-6 py-3.5 flex items-center gap-1.5">
                <Pill className="h-3.5 w-3.5 text-indigo-600" /> Drug Name
              </th>
              <th scope="col" className="px-6 py-3.5">Dosage</th>
              <th scope="col" className="px-6 py-3.5">Frequency</th>
              <th scope="col" className="px-6 py-3.5">Duration</th>
              <th scope="col" className="px-6 py-3.5">Instructions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-200 divide-dashed">
            {prescriptions.map((p, index) => (
              <tr key={p.id || index} className="hover:bg-slate-55 transition-colors">
                <td className="px-6 py-4 font-bold text-slate-800">{p.medicationName}</td>
                <td className="px-6 py-4 font-medium">{p.dosage}</td>
                <td className="px-6 py-4 text-slate-600">{p.frequency}</td>
                <td className="px-6 py-4">
                  <span className="bg-indigo-50 text-indigo-600 px-2.5 py-0.5 rounded-full text-xs font-semibold">
                    {p.durationDays} days
                  </span>
                </td>
                <td className="px-6 py-4 text-slate-500 max-w-xs truncate">{p.instructions || 'N/A'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
export default PrescriptionViewer;
