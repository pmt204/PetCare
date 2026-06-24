import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { LogOut, User, Menu, X } from 'lucide-react';

const DashboardLayout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [mobileMenuOpen, setMobileMenuOpen] = React.useState(false);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col font-sans">
      {/* Navbar */}
      <header className="bg-white border-b border-slate-200 sticky top-0 z-40">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16 items-center">
            <div className="flex items-center space-x-3">
              <span className="text-2xl">🐾</span>
              <Link to="/" className="text-xl font-bold text-slate-900 tracking-tight">PetCare Clinic</Link>
            </div>
            
            <nav className="hidden md:flex space-x-6 items-center">
              {user?.role === 'OWNER' && (
                <>
                  <Link to="/owner/pets" className="text-sm font-semibold text-slate-600 hover:text-indigo-600 transition">My Pets</Link>
                  <Link to="/owner/book" className="text-sm font-semibold text-slate-600 hover:text-indigo-600 transition">Book Appointment</Link>
                  <Link to="/owner/history" className="text-sm font-semibold text-slate-600 hover:text-indigo-600 transition">Medical History</Link>
                </>
              )}
              {user?.role === 'VET' && (
                <>
                  <Link to="/vet/schedule" className="text-sm font-semibold text-slate-600 hover:text-indigo-600 transition">My Schedule</Link>
                  <Link to="/vet/records" className="text-sm font-semibold text-slate-600 hover:text-indigo-600 transition">Add Record</Link>
                </>
              )}
              {user?.role === 'ADMIN' && (
                <>
                  <Link to="/admin/vets" className="text-sm font-semibold text-slate-600 hover:text-indigo-600 transition">Vets</Link>
                  <Link to="/admin/invoices" className="text-sm font-semibold text-slate-600 hover:text-indigo-600 transition">Invoices</Link>
                  <Link to="/admin/reports" className="text-sm font-semibold text-slate-600 hover:text-indigo-600 transition">Workload Reports</Link>
                </>
              )}
            </nav>

            <div className="hidden md:flex items-center space-x-4">
              <div className="flex items-center space-x-2 text-sm text-slate-700 bg-slate-100 py-1.5 px-3 rounded-full font-medium">
                <User className="h-4 w-4 text-slate-500" />
                <span>{user?.fullName} ({user?.role})</span>
              </div>
              <button 
                onClick={handleLogout}
                className="text-slate-500 hover:text-red-600 p-2 rounded-full hover:bg-red-50 transition"
                title="Log out"
              >
                <LogOut className="h-5 w-5" />
              </button>
            </div>

            {/* Mobile menu button */}
            <div className="md:hidden">
              <button 
                onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
                className="text-slate-600 p-2 hover:bg-slate-100 rounded-md"
              >
                {mobileMenuOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
              </button>
            </div>
          </div>
        </div>

        {/* Mobile menu */}
        {mobileMenuOpen && (
          <div className="md:hidden border-b border-slate-200 bg-white px-2 pt-2 pb-3 space-y-1 sm:px-3">
            {user?.role === 'OWNER' && (
              <>
                <Link to="/owner/pets" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-semibold text-slate-600 hover:bg-slate-50 hover:text-indigo-600">My Pets</Link>
                <Link to="/owner/book" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-semibold text-slate-600 hover:bg-slate-50 hover:text-indigo-600">Book Appointment</Link>
                <Link to="/owner/history" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-semibold text-slate-600 hover:bg-slate-50 hover:text-indigo-600">Medical History</Link>
              </>
            )}
            {user?.role === 'VET' && (
              <>
                <Link to="/vet/schedule" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-semibold text-slate-600 hover:bg-slate-50 hover:text-indigo-600">My Schedule</Link>
                <Link to="/vet/records" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-semibold text-slate-600 hover:bg-slate-50 hover:text-indigo-600">Add Record</Link>
              </>
            )}
            {user?.role === 'ADMIN' && (
              <>
                <Link to="/admin/vets" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-semibold text-slate-600 hover:bg-slate-50 hover:text-indigo-600">Vets</Link>
                <Link to="/admin/invoices" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-semibold text-slate-600 hover:bg-slate-50 hover:text-indigo-600">Invoices</Link>
                <Link to="/admin/reports" onClick={() => setMobileMenuOpen(false)} className="block px-3 py-2 rounded-md text-base font-semibold text-slate-600 hover:bg-slate-50 hover:text-indigo-600">Workload Reports</Link>
              </>
            )}
            <div className="pt-4 pb-2 border-t border-slate-100 flex items-center justify-between px-3">
              <span className="text-sm font-medium text-slate-600">{user?.fullName}</span>
              <button 
                onClick={handleLogout}
                className="flex items-center space-x-2 text-sm text-red-600 font-semibold hover:bg-red-50 py-1.5 px-3 rounded-md transition"
              >
                <LogOut className="h-4 w-4" />
                <span>Log out</span>
              </button>
            </div>
          </div>
        )}
      </header>

      {/* Main Content Area */}
      <main className="flex-grow max-w-7xl mx-auto w-full px-4 sm:px-6 lg:px-8 py-8">
        {children}
      </main>

      {/* Footer */}
      <footer className="bg-white border-t border-slate-200 py-6 text-center text-sm text-slate-500">
        © 2026 PetCare Management System. All rights reserved.
      </footer>
    </div>
  );
};

export default DashboardLayout;
