import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import ProtectedRoute from './components/common/ProtectedRoute';
import { Login } from './pages/auth/Login';
import { Pets } from './pages/owner/Pets';
import { BookAppointment } from './pages/owner/BookAppointment';
import { MedicalHistory } from './pages/owner/MedicalHistory';
import { Schedule } from './pages/vet/Schedule';
import { Vets } from './pages/admin/Vets';
import './App.css';

const queryClient = new QueryClient();

function AppRoutes() {
  const { user } = useAuth();

  return (
    <Routes>
      {/* Public Routes */}
      <Route path="/login" element={<Login />} />
      <Route path="/unauthorized" element={<div className="flex h-screen items-center justify-center text-slate-800 font-bold">Unauthorized Access</div>} />

      {/* Protected Routes for Owner */}
      <Route element={<ProtectedRoute allowedRoles={['OWNER']} />}>
        <Route path="/owner/pets" element={<Pets />} />
        <Route path="/owner/book" element={<BookAppointment />} />
        <Route path="/owner/history" element={<MedicalHistory />} />
      </Route>

      {/* Protected Routes for Vet */}
      <Route element={<ProtectedRoute allowedRoles={['VET']} />}>
        <Route path="/vet/schedule" element={<Schedule />} />
      </Route>

      {/* Protected Routes for Admin */}
      <Route element={<ProtectedRoute allowedRoles={['ADMIN']} />}>
        <Route path="/admin/vets" element={<Vets />} />
      </Route>

      {/* Fallback routing */}
      <Route 
        path="*" 
        element={
          user ? (
            user.role === 'ADMIN' ? (
              <Navigate to="/admin/vets" replace />
            ) : user.role === 'VET' ? (
              <Navigate to="/vet/schedule" replace />
            ) : (
              <Navigate to="/owner/pets" replace />
            )
          ) : (
            <Navigate to="/login" replace />
          )
        } 
      />
    </Routes>
  );
}

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <BrowserRouter>
          <AppRoutes />
        </BrowserRouter>
      </AuthProvider>
    </QueryClientProvider>
  );
}

export default App;
