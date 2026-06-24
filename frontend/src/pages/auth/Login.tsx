import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useForm } from 'react-hook-form';

export const Login: React.FC = () => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const { register, handleSubmit, formState: { errors } } = useForm();
  const [errorMsg] = React.useState('');

  const onSubmit = (data: any) => {
    if (data.username === 'admin') {
      login('mock-jwt-token-admin', { id: 1, username: 'admin', email: 'admin@petcare.com', fullName: 'Administrator', role: 'ADMIN' });
      navigate('/admin/vets');
    } else if (data.username === 'vet' || data.username === 'doctor') {
      login('mock-jwt-token-vet', { id: 2, username: 'vet', email: 'vet@petcare.com', fullName: 'Dr. John Doe', role: 'VET' });
      navigate('/vet/schedule');
    } else {
      login('mock-jwt-token-owner', { id: 3, username: data.username || 'owner', email: 'owner@petcare.com', fullName: 'Pet Owner', role: 'OWNER' });
      navigate('/owner/pets');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-100 py-12 px-4 sm:px-6 lg:px-8 font-sans">
      <div className="max-w-md w-full space-y-8 bg-white p-8 border border-slate-200 rounded-2xl shadow-sm">
        <div>
          <div className="text-center text-4xl mb-4">🐾</div>
          <h2 className="text-center text-3xl font-extrabold text-slate-900">Welcome to PetCare</h2>
          <p className="mt-2 text-center text-sm text-slate-500">
            Log in to manage appointments, medical records and invoicing
          </p>
        </div>
        <form className="mt-8 space-y-6" onSubmit={handleSubmit(onSubmit)}>
          {errorMsg && (
            <div className="bg-red-50 text-red-600 p-3 rounded-lg text-sm border border-red-200">{errorMsg}</div>
          )}
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-semibold text-slate-700 mb-1">Username / Email</label>
              <input
                {...register('username', { required: 'Username is required' })}
                type="text"
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none transition"
                placeholder="Enter 'admin', 'vet', or any name for owner"
              />
              {errors.username && <span className="text-xs text-red-500 mt-1 block">{errors.username.message as string}</span>}
            </div>
            <div>
              <label className="block text-sm font-semibold text-slate-700 mb-1">Password</label>
              <input
                {...register('password', { required: 'Password is required' })}
                type="password"
                className="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none transition"
                placeholder="••••••••"
              />
              {errors.password && <span className="text-xs text-red-500 mt-1 block">{errors.password.message as string}</span>}
            </div>
          </div>

          <div>
            <button
              type="submit"
              className="w-full flex justify-center py-2.5 px-4 border border-transparent rounded-lg shadow-sm text-sm font-semibold text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition"
            >
              Sign in
            </button>
          </div>
        </form>
        
        <div className="text-center pt-4 border-t border-slate-100 text-xs text-slate-400 space-y-1">
          <p>💡 Tip: Use username "admin" to log in as Admin,</p>
          <p>"vet" as Veterinarian, or any other name as Owner.</p>
        </div>
      </div>
    </div>
  );
};
