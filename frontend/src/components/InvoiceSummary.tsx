import React from 'react';
import { FileText, CheckCircle, Clock } from 'lucide-react';

export interface InvoiceServiceItem {
  id?: number;
  name: string;
  price: number;
}

export interface InvoicePrescriptionItem {
  name: string;
  price: number;
}

interface InvoiceSummaryProps {
  services: InvoiceServiceItem[];
  medications: InvoicePrescriptionItem[];
  taxRate?: number;
  paymentStatus?: string;
  totalOverride?: number;
}

export const InvoiceSummary: React.FC<InvoiceSummaryProps> = ({ 
  services, 
  medications, 
  taxRate = 0.1, 
  paymentStatus = 'PENDING',
  totalOverride
}) => {
  const hasItems = services.length > 0 || medications.length > 0;
  
  let servicesTotal = services.reduce((sum, item) => sum + item.price, 0);
  const medicationsTotal = medications.reduce((sum, item) => sum + item.price, 0);
  
  let subtotal = servicesTotal + medicationsTotal;
  let tax = subtotal * taxRate;
  let total = subtotal + tax;

  if (totalOverride && totalOverride > 0) {
    total = totalOverride;
    subtotal = total / (1 + taxRate);
    tax = total - subtotal;
  }

  // If no items are provided but we have a valid total, render a default summary service
  const displayedServices = (!hasItems && total > 0) 
    ? [{ name: 'Dịch vụ khám & Điều trị y tế', price: subtotal }]
    : services;

  const isPaid = paymentStatus.toUpperCase() === 'PAID' || paymentStatus.toUpperCase() === 'UNPAID';

  return (
    <div className="bg-white border border-slate-200 rounded-2xl shadow-sm overflow-hidden">
      {/* Title / Status Header */}
      <div className="bg-slate-50 border-b border-slate-200 px-6 py-4 flex justify-between items-center">
        <h3 className="font-bold text-slate-800 text-base flex items-center gap-1.5">
          <FileText className="h-4 w-4 text-slate-400" /> Hóa đơn thanh toán ca khám
        </h3>
        <span className={`flex items-center gap-1 text-xs font-bold uppercase tracking-wider py-1 px-3 rounded-full ${isPaid ? 'bg-green-50 text-green-700 border border-green-200' : 'bg-amber-50 text-amber-700 border border-amber-200'}`}>
          {isPaid ? <CheckCircle className="h-3 w-3" /> : <Clock className="h-3 w-3" />}
          {paymentStatus === 'PAID' ? 'ĐÃ THANH TOÁN' : paymentStatus === 'UNPAID' ? 'CHƯA THANH TOÁN' : paymentStatus}
        </span>
      </div>

      <div className="p-6 space-y-6">
        {/* Itemized Lists */}
        <div className="space-y-4 text-sm">
          {/* Services */}
          {displayedServices.length > 0 && (
            <div className="space-y-2">
              <span className="text-slate-400 text-xs font-bold uppercase tracking-wider block">Dịch vụ lâm sàng</span>
              <div className="divide-y divide-slate-100 bg-slate-50/50 rounded-xl px-4 py-2 border border-slate-100">
                {displayedServices.map((item, idx) => (
                  <div key={item.id || idx} className="py-2 flex justify-between text-slate-700">
                    <span>{item.name}</span>
                    <span className="font-semibold">{Math.round(item.price).toLocaleString('vi-VN')} ₫</span>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Medications */}
          {medications.length > 0 && (
            <div className="space-y-2">
              <span className="text-slate-400 text-xs font-bold uppercase tracking-wider block">Thuốc được kê đơn</span>
              <div className="divide-y divide-slate-100 bg-slate-50/50 rounded-xl px-4 py-2 border border-slate-100">
                {medications.map((item, idx) => (
                  <div key={idx} className="py-2 flex justify-between text-slate-700">
                    <span>{item.name}</span>
                    <span className="font-semibold">{Math.round(item.price).toLocaleString('vi-VN')} ₫</span>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>

        {/* Pricing Summary */}
        <div className="border-t border-slate-150 pt-4 space-y-2.5 text-sm">
          <div className="flex justify-between text-slate-500">
            <span>Tạm tính</span>
            <span>{Math.round(subtotal).toLocaleString('vi-VN')} ₫</span>
          </div>
          <div className="flex justify-between text-slate-500">
            <span>Thuế VAT ({taxRate * 100}%)</span>
            <span>{Math.round(tax).toLocaleString('vi-VN')} ₫</span>
          </div>
          <div className="flex justify-between text-slate-900 font-extrabold text-base pt-2.5 border-t border-slate-100">
            <span>Tổng thanh toán</span>
            <span className="text-indigo-600 flex items-center font-extrabold">{Math.round(total).toLocaleString('vi-VN')} ₫</span>
          </div>
        </div>
      </div>
    </div>
  );
};
export default InvoiceSummary;
