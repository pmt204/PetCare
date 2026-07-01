import React from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { LogOut, Menu, X, Shield, Calendar, FileText, Pill, ArrowLeft } from 'lucide-react';

const VetLayout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [mobileSidebarOpen, setMobileSidebarOpen] = React.useState(false);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const isActive = (path: string) => {
    return location.pathname === path;
  };

  const sidebarLinkClass = (path: string) => {
    return `flex items-center space-x-3 px-4 py-3 rounded-xl text-sm font-semibold transition-all duration-200 ${
      isActive(path)
        ? 'bg-teal-650 text-white shadow-md shadow-teal-900/20'
        : 'text-slate-400 hover:bg-slate-800 hover:text-white'
    }`;
  };

  return (
    <div className="min-h-screen bg-slate-100 flex font-sans">
      
      {/* Sidebar for Desktop */}
      <aside className="hidden md:flex flex-col w-64 bg-slate-900 text-white p-5 border-r border-slate-850 shrink-0">
        
        {/* Sidebar Header */}
        <div className="flex items-center space-x-3 pb-6 border-b border-slate-800 mb-6">
          <div className="bg-teal-600 p-2 rounded-xl text-white shadow-inner">
            <Shield className="h-6 w-6" />
          </div>
          <div>
            <h1 className="font-extrabold text-base tracking-tight text-white">PetCare VET</h1>
            <span className="text-[10px] text-teal-400 font-bold uppercase tracking-wider">Vet Portal</span>
          </div>
        </div>

        {/* Sidebar Nav */}
        <nav className="flex-grow space-y-1">
          <Link to="/vet/schedule" className={sidebarLinkClass('/vet/schedule')}>
            <Calendar className="h-4.5 w-4.5" />
            <span>Lịch hẹn của tôi</span>
          </Link>

          <Link to="/vet/records/create" className={sidebarLinkClass('/vet/records/create')}>
            <FileText className="h-4.5 w-4.5" />
            <span>Tạo bệnh án mới</span>
          </Link>

          <Link to="/vet/prescriptions" className={sidebarLinkClass('/vet/prescriptions')}>
            <Pill className="h-4.5 w-4.5" />
            <span>Kê đơn & Quản lý</span>
          </Link>
        </nav>

        {/* Sidebar Footer actions */}
        <div className="pt-6 border-t border-slate-800 space-y-2">
          <Link to="/" className="flex items-center space-x-3 px-4 py-2.5 rounded-xl text-xs font-bold text-slate-400 hover:text-white transition">
            <ArrowLeft className="h-4 w-4" />
            <span>Về Trang chủ</span>
          </Link>
          <button 
            onClick={handleLogout}
            className="w-full flex items-center space-x-3 px-4 py-2.5 rounded-xl text-xs font-bold text-red-400 hover:bg-red-950/30 hover:text-red-300 transition text-left"
          >
            <LogOut className="h-4 w-4" />
            <span>Đăng xuất</span>
          </button>
        </div>
      </aside>

      {/* Main Container */}
      <div className="flex-grow flex flex-col min-w-0">
        
        {/* Top Navbar */}
        <header className="bg-white border-b border-slate-200 h-16 px-6 flex items-center justify-between shrink-0 shadow-xs">
          
          <div className="flex items-center space-x-4">
            <button 
              onClick={() => setMobileSidebarOpen(true)}
              className="md:hidden p-2 text-slate-500 hover:bg-slate-100 rounded-xl transition"
            >
              <Menu className="h-5 w-5" />
            </button>
            
            <h2 className="text-lg font-extrabold text-slate-850 hidden sm:block">
              {isActive('/vet/schedule') && 'Lịch hẹn của tôi hôm nay'}
              {isActive('/vet/records/create') && 'Tạo Bệnh án Lâm sàng'}
              {isActive('/vet/prescriptions') && 'Quản lý Đơn thuốc Bác sĩ'}
            </h2>
          </div>

          {/* User info */}
          <div className="flex items-center space-x-3.5">
            <div className="text-right hidden sm:block">
              <span className="block text-sm font-bold text-slate-800">{user?.fullName}</span>
              <span className="block text-[10px] text-teal-600 font-bold uppercase tracking-wider">Bác sĩ Thú y</span>
            </div>
            <div className="h-9 w-9 rounded-xl bg-teal-50 border border-teal-150 flex items-center justify-center text-teal-700 font-extrabold text-sm shadow-xs">
              {user?.fullName?.charAt(0) || 'V'}
            </div>
          </div>
        </header>

        {/* Dynamic Page Content */}
        <main className="flex-grow p-6 overflow-y-auto max-w-7xl w-full mx-auto">
          {children}
        </main>
      </div>

      {/* Mobile Sidebar Overlay */}
      {mobileSidebarOpen && (
        <div className="fixed inset-0 z-50 flex md:hidden">
          {/* Backdrop */}
          <div 
            className="absolute inset-0 bg-slate-900/60 backdrop-blur-xs transition-opacity" 
            onClick={() => setMobileSidebarOpen(false)}
          />
          
          {/* Drawer Menu */}
          <div className="relative w-64 max-w-xs bg-slate-900 text-white p-5 flex flex-col z-10 animate-fade-in-right">
            
            <div className="flex items-center justify-between pb-6 border-b border-slate-800 mb-6">
              <div className="flex items-center space-x-3">
                <div className="bg-teal-600 p-2 rounded-xl text-white">
                  <Shield className="h-5 w-5" />
                </div>
                <span className="font-extrabold text-sm">PetCare VET</span>
              </div>
              <button 
                onClick={() => setMobileSidebarOpen(false)}
                className="p-1.5 hover:bg-slate-800 rounded-lg text-slate-400 hover:text-white transition"
              >
                <X className="h-4 w-4" />
              </button>
            </div>

            <nav className="flex-grow space-y-1">
              <Link 
                to="/vet/schedule" 
                onClick={() => setMobileSidebarOpen(false)} 
                className={sidebarLinkClass('/vet/schedule')}
              >
                <Calendar className="h-4.5 w-4.5" />
                <span>Lịch hẹn của tôi</span>
              </Link>

              <Link 
                to="/vet/records/create" 
                onClick={() => setMobileSidebarOpen(false)} 
                className={sidebarLinkClass('/vet/records/create')}
              >
                <FileText className="h-4.5 w-4.5" />
                <span>Tạo bệnh án mới</span>
              </Link>

              <Link 
                to="/vet/prescriptions" 
                onClick={() => setMobileSidebarOpen(false)} 
                className={sidebarLinkClass('/vet/prescriptions')}
              >
                <Pill className="h-4.5 w-4.5" />
                <span>Kê đơn & Quản lý</span>
              </Link>
            </nav>

            <div className="pt-6 border-t border-slate-800 space-y-2">
              <Link 
                to="/" 
                onClick={() => setMobileSidebarOpen(false)} 
                className="flex items-center space-x-3 px-4 py-2.5 rounded-xl text-xs font-bold text-slate-400 hover:text-white transition"
              >
                <ArrowLeft className="h-4 w-4" />
                <span>Về Trang chủ</span>
              </Link>
              <button 
                onClick={() => {
                  setMobileSidebarOpen(false);
                  handleLogout();
                }}
                className="w-full flex items-center space-x-3 px-4 py-2.5 rounded-xl text-xs font-bold text-red-400 hover:bg-red-950/30 hover:text-red-300 transition text-left"
              >
                <LogOut className="h-4 w-4" />
                <span>Đăng xuất</span>
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default VetLayout;
