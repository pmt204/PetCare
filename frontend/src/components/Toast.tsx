import React from 'react';
import { createRoot } from 'react-dom/client';

export type ToastType = 'success' | 'error' | 'info' | 'warning';

interface ToastProps {
  message: string;
  type?: ToastType;
  duration?: number;
  onClose: () => void;
}

export const Toast: React.FC<ToastProps> = ({ message, type = 'success', duration = 4000, onClose }) => {
  React.useEffect(() => {
    const timer = setTimeout(onClose, duration);
    return () => clearTimeout(timer);
  }, [duration, onClose]);

  const bgClass = 
    type === 'success' ? 'bg-emerald-50 border-emerald-200 text-emerald-800 shadow-emerald-100/30' :
    type === 'error' ? 'bg-rose-50 border-rose-200 text-rose-800 shadow-rose-100/30' :
    type === 'warning' ? 'bg-amber-50 border-amber-200 text-amber-800 shadow-amber-100/30' :
    'bg-sky-50 border-sky-200 text-sky-800 shadow-sky-100/30';

  const icon = 
    type === 'success' ? '🎉' :
    type === 'error' ? '❌' :
    type === 'warning' ? '⚠️' :
    'ℹ️';

  return (
    <div className={`fixed bottom-5 right-5 z-[9999] flex items-start space-x-3 p-4 border rounded-2xl shadow-xl max-w-sm font-sans ${bgClass} animate-toast-in`}>
      <div className="text-lg flex-shrink-0 mt-0.5">{icon}</div>
      <div className="flex-1 min-w-0">
        <div className="text-xs font-bold whitespace-pre-line leading-relaxed">{message}</div>
      </div>
    </div>
  );
};

export const showToast = (message: string, type: ToastType = 'success', duration = 4000) => {
  const container = document.createElement('div');
  document.body.appendChild(container);
  
  const root = createRoot(container);
  const handleClose = () => {
    root.unmount();
    container.remove();
  };
  
  root.render(<Toast message={message} type={type} duration={duration} onClose={handleClose} />);
};

export default showToast;
