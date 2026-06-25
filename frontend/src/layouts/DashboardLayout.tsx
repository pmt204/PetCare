import React from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { LogOut, User, Menu, X, ChevronDown, Calendar, History, Shield, LogIn } from 'lucide-react';

const DashboardLayout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [mobileMenuOpen, setMobileMenuOpen] = React.useState(false);
  const [profileDropdownOpen, setProfileDropdownOpen] = React.useState(false);

  const handleLogout = () => {
    logout();
    setProfileDropdownOpen(false);
    setMobileMenuOpen(false);
    navigate('/login');
  };

  const isActive = (path: string) => location.pathname === path;

  const navLinkClass = (path: string) => 
    `text-sm font-semibold transition-colors duration-200 py-2 px-1 border-b-2 ${
      isActive(path) 
        ? 'border-teal-500 text-teal-600' 
        : 'border-transparent text-slate-600 hover:text-teal-600 hover:border-teal-200'
    }`;

  const mobileNavLinkClass = (path: string) =>
    `block px-3 py-2 rounded-md text-base font-semibold transition-colors duration-200 ${
      isActive(path)
        ? 'bg-teal-50 text-teal-700'
        : 'text-slate-600 hover:bg-slate-50 hover:text-teal-600'
    }`;

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col font-sans">
      {/* Fixed Sticky Header Navbar */}
      <header className="bg-white/95 backdrop-blur-md border-b border-slate-200/80 sticky top-0 z-50 shadow-sm transition-all duration-300">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16 items-center">
            
            {/* Logo */}
            <div className="flex items-center space-x-3">
              <Link to="/" className="flex items-center space-x-2 group">
                <span className="text-2xl transform group-hover:scale-110 transition duration-300">🐾</span>
                <span className="text-2xl font-extrabold tracking-tight bg-gradient-to-r from-teal-600 to-teal-400 bg-clip-text text-transparent">
                  PetCare
                </span>
              </Link>
            </div>
            
            {/* Desktop Navigation */}
            <nav className="hidden md:flex space-x-8 items-center">
              <Link to="/" className={navLinkClass('/')}>Trang chủ</Link>
              <Link to="/about" className={navLinkClass('/about')}>Giới thiệu</Link>
              <Link to="/services" className={navLinkClass('/services')}>Dịch vụ</Link>
              <Link to="/vets" className={navLinkClass('/vets')}>Bác sĩ</Link>
              
              {/* Contextual links based on login state and role */}
              {user?.role === 'OWNER' && (
                <>
                  <Link to="/owner/book" className={navLinkClass('/owner/book')}>Đặt lịch</Link>
                  <Link to="/owner/pets" className={navLinkClass('/owner/pets')}>Thú cưng</Link>
                  <Link to="/owner/history" className={navLinkClass('/owner/history')}>Lịch sử khám</Link>
                </>
              )}
              {user?.role === 'VET' && (
                <>
                  <Link to="/vet/schedule" className={navLinkClass('/vet/schedule')}>Lịch hẹn bác sĩ</Link>
                  <Link to="/vet/records" className={navLinkClass('/vet/records')}>Tạo bệnh án</Link>
                </>
              )}


              {/* General booking redirection for guests */}
              {!user && (
                <Link to="/login" className={navLinkClass('/login')}>Đặt lịch</Link>
              )}
            </nav>

            {/* Desktop Right Panel (User / Login) */}
            <div className="hidden md:flex items-center space-x-4">
              {user ? (
                <div className="relative">
                  <button 
                    onClick={() => setProfileDropdownOpen(!profileDropdownOpen)}
                    onBlur={() => setTimeout(() => setProfileDropdownOpen(false), 200)}
                    className="flex items-center space-x-2 text-sm text-slate-700 bg-slate-50 hover:bg-slate-100 border border-slate-200 py-1.5 px-3 rounded-full font-medium transition duration-200 focus:outline-none"
                  >
                    <div className="h-6 w-6 rounded-full overflow-hidden bg-teal-500 text-white flex items-center justify-center text-xs font-bold shadow-inner">
                      {user.avatarUrl ? (
                        <img src={user.avatarUrl} alt={user.fullName} className="h-full w-full object-cover" />
                      ) : (
                        user.fullName.charAt(0).toUpperCase()
                      )}
                    </div>
                    <span>{user.fullName}</span>
                    <ChevronDown className={`h-4 w-4 text-slate-500 transition-transform duration-200 ${profileDropdownOpen ? 'transform rotate-180' : ''}`} />
                  </button>

                  {/* Dropdown Menu */}
                  {profileDropdownOpen && (
                    <div className="absolute right-0 mt-2 w-56 rounded-xl bg-white border border-slate-200 shadow-xl py-2 z-50 animate-fade-in-down">
                      <div className="px-4 py-2 border-b border-slate-100">
                        <p className="text-xs text-slate-400 font-medium uppercase tracking-wider">Tài khoản ({user.role})</p>
                        <p className="text-sm font-semibold text-slate-800 truncate">{user.email}</p>
                      </div>
                      
                      {user.role === 'OWNER' && (
                        <>
                          <Link 
                            to="/owner/pets" 
                            className="flex items-center space-x-2 px-4 py-2.5 text-sm text-slate-700 hover:bg-slate-50 hover:text-teal-600 transition"
                          >
                            <User className="h-4 w-4 text-slate-400" />
                            <span>Hồ sơ & Thú cưng</span>
                          </Link>
                          <Link 
                            to="/owner/history" 
                            className="flex items-center space-x-2 px-4 py-2.5 text-sm text-slate-700 hover:bg-slate-50 hover:text-teal-600 transition"
                          >
                            <History className="h-4 w-4 text-slate-400" />
                            <span>Lịch sử khám</span>
                          </Link>
                        </>
                      )}
                      
                      {user.role === 'VET' && (
                        <Link 
                          to="/vet/schedule" 
                          className="flex items-center space-x-2 px-4 py-2.5 text-sm text-slate-700 hover:bg-slate-50 hover:text-teal-600 transition"
                        >
                          <Calendar className="h-4 w-4 text-slate-400" />
                          <span>Lịch làm việc</span>
                        </Link>
                      )}

                      {user.role === 'ADMIN' && (
                        <Link 
                          to="/admin/vets" 
                          className="flex items-center space-x-2 px-4 py-2.5 text-sm text-slate-700 hover:bg-slate-50 hover:text-teal-600 transition"
                        >
                          <Shield className="h-4 w-4 text-slate-400" />
                          <span>Quản trị viên</span>
                        </Link>
                      )}

                      <div className="border-t border-slate-100 my-1"></div>
                      <button 
                        onClick={handleLogout}
                        className="w-full flex items-center space-x-2 px-4 py-2.5 text-sm text-red-600 hover:bg-red-50 transition text-left"
                      >
                        <LogOut className="h-4 w-4" />
                        <span>Đăng xuất</span>
                      </button>
                    </div>
                  )}
                </div>
              ) : (
                <Link 
                  to="/login"
                  className="flex items-center space-x-2 bg-gradient-to-r from-teal-600 to-teal-500 hover:from-teal-700 hover:to-teal-600 text-white text-sm font-semibold py-2 px-5 rounded-full shadow-md hover:shadow-lg transform hover:-translate-y-0.5 transition duration-200"
                >
                  <LogIn className="h-4 w-4" />
                  <span>Đăng nhập</span>
                </Link>
              )}
            </div>

            {/* Mobile menu button */}
            <div className="md:hidden">
              <button 
                onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
                className="text-slate-600 p-2 hover:bg-slate-100 rounded-lg transition"
              >
                {mobileMenuOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
              </button>
            </div>
          </div>
        </div>

        {/* Mobile Navigation Menu */}
        {mobileMenuOpen && (
          <div className="md:hidden border-b border-slate-200 bg-white px-4 pt-2 pb-4 space-y-1 shadow-inner animate-fade-in">
            <Link to="/" onClick={() => setMobileMenuOpen(false)} className={mobileNavLinkClass('/')}>Trang chủ</Link>
            <Link to="/about" onClick={() => setMobileMenuOpen(false)} className={mobileNavLinkClass('/about')}>Giới thiệu</Link>
            <Link to="/services" onClick={() => setMobileMenuOpen(false)} className={mobileNavLinkClass('/services')}>Dịch vụ</Link>
            <Link to="/vets" onClick={() => setMobileMenuOpen(false)} className={mobileNavLinkClass('/vets')}>Bác sĩ</Link>

            {user?.role === 'OWNER' && (
              <>
                <Link to="/owner/book" onClick={() => setMobileMenuOpen(false)} className={mobileNavLinkClass('/owner/book')}>Đặt lịch</Link>
                <Link to="/owner/pets" onClick={() => setMobileMenuOpen(false)} className={mobileNavLinkClass('/owner/pets')}>Thú cưng & Hồ sơ</Link>
                <Link to="/owner/history" onClick={() => setMobileMenuOpen(false)} className={mobileNavLinkClass('/owner/history')}>Lịch sử khám</Link>
              </>
            )}
            {user?.role === 'VET' && (
              <>
                <Link to="/vet/schedule" onClick={() => setMobileMenuOpen(false)} className={mobileNavLinkClass('/vet/schedule')}>Lịch làm việc</Link>
                <Link to="/vet/records" onClick={() => setMobileMenuOpen(false)} className={mobileNavLinkClass('/vet/records')}>Tạo bệnh án</Link>
              </>
            )}


            {!user && (
              <Link to="/login" onClick={() => setMobileMenuOpen(false)} className={mobileNavLinkClass('/login')}>Đặt lịch khám</Link>
            )}

            <div className="pt-4 border-t border-slate-100 mt-4">
              {user ? (
                <div className="flex flex-col space-y-2">
                  <div className="flex items-center space-x-3 px-3 py-1">
                    <div className="h-9 w-9 rounded-full overflow-hidden bg-teal-500 text-white flex items-center justify-center text-sm font-bold shadow-inner">
                      {user.avatarUrl ? (
                        <img src={user.avatarUrl} alt={user.fullName} className="h-full w-full object-cover" />
                      ) : (
                        user.fullName.charAt(0).toUpperCase()
                      )}
                    </div>
                    <div>
                      <p className="text-sm font-semibold text-slate-800">{user.fullName}</p>
                      <p className="text-xs text-slate-500">{user.email}</p>
                    </div>
                  </div>
                  <button 
                    onClick={handleLogout}
                    className="w-full flex items-center space-x-2 px-3 py-2 text-sm text-red-600 hover:bg-red-50 rounded-md transition"
                  >
                    <LogOut className="h-4 w-4" />
                    <span>Đăng xuất</span>
                  </button>
                </div>
              ) : (
                <Link 
                  to="/login"
                  onClick={() => setMobileMenuOpen(false)}
                  className="w-full flex items-center justify-center space-x-2 bg-teal-600 text-white py-2.5 px-4 rounded-xl text-center font-semibold shadow-md hover:bg-teal-700 transition"
                >
                  <LogIn className="h-4 w-4" />
                  <span>Đăng nhập</span>
                </Link>
              )}
            </div>
          </div>
        )}
      </header>

      {/* Main Content Area */}
      <main className="flex-grow w-full">
        {children}
      </main>

      {/* Footer */}
      <footer className="bg-slate-900 border-t border-slate-800 text-slate-400 py-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
            <div className="space-y-4">
              <div className="flex items-center space-x-2">
                <span className="text-2xl">🐾</span>
                <span className="text-xl font-bold text-white">PetCare</span>
              </div>
              <p className="text-sm text-slate-400">
                Hệ thống quản lý đặt lịch khám bệnh và theo dõi hồ sơ sức khỏe thú cưng toàn diện. Đặt lịch nhanh chóng, bác sĩ chuyên môn tận tâm.
              </p>
            </div>
            
            <div>
              <h3 className="text-white font-bold text-sm uppercase tracking-wider mb-4">Dịch Vụ Lâm Sàng</h3>
              <ul className="space-y-2 text-sm">
                <li><Link to="/about" className="hover:text-teal-400 transition">Khám sức khỏe tổng quát</Link></li>
                <li><Link to="/about" className="hover:text-teal-400 transition">Tiêm phòng vaccine</Link></li>
                <li><Link to="/about" className="hover:text-teal-400 transition">Xét nghiệm & Siêu âm</Link></li>
                <li><Link to="/about" className="hover:text-teal-400 transition">Phẫu thuật thú y</Link></li>
              </ul>
            </div>

            <div>
              <h3 className="text-white font-bold text-sm uppercase tracking-wider mb-4">Đường Dẫn Nhanh</h3>
              <ul className="space-y-2 text-sm">
                <li><Link to="/" className="hover:text-teal-400 transition">Trang chủ</Link></li>
                <li><Link to="/about" className="hover:text-teal-400 transition">Giới thiệu</Link></li>
                <li><Link to="/owner/book" className="hover:text-teal-400 transition">Đặt lịch hẹn</Link></li>
                <li><Link to="/login" className="hover:text-teal-400 transition">Đăng nhập</Link></li>
              </ul>
            </div>

            <div>
              <h3 className="text-white font-bold text-sm uppercase tracking-wider mb-4">Liên Hệ Y Tế</h3>
              <ul className="space-y-2 text-sm">
                <li>📍 Số 1 Đại Cồ Việt, Hai Bà Trưng, Hà Nội</li>
                <li>📞 Hotline khám: 1900 8888</li>
                <li>✉️ Email: support@petcare.vn</li>
                <li>🕒 Giờ làm việc: 8:00 - 20:00 (Cả T7 & CN)</li>
              </ul>
            </div>
          </div>
          
          <div className="border-t border-slate-800 mt-12 pt-6 text-center text-xs text-slate-500">
            © 2026 PetCare. Tất cả các quyền được bảo lưu. Chăm sóc tận tâm, thú cưng khỏe mạnh.
          </div>
        </div>
      </footer>
    </div>
  );
};

export default DashboardLayout;
