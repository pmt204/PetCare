import React from 'react';
import DashboardLayout from '../../layouts/DashboardLayout';
import { Calendar, User, Clock, FileText } from 'lucide-react';

export const BookAppointment: React.FC = () => {
  const [step, setStep] = React.useState(1);
  const [formData, setFormData] = React.useState({
    petId: '',
    vetId: '',
    date: '',
    time: '',
    reason: '',
  });

  const nextStep = () => setStep((prev) => prev + 1);
  const prevStep = () => setStep((prev) => prev - 1);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    alert('Appointment booked successfully!');
    setStep(1);
    setFormData({ petId: '', vetId: '', date: '', time: '', reason: '' });
  };

  return (
    <DashboardLayout>
      <div className="max-w-3xl mx-auto space-y-8 animate-fade-in font-sans">
        <div>
          <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight">Book an Appointment</h1>
          <p className="text-slate-500 text-sm mt-1">Schedule a visit with one of our experienced veterinarians in 3 simple steps</p>
        </div>

        {/* Progress Bar */}
        <div className="flex items-center justify-between relative py-2">
          <div className="absolute left-0 right-0 top-1/2 h-0.5 bg-slate-200 -z-10" />
          <div className={`absolute left-0 top-1/2 h-0.5 bg-indigo-600 transition-all duration-500 -z-10`} style={{ width: `${(step - 1) * 50}%` }} />
          
          <div className="flex flex-col items-center">
            <div className={`h-10 w-10 rounded-full flex items-center justify-center font-bold text-sm border-2 transition ${step >= 1 ? 'bg-indigo-600 border-indigo-600 text-white shadow' : 'bg-white border-slate-300 text-slate-500'}`}>1</div>
            <span className="text-xs font-semibold mt-2 text-slate-600">Select Pet & Vet</span>
          </div>
          <div className="flex flex-col items-center">
            <div className={`h-10 w-10 rounded-full flex items-center justify-center font-bold text-sm border-2 transition ${step >= 2 ? 'bg-indigo-600 border-indigo-600 text-white shadow' : 'bg-white border-slate-300 text-slate-500'}`}>2</div>
            <span className="text-xs font-semibold mt-2 text-slate-600">Choose Date & Time</span>
          </div>
          <div className="flex flex-col items-center">
            <div className={`h-10 w-10 rounded-full flex items-center justify-center font-bold text-sm border-2 transition ${step >= 3 ? 'bg-indigo-600 border-indigo-600 text-white shadow' : 'bg-white border-slate-300 text-slate-500'}`}>3</div>
            <span className="text-xs font-semibold mt-2 text-slate-600">Confirm Booking</span>
          </div>
        </div>

        {/* Form Container */}
        <div className="bg-white border border-slate-200 rounded-2xl p-8 shadow-sm">
          <form onSubmit={handleSubmit} className="space-y-6">
            
            {/* Step 1: Select Pet & Vet */}
            {step === 1 && (
              <div className="space-y-6 animate-slide-in">
                <div>
                  <label className="block text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                    <Clock className="h-4 w-4 text-slate-400" /> Select Pet
                  </label>
                  <select 
                    value={formData.petId}
                    onChange={(e) => setFormData({ ...formData, petId: e.target.value })}
                    className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm outline-none focus:ring-2 focus:ring-indigo-500"
                    required
                  >
                    <option value="">Select your pet</option>
                    <option value="1">Milo (Poodle)</option>
                    <option value="2">Bella (Siamese)</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                    <User className="h-4 w-4 text-slate-400" /> Select Veterinarian
                  </label>
                  <select 
                    value={formData.vetId}
                    onChange={(e) => setFormData({ ...formData, vetId: e.target.value })}
                    className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm outline-none focus:ring-2 focus:ring-indigo-500"
                    required
                  >
                    <option value="">Select veterinarian</option>
                    <option value="1">Dr. John Doe (General Vet)</option>
                    <option value="2">Dr. Sarah Conner (Surgeon)</option>
                  </select>
                </div>
                <div className="flex justify-end pt-4">
                  <button 
                    type="button" 
                    onClick={nextStep}
                    disabled={!formData.petId || !formData.vetId}
                    className="bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-2 px-6 rounded-lg text-sm shadow-sm transition disabled:opacity-50"
                  >
                    Next Step
                  </button>
                </div>
              </div>
            )}

            {/* Step 2: Choose Date & Time */}
            {step === 2 && (
              <div className="space-y-6 animate-slide-in">
                <div>
                  <label className="block text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                    <Calendar className="h-4 w-4 text-slate-400" /> Appointment Date
                  </label>
                  <input 
                    type="date"
                    value={formData.date}
                    onChange={(e) => setFormData({ ...formData, date: e.target.value })}
                    className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm outline-none focus:ring-2 focus:ring-indigo-500"
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                    <Clock className="h-4 w-4 text-slate-400" /> Preferred Time
                  </label>
                  <select 
                    value={formData.time}
                    onChange={(e) => setFormData({ ...formData, time: e.target.value })}
                    className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm outline-none focus:ring-2 focus:ring-indigo-500"
                    required
                  >
                    <option value="">Select time slot</option>
                    <option value="09:00">09:00 AM</option>
                    <option value="10:30">10:30 AM</option>
                    <option value="13:30">01:30 PM</option>
                    <option value="15:00">03:00 PM</option>
                  </select>
                </div>
                <div className="flex justify-between pt-4">
                  <button 
                    type="button" 
                    onClick={prevStep}
                    className="border border-slate-300 hover:bg-slate-50 text-slate-700 font-semibold py-2 px-6 rounded-lg text-sm transition"
                  >
                    Back
                  </button>
                  <button 
                    type="button" 
                    onClick={nextStep}
                    disabled={!formData.date || !formData.time}
                    className="bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-2 px-6 rounded-lg text-sm shadow-sm transition disabled:opacity-50"
                  >
                    Next Step
                  </button>
                </div>
              </div>
            )}

            {/* Step 3: Confirm Booking */}
            {step === 3 && (
              <div className="space-y-6 animate-slide-in">
                <div className="space-y-4 bg-slate-50 p-6 rounded-xl border border-slate-100">
                  <h3 className="font-bold text-slate-800 border-b border-slate-200 pb-2 text-base">Booking Summary</h3>
                  <div className="grid grid-cols-2 gap-4 text-sm">
                    <div>
                      <span className="text-slate-400 text-xs block uppercase">Pet</span>
                      <span className="font-semibold text-slate-700">{formData.petId === '1' ? 'Milo' : 'Bella'}</span>
                    </div>
                    <div>
                      <span className="text-slate-400 text-xs block uppercase">Veterinarian</span>
                      <span className="font-semibold text-slate-700">{formData.vetId === '1' ? 'Dr. John Doe' : 'Dr. Sarah Conner'}</span>
                    </div>
                    <div>
                      <span className="text-slate-400 text-xs block uppercase">Date</span>
                      <span className="font-semibold text-slate-700">{formData.date}</span>
                    </div>
                    <div>
                      <span className="text-slate-400 text-xs block uppercase">Time</span>
                      <span className="font-semibold text-slate-700">{formData.time}</span>
                    </div>
                  </div>
                </div>

                <div>
                  <label className="block text-sm font-semibold text-slate-700 mb-2 flex items-center gap-2">
                    <FileText className="h-4 w-4 text-slate-400" /> Reason for Visit
                  </label>
                  <textarea 
                    value={formData.reason}
                    onChange={(e) => setFormData({ ...formData, reason: e.target.value })}
                    rows={3}
                    className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm outline-none focus:ring-2 focus:ring-indigo-500"
                    placeholder="Briefly describe the symptoms or reason for scheduling..."
                    required
                  />
                </div>

                <div className="flex justify-between pt-4">
                  <button 
                    type="button" 
                    onClick={prevStep}
                    className="border border-slate-300 hover:bg-slate-50 text-slate-700 font-semibold py-2 px-6 rounded-lg text-sm transition"
                  >
                    Back
                  </button>
                  <button 
                    type="submit"
                    className="bg-indigo-600 hover:bg-indigo-700 text-white font-semibold py-2 px-6 rounded-lg text-sm shadow-sm transition"
                  >
                    Confirm Booking
                  </button>
                </div>
              </div>
            )}

          </form>
        </div>
      </div>
    </DashboardLayout>
  );
};
