import React from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { LogOut, Menu, X, Shield, Users, FileText, BarChart2, ArrowLeft } from 'lucide-react';

const AdminLayout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [mobileSidebarOpen, setMobileSidebarOpen] = React.useState(false);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const isActive = (path: string) => location.pathname === path;

  const sidebarLinkClass = (path: string) =>
    `flex items-center space-x-3 px-4 py-3 rounded-xl text-sm font-bold transition-all duration-200 ${
      isActive(path)
        ? 'bg-teal-600 text-white shadow-md shadow-teal-500/10'
        : 'text-slate-300 hover:bg-slate-800 hover:text-white'
    }`;

  return (
    <div className="min-h-screen bg-slate-100 flex font-sans">
      
      {/* Desktop Sidebar */}
      <aside className="hidden lg:flex lg:flex-col lg:w-64 bg-slate-900 text-slate-400 p-6 space-y-6 border-r border-slate-800 flex-shrink-0">
        {/* Brand Header */}
        <div className="flex items-center space-x-3 pb-6 border-b border-slate-800">
          <span className="text-2xl">🛡️</span>
          <div>
            <h1 className="text-lg font-black tracking-wider text-white uppercase m-0 leading-tight">PetCare</h1>
            <span className="text-[10px] font-bold text-teal-400 uppercase tracking-widest block mt-0.5">Admin DashBoard</span>
          </div>
        </div>

        {/* Sidebar Nav */}
        <nav className="flex-grow space-y-1">
          <Link to="/admin/vets" className={sidebarLinkClass('/admin/vets')}>
            <Users className="h-4.5 w-4.5" />
            <span>Quản lý Bác sĩ</span>
          </Link>

          <Link to="/admin/invoices" className={sidebarLinkClass('/admin/invoices')}>
            <FileText className="h-4.5 w-4.5" />
            <span>Quản lý Hóa đơn</span>
          </Link>

          <Link to="/admin/reports" className={sidebarLinkClass('/admin/reports')}>
            <BarChart2 className="h-4.5 w-4.5" />
            <span>Báo cáo Thống kê</span>
          </Link>
        </nav>

        {/* Sidebar Footer actions */}
        <div className="pt-6 border-t border-slate-800 space-y-2">
          <Link 
            to="/" 
            className="flex items-center space-x-3 px-4 py-2.5 rounded-xl text-sm font-bold text-slate-400 hover:bg-slate-800 hover:text-white transition"
          >
            <ArrowLeft className="h-4 w-4" />
            <span>Xem trang ngoài</span>
          </Link>

          <button
            onClick={handleLogout}
            className="w-full flex items-center space-x-3 px-4 py-2.5 rounded-xl text-sm font-bold text-red-400 hover:bg-red-950/20 hover:text-red-300 transition"
          >
            <LogOut className="h-4 w-4" />
            <span>Đăng xuất</span>
          </button>
        </div>
      </aside>

      {/* Main Content panel */}
      <div className="flex-grow flex flex-col min-w-0">
        
        {/* Top Header bar */}
        <header className="bg-white border-b border-slate-200/80 h-16 flex items-center justify-between px-6 sticky top-0 z-30 shadow-sm">
          {/* Left panel */}
          <div className="flex items-center space-x-4">
            {/* Mobile Menu trigger */}
            <button
              onClick={() => setMobileSidebarOpen(true)}
              className="lg:hidden p-2 hover:bg-slate-100 rounded-lg text-slate-600 transition"
            >
              <Menu className="h-6 w-6" />
            </button>
            <h2 className="text-lg font-extrabold text-slate-800 hidden sm:block">
              {isActive('/admin/vets') && 'Danh sách Bác sĩ Thú y'}
              {isActive('/admin/invoices') && 'Quản lý Hóa đơn Phòng khám'}
              {isActive('/admin/reports') && 'Báo cáo & Thống kê Công việc'}
            </h2>
          </div>

          {/* Right panel (User Profile) */}
          <div className="flex items-center space-x-4">
            <div className="flex items-center space-x-2 text-xs sm:text-sm font-semibold bg-slate-100 py-1.5 px-3 rounded-full text-slate-700 border border-slate-200">
              <Shield className="h-4 w-4 text-teal-600" />
              <span>{user?.fullName} ({user?.role})</span>
            </div>
          </div>
        </header>

        {/* Content body wrapper */}
        <main className="flex-grow p-6 sm:p-8 overflow-y-auto max-w-7xl w-full mx-auto">
          {children}
        </main>
      </div>

      {/* Mobile Drawer Sidebar Menu */}
      {mobileSidebarOpen && (
        <div className="fixed inset-0 z-50 lg:hidden flex">
          {/* Overlay backdrop */}
          <div 
            className="fixed inset-0 bg-slate-900/60 backdrop-blur-xs" 
            onClick={() => setMobileSidebarOpen(false)}
          />

          {/* Drawer Sidebar Panel */}
          <div className="relative flex flex-col w-64 max-w-xs bg-slate-900 text-slate-400 p-6 space-y-6 animate-slide-in-right z-10 h-full">
            <div className="flex justify-between items-center pb-6 border-b border-slate-800">
              <div className="flex items-center space-x-3">
                <span className="text-2xl">🛡️</span>
                <span className="text-base font-black text-white uppercase tracking-wider">PetCare Admin</span>
              </div>
              <button 
                onClick={() => setMobileSidebarOpen(false)}
                className="text-slate-400 hover:text-white p-1 rounded-md"
              >
                <X className="h-6 w-6" />
              </button>
            </div>

            <nav className="flex-grow space-y-1">
              <Link 
                to="/admin/vets" 
                onClick={() => setMobileSidebarOpen(false)} 
                className={sidebarLinkClass('/admin/vets')}
              >
                <Users className="h-4.5 w-4.5" />
                <span>Quản lý Bác sĩ</span>
              </Link>

              <Link 
                to="/admin/invoices" 
                onClick={() => setMobileSidebarOpen(false)} 
                className={sidebarLinkClass('/admin/invoices')}
              >
                <FileText className="h-4.5 w-4.5" />
                <span>Quản lý Hóa đơn</span>
              </Link>

              <Link 
                to="/admin/reports" 
                onClick={() => setMobileSidebarOpen(false)} 
                className={sidebarLinkClass('/admin/reports')}
              >
                <BarChart2 className="h-4.5 w-4.5" />
                <span>Báo cáo Thống kê</span>
              </Link>
            </nav>

            <div className="pt-6 border-t border-slate-800 space-y-2">
              <Link 
                to="/" 
                onClick={() => setMobileSidebarOpen(false)} 
                className="flex items-center space-x-3 px-4 py-2.5 rounded-xl text-sm font-bold text-slate-400 hover:bg-slate-800 hover:text-white"
              >
                <ArrowLeft className="h-4 w-4" />
                <span>Xem trang ngoài</span>
              </Link>

              <button
                onClick={handleLogout}
                className="w-full flex items-center space-x-3 px-4 py-2.5 rounded-xl text-sm font-bold text-red-400 hover:bg-red-950/20 hover:text-red-300"
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

export default AdminLayout;
