import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import DashboardLayout from '../../layouts/DashboardLayout';
import { ArrowLeft, Calendar, User, Activity, FileText, Download } from 'lucide-react';
import PrescriptionViewer from '../../components/PrescriptionViewer';
import type { PrescriptionItem } from '../../components/PrescriptionViewer';
import InvoiceSummary from '../../components/InvoiceSummary';
import type { InvoiceServiceItem, InvoicePrescriptionItem } from '../../components/InvoiceSummary';

interface DetailedMedicalRecord {
  id: number;
  petName: string;
  vetName: string;
  visitDate: string;
  diagnosis: string;
  treatmentNote: string;
  followUpInstruction?: string;
  status: string;
  prescriptions: PrescriptionItem[];
  services: InvoiceServiceItem[];
  labResultFile?: { name: string; url: string } | null;
  paymentStatus: string;
}

export const MedicalRecordDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [record, setRecord] = React.useState<DetailedMedicalRecord | null>(null);
  const [loading, setLoading] = React.useState(true);

  React.useEffect(() => {
    // Simulated fetching details based on ID
    // In final phase, we can integrate it with a real API
    setTimeout(() => {
      setRecord({
        id: Number(id),
        petName: 'Milo',
        vetName: 'Dr. John Doe',
        visitDate: '2026-06-22',
        diagnosis: 'Mild cough and minor throat irritation',
        treatmentNote: 'Give prescribed cough syrup twice a day after meals. Keep pet hydrated.',
        followUpInstruction: 'Return in 5 days if cough persists.',
        status: 'Completed',
        prescriptions: [
          { medicationName: 'Cough Syrup PetPlus', dosage: '5ml', frequency: '2 times / day', durationDays: 5, instructions: 'After meals' },
          { medicationName: 'Amoxicillin Vet', dosage: '250mg', frequency: '1 tablet / day', durationDays: 7, instructions: 'Morning' }
        ],
        services: [
          { name: 'General Physical Consultation', price: 45.00 },
          { name: 'Throat Swab / Culture', price: 20.00 }
        ],
        labResultFile: { name: 'throat_swab_milo_20260622.pdf', url: '#' },
        paymentStatus: 'PAID'
      });
      setLoading(false);
    }, 400);
  }, [id]);

  if (loading) {
    return (
      <DashboardLayout>
        <div className="flex h-64 items-center justify-center text-indigo-600 font-semibold animate-pulse">
          Loading visit details...
        </div>
      </DashboardLayout>
    );
  }

  if (!record) {
    return (
      <DashboardLayout>
        <div className="text-center py-12">
          <p className="text-slate-500 font-bold">Medical record not found.</p>
          <button onClick={() => navigate(-1)} className="mt-4 text-indigo-600 font-semibold hover:underline">Go back</button>
        </div>
      </DashboardLayout>
    );
  }

  const invoiceMedications: InvoicePrescriptionItem[] = record.prescriptions.map(p => ({
    name: p.medicationName,
    price: 15.00 // Mock price per medication
  }));

  return (
    <DashboardLayout>
      <div className="space-y-8 animate-fade-in font-sans">
        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div className="flex items-center space-x-4">
            <button 
              onClick={() => navigate(-1)} 
              className="p-2 border border-slate-200 rounded-xl bg-white hover:bg-slate-50 text-slate-600 transition"
              title="Back"
            >
              <ArrowLeft className="h-5 w-5" />
            </button>
            <div>
              <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Record Details</h1>
              <p className="text-slate-500 text-sm mt-1">Visit ID: #{record.id} • Pet: {record.petName}</p>
            </div>
          </div>
          <span className="self-start sm:self-center bg-indigo-50 text-indigo-700 border border-indigo-150 py-1.5 px-4 rounded-xl text-xs font-bold uppercase tracking-wider">
            {record.status}
          </span>
        </div>

        {/* Info Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          
          {/* Main Details (Colspan 2) */}
          <div className="lg:col-span-2 space-y-6">
            
            {/* Summary Metadata */}
            <div className="bg-white border border-slate-200 rounded-2xl p-6 shadow-sm grid grid-cols-2 sm:grid-cols-4 gap-6 text-sm">
              <div>
                <span className="text-slate-400 text-xs block uppercase">Date</span>
                <span className="font-bold text-slate-700 flex items-center gap-1.5 mt-1">
                  <Calendar className="h-4 w-4 text-slate-400" /> {record.visitDate}
                </span>
              </div>
              <div className="border-l border-slate-100 pl-4">
                <span className="text-slate-400 text-xs block uppercase">Veterinarian</span>
                <span className="font-bold text-slate-700 flex items-center gap-1.5 mt-1">
                  <User className="h-4 w-4 text-slate-400" /> {record.vetName}
                </span>
              </div>
              <div className="border-l border-slate-100 pl-4">
                <span className="text-slate-400 text-xs block uppercase">Pet Name</span>
                <span className="font-semibold text-slate-700 mt-1 block">{record.petName}</span>
              </div>
              <div className="border-l border-slate-100 pl-4">
                <span className="text-slate-400 text-xs block uppercase">Lab Results</span>
                <span className="font-semibold text-indigo-600 mt-1 block">
                  {record.labResultFile ? '1 PDF file' : 'None'}
                </span>
              </div>
            </div>

            {/* Diagnosis & Notes */}
            <div className="bg-white border border-slate-200 rounded-2xl p-6 shadow-sm space-y-6">
              <div className="space-y-2">
                <h3 className="font-bold text-slate-800 text-base flex items-center gap-2">
                  <Activity className="h-4 w-4 text-indigo-600" /> Diagnosis Summary
                </h3>
                <p className="text-sm text-slate-700 leading-relaxed font-medium bg-indigo-50/20 p-4 rounded-xl border border-indigo-50/50">
                  {record.diagnosis}
                </p>
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-6 pt-4 border-t border-slate-100">
                <div className="space-y-2">
                  <h4 className="text-sm font-bold text-slate-800 flex items-center gap-1.5">
                    <FileText className="h-4 w-4 text-slate-400" /> Treatment instructions
                  </h4>
                  <p className="text-sm text-slate-600 leading-relaxed">{record.treatmentNote}</p>
                </div>
                {record.followUpInstruction && (
                  <div className="space-y-2">
                    <h4 className="text-sm font-bold text-slate-800 flex items-center gap-1.5">
                      🔄 Follow-up Plan
                    </h4>
                    <p className="text-sm text-slate-600 leading-relaxed">{record.followUpInstruction}</p>
                  </div>
                )}
              </div>
            </div>

            {/* Prescription Viewer Section */}
            <div className="space-y-3">
              <h3 className="font-bold text-slate-850 text-base">Prescribed Medication</h3>
              <PrescriptionViewer prescriptions={record.prescriptions} />
            </div>

            {/* Lab results file row */}
            {record.labResultFile && (
              <div className="flex items-center justify-between p-4 bg-white border border-slate-200 rounded-2xl shadow-sm">
                <div className="flex items-center space-x-3">
                  <div className="bg-red-50 p-2.5 rounded-xl text-red-500 border border-red-100">
                    <FileText className="h-5 w-5" />
                  </div>
                  <div>
                    <p className="text-sm font-bold text-slate-850">{record.labResultFile.name}</p>
                    <p className="text-xs text-slate-400">PDF Document</p>
                  </div>
                </div>
                <a 
                  href={record.labResultFile.url}
                  className="flex items-center space-x-1.5 bg-slate-100 hover:bg-slate-200 text-slate-700 font-semibold py-1.5 px-4 rounded-xl text-xs transition"
                >
                  <Download className="h-4.5 w-4.5" />
                  <span>Download</span>
                </a>
              </div>
            )}

          </div>

          {/* Invoice Summary (Colspan 1) */}
          <div className="space-y-4">
            <h3 className="font-bold text-slate-850 text-base">Billing Summary</h3>
            <InvoiceSummary 
              services={record.services} 
              medications={invoiceMedications} 
              paymentStatus={record.paymentStatus} 
            />
          </div>

        </div>
      </div>
    </DashboardLayout>
  );
};
export default MedicalRecordDetails;
