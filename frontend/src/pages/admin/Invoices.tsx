import React from 'react';
import AdminLayout from '../../layouts/AdminLayout';
import { FileText, DollarSign, Calendar, Clock } from 'lucide-react';
import InvoiceSummary from '../../components/InvoiceSummary';
import type { InvoiceServiceItem, InvoicePrescriptionItem } from '../../components/InvoiceSummary';

interface LocalInvoice {
  id: number;
  petName: string;
  ownerName: string;
  totalAmount: number;
  paymentStatus: 'PAID' | 'PENDING';
  createdAt: string;
  services: InvoiceServiceItem[];
  medications: InvoicePrescriptionItem[];
}

export const Invoices: React.FC = () => {
  const [invoices, setInvoices] = React.useState<LocalInvoice[]>([
    {
      id: 101,
      petName: 'Milo',
      ownerName: 'Alice',
      totalAmount: 95.00,
      paymentStatus: 'PAID',
      createdAt: '2026-06-22 09:45',
      services: [
        { name: 'General Physical Consultation', price: 45.00 },
        { name: 'Throat Swab / Culture', price: 20.00 }
      ],
      medications: [
        { name: 'Cough Syrup PetPlus', price: 15.00 },
        { name: 'Amoxicillin Vet', price: 15.00 }
      ]
    },
    {
      id: 102,
      petName: 'Bella',
      ownerName: 'Bob',
      totalAmount: 50.00,
      paymentStatus: 'PENDING',
      createdAt: '2026-06-25 10:30',
      services: [
        { name: 'Routine Vaccination Consultation', price: 35.00 }
      ],
      medications: [
        { name: 'Flea Prevention Pipette', price: 15.00 }
      ]
    }
  ]);

  const [selectedInvoice, setSelectedInvoice] = React.useState<LocalInvoice | null>(null);

  const handleMarkAsPaid = (id: number) => {
    setInvoices(prev => 
      prev.map(inv => inv.id === id ? { ...inv, paymentStatus: 'PAID' } : inv)
    );
    if (selectedInvoice && selectedInvoice.id === id) {
      setSelectedInvoice({ ...selectedInvoice, paymentStatus: 'PAID' });
    }
    alert('Invoice marked as Paid.');
  };

  const totalSales = invoices
    .filter(inv => inv.paymentStatus === 'PAID')
    .reduce((sum, inv) => sum + inv.totalAmount, 0);

  const pendingSales = invoices
    .filter(inv => inv.paymentStatus === 'PENDING')
    .reduce((sum, inv) => sum + inv.totalAmount, 0);

  return (
    <AdminLayout>
      <div className="space-y-8 animate-fade-in font-sans">
        <div>
          <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Clinic Invoices</h1>
          <p className="text-slate-500 text-sm mt-1">Manage invoice statuses, billing reports, and collect payments</p>
        </div>

        {/* Stats Summary Widgets */}
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
          <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center space-x-4">
            <div className="bg-green-50 p-3 rounded-xl text-green-600 border border-green-150">
              <DollarSign className="h-6 w-6" />
            </div>
            <div>
              <span className="text-slate-400 text-xs block uppercase">Collected Earnings</span>
              <span className="text-2xl font-bold text-slate-800">${totalSales.toFixed(2)}</span>
            </div>
          </div>
          <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center space-x-4">
            <div className="bg-amber-50 p-3 rounded-xl text-amber-600 border border-amber-150">
              <Clock className="h-6 w-6" />
            </div>
            <div>
              <span className="text-slate-400 text-xs block uppercase">Pending Receivables</span>
              <span className="text-2xl font-bold text-slate-800">${pendingSales.toFixed(2)}</span>
            </div>
          </div>
          <div className="bg-white border border-slate-200 p-6 rounded-2xl shadow-sm flex items-center space-x-4">
            <div className="bg-indigo-50 p-3 rounded-xl text-indigo-650 border border-indigo-150">
              <FileText className="h-6 w-6" />
            </div>
            <div>
              <span className="text-slate-400 text-xs block uppercase">Total Invoices</span>
              <span className="text-2xl font-bold text-slate-800">{invoices.length}</span>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Invoice List (Colspan 2) */}
          <div className="lg:col-span-2 bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-sm">
            <div className="bg-slate-50 border-b border-slate-200 px-6 py-4">
              <h3 className="font-bold text-slate-800 text-base">All Invoices</h3>
            </div>
            <div className="divide-y divide-slate-100 text-sm">
              {invoices.map((inv) => (
                <div 
                  key={inv.id}
                  onClick={() => setSelectedInvoice(inv)}
                  className={`p-6 flex justify-between items-center cursor-pointer transition hover:bg-slate-50/50 ${selectedInvoice?.id === inv.id ? 'bg-indigo-50/30' : ''}`}
                >
                  <div className="space-y-1">
                    <div className="flex items-center space-x-2">
                      <span className="font-bold text-slate-800">Invoice #{inv.id}</span>
                      <span className={`text-[10px] font-bold px-2 py-0.5 rounded-full ${inv.paymentStatus === 'PAID' ? 'bg-green-50 text-green-700' : 'bg-amber-50 text-amber-700'}`}>
                        {inv.paymentStatus}
                      </span>
                    </div>
                    <p className="text-xs text-slate-500 flex items-center gap-1"><Calendar className="h-3 w-3" /> {inv.createdAt}</p>
                    <p className="text-slate-700 font-medium">Owner: {inv.ownerName} • Pet: {inv.petName}</p>
                  </div>
                  <div className="text-right space-y-1.5">
                    <span className="text-base font-extrabold text-slate-900">${inv.totalAmount.toFixed(2)}</span>
                    <div onClick={(e) => e.stopPropagation()} className="block">
                      {inv.paymentStatus === 'PENDING' && (
                        <button
                          onClick={() => handleMarkAsPaid(inv.id)}
                          className="text-[11px] font-bold text-indigo-650 bg-indigo-50 border border-indigo-150 hover:bg-indigo-100 rounded-lg px-2.5 py-1 transition"
                        >
                          Mark Paid
                        </button>
                      )}
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Details Sidebar / Summary View */}
          <div className="space-y-4">
            <h3 className="font-bold text-slate-850 text-base">Invoice Details</h3>
            {selectedInvoice ? (
              <div className="space-y-4 animate-fade-in">
                <InvoiceSummary 
                  services={selectedInvoice.services}
                  medications={selectedInvoice.medications}
                  paymentStatus={selectedInvoice.paymentStatus}
                />
                {selectedInvoice.paymentStatus === 'PENDING' && (
                  <button
                    onClick={() => handleMarkAsPaid(selectedInvoice.id)}
                    className="w-full py-2.5 bg-teal-600 hover:bg-teal-700 text-white font-bold rounded-xl shadow transition"
                  >
                    Xác nhận Thanh toán
                  </button>
                )}
              </div>
            ) : (
              <div className="border border-dashed border-slate-200 bg-slate-50 rounded-2xl p-8 text-center text-slate-400 text-sm">
                Click on any invoice in the list to view its complete summary break-down.
              </div>
            )}
          </div>
        </div>
      </div>
    </AdminLayout>
  );
};
export default Invoices;
