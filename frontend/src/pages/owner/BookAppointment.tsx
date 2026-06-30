import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';

export const BookAppointment: React.FC = () => {
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const vetId = searchParams.get('vetId');
  
  const state = location.state as { from?: string } | null;
  const fromPath = state?.from || '/owner/pets';
  
  const redirectUrl = vetId ? `${fromPath}?book=true&vetId=${vetId}` : `${fromPath}?book=true`;

  return <Navigate to={redirectUrl} replace />;
};

export default BookAppointment;
